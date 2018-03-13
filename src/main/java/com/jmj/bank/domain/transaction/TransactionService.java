package com.jmj.bank.domain.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.jmj.bank.domain.account.Account;
import com.jmj.bank.domain.account.AccountRepository;
import com.jmj.bank.domain.account.Movement;
import com.jmj.bank.domain.money.Money;

public class TransactionService {

	private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

	private final TransactionLogRepository transactionLogRepository;
	private final AccountRepository accountRepository;

	public TransactionService(TransactionLogRepository transactionLogRepository, AccountRepository accountRepository) {
		this.transactionLogRepository = transactionLogRepository;
		this.accountRepository = accountRepository;
	}

	void doTransaction(Transaction transaction) {
		List<Movement> accountFromMove = createAccountFromMovements(transaction);
		List<Movement> accountToMovements = createAccountToMovements(transaction);
		saveMovements(transaction.getAccountFrom(), accountFromMove);
		saveMovements(transaction.getAccountTo(), accountToMovements);
		TransactionLog transactionLog = saveTransactionLog(transaction);
		logTransaction(transactionLog);
	}

	private void logTransaction(TransactionLog transactionLog) {
		logger.info("Transaction successful {}", transactionAsText(transactionLog));
	}

	private String transactionAsText(TransactionLog transactionLog) {
		return new StringBuilder(" {")
				.append(" ID: ").append(transactionLog.getId().id())
				.append(" Account From ID: ").append(transactionLog.getAccountFrom().id())
				.append(" Account To ID: ").append(transactionLog.getAccountTo().id())
				.append(" Amount: $").append(transactionLog.getAmount())
				.append(" Tax: $").append(transactionLog.getTax())
				.append(" Time Stamp: ").append(transactionLog.getTimeStamp())
				.append(" }").toString();
	}

	private void saveMovements(Account account, List<Movement> accountMovements) {
		accountRepository.saveAccountMovements(account.getId(), accountMovements);
	}

	private List<Movement> createAccountToMovements(Transaction transaction) {
		Movement credit = Movement.credit(transaction.getAmount());
		return Lists.newArrayList(credit);
	}

	private List<Movement> createAccountFromMovements(Transaction transaction) {
		List<Movement> movements = new ArrayList();
		movements.add(Movement.debit(transaction.getAmount()));
		Optional<Movement> tax = createTaxMovement(transaction);
		tax.ifPresent(movements::add);
		return movements;
	}

	private TransactionLog saveTransactionLog(Transaction transaction) {
		TransactionId transactionId = transactionLogRepository.nextId();
		TransactionLog transactionLog = TransactionLogBuilder.builder().withId(transactionId).withTransaction(transaction).build();
		transactionLogRepository.save(transactionLog);
		return transactionLog;
	}

	private Optional<Movement> createTaxMovement(Transaction transaction) {
		Money tax = transaction.calculateTax();
		return tax.isGreaterThan(Money.ZERO) ? Optional.of(Movement.debit(tax)) : Optional.empty();
	}
}
