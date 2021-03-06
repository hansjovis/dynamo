package com.ocs.dynamo.utils;

import org.junit.Assert;
import org.junit.Test;

public class SystemPropertyUtilsTest {

	@Test
	public void testDefaults() {
		Assert.assertEquals("€", SystemPropertyUtils.getDefaultCurrencySymbol());
		Assert.assertEquals("dd-MM-yyyy", SystemPropertyUtils.getDefaultDateFormat());
		Assert.assertEquals("dd-MM-yyyy HH:mm:ss", SystemPropertyUtils.getDefaultDateTimeFormat());
		Assert.assertEquals(2, SystemPropertyUtils.getDefaultDecimalPrecision());
		Assert.assertEquals(3, SystemPropertyUtils.getDefaultListSelectRows());
		Assert.assertEquals("HH:mm:ss", SystemPropertyUtils.getDefaultTimeFormat());

		Assert.assertEquals(false, SystemPropertyUtils.allowTableExport());
		Assert.assertEquals("\"", SystemPropertyUtils.getExportCsvQuoteChar());
		Assert.assertEquals(";", SystemPropertyUtils.getExportCsvSeparator());

		Assert.assertEquals(15_000, SystemPropertyUtils.getMaximumExportRowsNonStreaming());
		Assert.assertEquals(100_000, SystemPropertyUtils.getMaximumExportRowsStreaming());
		Assert.assertEquals(30_000, SystemPropertyUtils.getMaximumExportRowsStreamingPivot());

		Assert.assertEquals(false, SystemPropertyUtils.useThousandsGroupingInEditMode());

		Assert.assertEquals("de", SystemPropertyUtils.getDefaultLocale());
	}
}
