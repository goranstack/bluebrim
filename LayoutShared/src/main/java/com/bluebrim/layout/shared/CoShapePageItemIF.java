package com.bluebrim.layout.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.stroke.shared.*;

/**
 * RMI-enabling interface for class CoShapePageItem.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */

public interface CoShapePageItemIF extends CoPageItemIF, CoLayout
{
	// slave positions (see CoShapePageItem for details on the master-slave mechanism)
	public static final int SLAVE_NO_POSITION = -1;
	public static final int SLAVE_TOP_LEFT_POSITION = 0;
	public static final int SLAVE_BOTTOM_LEFT_POSITION = 1;
	public static final int SLAVE_TOP_POSITION = 2;
	public static final int SLAVE_TOP_RIGHT_POSITION = 3;
	public static final int SLAVE_BOTTOM_RIGHT_POSITION = 4;
	public static final int SLAVE_BOTTOM_POSITION = 5;
	public static final int SLAVE_TOP_INSIDE_POSITION = 6;
	public static final int SLAVE_BOTTOM_INSIDE_POSITION = 7;
	public static final int SLAVE_POSITION_COUNT = 8;


	// see CoPageItemIF.State
	public static class State extends CoPageItemIF.State
	{
		// page item state needed by view, see CoShapePageItem for details
		public double m_x;
		public double m_y;
		public CoImmutableTransformIF m_transform;
		public CoImmutableShapeIF m_shape;
		public CoImmutableShapeIF m_effectiveShape;
		public CoImmutableStrokePropertiesIF m_strokeProperties;
		public boolean m_strokeEffectiveShape;
		public CoImmutableFillStyleIF m_fillStyle;
		public CoImmutableRunAroundSpecIF m_runAroundSpec;
		public CoImmutableLocationSpecIF m_locationSpec;
		public CoImmutableSizeSpecIF m_widthSpec;
		public CoImmutableSizeSpecIF m_heightSpec;
		public String m_name;
		public boolean m_doRunAround;
		public boolean m_layoutFailed;
		public boolean m_isSlave;
		public boolean m_hasSlave;
		public int m_slavePosition;
		public boolean m_supressPrintout;
		public boolean m_isLocationLocked;
		public boolean m_areDimensionsLocked;

		/** used for tracing page item dirty marking (transported to client so it can be printed in client log). */
		public String m_reasons;
	};
	
/**
 * Traverse parent path and return first ancestor that is an instance of the supplied class.
 */
public CoShapePageItemIF getAncestor( Class c );

public CoImmutableBaseLineGridIF getBaseLineGrid();
public CoImmutableColumnGridIF getColumnGrid();
/**
 * Overridas av de sidelement som har ett innehåll som har höjd t ex CoTextShape
 */
public double getContentHeight();
/**
 * Overridas av de sidelement som har ett innehåll som har bredd t ex CoTextShape
 */
public double getContentWidth();
public CoImmutableShapeIF getCoShape();
boolean getDoRunAround();
public CoImmutableShapeIF getEffectiveCoShape();
public CoImmutableFillStyleIF getFillStyle ();
double getInteriorHeight();
public CoImmutableShapeIF getInteriorShape ( );
double getInteriorWidth();

CoImmutableLocationSpecIF getLocationSpec();
public CoBaseLineGridIF getMutableBaseLineGrid();
public CoColumnGridIF getMutableColumnGrid();
CoShapeIF getMutableCoShape();
public CoFillStyleIF getMutableFillStyle();
CoSizeSpecIF getMutableHeightSpec();

CoLocationSpecIF getMutableLocationSpec();
CoRunAroundSpecIF getMutableRunAroundSpec();
public CoStrokePropertiesIF getMutableStrokeProperties();
CoTransformIF getMutableTransform();
CoSizeSpecIF getMutableWidthSpec();

CoImmutableRunAroundSpecIF getRunAroundSpec();
CoShapePageItemIF getSlave();
boolean getStrokeEffectiveShape();
public CoImmutableStrokePropertiesIF getStrokeProperties ();
public CoImmutableTransformIF getTransform();
String getType();

public double getX();
public double getY();

public double getHeight();
public double getWidth();

boolean isLayoutEngineActive();
boolean isSlave();




public void setColumnGrid ( CoColumnGridIF columnGrid );
public void setCoShape( CoImmutableShapeIF shape );
void setDerivedBaseLineGrid( boolean derived );
void setDerivedColumnGrid( boolean derived );
void setDoRunAround( boolean r );
public void setFillStyle ( CoFillStyleIF fillStyle);
void setLayoutEngineActive( boolean a );

void setPosition( double x, double y );
void setRunAroundSpec( CoRunAroundSpecIF ras );


void setStrokeEffectiveShape( boolean derived );
void setX( double x );
void setY( double y );

CoCompositePageItemIF getParent();

boolean isChildOf( CoCompositePageItemIF p );

public void setSlavePosition(int slavePosition);

public boolean areDimensionsLocked();

int bringForward();

int bringToFront();

void clearWorkpieceProjections();

public CoShapePageItemView createView_shallBeCalledBy_CoPageItemView_only( int detailMode );

CoDesktopLayoutAreaIF getDesktop();

CoImmutableSizeSpecIF getHeightSpec();

public String getName();

public CoPageItemPreferencesIF getPreferences();

boolean getSupressPrintout();

public CoImmutableShapeIF getTranslatedCoShape();

CoImmutableSizeSpecIF getWidthSpec();

public boolean isLocationLocked();

int moveToFirstLayoutPosition();

int moveToLastLayoutPosition();

int moveTowardsFirstLayoutPosition();

int moveTowardsLastLayoutPosition();

int sendBackwards();

int sendToBack();

public void setDimensionsLocked( boolean l );

void setLayoutEngineActiveAndForceExecution();

int setLayoutOrder( int i );

public void setLayoutSpecs( CoImmutableLocationSpecIF locationSpec, CoImmutableSizeSpecIF widthSpec, CoImmutableSizeSpecIF heightSpec );

public void setLocationLocked( boolean l );

public void setPageItemPreferences( CoPageItemPreferencesIF pip );

void setSupressPrintout( boolean b );

int setZOrder( int i );

void translate( double dx, double dy );

// public void visit( CoPageItemVisitor visitor, Object anything, boolean goDown );

}