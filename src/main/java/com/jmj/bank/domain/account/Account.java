package com.jmj.bank.domain.account;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.jmj.bank.domain.money.Money;
import com.jmj.bank.domain.bank.Bank;
import com.jmj.bank.domain.country.Country;
import com.jmj.bank.utils.validations.Preconditions;

public class Account {

	private final AccountId id;
	private final Bank bank;
	private final Country country;
	private final List<Movement> movements;

	public Account(AccountId id, Bank bank, Country country) {
		this(id, bank, country, new ArrayList());
	}

	public Account(AccountId id, Bank bank, Country country, List<Movement> movements) {
		this.id = id;
		this.bank = bank;
		this.country = country;
		this.movements = movements;
		validate();
	}

	public AccountId getId() {
		return id;
	}

	public ImmutableList<Movement> getMovements() {
		return ImmutableList.copyOf(movements);
	}

	public Money balance() {
		return calculateBalance();
	}

	public void impactWithMovements(List<Movement> movements) {
		this.movements.addAll(movements);
	}

	private Money calculateBalance() {
		return movements.parallelStream().map(Movement::getBalance).reduce(Money.ZERO, (x, y) -> x.add(y));
	}

	public Bank getBank() {
		return bank;
	}

	public Country getCountry() {
		return country;
	}

	private void validate() {
		Preconditions.checkArgumentNotNull(id, "Movement amount can not be null");
		Preconditions.checkArgumentNotNull(bank, "Movement type can not be null");
		Preconditions.checkArgumentNotNull(country, "Movement type can not be null");
		Preconditions.checkArgumentNotNull(movements, "Movement type can not be null");
	}
}
