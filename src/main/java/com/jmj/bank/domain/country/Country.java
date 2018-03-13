package com.jmj.bank.domain.country;

import org.apache.commons.lang3.StringUtils;
import com.jmj.bank.utils.validations.Preconditions;

public class Country {

	private final String name;

	public Country(String name) {
		this.name = name;
		validate();
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object otherCountry) {
		if (this == otherCountry) {
			return true;
		}
		if (!(otherCountry instanceof Country)) {
			return false;
		}
		Country country = (Country) otherCountry;
		return name.equals(country.name);
	}

	private void validate() {
		Preconditions.checkArgument(() -> !StringUtils.isEmpty(name), "Country name is mandatory");
	}
}
