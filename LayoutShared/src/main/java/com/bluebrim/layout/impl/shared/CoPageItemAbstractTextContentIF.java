package com.bluebrim.layout.impl.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.text.shared.*;

/**
 * RMI-enabling interface for class CoPageItemAbstractTextContent.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */
public interface CoPageItemAbstractTextContentIF extends CoPageItemContentIF {
	// first baseline values
	public static final String CAP_HEIGHT = CoTextGeometryIF.BASELINE_CAP_HEIGHT;
	public static final String CAP_ACCENT = CoTextGeometryIF.BASELINE_CAP_ACCENT;
	public static final String ASCENT = CoTextGeometryIF.BASELINE_ASCENT;

	// vertical alignment values
	public static final String ALIGN_TOP = CoTextGeometryIF.ALIGN_TOP;
	public static final String ALIGN_CENTERED = CoTextGeometryIF.ALIGN_CENTERED;
	public static final String ALIGN_BOTTOM = CoTextGeometryIF.ALIGN_BOTTOM;
	public static final String ALIGN_JUSTIFIED = CoTextGeometryIF.ALIGN_JUSTIFIED;

	// text lock values
	public static int UNLOCKED = 0; // text can be set and replaced
	public static int LOCKED = 1; // text can be set but not replaced
	public static int FIXED = 2; // text can't be set or replaced

	// see CoPageItemIF.State
	public static class State extends CoPageItemContentIF.State {
		// page item state needed by view, see CoPageItemAbstractTextContent for details
		public CoFormattedText m_text;
		public boolean m_isNull;
		public String m_firstBaselineType;
		public float m_firstBaselineOffset;
		public String m_verticalAlignmentType;
		public float m_verticalAlignmentMaxInter;
		public CoImmutableColumnGridIF m_columnGrid;
		public CoImmutableBaseLineGridIF m_baseLineGrid;
		public CoImmutableShapeIF m_columnGridShape;
		public float m_leftMargin;
		public float m_rightMargin;
		public float m_topMargin;
		public float m_bottomMargin;
		public int m_textLock;
	};

	public static class ViewState extends CoPageItemIF.ViewState {
		public long m_documentTimestamp;
	};

	void adjustHeightToText();

	public float getFirstBaselineOffset();
	public String getFirstBaselineType();

	public float getVerticalAlignmentMaxInter();
	public String getVerticalAlignmentType();

	public void setFirstBaselineOffset(float offset);
	public void setFirstBaselineType(String type);

	public void setVerticalAlignmentMaxInter(float value);
	public void setVerticalAlignmentType(String type);

	public float getBottomMargin();

	public float getLeftMargin();

	public float getRightMargin();

	public float getTopMargin();

	void setBottomMargin(float value);

	void setLeftMargin(float value);

	void setRightMargin(float value);

	void setTopMargin(float value);

	public CoFormattedText getFormattedText();

	public CoFormattedText getMutableFormattedText();

	void setTextLock(int l);

	void updateFormattedText(CoFormattedText text);
}