package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.base.shared.geom.*;
import com.bluebrim.gui.client.*;

/**
 * Value models for polygon and bezier curve class behavior.
 * 
 * @author: Dennis
 */
 
abstract class CoCurveValueModels
{
	public static final CoValueModel POLYGON_HANDLE_MODE;
	public static final CoValueModel BEZIER_CURVE_STICKY_POINTS_MODE;
	public static final CoValueModel BEZIER_CURVE_CONTINUITY_0;
	public static final CoValueModel BEZIER_CURVE_CONTINUITY_1;
	public static final CoValueModel BEZIER_CURVE_CONTINUITY_2;

	static
	{
		POLYGON_HANDLE_MODE =
			new CoValueModel( "POLYGON_HANDLE_MODE" )
			{
				public Object getValue()
				{
					return CoPolygonShape.m_usePointHandles ? Boolean.TRUE : Boolean.FALSE;
				}
				
				public void setValue( Object newValue )
				{
					boolean b = ( (Boolean) newValue ).booleanValue();
					if ( CoPolygonShape.m_usePointHandles == b ) return;
					CoPolygonShape.m_usePointHandles = b;
					valueHasChanged();
				}
			};

		BEZIER_CURVE_STICKY_POINTS_MODE =
			new CoValueModel( "BEZIER_CURVE_STICKY_POINTS_MODE" )
			{
				public Object getValue()
				{
					return CoCubicBezierCurveShape.m_useStickyPointHandles ? Boolean.TRUE : Boolean.FALSE;
				}
				
				public void setValue( Object newValue )
				{
					boolean b = ( (Boolean) newValue ).booleanValue();
					if ( CoCubicBezierCurveShape.m_useStickyPointHandles == b ) return;
					CoCubicBezierCurveShape.m_useStickyPointHandles = b;
					valueHasChanged();
				}
			};

		BEZIER_CURVE_CONTINUITY_0 =
			new CoValueModel( "BEZIER_CURVE_CONTINUITY_0" )
			{
				public Object getValue()
				{
					return CoCubicBezierCurveShape.m_continuity <= 0 ? Boolean.TRUE : Boolean.FALSE;
				}
				
				public void setValue( Object newValue )
				{
					boolean b = ( (Boolean) newValue ).booleanValue();
					if
						( b )
					{
						CoCubicBezierCurveShape.m_continuity = 0;
						valueHasChanged();
					}
				}
			};

		BEZIER_CURVE_CONTINUITY_1 =
			new CoValueModel( "BEZIER_CURVE_CONTINUITY_1" )
			{
				public Object getValue()
				{
					return CoCubicBezierCurveShape.m_continuity == 1 ? Boolean.TRUE : Boolean.FALSE;
				}
				
				public void setValue( Object newValue )
				{
					boolean b = ( (Boolean) newValue ).booleanValue();
					if
						( b )
					{
						CoCubicBezierCurveShape.m_continuity = 1;
						valueHasChanged();
					}
				}
			};

		BEZIER_CURVE_CONTINUITY_2 =
			new CoValueModel( "BEZIER_CURVE_CONTINUITY_2" )
			{
				public Object getValue()
				{
					return CoCubicBezierCurveShape.m_continuity >= 2 ? Boolean.TRUE : Boolean.FALSE;
				}
				
				public void setValue( Object newValue )
				{
					boolean b = ( (Boolean) newValue ).booleanValue();
					if
						( b )
					{
						CoCubicBezierCurveShape.m_continuity = 2;
						valueHasChanged();
					}
				}
			};
		}

	
}
