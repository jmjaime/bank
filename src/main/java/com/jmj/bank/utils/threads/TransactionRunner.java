package com.jmj.bank.utils.threads;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionRunner implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(TransactionRunner.class);
	private final BlockingQueue<Runnable> transactions;

	public TransactionRunner(BlockingQueue<Runnable> transactions) {
		this.transactions = transactions;
	}

	@Override
	public void run() {
		final String name = Thread.currentThread().getName();
		try {
			while (true) {
				Runnable task = transactions.take();
				task.run();
			}
		} catch (InterruptedException e) {
			logger.error(name + " was interrupted", e);
			Thread.currentThread().interrupt();
		}
	}
}
