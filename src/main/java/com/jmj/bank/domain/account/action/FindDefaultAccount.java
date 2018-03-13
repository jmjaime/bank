package com.jmj.bank.domain.account.action;

import com.jmj.bank.domain.account.Account;
import com.jmj.bank.domain.account.AccountId;
import com.jmj.bank.domain.account.AccountRepository;
import com.jmj.bank.domain.account.DefaultAccount;

public class FindDefaultAccount {

	private final AccountRepository accountRepository;

	public FindDefaultAccount(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public AccountDTO execute() {
		AccountId defaultAccountId = new AccountId(DefaultAccount.DEFAULT_ACCOUNT);
		Account account = accountRepository.find(defaultAccountId);
		return toDto(account);
	}

	private AccountDTO toDto(Account account) {
		return new AccountDTO(account.getId().id(), account.getBank().getName(), account.getCountry().getName(), account.balance().toBigDecimal());
	}

}
