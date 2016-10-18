package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public final class StockPrice implements Serializable {
	private static final long serialVersionUID = 6920472853042767550L;
	private static final int PRECISION = 5;
	private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

	private String symbol;
	private BigDecimal price;
	private BigDecimal currentPrice;
	private Date createDate;

	public StockPrice() {
	}

	public static StockPrice createCurrentPrice(String symbol, BigDecimal price) {
		return new StockPrice(symbol, price, BigDecimal.ONE, new Date());
	}

	public StockPrice(String symbol, BigDecimal price, BigDecimal change, Date createDate) {
		this.symbol = symbol;
		this.price = price.setScale(PRECISION, ROUNDING_MODE);
		this.currentPrice = change.setScale(PRECISION, ROUNDING_MODE);
		this.createDate = createDate;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public BigDecimal getCurrentPrice() {
		return this.currentPrice;
	}

	public BigDecimal getChangePercent() {
		return BigDecimal.valueOf(100.0).multiply(this.currentPrice.divide(this.price, PRECISION, ROUNDING_MODE).subtract(BigDecimal.ONE))
				.setScale(PRECISION, ROUNDING_MODE);
	}

	public Date getCreateDate() {
		return createDate;
	}
}

