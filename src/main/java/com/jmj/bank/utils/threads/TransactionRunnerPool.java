package com.jmj.bank.utils.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmj.bank.utils.validations.Preconditions;

public class TransactionRunnerPool implements Executor {

	private static final Logger logger = LoggerFactory.getLogger(TransactionRunnerPool.class);

	private final BlockingQueue<Runnable> transactions;
	private final int executeTimeOut;

	public TransactionRunnerPool(int queueSize, int executeTimeOut, int poolSize) {
		transactions = new LinkedBlockingQueue(queueSize);
		this.executeTimeOut = executeTimeOut;
		initThreads(poolSize);
	}

	private void initThreads(int poolSize) {
		for (int count = 0; count < poolSize; count++) {
			String threadName = "TransactionRunner-" + count;
			TransactionRunner transactionRunner = new TransactionRunner(transactions);
			Thread thread = new Thread(transactionRunner, threadName);
			thread.start();
		}
	}

	@Override
	public void execute(Runnable transaction) {
		Preconditions.checkArgumentNotNull(transaction, "Transaction can not be null");
		try {
			if (!transactions.offer(transaction, executeTimeOut, TimeUnit.MILLISECONDS)){
				throw new RejectedExecutionException("Transaction processor exhausted");
			}
		} catch (InterruptedException e) {
			logger.error( "TransactionRunnerPool was interrupted", e);
			Thread.currentThread().interrupt();
		}
	}
}
