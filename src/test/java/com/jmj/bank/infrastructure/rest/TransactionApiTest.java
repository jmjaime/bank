package com.jmj.bank.infrastructure.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jmj.bank.application.BankApplication;
import com.jmj.bank.domain.account.DefaultAccount;
import com.jmj.bank.infrastructure.rest.transaction.TransactionApi;
import com.jmj.bank.infrastructure.rest.transaction.request.TransactionRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { BankApplication.class })
@ExtendWith(SpringExtension.class)
public class TransactionApiTest {

	@LocalServerPort
	int port;

	@Test
	void whenDoTransactionShouldBeRegistered() {
		TransactionRequest bodyRequest = createBody("16678/67");
		given().port(port).basePath("/api").body(bodyRequest).contentType("application/json").when().post("/transaction").
				then().statusCode(200).assertThat().body("result", equalTo(TransactionApi.TRANSACTION_REGISTERED_OK));
	}

	@Test
	void whenDoTransactionWithInvalidAccountShouldFails() {
		TransactionRequest bodyRequest = createBody("INVALID_ACCOUNT");
		given().port(port).basePath("/api").body(bodyRequest).contentType("application/json").when().post("/transaction").
				then().statusCode(400).assertThat().body(equalTo("Account INVALID_ACCOUNT not found"));
	}

	private TransactionRequest createBody(String accountTo) {
		return new TransactionRequest(DefaultAccount.DEFAULT_ACCOUNT, accountTo, new BigDecimal(10));
	}

}
