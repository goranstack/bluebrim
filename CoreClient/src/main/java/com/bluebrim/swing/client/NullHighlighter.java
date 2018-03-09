package com.bluebrim.swing.client;

import java.awt.Graphics;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
	Implementering Highlighter som används för att stänga av
	selektering och highlighting i disablade textfält.
 */
public class NullHighlighter implements Highlighter
{

	private JTextComponent component;
	static Highlighter.HighlightPainter painter = new NullHighlightPainter();
	static NullHighlight hightlights[] 			= new NullHighlighter.NullHighlight[1];
	static {
		hightlights[0] = new NullHighlight();
	};

	
	public static class NullHighlightPainter implements Highlighter.HighlightPainter
	{
		public NullHighlightPainter()
		{
		}
		public void paint(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c)
		{
		}
	}
	static class NullHighlight implements Highlighter.Highlight
	{
		public int getStartOffset()
		{
			return 0;
		}
		public int getEndOffset()
		{
			return 0;
		}
		public Highlighter.HighlightPainter getPainter()
		{
			return painter;
		}
	}

	public NullHighlighter() {
	}
	/**
	 */
	public Object addHighlight(int p0, int p1, Highlighter.HighlightPainter p) throws BadLocationException {
		return null;
	}
	/**
	 */
	public void changeHighlight(Object tag, int p0, int p1) throws BadLocationException {
	}
	/**
	 * Called when the UI is being removed from the interface of
	 * a JTextComponent.
	 *
	 * @param c the component
	 * @see Highlighter#deinstall
	 */
	public void deinstall(JTextComponent c) {
	component = null;
	}
	/**
	 */
	public Highlighter.Highlight[] getHighlights() {
		return hightlights;
	}
	/**
	 * Called when the UI is being installed into the
	 * interface of a JTextComponent.  Installs the editor, and
	 * removes any existing highlights.
	 *
	 * @param c the editor component
	 * @see Highlighter#install
	 */
	public void install(JTextComponent c) {
	component = c;
	removeAllHighlights();
	}
	public void paint(Graphics g) {}
	/**
	 * Removes all highlights.
	 */
	public void removeAllHighlights() {
	}
	/**
	 * Removes a highlight from the view.
	 *
	 * @param tag the reference to the highlight
	 */
	public void removeHighlight(Object tag) {
	}
}
