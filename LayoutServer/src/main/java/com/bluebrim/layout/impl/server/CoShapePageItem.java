package com.bluebrim.layout.impl.server;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.server.decorator.*;
import com.bluebrim.layout.impl.server.geom.*;
import com.bluebrim.layout.impl.server.manager.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.stroke.impl.shared.*;
import com.bluebrim.stroke.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.transact.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of a page item with the following properties:
 * <ul>
 *	<li> The ability to have a parent.
 *	<li> Geometry; position, transformation (rotation), shape and dimensions.
 *	<li> Decorations: fill and stroke.
 *	<li> Grids: column grid and baseline grid.
 *	<li> Sibbling overlap behavior.
 *	<li> Declarative layout: location and span.
 *	<li> Support for slave/master relationships with other page items.
 * </ul>
 * @author: Dennis Malmström
 */
 
public abstract class CoShapePageItem extends CoPageItem implements CoShapePageItemIF, CoLayoutableIF
{
	
	// xml tag constants
	public static final String XML_X = "x";
	public static final String XML_Y = "y";
	public static final String XML_HEIGHT_SPEC = "height-spec";
	public static final String XML_WIDTH_SPEC = "width-spec";

	public static final String XML_LOCATION_SPEC = "location-spec";
	public static final String XML_MASTER = "master";
	public static final String XML_ROTATION = "rotation";
	public static final String XML_ROTATION_X = "rotation-x";
	public static final String XML_ROTATION_Y = "rotation-y";
	public static final String XML_RUN_AROUND = "run-around";
	public static final String XML_SLAVE = "slave";
	public static final String XML_SLAVE_POSITION = "slave-position";
	public static final String XML_STROKE_EFFECTIVE_SHAPE = "stroke-effective-shape";
	public static final String XML_SUPRESS_PRINTOUT = "supress-printout";
	public static final String XML_DIMENSIONS_LOCKED = "dimensions-locked";
	public static final String XML_LOCATION_LOCKED = "location-locked";

	// xml stroke properties
	public static final String XML_STROKE_PROPERTIES_ALIGNMENT = "alignment";
	public static final String XML_STROKE_PROPERTIES_ALIGNMENT_OFFSET = "alignment-offset";
	public static final String XML_STROKE_PROPERTIES_BG_COLOR = "bg-color";
	public static final String XML_STROKE_PROPERTIES_BG_SHADE = "bg-shade";
	public static final String XML_STROKE_PROPERTIES_FG_COLOR = "fg-color";
	public static final String XML_STROKE_PROPERTIES_FG_SHADE = "bfg-shade";
	public static final String XML_STROKE_PROPERTIES_STROKE = "stroke";
	public static final String XML_STROKE_PROPERTIES_SYMMETRY = "symmetry";
	public static final String XML_STROKE_PROPERTIES_WIDTH = "width";

	
	// parent
	protected CoCompositePageItem m_parent;

	// position
	protected double m_x;
	protected double m_y;

	// transformation
	protected CoTransformIF m_transform = new CoTransform();
	private CoTransformIF m_mutableTransformProxy = createMutableTransformProxy(); // Implementation of "mutable proxy" pattern

	/** shape (shape owns dimensions) */
	protected CoShapeIF m_shape = new CoRectangleShape( 0, 0, 200, 200 );
	private CoShapeIF.Owner m_shapeOwner; // Implementation of "mutable proxy" pattern
	private CoShapeIF m_mutableShapeProxy = createMutableCoShapeProxy(); // Implementation of "mutable proxy" pattern

	protected CoShapeIF m_effectiveShape = m_shape; // See updateEffectiveShape


	// fill stuff
	protected CoFillStyleIF m_fillStyle = new CoGradientFill();
	private CoFillStyleIF.Owner m_fillStyleOwner; // Implementation of "mutable proxy" pattern
	private CoFillStyleIF m_mutableFillStyleProxy = createMutableFillStyleProxy(); // Implementation of "mutable proxy" pattern


	// stroke stuff
	protected CoStrokePropertiesIF m_strokeProperties = new CoStrokeProperties();
	protected boolean m_strokeEffectiveShape = true; // predicate that decides wether the effective or nominal shape is stroked
	private CoStrokePropertiesIF m_mutableStrokePropertiesProxy = createMutableStrokePropertiesProxy(); // Implementation of "mutable proxy" pattern

	// run around spec stuff (defines how this page item behaves when siblings run around it)
	protected CoRunAroundSpecIF m_runAroundSpec = new CoShapeRunAroundSpec();
	private CoRunAroundSpecIF.Owner m_runAroundSpecOwner; // Implementation of "mutable proxy" pattern
	private CoRunAroundSpecIF m_mutableRunAroundSpecProxy = createMutableRunAroundSpecProxy(); // Implementation of "mutable proxy" pattern


	// layout spec stuff
	protected CoLayoutSpec m_layoutSpec = new CoLayoutSpec();
	private CoSizeSpecIF.Owner m_sizeSpecOwner; // Implementation of "mutable proxy" pattern
	private CoLocationSpecIF.Owner m_locationSpecOwner; // Implementation of "mutable proxy" pattern

	private CoSizeSpecIF m_mutableWidthSpecProxy = createMutableWidthSpecProxy(); // Implementation of "mutable proxy" pattern
	private CoSizeSpecIF m_mutableHeightSpecProxy = createMutableHeightSpecProxy(); // Implementation of "mutable proxy" pattern
	private CoLocationSpecIF m_mutableLocationSpecProxy = createMutableLocationSpecProxy(); // Implementation of "mutable proxy" pattern


	// column grid stuff
	protected CoColumnGrid m_columnGrid = new CoRegularColumnGridPretendingToBeDerived();
	private CoColumnGridIF m_mutableColumnGridProxy = createMutableColumnGridProxy(); // Implementation of "mutable proxy" pattern



	// baseline grid stuff
	protected CoBaseLineGrid m_baseLineGrid = createDefaultBaseLineGrid();
	private CoBaseLineGridIF m_mutableBaseLineGridProxy = createMutableBaseLineGridProxy(); // Implementation of "mutable proxy" pattern


	
	
	// slave/master stuff
	protected CoShapePageItem m_slave = null;  // reference to slave page item if any
	protected CoShapePageItem m_master = null; // reference to master page item if any
	protected SlavePosition m_slavePosition = getSlaveNoPosition(); // implementation of slave position behavior.

	// The slave position state is divided into a key and a behavior because we must be able to specifiy the
	// desired behavior (by using the key) even when no slave is present. The attribute m_slavePosition has a value
	// other than "empty implementation" only when a slave is actually present. The slave position key, however,
	// can be specified without the presence of a slave. When a slave appears m_slavePosition is assigned a value
	// based on the key value.

	// empty slave position implementation
	public static class SlavePosition
	{
		public double getLeftEdgeDelta( CoShapePageItem slave ) { return 0; }
		public double getTopEdgeDelta( CoShapePageItem slave ) { return 0; }
		public double getRightEdgeDelta( CoShapePageItem slave ) { return 0; }
		public double getBottomEdgeDelta( CoShapePageItem slave ) { return 0; }
		public double getLayoutHeightDelta( CoShapePageItem slave ) { return 0; }
		public double getLayoutWidthDelta( CoShapePageItem slave ) { return 0; }
		public void performLayout( CoShapePageItem slave ) {}
		protected final CoShapePageItem getMaster( CoShapePageItem slave ) { return slave.m_master; }
		public int getKey() { return SLAVE_NO_POSITION; }
	};





	/*
		Two page item sibblings (having the same parent) can enter into a master/slave relationship.
		The slave is geometricaly "attached" to the master.
		This means that the slave is ignored by the normal layout mechanism and is instead laid out relative its master.
		The slaves geometry can not be changed directly, only by changing its content or by changed the masters geometry.
		Currently the only use of the master/slave mechanism is when attaching captions to images.

		Comment: the master/slave mechanism is a bit messy and should be used with great care.
		         Any alternative solution to a layout problem is probably preferrable.
		         If another implementation to the image-caption relation can be found then the master/slave mechanism should be removed.
	*/
	
	// layout engine stuff
	protected boolean m_layoutSuccess = true; // page item was successfully laid out by the layout engine

	protected int m_slavePositionKey = SLAVE_BOTTOM_POSITION;	// position of slave relative its master.

	// misc.
	protected boolean m_supressPrintout; // predicate for ignoring page items when printing

	protected boolean m_areDimensionsLocked;
	// geometry locks (these locks are not enforced, only carried, by this class)
	protected boolean m_isLocationLocked;

	// preferences (used very rarely to intercept the normal page item preferences retreival behavior)
	private CoPageItemPreferencesIF m_pageItemPreferences;


	// run around stuff
	private boolean m_doRunAround; // predicate indicating wether the page item should run around its sibblings
	protected LayoutEngineState m_layoutEngineState = new LayoutEngineState();
	// change notification stuff, see method markDirty.
	private long m_modCount;
	private transient String m_reasons = "";

	// This class contains state that is used by the layout engine.
	// Most of it is not persistant and the rest is not considered part of the "real" page item state.
	protected static class LayoutEngineState
	{
		// this state is relevant only during a transaction that triggers the layout engine
		transient boolean m_layoutPending = false; // layout was requested
		transient boolean m_layoutIsInProgress = false; // layout engine is running
		transient boolean m_layoutValid = false; // page item was laid out
		transient boolean m_isWidthInvalid = false; // page item has valid width
		transient boolean m_isHeightInvalid = false; // page item has valid height

		// this state is persistant but changes to it should not trigger state change notifications
		boolean m_isLayoutEngineActive = true;   // is layout engine active ?
	}

