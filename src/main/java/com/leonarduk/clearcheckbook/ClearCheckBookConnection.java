package com.leonarduk.clearcheckbook;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.leonarduk.clearcheckbook.calls.AbstractCall;
import com.leonarduk.clearcheckbook.calls.AccountCall;
import com.leonarduk.clearcheckbook.calls.CategoryCall;
import com.leonarduk.clearcheckbook.calls.LimitCall;
import com.leonarduk.clearcheckbook.calls.PremiumCall;
import com.leonarduk.clearcheckbook.calls.ReminderCall;
import com.leonarduk.clearcheckbook.calls.ReportCall;
import com.leonarduk.clearcheckbook.calls.TransactionCall;
import com.leonarduk.clearcheckbook.calls.UserCall;
import com.leonarduk.clearcheckbook.dto.ParsedNameValuePair;
import com.leonarduk.utils.HtmlUnitUtils;

public class ClearCheckBookConnection {

	final private String password;
	final private String userName;
	public final String baseurl = "https://www.clearcheckbook.com/api/";

	private AccountCall accountCall;

	private CategoryCall categoryCall;
	private LimitCall limitCall;
	private PremiumCall premiumCall;
	private ReminderCall reminderCall;
	private ReportCall reportCall;
	private TransactionCall transactionCall;
	private UserCall userCall;

	private static final Logger _logger = Logger.getLogger(AbstractCall.class);

	public ClearCheckBookConnection(String userName, String password) {
		this.userName = userName;
		this.password = password;

		this.accountCall = new AccountCall(this);
		this.categoryCall = new CategoryCall(this);
		this.limitCall = new LimitCall(this);
		this.premiumCall = new PremiumCall(this);
		this.reminderCall = new ReminderCall(this);
		this.reportCall = new ReportCall(this);
		this.transactionCall = new TransactionCall(this);
		this.userCall = new UserCall(this);

	}


	public AccountCall account() {
		return accountCall;
	}

	public CategoryCall category() {
		return categoryCall;
	}

	public LimitCall limit() {
		return limitCall;
	}

	public PremiumCall premium() {
		return premiumCall;
	}

	public ReminderCall reminder() {
		return reminderCall;
	}

	public ReportCall report() {
		return reportCall;
	}

	public TransactionCall transaction() {
		return transactionCall;
	}

	public UserCall user() {
		return userCall;
	}

	public String getUserName() {
		return userName;
	}

	public String getBaseurl() {
		return baseurl;
	}

	/**
	 * 
	 * @param url
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public String getPage(String url, ParsedNameValuePair... parameters)
			throws IOException {
		_logger.debug("URL:" + getFullUrl(url));
		HtmlPage page = HtmlUnitUtils.getPage(getFullUrl(url), HttpMethod.GET,
				userName, password, parameters);
		return page.asText();
	}

	/**
	 * 
	 * @param url
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public String postPage(String url, ParsedNameValuePair... parameters)
			throws IOException {
		String fullPath = getFullUrl(url);
		_logger.debug("URL:" + fullPath + " " + parameters);
		HtmlPage page = HtmlUnitUtils.getPage(fullPath, HttpMethod.POST,
				userName, password, parameters);
		return page.asText();
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	private String getFullUrl(String url) {
		String fullPath = "https://" + userName + ":" + password
				+ "@www.clearcheckbook.com/api/" + url;
		return fullPath;
	}

	/**
	 * 
	 * @param url
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public String putPage(String url, ParsedNameValuePair... parameters)
			throws IOException {
		url += "?";
		for (int i = 0; i < parameters.length; i++) {
			url += parameters[i].getName() + "=" + parameters[i].getValue()
					+ "&";
		}
		String fullUrl = getFullUrl(url);

		_logger.debug("URL:" + fullUrl);
		HtmlPage page = HtmlUnitUtils.getPage(fullUrl, HttpMethod.PUT,
				userName, password, parameters);
		return page.asText();
	}

	/**
	 * 
	 * @param url
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public String deletePage(String url, ParsedNameValuePair... parameters)
			throws IOException {
		_logger.debug("URL:" + this.baseurl + url);
		HtmlPage page = HtmlUnitUtils.getPage(this.baseurl + url,
				HttpMethod.DELETE, userName, password, parameters);
		return page.asText();
	}

}
