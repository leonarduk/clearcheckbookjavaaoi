package com.leonarduk.clearcheckbook.file;

import java.util.Map;

import com.leonarduk.clearcheckbook.ClearcheckbookException;
import com.leonarduk.clearcheckbook.dto.TransactionDataType;

public class TransactionFilePreprocessor implements FilePreProcessor {

	private Long defaultAccountId;
	private int rowsToSkip;

	/* (non-Javadoc)
	 * @see com.leonarduk.clearcheckbook.FilePreProcessor#getRowsToSkip()
	 */
	@Override
	public int getRowsToSkip() {
		return rowsToSkip;
	}

	public TransactionFilePreprocessor() {
		this(0);
	}

	public TransactionFilePreprocessor(int rowsToSkip) {
		this(rowsToSkip, 0L);
	}

	public TransactionFilePreprocessor(Long accountId) {
		this(0, accountId);
	}

	public TransactionFilePreprocessor(int rowsToSkip, Long accountId) {
		this.defaultAccountId = accountId;
		this.rowsToSkip = rowsToSkip;
	}

	/* (non-Javadoc)
	 * @see com.leonarduk.clearcheckbook.FilePreProcessor#processRow(java.util.Map)
	 */
	@Override
	public Map<String, String> processRow(Map<String, String> fieldsMap)
			throws ClearcheckbookException {
		fieldsMap.put(TransactionDataType.Fields.DATE.name().toLowerCase(),
				getDate(fieldsMap));
		fieldsMap.put(TransactionDataType.Fields.AMOUNT.name().toLowerCase(),
				getAmount(fieldsMap));
		fieldsMap.put(TransactionDataType.Fields.DESCRIPTION.name()
				.toLowerCase(), getDesription(fieldsMap));
		fieldsMap.put(
				TransactionDataType.Fields.CHECK_NUM.name().toLowerCase(),
				getCheckNum(fieldsMap));
		fieldsMap.put(TransactionDataType.Fields.MEMO.name().toLowerCase(),
				getMemo(fieldsMap));
		fieldsMap.put(TransactionDataType.Fields.PAYEE.name().toLowerCase(),
				getPayee(fieldsMap));
		fieldsMap.put(TransactionDataType.Fields.ACCOUNT_ID.name()
				.toLowerCase(), getAccountId(fieldsMap));

		return fieldsMap;
	}

	protected String getAccountId(Map<String, String> fieldsMap) {
		String accountId = fieldsMap.get(TransactionDataType.Fields.ACCOUNT_ID
				.name().toLowerCase());
		if (null == accountId || accountId.equals("0")) {
			accountId = String.valueOf(this.defaultAccountId);
		}
		return accountId;
	}

	protected String getPayee(Map<String, String> fieldsMap) {
		return fieldsMap.get(TransactionDataType.Fields.PAYEE.name()
				.toLowerCase());
	}

	protected String getMemo(Map<String, String> fieldsMap) {
		return fieldsMap.get(TransactionDataType.Fields.MEMO.name()
				.toLowerCase());
	}

	protected String getCheckNum(Map<String, String> fieldsMap) {
		return fieldsMap.get(TransactionDataType.Fields.CHECK_NUM.name()
				.toLowerCase());
	}

	protected String getDesription(Map<String, String> fieldsMap) {
		return fieldsMap.get(TransactionDataType.Fields.DESCRIPTION.name()
				.toLowerCase());
	}

	protected String getAmount(Map<String, String> fieldsMap) {
		return fieldsMap.get(TransactionDataType.Fields.AMOUNT.name()
				.toLowerCase());
	}

	protected String getDate(Map<String, String> fieldsMap)
			throws ClearcheckbookException {
		return fieldsMap.get(TransactionDataType.Fields.DATE.name()
				.toLowerCase());
	}

	/**
	 * Helper function to remove all but numbers and periods
	 * 
	 * @param nextValue
	 * @return
	 */
	public double getDouble(String nextValue) {
		if (!nextValue.isEmpty()) {
			String number = nextValue.replaceAll("[^0-9. ]", "");
			return Double.valueOf(number);
		}
		return 0;
	}
}
