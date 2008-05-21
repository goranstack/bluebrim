package com.bluebrim.text.impl.server;
import org.w3c.dom.*;

import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Klass som representerar stycketypografi.
 * Denna klass ärver CoCharacterStyle och adderar ett antal typografiattribut.
 */

public class CoParagraphStyle extends CoCharacterStyle implements CoParagraphStyleIF {

	public static final String XML_TAG = "paragraph-style";

	public CoParagraphStyle(String name) {
		super();

		m_styleAttributes = new CoSimpleAttributeSet();

		CoStyleConstants.set(m_styleAttributes, CoTextConstants.PARAGRAPH_TAG, name);
	}

	public CoCharacterStyle deepClone() {
		CoCharacterStyle s = new CoParagraphStyle(getName());
		s.m_styleAttributes.addAttributes(m_styleAttributes);
		return s;
	}

	public Boolean getAdjustToBaseLineGrid() {
		return CoStyleConstants.getAdjustToBaseLineGrid(m_styleAttributes);
	}

	public CoEnumValue getAlignment() {
		return CoStyleConstants.getAlignment(m_styleAttributes);
	}

	public CoEnumValue getBottomRulerAlignment() {
		return CoStyleConstants.getBottomRulerAlignment(m_styleAttributes);
	}

	public Float getBottomRulerFixedWidth() {
		return CoStyleConstants.getBottomRulerFixedWidth(m_styleAttributes);
	}

	public Float getBottomRulerLeftInset() {
		return CoStyleConstants.getBottomRulerLeftInset(m_styleAttributes);
	}

	public Float getBottomRulerPosition() {
		return CoStyleConstants.getBottomRulerPosition(m_styleAttributes);
	}

	public Float getBottomRulerRightInset() {
		return CoStyleConstants.getBottomRulerRightInset(m_styleAttributes);
	}

	public CoEnumValue getBottomRulerSpan() {
		return CoStyleConstants.getBottomRulerSpan(m_styleAttributes);
	}

	public Float getBottomRulerThickness() {
		return CoStyleConstants.getBottomRulerThickness(m_styleAttributes);
	}

	public Boolean getDropCaps() {
		return CoStyleConstants.getDropCaps(m_styleAttributes);
	}

	public Integer getDropCapsCharacterCount() {
		return CoStyleConstants.getDropCapsCharacterCount(m_styleAttributes);
	}

	public Integer getDropCapsLineCount() {
		return CoStyleConstants.getDropCapsLineCount(m_styleAttributes);
	}

	public String getFactoryKey() {
		return CoTextConstants.PARAGRAPH_STYLE.toString();
	}

	public Float getFirstIndent() {
		return CoStyleConstants.getFirstLineIndent(m_styleAttributes);
	}

	public String getHyphenation() {
		return CoStyleConstants.getHyphenation(m_styleAttributes);
	}

	public CoEnumValue getHyphenationFallbackBehavior() {
		return CoStyleConstants.getHyphenationFallbackBehavior(m_styleAttributes);
	}

	public Boolean getKeepLinesTogether() {
		return CoStyleConstants.getKeepLinesTogether(m_styleAttributes);
	}

	public Boolean getLastInColumn() {
		return CoStyleConstants.getLastInColumn(m_styleAttributes);
	}

	public Float getLeftIndent() {
		return CoStyleConstants.getLeftIndent(m_styleAttributes);
	}
	public Float getRegularTabStopInterval() {
		return CoStyleConstants.getRegularTabStopInterval(m_styleAttributes);
	}

	public Float getRightIndent() {
		return CoStyleConstants.getRightIndent(m_styleAttributes);
	}

	public Float getSpaceAfter() {
		return CoStyleConstants.getSpaceBelow(m_styleAttributes);
	}

	public Float getSpaceBefore() {
		return CoStyleConstants.getSpaceAbove(m_styleAttributes);
	}

	public CoTabSetIF getTabSet() {
		return CoStyleConstants.getTabSet(m_styleAttributes);
	}

