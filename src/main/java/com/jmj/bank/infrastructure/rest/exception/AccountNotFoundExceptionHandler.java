package com.jmj.bank.infrastructure.rest.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jmj.bank.domain.account.exception.AccountNotFoundException;
import com.jmj.bank.domain.transaction.exception.ServiceUnavailableException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class AccountNotFoundExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({AccountNotFoundException.class, IllegalArgumentException.class})
	public final ResponseEntity handleUserNotFoundException(RuntimeException ex, WebRequest request) {
		String bodyResponse = ex.getMessage();
		return new ResponseEntity<>(bodyResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ServiceUnavailableException.class)
	public final ResponseEntity handleUserNotFoundException(ServiceUnavailableException ex, WebRequest request) {
		String bodyResponse = ex.getMessage();
		return new ResponseEntity<>(bodyResponse, HttpStatus.SERVICE_UNAVAILABLE);
	}
}
