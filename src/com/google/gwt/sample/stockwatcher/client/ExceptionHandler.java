package com.google.gwt.sample.stockwatcher.client;

public class ExceptionHandler {
	private StockWatcherMessages messages;
	private ExceptionListener exceptionListener;

	public ExceptionHandler(StockWatcherMessages messages, ExceptionListener stockWatcher) {
		this.messages = messages;
		this.exceptionListener = stockWatcher;
	}

	public void onException(Throwable caught) {
		String msg = caught.getMessage();

		if (caught instanceof BusinessException) {
			BusinessException.Type exceptionType = ((BusinessException) caught).getType();

			switch (exceptionType) {
			case NO_HISTORICAL_DATA:
				msg = messages.noHistoricalData();
				break;
			case HISTORICAL_DATA_LOAD_EXCEPTION :
				msg = messages.historicalDataLoadException();
				break;
			case NO_CURRENT_DATA :
				msg = messages.noCurrentData();
				break;
			case CURRENT_DATA_LOAD_EXCEPTION:
				msg = messages.currentDataLoadException();
				break;
			case INVALID_INPUT:
				msg = messages.invalidInput();
				break;
			default:
				throw new RuntimeException("Unknown exception type");
			}
		}

		exceptionListener.exceptionThrown(msg);
	}
}
