package com.google.gwt.sample.stockwatcher.server.impl;

import java.io.File;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.sample.stockwatcher.client.BusinessException;
import com.google.gwt.sample.stockwatcher.client.StockPrice;
import com.google.gwt.sample.stockwatcher.client.StockPriceService;
import com.google.gwt.sample.stockwatcher.server.DateUtils;
import com.google.gwt.sample.stockwatcher.server.StockPriceDataProvider;
import com.google.gwt.sample.stockwatcher.server.StockPriceRepository;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class StockPriceServiceImplTest {
	private StockPriceService stockPriceService = getInstance();

	@Test
	public void testGetPrices() throws ParseException {
		String[] codes = {"GBP", "CHF"};
		Date date = DateUtils.strToDate("14.10.2016");
		List<StockPrice> stockPrices = stockPriceService.getPrices(Arrays.asList(codes), date);

		assertFalse(stockPrices.isEmpty());
	}

	@Test(expected = BusinessException.class)
	public void testGetPricesWithNoHistoricalData() throws ParseException {
		String[] codes = {"GBP", "CHF"};
		Date date = DateUtils.strToDate("20.10.2017");
		List<StockPrice> stockPrices = stockPriceService.getPrices(Arrays.asList(codes), date);
	}

	@Test
	public void testCurrencyCodes() throws ParseException {
		List<String> currencyCodes = stockPriceService.getCurrencyCodes();
		assertFalse(currencyCodes.isEmpty());
	}

	private StockPriceService getInstance(){
		File dataRoot = new File("C:/Users/aytov/JAVA_HOME/workspace/stockWatcher/data");
		StockPriceDataProvider stockPriceDataProvider = new StockPriceDataProviderImpl(dataRoot);
		StockPriceRepository stockPriceRepository = new StockPriceInMemoryRepositoryImpl(stockPriceDataProvider);

		return new StockPriceServiceImpl(stockPriceRepository);
	}
}
