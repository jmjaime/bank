package com.jmj.bank.domain.transaction;

import static com.jmj.bank.domain.transaction.AccountDataForTest.ACCOUNT_ID_FROM;
import static com.jmj.bank.domain.transaction.AccountDataForTest.ACCOUNT_ID_TO;
import static com.jmj.bank.domain.transaction.AccountDataForTest.ARGENTINE;
import static com.jmj.bank.domain.transaction.AccountDataForTest.BRAZIL;
import static com.jmj.bank.domain.transaction.AccountDataForTest.CITI_BANK;
import static com.jmj.bank.domain.transaction.AccountDataForTest.INTERNATIONAL_TAX;
import static com.jmj.bank.domain.transaction.AccountDataForTest.NATIONAL_TAX;
import static com.jmj.bank.domain.transaction.AccountDataForTest.SAME_BANK_TAX;
import static com.jmj.bank.domain.transaction.AccountDataForTest.SANTANDER_BANK;

import java.math.BigDecimal;
import java.util.EnumMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jmj.bank.domain.account.Account;
import com.jmj.bank.domain.clock.Clock;
import com.jmj.bank.domain.clock.SystemClock;
import com.jmj.bank.domain.transaction.tax.TaxType;
import com.jmj.bank.domain.transaction.tax.TaxesCost;
import com.jmj.bank.domain.money.Money;

class TransactionTest {

	private static final Money TRANSACTION_AMOUNT = new Money(BigDecimal.valueOf(100));

	private final TaxesCost taxesCosts = new TaxesCost(createTaxes());
	private Clock clock = new SystemClock();

	private Account accountFrom;
	private Account accountTo;
	private Money taxPaid;
	private Transaction transaction;

	@Test
	@DisplayName("When is international transaction tax should be 5%")
	void whenIsInternationalTransactionTaxShouldBe5Percent() {
		givenInternationalTransaction();
		whenCalculateTax();
		thenTaxShouldBe(Money.valueOf(5));
	}

	@Test
	@DisplayName("When is national transaction and different bank tax should be 1%")
	void whenIsNationalTransactionAndDifferentBankTaxShouldBe1Percent() {
		givenNationalTransactionWithDifferentBank();
		whenCalculateTax();
		thenTaxShouldBe(Money.valueOf(1));
	}

	@Test
	@DisplayName("When is national transaction and same bank tax should be 0%")
	void whenIsNationalTransactionAndSameBankTaxShouldBe0Percent() {
		givenNationalTransactionWitSameBank();
		whenCalculateTax();
		thenTaxShouldBe(Money.ZERO);
	}

	private void givenNationalTransactionWitSameBank() {
		accountFrom = new Account(ACCOUNT_ID_FROM, CITI_BANK, ARGENTINE);
		accountTo = new Account(ACCOUNT_ID_TO, CITI_BANK, ARGENTINE);
		transaction = new Transaction(accountFrom, accountTo, TRANSACTION_AMOUNT, clock.timeStamp(), taxesCosts);
	}

	private void givenNationalTransactionWithDifferentBank() {
		accountFrom = new Account(ACCOUNT_ID_FROM, CITI_BANK, ARGENTINE);
		accountTo = new Account(ACCOUNT_ID_TO, SANTANDER_BANK, ARGENTINE);
		transaction = new Transaction(accountFrom, accountTo, TRANSACTION_AMOUNT, clock.timeStamp(), taxesCosts);
	}

	private void givenInternationalTransaction() {
		accountFrom = new Account(ACCOUNT_ID_FROM, CITI_BANK, ARGENTINE);
		accountTo = new Account(ACCOUNT_ID_TO, SANTANDER_BANK, BRAZIL);
		transaction = new Transaction(accountFrom, accountTo, TRANSACTION_AMOUNT, clock.timeStamp(), taxesCosts);
	}

	private void whenCalculateTax() {
		taxPaid = transaction.calculateTax();
	}

	private void thenTaxShouldBe(Money expectedTax) {
		Assertions.assertTrue(expectedTax.compareTo(taxPaid) == 0);
	}

	private EnumMap<TaxType, Money> createTaxes() {
		EnumMap<TaxType, Money> taxes = new EnumMap(TaxType.class);
		taxes.put(TaxType.SAME_BANK, SAME_BANK_TAX);
		taxes.put(TaxType.INTERNACIONAL, INTERNATIONAL_TAX);
		taxes.put(TaxType.NACIONAL, NATIONAL_TAX);
		return taxes;
	}

}
