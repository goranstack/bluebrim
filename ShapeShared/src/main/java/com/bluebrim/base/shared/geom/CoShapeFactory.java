package com.bluebrim.base.shared.geom;

import java.util.*;

import com.bluebrim.base.shared.*;

//

public class CoShapeFactory implements CoShapeFactoryIF
{
	private Map m_prototypes = new Hashtable();
	{
		m_prototypes.put( CoRectangleShapeIF.RECTANGLE_SHAPE, new CoRectangleShape() );
		m_prototypes.put( CoRoundedCornerIF.ROUNDED_CORNER, new CoRoundedCorner() );
		m_prototypes.put( CoBeveledCornerIF.BEVELED_CORNER, new CoBeveledCorner() );
		m_prototypes.put( CoConcaveCornerIF.CONCAVE_CORNER, new CoConcaveCorner() );
		m_prototypes.put( CoLineIF.LINE, new CoLine() );
		m_prototypes.put( CoEllipseShapeIF.ELLIPSE_SHAPE, new CoEllipseShape() );
		m_prototypes.put( CoLineIF.ORTHOGONAL_LINE, new CoOrthogonalLine() );
		m_prototypes.put( CoPolygonShapeIF.POLYGON_SHAPE, new CoPolygonShape() );
		m_prototypes.put( CoBoxedLineShapeIF.BOXED_LINE, new CoBoxedLineShape() );
		m_prototypes.put( CoCubicBezierCurveShapeIF.CUBIC_BEZIER_CURVE, new CoCubicBezierCurveShape() );
	}
public CoFactoryElementIF createObject()
{
	return null;
}
public CoShapeIF createShape( String key )
{
	CoShapeIF s = (CoShapeIF) m_prototypes.get( key );
	if ( s == null ) return null;
	return s.createNewInstance();
}
public CoShapeIF createShape( String key, CoImmutableShapeIF geometry )
{
	CoShapeIF s = (CoShapeIF) m_prototypes.get( key );
	if ( s == null ) return null;
	return s.createNewInstanceFrom( geometry );
}
public Collection getShapeKeys()
{
	return m_prototypes.keySet();
}
}
