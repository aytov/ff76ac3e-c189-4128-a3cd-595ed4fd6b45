package com.google.gwt.sample.stockwatcher.client;

public interface ExceptionListener {
	/**
	 * Triggers update when exception occurs.
	 * @param msg of the exception.
	 */
	void exceptionThrown(String msg);
}
