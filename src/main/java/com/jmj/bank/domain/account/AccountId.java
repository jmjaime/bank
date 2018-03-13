package com.jmj.bank.domain.account;

import org.apache.commons.lang3.StringUtils;
import com.jmj.bank.utils.validations.Preconditions;

public class AccountId {

	private final String id;

	public AccountId(String id) {
		this.id = id;
		validate();
	}

	public String id() {
		return id;
	}

	@Override
	public boolean equals(Object otherAccountId) {
		if (this == otherAccountId) {
			return true;
		}
		if (!(otherAccountId instanceof AccountId)) {
			return false;
		}
		AccountId accountId = (AccountId) otherAccountId;
		return id.equals(accountId.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	private void validate() {
		Preconditions.checkArgument(() -> !StringUtils.isEmpty(id), "Account id can not be null");
	}
}
