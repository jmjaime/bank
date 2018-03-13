package com.jmj.bank.infrastructure.repository.configuration;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import com.jmj.bank.domain.account.Account;
import com.jmj.bank.domain.account.AccountId;
import com.jmj.bank.domain.account.AccountRepository;
import com.jmj.bank.domain.account.DefaultAccount;
import com.jmj.bank.domain.account.Movement;
import com.jmj.bank.domain.account.MovementType;
import com.jmj.bank.domain.bank.Bank;
import com.jmj.bank.domain.country.Country;
import com.jmj.bank.domain.money.Money;
import com.jmj.bank.domain.transaction.TransactionLogRepository;
import com.jmj.bank.domain.transaction.tax.TaxType;
import com.jmj.bank.domain.transaction.tax.TaxesCost;
import com.jmj.bank.domain.transaction.tax.TaxesRepository;
import com.jmj.bank.infrastructure.repository.InMemoryAccountRepository;
import com.jmj.bank.infrastructure.repository.InMemoryTaxesRepository;
import com.jmj.bank.infrastructure.repository.InMemoryTransactionLogRepository;

@Configuration
public class RepositoriesConfiguration {

	@Bean
	public AccountRepository accountRepository() {
		Map<String, Account> accounts = createAccounts();
		return new InMemoryAccountRepository(accounts);
	}

	@Bean
	public TransactionLogRepository transactionLogRepository() {
		return new InMemoryTransactionLogRepository();
	}

	@Bean
	public TaxesRepository taxesRepository() {
		return new InMemoryTaxesRepository(createTaxes());
	}

	private Map<String, Account> createAccounts() {
		Map<String, Account> accounts = new HashMap();
		Account defaultAccount = createDefaultAccount();
		Account nationalSameBankAccount = createNationalSameBankAccount();
		Account nationalOtherBankAccount = createNationalAccount();
		Account internationalAccount = createInternationalAccount();
		accounts.put(defaultAccount.getId().id(), defaultAccount);
		accounts.put(nationalSameBankAccount.getId().id(), nationalSameBankAccount);
		accounts.put(nationalOtherBankAccount.getId().id(), nationalOtherBankAccount);
		accounts.put(internationalAccount.getId().id(), internationalAccount);
		return accounts;
	}

	private Account createDefaultAccount() {
		AccountId accountId = new AccountId(DefaultAccount.DEFAULT_ACCOUNT);
		List<Movement> initialMovements = Lists.newArrayList(new Movement(Money.valueOf("1000"), MovementType.CREDIT));
		return new Account(accountId, new Bank("Santander"), new Country("Argentina"), initialMovements);
	}

	private Account createNationalSameBankAccount() {
		AccountId accountId = new AccountId("16678/67");
		return new Account(accountId, new Bank("Santander"), new Country("Argentina"));
	}

	private Account createNationalAccount() {
		AccountId accountId = new AccountId("56789/32");
		return new Account(accountId, new Bank("Citi"), new Country("Argentina"));
	}

	private Account createInternationalAccount() {
		AccountId accountId = new AccountId("54321/81");
		return new Account(accountId, new Bank("Itau"), new Country("Brasil"));
	}

	private TaxesCost createTaxes() {
		EnumMap<TaxType, Money> taxes = new EnumMap(TaxType.class);
		taxes.put(TaxType.SAME_BANK, Money.valueOf("0"));
		taxes.put(TaxType.INTERNACIONAL, Money.valueOf("0.05"));
		taxes.put(TaxType.NACIONAL, Money.valueOf("0.01"));
		return new TaxesCost(taxes);
	}
}
