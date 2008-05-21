package com.bluebrim.layoutmanager;

/**
 * Extends CoLayoutManager for placement of CoLayoutableIF's
 * Creation date: (2000-07-12 15:37:53)
 * @author: Arvid Berg & Masod Jalalian 
 */
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

public class CoRectangleLayoutManager extends CoLayoutManager implements CoRectangleLayoutManagerIF
{

    public final static String XML_TAG = "rectangle-layout-manager";

    private double m_gap = 0;

    private CoCalculateDistanceIF distanceCalculator = null;

    private CoCalculateDistanceIF m_rectangleDistCalc = new CoDistRectangle();

    private CoCalculateDistanceIF m_horizontalDistCalc = new CoDistHorizontal();

    private CoCalculateDistanceIF m_verticalDistCalc = new CoDistVertical();

    private CoCalculateDistanceIF m_convexDistCalc = new CoDistConvex();

    private CoCalculateDistanceIF m_concaveDistCalc = new CoDistConcave();

    private CoCalculateDistanceIF m_triangleDistCalc = new CoDistTriangle();

    private Map m_keyToInstanceMap = new HashMap();
    {
        m_keyToInstanceMap.put(m_rectangleDistCalc.getName(), m_rectangleDistCalc);
        m_keyToInstanceMap.put(m_horizontalDistCalc.getName(), m_horizontalDistCalc);
        m_keyToInstanceMap.put(m_verticalDistCalc.getName(), m_verticalDistCalc);
        m_keyToInstanceMap.put(m_convexDistCalc.getName(), m_convexDistCalc);
        m_keyToInstanceMap.put(m_concaveDistCalc.getName(), m_concaveDistCalc);
        m_keyToInstanceMap.put(m_triangleDistCalc.getName(), m_triangleDistCalc);

    }

    private List m_keys = new ArrayList();
    {
        m_keys.addAll(m_keyToInstanceMap.keySet());
        m_keys.remove(m_rectangleDistCalc.getName());
    }

    protected class MutableProxy extends CoLayoutManager.MutableProxy implements CoRectangleLayoutManagerIF
    {

        public CoCalculateDistanceIF createDistanceCalculator(String key)
        {
            return CoRectangleLayoutManager.this.createDistanceCalculator(key);
        }

        public CoCalculateDistanceIF getDefaultDistanceCalculator()
        {
            return CoRectangleLayoutManager.this.getDefaultDistanceCalculator();
        }

        public CoCalculateDistanceIF getDistanceCalculator()
        {
            return CoRectangleLayoutManager.this.getDistanceCalculator();
        }

        public java.util.List getKeys()
        {
            return CoRectangleLayoutManager.this.getKeys();
        }

        public void setDistanceCalculator(CoCalculateDistanceIF newDistanceCalculator)
        {
            CoRectangleLayoutManager.this.setDistanceCalculator(newDistanceCalculator);
            notifyOwner();
        }

        public double getGap()
        {
            return CoRectangleLayoutManager.this.getGap();
        }

        public void setGap(double g)
        {
            CoRectangleLayoutManager.this.setGap(g);
            notifyOwner();
        }
    };

    public CoRectangleLayoutManager()
    {
        distanceCalculator = new CoDistRectangle();
    }

    public CoRectangleLayoutManager(CoCalculateDistanceIF param)
    {
        distanceCalculator = param;
    }

    public CoCalculateDistanceIF createDistanceCalculator(String key)
    {

        CoCalculateDistanceIF m = (CoCalculateDistanceIF) m_keyToInstanceMap.get(key);

        if (m != null) return m;

        return null;
    }

    protected CoLayoutManager.MutableProxy createMutableProxy()
    {
        return new MutableProxy();
    }

    public CoLayoutManagerIF deepClone()
    {
        CoRectangleLayoutManager m = new CoRectangleLayoutManager();
        m.distanceCalculator = distanceCalculator;

        return m;
    }

    public boolean doesSetSize()
    {
        return false;
    }

    public CoCalculateDistanceIF getDefaultDistanceCalculator()
    {
        return m_rectangleDistCalc;
    }

    public CoCalculateDistanceIF getDistanceCalculator()
    {
        return distanceCalculator;
    }

    public String getFactoryKey()
    {
        //if(distanceCalculator!=null)
        //	return distanceCalculator.getName();

        return RECTANGLE_LAYOUT_MANAGER;
    }

