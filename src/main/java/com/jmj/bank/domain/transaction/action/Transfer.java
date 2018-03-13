package com.jmj.bank.domain.transaction.action;

import com.jmj.bank.domain.account.AccountRepository;
import com.jmj.bank.domain.clock.Clock;
import com.jmj.bank.domain.transaction.tax.TaxesCost;
import com.jmj.bank.domain.transaction.tax.TaxesRepository;
import com.jmj.bank.domain.transaction.TransactionDispatcher;
import com.jmj.bank.domain.money.Money;
import com.jmj.bank.domain.account.Account;
import com.jmj.bank.domain.account.AccountId;
import com.jmj.bank.domain.transaction.Transaction;

public class Transfer {

	private final AccountRepository accountRepository;
	private final TaxesRepository taxesRepository;
	private final TransactionDispatcher transactionDispatcher;
	private final Clock clock;

	public Transfer(AccountRepository accountRepository, TaxesRepository taxesRepository, TransactionDispatcher transactionDispatcher, Clock clock) {
		this.accountRepository = accountRepository;
		this.taxesRepository = taxesRepository;
		this.transactionDispatcher = transactionDispatcher;
		this.clock = clock;
	}

	public void execute(TransferData transferData) {
		Account accountFrom = findAccount(transferData.getAccountIdFrom());
		Account accountTo = findAccount(transferData.getAccountIdTo());
		TaxesCost currentTaxes = findTaxes();
		Money amountToTransfer = new Money(transferData.getAmount());
		Transaction transaction = new Transaction(accountFrom, accountTo, amountToTransfer, clock.timeStamp(), currentTaxes);
		transactionDispatcher.execute(transaction);
	}

	private TaxesCost findTaxes() {
		return taxesRepository.findCurrentTaxes();
	}

	private Account findAccount(String accountId) {
		AccountId id = new AccountId(accountId);
		return accountRepository.find(id);
	}

}