	public Boolean getTopOfColumn() {
		return CoStyleConstants.getTopOfColumn(m_styleAttributes);
	}

	public CoEnumValue getTopRulerAlignment() {
		return CoStyleConstants.getTopRulerAlignment(m_styleAttributes);
	}

	public Float getTopRulerFixedWidth() {
		return CoStyleConstants.getTopRulerFixedWidth(m_styleAttributes);
	}

	public Float getTopRulerLeftInset() {
		return CoStyleConstants.getTopRulerLeftInset(m_styleAttributes);
	}

	public Float getTopRulerPosition() {
		return CoStyleConstants.getTopRulerPosition(m_styleAttributes);
	}

	public Float getTopRulerRightInset() {
		return CoStyleConstants.getTopRulerRightInset(m_styleAttributes);
	}

	public CoEnumValue getTopRulerSpan() {
		return CoStyleConstants.getTopRulerSpan(m_styleAttributes);
	}

	public Float getTopRulerThickness() {
		return CoStyleConstants.getTopRulerThickness(m_styleAttributes);
	}

	public Float getTrailingLinesIndent() {
		return CoStyleConstants.getTrailingLinesIndent(m_styleAttributes);
	}

	public void setAdjustToBaseLineGrid(Boolean state) {
		CoStyleConstants.setAdjustToBaseLineGrid(m_styleAttributes, state);
		markDirty();
	}

	public void setAlignment(CoEnumValue alignment) {
		CoStyleConstants.setAlignment(m_styleAttributes, alignment);
		markDirty();
	}

	public void setBottomRulerAlignment(CoEnumValue v) {
		CoStyleConstants.setBottomRulerAlignment(m_styleAttributes, v);
		markDirty();
	}

	public void setBottomRulerFixedWidth(Float v) {
		CoStyleConstants.setBottomRulerFixedWidth(m_styleAttributes, v);
		markDirty();
	}

	public void setBottomRulerLeftInset(Float v) {
		CoStyleConstants.setBottomRulerLeftIndent(m_styleAttributes, v);
		markDirty();
	}

	public void setBottomRulerPosition(Float v) {
		CoStyleConstants.setBottomRulerPosition(m_styleAttributes, v);
		markDirty();
	}

	public void setBottomRulerRightInset(Float v) {
		CoStyleConstants.setBottomRulerRightIndent(m_styleAttributes, v);
		markDirty();
	}

	public void setBottomRulerSpan(CoEnumValue v) {
		CoStyleConstants.setBottomRulerSpan(m_styleAttributes, v);
		markDirty();
	}

	public void setBottomRulerThickness(Float v) {
		CoStyleConstants.setBottomRulerThickness(m_styleAttributes, v);
		markDirty();
	}

	public void setDropCaps(Boolean state) {
		CoStyleConstants.setDropCaps(m_styleAttributes, state);
		markDirty();
	}

	public void setDropCapsCharacterCount(Integer count) {
		CoStyleConstants.setDropCapsCharacterCount(m_styleAttributes, count);
		markDirty();
	}

	public void setDropCapsLineCount(Integer count) {
		CoStyleConstants.setDropCapsLineCount(m_styleAttributes, count);
		markDirty();
	}

	public void setFirstIndent(Float indent) {
		CoStyleConstants.setFirstLineIndent(m_styleAttributes, indent);
		markDirty();
	}

	public void setHyphenation(String b) {
		CoStyleConstants.setHyphenation(m_styleAttributes, b);
		markDirty();
	}

	public void setHyphenationFallbackBehavior(CoEnumValue v) {
		CoStyleConstants.setHyphenationFallbackBehavior(m_styleAttributes, v);
		markDirty();
	}

	public void setKeepLinesTogether(Boolean state) {
		CoStyleConstants.setKeepLinesTogether(m_styleAttributes, state);
		markDirty();
	}

	public void setLastInColumn(Boolean state) {
		CoStyleConstants.setLastInColumn(m_styleAttributes, state);
		markDirty();
	}

