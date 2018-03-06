package com.bluebrim.paint.shared;
import java.awt.*;

import com.bluebrim.browser.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.xml.shared.*;

/**
 */

public interface CoColorIF extends Cloneable, CoCatalogElementIF, CoDistinguishable, java.rmi.Remote, CoXmlExportEnabledIF {
	public boolean canBeDeleted();
	/*
	 * Färger som kan förändras svarar sant t ex com.bluebrim.paint.impl.shared.CoUserDefinedColor
	 * medan t ex com.bluebrim.paint.impl.shared.CoWhiteColor svarar falskt.
	 */
	public abstract boolean canBeEdited();

	/**
	 * This method was created in VisualAge.
	 * @return float
	 */
	public float getBlackPercentage();
	/**
	 * This method was created in VisualAge.
	 * @return float
	 */
	public float getCyanPercentage();
	/**
	 * This method was created in VisualAge.
	 * @return float
	 */
	public float getMagentaPercentage();
	/*
	 * Returnerar färgens namn
	 */
	public abstract String getName();
	/*
	 * Returnerar en färg som kan användas för att visa upp mottagaren på
	 * bildskärmen. 
	 */

	public abstract Color getPreviewColor();
	/*
	 * Returnerar en färg med applicerat tonvärde som kan användas för 
	 * att visa upp mottagaren på bildskärmen. 
	 */

	public Color getShadedPreviewColor(float shade);
	/**
	 * This method was created in VisualAge.
	 * @return float
	 */
	public float getYellowPercentage();

	public CoColorIF deepClone();

	com.bluebrim.transact.shared.CoRef getId();
}