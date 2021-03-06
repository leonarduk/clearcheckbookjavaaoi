package com.leonarduk.clearcheckbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.leonarduk.clearcheckbook.dto.AbstractDataType;
import com.leonarduk.clearcheckbook.dto.AccountDataType;
import com.leonarduk.clearcheckbook.dto.CategoryDataType;
import com.leonarduk.clearcheckbook.dto.LimitDataType;
import com.leonarduk.clearcheckbook.dto.ReminderDataType;
import com.leonarduk.clearcheckbook.dto.TransactionDataType;
import com.leonarduk.clearcheckbook.file.NationwideFilePreprocessor;
import com.leonarduk.clearcheckbook.file.TransactionFilePreprocessor;
import com.leonarduk.utils.DateUtils;

public class ClearCheckBookClientIT {

	private static final Logger _logger = Logger
			.getLogger(ClearCheckBookClientIT.class);
	private ClearCheckBookHelper client;
	private final String categoriesFileName = "clientCategories.csv";
	private final String accountsFileName = "clientAccounts.csv";
	private final String limitsfileName = "clientLimits.csv";
	private final String remindersFileName = "clientReminders.csv";
	private final String transactionsFileName = "clientTransactions.csv";
	private String transactionsNationwideFileName = "src/main/resources/nationwide.csv";
	private String transactionstestFileName = "src/main/resources/filehandler_transactions.csv";

	@Before
	public void setUp() throws Exception {
		String user = "unittest_luk";
		String password = "unittest_luk";
		int numberOfConnections = 20;
		client = new ClearCheckBookHelper(user, password, numberOfConnections);
	}

	@Test
	public void testGetAccounts() {
		try {
			List<AccountDataType> accounts = this.client.getAccounts();
			_logger.info(accounts);
		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testGetAccounts", e);
			fail();
		}
	}

	@Test
	public void testGetTransactionsAccountDataType() {
		try {
			List<AccountDataType> accounts = this.client.getAccounts();
			List<TransactionDataType> transactionDataTypes = this.client
					.getTransactions(accounts.get(0));
			_logger.info(transactionDataTypes);
		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testGetTransactionsAccountDataType", e);
			fail();
		}
	}

	@Test
	public void testGetTransactions() {
		try {
			List<TransactionDataType> transactionDataTypes = this.client
					.getTransactions();
			_logger.info(transactionDataTypes);
		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testGetTransactions", e);
			fail();
		}
	}

	@Test
	public void testGetCategories() {
		try {
			List<CategoryDataType> categoryDataTypes = this.client
					.getCategories();
			_logger.info(categoryDataTypes);
		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testGetCategories", e);
			fail();
		}
	}

	@Test
	public void testExportAccounts() {
		try {
			List<AccountDataType> accounts = this.client.getAccounts();
			this.client.exportAccounts(accountsFileName, accounts);
			_logger.info(accounts);
		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testExportAccounts", e);
			fail();
		}
	}

	@Test
	public void testExportCategories() {
		try {
			List<CategoryDataType> categoryDataTypes = this.client
					.getCategories();
			this.client.exportCategories(categoriesFileName, categoryDataTypes);
			_logger.info(categoryDataTypes);
		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testExportCategories", e);
			fail();
		}
	}

	@Test
	public void testExportLimits() {
		try {
			List<LimitDataType> limits = this.client.getLimits();
			this.client.exportLimits(limitsfileName, limits);
			_logger.info(limits);
		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testExportLimits", e);
			fail();
		}
	}

	@Test
	public void testExportReminders() {
		try {
			List<ReminderDataType> reminders = this.client.getReminders();
			this.client.exportReminders(remindersFileName, reminders);
			_logger.info(reminders);
		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testExportReminders", e);
			fail();
		}
	}

	@Test
	public void testExportTransactions() {
		try {
			List<TransactionDataType> transactions = this.client
					.getTransactions();
			this.client.exportTransactions(transactionsFileName, transactions);
		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testExportTransactions", e);
			fail();
		}
	}

	@Test
	public void testImportTransactions() {
		try {
			List<TransactionDataType> actual = this.client
					.importTransactions(transactionstestFileName,
							new TransactionFilePreprocessor());
			assertEquals(2, actual.size());

		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testExportTransactions", e);
			fail();
		}
	}

