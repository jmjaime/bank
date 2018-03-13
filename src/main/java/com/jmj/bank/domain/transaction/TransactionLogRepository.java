package com.jmj.bank.domain.transaction;

import com.google.common.collect.ImmutableList;

public interface TransactionLogRepository {

	 void save(TransactionLog transactionLog);

	ImmutableList<TransactionLog> findAllTransactions();

	TransactionId nextId();
}
