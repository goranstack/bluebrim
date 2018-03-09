package com.bluebrim.layout.impl.server;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.layout.impl.server.geom.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * A page item designed to work as the "background" in a layout editor.
 * Its dimensions are about 35000000 x 35000000 m which for all practical uses is basically infinite.
 * A desktop layout area doesn't have a parent.
 * 
 * @author: Dennis Malmström
 */

public class CoDesktopLayoutArea extends CoCompositePageItem implements CoDesktopLayoutAreaIF {
	private CoLayoutParameters m_layoutParameters;
	protected CoCustomGrid m_customGrid;
	private RemoteCustomGrid m_mutableCustomGridProxy = new RemoteCustomGrid();
	// xml tag constants
	public static final String XML_TAG = "desktop-layout-area";
	public static final String XML_X_GRID_LINES = "x-grid-lines";
	public static final String XML_Y_GRID_LINES = "y-grid-lines";

	private class RemoteCustomGrid implements CoRemoteCustomGridIF {
		public void addPropertyChangeListener(CoPropertyChangeListener l) {
			m_customGrid.addPropertyChangeListener(l);
		}
		public void removePropertyChangeListener(CoPropertyChangeListener l) {
			m_customGrid.removePropertyChangeListener(l);
		}
		public CoImmutableSnapGridIF deepClone() {
			return m_customGrid.deepClone();
		}
		public void xmlVisit(CoXmlVisitorIF visitor) {
		};
		public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
		}
		public void xmlImportFinished(Node node, CoXmlContext context) {
		}

		public int getXPositionCount() {
			return m_customGrid.getXPositionCount();
		}
		public int getYPositionCount() {
			return m_customGrid.getYPositionCount();
		}
		public double getXPosition(int i) {
			return m_customGrid.getXPosition(i);
		}
		public double getYPosition(int i) {
			return m_customGrid.getYPosition(i);
		}
		public double findXPosition(double pos, double range) {
			return m_customGrid.findXPosition(pos, range);
		}
		public double findYPosition(double pos, double range) {
			return m_customGrid.findYPosition(pos, range);
		}
		public Collection getGridLines() {
			return m_customGrid.getGridLines();
		}
		public Point2D snap(double d0, double d1, double d2, double d3, double d4, Point2D p) {
			return m_customGrid.snap(d0, d1, d2, d3, d4, p);
		}
		public Point2D snap(double d0, double d1, double d2, int i0, int i1, boolean b, Point2D p) {
			return m_customGrid.snap(d0, d1, d2, i0, i1, b, p);
		}
		public Shape getShape(int i) {
			return m_customGrid.getShape(i);
		}

		public void addXPosition(double x) {
			m_customGrid.addXPosition(x);
			doAfterCustomGridChanged();
		}

		public void addYPosition(double y) {
			m_customGrid.addYPosition(y);
			doAfterCustomGridChanged();
		}

		public void removeXPosition(double x) {
			m_customGrid.removeXPosition(x);
			doAfterCustomGridChanged();
		}

		public void removeYPosition(double y) {
			m_customGrid.removeYPosition(y);
			doAfterCustomGridChanged();
		}

		public void removeXPosition(int i) {
			m_customGrid.removeXPosition(i);
			doAfterCustomGridChanged();
		}

		public void removeYPosition(int i) {
			m_customGrid.removeYPosition(i);
			doAfterCustomGridChanged();
		}

		public void removeAllXPositions() {
			m_customGrid.removeAllXPositions();
			doAfterCustomGridChanged();
		}