	// Eventhandling
	private CoEventListenerList m_listeners = new CoEventListenerList();


protected CoBaseLineGrid createDefaultBaseLineGrid()
{
	return new CoRegularBaseLineGridPretendingToBeDerived();

}



private CoBaseLineGridIF createMutableBaseLineGridProxy()
{
	return new CoRemoteBaseLineGridIF()
	{
		public void addPropertyChangeListener( CoPropertyChangeListener l ) { m_baseLineGrid.addPropertyChangeListener( l ); }
		public void removePropertyChangeListener( CoPropertyChangeListener l ) { m_baseLineGrid.removePropertyChangeListener( l ); }
		public Object clone() throws CloneNotSupportedException { return m_baseLineGrid.clone(); }
		public CoImmutableBaseLineGridIF deepClone() { return m_baseLineGrid.deepClone(); }
		public CoImmutableBaseLineGridIF derive( double y ) { return m_baseLineGrid.derive( y ); }
		public CoBaseLineGeometryIF getBaseLineGeometry( double y ) { return m_baseLineGrid.getBaseLineGeometry( y ); }
		public double getY0() { return m_baseLineGrid.getY0(); }
		public double getY0Position() { return m_baseLineGrid.getY0Position(); }
		public double getDeltaY() { return m_baseLineGrid.getDeltaY(); }
		public boolean isDerived() { return m_baseLineGrid.isDerived(); }
		
		public void setY0( double y0 )
		{
			( (CoBaseLineGridIF) m_baseLineGrid ).setY0( y0 );
			notifyPageItem();
		}
		
		public void setDeltaY( double dy )
		{
			( (CoBaseLineGridIF) m_baseLineGrid ).setDeltaY( dy );
			notifyPageItem();
		}
		
		public void set( double y0, double dy )
		{
			( (CoBaseLineGridIF) m_baseLineGrid ).set( y0, dy );
			notifyPageItem();
		}

		private void notifyPageItem()
		{
			doAfterBaseLineGridChanged();
		}
	};
}
private CoColumnGridIF createMutableColumnGridProxy()
{
	return new CoRemoteColumnGridIF()
	{
		public void addPropertyChangeListener( CoPropertyChangeListener l ) { m_columnGrid.addPropertyChangeListener( l ); }
		public void removePropertyChangeListener( CoPropertyChangeListener l ) { m_columnGrid.removePropertyChangeListener( l ); }
		public Object clone() throws CloneNotSupportedException { return m_columnGrid.clone(); }
		public CoImmutableSnapGridIF deepClone() { return m_columnGrid.deepClone(); }
		public CoImmutableColumnGridIF derive( double x, double y, double w, double h ) { return m_columnGrid.derive( x, y, w, h ); }
		public double getWidthFor(int numberOfColumns) { return m_columnGrid.getWidthFor(numberOfColumns); }	
		public double getBottomMargin() { return m_columnGrid.getBottomMargin(); }
		public double getBottomMarginPosition() { return m_columnGrid.getBottomMarginPosition(); }
		public int getColumnCount() { return m_columnGrid.getColumnCount(); }
		public CoColumnGeometryIF getColumnGeometry( CoImmutableShapeIF shape ) { return m_columnGrid.getColumnGeometry( shape ); }
		public double getColumnSpacing() { return m_columnGrid.getColumnSpacing(); }
		public double getColumnWidth() { return m_columnGrid.getColumnWidth(); }
		public double getWidth() { return m_columnGrid.getWidth(); }
		public double getHeight() { return m_columnGrid.getHeight(); }
		public double getLeftMargin() { return m_columnGrid.getLeftMargin(); }
		public double getLeftMarginPosition() { return m_columnGrid.getLeftMarginPosition(); }
		public double getRightMargin() { return m_columnGrid.getRightMargin(); }
		public double getRightMarginPosition() { return m_columnGrid.getRightMarginPosition(); }
		public Shape getShape( int detailMask ) { return m_columnGrid.getShape( detailMask ); }
		public boolean isLeftOutsideSensitive() { return m_columnGrid.isLeftOutsideSensitive(); }
		public boolean isSpread() { return m_columnGrid.isSpread(); }
		public double getTopMargin() { return m_columnGrid.getTopMargin(); }
		public double getTopMarginPosition() { return m_columnGrid.getTopMarginPosition(); }
		public double getLiveAreaWidth() { return m_columnGrid.getLiveAreaWidth(); }
		public double getLiveAreaHeight() { return m_columnGrid.getLiveAreaHeight(); }
		public boolean isDerived() { return m_columnGrid.isDerived(); }
		public Point2D snap( double x, double y, double w, double h, double range, Point2D d ) { return m_columnGrid.snap( x, y, w, h, range, d ); }
		public Point2D snap( double x, double y, double range, int edgeMask, int dirMask, boolean useEdges, Point2D d ) { return m_columnGrid.snap( x, y, range, edgeMask, dirMask, useEdges, d ); }
		public Collection getGridLines() { return m_columnGrid.getGridLines(); }
		public void xmlVisit(CoXmlVisitorIF visitor) throws CoXmlWriteException { m_columnGrid.xmlVisit(visitor); }
		public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) { m_columnGrid.xmlAddSubModel(name, subModel, context); }
		public void xmlImportFinished(Node node, CoXmlContext context) { m_columnGrid.xmlImportFinished(node, context); }
		
		public void setSpread( boolean s )
		{
			( (CoColumnGridIF) m_columnGrid ).setSpread( s );
			notifyPageItem();
		}
		
		public void setLeftOutsideSensitive( boolean s )
		{
			( (CoColumnGridIF) m_columnGrid ).setLeftOutsideSensitive( s );
			notifyPageItem();
		}
		
		public void setBottomMargin( double bottom )
		{
			( (CoColumnGridIF) m_columnGrid ).setBottomMargin( bottom );
			notifyPageItem();
		}
		
		public void setColumnCount( int c )
		{
			( (CoColumnGridIF) m_columnGrid ).setColumnCount( c );
			notifyPageItem();
		}
		
		public void setColumnSpacing( double s )
		{
			( (CoColumnGridIF) m_columnGrid ).setColumnSpacing( s );
			notifyPageItem();
		}
		
		public void setLeftMargin( double left )
		{
			( (CoColumnGridIF) m_columnGrid ).setLeftMargin( left );
			notifyPageItem();
		}
		
		public void setMargins( double left, double top, double right, double bottom )
		{
			( (CoColumnGridIF) m_columnGrid ).setMargins( left, top, right, bottom );
			notifyPageItem();
		}
		
		public void set( int columnCount, double spacing, double left, double top, double right, double bottom )
		{
			( (CoColumnGridIF) m_columnGrid ).set( columnCount, spacing, left, top, right, bottom );
			notifyPageItem();
		}
		
		public void setRightMargin( double right )
		{
			( (CoColumnGridIF) m_columnGrid ).setRightMargin( right );
			notifyPageItem();
		}
		
		public void setTopMargin( double top )
		{
			( (CoColumnGridIF) m_columnGrid ).setTopMargin( top );
			notifyPageItem();
		}

		private void notifyPageItem()
		{
			doAfterColumnGridChanged();
		}
	};
}






private CoStrokePropertiesIF createMutableStrokePropertiesProxy()
{
	class RemoteStrokeProperties implements CoRemoteStrokePropertiesIF
	{
		public void xmlVisit(CoXmlVisitorIF visitor) {};
	  public String getFactoryKey() { return m_strokeProperties.getFactoryKey(); }
	  public CoStrokePropertiesIF deepClone() { return m_strokeProperties.deepClone(); }
		public int getAlignment() { return m_strokeProperties.getAlignment(); }
		public float getAlignOffset() { return m_strokeProperties.getAlignOffset(); }
		public CoStrokeIF getStroke() { return m_strokeProperties.getStroke(); }
		public float getWidth() { return m_strokeProperties.getWidth(); }
		public float getInsideWidth() { return m_strokeProperties.getInsideWidth(); }
		public float getOutsideWidth() { return m_strokeProperties.getOutsideWidth(); }
		public CoColorIF getForegroundColor() { return m_strokeProperties.getForegroundColor(); }
		public float getForegroundShade() { return m_strokeProperties.getForegroundShade(); }
		public CoColorIF getBackgroundColor() { return m_strokeProperties.getBackgroundColor(); }
		public float getBackgroundShade() { return m_strokeProperties.getBackgroundShade(); }
		public String getSymmetry() { return m_strokeProperties.getSymmetry(); }
		public CoRef getForegroundColorId() { return m_strokeProperties.getForegroundColorId(); }
		public CoRef getBackgroundColorId() { return m_strokeProperties.getBackgroundColorId(); }

		public void setSymmetry( String s )
		{
		  if ( m_strokeProperties.getSymmetry().equals( s ) ) return;
		  m_strokeProperties.setSymmetry( s );
	    notifyPageItem();
		}
		
		public void setAlignment( int a )
	  {
		  if ( m_strokeProperties.getAlignment() == a ) return;
		  m_strokeProperties.setAlignment( a );
	    postShapeChanged(); // alignment affects effective shape
	    notifyPageItem();
	  }

	  public void setAlignOffset( float ao )
	  {
		  if ( m_strokeProperties.getAlignOffset() == ao ) return;
	    m_strokeProperties.setAlignOffset( ao );
	    postShapeChanged(); // alignment offset affects effective shape
	    notifyPageItem();
	  }

	  public void setBackgroundColor( CoColorIF bgc )
	  {
		  if ( m_strokeProperties.getBackgroundColor() == bgc ) return;
	    if ( bgc != null ) m_strokeProperties.setBackgroundColor( bgc );
	    notifyPageItem();
	  }

	  public void setBackgroundShade( float s )
	  {
		  if ( m_strokeProperties.getBackgroundShade() == s ) return;
	    m_strokeProperties.setBackgroundShade( s );
	    notifyPageItem();
	  }

	  public void setForegroundColor( CoColorIF bgc )
	  {
		  if ( m_strokeProperties.getForegroundColor() == bgc ) return;
	    if ( bgc != null ) m_strokeProperties.setForegroundColor( bgc );
	    notifyPageItem();
	  }

	  public void setForegroundShade( float s )
	  {
		  if ( m_strokeProperties.getForegroundShade() == s ) return;
	    m_strokeProperties.setForegroundShade( s );
	    notifyPageItem();
	  }

	  public void setStroke( CoStrokeIF ss )
	  {
		  if ( m_strokeProperties.getStroke() == ss ) return;
	    if ( ss != null ) m_strokeProperties.setStroke( ss );
	    notifyPageItem();
	  }

	  public void setWidth( float w )
	  {
		  if ( m_strokeProperties.getWidth() == w ) return;
	    m_strokeProperties.setWidth( w );
	    postShapeChanged(); // stroke width affects effective shape
	    notifyPageItem();
	  }


	  private void notifyPageItem()
	  {
			doAfterStrokePropertiesChanged();
	  }
	};

	
	return new RemoteStrokeProperties();
}
private final CoTransformIF createMutableTransformProxy()
{
	return new CoRemoteTransformIF()
	{
	  public CoTransformIF deepClone() { return m_transform.deepClone(); }
	  public String getFactoryKey() { return m_transform.getFactoryKey(); }

		public boolean isIdentity() { return m_transform.isIdentity(); }
		public double getRotation() { return m_transform.getRotation(); }
		public double getRotationPointX() { return m_transform.getRotationPointX(); }
		public double getRotationPointY() { return m_transform.getRotationPointY(); }
		public void applyOn( AffineTransform t ) { m_transform.applyOn( t ); }
		public void applyOn( Graphics2D g ) { m_transform.applyOn( g ); }
		public void applyOn( CoPaintable g ) { m_transform.applyOn( g ); }
		public void unapplyOn( Graphics2D g ) { m_transform.unapplyOn( g ); }
		public void unapplyOn( CoPaintable g ) { m_transform.unapplyOn( g ); }
		public void transform( Point2D p ) { m_transform.transform( p ); }
		public void untransform( Point2D p ) { m_transform.untransform( p ); }
		public AffineTransform createAffineTransform() { return m_transform.createAffineTransform(); }
		public AffineTransform createInverseAffineTransform() { return m_transform.createInverseAffineTransform(); }
	  public void xmlVisit(CoXmlVisitorIF visitor) throws CoXmlWriteException { m_transform.xmlVisit(visitor); }

	  public void setRotation( double r )
	  {
		  if ( r == m_transform.getRotation() ) return;
	    m_transform.setRotation( r );
	    notifyPageItem( false, false );
	  }

	  public void setRotationPoint( double x, double y )
	  {
		  if ( x == m_transform.getRotationPointX() && y == m_transform.getRotationPointY() ) return;
	    m_transform.setRotationPoint( x, y );
	    notifyPageItem( false, false );
	  }

	  private void notifyPageItem( boolean dx, boolean dy )
	  {
		  makeLayoutSpecAbsolute( dx, dy, dx, dy, false, false );
	    doAfterTransformChanged();
	  }
	};
}

