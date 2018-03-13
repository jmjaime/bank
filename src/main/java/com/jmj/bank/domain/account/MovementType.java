package com.jmj.bank.domain.account;

public enum MovementType {

	CREDIT(1), DEBIT(-1);

	MovementType(Integer sing){
		this.sing = sing;
	}
	public final Integer sing;
}
