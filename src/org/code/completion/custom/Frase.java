package org.code.completion.custom;

/**
 * Fraze
 *
 * @author Wojciech Holisz <wojciech.holisz@gmail.com>
 */
public class Frase {
	public String text;
	public int offset;

	public Frase(String text, int offset) {
		this.text = text;
		this.offset = offset;
	}
}
