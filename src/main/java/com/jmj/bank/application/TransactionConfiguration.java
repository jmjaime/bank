package com.jmj.bank.application;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jmj.bank.domain.account.AccountRepository;
import com.jmj.bank.domain.account.action.FindDefaultAccount;
import com.jmj.bank.domain.clock.Clock;
import com.jmj.bank.domain.clock.SystemClock;
import com.jmj.bank.domain.transaction.AsyncTransactionDispatcher;
import com.jmj.bank.domain.transaction.TransactionDispatcher;
import com.jmj.bank.domain.transaction.TransactionLogRepository;
import com.jmj.bank.domain.transaction.TransactionService;
import com.jmj.bank.domain.transaction.action.Transfer;
import com.jmj.bank.domain.transaction.tax.TaxesRepository;
import com.jmj.bank.utils.threads.TransactionRunnerPool;

@Configuration
public class TransactionConfiguration {

	private static final int QUEUE_SIZE = 100;
	private static final int EXECUTE_TIME_OUT = 1000;
	private static final int POOL_SIZE = 5;

	@Bean
	public Transfer transfer(AccountRepository accountRepository, TaxesRepository taxesRepository, TransactionDispatcher transactionDispatcher,
			Clock clock) {
		return new Transfer(accountRepository, taxesRepository, transactionDispatcher, clock);
	}

	@Bean
	public TransactionDispatcher transactionDispatcher(Executor executor, TransactionService transactionService) {
		return new AsyncTransactionDispatcher(executor, transactionService);
	}

	@Bean
	public TransactionService transactionService(TransactionLogRepository transactionLogRepository, AccountRepository accountRepository) {
		return new TransactionService(transactionLogRepository, accountRepository);
	}

	@Bean
	public FindDefaultAccount findDefaultAccount(AccountRepository accountRepository){
		return new FindDefaultAccount(accountRepository);
	}
	@Bean
	public Executor executor() {
		return new TransactionRunnerPool(QUEUE_SIZE, EXECUTE_TIME_OUT, POOL_SIZE);
	}

	@Bean
	public Clock clock() {
		return new SystemClock();
	}
}