private void createSizeSpecOwner()
{
	if
		( m_sizeSpecOwner == null )
	{
		m_sizeSpecOwner =
			new CoSizeSpecIF.Owner()
			{
				public void notifyOwner()
				{
					doAfterLayoutSpecChanged( "SizeSpecChanged" );
				}
			};
	}
}

protected final void doAfterBaseLineGridChanged()
{
	postBaseLineGridChanged();

	performLocalLayout();

	markDirty( "BaseLineGridChanged" );
}
protected final void doAfterColumnGridChanged()
{
	postColumnGridChanged();

	performLocalLayout();

	markDirty( "GridChanged" );
}
protected final void doAfterDoRunAroundChanged()
{
	postDoRunAroundChanged();

	performLocalLayout();
	
	markDirty( "setDoRunAround" );
}
protected final void doAfterDrawPriorityChanged()
{
	postDrawPriorityChanged();

	performLocalLayout();

	markDirty( "DrawPriorityChanged" );
}
protected final void doAfterFillStyleChanged()
{
	postFillStyleChanged();

	performLocalLayout();

	markDirty( "FillStyleChanged" );
}
protected final void doAfterLayoutLocationChanged()
{
	postPositionChanged();

	markDirty( "PositionChanged" );
}
protected final void doAfterLayoutPriorityChanged()
{
	postLayoutPriorityChanged();

	performLocalLayout();

	markDirty( "LayoutPriorityChanged" );
}
protected final void doAfterLayoutSpecChanged( String reason )
{
	postLayoutSpecChanged();

	performLocalLayout();

	markDirty( reason );
}
protected final void doAfterPositionChanged()
{
	postPositionChanged();

	performLocalLayout();

	markDirty( "PositionChanged" );
}
protected final void doAfterRunAroundSpecChanged()
{
	postRunAroundSpecChanged();

	performLocalLayout();
	
	markDirty( "RunAroundSpecChanged" );
}
protected final void doAfterShapeChanged()
{
	double dx = m_shape.getX();
	double dy = m_shape.getY();

	if
		( ( dx != 0 ) || ( dy != 0 ) )
	{
		m_shape.setTranslation( 0, 0 );
		m_x += dx;
		m_y += dy;
		postPositionChanged();

		handleShapeTranslation( dx, dy );
	}

	
	if
		(
			( m_transform.getRotationPointX() != m_shape.getWidth() / 2.0 )
		||
			( m_transform.getRotationPointY() != m_shape.getHeight() / 2.0 )
		)
	{
		m_transform.setRotationPoint( m_shape.getWidth() / 2.0, m_shape.getHeight() / 2.0 );
	}
	
	postShapeChanged();

	performLocalLayout();

	markDirty( "ShapeChanged" );
}
protected final void doAfterSlaveChanged()
{
	postSlaveChanged();
//	postSubTreeStructureChange();

	performLocalLayout();

	markDirty( "SlaveChanged" );
}
protected final void doAfterSlavePositionChanged()
{
	postSlavePositionChanged();

	performLocalLayout();

	markDirty( "SlavePositionChanged" );
}
protected final void doAfterStrokeEffectiveShapeChanged()
{
	postStrokeEffectiveShapeChanged();
	
	markDirty( "StrokeEffectiveShapeChanged" );
}
protected final void doAfterStrokePropertiesChanged()
{
	postStrokePropertiesChanged();
	
	performLocalLayout();
	
	markDirty( "StrokePropertiesChanged" );
}
protected final void doAfterTransformChanged()
{
	postTransformChanged();

	performLocalLayout();
	
	markDirty( "TransformChanged" );
}

protected void doSetLayoutEngineActive( boolean a )
{
	m_layoutEngineState.m_isLayoutEngineActive = a;
}
// Traverse parent path and return first ancestor that is an instance of the supplied class.

public CoShapePageItemIF getAncestor( Class c )
{
	if
		( c.isInstance( this ) )
	{
		return this;
	} else {
		return ( m_parent == null ) ? null : m_parent.getAncestor( c );
	}
}
public double getArea()
{
	return getLayoutHeight() * getLayoutWidth();
}
public CoImmutableBaseLineGridIF getBaseLineGrid()
{
//	if ( m_baseLineGrid == null ) m_baseLineGrid = createDefaultBaseLineGrid();
	return m_baseLineGrid;
}
// position of bottom edge (in parents coordinate space)

public double getBottomEdge()
{
	double h = m_shape.getHeight() + m_slavePosition.getBottomEdgeDelta( m_slave );
	
	if
		( h < 0 )
	{
		return m_y;
	} else {
		return m_y + h;
	}
}
public CoImmutableColumnGridIF getColumnGrid()
{
//	if (m_columnGrid == null) m_columnGrid = createDefaultColumnGrid(getCoShape().getWidth(), getCoShape().getHeight());
	return m_columnGrid;
}
// Content height, overridden by subclasses that have content

public double getContentHeight()
{
	return m_shape.getHeight();
}
// Content width, overridden by subclasses that have content

public double getContentWidth()
{
	return m_shape.getWidth();
}
public CoImmutableShapeIF getCoShape()
{
	return m_shape;
}

public final boolean getDoRunAround()
{
	return m_doRunAround;
}
public CoImmutableShapeIF getEffectiveCoShape()
{
	return m_effectiveShape;
}
// exterior shape = outer bounds of stroke

public CoImmutableShapeIF getExteriorShape()
{
	float d = m_strokeProperties.getOutsideWidth();
	
	return getCoShape().createExpandedInstance( d );
}

public CoImmutableFillStyleIF getFillStyle()
{
	return m_fillStyle;
}
public double getHeight()
{
	return m_layoutEngineState.m_isHeightInvalid ? 0 : m_shape.getHeight();
}
// height - column grid margins

public double getInteriorHeight()
{
	if
		( m_columnGrid != null )
	{
		return m_columnGrid.getLiveAreaHeight();
	} else {
		return m_shape.getHeight();
	}
}
// interior shape = inner bounds of stroke

public CoImmutableShapeIF getInteriorShape()
{
	float d = m_strokeProperties.getInsideWidth();
	
	CoImmutableShapeIF s = ( (CoShape) getEffectiveCoShape() ).createExpandedInstance( - d );
	return s;
}
// width - column grid margins

public double getInteriorWidth()
{
	if
		(m_columnGrid != null)
	{
		return m_columnGrid.getLiveAreaWidth();
	} else {
		return m_shape.getWidth();
	}
}
// height according to layout engine

public double getLayoutHeight()
{
	boolean invalid = true;
	
	if
		( m_master != null )
	{
		invalid = m_master.m_layoutEngineState.m_isHeightInvalid;
	} else {
		invalid = m_layoutEngineState.m_isHeightInvalid;
	}

	if ( invalid ) return 0;
	
	double h = m_shape.getHeight() + m_slavePosition.getLayoutHeightDelta( m_slave );
	
	return h;
}
// Implementation of CoLayoutableIF

public CoLayoutableContainerIF getLayoutParent()
{
	return (CoLayoutableContainerIF) getParent();
}

// width according to layout engine

public double getLayoutWidth()
{
	return getWidth() + m_slavePosition.getLayoutWidthDelta( m_slave );
}
// position of left edge (in parents coordinate space)

public double getLeftEdge()
{
	double x = m_x;
		
	return m_x + m_slavePosition.getLeftEdgeDelta( m_slave );
}

/**
 * Changed previous implementation that assumed m_layoutSpec != null / Göran S
 */
public CoImmutableLocationSpecIF getLocationSpec()
{
	return (m_layoutSpec == null) ? null :m_layoutSpec.getLocationSpec(); 
}

public CoBaseLineGridIF getMutableBaseLineGrid()
{
	CoImmutableBaseLineGridIF g = getBaseLineGrid();
	if
		( g.isDerived() )
	{
		return null; // derived grid can't be mutated
	} else {
		return m_mutableBaseLineGridProxy;
	}
}
public CoColumnGridIF getMutableColumnGrid()
{
	CoImmutableColumnGridIF g = getColumnGrid();
	if
		( g.isDerived() )
	{
		return null; // derived grid can't be mutated
	} else {
		return m_mutableColumnGridProxy;
	}
}
public CoShapeIF getMutableCoShape()
{
	return m_mutableShapeProxy;
}
public CoFillStyleIF getMutableFillStyle()
{
	return m_mutableFillStyleProxy;
}
public CoSizeSpecIF getMutableHeightSpec()
{
	return m_mutableHeightSpecProxy;
}

public CoLocationSpecIF getMutableLocationSpec()
{
	return m_mutableLocationSpecProxy;
}
public CoRunAroundSpecIF getMutableRunAroundSpec()
{
	return m_mutableRunAroundSpecProxy;
}
public CoStrokePropertiesIF getMutableStrokeProperties()
{
	return m_mutableStrokePropertiesProxy;
}
public CoTransformIF getMutableTransform()
{
	return m_mutableTransformProxy;
}
public CoSizeSpecIF getMutableWidthSpec()
{
	return m_mutableWidthSpecProxy;
}
// collect the page items that overlap this page item

Iterator getPageItemsToRunAround()
{
	if
		( ! getDoRunAround() )
	{
		return Collections.EMPTY_LIST.iterator();
	}
	
	return new Iterator()
	{
		CoShapePageItem m_pi = CoShapePageItem.this;
		
		Iterator m_sibblings = getSibblingsToRunAround();
		{
			update();
		}

		Object m_nextElement;
		{
			_next();
		}
		
		private void update()
		{
			if ( m_pi == null ) return;
			
			if
				( ( m_sibblings == null ) || ( ! m_sibblings.hasNext() ) )
			{
				m_pi = (CoShapePageItem) m_pi.getParent();
				if
					( m_pi == null )
				{
					m_sibblings = null;
				} else {
					m_sibblings = m_pi.getPageItemsToRunAround();
					update();
				}
			}
		}
		
		private void _next()
		{
			if
				( m_sibblings == null )
			{
				m_nextElement = null;
			} else {
				m_nextElement = m_sibblings.next();
				if
					( m_nextElement == CoShapePageItem.this )
				{
					m_sibblings = null;
					update();
					_next();
				}
			}
		}

		public boolean hasNext()
		{
			return ( m_nextElement != null );
		}

		public Object next()
		{
			Object o = m_nextElement;
			_next();
			return o;
		}

		public void remove()
		{
		}

	};

}

