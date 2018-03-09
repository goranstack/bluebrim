package com.bluebrim.text.impl.server;
import java.awt.*;
import java.util.*;

import javax.swing.text.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Klass som representerar teckentypografi.
 * All typografiinfo ligger lagrad i ett SimpleAttributeSet.
 */
public class CoCharacterStyle extends CoObject implements CoCharacterStyleIF {

	public static final String XML_DELETED = "deleted";
	public static final String XML_INHERIT = "inherit";
	public static final String XML_NAME = "name";
	public static final String XML_TAG = "character-style";
	public static final String XML_ATTRIBUTES = "attributes";

	protected CoCharacterStyle m_basedOn;
	private boolean m_deleted;
	private boolean m_inherit = true;
	protected CoSimpleAttributeSet m_styleAttributes;
	private boolean m_dirty;

	CoCharacterStyle() {
		super();
	}

	public CoCharacterStyle(String name) {
		super();

		m_styleAttributes = new CoSimpleAttributeSet();

		CoStyleConstants.set(m_styleAttributes, CoTextConstants.CHARACTER_TAG, name);
	}

	public void clear() {
		m_styleAttributes.removeAttributes(m_styleAttributes);
		markDirty();
	}

	public CoCharacterStyle deepClone() {
		CoCharacterStyle s = new CoCharacterStyle(getName());
		s.m_styleAttributes.addAttributes(m_styleAttributes);
		return s;
	}

	public boolean equals(Object o) {
		if (o instanceof CoCharacterStyleIF) {
			return getName().equals(((CoCharacterStyleIF) o).getName());
			//		CoCharacterStyleIF s = (CoCharacterStyleIF) o;
			//		return m_styleAttributes.equals( s.getAttributes() );
		} else {
			return super.equals(o);
		}
	}

	public Boolean getAllCaps() {
		return CoStyleConstants.getAllCaps(m_styleAttributes);
	}

	public AttributeSet getAttributes() {
		return m_styleAttributes;
	}

	public Float getBaselineOffset() {
		return CoStyleConstants.getBaselineOffset(m_styleAttributes);
	}

	public String getFactoryKey() {
		return CoTextConstants.CHARACTER_STYLE.toString();
	}

	public Font getFont() {
		String family = getFontFamily();
		if (family == null)
			family = "dialog";

		int style = Font.PLAIN;
		if (getWeight().getValue() == CoTextConstants.BOLD)
			style |= Font.BOLD;
		if (getStyle().getValue() == CoTextConstants.ITALIC)
			style |= Font.ITALIC;

		int size = 16;
		Float f = getFontSize();
		if (f != null)
			size = (int) f.floatValue();

		return new Font(family, style, size);
	}

	public String getFontFamily() {
		return CoStyleConstants.getFontFamily(m_styleAttributes);
	}

	public Float getFontSize() {
		return CoStyleConstants.getFontSize(m_styleAttributes);
	}

	public String getForegroundColor() {
		return CoStyleConstants.getForegroundColor(m_styleAttributes);
	}

	public Float getForegroundShade() {
		return CoStyleConstants.getForegroundShade(m_styleAttributes);
	}

	public Float getHorizontalScale() {
		return CoStyleConstants.getHorizontalScale(m_styleAttributes);
	}

	public javax.swing.KeyStroke getKeyStroke() {
		return CoStyleConstants.getKeyStroke(m_styleAttributes);
	}

	public Boolean getShadow() {
		return CoStyleConstants.getShadow(m_styleAttributes);
	}

	public Boolean getStrikeThru() {
		return CoStyleConstants.getStrikeThru(m_styleAttributes);
	}

	public com.bluebrim.font.shared.CoFontAttribute getStyle() {
		return CoStyleConstants.getStyle(m_styleAttributes);
	}

	public Boolean getSuperior() {
		return CoStyleConstants.getSuperior(m_styleAttributes);
	}

	public Float getTrackAmount() {
		return CoStyleConstants.getTrackAmount(m_styleAttributes);
	}

	public CoEnumValue getUnderline() {
		return CoStyleConstants.getUnderline(m_styleAttributes);
	}

	public com.bluebrim.font.shared.CoFontAttribute getWeight() {
		return CoStyleConstants.getWeight(m_styleAttributes);
	}

	public CoEnumValue getVerticalPosition() {
		return CoStyleConstants.getVerticalPosition(m_styleAttributes);
	}

