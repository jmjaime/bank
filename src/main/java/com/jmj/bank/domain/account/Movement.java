package com.jmj.bank.domain.account;

import com.jmj.bank.domain.money.Money;
import com.jmj.bank.utils.validations.Preconditions;

public class Movement {

	private final Money amount;
	private final MovementType type;

	public Movement(Money amount, MovementType type) {
		this.amount = amount;
		this.type = type;
		validate();
	}

	public static Movement debit(Money amount){
		return new Movement(amount,MovementType.DEBIT);
	}

	public static Movement credit(Money amount){
		return new Movement(amount,MovementType.CREDIT);
	}

	public Money getBalance() {
		return amount.multiply(Money.valueOf(type.sing));
	}

	private void validate() {
		Preconditions.checkArgumentNotNull(amount, "Movement amount can not be null");
		Preconditions.checkArgument(() -> amount.isGreaterThan(Money.ZERO), "Movement amount should be greater than 0");
		Preconditions.checkArgumentNotNull(type, "Movement type can not be null");
	}

}
