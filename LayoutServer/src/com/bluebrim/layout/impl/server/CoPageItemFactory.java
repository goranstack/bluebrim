package com.bluebrim.layout.impl.server;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Map;

import com.bluebrim.base.shared.CoFactoryElementIF;
import com.bluebrim.base.shared.geom.CoGeometryConstants;
import com.bluebrim.layout.impl.server.decorator.CoGradientFill;
import com.bluebrim.layout.impl.server.geom.CoBoundingBoxRunAroundSpec;
import com.bluebrim.layout.impl.server.geom.CoNoneRunAroundSpec;
import com.bluebrim.layout.impl.server.geom.CoShapeRunAroundSpec;
import com.bluebrim.layout.impl.shared.CoBoundingBoxRunAroundSpecIF;
import com.bluebrim.layout.impl.shared.CoContentWrapperPageItemIF;
import com.bluebrim.layout.impl.shared.CoFillStyleIF;
import com.bluebrim.layout.impl.shared.CoLayoutAreaIF;
import com.bluebrim.layout.impl.shared.CoNoneRunAroundSpecIF;
import com.bluebrim.layout.impl.shared.CoPageItemAbstractTextContentIF;
import com.bluebrim.layout.impl.shared.CoPageItemContentIF;
import com.bluebrim.layout.impl.shared.CoPageItemFactoryIF;
import com.bluebrim.layout.impl.shared.CoPageItemImageContentIF;
import com.bluebrim.layout.impl.shared.CoPageItemLayoutContentIF;
import com.bluebrim.layout.impl.shared.CoPageItemNoContentIF;
import com.bluebrim.layout.impl.shared.CoPageItemTextContentIF;
import com.bluebrim.layout.impl.shared.CoPageItemWorkPieceTextContentIF;
import com.bluebrim.layout.impl.shared.CoPageLayoutAreaIF;
import com.bluebrim.layout.impl.shared.CoRunAroundSpecIF;
import com.bluebrim.layout.impl.shared.CoShapeRunAroundSpecIF;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layout.shared.CoImmutableColumnGridIF;

/**
 * Factroy class for creating page items and page item slave positions.
 *
 * @author: Dennis
 */
public class CoPageItemFactory implements CoPageItemFactoryIF {
	
	private CoTextDistribution m_nullTextDistribution = new CoTextDistribution(null, null, null) {
		public boolean isNull() {
			return true;
		}

	};

	private Map m_runAroundSpecs;
	// slave position singletons
	private final CoShapePageItem.SlavePosition[] m_slavePositions;
	private final CoShapePageItem.SlavePosition m_slaveNoPosition = new CoShapePageItem.SlavePosition();

	private static abstract class SlaveLeftPosition extends CoShapePageItem.SlavePosition {
		public double getLeftEdgeDelta(CoShapePageItem slave) {
			return -slave.getWidth();
		}
		public void performLayout(CoShapePageItem slave) {
			CoShapePageItem master = getMaster(slave);

			double x0 = 0;
			double y = master.getY();
			double w = 0;

			if (master.getParent() instanceof CoDesktopLayoutArea) {
				x0 = master.getX() - slave.getWidth() - master.getRunAroundSpec().getLeftMargin();
				w = slave.getWidth();
			} else {
				CoImmutableColumnGridIF g = master.getParent().getColumnGrid();

				double x1 = master.getX() - master.getRunAroundSpec().getLeftMargin();
				x0 = g.snap(x1, y, Double.POSITIVE_INFINITY, CoGeometryConstants.LEFT_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null).getX();
				w = x1 - x0;
				if (Math.abs(w) < 0.001)
					w = 0;
				if (w < 0) {
					w += g.getColumnWidth() + g.getColumnSpacing();
					x0 -= g.getColumnWidth() + g.getColumnSpacing();
				}
			}

			double h = slave.getContentHeight();
			slave.setLayoutWidth(w);
			slave.setLayoutHeight(h);
			slave.setLayoutLocation(x0, getY(y, master.getHeight(), h));
		}

		protected abstract double getY(double y, double masterH, double slaveH);
	};

	private static class SlaveTopLeftPosition extends SlaveLeftPosition {
		public int getKey() {
			return CoShapePageItemIF.SLAVE_TOP_LEFT_POSITION;
		}
		protected double getY(double y, double masterH, double slaveH) {
			return y;
		}
	};

	private static class SlaveBottomLeftPosition extends SlaveLeftPosition {
		public int getKey() {
			return CoShapePageItemIF.SLAVE_BOTTOM_LEFT_POSITION;
		}
		protected double getY(double y, double masterH, double slaveH) {
			return y + masterH - slaveH;
		}
	};

	private abstract static class SlaveRightPosition extends CoShapePageItem.SlavePosition {
		public double getRightEdgeDelta(CoShapePageItem slave) {
			return slave.getWidth();
		}

