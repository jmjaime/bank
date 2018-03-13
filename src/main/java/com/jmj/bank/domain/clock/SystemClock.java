package com.jmj.bank.domain.clock;

import java.time.Instant;
import java.time.LocalDateTime;

public class SystemClock implements Clock {
	@Override
	public LocalDateTime now() {
		return LocalDateTime.now();
	}

	@Override
	public Instant timeStamp() {
		return Instant.now();
	}
}