	public java.lang.Float getVerticalScale() {
		return CoStyleConstants.getVerticalScale(m_styleAttributes);
	}

	protected void markDirty() {
		m_dirty = !m_dirty;

		//	firePropertyChange( ATTRBUTE_SET_PROPERTY, null, null );
		if (CoAssertion.SIMULATION_SUPPORT)
			CoAssertion.addChangedObject(this);
	}

	public void setAllCaps(Boolean state) {
		CoStyleConstants.setAllCaps(m_styleAttributes, state);
		markDirty();
	}

	public void setAttributes(AttributeSet a) {
		m_styleAttributes = new CoSimpleAttributeSet(a);
		markDirty();
	}

	public void setBaselineOffset(Float blof) {
		CoStyleConstants.setBaselineOffset(m_styleAttributes, blof);
		markDirty();
	}

	public void setFontFamily(String family) {
		CoStyleConstants.setFontFamily(m_styleAttributes, family);
		markDirty();
	}

	public void setFontSize(Float size) {
		CoStyleConstants.setFontSize(m_styleAttributes, size);
		markDirty();
	}

	public void setForegroundColor(String c) {
		CoStyleConstants.setForegroundColor(m_styleAttributes, c);
		markDirty();
	}

	public void setForegroundShade(Float s) {
		CoStyleConstants.setForegroundShade(m_styleAttributes, s);
		markDirty();
	}

	public void setHorizontalScale(java.lang.Float s) {
		CoStyleConstants.setHorizontalScale(m_styleAttributes, s);
		markDirty();
	}

	public void setKeyStroke(javax.swing.KeyStroke ks) {
		CoStyleConstants.setKeyStroke(m_styleAttributes, ks);
		markDirty();
	}

	public void setName(String name) throws CoCharacterStyleIF.NameNotUniqueException {
		CoStyleConstants.set(m_styleAttributes, CoTextConstants.CHARACTER_TAG, name);
		markDirty();
	}

	public void setShadow(Boolean state) {
		CoStyleConstants.setShadow(m_styleAttributes, state);
		markDirty();
	}

	public void setStrikeThru(Boolean state) {
		CoStyleConstants.setStrikeThru(m_styleAttributes, state);
		markDirty();
	}

	public void setStyle(com.bluebrim.font.shared.CoFontAttribute bold) {
		CoStyleConstants.setStyle(m_styleAttributes, bold);
		markDirty();
	}

	public void setSuperior(Boolean state) {
		CoStyleConstants.setSuperior(m_styleAttributes, state);
		markDirty();
	}

	public void setTrackAmount(Float trackAmount) {
		CoStyleConstants.setTrackAmount(m_styleAttributes, trackAmount);
		markDirty();
	}

	public void setUnderline(CoEnumValue underline) {
		CoStyleConstants.setUnderline(m_styleAttributes, underline);
		markDirty();
	}

	public void setWeight(com.bluebrim.font.shared.CoFontAttribute bold) {
		CoStyleConstants.setWeight(m_styleAttributes, bold);
		markDirty();
	}

	public void setVerticalPosition(CoEnumValue vp) {
		CoStyleConstants.setVerticalPosition(m_styleAttributes, vp);
		markDirty();
	}

	public void setVerticalScale(java.lang.Float s) {
		CoStyleConstants.setVerticalScale(m_styleAttributes, s);
		markDirty();
	}

	void collectAttributes(MutableAttributeSet a) {
		if (m_basedOn != null) {
			m_basedOn.collectAttributes(a);
		}

		a.addAttributes(m_styleAttributes);
	}

	public CoCharacterStyleIF getBasedOn() {
		return m_basedOn;
	}

	public AttributeSet getEffectiveAttributes() {
		MutableAttributeSet a = new CoSimpleAttributeSet();

		collectAttributes(a);

		return a;
	}

	public String getIconResourceAnchor() {
		return CoCharacterStyleIF.class.getName();
	}

	public String getIdentity() {
		return getName();
	}

	public boolean getInherit() {
		return m_inherit;
	}

	public String getName() {
		return (String) CoStyleConstants.get(m_styleAttributes, CoTextConstants.CHARACTER_TAG, null);
	}

	public String getType() {
		return getFactoryKey();
	}

	public boolean isDeleted() {
		return m_deleted;
	}

	public void setBasedOn(CoCharacterStyleIF bo) {
		if (m_basedOn == bo)
			return;

		m_basedOn = (CoCharacterStyle) bo;
		markDirty();
	}

