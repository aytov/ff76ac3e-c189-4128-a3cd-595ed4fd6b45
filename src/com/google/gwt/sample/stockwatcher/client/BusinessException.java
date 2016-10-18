package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

public class BusinessException extends RuntimeException implements Serializable {
	private static final long serialVersionUID = -6400578537246784917L;
	private Type type;

	public BusinessException() {
	}

	public BusinessException(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public enum Type{
		NO_HISTORICAL_DATA, HISTORICAL_DATA_LOAD_EXCEPTION, NO_CURRENT_DATA, CURRENT_DATA_LOAD_EXCEPTION, INVALID_INPUT
	}
}
