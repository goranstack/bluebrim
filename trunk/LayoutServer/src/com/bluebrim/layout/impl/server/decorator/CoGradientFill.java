package com.bluebrim.layout.impl.server.decorator;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.transact.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of gradient fill style
 * 
 * @author: Dennis Malmström
 */

public class CoGradientFill extends CoFillStyle implements CoGradientFillIF, Cloneable {
	// xlm tags
	public static final String XML_TAG = "gradient-fill";

	public static final String XML_ANGLE = "angle";
	public static final String XML_BLEND_LENGTH = "blend-length";
	public static final String XML_COLOR_1 = "color-1";
	public static final String XML_COLOR_2 = "color-2";
	public static final String XML_CYCLIC = "cyclic";
	public static final String XML_SHADE1 = "shade-1";
	public static final String XML_SHADE2 = "shade-2";
	public static final String XML_X1 = "x1";
	public static final String XML_X2 = "x2";
	public static final String XML_Y1 = "y1";
	public static final String XML_Y2 = "y2";
	private transient CoRef m_colorId1;
	private transient CoRef m_colorId2;

	protected double m_x1;
	protected double m_y1;

	protected double m_x2;
	protected double m_y2;
	protected boolean m_cyclic;

	private CoColorIF m_color1;
	private boolean m_isNoColor1;
	private float m_shade1 = 100;

	private CoColorIF m_color2;
	private boolean m_isNoColor2;
	private float m_shade2 = 100;

	protected double m_angle;
	protected double m_blendLength = -1; // <0 -> relative, >0 -> absolute

	protected class MutableProxy extends CoFillStyle.MutableProxy implements CoRemoteGradientFillIF {
		public boolean getCyclic() {
			return CoGradientFill.this.getCyclic();
		}
		public double getX1() {
			return CoGradientFill.this.getX1();
		}
		public double getY1() {
			return CoGradientFill.this.getY1();
		}
		public double getX2() {
			return CoGradientFill.this.getX2();
		}
		public double getY2() {
			return CoGradientFill.this.getY2();
		}
		public double getAngle() {
			return CoGradientFill.this.getAngle();
		}
		public double getBlendLength() {
			return CoGradientFill.this.getBlendLength();
		}
		public CoColorIF getColor1() {
			return CoGradientFill.this.getColor1();
		}
		public CoColorIF getColor2() {
			return CoGradientFill.this.getColor2();
		}
		public float getShade1() {
			return CoGradientFill.this.getShade1();
		}
		public float getShade2() {
			return CoGradientFill.this.getShade2();
		}
		public CoRef getColorId1() {
			return CoGradientFill.this.getColorId1();
		}
		public CoRef getColorId2() {
			return CoGradientFill.this.getColorId2();
		}

		public void setCyclic(boolean b) {
			CoGradientFill.this.setCyclic(b);
			m_owner.notifyOwner();
		}

		public void setColor1(CoColorIF color) {
			CoGradientFill.this.setColor1(color);
			m_owner.notifyOwner();
		}

		public void setColor2(CoColorIF color) {
			CoGradientFill.this.setColor2(color);
			m_owner.notifyOwner();
		}

		public void setShade1(float s) {
			CoGradientFill.this.setShade1(s);
			m_owner.notifyOwner();
		}

		public void setShade2(float s) {
			CoGradientFill.this.setShade2(s);
			m_owner.notifyOwner();
		}

		public void setPoint1(double x1, double y1) {
			CoGradientFill.this.setPoint1(x1, y1);
			m_owner.notifyOwner();
		}

		public void setPoint2(double x2, double y2) {
			CoGradientFill.this.setPoint2(x2, y2);
			m_owner.notifyOwner();
		}

		public void setAngle(double a) {
			CoGradientFill.this.setAngle(a);
			m_owner.notifyOwner();
		}

