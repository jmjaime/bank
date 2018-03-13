package com.jmj.bank.infrastructure.repository;

import com.jmj.bank.domain.transaction.tax.TaxesCost;
import com.jmj.bank.domain.transaction.tax.TaxesRepository;

public class InMemoryTaxesRepository  implements TaxesRepository {

	private final TaxesCost taxesCost;

	public InMemoryTaxesRepository(TaxesCost taxesCost) {
		this.taxesCost = taxesCost;
	}

	@Override
	public TaxesCost findCurrentTaxes() {
		return taxesCost;
	}
}
