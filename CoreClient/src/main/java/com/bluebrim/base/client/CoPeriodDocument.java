package com.bluebrim.base.client;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class CoPeriodDocument extends PlainDocument {
	private char _periodDelimiter;

	private int _maxPeriodElementLen;
	private int _delimiterPos = -1;
	private int _periodEndPos = -1;

	private final static Toolkit _beeper = Toolkit.getDefaultToolkit();
	public CoPeriodDocument() {
		this('-', 10);
	}
	public CoPeriodDocument(char periodDelimiter, int maxElementLen) {
		super();

		initialize(periodDelimiter, maxElementLen);
	}
	public CoPeriodDocument(Content content, char periodDelimiter, int maxElementLen) {
		super(content);

		initialize(periodDelimiter, maxElementLen);
	}
	private void beep() {
		_beeper.beep();
	}
	private void doInsertString(int offs, String str, AttributeSet a) throws BadLocationException {
		super.insertString(offs, str, a);
		preInsert(offs, str);
	}
	private void doRemove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		preRemove(offs, len);
	}
	public String getText(char delimiterReplacement) {
		StringBuffer text = null;
		try {
			text = new StringBuffer(getText(0, getLength()));
			if (delimiterReplacement != '\0') {
				text.setCharAt(_delimiterPos, delimiterReplacement);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return (text != null) ? text.toString() : null;
	}
	private void initialize(char periodDelimiter, int maxElementLen) {
		_periodDelimiter = periodDelimiter;
		_maxPeriodElementLen = maxElementLen;

		try {
			doInsertString(0, new Character(periodDelimiter).toString(), null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if (isEditingLeftElement(offs)) {
			if (isSpaceInLeftElement(offs, str)) {
				doInsertString(offs, str, a);
			} else {
				beep();
			}
		} else {
			if (isSpaceInRightElement(offs, str)) {
				doInsertString(offs, str, a);
			} else {
				beep();
			}
		}
	}
	private boolean isDelimiterIncluded(int offs, int len) {
		return _delimiterPos >= offs && _delimiterPos < (offs + len);
	}
	private boolean isEditingLeftElement(int offs) {
		return offs <= _delimiterPos;
	}
	private boolean isPeriodFull() {
		return _delimiterPos >= _maxPeriodElementLen && _periodEndPos >= _maxPeriodElementLen * 2;
	}
	private boolean isSpaceInLeftElement(int offs, String str) {
		return (offs + str.length() - 1) < _maxPeriodElementLen && _delimiterPos < _maxPeriodElementLen;
	}
	private boolean isSpaceInRightElement(int offs, String str) {
		return (offs + str.length() - 1) <= (_maxPeriodElementLen * 2) && (_periodEndPos - _delimiterPos) < _maxPeriodElementLen;
	}
	private boolean isSpaceLeft(int offs, String str) {
		return offs <= _delimiterPos;
	}
	private void preInsert(int offs, String str) {
		if (offs <= _delimiterPos || _delimiterPos == -1) {
			_delimiterPos += str.length();
		}
		_periodEndPos += str.length();
	}
	private void preRemove(int offs, int len) {
		if (offs <= _delimiterPos) {
			_delimiterPos -= len;
		}
		_periodEndPos -= len;
	}
	public void remove(int offs, int len) throws BadLocationException {
		if (isDelimiterIncluded(offs, len)) {
			int firstRemovalLen = _delimiterPos - offs;
			int secondRemovalLen = (offs + len - 1) - _delimiterPos;

			boolean didRemove = false;
			if (firstRemovalLen > 0) {
				doRemove(offs, firstRemovalLen);
				didRemove = true;
			}
			if (secondRemovalLen > 0) {
				doRemove(_delimiterPos + 1, secondRemovalLen);
				didRemove = true;
			}
			if (!didRemove) {
				beep();
			}
		} else {
			doRemove(offs, len);
		}
	}
	public void setText(String text, char outsideDelimiter) {
		try {
			// Clear the document.
			super.remove(0, getLength());
			if (text == null || text.length() == 0) {
				_delimiterPos = 0;
				super.insertString(0, new Character(_periodDelimiter).toString(), null);
				return;
			}

			int delimiterPos;
			StringBuffer str = new StringBuffer(text);
			// If the delimiter in the argument text is given.
			if (outsideDelimiter != '\0') {
				delimiterPos = text.indexOf(outsideDelimiter);
				str.setCharAt(delimiterPos, _periodDelimiter);
				// If the delimiter is not given lets assume it's 
				// the same as our delimiter.		
			} else {
				delimiterPos = text.indexOf(_periodDelimiter);
				if (delimiterPos < 0)
					delimiterPos = _maxPeriodElementLen;
			}
			// Insert the string
			super.insertString(0, str.toString(), null);

			_delimiterPos = delimiterPos;
			_periodEndPos = text.length() - 1;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}