package com.jmj.bank.domain.transaction;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmj.bank.domain.transaction.exception.ServiceUnavailableException;

public class AsyncTransactionDispatcher implements TransactionDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(TransactionDispatcher.class);
	private final Executor executor;
	private final TransactionService transactionService;

	public AsyncTransactionDispatcher(Executor executor, TransactionService transactionService) {
		this.executor = executor;
		this.transactionService = transactionService;
	}

	@Override
	public void execute(Transaction transaction) {
		try {
			executor.execute(() -> transactionService.doTransaction(transaction));
		} catch (RejectedExecutionException rejected) {
			logger.error("Transaction can not be dispatched", rejected);
			throw new ServiceUnavailableException("Service unavailable, please retry un some minutes", rejected);
		}
	}

}
