package com.google.gwt.sample.stockwatcher.server.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.sample.stockwatcher.client.BusinessException;
import com.google.gwt.sample.stockwatcher.client.StockPrice;
import com.google.gwt.sample.stockwatcher.server.DateUtils;
import com.google.gwt.sample.stockwatcher.server.StockPriceDataProvider;
import com.google.gwt.sample.stockwatcher.server.StockPriceRepository;

public class StockPriceInMemoryRepositoryImpl implements StockPriceRepository {
	private List<String> currencyCodes = Collections.emptyList();
	private Map<String, Map<String, StockPrice>> historicalData = Collections.emptyMap();

	public StockPriceInMemoryRepositoryImpl(StockPriceDataProvider dataProvider) {
		this.historicalData = dataProvider.getHistoricalData();
		this.currencyCodes = new ArrayList<>();

		for (String currencyCode : dataProvider.getCurrentData().keySet()) {
			if (!currencyCodes.contains(currencyCode)) {
				currencyCodes.add(currencyCode);
			}
		}
	}

	@Override
	public List<String> getCurrencyCodes() {
		return this.currencyCodes;
	}

	@Override
	public List<StockPrice> getHistoricalDataBy(List<String> currencyCodes, Date date) {
		Map<String, StockPrice> stockPriceMap = historicalData.get(asDateKey(date));

		if(stockPriceMap == null){
			throw new BusinessException(BusinessException.Type.NO_HISTORICAL_DATA);
		}

		ArrayList<StockPrice> stockPrices = new ArrayList<>();
		for (String currencyCode : currencyCodes) {
			stockPrices.add(stockPriceMap.get(currencyCode));
		}

		return stockPrices;
	}

	private String asDateKey(Date date){
		try{
			return DateUtils.dateToStr(date);
		} catch (ParseException e) {
			throw new BusinessException(BusinessException.Type.INVALID_INPUT);
		}
	}
}