	@Test
	public void testBulkUpdate() {
		try {
			List<TransactionDataType> original = this.client.getTransactions();
			List<TransactionDataType> changeList = new LinkedList<>(original);
			TransactionDataType editType = new TransactionDataType(
					changeList.get(1));
			editType.setDescription("updated " + DateUtils.getNowyyyyMMddHHmm());
			TransactionDataType deleteOne = new TransactionDataType(
					original.get(2));
			deleteOne.markToBeDeleted();
			changeList.set(0, editType);
			changeList.set(1, deleteOne);
			List<String> returnStatus = this.client.processTransactions(
					original, this.client.getTransactions());
			_logger.info(returnStatus);
			assertEquals(2, returnStatus.size());
			assertTrue(returnStatus.get(0).contains("Edited"));
			assertTrue(returnStatus.get(1).contains("Deleted"));
		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testExportTransactions", e);
			fail();
		}
	}

	@Test
	public void testBulkUpdateInParallel() {
		try {
			List<TransactionDataType> original = this.client.getTransactions();
			List<TransactionDataType> changeList = new LinkedList<>(original);
			TransactionDataType editType = new TransactionDataType(
					changeList.get(1));
			// this needs to be different from the testBulkUpdate()
			// setDescription call
			editType.setDescription("parallel update "
					+ DateUtils.getNowyyyyMMddHHmm());
			TransactionDataType deleteOne = new TransactionDataType(
					original.get(2));
			deleteOne.markToBeDeleted();
			changeList.set(0, editType);
			changeList.set(1, deleteOne);
			List<String> returnStatus = this.client
					.processTransactionsInParallel(original,
							this.client.getTransactions());
			_logger.info(returnStatus);
			assertEquals("Returned " + returnStatus, 2, returnStatus.size());
			if (returnStatus.get(0).contains("Edited")) {
				assertTrue(returnStatus.get(1).contains("Deleted"));

			} else if (returnStatus.get(1).contains("Edited")) {
				assertTrue(returnStatus.get(0).contains("Deleted"));

			} else {
				fail("Results not as expected");
			}

		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testExportTransactions", e);
			fail();
		}
	}

	@Test
	public void testBulkUpdateNationwide() {
		try {
			List<TransactionDataType> file = this.client.importTransactions(
					transactionsNationwideFileName,
					new NationwideFilePreprocessor());
			file.get(1).setDescription(
					"updated " + DateUtils.getNowyyyyMMddHHmm());
			List<String> results = this.client.processTransactions(file);
			assertEquals(3, results.size());
			for (String result : results) {
				assertTrue(result.contains("Inserted"));
			}
			_logger.info(results);

		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testExportTransactions", e);
			fail();
		}
	}

	@Test
	public void testBulkUpdateNationwideInParallel() {
		try {
			List<TransactionDataType> file = this.client.importTransactions(
					transactionsNationwideFileName,
					new NationwideFilePreprocessor());
			file.get(1).setDescription(
					"updated " + DateUtils.getNowyyyyMMddHHmm());
			List<String> results = this.client
					.processTransactionsInParallel(file);
			assertEquals(3, results.size());
			for (String result : results) {
				assertTrue(result.contains("Inserted"));
			}
			_logger.info(results);

		} catch (ClearcheckbookException e) {
			_logger.fatal("Failed to testExportTransactions", e);
			fail();
		}
	}

	private void compareTransactionList(List<TransactionDataType> expected,
			List<TransactionDataType> actual) {
		assertEquals(expected.size(), actual.size());
		_logger.info("testImportTransactions: " + actual.size() + ": " + actual);
		Map<Long, AbstractDataType<?>> expectedMap = new HashMap<>();
		for (Iterator<TransactionDataType> iterator = expected.iterator(); iterator
				.hasNext();) {
			TransactionDataType next = iterator.next();
			expectedMap.put(next.getId(), next);
		}
		for (Iterator<TransactionDataType> iterator = actual.iterator(); iterator
				.hasNext();) {
			TransactionDataType transactionDataType = iterator.next();
			_logger.debug("comparing " + transactionDataType);
			assertTrue(expectedMap.containsKey(transactionDataType.getId()));
			AbstractDataType<?> expectedValue = expectedMap
					.get(transactionDataType.getId());
			assertTrue(expectedValue.equals(transactionDataType));
		}
	}

}
