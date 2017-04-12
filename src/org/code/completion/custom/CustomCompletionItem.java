package org.code.completion.custom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.code.completion.custom.utils.Escape;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;

/**
 * OptCompletionItem
 *
 * Przechowuje jedną podpowiedź. Odpowiada m.in. za wstawienie do edytora kodu
 * odpowiedniej frazy, w przypadku wybrania jej przez użytkownika.
 */
public class CustomCompletionItem implements CompletionItem {

	/**
	 * Konstruktor
	 *
	 * @param text Treść podpowiedzi.
	 * @param dotOffset Początek frazy, która powinna zostać uzupełniona.
	 * @param caretOffset Pozycja kursora w edytorze kodu.
	 */
	public CustomCompletionItem(String text, int offset, int dotOffset, int caretOffset) {
		this.text = text;
		this.offset = offset;
		this.dotOffset = dotOffset;
		this.caretOffset = caretOffset;
	}

	/**
	 * Wykonuje się po wybraniu podpowiedzi przez użytkownika (np. klawiszem Enter).
	 * Uzupełnia frazę rozpoczętą przez użytkownika.
	 *
	 * @param jtc Edytor kodu.
	 */
	@Override
	public void defaultAction(JTextComponent jtc) {
		try {
			final StyledDocument doc = (StyledDocument) jtc.getDocument();

			// Replace existing text with complete frase.
			doc.remove(dotOffset, caretOffset - dotOffset);
			doc.insertString(dotOffset, text, null);

			jtc.setCaretPosition(dotOffset + offset);
		}
		catch (Exception ex) {
		}

		Completion.get().hideAll();
	}

	@Override
	public void processKeyEvent(KeyEvent ke) {
	}

	/**
	 * Zwraca szerokość podpowiedzi.
	 */
	@Override
	public int getPreferredWidth(Graphics grphcs, Font font) {
		return CompletionUtilities.getPreferredWidth(Escape.html(text), null, grphcs, font);
	}

	/**
	 * Generuje element na liście podpowiedzi.
	 */
	@Override
	public void render(Graphics grphcs, Font font, Color color, Color color1, int i, int i1, boolean bln) {
		CompletionUtilities.renderHtml(null, Escape.html(text), null, grphcs, font, color, i, i1, bln);
	}

	@Override
	public CompletionTask createDocumentationTask() {
		return null;
	}

	@Override
	public CompletionTask createToolTipTask() {
		return null;
	}

	@Override
	public boolean instantSubstitution(JTextComponent jtc) {
		return false;
	}

	@Override
	public int getSortPriority() {
		return 0;
	}

	/**
	 * Zwraca tekst wykorzystywany do alfabetyczego sortowania podpowiedzi.
	 *
	 * @return Treść podpowiedzi.
	 */
	@Override
	public CharSequence getSortText() {
		return text;
	}

	@Override
	public CharSequence getInsertPrefix() {
		return text;
	}

	private String text;
	private int offset;
	private int caretOffset;
	private int dotOffset;

}
