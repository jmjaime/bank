package com.jmj.bank.domain.transaction.action;

import static com.jmj.bank.domain.transaction.AccountDataForTest.ACCOUNT_ID_FROM;
import static com.jmj.bank.domain.transaction.AccountDataForTest.ACCOUNT_ID_TO;
import static com.jmj.bank.domain.transaction.AccountDataForTest.ARGENTINE;
import static com.jmj.bank.domain.transaction.AccountDataForTest.CITI_BANK;
import static com.jmj.bank.domain.transaction.AccountDataForTest.INTERNATIONAL_TAX;
import static com.jmj.bank.domain.transaction.AccountDataForTest.NATIONAL_TAX;
import static com.jmj.bank.domain.transaction.AccountDataForTest.SAME_BANK_TAX;
import static com.jmj.bank.domain.transaction.AccountDataForTest.SANTANDER_BANK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;

import com.jmj.bank.domain.account.Account;
import com.jmj.bank.domain.account.AccountRepository;
import com.jmj.bank.domain.account.exception.AccountNotFoundException;
import com.jmj.bank.domain.clock.Clock;
import com.jmj.bank.domain.clock.SystemClock;
import com.jmj.bank.domain.money.Money;
import com.jmj.bank.domain.transaction.AsyncTransactionDispatcher;
import com.jmj.bank.domain.transaction.TransactionDispatcher;
import com.jmj.bank.domain.transaction.TransactionService;
import com.jmj.bank.domain.transaction.exception.ServiceUnavailableException;
import com.jmj.bank.domain.transaction.tax.TaxType;
import com.jmj.bank.domain.transaction.tax.TaxesCost;
import com.jmj.bank.domain.transaction.tax.TaxesRepository;
import com.jmj.bank.infrastructure.repository.InMemoryAccountRepository;
import com.jmj.bank.infrastructure.repository.InMemoryTaxesRepository;

public class TransferTest {

	private static final BigDecimal TRANSFER_AMOUNT = new BigDecimal("1575.9");
	public static final String INVALID_ACCOUNT = "INVALID_ACCOUNT";

	private Transfer transfer;

	private Account accountFrom = new Account(ACCOUNT_ID_FROM, CITI_BANK, ARGENTINE);
	private Account accountTo = new Account(ACCOUNT_ID_TO, SANTANDER_BANK, ARGENTINE);
	private Clock clock = new SystemClock();
	private Executor executor;
	private TransferData transferData;

	@Test
	void whenTransferShouldDispatchTransaction() {
		givenValidTransfer();
		onDoTransfer();
		thenTransactionShouldBeDispatched();
	}

	@Test
	void whenTransferWithInvalidOriginAccountShouldFail() {
		givenTransferWithInvalidOriginAccount();
		Throwable exception = assertThrows(AccountNotFoundException.class, this::onDoTransfer);
		thenShouldFailByUnknownAccount(exception);
	}

	@Test
	void whenTransferWithInvalidAccountToShouldFail() {
		givenTransferWithInvalidAccountTo();
		Throwable exception = assertThrows(AccountNotFoundException.class, this::onDoTransfer);
		thenShouldFailByUnknownAccount(exception);
	}

	@Test()
	void whenTransferCanNotBeDispatchedShouldFail() {
		givenValidTransferAndTooBusyDispatcher();
		Throwable exception = assertThrows(ServiceUnavailableException.class, this::onDoTransfer);
		thenShouldFailByUnavailableService(exception);
	}

	private void givenTransferWithInvalidOriginAccount() {
		executor = Mockito.mock(Executor.class);
		transferData = new TransferData(INVALID_ACCOUNT, ACCOUNT_ID_TO.id(), TRANSFER_AMOUNT);
		createTransferAction();
	}

	private void givenTransferWithInvalidAccountTo() {
		executor = Mockito.mock(Executor.class);
		transferData = new TransferData(ACCOUNT_ID_FROM.id(), INVALID_ACCOUNT, TRANSFER_AMOUNT);
		createTransferAction();
	}

	private void givenValidTransfer() {
		executor = Mockito.mock(Executor.class);
		transferData = new TransferData(ACCOUNT_ID_FROM.id(), ACCOUNT_ID_TO.id(), TRANSFER_AMOUNT);
		createTransferAction();
	}

	private void givenValidTransferAndTooBusyDispatcher() {
		givenValidTransfer();
		Mockito.doThrow(RejectedExecutionException.class).when(executor).execute(Mockito.any(Runnable.class));
	}

	private void onDoTransfer() {
		transfer.execute(transferData);
	}

	private void thenTransactionShouldBeDispatched() {
		Mockito.verify(executor, new Times(1)).execute(Mockito.any(Runnable.class));
	}

	private void thenShouldFailByUnknownAccount(Throwable exception) {
		assertEquals("Account "+INVALID_ACCOUNT+" not found", exception.getMessage());
	}

	private void thenShouldFailByUnavailableService(Throwable exception) {
		assertEquals("Service unavailable, please retry un some minutes", exception.getMessage());
	}

	private void createTransferAction() {
		TransactionService transactionService = Mockito.mock(TransactionService.class);
		TransactionDispatcher transactionDispatcher = new AsyncTransactionDispatcher(executor, transactionService);
		TaxesRepository taxesRepository = new InMemoryTaxesRepository(createTaxes());
		AccountRepository accountRepository = createAccountRepository();
		transfer = new Transfer(accountRepository, taxesRepository, transactionDispatcher, clock);
	}

	private InMemoryAccountRepository createAccountRepository() {
		Map<String, Account> accounts = new HashMap();
		accounts.put(accountFrom.getId().id(), accountFrom);
		accounts.put(accountTo.getId().id(), accountTo);
		return new InMemoryAccountRepository(accounts);
	}

	private TaxesCost createTaxes() {
		EnumMap<TaxType, Money> taxes = new EnumMap(TaxType.class);
		taxes.put(TaxType.SAME_BANK, SAME_BANK_TAX);
		taxes.put(TaxType.INTERNACIONAL, INTERNATIONAL_TAX);
		taxes.put(TaxType.NACIONAL, NATIONAL_TAX);
		return new TaxesCost(taxes);
	}

}
