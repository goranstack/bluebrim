package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Specificerar och sätter storlek på sidelement
 */
 
public abstract class CoSizeSpec extends CoSimpleObject implements CoSizeSpecIF, com.bluebrim.xml.shared.CoXmlEnabledIF
{
	protected class MutableProxy implements CoRemoteSizeSpecIF
	{
		public CoSizeSpecIF.Owner m_owner;

		public void removePropertyChangeListener( CoPropertyChangeListener l ) { CoSizeSpec.this.removePropertyChangeListener( l ); }
		public void addPropertyChangeListener( CoPropertyChangeListener l ) { CoSizeSpec.this.addPropertyChangeListener( l ); }
		public CoSizeSpecIF deepClone() { return CoSizeSpec.this.deepClone(); }
		public String getFactoryKey() { return CoSizeSpec.this.getFactoryKey(); }
		public String getDescription() { return CoSizeSpec.this.getDescription();};
		public void configure( CoShapePageItemIF pi ) { CoSizeSpec.this.configure( pi ); }
	}

	protected boolean m_isSticky = false;
	
	public final static String XML_STICKY = "sticky";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-25
 */
 
public void xmlImportFinished( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
}


public CoSizeSpec()
{
	m_isSticky = false;
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-25
 */
 
protected CoSizeSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super();
	
	// xml init
	NamedNodeMap map = node.getAttributes();
	m_isSticky = CoModelBuilder.getBoolAttrVal( map, XML_STICKY, m_isSticky );
}


protected boolean checkSizeSpecMask( int sizeSpecMask )
{
	return ( sizeSpecMask & CoLayoutSpec.ALL_BUT_CONTENT ) != 0;
}


protected MutableProxy createMutableProxy()
{
	return new MutableProxy();
}


public CoSizeSpecIF deepClone ()
{
	try
	{
		return (CoSizeSpecIF) clone();
	}
	catch ( CloneNotSupportedException ex )
	{
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false , getClass() + ".deepClone failed." );
		return null;
	}
}


/*
 *	Implementeras av de subklasser som ska sätta storlek efter det att 
 *	de pageItems som har en absolut storlek har placerats.
 */
public void dontSetSizeAfterLocation( CoLayoutableIF layoutable,
	                                CoLocationSpec locationSpec )
{
}


public void dontSetSizeBeforeLocation( CoLayoutableIF layoutable )
{
}


protected static final CoLayoutSpecFactoryIF getFactory()
{
	return (CoLayoutSpecFactoryIF) CoFactoryManager.getFactory( CoLayoutSpecIF.LAYOUT_SPEC );
}


public CoSizeSpecIF getMutableProxy( CoSizeSpecIF.Owner owner )
{
	MutableProxy mp = createMutableProxy();
	mp.m_owner = owner;
	return mp;
	/*
	if
		( m_mutableProxy == null )
	{
		m_mutableProxy = createMutableProxy();
	}
	m_mutableProxy.m_owner = owner;
	return m_mutableProxy;
	*/
}


public static CoSizeSpecIF getNoSizeSpec()
{
	return getFactory().getNoSizeSpec();
}


public final CoSizeSpecIF getSizeSpecAfterReshape()
{
	return m_isSticky ? this : getNoSizeSpec();
}


public static CoSizeSpecIF getSlaveSizeSpec()
{
	return getFactory().getSlaveSizeSpec();
}


public String getType (){
	return CoPageItemStringResources.getName(getFactoryKey ());
}


public abstract String getXmlTag();


public final static boolean hasValidParent( CoLayoutableIF layoutable, boolean checkParentDimensions )
{
	return isValidParent( layoutable.getLayoutParent(), checkParentDimensions );
}


public boolean isContentDependent()
{
	return false;
}


public boolean isNull()
{
	return false;
}


public final static boolean isValidParent( CoLayoutableContainerIF parent, boolean checkDimensions )
{
	if ( parent == null ) return false;

	if ( ! checkDimensions ) return true;

	return parent.hasFiniteDimensions();
}


/**
 * Adderar på eventuellt offset som kan vara negativt
 */
protected void setLayoutableHeight( CoLayoutableIF layoutable, double height)
{
	if ( height == Double.MAX_VALUE ) return;
	
	layoutable.setLayoutHeight(height);
}


/**
 * Adderar på eventuellt offset som kan vara negativt
 */
protected void setLayoutableWidth( CoLayoutableIF layoutable, double width)
{
	if ( width == Double.MAX_VALUE ) return;
	
	layoutable.setLayoutWidth(width);
}


/*
 *	Implementeras av de subklasser som ska sätta storlek efter det att 
 *	de pageItems som har en absolut storlek har placerats.
 */
public void setSizeAfterLocation( CoLayoutableIF layoutable,
	                                CoLocationSpec locationSpec )
{
}


/*
 *	Implementeras av de subklasser som ska sätta storlek efter det att 
 *	de pageItems som har en absolut storlek har placerats.
 */
public final void setSizeAfterLocation( CoLayoutableIF layoutable,
	                                CoLocationSpec locationSpec,
	                                int sizeSpecMask )
{
	if
		( checkSizeSpecMask( sizeSpecMask ) )
	{
		setSizeAfterLocation( layoutable, locationSpec );
	} else {
		dontSetSizeAfterLocation( layoutable, locationSpec );
	}
}


public void setSizeBeforeLocation( CoLayoutableIF layoutable )
{
}


public final void setSizeBeforeLocation( CoLayoutableIF layoutable,
	                                 int sizeSpecMask )
{
	if
		( checkSizeSpecMask( sizeSpecMask ) )
	{
		setSizeBeforeLocation( layoutable );
	} else {
		dontSetSizeBeforeLocation( layoutable );
	}
}


public String toString (){
	return getType();
}


/*
 * Used at XML import
 * Helena Rankegård 2001-10-25
 */
 
public void xmlAddSubModel( String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context )
{
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-26
 */
 
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	visitor.exportAttribute( XML_STICKY, ( m_isSticky ? Boolean.TRUE : Boolean.FALSE ).toString() );
}
}