    public double getGap()
    {
        return m_gap;
    }

    public java.util.List getKeys()
    {
        return m_keys;
    }

    public String getPanelClassName()
    {
        return "com.bluebrim.layout.impl.client.CoRectangleLayoutManagerPanel";
    }

    /**
     * Takes a CoLayoutableContainerIF and layout it's children.
     */
    public void layout(CoLayoutableContainerIF parent)
    {
        int align = CoCornerUtilities.BOTTOM_RIGHT;
        // int align = Utilities.TOP_LEFT;
        if (distanceCalculator == null) distanceCalculator = new CoDistRectangle();
        CoCalculateDistanceIF dist = distanceCalculator;
        System.out.println(distanceCalculator.getClass().getName());

        CoImmutableColumnGridIF g = parent.getColumnGrid();
        List freeList = new LinkedList();
        List freeListSize;
        Rectangle2D container = new Rectangle2D.Double();
        container.setFrameFromDiagonal(g.getLeftMarginPosition(), g.getTopMarginPosition(), g.getRightMarginPosition(), g
                .getBottomMarginPosition());

        CoRectanglePlacement la = new CoRectanglePlacement();

        Point2D corner = new Point2D.Double(-CoCornerUtilities.calculateDisplacement(align).getX() * container.getWidth(),
                -CoCornerUtilities.calculateDisplacement(align).getY() * container.getHeight());
        freeList.add(container);
        // remove all locked objects from freeList with removeRect

        Iterator listIter = parent.getLayoutChildren().iterator();
        while (listIter.hasNext())
        {
            CoLayoutableIF ly = (CoLayoutableIF) listIter.next();
            // sets the align to that of the LocationSpec of
            // the CoLayoutableIF for eatch object
            if ((align = CoCornerUtilities.alignLocSpec(ly.getLayoutSpec().getLocationSpec())) == 0) continue;
            corner = new Point2D.Double(-CoCornerUtilities.calculateDisplacement(align).getX() * container.getWidth(), -CoCornerUtilities
                    .calculateDisplacement(align).getY()
                    * container.getHeight());

            Rectangle2D r = new Rectangle2D.Double(ly.getX(), ly.getY(), ly.getLayoutWidth(), ly.getLayoutHeight());

            freeListSize = la.reduceRectangles(la.getFreeRects(r, freeList));

            if (freeListSize.size() > 0)
            {
                Point2D pp = la.choseBestPoint(r, la.getPointsWithGap(freeListSize, align, m_gap, container), corner, align, dist);

                ly.setLayoutX(pp.getX());
                ly.setLayoutY(pp.getY());
                ly.setLayoutSuccess(true);
                double posX = 0;
                if (CoCornerUtilities.areEqual(align, CoCornerUtilities.RIGHT))
                {
                    posX = g.snap(pp.getX(), pp.getY(), Double.POSITIVE_INFINITY, CoGeometryConstants.RIGHT_EDGE_MASK,
                            CoGeometryConstants.TO_LEFT_MASK, false, null).getX();
                    r.setRect(posX, pp.getY(), r.getWidth() + (pp.getX() - posX), r.getHeight());
                } else
                {
                    posX = g.snap(pp.getX() + r.getWidth(), pp.getY(), Double.POSITIVE_INFINITY, CoGeometryConstants.LEFT_EDGE_MASK,
                            CoGeometryConstants.TO_RIGHT_MASK, false, null).getX();
                    r.setRect(pp.getX(), pp.getY(), Math.floor(posX - pp.getX()), r.getHeight());
                }

                freeList = la.reduceRectangles(la.removeRect(r, freeList));
            } else
                ly.setLayoutSuccess(false);
        }
    }

    public void setDistanceCalculator(CoCalculateDistanceIF newDistanceCalculator)
    {
        distanceCalculator = newDistanceCalculator;
    }

    public void setGap(double g)
    {
        m_gap = g;
    }

    public String getLocalizedName()
    {
        return CoExJobbLayoutManagerResources.getName(RECTANGLE_LAYOUT_MANAGER);
    }

    public String xmlGetTag()
    {
        return XML_TAG;
    }

    public void xmlInit(java.util.Map attributes)
    {
        super.xmlInit(attributes);

        //PENDING: ???
    }

    public void xmlVisit(CoXmlVisitorIF visitor)
    {
        //PENDING: ???
    }
}