package com.jmj.bank.domain.transaction;

import static com.jmj.bank.domain.transaction.AccountDataForTest.ACCOUNT_ID_FROM;
import static com.jmj.bank.domain.transaction.AccountDataForTest.ARGENTINE;
import static com.jmj.bank.domain.transaction.AccountDataForTest.BRAZIL;
import static com.jmj.bank.domain.transaction.AccountDataForTest.CITI_BANK;
import static com.jmj.bank.domain.transaction.AccountDataForTest.INTERNATIONAL_TAX;
import static com.jmj.bank.domain.transaction.AccountDataForTest.NATIONAL_TAX;
import static com.jmj.bank.domain.transaction.AccountDataForTest.SAME_BANK_TAX;
import static com.jmj.bank.domain.transaction.AccountDataForTest.SANTANDER_BANK;

import java.time.Instant;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.jmj.bank.domain.account.Account;
import com.jmj.bank.domain.account.AccountId;
import com.jmj.bank.domain.account.AccountRepository;
import com.jmj.bank.domain.money.Money;
import com.jmj.bank.domain.transaction.tax.TaxType;
import com.jmj.bank.domain.transaction.tax.TaxesCost;
import com.jmj.bank.infrastructure.repository.InMemoryAccountRepository;
import com.jmj.bank.infrastructure.repository.InMemoryTransactionLogRepository;

public class TransactionServiceTest {

	private static final Money TRANSACTION_AMOUNT = Money.valueOf("1000");

	private Account accountFrom = new Account(ACCOUNT_ID_FROM, CITI_BANK, ARGENTINE);
	private Account nationalSameBankAccountTo = new Account(new AccountId("232323/45"), CITI_BANK, ARGENTINE);
	private Account nationalAccountTo = new Account(new AccountId("12323/45"), SANTANDER_BANK, ARGENTINE);
	private Account internationalAccountTo = new Account(new AccountId("67323/45"), SANTANDER_BANK, BRAZIL);

	private Transaction transaction;
	private TransactionService transactionService;
	private TransactionLogRepository transactionLogRepository;
	private AccountRepository accountRepository;

	@Test
	void processInternationalTransaction() {
		givenATransactionWithInternationalTax();
		onDoTransaction();
		thenAccountsWereImpactedWithTax(internationalAccountTo.getId(), INTERNATIONAL_TAX);
		thenTransactionLogWereStored(internationalAccountTo.getId(), INTERNATIONAL_TAX);
	}

	@Test
	void processNationalTransactionWithDifferentBanks() {
		givenATransactionWithNationalTax();
		onDoTransaction();
		thenAccountsWereImpactedWithTax(nationalAccountTo.getId(), NATIONAL_TAX);
		thenTransactionLogWereStored(nationalAccountTo.getId(), NATIONAL_TAX);
	}

	@Test
	void processNationalTransactionWitSameBanks() {
		givenATransactionWithoutTax();
		onDoTransaction();
		thenAccountsWereImpactedWithoutTax(nationalSameBankAccountTo.getId());
		thenTransactionLogWereStored(nationalSameBankAccountTo.getId(), SAME_BANK_TAX);
	}

	private void givenATransactionWithInternationalTax() {
		transactionService = createTransactionService();
		transaction = new Transaction(accountFrom, internationalAccountTo, TRANSACTION_AMOUNT, Instant.now(), createTaxes());
	}

	private void givenATransactionWithNationalTax() {
		transactionService = createTransactionService();
		transaction = new Transaction(accountFrom, nationalAccountTo, TRANSACTION_AMOUNT, Instant.now(), createTaxes());
	}

	private void givenATransactionWithoutTax() {
		transactionService = createTransactionService();
		transaction = new Transaction(accountFrom, nationalSameBankAccountTo, TRANSACTION_AMOUNT, Instant.now(), createTaxes());
	}

	private void onDoTransaction() {
		transactionService.doTransaction(transaction);
	}

	private void thenAccountsWereImpactedWithoutTax(AccountId accountToId) {
		Account originAccount = accountRepository.find(accountFrom.getId());
		Account toAccount = accountRepository.find(accountToId);
		Assertions.assertEquals(1, originAccount.getMovements().size());
		Assertions.assertEquals(1, toAccount.getMovements().size());
		Money originBalance = TRANSACTION_AMOUNT.multiply(Money.valueOf(-1));
		Assertions.assertTrue(originBalance.compareTo(originAccount.balance()) == 0);
		Assertions.assertTrue(TRANSACTION_AMOUNT.compareTo(toAccount.balance()) == 0);
	}

	private void thenTransactionLogWereStored(AccountId accountToId, Money tax) {
		Optional<TransactionLog> optionalTransactionLog = transactionLogRepository.findAllTransactions().stream().findFirst();
		Assertions.assertTrue(optionalTransactionLog.isPresent());
		optionalTransactionLog.ifPresent(it -> {
			Assertions.assertEquals(ACCOUNT_ID_FROM, it.getAccountFrom());
			Assertions.assertEquals(accountToId, it.getAccountTo());
			Assertions.assertEquals(accountToId, it.getAccountTo());
			Assertions.assertTrue(TRANSACTION_AMOUNT.compareTo(it.getAmount()) == 0);
			Money taxValue = TRANSACTION_AMOUNT.multiply(tax);
			Assertions.assertTrue(taxValue.compareTo(it.getTax()) == 0);
		});
	}

	private void thenAccountsWereImpactedWithTax(AccountId accountToId, Money tax) {
		Account originAccount = accountRepository.find(accountFrom.getId());
		Account toAccount = accountRepository.find(accountToId);
		Assertions.assertEquals(2, originAccount.getMovements().size());
		Assertions.assertEquals(1, toAccount.getMovements().size());
		Money taxValue = TRANSACTION_AMOUNT.multiply(tax);
		Money originBalance = TRANSACTION_AMOUNT.add(taxValue).multiply(Money.valueOf(-1));
		Assertions.assertTrue(originBalance.compareTo(originAccount.balance()) == 0);
		Assertions.assertTrue(TRANSACTION_AMOUNT.compareTo(toAccount.balance()) == 0);
	}

	private TransactionService createTransactionService() {
		transactionLogRepository = new InMemoryTransactionLogRepository();
		Map<String, Account> accounts = new HashMap();
		accounts.put(accountFrom.getId().id(), accountFrom);
		accounts.put(nationalAccountTo.getId().id(), nationalAccountTo);
		accounts.put(internationalAccountTo.getId().id(), internationalAccountTo);
		accounts.put(nationalSameBankAccountTo.getId().id(), nationalSameBankAccountTo);
		accountRepository = new InMemoryAccountRepository(accounts);
		return new TransactionService(transactionLogRepository, accountRepository);
	}

	private TaxesCost createTaxes() {
		EnumMap<TaxType, Money> taxes = new EnumMap(TaxType.class);
		taxes.put(TaxType.SAME_BANK, SAME_BANK_TAX);
		taxes.put(TaxType.INTERNACIONAL, INTERNATIONAL_TAX);
		taxes.put(TaxType.NACIONAL, NATIONAL_TAX);
		return new TaxesCost(taxes);
	}

}
