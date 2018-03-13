package com.jmj.bank.utils.threads;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.RejectedExecutionException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TransactionRunnerPoolTest {

	private TransactionRunnerPool transactionRunnerPool;

	@Test
	void whenPoolIsExhaustedShouldRejectNewTask(){
		givenExhaustedPool();
		Throwable exception = assertThrows(RejectedExecutionException.class, ()->onExecuteNewTasks(5));
		thenShouldFailByRejectedExecution(exception);
	}

	private void thenShouldFailByRejectedExecution(Throwable exception) {
		Assertions.assertEquals("Transaction processor exhausted", exception.getMessage());
	}

	private void givenExhaustedPool(){
		transactionRunnerPool = new TransactionRunnerPool(3,2,1);
	}
	private void onExecuteNewTasks(int amountTask) {
		for (int i = 0; i < amountTask; i++) {
			transactionRunnerPool.execute(createTask());
		}
	}

	private Runnable createTask() {
		return () -> {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}
		};
	}

}