		public void performLayout(CoShapePageItem slave) {
			CoShapePageItem master = getMaster(slave);

			double x0 = master.getX() + master.getWidth() + master.getRunAroundSpec().getRightMargin();
			double y = master.getY();
			double w = 0;
			if (master.getParent() instanceof CoDesktopLayoutArea) {
				w = slave.getWidth() - master.getRunAroundSpec().getRightMargin();
			} else {
				CoImmutableColumnGridIF g = master.getParent().getColumnGrid();
				double x1 = g.snap(x0, y, Double.POSITIVE_INFINITY, CoGeometryConstants.RIGHT_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null).getX();
				w = x1 - x0;
				if (Math.abs(w) < 0.001)
					w = 0;
				if (w < 0)
					w += g.getColumnWidth() + g.getColumnSpacing();
			}

			double h = slave.getContentHeight();
			slave.setLayoutWidth(w);
			slave.setLayoutHeight(h);
			slave.setLayoutLocation(x0, getY(y, master.getHeight(), h));
		}

		protected abstract double getY(double y, double masterH, double slaveH);
	};

	private static class SlaveTopRightPosition extends SlaveRightPosition {
		public int getKey() {
			return CoShapePageItemIF.SLAVE_TOP_RIGHT_POSITION;
		}

		protected double getY(double y, double masterH, double slaveH) {
			return y;
		}
	};

	private static class SlaveBottomRightPosition extends SlaveRightPosition {
		public int getKey() {
			return CoShapePageItemIF.SLAVE_BOTTOM_RIGHT_POSITION;
		}

		protected double getY(double y, double masterH, double slaveH) {
			return y + masterH - slaveH;
		}
	};

	private static class SlaveTopPosition extends CoShapePageItem.SlavePosition {
		public int getKey() {
			return CoShapePageItemIF.SLAVE_TOP_POSITION;
		}
		public double getTopEdgeDelta(CoShapePageItem slave) {
			return -slave.getContentHeight() - getMaster(slave).getRunAroundSpec().getTopMargin();
		}
		public double getLayoutHeightDelta(CoShapePageItem slave) {
			return slave.getContentHeight() + getMaster(slave).getRunAroundSpec().getTopMargin();
		}
		public void performLayout(CoShapePageItem slave) {
			CoShapePageItem master = getMaster(slave);
			slave.setLayoutWidth(master.getWidth());
			slave.setLayoutLocation(master.getX(), master.getY() - slave.getContentHeight() - master.getRunAroundSpec().getTopMargin());
			slave.setLayoutHeight(slave.getContentHeight());
		}
	};

	private static class SlaveBottomPosition extends CoShapePageItem.SlavePosition {
		public int getKey() {
			return CoShapePageItemIF.SLAVE_BOTTOM_POSITION;
		}
		public double getBottomEdgeDelta(CoShapePageItem slave) {
			return slave.getContentHeight() + getMaster(slave).getRunAroundSpec().getBottomMargin();
		}
		public double getLayoutHeightDelta(CoShapePageItem slave) {
			return slave.getContentHeight() + getMaster(slave).getRunAroundSpec().getBottomMargin();
		}
		public void performLayout(CoShapePageItem slave) {
			CoShapePageItem master = getMaster(slave);
			slave.setLayoutLocation(master.getX(), master.getY() + master.getHeight() + master.getRunAroundSpec().getBottomMargin());
			slave.setLayoutWidth(master.getWidth());
			slave.setLayoutHeight(slave.getContentHeight());
		}
	};

	private static abstract class SlaveInsidePosition extends CoShapePageItem.SlavePosition {
		public void performLayout(CoShapePageItem slave) {
			CoShapePageItem master = getMaster(slave);

			double x = master.getX();
			double y = master.getY();
			double w = master.getWidth();
			double h = slave.getContentHeight();
			slave.setLayoutWidth(w);
			slave.setLayoutHeight(h);
			slave.setLayoutLocation(x, getY(y, master.getHeight(), h));
		}

		protected abstract double getY(double y, double masterH, double slaveH);
	};

	private static class SlaveTopInsidePosition extends SlaveInsidePosition {
		public int getKey() {
			return CoShapePageItemIF.SLAVE_TOP_INSIDE_POSITION;
		}
		protected double getY(double y, double masterH, double slaveH) {
			return y;
		}
	};

	private static class SlaveBottomInsidePosition extends SlaveInsidePosition {
		public int getKey() {
			return CoShapePageItemIF.SLAVE_BOTTOM_INSIDE_POSITION;
		}
		protected double getY(double y, double masterH, double slaveH) {
			return y + masterH - slaveH;
		}
	};
	