public CoPageItemPreferencesIF getPreferences()
{
	if      ( m_pageItemPreferences != null ) return m_pageItemPreferences;
	else if ( m_parent != null )              return m_parent.getPreferences();
	else                                      return null;
}


// position of right edge (in parents coordinate space)

public double getRightEdge()
{
	return m_x + m_shape.getWidth() +m_slavePosition.getRightEdgeDelta( m_slave );
}
CoImmutableShapeIF getRunAroundShape()
{
	return ( (CoRunAroundSpec) m_runAroundSpec ).getRunAroundShape( this );
}
public CoImmutableRunAroundSpecIF getRunAroundSpec()
{
	return m_runAroundSpec;
}

public CoShapePageItemIF getSlave()
{
	return m_slave;
}
private static SlavePosition getSlaveNoPosition()
{
	CoPageItemFactory f = (CoPageItemFactory) CoFactoryManager.getFactory( CoPageItemFactoryIF.PAGE_ITEM_FACTORY );
	return f.getSlaveNoPosition();
}
public int getSlavePosition()
{
	return m_slavePositionKey;
}
private static SlavePosition getSlavePosition( int key )
{
	CoPageItemFactory f = (CoPageItemFactory) CoFactoryManager.getFactory( CoPageItemFactoryIF.PAGE_ITEM_FACTORY );
	return f.getSlavePosition( key );
}

public boolean getStrokeEffectiveShape()
{
	return m_strokeEffectiveShape;
}
public CoImmutableStrokePropertiesIF getStrokeProperties()
{
	return m_strokeProperties;
}
// Return the style rule applier of this page item.

protected final CoTextStyleApplier getTextStyleApplier() {
	CoTextStyleApplier applier = doGetTextStyleApplier();
	

	if (applier != null) {
		Map macroValues = new HashMap();
		macroValues.put("Sidnummer", null);
		macroValues.put("Del", null);
		macroValues.put("Edition", null);
		macroValues.put("Utgivningsdatum", null);
		macroValues.put("Avdelning", null);
		macroValues.put("Skribent", null);
		macroValues.put("Fotograf", null);

		bindTextVariableValues(macroValues);
		applier.setMacroMap(macroValues);
	}

	return applier;
}
// position of top edge (in parents coordinate space)

public double getTopEdge()
{
	return m_y + m_slavePosition.getTopEdgeDelta( m_slave );
}
public CoImmutableTransformIF getTransform()
{
	return m_transform;
}
public double getWidth()
{
	return m_layoutEngineState.m_isWidthInvalid ? 0 : m_shape.getWidth();
}
public double getX()
{
	return m_x;
}
public double getY()
{
	return m_y;
}
protected void handleShapeTranslation( double dx, double dy )
{
}
boolean hasSlave()
{
	return m_slave != null;
}
public boolean hasValidLayout()
{
	return m_layoutEngineState.m_layoutValid;
}
public void invalidateHeight()
{
	m_layoutEngineState.m_isHeightInvalid = true;
}
public void  invalidateLayout()
{
	m_layoutEngineState.m_layoutValid = false;
}
public void invalidateWidth()
{
	m_layoutEngineState.m_isWidthInvalid = true;
}

public boolean isLayoutEngineActive()
{
	return m_layoutEngineState.m_isLayoutEngineActive;
}
public boolean isSlave()
{
	return m_master != null;
}

protected void makeLayoutSpecAbsolute( boolean dx0, boolean dy0, boolean dx1, boolean dy1, boolean dw, boolean dh )
{
	if 
		( dw )
	{
		m_layoutSpec.setWidthSpec( ( (CoSizeSpec) m_layoutSpec.getWidthSpec() ).getSizeSpecAfterReshape() );
		m_mutableWidthSpecProxy = createMutableWidthSpecProxy();
	}

	if
		( dh )
	{
	  m_layoutSpec.setHeightSpec( ( (CoSizeSpec) m_layoutSpec.getHeightSpec() ).getSizeSpecAfterReshape() );
		m_mutableHeightSpecProxy = createMutableHeightSpecProxy();
	}

	CoLocationSpecIF ls = ( (CoLocationSpec) m_layoutSpec.getLocationSpec() ).getLocationSpecAfterReshape( dx0, dy0, dx1, dy1 );
	if
		( ls != null )
	{
		m_layoutSpec.setLocationSpec( ls );
		m_mutableLocationSpecProxy = createMutableLocationSpecProxy();
	}
}





// run layout engine on this page item
// sizeSpecMask: see CoLayoutSpec
// skipLocation: directive to layout engine: do not change page items position, only dimensions

void performLayout( int sizeSpecMask, boolean skipLocation )
{
	/*
		Dennis, 2001-05-08: this behavior has been moved to the location and size specs.

	// Running the layout engine when the parent is a desktop can be a bad idea since desktops are very large (for instance a top-right location spec will send the page item across the street)
	boolean validParent = ( ( m_parent != null ) && ! ( m_parent instanceof CoDesktopLayoutArea ) );
	
	if
		( validParent )
	{
		m_layoutSpec.layout( this, sizeSpecMask, skipLocation );
	}
*/
	if
		( m_layoutSpec != null ) // don't do layout during destroy
	{
		m_layoutSpec.layout( this, sizeSpecMask, skipLocation );
		performSlaveLayout();
	}
}
// if this page item needs a layout request one

protected final void performLocalLayout()
{
	if ( ! m_layoutEngineState.m_layoutPending ) return;
	if ( ! m_layoutEngineState.m_isLayoutEngineActive )  return;

	requestLayout();

	m_layoutEngineState.m_layoutPending = false;
}
// lay out the slave if present

private void performSlaveLayout()
{
	if
		( m_parent != null )
	{
		m_slavePosition.performLayout( m_slave );
	}
}

protected void postBaseLineGridChanged()
{
	updateBaseLineGridDimensions();
}
protected void postDoRunAroundChanged()
{
	updateEffectiveShape();
	requestLocalLayout();
}
protected void postDrawPriorityChanged()
{
	if
		( m_parent != null )
	{
		m_parent.updateChildrensEffectiveShapes();
	} else {
		updateEffectiveShape();
	}
}

protected void postFillStyleChanged()
{
}

protected void postLayoutPriorityChanged()
{
	requestLocalLayout();
}
protected void postLayoutSpecChanged()
{
	requestLocalLayout();
}
protected void postPositionChanged()
{
	updateColumnGridDimensions();
	updateBaseLineGridDimensions();
	
	requestLocalLayout();
	
	if
		( m_parent != null )
	{
		m_parent.updateChildrensEffectiveShapes();
	} else {
		updateEffectiveShape();
	}
}

protected void postRunAroundSpecChanged()
{
	if
		( m_parent != null )
	{
		m_parent.updateChildrensEffectiveShapes();
	} else {
		updateEffectiveShape();
	}

	if
		( hasSlave() )
	{
		requestLocalLayout();
	}
}
protected void postShapeChanged()
{
	updateColumnGridDimensions();
	updateBaseLineGridDimensions();
	
	requestLocalLayout();

	if
		( m_parent != null )
	{
		m_parent.updateChildrensEffectiveShapes();
	} else {
		updateEffectiveShape();
	}
}
protected void postSlaveChanged()
{
	requestLocalLayout();
}
protected void postSlavePositionChanged()
{
	requestLocalLayout();
}
protected void postStrokeEffectiveShapeChanged()
{
}
protected void postStrokePropertiesChanged()
{
}
protected void postTransformChanged()
{
	if
		( m_parent != null )
	{
		m_parent.updateChildrensEffectiveShapes();
	} else {
		updateEffectiveShape();
	}
}
/**
 * start layout engine
 */

final void requestLayout()
{
	if
		( m_layoutSpec == null )
	{
		 // destroy in progress, no need to run layout engine
		return;
	}
	
	if
		( ! m_layoutEngineState.m_isLayoutEngineActive )
	{
		// layout engine has been temporarily disabled
		return;
	}


	
	// protect against (indirect) recursive calls {
	if
		( m_layoutEngineState.m_layoutIsInProgress )
	{
		// layout engine is already running
		return;
	}

	m_layoutEngineState.m_layoutIsInProgress = true;
	// } protect against (indirect) recursive calls


	
	if
		( m_parent != null )
	{
		if
			( m_parent.m_layoutSpec.isContentDependent() )
		{
			// parents layout is depentant of its children -> delegate to parent
			m_parent.requestLayout();
		} else {
			// parents layout is not depentant on its children -> run layout engine on parent (so it is run on this page items sibblings)
			m_parent.performLayout( CoLayoutSpec.ALL, false );
		}
	} else {

		// no parent -> run layout engine on this page item
		performLayout( CoLayoutSpec.ALL, false );
	}


	m_layoutEngineState.m_layoutIsInProgress = false;
}
// mark this page item as needing layout

protected final void requestLocalLayout()
{
	m_layoutEngineState.m_layoutPending = true;
}


public final void setBaseLineGrid( CoBaseLineGridIF baseLineGrid )
{
	if ( m_baseLineGrid == baseLineGrid ) return; // PENDING: equality check ???
	
	m_baseLineGrid = (CoBaseLineGrid) baseLineGrid;
	
	doAfterBaseLineGridChanged();
}
public final void setColumnGrid( CoColumnGridIF columnGrid )
{
	if ( m_columnGrid == columnGrid ) return; // PENDING: equality check ???
	
	m_columnGrid = (CoColumnGrid) columnGrid;
	
	doAfterColumnGridChanged();
}
public final void setCoShape( CoImmutableShapeIF shape )
{
	setCoShape( shape, false );
}
public final void setDerivedBaseLineGrid( boolean derived )
{
	if
		( derived && ( ( m_baseLineGrid == null ) || ! m_baseLineGrid.isDerived() ) )
	{
		if
			( m_parent == null )
		{
			m_baseLineGrid = new CoRegularBaseLineGridPretendingToBeDerived();
		} else {
			m_baseLineGrid = (CoBaseLineGrid) m_parent.getBaseLineGrid().derive( m_y );
		}
	} else if
		( ! derived && m_baseLineGrid.isDerived() )
	{
		m_baseLineGrid = new CoRegularBaseLineGrid( 0, 0 );
	} else {
		return;
	}
	
	doAfterBaseLineGridChanged();
}
public final void setDerivedColumnGrid( boolean derived )
{
	if
		( derived && ( ( m_columnGrid == null ) || ! m_columnGrid.isDerived() ) )
	{
		if
			( m_parent == null )
		{
			m_columnGrid = new CoRegularColumnGridPretendingToBeDerived();
		} else {
			m_columnGrid = (CoColumnGrid) m_parent.getColumnGrid().derive( m_x, m_y, getCoShape().getWidth(), getCoShape().getHeight() );
		}
	} else if
		( ! derived && m_columnGrid.isDerived() )
	{
		m_columnGrid = m_columnGrid.createSnapshot();
	} else {
		return;
	}
	
	doAfterColumnGridChanged();
}
public final void setDoRunAround( boolean r )
{
	if
		( r != m_doRunAround )
	{
		m_doRunAround = r;
		doAfterDoRunAroundChanged();
	}
}
public void setFillStyle(CoFillStyleIF fillStyle)
{
	if ( m_fillStyle == fillStyle ) return; // PENDING: equality check ???
	
	m_fillStyle = fillStyle;
	m_mutableFillStyleProxy = createMutableFillStyleProxy();
	
	doAfterFillStyleChanged();
}
public void setHeight( double h )
{
	m_layoutEngineState.m_isHeightInvalid = false;
	
	if
		( m_shape.getHeight() != h )
	{
		m_shape.setHeight( h );
		doAfterShapeChanged();
	}
}
public final void setLayoutEngineActive( boolean a )
{
	if ( m_layoutEngineState.m_isLayoutEngineActive == a ) return;

	doSetLayoutEngineActive( a );

	if
		( m_layoutEngineState.m_isLayoutEngineActive )
	{
		performLocalLayout();
	}
}
// Height set by layout engine

