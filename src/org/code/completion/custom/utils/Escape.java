package org.code.completion.custom.utils;

/**
 * Escape
 *
 * @author Wojciech Holisz <wojciech.holisz@gmail.com>
 */
public class Escape {

	public static String html(String value) {
		value = value.replace("<", "&lt;");
		value = value.replace(">", "&gt;");

		return value;
	}
}
