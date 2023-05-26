package org.code.completion.custom.utils;

public class Escape {

	public static String html(String value) {
		value = value.replace("<", "&lt;");
		value = value.replace(">", "&gt;");

		return value;
	}
}
