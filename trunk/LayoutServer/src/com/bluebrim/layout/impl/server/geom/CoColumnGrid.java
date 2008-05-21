package com.bluebrim.layout.impl.server.geom;

import java.awt.geom.*;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Abstract superclass to column grids.
 * 
 * @author: Dennis Malmström
 */
public abstract class CoColumnGrid extends CoSnapGrid implements CoImmutableColumnGridIF {
	// xml tag constant
	public static final String XML_TYPE = "type";

	protected static final double MIN_COLUMN_WIDTH = 0.5;
	protected static final double MIN_COLUMN_HEIGHT = 0.5;
	
	// horizontal interval for which m_gridLines is valid, NaN means m_gridlines is complete
	private double m_gridLinesX0 = Double.NaN;
	private double m_gridLinesX1 = Double.NaN;
	protected boolean m_isLeftOutside = false;
	

	public CoColumnGrid() {
		super();
	}

	protected abstract void createGridLines();

	protected abstract void createGridLines(double x0, double x1);

	public CoColumnGrid createSnapshot() {
		return createSnapshot(0, 0, m_width, m_height);
	}

	protected abstract CoColumnGrid createSnapshot(double x, double y, double w, double h);
	
	/**
	 * Return the width for the specified number of columns. If specified number of
	 * columns exceed the number of columns in the <code>CoColumnGrid</code> the result
	 * will be based on number of columns in the <code>CoColumnGrid</code>.
	 * @param numberOfColumns
	 * @return double
	 */
	public double getWidthFor(int numberOfColumns) {
	
		int count = Math.min( numberOfColumns, getColumnCount() );
		double columnGap = ( count <= 1 ) ? 0 : getColumnSpacing();
		return ( getColumnWidth() + columnGap ) * count - columnGap;
	}

	/**
	 * Return the distance between left and right margin
	 */ 
	public double getLiveAreaWidth() {
		return getRightMarginPosition() - getLeftMarginPosition();
	}

	/**
	 * Return the distance between top and bottom margin
	 */ 
	public double getLiveAreaHeight() {
		return getBottomMarginPosition() - getTopMarginPosition();
	}

	public CoImmutableColumnGridIF derive(double x, double y, double w, double h) {
		CoDerivedColumnGrid grid = new CoDerivedColumnGrid(this);
		grid.setBounds(x, y, w, h, m_isLeftOutside);
		return grid;
	}

	double getBottomMarginPosition(double y, double height) {
		double m = getBottomMarginPosition();
		if (y + height < m) {
			return height;
		} else {
			return m - y;
		}
	}

	public CoColumnGeometryIF getColumnGeometry(CoImmutableShapeIF shape) {
		return getColumnGeometry(shape, 0, 0);
	}

	protected abstract CoColumnGeometryIF getColumnGeometry(CoImmutableShapeIF shape, double x, double y);

	protected abstract double getDx();

	public String getFactoryKey() {
		return COLUMN_GRID;
	}

	/**
	 * Returns AT LEAST the gridlines inside the specified horizontal interval.
	 */
	protected Collection getGridLines(double x, double width) {
		if (m_gridLines != null) {
			// some gridlines are available, check if the interval is covered
			if (!Double.isNaN(m_gridLinesX0) && ((x < m_gridLinesX0) || (x + width > m_gridLinesX1))) {
				// interval not covered -> calculate new interval and create its gridlines
				m_gridLinesX0 = Math.min(x, m_gridLinesX0);
				m_gridLinesX1 = Math.max(x + width, m_gridLinesX1);
				createGridLines(m_gridLinesX0, m_gridLinesX1);
			}

		} else {
			// no gridlines ready
			if (getColumnCount() < 20) {
				// small number of columns -> create all gridlines
				createGridLines();
			} else {
				// large number of columns -> create only requested gridlines
				m_gridLinesX0 = x;
				m_gridLinesX1 = x + width;
				createGridLines(m_gridLinesX0, m_gridLinesX1);
			}
		}

		return m_gridLines;
	}

	double getLeftMarginPosition(double x, double width) {
		double m = getLeftMarginPosition();
		if (x > m) {
			return 0;
		} else {
			return m - x;
		}
	}

	double getRightMarginPosition(double x, double width) {
		double m = getRightMarginPosition();
		if (x + width < m) {
			return width;
		} else {
			return m - x;
		}
	}

	public java.awt.Shape getShape(int detailMask) {
		java.awt.geom.GeneralPath shape = new GeneralPath();
		boolean isEmpty = true;

		Iterator lines = getGridLines().iterator();

		CoGridLineIF l = null;

		// edges

		while (lines.hasNext()) {
			l = (CoGridLineIF) lines.next();

			if (l.getType() == CoGridLineIF.EDGE)
				continue;

			if (l.getType() == CoGridLineIF.MARGIN) {
				if ((detailMask & MARGINS) != 0) {
					java.awt.geom.Line2D s = l.getShape();
					double x1 = s.getX1();
					double y1 = s.getY1();
					double x2 = s.getX2();
					double y2 = s.getY2();

					final double delta = 0.5;
					while (true) {
						if (x1 == x2) {
							if (Math.abs(x1) < delta)
								break;
							if (Math.abs(x1 - m_width) < delta)
								break;
						}
						if (y1 == y2) {
							if (Math.abs(y1) < delta)
								break;
							if (Math.abs(y1 - m_height) < delta)
								break;
						}

						shape.moveTo((float) x1, (float) y1);
						shape.lineTo((float) x2, (float) y2);
						isEmpty = false;
						break;
					}
				}

				continue;
			}

			if (l.getType() == CoGridLineIF.COLUMN) {
				if ((detailMask & COLUMNS) != 0) {
					java.awt.geom.Line2D s = l.getShape();
					shape.moveTo((float) s.getX1(), (float) s.getY1());
					shape.lineTo((float) s.getX2(), (float) s.getY2());
					isEmpty = false;
				}

				if (lines.hasNext()) {
					l = (CoGridLineIF) lines.next();

					if ((l.getType() == CoGridLineIF.COLUMN) && ((detailMask & COLUMNS) != 0) && ((detailMask & COLUMN_SPACINGS) != 0)) {
						java.awt.geom.Line2D s = l.getShape();
						shape.moveTo((float) s.getX1(), (float) s.getY1());
						shape.lineTo((float) s.getX2(), (float) s.getY2());
						isEmpty = false;
					}
				}

				continue;
			}
		}

		if (isEmpty) {
			shape = null;
		}

		return shape;
	}

	double getTopMarginPosition(double y, double height) {
		double m = getTopMarginPosition();
		if (y > m) {
			return 0;
		} else {
			return m - y;
		}
	}

	protected abstract void invalidate();

	public boolean isDerived() {
		return false;
	}

	public void setBounds(double x, double y, double w, double h, boolean isLeftOutside) {
		m_isLeftOutside = isLeftOutside;
		setLocation(x, y);
		setSize(w, h);
	}

	/*
	 * Used at XML import
	 * Helena Rankegård 2001-10-29
	 */
	public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
	}
	
	/*
	 * Used at XML import
	 * Helena Rankegård 2001-10-29
	 */
	public void xmlImportFinished(Node node, CoXmlContext context) {
	}

	/*
	 * Used at XML export
	 * Helena Rankegård 2001-10-29
	 */
	public void xmlVisit(CoXmlVisitorIF visitor) {
	}
}