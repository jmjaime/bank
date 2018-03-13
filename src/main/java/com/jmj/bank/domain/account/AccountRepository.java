package com.jmj.bank.domain.account;

import java.util.List;

public interface AccountRepository {

	Account find(AccountId accountId);

	void saveAccountMovements(AccountId accountId, List<Movement> movements);

}
