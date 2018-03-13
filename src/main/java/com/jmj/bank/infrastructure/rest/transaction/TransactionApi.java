package com.jmj.bank.infrastructure.rest.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jmj.bank.domain.transaction.action.Transfer;
import com.jmj.bank.domain.transaction.action.TransferData;
import com.jmj.bank.infrastructure.rest.transaction.Response.TransactionResponse;
import com.jmj.bank.infrastructure.rest.transaction.request.TransactionRequest;

@RestController
public class TransactionApi {

	public static final String TRANSACTION_REGISTERED_OK =
			"La transaccion a sido registrada correctamente, la vera reflejada en su saldo en breve (S.E.U.O.)";
	private final Transfer transfer;

	public TransactionApi(Transfer transfer) {
		this.transfer = transfer;
	}

	@RequestMapping(value = "/api/transaction", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TransactionResponse transfer(@RequestBody TransactionRequest transactionRequest) {
		TransferData transferData = new TransferData(transactionRequest.getAccountFrom(), transactionRequest.getAccountTo(),
				transactionRequest.getAmount());
		transfer.execute(transferData);
		return new TransactionResponse(TRANSACTION_REGISTERED_OK);
	}
}
