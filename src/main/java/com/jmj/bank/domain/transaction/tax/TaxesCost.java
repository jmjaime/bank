package com.jmj.bank.domain.transaction.tax;

import java.util.EnumMap;

import com.jmj.bank.domain.money.Money;
import com.jmj.bank.utils.validations.Preconditions;

public class TaxesCost {

	private final EnumMap<TaxType, Money> taxesCosts;

	public TaxesCost(EnumMap<TaxType, Money> taxesCosts) {
		this.taxesCosts = taxesCosts;
		validate();
	}

	public Money costForTax(TaxType taxType){
		return taxesCosts.get(taxType);
	}

	private void validate() {
		Preconditions.checkArgumentNotNull(taxesCosts, "Taxes costs can not be null");
		Preconditions.checkArgument(() -> taxesCosts.size() == TaxType.values().length, "Should be configured all taxes costs");
	}

}