		public void setBlendLength(double a) {
			CoGradientFill.this.setBlendLength(a);
			m_owner.notifyOwner();
		}
	};

	public CoGradientFill() {

		super();

		setColor1(getDefaultColor1());
		setShade1(100);
		setColor2(getDefaultColor2());
		setShade2(100);
	}

	/*
	 * Contructor used for XML import.
	 * Helena Rankegård 2001-10-26
	 */

	protected CoGradientFill(Node node, CoXmlContext context) {
		super();

		// xml init
		NamedNodeMap map = node.getAttributes();

		m_angle = CoModelBuilder.getDoubleAttrVal(map, XML_ANGLE, m_angle);
		m_blendLength = CoModelBuilder.getDoubleAttrVal(map, XML_BLEND_LENGTH, m_blendLength);
		m_shade1 = CoModelBuilder.getFloatAttrVal(map, XML_SHADE1, m_shade1);
		m_shade2 = CoModelBuilder.getFloatAttrVal(map, XML_SHADE2, m_shade2);

		m_x1 = CoModelBuilder.getDoubleAttrVal(map, XML_X1, m_x1);
		m_y1 = CoModelBuilder.getDoubleAttrVal(map, XML_Y1, m_y1);
		m_x2 = CoModelBuilder.getDoubleAttrVal(map, XML_X2, m_x2);
		m_y2 = CoModelBuilder.getDoubleAttrVal(map, XML_Y2, m_y2);

		m_cyclic = CoModelBuilder.getBoolAttrVal(map, XML_CYCLIC, m_cyclic);

		String goiStr = CoModelBuilder.getAttrVal(map, XML_COLOR_1, null);
		if (goiStr != null) {
			CoColorIF c = ((CoPageItemPreferencesIF) context.getValue(CoLayoutParameters.class)).getColor(new CoGOI(goiStr));

			if (c == null) {
				System.err.println("Warning: could not find color: \"" + goiStr + " \"");
			} else {
				setColor1(c);
			}
		}

		goiStr = CoModelBuilder.getAttrVal(map, XML_COLOR_2, null);
		if (goiStr != null) {
			CoColorIF c = ((CoPageItemPreferencesIF) context.getValue(CoLayoutParameters.class)).getColor(new CoGOI(goiStr));

			if (c == null) {
				System.err.println("Warning: could not find color: \"" + goiStr + " \"");
			} else {
				setColor2(c);
			}
		}
	}

	public CoGradientFill(CoColorIF c) {
		super();

		setColor1(c);
		setShade1(100);
		setColor2(getDefaultColor2());
		setShade2(100);
	}

	protected CoFillStyle.MutableProxy createMutableProxy() {
		return new CoGradientFill.MutableProxy();
	}

	public CoFillStyleIF deepClone() {
		CoGradientFill c = null;
		try {
			c = (CoGradientFill) clone();
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(getClass() + ".clone() failed");
		}

		return c;
	}

	public boolean equals(Object s) {
		if (s == this)
			return true;

		if (!(s instanceof CoGradientFill))
			return false;

		CoGradientFill g = (CoGradientFill) s;

		return (
			(m_x1 == g.m_x1)
				&& (m_y1 == g.m_y1)
				&& (m_x2 == g.m_x2)
				&& (m_y2 == g.m_y2)
				&& (m_angle == g.m_angle)
				&& (m_blendLength == g.m_blendLength)
				&& (m_cyclic == g.m_cyclic)
				&& (m_shade1 == g.m_shade1)
				&& (m_shade2 == g.m_shade2)
				&& (m_color1.equals(g.m_color1))
				&& (m_color2.equals(g.m_color2)));

	}

	public double getAngle() {
		return m_angle;
	}

	public double getBlendLength() {
		return m_blendLength;
	}

	public CoColorIF getColor1() {
		/*
		if
			( m_cachedColor1 == null )
		{
			m_cachedColor1 = m_colorShade1.getColor();
		}
		
		return m_cachedColor1;
		*/
		return m_color1;

	}

