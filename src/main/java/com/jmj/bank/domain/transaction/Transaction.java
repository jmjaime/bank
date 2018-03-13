package com.jmj.bank.domain.transaction;

import java.time.Instant;

import com.jmj.bank.domain.account.Account;
import com.jmj.bank.domain.money.Money;
import com.jmj.bank.domain.transaction.tax.TaxType;
import com.jmj.bank.domain.transaction.tax.TaxesCost;
import com.jmj.bank.utils.validations.Preconditions;

public class Transaction {

	private final Account accountFrom;
	private final Account accountTo;
	private final Money amount;
	private final Instant timeStamp;
	private final TaxesCost taxesCosts;

	public Transaction(Account accountFrom, Account accountTo, Money amount, Instant timeStamp, TaxesCost taxesCosts) {
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amount = amount;
		this.timeStamp = timeStamp;
		this.taxesCosts = taxesCosts;
		validate();
	}

	public Account getAccountFrom() {
		return accountFrom;
	}

	public Account getAccountTo() {
		return accountTo;
	}

	public Instant getTimeStamp() {
		return timeStamp;
	}

	public Money getAmount() {
		return amount;
	}

	public Money calculateTax() {
		Money taxCost = calculateTaxCost();
		return amount.multiply(taxCost);
	}

	private Money calculateTaxCost() {
		if (isInternationalTransaction()) {
			return taxesCosts.costForTax(TaxType.INTERNACIONAL);
		}
		if (!isSameBankTransaction()) {
			return taxesCosts.costForTax(TaxType.NACIONAL);
		}
		return taxesCosts.costForTax(TaxType.SAME_BANK);
	}

	private boolean isInternationalTransaction() {
		return !accountFrom.getCountry().equals(accountTo.getCountry());
	}

	private boolean isSameBankTransaction() {
		return accountFrom.getBank().equals(accountTo.getBank());
	}

	private void validate() {
		Preconditions.checkArgumentNotNull(accountFrom, "Transaction from can not be null");
		Preconditions.checkArgumentNotNull(accountTo, "Transaction to can not be null");
		Preconditions.checkArgumentNotNull(amount, "Transaction amount can not be null");
		Preconditions.checkArgument(() -> amount.isGreaterThan(Money.ZERO), "Transaction amount should be greater than 0");
		Preconditions.checkArgumentNotNull(timeStamp, "Transaction timeStamp can not be null");
		Preconditions.checkArgumentNotNull(taxesCosts, "Transaction taxesCosts can not be null");
	}

}