package com.google.gwt.sample.stockwatcher;

import com.google.gwt.junit.client.GWTTestCase;

public class StockWatcherTest extends GWTTestCase {                       // <span style="color:black;">**(1)**</span>

	/**
	 * Must refer to a valid module that sources this class.
	 */
	public String getModuleName() {                                         // <span style="color:black;">**(2)**</span>
		return "com.google.gwt.sample.stockwatcher.StockWatcher";
	}

	/**
	 * Add as many tests as you like.
	 */
	public void testSimple() {                                              // <span style="color:black;">**(3)**</span>
		assertTrue(true);
	}

}
