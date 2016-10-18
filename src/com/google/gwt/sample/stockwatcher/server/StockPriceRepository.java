package com.google.gwt.sample.stockwatcher.server;

import java.util.Date;
import java.util.List;

import com.google.gwt.sample.stockwatcher.client.StockPrice;

public interface StockPriceRepository {
	/**
	 * Extracts all currency codes.
	 * @return
	 */
	List<String> getCurrencyCodes();

	/**
	 * Filters historical stock prices data by criteria.
	 * @param currencyCodes
	 * @param date
	 * @return
	 */
	List<StockPrice> getHistoricalDataBy(List<String> currencyCodes, Date date);
}
