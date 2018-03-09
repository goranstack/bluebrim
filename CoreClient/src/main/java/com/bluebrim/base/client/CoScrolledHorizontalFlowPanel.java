package com.bluebrim.base.client;

import com.bluebrim.gui.client.CoAbstractScrolledPanel;

/**
 * En subklass till CoAbstractScrolledPanel som arrangerar sina barn i rader fr�n 
 * h�ger till v�nster. Anv�ndning av horisontell scrollbar undviks s� l�ngt det �r
 * m�jligt.
 */
public class CoScrolledHorizontalFlowPanel extends CoAbstractScrolledPanel {

	public CoScrolledHorizontalFlowPanel() {
		this(false);
	}

	public CoScrolledHorizontalFlowPanel(boolean isDoubleBuffered) {
		super(new CoScrolledHorizontalFlowPanelLayout(), isDoubleBuffered);
	}
}