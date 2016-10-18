package com.google.gwt.sample.stockwatcher.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("stockPrices")
public interface StockPriceService extends RemoteService {
	/**
	 * Extracts stock prices by criteria.
	 * @param currencyCodes
	 * @param date
	 * @return
	 * @throws BusinessException
	 */
	List<StockPrice> getPrices(List<String> currencyCodes, Date date) throws BusinessException;

	/**
	 * Extracts all currency codes
	 * @return
	 * @throws BusinessException
	 */
	List<String> getCurrencyCodes() throws BusinessException;
}

