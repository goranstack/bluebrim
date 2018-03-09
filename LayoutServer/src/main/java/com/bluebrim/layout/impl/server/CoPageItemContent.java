package com.bluebrim.layout.impl.server;

import java.util.Map;


import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoContentWrapperPageItemIF;
import com.bluebrim.layout.impl.shared.CoPageItemContentIF;
import com.bluebrim.layout.impl.shared.CoPageItemIF;
import com.bluebrim.layout.impl.shared.CoPageItemPreferencesIF;
import com.bluebrim.layout.impl.shared.view.CoContentWrapperPageItemView;
import com.bluebrim.layout.impl.shared.view.CoPageItemContentView;
import com.bluebrim.layout.shared.*;

/**
 * Abstract superclass for all page item content implementations.
 * A page item content projects a piece of content (text, image, ...) in a page item structure.
 * 
 * A page item can not exist in a page item structure on its own. For one it doesn't have any geometrical properties.
 * Instead it must coexists with a content wrapper page item (which have the properties that the page item content lacks).
 * 
 * The fact that CoPageItemContent and CoContentWrapperPageItem share a common superclass is not obvious.
 * They are after all in many respects complementary.
 * However they do also share some properties (most importantly the property of being represented by a view on the client side) so its is quite convenient to have a common superclass.
 * 
 * @author: Dennis Malmström
 */
 
public abstract class CoPageItemContent extends CoPageItem implements CoPageItemContentIF
{
	protected CoContentWrapperPageItem m_owner;



	protected static final double m_emptyWidth = CoLengthUnit.CM.from( 5 );
	protected static final double m_emptyHeight = m_emptyWidth;
	// xml tag constants
	public static final String XML_CONTENT = "page-item-content";


public CoPageItemIF.State createState()
{
	return new CoPageItemContentIF.State();
}



public double getEmptyHeight()
{
	return m_emptyHeight;
}
public double getEmptyWidth()
{
	return m_emptyWidth;
}
// content height

abstract double getHeight();
public CoContentWrapperPageItemIF getOwner()
{
	return m_owner;
}
protected final CoPageItemPreferencesIF getPreferences()
{
	return ( m_owner == null ) ? null : m_owner.getPreferences();
}


protected abstract String getWrappersType();
// content type

protected abstract String getType();
// content width

abstract double getWidth();
// owner position changed

protected void handleShapeTranslation( double dx, double dy )
{
}

// is content empty ?

public abstract boolean isEmpty();


// owners slave was changed

protected void postSlaveChanged()
{
}
// dispatch layout requests to owner (if they are relevant)

protected final void requestLayout()
{
	if
		( ( m_owner != null ) && ( m_owner.m_layoutSpec != null ) )
	{
		if
			( ( m_owner.m_layoutSpec.isContentDependent() ) || m_owner.isSlave() );
		{
			m_owner.requestLayout();
		}
	}
}

void setOwner( CoContentWrapperPageItemIF owner )
{
	m_owner = (CoContentWrapperPageItem) owner;
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

// owner was added to a new parent

protected void postAddTo( CoCompositePageItem parent )
{
}

// owner was removed from its parent

protected void postRemoveFrom(CoCompositePageItem parent )
{
}





protected void deepCopy( CoPageItem copy )
{
	super.deepCopy( copy );
	
	CoPageItemContent c = (CoPageItemContent) copy;
	
	c.m_owner = null; // owners cannot be shared
}

final void markDirty( String reason )
{
	if ( m_owner != null ) m_owner.markDirty( reason );
}

public void visit( CoPageItemVisitor visitor, Object anything, boolean goDown )
{
	visitor.doToPageItemContent( this, anything, goDown );
}

protected void collectState( CoPageItemIF.State s, CoPageItemIF.ViewState viewState )
{
	super.collectState( s, viewState );
	
	CoPageItemContentIF.State S = (CoPageItemContentIF.State) s;

	S.m_type = getType();
}

abstract CoPageItemContentView createView( CoContentWrapperPageItemView owner );

public final String getFactoryKey()
{
	return getType();
}

protected void bindTextVariableValues( Map values)
{
}
}