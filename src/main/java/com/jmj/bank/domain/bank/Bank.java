package com.jmj.bank.domain.bank;

import org.apache.commons.lang3.StringUtils;

import com.jmj.bank.utils.validations.Preconditions;

public class Bank {

	private final String name;

	public Bank(String name) {
		this.name = name;
		validate();
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object otherBank) {
		if (this == otherBank) {
			return true;
		}
		if (!(otherBank instanceof Bank)) {
			return false;
		}
		Bank bank = (Bank) otherBank;
		return name.equals(bank.name);
	}

	private void validate() {
		Preconditions.checkArgument(() -> !StringUtils.isEmpty(name), "Bank name is mandatory");
	}

}
