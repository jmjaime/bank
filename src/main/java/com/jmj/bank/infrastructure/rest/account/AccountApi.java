package com.jmj.bank.infrastructure.rest.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jmj.bank.domain.account.action.AccountDTO;
import com.jmj.bank.domain.account.action.FindDefaultAccount;
import com.jmj.bank.infrastructure.rest.account.response.AccountResponse;

@RestController
public class AccountApi {

	private final FindDefaultAccount findDefaultAccount;

	public AccountApi(FindDefaultAccount findDefaultAccount) {
		this.findDefaultAccount = findDefaultAccount;
	}

	@RequestMapping(value = "/api/account", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public AccountResponse defaultAccount() {
		AccountDTO accountDTO = findDefaultAccount.execute();
		return toResponse(accountDTO);
	}

	private AccountResponse toResponse(AccountDTO accountDTO) {
		return new AccountResponse(accountDTO.getId(), accountDTO.getBank(), accountDTO.getCountry(), accountDTO.getBalance());
	}
}
