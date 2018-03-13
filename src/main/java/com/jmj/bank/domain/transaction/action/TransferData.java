package com.jmj.bank.domain.transaction.action;

import java.math.BigDecimal;

import com.jmj.bank.utils.validations.Preconditions;

public class TransferData {

	private final String accountIdFrom;
	private final String accountIdTo;
	private final BigDecimal amount;

	public TransferData(String accountIdFrom, String toAccountId, BigDecimal amount) {
		this.accountIdFrom = accountIdFrom;
		this.accountIdTo = toAccountId;
		this.amount = amount;
		validate();
	}

	public String getAccountIdFrom() {
		return accountIdFrom;
	}

	public String getAccountIdTo() {
		return accountIdTo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	private void validate() {
		Preconditions.checkArgumentNotNull(accountIdFrom, "accountIdFrom can not be null");
		Preconditions.checkArgumentNotNull(accountIdTo, "accountIdTo can not be null");
		Preconditions.checkArgumentNotNull(amount, "Amount can not be null");
		Preconditions.checkArgument(() -> !accountIdFrom.equals(accountIdTo), "Accounts can not be the same");
	}
}
