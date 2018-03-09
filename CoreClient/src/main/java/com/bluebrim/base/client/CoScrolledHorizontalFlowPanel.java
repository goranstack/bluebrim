package com.bluebrim.base.client;

import com.bluebrim.gui.client.CoAbstractScrolledPanel;

/**
 * En subklass till CoAbstractScrolledPanel som arrangerar sina barn i rader från 
 * höger till vänster. Användning av horisontell scrollbar undviks så långt det är
 * möjligt.
 */
public class CoScrolledHorizontalFlowPanel extends CoAbstractScrolledPanel {

	public CoScrolledHorizontalFlowPanel() {
		this(false);
	}

	public CoScrolledHorizontalFlowPanel(boolean isDoubleBuffered) {
		super(new CoScrolledHorizontalFlowPanelLayout(), isDoubleBuffered);
	}
}