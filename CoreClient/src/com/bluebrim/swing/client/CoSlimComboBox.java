package com.bluebrim.swing.client;

import java.awt.*;

/**
 * ComboBox stolen from CoAbstractToolbar (and fixed min & max size).
 * @author Markus Persson (originally Dennis)
 */
public class CoSlimComboBox extends CoComboBox {
	public CoSlimComboBox() {
		super();
		init();
	}

	public CoSlimComboBox(int i) {
		super(i);
		init();
	}

    public CoSlimComboBox(Object[] items) {
        super(items);
		init();
    }

	private void init() {
//		((JComponent) getEditor().getEditorComponent()).setBorder(BorderFactory.createEtchedBorder());
//		setBorder(null);
	}
	
	private int fixedHeight() {
		Insets i = getInsets();
		return getEditor().getEditorComponent().getPreferredSize().height + i.top + i.bottom;
	}
	public Dimension getMaximumSize() {
		Dimension d = super.getMaximumSize();
		d.height = fixedHeight();
		return d;
	}
	public Dimension getMinimumSize() {
		Dimension d = super.getMinimumSize();
		d.height = fixedHeight();
		return d;
	}
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.height = fixedHeight();
		return d;
	}
}