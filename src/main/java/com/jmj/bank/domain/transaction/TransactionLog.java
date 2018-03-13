package com.jmj.bank.domain.transaction;

import com.jmj.bank.domain.account.AccountId;
import com.jmj.bank.domain.money.Money;
import com.jmj.bank.utils.validations.Preconditions;

public class TransactionLog {

	private final TransactionId id;
	private final AccountId accountFrom;
	private final AccountId accountTo;
	private final Money amount;
	private final Money tax;
	private final Long timeStamp;

	public TransactionLog(TransactionId id, AccountId accountFrom, AccountId accountTo, Money amount, Money tax, Long timeStamp) {
		this.id = id;
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amount = amount;
		this.tax = tax;
		this.timeStamp = timeStamp;
		validate();
	}

	public TransactionId getId() {
		return id;
	}

	public AccountId getAccountFrom() {
		return accountFrom;
	}

	public AccountId getAccountTo() {
		return accountTo;
	}

	public Money getTax() {
		return tax;
	}

	public Money getAmount() {
		return amount;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	private void validate() {
		Preconditions.checkArgumentNotNull(id, "TransactionLog from can not be null");
		Preconditions.checkArgumentNotNull(accountFrom, "TransactionLog from can not be null");
		Preconditions.checkArgumentNotNull(accountTo, "TransactionLog to can not be null");
		Preconditions.checkArgumentNotNull(amount, "TransactionLog amount can not be null");
		Preconditions.checkArgumentNotNull(tax, "TransactionLog tax can not be null");
		Preconditions.checkArgumentNotNull(timeStamp, "TransactionLog timeStamp can not be null");

	}

}