	public CoColorIF getColor2() {
		/*
		if
			( m_cachedColor2 == null )
		{
			m_cachedColor2 = m_colorShade2.getColor();
		}
		
		return m_cachedColor2;
		*/
		return m_color2;
	}

	public CoRef getColorId1() {
		if (m_colorId1 == null) {
			m_colorId1 = m_color1.getId();
		}

		return m_colorId1;

	}

	public CoRef getColorId2() {
		if (m_colorId2 == null) {
			m_colorId2 = m_color2.getId();
		}

		return m_colorId2;

	}

	public boolean getCyclic() {
		return m_cyclic;
	}

	protected CoColorIF getDefaultColor1() {
		return (CoColorIF) CoFactoryManager.createObject(CoNoColor.NO_COLOR);
	}

	protected CoColorIF getDefaultColor2() {
		return (CoColorIF) CoFactoryManager.createObject(CoNoColor.NO_COLOR);
	}

	public String getFactoryKey() {
		return GRADIENT_FILL;
	}

	public Paint getPaint(Rectangle2D bounds) {
		if (m_isNoColor1 || m_isNoColor2) {

			return m_isNoColor1
				? null
				: m_color1.getShadedPreviewColor(m_shade1);
		} else {
			Color c1 = m_color1.getShadedPreviewColor(m_shade1);
			Color c2 = m_color2.getShadedPreviewColor(m_shade2);
			double a = Math.toRadians(m_angle);

			double cosA = Math.cos(a);
			double sinA = Math.sin(a);

			double k = (m_blendLength == 0) ? -1 : m_blendLength / 2;

			if (k < 0) {
				k = Math.min(Math.abs(bounds.getHeight() / sinA), Math.abs(bounds.getWidth() / cosA)) / 2;

				k *= -m_blendLength;
			}

			return new GradientPaint(
				(float) (bounds.getWidth() / 2 - cosA * k),
				(float) (bounds.getHeight() / 2 - sinA * k),
				c1,
				(float) (bounds.getWidth() / 2 + cosA * k),
				(float) (bounds.getHeight() / 2 + sinA * k),
				c2,
				m_cyclic);
		}
	}

	public float getShade1() {
		/*
		if
			( m_cachedShade1 == -1 )
		{
			m_cachedShade1 = m_colorShade1.getShade();
		}
		
		return m_cachedShade1;
		*/
		return m_shade1;
	}

	public float getShade2() {
		/*
		if
			( m_cachedShade2 == -1 )
		{
			m_cachedShade2 = m_colorShade2.getShade();
		}
		
		return m_cachedShade2;
		*/
		return m_shade2;
	}

	public double getX1() {
		return m_x1;
	}

	public double getX2() {
		return m_x2;
	}

	public double getY1() {
		return m_y1;
	}

	public double getY2() {
		return m_y2;
	}

	public void setAngle(double a) {
		m_angle = a;
	}

	public void setBlendLength(double a) {
		m_blendLength = a;
	}

	public void setColor1(CoColorIF color) {
		m_color1 = color;
		m_isNoColor1 = m_color1.getFactoryKey().equals(CoNoColorIF.NO_COLOR);
		m_colorId1 = null;
	}

	public void setColor2(CoColorIF color) {
		m_color2 = color;
		m_isNoColor2 = m_color2.getFactoryKey().equals(CoNoColorIF.NO_COLOR);
		m_colorId2 = null;
	}

	public void setCyclic(boolean cyclic) {
		m_cyclic = cyclic;
	}

	public void setPoint1(double x1, double y1) {
		m_x1 = x1;
		m_y1 = y1;
	}

	public void setPoint2(double x2, double y2) {
		m_x2 = x2;
		m_y2 = y2;
	}

	public void setShade1(float s) {
		m_shade1 = s;
	}