public void setLayoutHeight( double h )
{
	h -= m_slavePosition.getLayoutHeightDelta( m_slave );
	
	setHeight( h );
}
// Position set by layout engine

public void setLayoutLocation( double x, double y )
{
	m_layoutEngineState.m_layoutValid = true;

	y -= m_slavePosition.getTopEdgeDelta( m_slave );

	if
		( ( m_y != y ) || ( m_x != x ) )
	{
		m_x = x;
		m_y = y;
		doAfterLayoutLocationChanged();
	}
}

public void setLayoutSuccess( boolean b )
{
	if ( m_layoutSuccess == b ) return;
	
	m_layoutSuccess = b;
	markDirty( "set layout success" );
}
// Width set by layout engine

public void setLayoutWidth( double w )
{
	setWidth( w );
}
// X position set by layout engine

public void setLayoutX( double x )
{
	m_layoutEngineState.m_layoutValid = true;

	if
		( m_x != x )
	{
		m_x = x;
		doAfterLayoutLocationChanged();
	}
}
// Y position set by layout engine

public void setLayoutY( double y )
{
	m_layoutEngineState.m_layoutValid = true;

	y -= m_slavePosition.getTopEdgeDelta( m_slave );

	if
		( m_y != y )
	{
		m_y = y;
		doAfterLayoutLocationChanged();
	}
}

public final void setPosition( double x, double y )
{
	m_layoutEngineState.m_layoutValid = true;

	if
		( ( m_x != x ) || ( m_y != y ) )
	{
		makeLayoutSpecAbsolute( ( m_x != x ), ( m_y != y ), ( m_x != x ), ( m_y != y ), false, false );
		m_x = x;
		m_y = y;
		doAfterPositionChanged();
	}	
}
public final void setRunAroundSpec( CoRunAroundSpecIF ras )
{
	if ( m_runAroundSpec == ras ) return;
	
	m_runAroundSpec = (CoRunAroundSpec) ras;
	m_mutableRunAroundSpecProxy = createMutableRunAroundSpecProxy();

	doAfterRunAroundSpecChanged();
}
public void setSlave( CoShapePageItemIF slave )
{
	if
		( m_slave == slave )
	{
		return;
	}
	
		
	if
		( m_slave != null )
	{
		CoSizeSpec x = (CoSizeSpec) m_slave.getLayoutSpec().getWidthSpec();
		
		m_slave.setLayoutSpecs( CoLocationSpec.getNoLocation(), CoSizeSpec.getNoSizeSpec(), CoSizeSpec.getNoSizeSpec() );
		m_slave.m_master = null;
	}

	m_slave = (CoShapePageItem) slave;
	
	if
		( m_slave != null )
	{
		CoSizeSpec x = (CoSizeSpec) m_slave.getLayoutSpec().getWidthSpec();
		
		m_slave.m_master = this;
		m_slave.setLayoutSpecs( CoLocationSpec.getSlaveLocation(), CoSizeSpec.getSlaveSizeSpec(), CoSizeSpec.getSlaveSizeSpec() );
		m_slavePosition = getSlavePosition( m_slavePositionKey );
	} else {
		m_slavePosition = getSlaveNoPosition();
	}

	doAfterSlaveChanged();
}
public void setSlavePosition( int key )
{
	if ( m_slavePositionKey == key ) return;

	m_slavePositionKey = key;

	doAfterSlavePositionKeyChanged();
	
	if ( m_slave == null ) return;

	SlavePosition pos = getSlavePosition( m_slavePositionKey );
	
	if
		( m_slavePosition != pos )
	{
		m_slavePosition = pos;
		doAfterSlavePositionChanged();
	}
}
public void setStrokeEffectiveShape( boolean b )
{
	if
		( m_strokeEffectiveShape != b )
	{
		m_strokeEffectiveShape = b;

		doAfterStrokeEffectiveShapeChanged();
	}
}
public void setWidth( double w )
{
	m_layoutEngineState.m_isWidthInvalid = false;
	if
		( m_shape.getWidth() != w )
	{
		m_shape.setWidth( w );
		doAfterShapeChanged();
	}
}
public final void setX( double x )
{
	m_layoutEngineState.m_layoutValid = true;

	if
		( m_x != x )
	{
		m_x = x;
		makeLayoutSpecAbsolute( true, false, true, false, false, false );
		doAfterPositionChanged();
	}	
}
public final void setY( double y )
{
	m_layoutEngineState.m_layoutValid = true;

	if
		( m_y != y )
	{
		m_y = y;
		makeLayoutSpecAbsolute( false, true, false, true, false, false );
		doAfterPositionChanged();
	}
}
// transform p from global coordinate space to the coordinate space of this pageitem

public void transformFromAbsolute( Point2D p )
{
	if ( m_parent != null ) m_parent.transformFromAbsolute( p );
	
	p.setLocation( p.getX() - m_x, p.getY() - m_y );
	if ( m_transform != null ) m_transform.untransform( p );
}
// transform p from the coordinate space of this pageitem to global coordinate space

public void transformToAbsolute( Point2D p )
{
	if ( m_transform != null ) m_transform.transform( p );
	p.setLocation( p.getX() + m_x, p.getY() + m_y );

	if ( m_parent != null ) m_parent.transformToAbsolute( p );
}
protected void updateBaseLineGrid()
{
	if
		( m_baseLineGrid.isDerived() )
	{
		// derived baseline grid -> rederive it
		CoBaseLineGrid g = null;
		CoImmutableBaseLineGridIF old = getBaseLineGrid();
		if
			( m_parent != null )
		{
			g = (CoBaseLineGrid) m_parent.getBaseLineGrid().derive( m_y );
		} else {
			g = new CoRegularBaseLineGridPretendingToBeDerived();
		}
		
		m_baseLineGrid = g;
		postBaseLineGridChanged();
		markDirty( "updateBaseLineGrid" );
		
	} else {

		// propagate to children
		updateChildrensBaseLineGrids();
	}
}
private void updateBaseLineGridDimensions()
{
	m_baseLineGrid.setY( m_y );
	updateChildrensBaseLineGrids();
}


protected void updateColumnGrid()
{
	if
		( m_columnGrid.isDerived() )
	{
		CoImmutableShapeIF s = getCoShape();
		
		CoColumnGrid g = null;
		if
			( m_parent != null )
		{
			g = (CoColumnGrid) m_parent.getColumnGrid().derive( m_x, m_y, s.getWidth(), s.getHeight() );
		} else {
			g = new CoRegularColumnGridPretendingToBeDerived();
		}

		m_columnGrid = g;
		postColumnGridChanged();
		markDirty( "updateGrid" );
		
	} else {
		updateChildrensColumnGrids();
	}
}
private void updateColumnGridDimensions()
{
	updateColumnGridDimensions( m_columnGrid );
	updateChildrensColumnGrids();
}
private void updateColumnGridDimensions( CoColumnGrid g )
{
	g.setBounds( m_x, m_y, m_shape.getWidth(), m_shape.getHeight(), ( m_parent == null ) || m_parent.isLeftSide() );
}
protected void updateEffectiveShape()
{
	CoShapeIF newEffectiveShape = null;

	if
		(
			( ! m_doRunAround ) // don't run around -> ignore sibblings
		||
			( m_parent == null ) // no parent -> no sibblings
		)
	{
		newEffectiveShape = m_shape;
		
	} else {
		
		double W = getCoShape().getWidth();
		double H = getCoShape().getHeight();

		Iterator e = getPageItemsToRunAround();

		List sibblingsToRunAround = new ArrayList(); // [ CoShapePageItem ]
		Point2D p = new Point2D.Double();
		CoRunAroundShape S = new CoRunAroundShape( m_shape );

		// traverse sibblings that might cause run around
		while
			( e.hasNext() )
		{
			// get sibbling shape
			CoShapePageItem sibbling = (CoShapePageItem) e.next();
			CoImmutableShapeIF s = sibbling.getRunAroundShape();

			if
				( s != null )
			{
				// transform sibblings shape to this coordinate space
				boolean isAnythingTransformed = ( ! sibbling.getTransform().isIdentity() ) || ( ! getTransform().isIdentity() );
				if
					( isAnythingTransformed )
				{
					AffineTransform T = sibbling.getTransform().createAffineTransform();
					T.concatenate( getTransform().createInverseAffineTransform() );
					s = new CoTransformedShape( (CoShapeIF) s, T );
				}
				p.setLocation( 0.0, 0.0 );
				sibbling.transformToAbsolute( p );
				transformFromAbsolute( p );
				Rectangle2D r = s.getShape().getBounds2D();

				// check overlap
				double x = r.getX() + p.getX();
				double y = r.getY() + p.getY();
				double w = r.getWidth();
				double h = r.getHeight();
				if ( x >= W ) continue; // no overlap -> next sibbling
				if ( y >= H ) continue; // no overlap -> next sibbling
				if ( x + w <= 0 ) continue; // no overlap -> next sibbling
				if ( y + h <= 0 ) continue; // no overlap -> next sibbling

				// perform run around
				if
					( isAnythingTransformed )
				{
					sibbling.getTransform().untransform( p );
					getTransform().transform( p );
				}
				if
					( S.subtractSimpleShape( s.copyTranslatedBy( p.getX(), p.getY() ) ) )
				{
					sibblingsToRunAround.add( sibbling );
				}
			}
		}

		
		if
			( sibblingsToRunAround.isEmpty() )
		{
			newEffectiveShape = m_shape;
		} else {
			newEffectiveShape = S;
		}
	}

	// apply new effective shape only if it differs from previous one
	if
		( ! newEffectiveShape.equals( m_effectiveShape ) )
	{
		m_effectiveShape = newEffectiveShape;
		markDirty( "updateEffectiveShape" );
	}
}

