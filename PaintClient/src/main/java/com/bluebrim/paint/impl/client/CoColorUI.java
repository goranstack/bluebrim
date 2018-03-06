package com.bluebrim.paint.impl.client;
import java.awt.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.paint.shared.*;

/**
 *
 */
public abstract class CoColorUI extends CoDomainUserInterface {
	protected CoColorSampleIcon colorPreviewIcon;
	public static final String PREVIEW = "preview";

	public CoColorUI() {
		super();
	}

	public CoColorUI(CoObjectIF aDomainObject) {
		super(aDomainObject);
	}

	protected void createPreviewIcon() {
		this.colorPreviewIcon =
			new CoColorSampleIcon(
				(CoColorIF) getDomain(),
				Color.white,
				new Dimension(30, 30));
	}

	protected void setDomainFrom(CoObjectIF selectedValue) {
		setDomain(selectedValue);
	}

	public void setColorEditable(boolean e) {
	}
}