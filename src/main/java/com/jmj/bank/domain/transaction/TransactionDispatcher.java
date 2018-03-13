package com.jmj.bank.domain.transaction;

public interface TransactionDispatcher {

	void execute(Transaction transaction);
}