		public void removeAllYPositions() {
			m_customGrid.removeAllYPositions();
			doAfterCustomGridChanged();
		}
	}

	protected CoBaseLineGrid createDefaultBaseLineGrid() {
		return new CoRegularBaseLineGrid(0, 0);
	}

	public String getName() {
		return getType();
	}

	public String getType() {
		return CoPageItemStringResources.getName(DESKTOP_LAYOUT);
	}

	// This method usually returns the page item preferences of the owner of the desktop layout area.

	public CoPageItemPreferencesIF getPreferences() {
		return (CoPageItemPreferencesIF) getLayoutParameters();
	}

	public CoLayoutParameters getLayoutParameters() {
		return m_layoutParameters;
	}

	public Object getAttributeValue(java.lang.reflect.Field d) throws IllegalAccessException {
		try {
			return d.get(this);
		} catch (IllegalAccessException ex) {
			return super.getAttributeValue(d);
		}
	}

	public void setParent(CoCompositePageItem parent) {
		CoAssertion.assertTrue(parent == null, "Desktop layout area can't have a parent");
	}

	public CoDesktopLayoutArea(CoLayoutParameters layoutParameters) {
		super();

		m_layoutParameters = layoutParameters;

		getMutableCoShape().setSize(INFINITE_DIMENSION, INFINITE_DIMENSION);

		m_customGrid = new CoCustomGrid();
		m_customGrid.setSize(INFINITE_DIMENSION, INFINITE_DIMENSION);
	}

	protected void collectState(CoPageItemIF.State s, CoPageItemIF.ViewState viewState) {
		super.collectState(s, viewState);

		CoDesktopLayoutAreaIF.State S = (CoDesktopLayoutAreaIF.State) s;

		S.m_customGrid = m_customGrid;
	}

	// See CoPageItem.createState

	public CoPageItemIF.State createState() {
		return new CoDesktopLayoutAreaIF.State();
	}

	protected void deepCopy(CoPageItem copy) {
		super.deepCopy(copy);

		CoDesktopLayoutArea c = (CoDesktopLayoutArea) copy;

		c.m_customGrid = (CoCustomGrid) m_customGrid.deepClone();
		c.m_mutableCustomGridProxy = c.new RemoteCustomGrid();
	}

	private void doAfterCustomGridChanged() {
		postCustomGridChanged();

		markDirty("CustomGridChanged");
	}

	protected com.bluebrim.text.shared.CoTextStyleApplier doGetTextStyleApplier() {
		return getPreferences().getTextStyleApplier();
	}

	public CoImmutableCustomGridIF getCustomGrid() {
		return m_customGrid;
	}

	public CoDesktopLayoutAreaIF getDesktop() {
		return this;
	}

	public CoCustomGridIF getMutableCustomGrid() {
		return m_mutableCustomGridProxy;
	}

	public boolean hasFiniteDimensions() {
		return false;
	}

	// This apge item can't create its view.
	// The view must be created by the client.

	protected CoShapePageItemView newView(CoCompositePageItemView parent, int detailMode) {
		CoAssertion.assertTrue(false, "Illegal call");
		return null;
	}

	protected void postCustomGridChanged() {
	}

	public void visit(CoPageItemVisitor visitor, Object anything, boolean goDown) {
		visitor.doToDesktopLayoutArea(this, anything, goDown);
	}

	/*
	 * Used at XML export
	 * Helena Rankegård 2001-10-23
	 */

	public void xmlVisit(CoXmlVisitorIF visitor) {
		super.xmlVisit(visitor);

		StringBuffer s = new StringBuffer();
		int I = m_customGrid.getXPositionCount();
		for (int i = 0; i < I; i++) {
			if (i > 0)
				s.append(' ');
			s.append(m_customGrid.getXPosition(i));
		}
		if (I > 0)
			visitor.exportAttribute(XML_X_GRID_LINES, s.toString());

		s.setLength(0);
		I = m_customGrid.getYPositionCount();
		for (int i = 0; i < I; i++) {
			if (i > 0)
				s.append(' ');
			s.append(m_customGrid.getYPosition(i));
		}
		if (I > 0)
			visitor.exportAttribute(XML_Y_GRID_LINES, s.toString());
	}

	/*
	 * Used at XML import.
	 * Helena Rankegård 2001-10-30
	 */

	public void xmlInit(NamedNodeMap map, CoXmlContext context) {
		super.xmlInit(map, context);

		// xml init
		m_customGrid.removeAllXPositions();
		m_customGrid.removeAllYPositions();

		String tmp = CoModelBuilder.getAttrVal(map, XML_X_GRID_LINES, null);

		if (tmp != null) {
			StringTokenizer t = new StringTokenizer(tmp, " ");

			int i = 0;
			while (t.hasMoreTokens()) {
				double x = CoXmlUtilities.parseDouble(t.nextToken(), Double.NaN);
				if (Double.isNaN(x)) {
					throw new IllegalArgumentException("invalid x custom grid line");
				}

				m_customGrid.addXPosition(x);
			}
		}

		tmp = CoModelBuilder.getAttrVal(map, XML_Y_GRID_LINES, null);

		if (tmp != null) {
			StringTokenizer t = new StringTokenizer(tmp, " ");

			int i = 0;
			while (t.hasMoreTokens()) {
				double y = CoXmlUtilities.parseDouble(t.nextToken(), Double.NaN);
				if (Double.isNaN(y)) {
					throw new IllegalArgumentException("invalid y custom grid line");
				}

				m_customGrid.addYPosition(y);
			}
		}
	}

	public String getFactoryKey() {
		return DESKTOP_LAYOUT;
	}

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		CoDesktopLayoutArea desktopArea = new CoDesktopLayoutArea((CoLayoutParameters)context.getValue(CoLayoutParameters.class));
		desktopArea.xmlInit(node.getAttributes(), context);
		return desktopArea;
	}

}