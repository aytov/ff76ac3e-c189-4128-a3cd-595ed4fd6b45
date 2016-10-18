package com.google.gwt.sample.stockwatcher.server.impl;

import java.util.Date;
import java.util.List;

import com.google.gwt.sample.stockwatcher.client.BusinessException;
import com.google.gwt.sample.stockwatcher.client.StockPrice;
import com.google.gwt.sample.stockwatcher.client.StockPriceService;
import com.google.gwt.sample.stockwatcher.server.StockPriceRepository;
import com.google.gwt.sample.stockwatcher.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {
	private static final long serialVersionUID = 6962781587812575499L;
	private StockPriceRepository stockPricesRepository = new StockPriceInMemoryRepositoryImpl(
			new StockPriceDataProviderImpl());

	@Override
	public List<StockPrice> getPrices(List<String> currencyCodes, Date date) throws BusinessException {
		//validation
		if (date == null || !validate(currencyCodes)) {
			throw new BusinessException(BusinessException.Type.INVALID_INPUT);
		}

		return stockPricesRepository.getHistoricalDataBy(currencyCodes, date);
	}

	@Override
	public List<String> getCurrencyCodes() throws BusinessException {
		return stockPricesRepository.getCurrencyCodes();
	}

	private boolean validate(List<String> currencyCodes) {
		if (currencyCodes == null) {
			return false;
		}

		for (String currencyCode : currencyCodes) {
			if (!FieldVerifier.isValidName(currencyCode)) {
				return false;
			}
		}

		return true;
	}

	public void setStockPricesRepository(StockPriceRepository stockPricesRepository) {
		this.stockPricesRepository = stockPricesRepository;
	}
}