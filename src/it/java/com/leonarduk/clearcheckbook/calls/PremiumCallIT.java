package com.leonarduk.clearcheckbook.calls;

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.leonarduk.clearcheckbook.ClearCheckBookConnectionIT;
import com.leonarduk.clearcheckbook.ClearcheckbookException;
import com.leonarduk.clearcheckbook.dto.PremiumDataType;

public class PremiumCallIT {

	private static final Logger _logger = Logger
			.getLogger(PremiumCallIT.class);

	private PremiumCall call;

	@Before
	public void setUp() throws Exception {
		this.call = new PremiumCall(
				ClearCheckBookConnectionIT.getTestConnection());

	}

	@Test
	public void testGetAll() {
		PremiumDataType premiumDataType;
		try {
			premiumDataType = this.call.get();
			_logger.info(premiumDataType);
		} catch (ClearcheckbookException e) {
			_logger.error("Failed to getAll", e);
			fail();
		}
	}

}
