package com.jmj.bank.domain.clock;

import java.time.Instant;
import java.time.LocalDateTime;

public interface Clock {

	LocalDateTime now();

	Instant timeStamp();
}
