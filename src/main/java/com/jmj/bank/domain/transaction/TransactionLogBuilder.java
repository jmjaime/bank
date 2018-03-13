package com.jmj.bank.domain.transaction;

import com.jmj.bank.domain.account.AccountId;
import com.jmj.bank.domain.money.Money;

public class TransactionLogBuilder {

	private TransactionId id;
	private Transaction transaction;

	public static TransactionLogBuilder builder() {
		return new TransactionLogBuilder();
	}

	public TransactionLogBuilder withId(TransactionId id) {
		this.id = id;
		return this;
	}

	public TransactionLogBuilder withTransaction(Transaction transaction) {
		this.transaction = transaction;
		return this;
	}

	public TransactionLog build() {
		AccountId accountFrom = transaction.getAccountFrom().getId();
		AccountId accountTo = transaction.getAccountTo().getId();
		Money amount = transaction.getAmount();
		Money tax = transaction.calculateTax();
		Long timeStamp = transaction.getTimeStamp().getEpochSecond();
		return new TransactionLog(id, accountFrom, accountTo, amount, tax, timeStamp);
	}

	private TransactionLogBuilder() {

	}

}
