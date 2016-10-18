package com.google.gwt.sample.stockwatcher.server;

import java.io.File;
import java.text.ParseException;

public class FileUtils {
	public static String getNameWithoutExtension(File file) throws ParseException {
		return file.getName().replaceFirst("[.][^.]+$", "");
	}
}
