package com.bluebrim.paint.impl.shared;
import java.awt.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.transact.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Instanser av CoColor representerar de färger som ingår
 * i pageitems-paketet. LayoutEditorn har ett menyval i edit-menyn 
 * som öppnar en färgdialog. I den kan du se
 * processfärger samt registrera dekorfärger och multi-ink-färger. Alla dessa blir
 * instanser av CoPredefinedColors olika subklasser.
 * 
 */
public abstract class CoColor extends CoObject implements CoColorIF {
	public static final String XML_GOI = "goi";
	public static final String XML_TAG = "color";
	public static final String XML_TYPE = "type";

	protected CoGOI m_goi;

	public CoColor() {
		m_goi = new CoGOI(new CoSpecificContext("colorSingleton", "anySystem", getFactoryKey()), 0);
	}

	public abstract boolean canBeDeleted();
	/**
	 * Färger som kan förändras svarar sant t ex CoUserDefinedColor
	 * medan t ex CoWhiteColor svarar falskt.
	 */
	public abstract boolean canBeEdited();

	public abstract float getBlackPercentage();

	public abstract float getCyanPercentage();

	public String getIconName() {

		return getSmallIconName();
	}

	public String getIconResourceAnchor() {

		return getClass().getName();
	}

	public String getIdentity() {

		return getName();
	}

	public abstract float getMagentaPercentage();

	/**
	 * Returnerar färgens namn
	 */
	public abstract String getName();

	/**
	 * Returnerar en färg som kan användas för att visa upp mottagaren på
	 * bildskärmen. 
	 */

	public abstract Color getPreviewColor();

	/**
	 * Returnerar en färg med applicerat tonvärde som kan användas för 
	 * att visa upp mottagaren på bildskärmen. 
	 */

	public Color getShadedPreviewColor(float shade) {
		if (shade > 100)
			shade = 100;
		if (shade < 0)
			shade = 0;

		int red, green, blue;
		Color c = getPreviewColor();
		red = 255 - (255 - c.getRed()) * (int) shade / 100;
		green = 255 - (255 - c.getGreen()) * (int) shade / 100;
		blue = 255 - (255 - c.getBlue()) * (int) shade / 100;
		return new Color(red, green, blue);
	}

	public String getSmallIconName() {
		return "DefaultIcon.gif";
	}

	public abstract String getType();

	public abstract float getYellowPercentage();

	protected void markDirty() {
		if (CoAssertion.SIMULATION_SUPPORT)
			CoAssertion.addChangedObject(this);
	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.exportAttribute(XML_TYPE, getFactoryKey());
		visitor.exportGOIAttribute(XML_GOI, m_goi);
	}


	public CoColorIF deepClone() {
		return this;
	}

	public long getCOI() {
		return m_goi.getCoi();
	}

	public CoGOI getGOI() {
		return m_goi;
	}

	// Return identity

	public final CoRef getId() {
		return CoRef.to(this);
	}

	public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
		if (subModel != null) {
			System.out.println(getClass().getName() + "  Ignoring sub model : " + subModel);
		}
	}

	public void xmlInit(java.util.Map attributes, CoXmlContext context) {
		String tmp = CoXmlUtilities.parseString((String) attributes.get(XML_GOI), null);
		if (tmp != null) {
			m_goi = new CoGOI(tmp);
		}
	}
}