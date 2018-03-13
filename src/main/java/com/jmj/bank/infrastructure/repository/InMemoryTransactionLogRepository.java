package com.jmj.bank.infrastructure.repository;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jmj.bank.domain.transaction.TransactionId;
import com.jmj.bank.domain.transaction.TransactionLog;
import com.jmj.bank.domain.transaction.TransactionLogRepository;

public class InMemoryTransactionLogRepository implements TransactionLogRepository {

	private long lastId = 0L;
	private final List<TransactionLog> transactionsLog = new LinkedList();

	@Override
	public void save(TransactionLog transactionLog) {
		transactionsLog.add(transactionLog);
	}

	@Override
	public ImmutableList<TransactionLog> findAllTransactions() {
		return ImmutableList.copyOf(transactionsLog);
	}

	@Override
	public TransactionId nextId() {
		return new TransactionId(nextIdValue());
	}

	private synchronized Long nextIdValue() {
		lastId++;
		return lastId;
	}
}