	public void setShade2(float s) {
		m_shade2 = s;
	}

	public void xmlInit(Map attributes, CoXmlContext context) {
		m_angle = CoXmlUtilities.parseDouble((String) attributes.get(XML_ANGLE), m_angle);
		m_blendLength = CoXmlUtilities.parseDouble((String) attributes.get(XML_BLEND_LENGTH), m_blendLength);
		m_shade1 = CoXmlUtilities.parseFloat((String) attributes.get(XML_SHADE1), m_shade1);
		m_shade2 = CoXmlUtilities.parseFloat((String) attributes.get(XML_SHADE2), m_shade2);

		m_x1 = CoXmlUtilities.parseDouble((String) attributes.get(XML_X1), m_x1);
		m_y1 = CoXmlUtilities.parseDouble((String) attributes.get(XML_Y1), m_y1);
		m_x2 = CoXmlUtilities.parseDouble((String) attributes.get(XML_X2), m_x2);
		m_y2 = CoXmlUtilities.parseDouble((String) attributes.get(XML_Y2), m_y2);

		m_cyclic = CoXmlUtilities.parseBoolean((String) attributes.get(XML_CYCLIC), m_cyclic).booleanValue();

		if (context.useGOI()) {
			String goiStr = CoXmlUtilities.parseString((String) attributes.get(XML_COLOR_1), null);
			if (goiStr != null) {
				CoColorIF c = ((CoPageItemPreferencesIF) context.getValue(CoLayoutParameters.class)).getColor(new CoGOI(goiStr));

				if (c == null) {
					System.err.println("Warning: could not find color: \"" + goiStr + " \"");
				} else {
					setColor1(c);
				}
			}

			goiStr = CoXmlUtilities.parseString((String) attributes.get(XML_COLOR_2), null);
			if (goiStr != null) {
				CoColorIF c = ((CoPageItemPreferencesIF) context.getValue(CoLayoutParameters.class)).getColor(new CoGOI(goiStr));

				if (c == null) {
					System.err.println("Warning: could not find color: \"" + goiStr + " \"");
				} else {
					setColor2(c);
				}
			}
		}
	}

	/*
	 * Used at XML export
	 * Helena Rankegård 2001-10-26
	 */

	public void xmlVisit(CoXmlVisitorIF visitor) {
		super.xmlVisit(visitor);

		visitor.exportAttribute(XML_ANGLE, Double.toString(getAngle()));
		visitor.exportAttribute(XML_CYCLIC, (getCyclic() ? Boolean.TRUE : Boolean.FALSE).toString());
		visitor.exportAttribute(XML_BLEND_LENGTH, Double.toString(getBlendLength()));
		visitor.exportAttribute(XML_X1, Double.toString(getX1()));
		visitor.exportAttribute(XML_Y1, Double.toString(getY1()));
		visitor.exportAttribute(XML_X2, Double.toString(getX2()));
		visitor.exportAttribute(XML_Y2, Double.toString(getY2()));
		visitor.exportAttribute(XML_SHADE1, Double.toString(getShade1()));
		visitor.exportAttribute(XML_SHADE2, Double.toString(getShade2()));

		visitor.exportAsGOIorObject(XML_COLOR_1, getColor1());
		visitor.exportAsGOIorObject(XML_COLOR_2, getColor2());
	}

	/*
	 * Used at XML import
	 * Helena Rankegård 2001-10-26
	 */
	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		return new CoGradientFill(node, context);
	}

	public void xmlAddSubModel(String parameter, Object subModel, CoXmlContext context) {
		super.xmlAddSubModel(parameter, subModel, context);
		if (parameter == null)
			return;
		if (!context.useGOI()) {
			if (parameter.equals(XML_COLOR_1)) {
				setColor1((CoColorIF) subModel);
			} else
				if (parameter.equals(XML_COLOR_2)) {
					setColor2((CoColorIF) subModel);
				};
		}
	}

}