package com.google.gwt.sample.stockwatcher.server;

import java.util.Map;

import com.google.gwt.sample.stockwatcher.client.StockPrice;

public interface StockPriceDataProvider {
	/**
	 * Extracts historical data for the stock prices.
	 * @return
	 */
	Map<String, Map<String, StockPrice>> getHistoricalData();

	/**
	 * Extracts the current stock prices.
	 * @return
	 */
	Map<String, StockPrice> getCurrentData();
}
