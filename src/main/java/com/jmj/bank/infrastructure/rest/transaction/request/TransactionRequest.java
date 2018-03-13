package com.jmj.bank.infrastructure.rest.transaction.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionRequest {

	private final String accountFrom;
	private final String accountTo;
	private final BigDecimal amount;

	@JsonCreator
	public TransactionRequest(@JsonProperty("account_from") String accountFrom, @JsonProperty("account_to") String accountTo,
			@JsonProperty("amount") BigDecimal amount) {
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amount = amount;
	}

	public String getAccountFrom() {
		return accountFrom;
	}

	public String getAccountTo() {
		return accountTo;
	}

	public BigDecimal getAmount() {
		return amount;
	}
}
