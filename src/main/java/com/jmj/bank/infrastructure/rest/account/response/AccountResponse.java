package com.jmj.bank.infrastructure.rest.account.response;

import java.math.BigDecimal;

public class AccountResponse {

	private final String id;
	private final String bank;
	private final String country;
	private final BigDecimal balance;

	public AccountResponse(String id, String bank, String country,BigDecimal balance) {
		this.id = id;
		this.bank = bank;
		this.country = country;
		this.balance = balance;
	}

	public String getId() {
		return id;
	}

	public String getBank() {
		return bank;
	}

	public String getCountry() {
		return country;
	}

	public BigDecimal getBalance() {
		return balance;
	}
}
