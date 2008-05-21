package com.bluebrim.layout.impl.server;

import org.w3c.dom.Node;

import com.bluebrim.base.shared.CoSimpleObject;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoPageItemIF;
import com.bluebrim.layout.shared.*;

/**
 * Abstract ancestor of all page item classes.
 * 
 * @author: Dennis Malmström
 */
 
public abstract class CoPageItem extends CoSimpleObject implements CoPageItemIF, com.bluebrim.base.shared.debug.CoDiffable
{	
	protected transient CoPageItemIF.State m_state; // see method getState.
public CoPageItem()
{
	super();
}

// Creates an instance of CoPageItemIF.State that is appropiate for the particular page item class.
// Page item subclasses having their own subclasses of CoPageItemIF.State must override this method.

protected CoPageItemIF.State createState()
{
	return new CoPageItemIF.State();
}
// Creates a copy of a page item structure.

public final CoPageItemIF deepClone()
{
	CoPageItem c = null;
	
	try
	{
		c = (CoPageItem) clone();
		deepCopy( c );
		postDeepClone( c );
	}
	catch ( CloneNotSupportedException ex )
	{
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "CoPageItem.clone() failed in CoPageItem.deepClone()" );
	}

	return c;
}
// Cleanup when a page item is disposed

public final void destroy()
{
	preDestroy();
	doDestroy();
	postDestroy();
}



// See CoCompositePageItem.doAdd

int getPositionWeight()
{
	return Integer.MAX_VALUE / 2;
}

// State collection algorithm (see getState( Object anything )).

protected final void getState( CoPageItemIF.State s, CoPageItemIF.ViewState viewState )
{
}
// Return page item state in a CoPageItemIF.State struct.
// Parameter viewState can be used to pass view information to the state collection algorithm.

public final CoPageItemIF.State getState( CoPageItemIF.ViewState viewState )
{
	if
		( m_state == null )
	{
		m_state = createState();
	}

	collectState( m_state, viewState );
	
	return m_state;
}






// classname (without package path) + page item name

public String toString()
{
	String tmp = super.toString();
	tmp = tmp.substring( tmp.lastIndexOf( '.' ) + 1 ) + " (" + getName() + ")";
	return tmp;
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-23
 */
 
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
}








// implementation of com.bluebrim.base.shared.debug.CoDiffable (for test purposes)

public Object getAttributeValue( java.lang.reflect.Field d ) throws IllegalAccessException
{
	return d.get( this );
}



// State collection algorithm (see method getState).

protected void collectState( CoPageItemIF.State s, CoPageItemIF.ViewState viewState )
{
}

// Copy state into copy, see method deepClone


protected void deepCopy( CoPageItem copy )
{
	copy.m_state = null;
}

// see method destroy

protected void doDestroy()
{
}

// Return page item identity

public final com.bluebrim.transact.shared.CoRef getId()
{
	return com.bluebrim.transact.shared.CoRef.to( this );
}

public abstract String getName();

// see method deepClone

protected void postDeepClone( CoPageItem copy )
{
}

// see method destroy

protected void postDestroy()
{
}

// see method destroy

protected void preDestroy()
{
}


// Page item visitor engine.
// All concrete subclasses must override.

public void visit( CoPageItemVisitor visitor, Object anything, boolean goDown )
{
	visitor.doToPageItem( this, anything, goDown );
}

/*
 * Used at XML import
 * Helena Rankegård 2001-10-23
 */
 
public void xmlAddSubModel( String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context )
{
}


/*
 * Used at XML import
 * Helena Rankegård 2001-10-23
 */
 
public void xmlImportFinished( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
}
}