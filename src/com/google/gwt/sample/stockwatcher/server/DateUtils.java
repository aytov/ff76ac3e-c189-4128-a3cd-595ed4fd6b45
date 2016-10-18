package com.google.gwt.sample.stockwatcher.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static final String DEFAULT_DATE_ONLY_FORMAT = "dd.MM.yyyy";

	public static final Date strToDate(String str) throws ParseException {
		return new SimpleDateFormat(DEFAULT_DATE_ONLY_FORMAT).parse(str);
	}

	public static final String dateToStr(Date date) throws ParseException {
		return new SimpleDateFormat(DEFAULT_DATE_ONLY_FORMAT).format(date);
	}
}