	public void setLeftIndent(Float indent) {
		CoStyleConstants.setLeftIndent(m_styleAttributes, indent);
		markDirty();
	}

	public void setName(String name) throws CoCharacterStyleIF.NameNotUniqueException {
		CoStyleConstants.set(m_styleAttributes, CoTextConstants.PARAGRAPH_TAG, name);
	}

	public void setRegularTabStopInterval(Float f) {
		CoStyleConstants.setRegularTabStopInterval(m_styleAttributes, f);
		markDirty();
	}

	public void setRightIndent(Float indent) {
		CoStyleConstants.setRightIndent(m_styleAttributes, indent);
		markDirty();
	}

	public void setSpaceAfter(Float space) {
		CoStyleConstants.setSpaceBelow(m_styleAttributes, space);
		markDirty();
	}

	public void setSpaceBefore(Float space) {
		CoStyleConstants.setSpaceAbove(m_styleAttributes, space);
		markDirty();
	}

	public void setTabSet(CoTabSetIF ts) {
		CoStyleConstants.setTabSet(m_styleAttributes, ts);
		markDirty();
	}

	public void setTopOfColumn(Boolean state) {
		CoStyleConstants.setTopOfColumn(m_styleAttributes, state);
		markDirty();
	}

	public void setTopRulerAlignment(CoEnumValue v) {
		CoStyleConstants.setTopRulerAlignment(m_styleAttributes, v);
		markDirty();
	}

	public void setTopRulerFixedWidth(Float v) {
		CoStyleConstants.setTopRulerFixedWidth(m_styleAttributes, v);
		markDirty();
	}

	public void setTopRulerLeftInset(Float v) {
		CoStyleConstants.setTopRulerLeftIndent(m_styleAttributes, v);
		markDirty();
	}

	public void setTopRulerPosition(Float v) {
		CoStyleConstants.setTopRulerPosition(m_styleAttributes, v);
		markDirty();
	}

	public void setTopRulerRightInset(Float v) {
		CoStyleConstants.setTopRulerRightIndent(m_styleAttributes, v);
		markDirty();
	}

	public void setTopRulerSpan(CoEnumValue v) {
		CoStyleConstants.setTopRulerSpan(m_styleAttributes, v);
		markDirty();
	}

	public void setTopRulerThickness(Float v) {
		CoStyleConstants.setTopRulerThickness(m_styleAttributes, v);
		markDirty();
	}

	public void setTrailingLinesIndent(Float indent) {
		CoStyleConstants.setTrailingLinesIndent(m_styleAttributes, indent);
		markDirty();
	}

	public String getIconName() {
		return "CoParagraphStyle32.gif";
	}

	public String getName() {
		return (String) CoStyleConstants.get(m_styleAttributes, CoTextConstants.PARAGRAPH_TAG, null);
	}

	public Float getMinimumSpaceWidth() {
		return CoStyleConstants.getMinimumSpaceWidth(m_styleAttributes);
	}

	public Float getOptimumSpaceWidth() {
		return CoStyleConstants.getOptimumSpaceWidth(m_styleAttributes);
	}

	public void setMinimumSpaceWidth(Float v) {
		CoStyleConstants.setMinimumSpaceWidth(m_styleAttributes, v);
		markDirty();
	}

	public void setOptimumSpaceWidth(Float v) {
		CoStyleConstants.setOptimumSpaceWidth(m_styleAttributes, v);
		markDirty();
	}

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		return xmlInit(
			((CoTypographyRuleIF) context.getValue(CoTypographyRuleIF.class)).addParagraphStyle(node.getAttributes().getNamedItem(XML_NAME).getNodeValue()),
			node);
	}

	public CoLeading getLeading() {
		return CoStyleConstants.getLeading(m_styleAttributes);
	}

	public void setLeading(CoLeading leading) {
		CoStyleConstants.setLeading(m_styleAttributes, leading);
		markDirty();
	}
}