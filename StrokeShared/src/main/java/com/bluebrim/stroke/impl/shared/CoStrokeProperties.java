package com.bluebrim.stroke.impl.shared;
import com.bluebrim.base.shared.*;
import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.stroke.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * This class encapsulates stroking data.
 * It has no rendering behavior.
 * 
 * @author: Dennis Malmström
 */

public class CoStrokeProperties extends CoSimpleObject implements CoStrokePropertiesIF {
	// line stroke state
	private CoStrokeIF m_stroke;
	private float m_width = 0;
	private int m_alignment = ALIGN_INSIDE;
	private float m_alignOffset;
	private float m_alignmentPosition = 0;

	private CoColorIF m_foregroundColor = (CoColorIF) CoFactoryManager.createObject(CoProcessBlackIF.PROCESS_BLACK);
	private float m_foregroundShade = 100;
	private CoColorIF m_backgroundColor = (CoColorIF) CoFactoryManager.createObject(CoNoColorIF.NO_COLOR);
	private float m_backgroundShade = 100;
	private transient CoRef m_backgroundColorId;
	private transient CoRef m_foregroundColorId;
	private String m_symmetry = NON_SYMMETRIC;

	/* text stroke state
	private String m_text = null;
	private String m_fontFamily = "Serif";
	private float m_baselineOffset = 0;
	private float m_fontSize = 12;
	private int m_fontStyle = Font.PLAIN;
	private float m_repeatGap = 0;
	private boolean m_repeat = false;
	private Font m_font;
	private CoColorIF m_textColor = (CoColorIF) CoFactoryManager.createObject(CoProcessBlackIF.PROCESS_BLACK);
	private float m_textShade = 100;
	private transient CoRef m_textColorId;
	*/

	public CoStrokeProperties() {
		setStroke(getDefaultStroke());
		setWidth(0);
	}
	public CoStrokePropertiesIF deepClone() {
		CoStrokeProperties c = null;

		try {
			c = (CoStrokeProperties) clone();
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(getClass() + ".clone() failed");
		}

		try {
			if (m_backgroundColor != null)
				c.m_backgroundColor = m_backgroundColor.deepClone();
			if (m_foregroundColor != null)
				c.m_foregroundColor = m_foregroundColor.deepClone();
		} catch (Exception e) {
			System.out.println("GARF! Bryr mig inte just nu, fixa Dennis.");
		}

		return c;
	}

	public int getAlignment() {
		return m_alignment;
	}
	public float getAlignOffset() {
		return m_alignOffset;
	}

	public float getBackgroundShade() {
		return m_backgroundShade;
	}
	private static CoStrokeIF getDefaultStroke() {
		return (CoStrokeIF) CoFactoryManager.createObject(CoStrokeIF.SOLID);
	}
	public String getFactoryKey() {
		return STROKE_PROPERTIES;
	}
	public float getForegroundShade() {
		return m_foregroundShade;
	}
	public float getInsideWidth() {
		return m_width * (1 - m_alignmentPosition);
	}
	public float getOutsideWidth() {
		return m_width * m_alignmentPosition;
	}
	public CoStrokeIF getStroke() {
		return m_stroke;
	}
	public float getWidth() {
		return m_width;
	}
	public void setAlignment(int a) {
		m_alignment = a;
		switch (m_alignment) {
			case ALIGN_INSIDE :
				m_alignmentPosition = 0;
				break;

			case ALIGN_CENTER :
				m_alignmentPosition = 0.5f;
				break;

			case ALIGN_OUTSIDE :
				m_alignmentPosition = 1;
				break;

			case ALIGN_BY_OFFSET :
				m_alignmentPosition = m_alignOffset;
				break;
		}
	}
	public void setAlignOffset(float ao) {
		m_alignOffset = ao;
		if (m_alignment == ALIGN_BY_OFFSET)
			m_alignmentPosition = m_alignOffset;
	}

	public void setBackgroundShade(float s) {
		m_backgroundShade = s;
	}
	public void setForegroundShade(float s) {
		m_foregroundShade = s;
	}
	public void setStroke(CoStrokeIF ss) {
		m_stroke = ss;
	}
	public void setWidth(float w) {
		m_width = w;
		//	m_font = null;
	}
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof CoImmutableStrokePropertiesIF))
			return false;

		CoImmutableStrokePropertiesIF s = (CoImmutableStrokePropertiesIF) o;

		return (m_width == s.getWidth())
			&& (m_alignment == s.getAlignment())
			&& (m_alignOffset == s.getAlignOffset())
			&& (m_symmetry.equals(s.getSymmetry()))
			&& (m_foregroundShade == s.getForegroundShade())
			&& (m_backgroundShade == s.getBackgroundShade())
			&& (m_foregroundColor.equals(s.getForegroundColor()))
			&& (m_backgroundColor.equals(s.getBackgroundColor()))
			&& (m_stroke.equals(s.getStroke()));
	}

	public CoColorIF getBackgroundColor() {
		return m_backgroundColor;
	}

	public CoRef getBackgroundColorId() {
		if (m_backgroundColorId == null) {
			m_backgroundColorId = m_backgroundColor.getId();
		}

		return m_backgroundColorId;
	}

	public CoColorIF getForegroundColor() {
		return m_foregroundColor;
	}

	public CoRef getForegroundColorId() {
		if (m_foregroundColorId == null) {
			m_foregroundColorId = m_foregroundColor.getId();
		}

		return m_foregroundColorId;
	}

	public String getSymmetry() {
		return m_symmetry;
	}

	public void setBackgroundColor(CoColorIF c) {
		m_backgroundColor = c;
		m_backgroundColorId = null;
	}

	public void setForegroundColor(CoColorIF c) {
		m_foregroundColor = c;
		m_foregroundColorId = null;
	}

	public void setSymmetry(String s) {
		m_symmetry = s;
	}

}