/*
 * Used at XML export
 * Helena Rankegård 2001-10-23
 */
 
public void xmlVisit( CoXmlVisitorIF visitor )
{
	super.xmlVisit( visitor );

	// location
	visitor.exportAttribute( XML_X, Double.toString( getX() ) );
	visitor.exportAttribute( XML_Y, Double.toString( getY() ) );
	
	visitor.exportAttribute( XML_LOCATION_LOCKED, ( m_isLocationLocked ? Boolean.TRUE : Boolean.FALSE ).toString() );
	visitor.exportAttribute( XML_DIMENSIONS_LOCKED, ( m_areDimensionsLocked ? Boolean.TRUE : Boolean.FALSE ).toString() );

	// transformation
	visitor.exportAttribute( XML_ROTATION, Double.toString( getTransform().getRotation() ) );
	visitor.exportAttribute( XML_ROTATION_X, Double.toString( getTransform().getRotationPointX() ) );
	visitor.exportAttribute( XML_ROTATION_Y, Double.toString( getTransform().getRotationPointY() ) );

	// layout spec
	visitor.export( (CoLocationSpec) getLayoutSpec().getLocationSpec() );
	visitor.export( CoXmlWrapperFlavors.NAMED, XML_WIDTH_SPEC, (CoSizeSpec) getLayoutSpec().getWidthSpec() );
	visitor.export( CoXmlWrapperFlavors.NAMED, XML_HEIGHT_SPEC, (CoSizeSpec) getLayoutSpec().getHeightSpec() );

	// shape
	visitor.export( getCoShape() );

	// run around
	visitor.exportAttribute( XML_RUN_AROUND, ( getDoRunAround() ? Boolean.TRUE : Boolean.FALSE ).toString() );

	// grids
	if ( ! getColumnGrid().isDerived() ) 
		visitor.export( getColumnGrid() );
	if ( ! getBaseLineGrid().isDerived() ) 
		visitor.export( (CoBaseLineGrid) getBaseLineGrid() );

	// fill
	visitor.export( ( CoFillStyle) getFillStyle() );
	
	// run around spec
	visitor.export( ( CoRunAroundSpec ) getRunAroundSpec() );

	// slave/master state
	visitor.exportAttribute( XML_SLAVE_POSITION, Integer.toString( getSlavePosition() ) );
	if ( m_slave != null ) visitor.export( CoXmlWrapperFlavors.NAMED, XML_SLAVE, m_slave );
	if ( m_master != null ) visitor.export( CoXmlWrapperFlavors.NAMED, XML_MASTER, m_master );
	
	// misc
	visitor.exportAttribute( XML_STROKE_EFFECTIVE_SHAPE, ( m_strokeEffectiveShape ? Boolean.TRUE : Boolean.FALSE ).toString() );
	visitor.exportAttribute( XML_SUPRESS_PRINTOUT, ( m_supressPrintout ? Boolean.TRUE : Boolean.FALSE ).toString() );
//	visitor.exportAttribute( XML_IS_LAYOUT_ENGINE_ACTIVE, ( m_supressPrintout ? Boolean.TRUE : Boolean.FALSE ).toString() );

	// stroke properties
	visitor.exportAttribute( XML_STROKE_PROPERTIES_WIDTH, Float.toString( m_strokeProperties.getWidth() ) );
	visitor.exportAttribute( XML_STROKE_PROPERTIES_ALIGNMENT, Integer.toString( m_strokeProperties.getAlignment() ) );
	visitor.exportAttribute( XML_STROKE_PROPERTIES_ALIGNMENT_OFFSET, Float.toString( m_strokeProperties.getAlignOffset() ) );
	visitor.exportAttribute( XML_STROKE_PROPERTIES_SYMMETRY, m_strokeProperties.getSymmetry() );
	visitor.exportAttribute( XML_STROKE_PROPERTIES_BG_SHADE, Float.toString( m_strokeProperties.getBackgroundShade() ) );
	visitor.exportAttribute( XML_STROKE_PROPERTIES_FG_SHADE, Float.toString( m_strokeProperties.getForegroundShade() ) );	
	visitor.exportAsGOIorObject( XML_STROKE_PROPERTIES_BG_COLOR, m_strokeProperties.getBackgroundColor());
	visitor.exportAsGOIorObject( XML_STROKE_PROPERTIES_FG_COLOR, m_strokeProperties.getForegroundColor());
	visitor.exportAsGOIorObject( XML_STROKE_PROPERTIES_STROKE, m_strokeProperties.getStroke());
	visitor.exportAttribute( XML_STROKE_EFFECTIVE_SHAPE, ( m_strokeEffectiveShape ? Boolean.TRUE : Boolean.FALSE ).toString() );
}



protected final void doAfterSlavePositionKeyChanged()
{
	postSlavePositionKeyChanged();

	markDirty( "SlavePositionKeyChanged" );
}



public Object getAttributeValue( java.lang.reflect.Field d ) throws IllegalAccessException
{
	try
	{
		return d.get( this );
	}
	catch ( IllegalAccessException ex )
	{
		return super.getAttributeValue( d );
	}
}

public CoCompositePageItemIF getParent()
{
	return m_parent;
}

// check if this page item is (grand^n)child of supplied page item

public boolean isChildOf( CoCompositePageItemIF parent )
{
	if ( m_parent == null ) return false;
	if ( m_parent == parent ) return true;

	return m_parent.isChildOf( parent );
}

protected void postAddTo( CoCompositePageItem parent )
{
	setParent( parent );

	updateColumnGrid();
	updateBaseLineGrid();
}

protected void postRemoveFrom(CoCompositePageItem parent )
{
	setParent( null );
}

protected void postSlavePositionKeyChanged()
{
}

public void setParent( CoCompositePageItem parent )
{
	if ( m_parent == parent ) return;

	CoCompositePageItem old = m_parent;
	
	m_parent = parent;

//	if ( m_parentListener != null ) m_parentListener.parentChanged( this, old );
}


















// See m_supressPrintout

public boolean getSupressPrintout()
{
	return m_supressPrintout;
}

public final void setCoShape( CoImmutableShapeIF shape, boolean leaveLayoutSpecIntact )
{
	if ( m_shape == shape ) return; // PENDING: equality check ???
	
	CoShapeIF old = m_shape;
	
	m_shape = (CoShapeIF) shape;
	m_mutableShapeProxy = createMutableCoShapeProxy(); // create new mutable proxy

	if
		( ! leaveLayoutSpecIntact )
	{
		// make nesseccary changes to layout spec
		
		double x = m_shape.getX();
		double y = m_shape.getY();
		double deltaW = old.getWidth() - m_shape.getWidth();
		double deltaH = old.getHeight() - m_shape.getHeight();
		
		boolean dx0 = ( x != 0 );
		boolean dy0 = ( y != 0 );
		boolean dw = ( deltaW != 0 );
		boolean dh = ( deltaH != 0 );
		boolean dx1 = ( deltaW != x );
		boolean dy1 = ( deltaH != y );
		
		makeLayoutSpecAbsolute( dx0, dy0, dx1, dy1, dw, dh );
	}
	
	doAfterShapeChanged();
}

public void setSupressPrintout( boolean b )
{
	if ( m_supressPrintout == b ) return;
	
	m_supressPrintout = b;

	markDirty( "setSupressPrintout" );
}









public boolean areDimensionsLocked()
{
	return m_areDimensionsLocked;
}

protected Object clone() throws CloneNotSupportedException
{
	CoShapePageItem clone = (CoShapePageItem) super.clone();
	
	clone.m_slave = null;
	clone.m_master = null;

	clone.m_layoutEngineState = new LayoutEngineState();
	clone.m_layoutEngineState.m_isLayoutEngineActive = false;

	clone.m_pageItemPreferences = null;

	return clone;
}


public CoLayout copy() {
	return (CoLayout)deepClone();
}

public void setFixedSize( CoDimension2D size ) {
	getMutableCoShape().setSize(size.getWidth(), size.getHeight());
	setDimensionsLocked(true);
}

public void setLayoutParameters( CoLayoutParameters parameters ) {
	setPageItemPreferences((CoPageItemPreferencesIF)parameters);
}

protected void deepCopy( CoPageItem copy )
{
	super.deepCopy( copy );
	
	CoShapePageItem c = (CoShapePageItem) copy;

	c.m_parent = null; // parent-child relation is not copied
	
	c.m_transform = m_transform.deepClone();
	c.m_mutableTransformProxy = c.createMutableTransformProxy();

	
	c.m_shapeOwner = null;
	c.m_shape = ( (CoShape) m_shape ).deepClone();
	c.m_mutableShapeProxy = c.createMutableCoShapeProxy();
	if
		( m_shape == m_effectiveShape )
	{
		c.m_effectiveShape = c.m_shape;
	} else {
		c.m_effectiveShape = ( (CoShape) m_effectiveShape ).deepClone();
	}

	
	c.m_sizeSpecOwner = null;
	c.m_locationSpecOwner = null;
	c.m_layoutSpec = (CoLayoutSpec) m_layoutSpec.deepClone();
	c.m_mutableWidthSpecProxy = c.createMutableWidthSpecProxy();
	c.m_mutableHeightSpecProxy = c.createMutableHeightSpecProxy();
	c.m_mutableLocationSpecProxy = c.createMutableLocationSpecProxy();

	
	c.m_strokeProperties = m_strokeProperties.deepClone();
	c.m_mutableStrokePropertiesProxy = c.createMutableStrokePropertiesProxy();

	
	c.m_fillStyleOwner = null;
	c.m_fillStyle = (CoFillStyleIF) m_fillStyle.deepClone();
	c.m_mutableFillStyleProxy = c.createMutableFillStyleProxy();

	
	c.m_runAroundSpecOwner = null;
	c.m_runAroundSpec = (CoRunAroundSpec) m_runAroundSpec.deepClone();
	c.m_mutableRunAroundSpecProxy = c.createMutableRunAroundSpecProxy();


	if
		( m_columnGrid.isDerived() )
	{	
		c.m_columnGrid = null;
		c.setDerivedColumnGrid( true );
	} else {
		c.m_columnGrid = (CoColumnGrid) m_columnGrid.deepClone();
	}
	c.m_mutableColumnGridProxy = c.createMutableColumnGridProxy();

	
	if
		( m_baseLineGrid.isDerived() )
	{	
		c.m_baseLineGrid = null;
		c.setDerivedBaseLineGrid( true );
	} else {
		c.m_baseLineGrid = (CoBaseLineGrid) m_baseLineGrid.deepClone();
	}
	c.m_mutableBaseLineGridProxy = c.createMutableBaseLineGridProxy();
		

	c.m_slavePosition = m_slavePosition;
	if
		( m_slave != null )
	{	
		c.m_slave = (CoShapePageItem) m_slave.deepClone();
		c.m_slave.m_master = c;
	}

	c.m_reasons = "";
}



