package com.bluebrim.paint.impl.shared;
import java.awt.*;
import java.util.*;

import com.bluebrim.paint.shared.*;
import com.bluebrim.transact.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Representerar de färger som användare själva kan 
 * definiera och döpa till valfritt namn
 * 
 */
public abstract class CoUserDefinedColor extends CoTrappableColor implements CoUserDefinedColorIF {
	public static final String XML_NAME = "name";

	protected String m_name;

	public CoUserDefinedColor() {
		super();
		m_goi = CoRefService.createGOI();
	}
	
	public boolean canBeDeleted() {
		return true;
	}

	public boolean canBeEdited() {
		return true;
	}

	public boolean equals(Object o) {
		return (o instanceof CoUserDefinedColor) && (((CoUserDefinedColor) o).m_name.equals(m_name));
	}

	public String getIdentity() {

		return getName();
	}

	public String getName() {
		return m_name;
	}

	public String getType() {
		return CoColorResources.getName(USER_DEFINED_COLOR);
	}

	public abstract void setColor(Color c);

	public void setName(String name) {

		m_name = name;
		markDirty();
	}

	public CoColorIF deepClone() {
		CoUserDefinedColor tColor = null;

		try {
			tColor = (CoUserDefinedColor) clone();
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(getClass() + ".clone() failed");
		}

		tColor.m_name = new String(m_name);
		return tColor;
	}

	public void xmlInit(Map attributes, CoXmlContext context) {
		super.xmlInit(attributes, context);

		m_name = CoXmlUtilities.parseString((String) attributes.get(XML_NAME), m_name);
	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		super.xmlVisit(visitor);

		visitor.exportAttribute(XML_NAME, m_name);
	}
}