	public void setDeleted(boolean i) {
		if (m_deleted == i)
			return;
		m_deleted = i;
		markDirty();
	}

	public void setInherit(boolean i) {
		if (m_inherit == i)
			return;
		m_inherit = i;
		markDirty();
	}

	public String getIconName() {
		return "CoCharacterStyle32.gif";
	}

	public boolean isBasedOn(CoCharacterStyleIF s) {
		if (m_basedOn == null)
			return false;
		if (m_basedOn == s)
			return true;
		return m_basedOn.isBasedOn(s);
	}

	public void xmlAddSubModel(String parameter, Object subModel, CoXmlContext context) {
		if (subModel instanceof CoCharacterStyleIF) {
			setBasedOn((CoCharacterStyleIF) subModel);
		} else
			if (XML_ATTRIBUTES.equals(parameter)) {
				MutableAttributeSet s = (MutableAttributeSet) getAttributes();

				Iterator i = (Iterator) subModel;
				while (i.hasNext()) {
					String tag = (String) i.next();
					String str = (String) i.next();

					CoAttributeTranslator t = CoAttributeTranslator.getTranslator(tag);
					s.addAttribute(t.getAttribute(), t.string2value(str));
				}
			}
	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.export(m_basedOn);

		visitor.exportAttribute(XML_NAME, getName());

		visitor.exportAttribute(XML_DELETED, (m_deleted ? Boolean.TRUE : Boolean.FALSE).toString());
		visitor.exportAttribute(XML_INHERIT, (m_inherit ? Boolean.TRUE : Boolean.FALSE).toString());

		java.util.List l = new ArrayList();

		Enumeration names = getAttributes().getAttributeNames();
		while (names.hasMoreElements()) {
			Object attribute = names.nextElement();
			Object value = getAttributes().getAttribute(attribute);

			CoAttributeTranslator t = CoAttributeTranslator.getTranslator(attribute);
			l.add(t.getTag());
			l.add(t.value2string(value));
		}

		visitor.export(XML_ATTRIBUTES, l);
	}

	public Float getShadowAngle() {
		return CoStyleConstants.getShadowAngle(m_styleAttributes);
	}

	public String getShadowColor() {
		return CoStyleConstants.getShadowColor(m_styleAttributes);
	}

	public Float getShadowOffset() {
		return CoStyleConstants.getShadowOffset(m_styleAttributes);
	}

	public Float getShadowShade() {
		return CoStyleConstants.getShadowShade(m_styleAttributes);
	}

	public void setShadowAngle(Float blof) {
		CoStyleConstants.setShadowAngle(m_styleAttributes, blof);
		markDirty();
	}

	public void setShadowColor(String c) {
		CoStyleConstants.setShadowColor(m_styleAttributes, c);
		markDirty();
	}

	public void setShadowOffset(Float so) {
		CoStyleConstants.setShadowOffset(m_styleAttributes, so);
		markDirty();
	}

	public void setShadowShade(Float s) {
		CoStyleConstants.setShadowShade(m_styleAttributes, s);
		markDirty();
	}

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		return xmlInit(
			((CoTypographyRuleIF) context.getValue(CoTypographyRuleIF.class)).addCharacterStyle(
				node.getAttributes().getNamedItem(XML_NAME).getNodeValue()),
			node);
	}

	protected static CoCharacterStyleIF xmlInit(CoCharacterStyleIF s, Node node) {
		s.setDeleted(
			CoXmlUtilities.parseBoolean(node.getAttributes().getNamedItem(XML_DELETED).getNodeValue(), s.isDeleted()).booleanValue());
		s.setInherit(
			CoXmlUtilities.parseBoolean(node.getAttributes().getNamedItem(XML_INHERIT).getNodeValue(), s.getInherit()).booleanValue());

		/*
		Iterator i = attributes.entrySet().iterator();
		while
			( i.hasNext() )
		{
			Map.Entry e = (Map.Entry) i.next();
			String name = (String) e.getKey();
			
			CoAttributeTranslator translator = CoAttributeTranslator.getTranslator( name );
			if ( translator == null ) continue;
		
			m_styleAttributes.addAttribute( translator.getAttribute(), translator.string2value( (String) CoXmlConstants.getMappedForText( e.getValue() ) ) );
		}
		//PENDING: attributes
		*/

		return s;

	}

	/**
	 * This method is called after an object and all its sub-objects have
	 * been read from an XML file.
	 */
	public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
	}
}