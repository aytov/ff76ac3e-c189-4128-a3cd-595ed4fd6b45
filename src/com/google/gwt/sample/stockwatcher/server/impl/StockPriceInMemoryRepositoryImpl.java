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
		loadData(dataProvider);
	}

	private void loadData(StockPriceDataProvider dataProvider) {
		try {
			Map<String, StockPrice> currentData = dataProvider.getCurrentData();
			this.historicalData = dataProvider.getHistoricalData(currentData);
			this.currencyCodes = new ArrayList<>();

			for (String currencyCode : currentData.keySet()) {
				if (!currencyCodes.contains(currencyCode)) {
					currencyCodes.add(currencyCode);
				}
			}
		} catch (BusinessException be) {
			if (be.getType().equals(BusinessException.Type.CURRENT_DATA_LOAD_EXCEPTION) || be.getType()
					.equals(BusinessException.Type.HISTORICAL_DATA_LOAD_EXCEPTION)) {
				//do nothing
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

		if (stockPriceMap == null) {
			throw new BusinessException(BusinessException.Type.NO_HISTORICAL_DATA);
		}

		ArrayList<StockPrice> stockPrices = new ArrayList<>();
		for (String currencyCode : currencyCodes) {
			StockPrice stockPrice = stockPriceMap.get(currencyCode);

			if(stockPrice != null){
				stockPrices.add(stockPrice);
			}else{
				//TODO maybe rise BusinessException.Type.INVALID_INPUT
			}
		}

		return stockPrices;
	}

	private String asDateKey(Date date) {
		try {
			return DateUtils.dateToStr(date);
		} catch (ParseException e) {
			throw new BusinessException(BusinessException.Type.INVALID_INPUT);
		}
	}
}
