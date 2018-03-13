package com.jmj.bank.infrastructure.rest.transaction.Response;

public class TransactionResponse {

	private final String result;

	public TransactionResponse(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}
}
