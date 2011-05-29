package org.code.completion.custom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * CustomCompletion
 *
 * @author Wojciech Holisz <wojciech.holisz@gmail.com>
 */
public class CustomCompletion {

	public CustomCompletion() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		Preferences pref = Preferences.userNodeForPackage(CustomCodeCompletionPanel.class);
		String filename = pref.get("dictionaryFilename", "");

		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse(filename);

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr = xpath.compile("//frase");

		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList)result;

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			String frase = nodes.item(i).getTextContent();

			NamedNodeMap attributes = node.getAttributes();
			Node after = attributes.getNamedItem("after");
			Node caret = attributes.getNamedItem("caret");
			int offset = 0;

			if (caret != null) {
				offset = Integer.parseInt(caret.getNodeValue());
			}
			else if (after != null) {
				offset = frase.indexOf(after.getNodeValue()) + 1;
			}
			else {
				offset = frase.length();
			}

			items.add(new Frase(frase, offset));
		}
	}

	/**
	 * Generuje listę możliwych zakończeń podanej frazy.
	 *
	 * @param line Fraza.
	 * @return Tablica z listą możliwych podpowiedzi.
	 */
	public ArrayList<Frase> getItems(String line) {

		// Usuń białe znaki.
		line = line.trim();

		// Przygotuj listę podpowiedzi.
		ArrayList<Frase> matchingItems = new ArrayList<Frase>();

		for (Frase item : items) {
			if (item.text.startsWith(line) == true) {
				matchingItems.add(item);
			}
		}

		return matchingItems;
	}

	/**
	 * Lista rozpoznawanych poleceń.
	 */
	protected ArrayList<Frase> items = new ArrayList<Frase>();

}