	public CoPageItemFactory() {
		m_slavePositions = new CoShapePageItem.SlavePosition[CoShapePageItemIF.SLAVE_POSITION_COUNT];
		CoShapePageItem.SlavePosition p = null;

		p = new SlaveTopLeftPosition();
		m_slavePositions[p.getKey()] = p;

		p = new SlaveBottomLeftPosition();
		m_slavePositions[p.getKey()] = p;

		p = new SlaveTopPosition();
		m_slavePositions[p.getKey()] = p;

		p = new SlaveTopRightPosition();
		m_slavePositions[p.getKey()] = p;

		p = new SlaveBottomRightPosition();
		m_slavePositions[p.getKey()] = p;

		p = new SlaveBottomPosition();
		m_slavePositions[p.getKey()] = p;

		p = new SlaveTopInsidePosition();
		m_slavePositions[p.getKey()] = p;

		p = new SlaveBottomInsidePosition();
		m_slavePositions[p.getKey()] = p;
		
		m_runAroundSpecs = new Hashtable();

		m_runAroundSpecs.put(CoNoneRunAroundSpecIF.NONE_RUN_AROUND_SPEC, new CoNoneRunAroundSpec());
		m_runAroundSpecs.put(CoShapeRunAroundSpecIF.SHAPE_RUN_AROUND_SPEC, new CoShapeRunAroundSpec());
		m_runAroundSpecs.put(CoBoundingBoxRunAroundSpecIF.BOUNDING_BOX_RUN_AROUND_SPEC, new CoBoundingBoxRunAroundSpec());
			
	}
	
		
	public CoContentWrapperPageItemIF createCaptionBox() {
		return CoPageItemTextContent.createContentWrapper();
	}

	public CoFillStyleIF createFillStyle(Color c) {
		CoGradientFill f = new CoGradientFill();
		f.setColor1(new com.bluebrim.paint.impl.shared.CoSpotColor(c));
		f.setShade1(100);
		return f;
	}

	public CoContentWrapperPageItemIF createImageBox() {
		return CoPageItemImageContent.createContentWrapper();
	}

	public CoLayoutAreaIF createLayoutArea() {
		return new CoLayoutArea();
	}

	public CoContentWrapperPageItemIF createLayoutBox() {
		return CoPageItemLayoutContent.createContentWrapper();
	}

	public CoContentWrapperPageItemIF createNoContentBox() {
		return CoPageItemNoContent.createContentWrapper();
	}

	public CoFactoryElementIF createObject() {
		return null;
	}

	public CoContentWrapperPageItemIF createWorkPieceTextBox() {
		return CoPageItemWorkPieceTextContent.createContentWrapper();
	}

	public CoShapePageItem.SlavePosition getSlaveNoPosition() {
		return m_slaveNoPosition;
	}

	public CoShapePageItem.SlavePosition getSlavePosition(int key) {
		return m_slavePositions[key];
	}

	public CoLayoutAreaIF createLayoutArea(com.bluebrim.content.shared.CoWorkPieceIF wp) {
		return new CoLayoutArea(wp);
	}

	public CoPageItemContentIF createPageItemContent(String type, CoPageItemContentIF deriveFrom) {
		if ((type == null) || (type.equals(CoPageItemNoContentIF.NO_CONTENT))) {
			return new CoPageItemNoContent();
		} else if (type.equals(CoPageItemImageContentIF.IMAGE_CONTENT)) {
			return new CoPageItemImageContent();
		} else if (type.equals(CoPageItemLayoutContentIF.LAYOUT_CONTENT)) {
			return new CoPageItemLayoutContent();
		} else if (type.equals(CoPageItemTextContentIF.TEXT_CONTENT)) {
			CoPageItemTextContentIF c = new CoPageItemTextContent();
			if (deriveFrom instanceof CoPageItemAbstractTextContentIF) {
				c.getFormattedTextHolder().setFormattedText(((CoPageItemAbstractTextContentIF) deriveFrom).getFormattedText());
			}
			return c;
		} else if (type.equals(CoPageItemWorkPieceTextContentIF.WORKPIECE_TEXT_CONTENT)) {
			return new CoPageItemWorkPieceTextContent();
		}

		return null;
	}

	public CoPageLayoutAreaIF createPageLayerLayoutArea() {
		return new CoPageLayoutArea(null);
	}

	public CoRunAroundSpecIF createRunAroundSpec(String key) {
		CoRunAroundSpecIF s = (CoRunAroundSpecIF) m_runAroundSpecs.get(key);
		if (s == null)
			return null;
		return s.deepClone();
	}

	public CoContentWrapperPageItemIF createTextBox() {
		return CoPageItemTextContent.createContentWrapper();
	}

	public CoContentWrapperPageItemIF createTextBox(String initialText, boolean doRunAround) {
		return CoPageItemTextContent.createContentWrapper(initialText, doRunAround);
	}

	public CoTextDistribution getNullTextDistribution() {
		return m_nullTextDistribution;
	}
}