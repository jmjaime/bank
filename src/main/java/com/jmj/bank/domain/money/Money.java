package com.jmj.bank.domain.money;

import java.math.BigDecimal;

import com.jmj.bank.utils.validations.Preconditions;

public class Money implements Comparable<Money>{

	private final BigDecimal amount;
	public static final Money ZERO = new Money(BigDecimal.ZERO);

	public Money(BigDecimal amount) {
		this.amount = amount;
		validate();
	}

	public static Money valueOf(final String money) {
		return new Money(new BigDecimal(money));
	}

	public static Money valueOf(final Integer money) {
		return new Money(new BigDecimal(money));
	}

	public Money multiply(Money value) {
		return new Money(amount.multiply(value.amount));
	}

	public Money add(Money value) {
		return new Money(amount.add(value.amount));
	}

	public boolean isGreaterThan(Money value) {
		return amount.compareTo(value.amount) > 0;
	}

	@Override
	public int compareTo(Money otherMoney) {
		return amount.compareTo(otherMoney.amount);
	}

	@Override
	public String toString() {
		return amount.toString();
	}

	public BigDecimal toBigDecimal(){
		return amount;
	}

	private void validate() {
		Preconditions.checkArgumentNotNull(amount, "Money amount can not be null");
	}
}
