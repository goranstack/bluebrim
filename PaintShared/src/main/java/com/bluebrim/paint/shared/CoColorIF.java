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
	 * F�rger som kan f�r�ndras svarar sant t ex com.bluebrim.paint.impl.shared.CoUserDefinedColor
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
	 * Returnerar f�rgens namn
	 */
	public abstract String getName();
	/*
	 * Returnerar en f�rg som kan anv�ndas f�r att visa upp mottagaren p�
	 * bildsk�rmen. 
	 */

	public abstract Color getPreviewColor();
	/*
	 * Returnerar en f�rg med applicerat tonv�rde som kan anv�ndas f�r 
	 * att visa upp mottagaren p� bildsk�rmen. 
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