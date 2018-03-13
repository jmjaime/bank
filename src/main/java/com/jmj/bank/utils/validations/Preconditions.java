package com.jmj.bank.utils.validations;

import java.util.function.BooleanSupplier;

public class Preconditions {

	public static void checkArgumentNotNull(Object objectToCheck, String errorMsg) {
		checkArgument(() -> (objectToCheck != null), errorMsg);
	}

	public static void checkArgument(BooleanSupplier expression, String errorMsg) {
		if (!expression.getAsBoolean()) {
			throw new IllegalArgumentException(errorMsg);
		}
	}
}
