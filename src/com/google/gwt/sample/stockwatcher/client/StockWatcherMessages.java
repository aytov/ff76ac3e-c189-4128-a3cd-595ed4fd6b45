package com.google.gwt.sample.stockwatcher.client;

import java.util.Date;

import com.google.gwt.i18n.client.Messages;

public interface StockWatcherMessages extends Messages {
	@DefaultMessage("Last update: {0,date,medium} {0,time,medium}")
	String lastUpdate(Date timestamp);

	@DefaultMessage("No historical data for this day!")
	String noHistoricalData();

	@DefaultMessage("Problem with loading historical data!")
	String historicalDataLoadException();

	@DefaultMessage("No current data!")
	String noCurrentData();

	@DefaultMessage("Problem with loading current data!")
	String currentDataLoadException();

	@DefaultMessage("Invalid input!")
	String invalidInput();
}