protected final void doAfterDimensionsLockedChanged()
{
	postDimensionsLockedChanged();

	markDirty( "DimensionsLockedChanged" );
}

protected final void doAfterLocationLockedChanged()
{
	postLocationLockedChanged();

	markDirty( "LocationLockedChanged" );
}



public boolean hasFiniteDimensions()
{
	return true;
}

public boolean isLocationLocked()
{
	return m_isLocationLocked;
}

protected void postDeepClone( CoPageItem copy )
{
	super.postDeepClone( copy );
	
	CoShapePageItem c = (CoShapePageItem) copy;

	c.doSetLayoutEngineActive( true );
}

protected void postDimensionsLockedChanged()
{
}

protected void postDistributionBasisChange()
{
	if ( m_parent != null ) m_parent.postDistributionBasisChange();
}

protected void postLocationLockedChanged()
{
}

protected void postSubTreeStructureChange()
{
	if ( m_parent != null ) m_parent.postSubTreeStructureChange();
}

protected void postSubTreeStructureChange( boolean added, CoShapePageItemIF pageItem, CoCompositePageItemIF otherParent )
{
	if ( m_parent != null ) m_parent.postSubTreeStructureChange( added, pageItem, otherParent );
}

public final void setDimensionsLocked( boolean l )
{
	if ( m_areDimensionsLocked == l ) return;

	m_areDimensionsLocked = l;
	
	doAfterDimensionsLockedChanged();
}

public final void setLocationLocked( boolean l )
{
	if ( m_isLocationLocked == l ) return;

	m_isLocationLocked = l;
	
	doAfterLocationLockedChanged();
}



/*
 * Used at XML import
 * Helena Rankegård 2001-10-23
 */
 
public void xmlAddSubModel( String name, Object subModel, CoXmlContext context )
{
	boolean didHandle = false;
	
	if
		( name == null )
	{
		if
			(subModel instanceof CoColumnGridIF)
		{
			setColumnGrid((CoColumnGridIF) subModel);
			didHandle = true;
		} else if
		
			(subModel instanceof CoBaseLineGridIF)
		{
			setBaseLineGrid((CoBaseLineGridIF) subModel);
			didHandle = true;
		} else if
		
			( subModel instanceof CoLocationSpecIF )
		{
			setLayoutSpecs( (CoLocationSpecIF) subModel, null, null );
			didHandle = true;
		} else if

			(subModel instanceof CoFillStyleIF)
		{
			setFillStyle((CoFillStyleIF) subModel);
			didHandle = true;
		//} else if
		
			//(subModel instanceof CoStrokePropertiesIF)
		//{
			//didHandle = true;
		} else if
		
			(subModel instanceof CoShapeIF)
		{
			setCoShape((CoShapeIF) subModel, true);
			didHandle = true;
		} else if
		
			(subModel instanceof CoRunAroundSpecIF)
		{
			setRunAroundSpec( (CoRunAroundSpecIF) subModel );
			didHandle = true;
		}
	
	} else {
		if
			( subModel instanceof CoSizeSpecIF )
		{
			if
				( name.equals( CoShapePageItem.XML_WIDTH_SPEC ) )
			{
				setLayoutSpecs( null, (CoSizeSpecIF) subModel, null );
				didHandle = true;
			} else if
				( name.equals( CoShapePageItem.XML_HEIGHT_SPEC ) )
			{
				setLayoutSpecs( null, null, (CoSizeSpecIF) subModel );
				didHandle = true;
			}
			
		}	else if
			( subModel instanceof CoShapePageItem )
		{
			if
				( name.equals( CoShapePageItem.XML_SLAVE ) )
			{
				setSlave( (CoShapePageItem) subModel );
				didHandle = true;
			} else if
				( name.equals( CoShapePageItem.XML_MASTER ) )
			{
				m_master = (CoShapePageItem) subModel;
				didHandle = true;
			}
		} else
		if (name.equals(XML_STROKE_PROPERTIES_STROKE)) {
			m_strokeProperties.setStroke((CoStrokeIF)subModel);
			didHandle = true;
		} else
		if (name.equals(XML_STROKE_PROPERTIES_FG_COLOR)) {
			m_strokeProperties.setForegroundColor((CoColorIF)subModel);
			didHandle = true;
		} else
		if (name.equals(XML_STROKE_PROPERTIES_BG_COLOR)) {
			m_strokeProperties.setBackgroundColor((CoColorIF)subModel);
			didHandle = true;
		}
				
	}

	if ( ! didHandle ) super.xmlAddSubModel( name, subModel, context );
	
}

// See CoLayoutArea.bringForward

public int bringForward()
{
	if
		( m_parent != null )
	{
		if ( m_parent.isLastChild( this ) ) return -1;
		int i = m_parent.getIndexOfChild( this );
		m_parent.bringForward( this );
		doAfterDrawPriorityChanged();
		return i;
	}

	return -1;
}

// See CoLayoutArea.bringToFront

public int bringToFront()
{
	if
		( m_parent != null )
	{
		if ( m_parent.isLastChild( this ) ) return -1;
		int i = m_parent.getIndexOfChild( this );
		m_parent.bringToFront( this );
		doAfterDrawPriorityChanged();
		return i;
	}

	return -1;
}

public CoDesktopLayoutAreaIF getDesktop()
{
	return ( m_parent == null ) ? null : m_parent.getDesktop();
}

public CoImmutableSizeSpecIF getHeightSpec()
{
	return (m_layoutSpec == null) ? null : m_layoutSpec.getHeightSpec();
}

public final CoLayoutSpecIF getLayoutSpec()
{
	return m_layoutSpec;
}

public CoImmutableSizeSpecIF getWidthSpec()
{
	return (m_layoutSpec == null) ? null :m_layoutSpec.getWidthSpec();
}

// See CoLayoutArea.moveToFirstLayoutPosition

public int moveToFirstLayoutPosition()
{
	if
		( m_parent != null )
	{
		if ( m_parent.isFirstLayoutChild( this ) ) return -1;
		int i = m_parent.getLayoutIndexOfChild( this );
		m_parent.moveToFirstLayoutPosition( this );
		doAfterLayoutPriorityChanged();
		return i;
	}

	return -1;
}

// See CoLayoutArea.moveToLastLayoutPosition

public int moveToLastLayoutPosition()
{
	if
		( m_parent != null )
	{
		if ( m_parent.isLastLayoutChild( this ) ) return -1;
		int i = m_parent.getLayoutIndexOfChild( this );
		m_parent.moveToLastLayoutPosition( this );
		doAfterLayoutPriorityChanged();
		return i;
	}

	return -1;
}

// See CoLayoutArea.moveTowardsFirstLayoutPosition

public int moveTowardsFirstLayoutPosition()
{
	if
		( m_parent != null )
	{
		if ( m_parent.isFirstLayoutChild( this ) ) return -1;
		int i = m_parent.getLayoutIndexOfChild( this );
		m_parent.moveTowardsFirstLayoutPosition( this );
		doAfterLayoutPriorityChanged();
		return i;
	}

	return -1;
}

// See CoLayoutArea.moveTowardsLastLayoutPosition

public int moveTowardsLastLayoutPosition()
{
	if
		( m_parent != null )
	{
		if ( m_parent.isLastLayoutChild( this ) ) return -1;
		int i = m_parent.getLayoutIndexOfChild( this );
		m_parent.moveTowardsLastLayoutPosition( this );
		doAfterLayoutPriorityChanged();
		return i;
	}

	return -1;
}

// See CoLayoutArea.sendBackwards

public int sendBackwards()
{
	if
		( m_parent != null )
	{
		if ( m_parent.isFirstChild( this ) ) return -1;
		int i = m_parent.getIndexOfChild( this );
		m_parent.sendBackwards( this );
		doAfterDrawPriorityChanged();
		return i;
	}

	return -1;
}

// See CoLayoutArea.sendToBack

public int sendToBack()
{
	if
		( m_parent != null )
	{
		if ( m_parent.isFirstChild( this ) ) return -1;
		int i = m_parent.getIndexOfChild( this );
		m_parent.sendToBack( this );
		doAfterDrawPriorityChanged();
		return i;
	}

	return -1;
}



// See CoLayoutArea.setLayoutOrder

public int setLayoutOrder( int i )
{
	if
		( m_parent != null )
	{
		int n = m_parent.getLayoutIndexOfChild( this );
		if ( n == i ) return i;
		m_parent.setLayoutOrder( this, i );
		doAfterDrawPriorityChanged();
		return n;
	}

	return -1;
}

public void setLayoutSpecs( CoImmutableLocationSpecIF ls, CoImmutableSizeSpecIF ws, CoImmutableSizeSpecIF hs )
{
	boolean dirty = false;

  if
  	( ( ls != null ) && m_layoutSpec.getLocationSpec() != ls )
  {
	  m_layoutSpec.setLocationSpec( ls );
		m_mutableLocationSpecProxy = createMutableLocationSpecProxy();
	  dirty = true;
  }

  if
  	( ( ws != null ) && m_layoutSpec.getWidthSpec() != ws )
  {
	  m_layoutSpec.setWidthSpec( ws );
		m_mutableWidthSpecProxy = createMutableWidthSpecProxy();
	  dirty = true;
  }

  if
  	( ( hs != null ) && m_layoutSpec.getHeightSpec() != hs )
  {
	  m_layoutSpec.setHeightSpec( hs );
		m_mutableHeightSpecProxy = createMutableHeightSpecProxy();
	  dirty = true;
  }

  if
  	( dirty )
  {
		doAfterLayoutSpecChanged( "setLayoutSpecs" );
  }
}





// See CoLayoutArea.setZOrder

public int setZOrder( int i )
{
	if
		( m_parent != null )
	{
		int n = m_parent.getIndexOfChild( this );
		if ( n == i ) return i;
		m_parent.setZOrder( this, i );
		doAfterDrawPriorityChanged();
		return n;
	}

	return -1;
}

public void translate( double dx, double dy )
{
	setPosition( m_x + dx, m_y + dy );
}

public void visit( CoPageItemVisitor visitor, Object anything, boolean goDown )
{
	visitor.doToShapePageItem( this, anything, goDown );
}

private Iterator getSibblingsToRunAround()
{
	if
		( m_parent != null )
	{
		Iterator e = m_parent.getSibblingsToRunAround( this );
		if ( e == null ) e = Collections.EMPTY_LIST.iterator();
		return e;
	} else {
		return Collections.EMPTY_LIST.iterator();
	}
}




