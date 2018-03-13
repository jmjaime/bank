package com.jmj.bank.domain.transaction;

import com.jmj.bank.utils.validations.Preconditions;

public class TransactionId {

	private final Long id;

	public TransactionId(Long id) {
		this.id = id;
		validate();
	}

	public Long id() {
		return id;
	}

	@Override
	public boolean equals(Object otherTransactionId) {
		if (this == otherTransactionId) {
			return true;
		}
		if (!(otherTransactionId instanceof TransactionId)) {
			return false;
		}
		TransactionId transactionId = (TransactionId) otherTransactionId;
		return id.equals(transactionId.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	private void validate() {
		Preconditions.checkArgumentNotNull(id, "Transaction id can not be null");
	}
}
