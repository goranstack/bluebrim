package com.bluebrim.text.impl.client;

import java.util.NoSuchElementException;

import javax.swing.JTextPane;

//

//public class CoTextPaneWordParser implements WordParser
// WordParser is part of Wintertree spell checker that will be replaced by a GPL spell checker
public class CoTextPaneWordParser

{
	// The current cursor position
	int cursor;

	// true if the cursor is positioned at the first word in the string.
	private boolean is1stWord;

	// true if hyphens are to be treated as delimiters, false if they're to be included in words
	private boolean isHyphenDelimiter;

	// Number of replacements made.
	private int nReplacements;

	// Number of words obtained.
	private int nWords;

	// Position of the start of the previous word, or < 0 if the position is unknown.
	private int prevWordPos;

	// true if the current word should always be selected in the TextArea to show context.
	private boolean selectCurWord;

	// The text extracted from the JTextArea
	private String text;

	// The JTextArea being parsed
	private JTextPane textArea;
	/**
	 * Construct a JTextAreaWordParser to parse words from the contents of a
	 * JTextArea component.
	 * @param textArea The JTextArea component to parse.
	 * @param isHyphenDelim true if a hyphen is to be considered a word
	 *	delimiter, false if it's part of a word
	 * @param selectCurWord true if each word should be selected in the
	 *	TextArea to show context. Enabling this option degrades performance.
	 */
	public CoTextPaneWordParser(JTextPane textArea, boolean isHyphenDelim,
	  boolean selectCurWord) {
		cursor = 0;
		is1stWord = true;
		isHyphenDelimiter = isHyphenDelim;
		nReplacements = 0;
		nWords = 0;
		prevWordPos = -1;
		this.selectCurWord = selectCurWord;
		this.textArea = textArea;
		text = textArea.getText();
	}
	/**
	 * Delete a specified number of characters from the text starting at
	 * the current cursor position.
	 * @param numChars The number of characters to delete.
	 * @exception NoSuchElementException Attempt to delete beyond the end of
	 * the string.
	 */
	public void deleteText(int numChars) throws NoSuchElementException {
		// Make sure the requested number of characters can be deleted.
		if (numChars > text.length() - cursor) {
			throw new NoSuchElementException();
		}

		// Delete the text in the text area. Because both selectionStart and
		// selectionEnd are constrained so selectionStart <= selectionEnd,
		// we have to set selectionStart 2x to ensure it takes.
		textArea.setSelectionStart(cursor);
		textArea.setSelectionEnd(cursor + numChars);
		textArea.setSelectionStart(cursor);
		textArea.replaceSelection("");

		// Refresh the local image.
		text = textArea.getText();
	}
	/**
	 * Delete the word at the cursor position. Leading whitespace is also
	 * deleted.
	 * @return The offset of the first deleted character.
	 * @exception NoSuchElementException The cursor is positioned at the end
	 *	of the string.
	 */
	public int deleteWord() throws NoSuchElementException {
		StringBuffer junk = new StringBuffer();

		return (deleteWord(junk));
	}
	/**
	 * Delete the word at the cursor position. Leading whitespace is also
	 * deleted. The cursor is left positioned at the start of first word
	 * following the deleted word.
	 * @param delText The text which was deleted
	 * @return The offset of the first deleted character
	 * @exception NoSuchElementException The cursor is positioned at the end
	 *	of the string.
	 */
	public int deleteWord(StringBuffer delText) throws NoSuchElementException {
		// The word being deleted includes leading whitespace.
		// Count and collect the total number of characters that must be
		// deleted. The cursor is positioned at the first character of the
		// word to be deleted.

		// Find the first whitepace character following the previous word.
		while (cursor > 0) {
			if (!Character.isSpaceChar(text.charAt(cursor - 1))) {
				break;
			}
			else {
				--cursor;
			}
		}
		int offset = cursor;

		// Count and collect the characters to be deleted, including the
		// leading whitespace and the word.
		delText.setLength(0);
		boolean hitWord = false;
		int nDelChars = 0;
		int tc;
		for (tc = cursor; tc < text.length(); ++tc) {
			char c = text.charAt(tc);
			if (is1stWordChar(c)) {
				hitWord = true;
			}
			if (hitWord && !isWordChar(c)) {
				// The end of the word being deleted was found.
				break;
			}
			else {
				// Collect and count the text to be deleted.
				delText.append(c);
				++nDelChars;
			}
		}
		if (!hitWord) {
			throw new NoSuchElementException();
		}

		// Delete the text.
		deleteText(tc - cursor);

		return (offset);
	}
	/**
	 * Obtain the current cursor position, expressed as an offset from the
	 * start of the text area.
	 * @return The current cursor position
	 * @see TextAreaWordParser#setCursor
	 */
	public int getCursor() {
		return cursor;
	}
	/**
	 * Get the number of words replaced so far.
	 * @return The number of replacements made.
	 */
	public int getNumReplacements() {
		return nReplacements;
	}
	/**
	 * Get the number of words obtained so far.
	 * @return The number of words obtained.
	 */
	public int getNumWords() {
		return nWords;
	}
	/**
	 * Obtain the word at the WordParser's current cursor position. Note that
	 * the cursor position is not changed by this function, so calling it
	 * repeatedly without intervening calls to nextElement or nextWord will
	 * return the same word.
	 * @return The word at the current position
	 * @exception NoSuchElementException The cursor is positioned at the end
	 *	of the string.
	 * @see TextAreaWordParser#nextElement
	 * @see TextAreaWordParser#nextWord
	 */
	public String getWord() throws NoSuchElementException {
		// Skip any junk preceding the word.
		while (cursor < text.length() &&
		  !is1stWordChar(text.charAt(cursor))) {
			++cursor;
		}

		if (cursor >= text.length()) {
			// We reached the end of the text without finding a word.
			throw new NoSuchElementException();
		}

		// Collect the word. The cursor is positioned at the first character.
		StringBuffer word = new StringBuffer();
		boolean initialism = false;
		int tc;
		for (tc = cursor; tc < text.length(); ++tc) {
			char c = text.charAt(tc);
			if (c == '.') {
		  		// Periods are acceptable if they're surrounded by letters.
				if (tc > 0 && Character.isLetter(text.charAt(tc - 1)) &&
				  tc + 1 < text.length() &&
				  Character.isLetter(text.charAt(tc + 1))) {
					// Word contains embedded periods.
					initialism = true;
				}
				else {
					break;
				}
			}
			else if (c == '-' && !isHyphenDelimiter) {
				// Hyphens are acceptable if they're surrounded by word
				// characters.
				if (tc == 0 ||
				  !isWordChar(text.charAt(tc - 1)) ||
				  tc + 1 > text.length() ||
				  !isWordChar(text.charAt(tc + 1))) {
			  		break;
				}
			}
			else if (!isWordChar(c)) {
				break;
			}

			word.append(c);
		}

		// Accept a terminating period only if the word contained embedded
		// periods (ie is an initialism like R.C.M.P.)
		if (initialism && tc < text.length() &&
		  text.charAt(tc) == '.') {
			word.append(text.charAt(tc));
			++tc;
		}

		// Accept a terminating apostrophe only as part of a possessive (s')
		if (UniCharacter.isApostrophe(word.charAt(word.length() - 1))) {
			if (word.length() >= 2 &&
			  Character.toLowerCase(word.charAt(word.length() - 2)) != 's') {
				// Delete the terminating apostrophe
				word.setLength(word.length() - 1);
			}
		}

		if (selectCurWord) {
			// Select the word in the text area to show context. The
			// implementation of the TextArea.select method calls getText
			// to validate the boundaries. When the TextArea holds a lot
			// of text, calling getText can significantly degrade performance.
			textArea.requestFocus();
			textArea.select(cursor, cursor + word.length());
		}

		return word.toString();
	}
	/**
	 * Determine if there are more words available.
	 * @return true if more words are available for nextElement, nextWord,
	 *	or getWord
	 * @see TextAreaWordParser#nextElement
	 * @see TextAreaWordParser#nextWord
	 * @see TextAreaWordParser#getWord
	 */
	public boolean hasMoreElements() {
		// See if any words are present
		for (int i = cursor; i < text.length(); ++i) {
			if (is1stWordChar(text.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Highlight the current word in the text area.
	 */
	public void highlightWord() {
		if (hasMoreElements()) {
			String word = getWord();
			textArea.requestFocus();
			
			// selectionStart and selectionEnd are constrained so
			// selectionStart <= selectionEnd, so selectionStart
			// must be set 2x to ensure it gets set to what we want.
			textArea.setSelectionStart(cursor);
			textArea.setSelectionEnd(cursor + word.length());
			textArea.setSelectionStart(cursor);
		}
	}
	/**
	 * Insert text at a specified position.
	 * @param pos The position at which new text is to be inserted.
	 * @param newText The text to insert.
	 */
	public void insertText(int pos, String newText)
	{
		try
		{
			textArea.getDocument().insertString(pos, newText, null);
		}
		catch ( javax.swing.text.BadLocationException ex )
		{
			com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, getClass() + ".insertText falied:\n" + ex );
		}

		// Update the local image following the change.
		text = textArea.getText();
	}
	/**
	 * Determine if a character can be the first character of a word.
	 * @param c The character to test
	 * @return true if the character could appear at the start of a word
	 */
	private boolean is1stWordChar(char c) {
		return Character.isLetter(c) || Character.isDigit(c);
	}
	/**
	 * Determine if the current word and the previous word are identical,
	 * and that no punctuation appears between them.
	 * @param caseSensitive true if case should be considered when comparing
	 *	this word and the previous one
	 * @return true if this word is identical to the previous one
	 * @exception NoSuchElementException The cursor is positioned at the end
	 *	of the string.
	 */
	public boolean isDoubledWord(boolean caseSensitive) {
		if (prevWordPos < 0 || prevWordPos == cursor) {
			// There is no previous word.
			return false;
		}

		// Get the previous and current words.
		String curWord = getWord();
		int saveCursor = cursor;
		cursor = prevWordPos;
		String prevWord = getWord();
		char termChar = text.charAt(cursor);
		cursor = saveCursor;

		// See if the previous and current words match.
		boolean wordsMatch;
		if (caseSensitive) {
			wordsMatch = prevWord.equals(curWord);
		}
		else {
			wordsMatch = prevWord.equalsIgnoreCase(curWord);
		}
		if (wordsMatch) {
			// The previous and current words are doubled only if there is no
			// intervening punctuation.
			return !UniCharacter.isPunctuation(termChar);
		}
		return false;
	}
	/**
	 * Determine if the cursor is positioned at the first word in the string.
	 * @return true if the cursor is at the first word.
	 */
	public boolean isFirstWord() {
		return is1stWord;
	}
	/**
	 * Determine if a character can appear within a word.
	 * @param c The character to test
	 * @return true if the character could appear within a word
	 */
	private boolean isWordChar(char c) {
		return is1stWordChar(c) || UniCharacter.isApostrophe(c);
	}
	/**
	 * Obtain the current word, returned as an Object, and advance to the
	 * next word. This function behaves in a manner identical to nextWord,
	 * but it returns the word as an Object for compliance with the
	 * Enumeration interface. The cursor is advanced to the next word or
	 * to the end of the string.
	 * @return The current word
	 * @exception NoSuchElementException The cursor is positioned at the
	 *	end of the string
	 * @see StringWordParser#nextWord
	 */
	public Object nextElement() {
		return nextWord();
	}
	/**
	 * Obtain the current word and advance to the next word. The cursor
	 * is advanced to the next word or to the end of the string.
	 * @return The current word
	 * @exception NoSuchElementException The cursor is positioned at the
	 *	end of the string
	 * @see TextAreaWordParser#nextElement
	 * @see TextAreaWordParser#getWord
	 */
	public String nextWord() throws NoSuchElementException {
		// Save the position of the current word before advancing to the next.
		prevWordPos = cursor;

		// Skip any junk preceding the word.
		while (cursor < text.length() &&
		  !is1stWordChar(text.charAt(cursor))) {
			++cursor;
		}

		if (cursor >= text.length()) {
			// We reached the end of the block without finding a word.
			throw new NoSuchElementException();
		}

		// Skip the word.
		String curWord = getWord();
		cursor += curWord.length();

		// Skip any junk following the word.
		while (cursor < text.length()) {
			char c = text.charAt(cursor);

			if (is1stWordChar(c)) {
				break;
			}

			// Any printable junk between words means the two words can't
			// be doubled.
			if (UniCharacter.isPrintable(c) && !Character.isSpaceChar(c)) {

				// Disable the doubled-word test next pass.
				prevWordPos = -1;
			}
			++cursor;
		}

		// We're no longer at the first word.
		is1stWord = false;

		// Count the number of words here rather than getWord, because
		// getWord can be called any number of times for the same word.
		++nWords;

		return curWord;
	}
	/**
	 * Replace the word at the current position with a new word.
	 * @param newWord The word to replace the word at the current position.
	 * @exception NoSuchElementException The cursor is positioned at the end
	 *	of the string.
	 */
	public void replaceWord(String newWord) throws NoSuchElementException {

		// Note that deleteWord() can't be used, because it deletes
		// trailing punctuation as well.

		// Get the word being replaced.
		String oldWord = getWord();

		// Replace the old with the new. Because selectionStart and
		// selectionEnd are constrained so selectionStart <= selectionEnd,
		// we have to set selectionStart 2x to ensure it takes.
		textArea.setSelectionStart(cursor);
		textArea.setSelectionEnd(cursor + oldWord.length());
		textArea.setSelectionStart(cursor);
		textArea.replaceSelection(newWord);

		// Update the local image following the change.
		text = textArea.getText();

		++nReplacements;
	}
	/**
	 * Set the cursor to a given position.
	 * @param index The position in the string to set the cursor to.
	 * @exception StringIndexOutOfBoundsException The index exceeds the
	 *	string size.
	 * @see StringWordParser#getCursor
	 */
	public void setCursor(int index) throws StringIndexOutOfBoundsException {
		if (index < 0 || index >= text.length()) {
			throw new StringIndexOutOfBoundsException(index);
		}
		cursor = index;
	}
	/**
	 * Convert the text to a string.
	 * @return The text held by the parser expressed as a string.
	 */
	public String toString() {
		return text;
	}
	/**
	 * Report that the contents of the TextArea have changed. This method
	 * must be called if the TextArea is edited by the user or updated
	 * by other software. Currently, it causes checking of the TextArea
	 * to restart. In future, it will call the 1.1 TextArea's getCaret
	 * method and check from that point.
	 */
	public void updateText() {
		text = textArea.getText();
		cursor = 0;
	}
	
	/**
	 * Fake class to get rid of compile errors
	 *
	 */
	private static class UniCharacter
	{

		public static boolean isApostrophe(char charAt) {
			throw new UnsupportedOperationException("Until Wintertree spell checker is replaced by a GPL spell checker");
		}

		public static boolean isPrintable(char c) {
			throw new UnsupportedOperationException("Until Wintertree spell checker is replaced by a GPL spell checker");
		}

		public static boolean isPunctuation(char termChar) {
			throw new UnsupportedOperationException("Until Wintertree spell checker is replaced by a GPL spell checker");
		}
		
	}
}
