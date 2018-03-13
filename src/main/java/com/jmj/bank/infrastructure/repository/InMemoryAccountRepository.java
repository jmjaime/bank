package com.jmj.bank.infrastructure.repository;

import java.util.List;
import java.util.Map;

import com.jmj.bank.domain.account.Account;
import com.jmj.bank.domain.account.AccountId;
import com.jmj.bank.domain.account.AccountRepository;
import com.jmj.bank.domain.account.Movement;
import com.jmj.bank.domain.account.exception.AccountNotFoundException;

public class InMemoryAccountRepository implements AccountRepository {

	private final Map<String, Account> accounts;

	public InMemoryAccountRepository(Map<String, Account> accounts) {
		this.accounts = accounts;
	}

	@Override
	public Account find(AccountId accountId) {
		Account account = accounts.get(accountId.id());
		validateAccount(account, accountId);
		return account;
	}

	@Override
	public void saveAccountMovements(AccountId accountId, List<Movement> movements) {
		Account account = find(accountId);
		account.impactWithMovements(movements);
	}

	private void validateAccount(Account account, AccountId accountId) {
		if (account == null) {
			throw new AccountNotFoundException("Account " + accountId.id() + " not found");
		}
	}
}
