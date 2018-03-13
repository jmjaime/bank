package com.jmj.bank.domain.transaction;

import com.jmj.bank.domain.account.AccountId;
import com.jmj.bank.domain.bank.Bank;
import com.jmj.bank.domain.country.Country;
import com.jmj.bank.domain.money.Money;

public interface AccountDataForTest {

	AccountId ACCOUNT_ID_FROM = new AccountId("34234234/45");
	AccountId ACCOUNT_ID_TO = new AccountId("565656/78");

	Bank CITI_BANK = new Bank("City");
	Bank SANTANDER_BANK = new Bank("Santander");

	Country ARGENTINE = new Country("ARG");
	Country BRAZIL = new Country("BR");

	Money INTERNATIONAL_TAX = Money.valueOf("0.05");
	Money NATIONAL_TAX = Money.valueOf("0.01");
	Money SAME_BANK_TAX = Money.ZERO;
}
