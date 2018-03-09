package com.bluebrim.layout.impl.server;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Abstract superclass for page item content holding bounded contents (content that have a width and heigth).
 * 
 * @author: Dennis Malmström
 */
 
public abstract class CoPageItemBoundedContent extends CoPageItemContent implements CoPageItemBoundedContentIF
{
	// content projection scale
	protected double m_scaleX = 1.0;
	protected double m_scaleY = 1.0;
	
	// content projection translation
	protected double m_x = 0.0;
	protected double m_y = 0.0;
	
	// content projection mirroring
	protected boolean m_flipX = false;
	protected boolean m_flipY = false;
	protected boolean m_wantsCaption = false;


	public static final String XML_FLIP_X = "flip-x";
	public static final String XML_FLIP_Y = "flip-y";
	public static final String XML_SCALE_X = "scale-x";
	public static final String XML_SCALE_Y = "scale-y";
	public static final String XML_WANTS_CAPTION = "wants-caption";
	// xml tag constants
	public static final String XML_X = "x";
	public static final String XML_Y = "y";


	protected int m_contentLock = UNLOCKED; // content lock, see CoPageItemBoundedContentIF
	public static final String XML_CONTENT_LOCK = "content-lock";
public CoPageItemBoundedContent()
{
	super();
}
// adjust content position and scale to fit the owners dimensions

public void adjustContentToFit( int dimensionMask )
{
	if ( ! hasContent() ) return;
	
	double sx = 1.0;
	double sy = 1.0;
	double x = m_x;
	double y = m_y;

	if
		( ( dimensionMask & X ) == X )
	{
		sx = m_owner.getCoShape().getWidth() / getContentWidth();
		x = 0;
	}

	if
		( ( dimensionMask & Y ) == Y )
	{
		sy = m_owner.getCoShape().getHeight() / getContentHeight();
		y = 0;
	}
	
	setScale( sx, sy );
	setPosition( x, y );
}
// adjust content position and scale to fit the owners dimensions without changing the content aspect ratio

public void adjustContentToFitKeepAspectRatio( int dimensionMask )
{
	if ( ! hasContent() ) return;
	
	double x = m_x;
	double y = m_y;
	double s = Math.min( m_scaleX, m_scaleY );

	if
		( ( dimensionMask & XY ) == XY )
	{
		s = Math.min( m_owner.getCoShape().getWidth() / getContentWidth(),
			            m_owner.getCoShape().getHeight() / getContentHeight() );
		x = 0;
		y = 0;
	} else if
		( ( dimensionMask & X ) == X )
	{
		s = m_owner.getCoShape().getWidth() / getContentWidth();
		x = 0;
	} else if
		( ( dimensionMask & Y ) == Y )
	{
		s = m_owner.getCoShape().getHeight() / getContentHeight();
		y = 0;
	}

	setScale( s, s );
	setPosition( x, y );
}
// adjust owners dimensions to fit the contents unscaled dimensions

public void adjustToContentSize( int dimensionMask )
{
	if ( ! hasContent() ) return;
	
	double sx = m_scaleX;
	double sy = m_scaleY;
	double x = m_x;
	double y = m_y;

	if
		( ( dimensionMask & X ) == X )
	{
		m_owner.getMutableCoShape().setWidth( getContentWidth() );
		sx = 1;
		x = 0;
	}

	if
		( ( dimensionMask & Y ) == Y )
	{
		m_owner.getMutableCoShape().setHeight( getContentHeight() );
		sy = 1;
		y = 0;
	}
	
	
	setScale( sx, sy );
	setPosition( x, y );
}
// adjust owners dimensions to fit the contents dimensions

public void adjustToScaledContentSize( int dimensionMask )
{
	if ( ! hasContent() ) return;
	
	double x = m_x;
	double y = m_y;

	if
		( ( dimensionMask & X ) == X )
	{
		m_owner.getMutableCoShape().setWidth( getContentWidth() * m_scaleX );
		x = 0;
	}

	if
		( ( dimensionMask & Y ) == Y )
	{
		m_owner.getMutableCoShape().setHeight( getContentHeight() * m_scaleY );
		y = 0;
	}
	
	setPosition( x, y );
}
// create a page item to be used as caption for this content

public CoShapePageItemIF createCaption()
{
	CoShapePageItemIF captionBox = null;
	
	CoCompositePageItemIF parent = m_owner.getParent();

	// caption requires parent (it is a sibling)
	if
		( parent != null )
	{
		CoPageItemPreferencesIF p = getPreferences();
		if
			( p != null )
		{
			captionBox = (CoShapePageItemIF) p.getCaptionboxPrototype().deepClone(); 
		} else {
			CoPageItemFactoryIF f = (CoPageItemFactoryIF) CoFactoryManager.getFactory( CoPageItemFactoryIF.PAGE_ITEM_FACTORY );
			captionBox = f.createCaptionBox();
			captionBox.setDerivedColumnGrid( false );
			captionBox.getMutableColumnGrid().setColumnCount( 1 );
		}
	}

	return captionBox;
}
public CoPageItemIF.State createState()
{
	return new CoPageItemBoundedContentIF.State();
}

protected final void doAfterContentChanged()
{
	postContentChanged();

	doAfterContentSizeChanged();
	
	markDirty("ContentChanged");
}
protected final void doAfterContentSizeChanged()
{
	postContentSizeChanged();
	markDirty("ContentSizeChanged");
}

protected abstract double getContentHeight();
protected abstract double getContentWidth();
public boolean getFlipX()
{
	return m_flipX;
}
public boolean getFlipY()
{
	return m_flipY;
}
final double getHeight()
{
	return getContentHeight() * m_scaleX;
}
public double getScaleX()
{
	return m_scaleX;
}
public double getScaleY()
{
	return m_scaleY;
}

final double getWidth()
{
	return getContentWidth() * m_scaleX;
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
	super.handleShapeTranslation( dx, dy );

	// keep content position i global coordinate space unchanged
	setPosition( m_x - dx, m_y - dy );
}
protected abstract boolean hasContent();
protected void postContentChanged()
{
}
protected void postContentSizeChanged()
{
	requestLayout();
}
protected void postSlaveChanged()
{
	super.postSlaveChanged();

	updateCaption();
}
public void setFlipX( boolean b )
{
	if ( m_flipX == b ) return;
	
	m_flipX = b;

	markDirty("setFlipX");
}
public void setFlipY( boolean b )
{
	if ( m_flipY == b ) return;
	
	m_flipY = b;

	markDirty("setFlipY");
}
protected void setPosition( double x, double y )
{
	if ( m_x == x && m_y == y ) return;
	
	m_x = x;
	m_y = y;

	markDirty("setPosition");
}
protected void setScale( double sx, double sy )
{
	if ( m_scaleX == sx && m_scaleY == sy ) return;
	
	m_scaleX = sx;
	m_scaleY = sy;

	markDirty("setScale");

	doAfterContentSizeChanged();
}





protected void updateCaption()
{
	if
		( ( m_owner != null ) && m_owner.hasSlave() )
	{
		( (CoPageItemTextContent) ( (CoContentWrapperPageItem) m_owner.getSlave() ).getContent() ).setFormattedTextHolder( getCaption() );
	}
}

/*
 * Used at XML export
 * Helena Rankegård 2001-10-23
 */
 
public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
	super.xmlVisit(visitor);
	visitor.exportAttribute( XML_X, Double.toString(getX()));
	visitor.exportAttribute( XML_Y, Double.toString(getY()));
	visitor.exportAttribute( XML_SCALE_X, Double.toString(getScaleX()));
	visitor.exportAttribute( XML_SCALE_Y, Double.toString(getScaleY()));
	visitor.exportAttribute( XML_FLIP_X, ( m_flipX ? Boolean.TRUE : Boolean.FALSE ).toString() );
	visitor.exportAttribute( XML_FLIP_Y, ( m_flipY ? Boolean.TRUE : Boolean.FALSE ).toString() );
	visitor.exportAttribute( XML_WANTS_CAPTION, ( m_wantsCaption ? Boolean.TRUE : Boolean.FALSE ).toString() );
	visitor.exportAttribute( XML_CONTENT_LOCK, Integer.toString( m_contentLock ) );
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

public boolean hasCaption()
{
	return 
		( 
			( m_owner != null ) && m_owner.hasSlave() // if owner has a slave it must be the caption (slaves are not used for anything else)
		||
			(
				( ( m_owner == null ) || ( m_owner.m_parent == null ) ) // if owner is missing or can't have a slave
			&&
				( m_wantsCaption )                                      // then defer to this objects wish to have a caption
			)
		);
}

protected void postAddTo( CoCompositePageItem parent )
{
	super.postAddTo( parent );

	// update caption box state
	if ( ( parent != null ) && m_wantsCaption ) setCaption( null );
	updateCaption();
}



public void setCaptionPosition( int slavePositionKey )
{
	if ( m_owner == null ) return;

	m_owner.setSlavePosition( slavePositionKey );
}















protected final void doAfterContentLockChanged()
{
	postContentLockChanged();
	
	markDirty("ContentLockChanged");
}

public int getContentLock()
{
	return m_contentLock;
}

protected void postContentLockChanged()
{
}

public CoShapePageItemIF removeCaption()
{
	if ( ! m_wantsCaption ) return null;
	
	m_wantsCaption = false;

	markDirty( "removeCaption" );

	// remove caption box if it exists
	if ( m_owner == null ) return null;
	if ( ! m_owner.hasSlave() ) return null;
	if ( m_owner.m_parent == null ) return null;

	CoShapePageItemIF old = m_owner.getSlave();

	CoShapePageItemIF caption = m_owner.getSlave();
	m_owner.setSlave( null );
	m_owner.m_parent.remove( caption );

	return old;
}



public void setContentLock( int l )
{
	if ( l == m_contentLock ) return;
	m_contentLock = l;
	doAfterContentLockChanged();
}

// set content projection scale and translation
// NaN values are ignored

public void setScaleAndPosition( double sx, double sy, double x, double y )
{
	boolean scaleChanged = false;
	
	if
		( ! Double.isNaN( sx ) && ( sx != m_scaleX ) )
	{
		m_scaleX = sx;
		scaleChanged = true;
	}
	
	if
		( ! Double.isNaN( sy ) && ( sy != m_scaleY ) )
	{
		m_scaleY = sy;
		scaleChanged = true;
	}


	boolean positionChanged = false;
	
	if
		( ! Double.isNaN( x ) && ( x != m_x ) )
	{
		m_x = x;
		positionChanged = true;
	}
	
	if
		( ! Double.isNaN( y ) && ( y != m_y ) )
	{
		m_y = y;
		positionChanged = true;
	}

	if
		( scaleChanged )
	{
		doAfterContentSizeChanged();
	} else if
		( positionChanged )
	{
		markDirty( "setScaleAndPosition" );
	}
}

public void visit( CoPageItemVisitor visitor, Object anything, boolean goDown )
{
	visitor.doToBoundedContent( this, anything, goDown );
}

protected void collectState( CoPageItemIF.State s, CoPageItemIF.ViewState viewState )
{
	super.collectState( s, viewState );
	
	CoPageItemBoundedContentIF.State S = (CoPageItemBoundedContentIF.State) s;
	
	S.m_scaleX = getScaleX();
	S.m_scaleY = getScaleY();
	S.m_x = getX();
	S.m_y = getY();
	S.m_flipX = getFlipX();
	S.m_flipY = getFlipY();
	S.m_hasCaption = hasCaption();
	S.m_contentLock = m_contentLock;
}

// return caption text as a document holder

protected abstract com.bluebrim.text.shared.CoFormattedTextHolderIF getCaption();

// Add a caption box
// If a caption box can't be added, remember the wish to have one.
// If no caption box is supplied (captionBox==null) one is created.

public void setCaption( CoShapePageItemIF captionBox )
{
	m_wantsCaption = true; // remember wish

	if
		(
			( m_owner == null ) // no owner
		||
			( m_owner.hasSlave() ) // owner allready has slave
		||
			( m_owner.m_parent == null ) // owner can't have slave
		)
	{
		// caption box can't be added
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( captionBox == null, "Attempt to add caption box failed" );
		return;
	}
	
	if ( captionBox == null ) captionBox = createCaption();
	
	m_owner.m_parent.add( captionBox );
	
	m_owner.setSlave( captionBox );
	int i0 = m_owner.m_parent.getIndexOfChild( m_owner );
	int i = m_owner.m_parent.getIndexOfChild( captionBox );
	while ( i++ < i0 ) captionBox.bringForward();
	
	markDirty( "setCaption" );
}


/*
 * Used at XML import.
 * Helena Rankegård 2001-10-30
 */
 
public void xmlInit( NamedNodeMap map, com.bluebrim.xml.shared.CoXmlContext context )
{
	// xml init
	
	m_x = CoModelBuilder.getDoubleAttrVal( map, XML_X, m_x );
	m_y = CoModelBuilder.getDoubleAttrVal( map, XML_Y, m_y );
	m_scaleX = CoModelBuilder.getDoubleAttrVal( map, XML_SCALE_X, m_scaleX );
	m_scaleY = CoModelBuilder.getDoubleAttrVal( map, XML_SCALE_Y, m_scaleY );

	m_flipX = CoModelBuilder.getBoolAttrVal( map, XML_FLIP_X, m_flipX );
	m_flipY = CoModelBuilder.getBoolAttrVal( map, XML_FLIP_Y, m_flipY );
	
	m_wantsCaption = CoModelBuilder.getBoolAttrVal( map, XML_WANTS_CAPTION, m_wantsCaption );
	m_contentLock = CoModelBuilder.getIntAttrVal( map, XML_CONTENT_LOCK, m_contentLock );
}
}