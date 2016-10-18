package com.google.gwt.sample.stockwatcher.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StockPriceServiceAsync {
	void getPrices(List<String> currencyCodes, Date date, AsyncCallback<List<StockPrice>> async);

	void getCurrencyCodes(AsyncCallback<List<String>> async);
}