// Return the style rule applier of this page item.

protected CoTextStyleApplier doGetTextStyleApplier()
{
	if      ( m_pageItemPreferences != null ) return m_pageItemPreferences.getTextStyleApplier();
	else if ( m_parent != null )              return m_parent.doGetTextStyleApplier();
	else                                      return null;

	
//	return m_parent == null ? null : m_parent.getTextStyleApplier();
}

protected void bindTextVariableValues( Map values )
{
	if ( m_parent != null ) m_parent.bindTextVariableValues( values );
	if ( m_master != null ) m_master.bindTextVariableValues( values );
}

public void setPageItemPreferences( CoPageItemPreferencesIF pip )
{
	m_pageItemPreferences = pip;
}



// see CoClearContentPageItemVisitor

public void clearWorkpieceProjections()
{
	visit( new CoClearContentPageItemVisitor(), null, true );
}

protected void collectState( CoPageItemIF.State s, CoPageItemIF.ViewState viewState )
{
	super.collectState( s, viewState );
	
	CoShapePageItemIF.State S = (CoShapePageItemIF.State) s;
	
	S.m_x = m_x;
	S.m_y = m_y;
	S.m_isLocationLocked = m_isLocationLocked;
	S.m_areDimensionsLocked = m_areDimensionsLocked;
	S.m_transform = getTransform();
	S.m_shape = getCoShape();
	S.m_effectiveShape = getEffectiveCoShape();
	S.m_strokeProperties = getStrokeProperties();
	S.m_fillStyle = getFillStyle();
	S.m_runAroundSpec = getRunAroundSpec();
	S.m_locationSpec = getLocationSpec();
	S.m_widthSpec = getWidthSpec();
	S.m_heightSpec = getHeightSpec();
	S.m_name = getName();
	S.m_doRunAround = getDoRunAround();
	S.m_layoutFailed = ! m_layoutSuccess;
	S.m_isSlave = isSlave();
	S.m_hasSlave = hasSlave();
	S.m_slavePosition = getSlavePosition();
	S.m_strokeEffectiveShape = getStrokeEffectiveShape();
	S.m_supressPrintout = getSupressPrintout();

	S.m_reasons = m_reasons;
	m_reasons = "";
}

private CoShapeIF createMutableCoShapeProxy()
{
	if
		( m_shapeOwner == null )
	{
		m_shapeOwner =
			new CoShapeIF.Owner()
			{
				public void shapeChanged( int properties )
				{
					boolean dw = ( ( properties & CoShapeIF.Owner.WIDTH ) != 0 );
					boolean dh = ( ( properties & CoShapeIF.Owner.HEIGHT ) != 0 );
					makeLayoutSpecAbsolute( false, false, false, false, dw, dh );
			   	doAfterShapeChanged();
				}
			};
	}

	return m_shape.getMutableProxy( m_shapeOwner );
}

private CoFillStyleIF createMutableFillStyleProxy()
{
	if
		( m_fillStyleOwner == null )
	{
		m_fillStyleOwner =
			new CoFillStyleIF.Owner()
			{
				public void notifyOwner()
				{
					doAfterFillStyleChanged();
				}
			};
	}
	
	return ( (CoFillStyle) m_fillStyle ).getMutableProxy( m_fillStyleOwner );
}

private CoSizeSpecIF createMutableHeightSpecProxy()
{
	createSizeSpecOwner();
	return ( (CoSizeSpec) m_layoutSpec.getHeightSpec() ).getMutableProxy( m_sizeSpecOwner );
}

private CoLocationSpecIF createMutableLocationSpecProxy()
{
	if
		( m_locationSpecOwner == null )
	{
		m_locationSpecOwner =
			new CoLocationSpecIF.Owner()
			{
				public void notifyOwner()
				{
					doAfterLayoutSpecChanged( "LocationSpecChanged" );
				}
			};
	}
	
	return ( (CoLocationSpec) m_layoutSpec.getLocationSpec() ).getMutableProxy( m_locationSpecOwner );
}

private CoRunAroundSpecIF createMutableRunAroundSpecProxy()
{
	if
		( m_runAroundSpecOwner == null )
	{
		m_runAroundSpecOwner =
			new CoRunAroundSpecIF.Owner()
			{
				public void runAroundSpecChanged()
				{
			   	doAfterRunAroundSpecChanged();
				}
			};
	}
	
	return m_runAroundSpec.getMutableProxy( m_runAroundSpecOwner );
}

private CoSizeSpecIF createMutableWidthSpecProxy()
{
	createSizeSpecOwner();
	return( (CoSizeSpec) m_layoutSpec.getWidthSpec() ).getMutableProxy( m_sizeSpecOwner );
}

public CoView getView() {
	return CoPageItemView.create( this, null, CoPageItemView.DETAILS_EVERYTHING );
}


abstract CoShapePageItemView createView( CoCompositePageItemView parent, int detailMode );

public CoShapePageItemView createView_shallBeCalledBy_CoPageItemView_only( int detailMode )
{
	CoShapePageItemView v = createView( null, detailMode );

	v.refresh();

	return v;
}

// Cleanup when a page item is discarted.

protected void doDestroy()
{
	if ( m_slave != null ) m_slave.destroy();
	m_slave = null;
	
	m_master = null;
	
	m_layoutSpec = null; // disable layout engine

	super.doDestroy();
}

public CoImmutableShapeIF getTranslatedCoShape()
{
	CoShapeIF s = m_shape.deepClone();
	s.setTranslation( m_x, m_y );
	return s;
}

// Forces GemStone into thinking that this object was changed.

public final void markDirty( String reason )
{
	m_modCount++;

	m_reasons += reason + "\n"; // trace changes

	// Generate notification when running in non-distributed mode.
	if ( CoAssertion.SIMULATION_SUPPORT ) CoAssertion.addChangedObject( this );
	
	fireLayoutChange(this);
}

protected void postColumnGridChanged()
{
	updateColumnGridDimensions();
}

// Cleanup when a page item is discarted.

protected void preDestroy()
{
	m_layoutSpec = null; // disable layout engine

	super.preDestroy();
}

public final void setLayoutEngineActiveAndForceExecution()
{
	requestLocalLayout();
	setLayoutEngineActive( true );
}

protected abstract void updateChildrensBaseLineGrids();

protected abstract void updateChildrensColumnGrids();


/*
 * Used at XML import
 * Helena Rankegård 2001-10-23
 */ 
public void xmlImportFinished( Node node, CoXmlContext context )
{
	super.xmlImportFinished( node, context );

	NamedNodeMap map = node.getAttributes();
	setDoRunAround( CoModelBuilder.getBoolAttrVal( map, XML_RUN_AROUND, getDoRunAround() ) );

	updateEffectiveShape();
//	setLayoutEngineActive( true );
}


/*
 * Used at XML import.
 * Helena Rankegård 2001-10-30
 */
 
public void xmlInit( NamedNodeMap map, CoXmlContext context )
{
	// xml init
	setDoRunAround( CoModelBuilder.getBoolAttrVal( map, XML_RUN_AROUND, getDoRunAround() ) );
	
	setLayoutEngineActive( false );

	m_x = CoModelBuilder.getDoubleAttrVal( map, XML_X, m_x ); 
	m_y = CoModelBuilder.getDoubleAttrVal( map, XML_Y, m_y );

	m_isLocationLocked = CoModelBuilder.getBoolAttrVal( map, XML_LOCATION_LOCKED, m_isLocationLocked );
	m_areDimensionsLocked = CoModelBuilder.getBoolAttrVal( map, XML_DIMENSIONS_LOCKED, m_areDimensionsLocked );

	m_transform.setRotation( CoModelBuilder.getDoubleAttrVal( map, XML_ROTATION, m_transform.getRotation() ) );

	double rx = CoModelBuilder.getDoubleAttrVal( map, XML_ROTATION_X, m_transform.getRotationPointX() );
	double ry = CoModelBuilder.getDoubleAttrVal( map, XML_ROTATION_Y, m_transform.getRotationPointY() );;
	m_transform.setRotationPoint( rx, ry );
	
	m_strokeEffectiveShape = CoModelBuilder.getBoolAttrVal( map, XML_STROKE_EFFECTIVE_SHAPE, m_strokeEffectiveShape );
	m_supressPrintout = CoModelBuilder.getBoolAttrVal( map, XML_SUPRESS_PRINTOUT, m_supressPrintout );
	
	setSlavePosition( CoModelBuilder.getIntAttrVal( map, XML_SLAVE_POSITION, getSlavePosition() ) );

	setDerivedColumnGrid( true );
	setDerivedBaseLineGrid( true );


	m_strokeProperties.setWidth( CoModelBuilder.getFloatAttrVal( map, XML_STROKE_PROPERTIES_WIDTH, m_strokeProperties.getWidth() ) );
	m_strokeProperties.setAlignment( CoModelBuilder.getIntAttrVal( map, XML_STROKE_PROPERTIES_ALIGNMENT, m_strokeProperties.getAlignment() ) );
	m_strokeProperties.setAlignOffset( CoModelBuilder.getFloatAttrVal( map, XML_STROKE_PROPERTIES_ALIGNMENT_OFFSET, m_strokeProperties.getAlignOffset() ) );
	m_strokeProperties.setBackgroundShade( CoModelBuilder.getFloatAttrVal( map, XML_STROKE_PROPERTIES_BG_SHADE, m_strokeProperties.getBackgroundShade() ) );
	m_strokeProperties.setForegroundShade( CoModelBuilder.getFloatAttrVal( map, XML_STROKE_PROPERTIES_FG_SHADE, m_strokeProperties.getForegroundShade() ) );
	m_strokeProperties.setSymmetry( CoModelBuilder.getAttrVal( map, XML_STROKE_PROPERTIES_SYMMETRY, m_strokeProperties.getSymmetry() ) );

}

public void activateLayoutEngine() {
	setLayoutEngineActive(true);
}

public void addLayoutChangeListener(CoLayoutChangeListener listener) {
	m_listeners.add(CoLayoutChangeListener.class, listener);
}


public void removeLayoutChangeListener(CoLayoutChangeListener listener) {
	m_listeners.remove(CoLayoutChangeListener.class, listener);
}

/**
 * Fires EventObject to the listeners if we represent a
 * root item else delegate to parent.
 */
protected void fireLayoutChange(Object source) {
	if (m_parent != null)
		m_parent.fireLayoutChange(source);
	Object[] targets;

	synchronized (this) {
		if (m_listeners.getListenerCount() < 1)
			return;
		targets = (Object[]) m_listeners.getListenerList().clone();
	}
	EventObject evt = new EventObject(source);

	for (int i = 0; i < targets.length; i += 2) {
		if ((Class) targets[i] == CoLayoutChangeListener.class) {
			CoLayoutChangeListener target = (CoLayoutChangeListener) targets[i + 1];
			target.layoutChange(evt);
		}
	}
}

}