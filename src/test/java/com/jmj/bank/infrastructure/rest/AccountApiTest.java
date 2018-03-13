package com.jmj.bank.infrastructure.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jmj.bank.application.BankApplication;
import com.jmj.bank.domain.account.DefaultAccount;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = { BankApplication.class})
@ExtendWith(SpringExtension.class)
public class AccountApiTest {

	@LocalServerPort
	int port;

	@Test
	void whenRequestDefaultAcountShouldReturnIt() {
		given().port(port).basePath("/api").when().get("/account").
				then().statusCode(200).assertThat().body("id", equalTo(DefaultAccount.DEFAULT_ACCOUNT));
	}
}
