package org.code.completion.custom.utils;

/**
 * Escape
 *
 * Klasa służy do filtrowania znaków.
 *
 * @author Wojciech Holisz <wojciech.holisz@gmail.com>
 */
public class Escape {

	/**
	 * Zamienia niebezpieczne znaki na encje HTML.
	 *
	 * @param text Tekst bez encji HTML.
	 * @return Tekst z encjami HTML.
	 */
	public static String html(String text) {
		text = text.replace("<", "&lt;");
		text = text.replace(">", "&gt;");

		return text;
	}

}
