package com.google.gwt.sample.stockwatcher.server.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.sample.stockwatcher.client.BusinessException;
import com.google.gwt.sample.stockwatcher.client.StockPrice;
import com.google.gwt.sample.stockwatcher.server.DateUtils;
import com.google.gwt.sample.stockwatcher.server.FileUtils;
import com.google.gwt.sample.stockwatcher.server.StockPriceDataProvider;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class StockPriceDataProviderImpl implements StockPriceDataProvider {
	private static final String DATA_ROOT = "/data";
	private static final String DATA_EXTENSION = ".csv";
	private static Logger Log = Logger.getLogger("StockPriceDataProviderImpl");

	@Override
	public Map<String, Map<String, StockPrice>> getHistoricalData() {
		try {
			return loadHistoricalData(getDataRoot(), getCurrentData());
		} catch (IOException | ParseException e) {
			throw new BusinessException(BusinessException.Type.HISTORICAL_DATA_LOAD_EXCEPTION);
		}
	}

	private File getDataRoot() {
		ClassLoader classLoader = StockPriceDataProviderImpl.class.getClassLoader();
		File dataRoot = new File(classLoader.getResource(DATA_ROOT).getPath());

		Log.info("Base path is:" + dataRoot.getAbsolutePath());

		if (!dataRoot.exists()) {
			throw new BusinessException(BusinessException.Type.HISTORICAL_DATA_LOAD_EXCEPTION);
		}

		return dataRoot;
	}

	@Override
	public Map<String, StockPrice> getCurrentData() {
		try {
			String fileName = DateUtils.dateToStr(new Date()) + DATA_EXTENSION;
			File currentDataFile = new File(getDataRoot().getAbsolutePath() + "/" + fileName);

			return loadCurrentData(currentDataFile);
		} catch (IOException|ParseException e) {
			throw new BusinessException(BusinessException.Type.CURRENT_DATA_LOAD_EXCEPTION);
		}
	}

	private Map<String, StockPrice> loadCurrentData(File path) throws IOException {
		Map<String, StockPrice> stockPrices = new HashMap<>(4);

		Reader in = new FileReader(path);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

		for (CSVRecord record : records) {
			String currencyCode = record.get(1);
			BigDecimal priceInHKD = new BigDecimal(record.get(2));
			StockPrice stockPrice = StockPrice.createCurrentPrice(currencyCode, priceInHKD);
			stockPrices.put(stockPrice.getSymbol(), stockPrice);
		}

		return stockPrices;
	}

	private Map<String, Map<String, StockPrice>> loadHistoricalData(File dataRoot, Map<String, StockPrice> currentPrices)
			throws IOException, ParseException {
		Map<String, Map<String, StockPrice>> stockPriceByDate = new HashMap<>(4);

		for (final File fileEntry : dataRoot.listFiles()) {
			if (!fileEntry.isDirectory()) {
				String createDateKey = FileUtils.getNameWithoutExtension(fileEntry);
				Date createDate = DateUtils.strToDate(createDateKey);

				Reader in = new FileReader(fileEntry.getAbsolutePath());
				Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
				Map<String, StockPrice> stockPricesByCode = new HashMap<>();

				for (CSVRecord record : records) {
					String currencyCode = record.get(1);
					BigDecimal priceInHKD = new BigDecimal(record.get(2));
					StockPrice currentPrice = currentPrices.get(currencyCode);

					StockPrice stockPrice = new StockPrice(currencyCode, priceInHKD, currentPrice.getPrice(),
							createDate);
					stockPricesByCode.put(currencyCode, stockPrice);
				}

				stockPriceByDate.put(createDateKey, stockPricesByCode);
			}
		}

		return stockPriceByDate;
	}
}
