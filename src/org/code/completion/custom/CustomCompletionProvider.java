package org.code.completion.custom;

import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 * OptCompletionProvider
 *
 * Implemetancja interfejsu CompletionProvider służącego do generowania podpowiedzi.
 */
public class CustomCompletionProvider implements CompletionProvider {

	@Override
	public CompletionTask createTask(int i, JTextComponent jtc) {

		if (i != CompletionProvider.COMPLETION_QUERY_TYPE) {
			return null;
		}

		return new AsyncCompletionTask(new AsyncCompletionQuery() {

			@Override
			protected void query(CompletionResultSet completionResultSet, Document document, int caretOffset) {

				String filter = null;
				int startOffset = caretOffset - 1;

				Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

				// Wyszukaj frazę dla której będziemy szukali podpowiedzi.
				try {
					final StyledDocument bDoc = (StyledDocument) document;
					final int lineStartOffset = getRowFirstNonWhite(bDoc, caretOffset);
					final char[] line = bDoc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
					final int whiteOffset = indexOfWhite(line);
					filter = new String(line, whiteOffset + 1, line.length - whiteOffset - 1);

					if (whiteOffset > 0) {
						startOffset = lineStartOffset + whiteOffset + 1;
					}
					else {
						startOffset = lineStartOffset;
					}
				}
				catch (Exception ex) {
					logger.warning(ex.getMessage());
				}

				// Wygeneruj listę podpowiedzi.
				try {
					CustomCompletion optCompleter = new CustomCompletion();
					ArrayList<Frase> items = optCompleter.getItems(filter);

					for (Frase item : items) {
						completionResultSet.addItem(new CustomCompletionItem(item.text, item.offset, startOffset, caretOffset));
					}
				}
				catch (Exception ex) {
					logger.warning(ex.getMessage());
				}

				completionResultSet.finish();
			}

		}, jtc);
	}

	@Override
	public int getAutoQueryTypes(JTextComponent jtc, String string) {
		return COMPLETION_ALL_QUERY_TYPE;
	}

	static int getRowFirstNonWhite(StyledDocument doc, int offset) throws BadLocationException {
		Element lineElement = doc.getParagraphElement(offset);
		int startOffset = lineElement.getStartOffset();
		while (startOffset + 1 < lineElement.getEndOffset()) {
			try {
				if (doc.getText(startOffset, 1).charAt(0) != ' ') {
					break;
				}
			}
			catch (BadLocationException ex) {
				throw (BadLocationException) new BadLocationException(
					"calling getText(" + startOffset + ", " + (startOffset + 1)
					+ ") on doc of length: " + doc.getLength(), startOffset).initCause(ex);
			}
			startOffset++;
		}
		return startOffset;
	}

	static int indexOfWhite(char[] line) {
		int i = line.length;
		while (--i > -1) {
			final char c = line[i];
			if (Character.isWhitespace(c)) {
				return i;
			}
		}
		return -1;
	}

}
