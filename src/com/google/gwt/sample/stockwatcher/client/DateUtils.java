package com.google.gwt.sample.stockwatcher.client;


import com.google.gwt.i18n.client.DateTimeFormat;

public class DateUtils {
	public static final String DEFAULT_DATE_ONLY_FORMAT = "dd.MM.yyyy";
	public static final DateTimeFormat DATE_FORMAT  = DateTimeFormat.getFormat(DEFAULT_DATE_ONLY_FORMAT);

	public static final java.util.Date date(String str) {
		return DateTimeFormat.getFormat(DEFAULT_DATE_ONLY_FORMAT).parse(str);
	}
}
