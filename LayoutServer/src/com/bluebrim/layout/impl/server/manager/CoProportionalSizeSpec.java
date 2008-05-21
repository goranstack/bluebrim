package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Abstrakt superklass till bredd- och höjdspecifikationsobjekt
 * som uttrycker en storlek som en del av layoutareans storlek.
 */
 
public abstract class CoProportionalSizeSpec extends CoRelativeSizeSpec implements CoProportionalSizeSpecIF
{

	protected double m_proportion = 0.5;
	protected final double m_maxProportionValue;
	protected final boolean m_useTopLevelAncestorAsBase;
	// xml tags
	public static final String XML_PROPORTION = "proportion";

	protected class MutableProxy extends CoSizeSpec.MutableProxy implements CoRemoteProportionalSizeSpecIF
	{
		public double getProportion() { return CoProportionalSizeSpec.this.getProportion(); }
		public void setProportion( double proportion )
		{
			CoProportionalSizeSpec.this.setProportion( proportion );
			m_owner.notifyOwner();
		}
		public String getDescription()
		{
			return CoProportionalSizeSpec.this.getDescription();
		}
	}

/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoProportionalSizeSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super( node, context );
	
	m_useTopLevelAncestorAsBase = false;
	m_maxProportionValue = 0;
	
	// xml init
	NamedNodeMap map = node.getAttributes();
	m_proportion = CoModelBuilder.getDoubleAttrVal( map, XML_PROPORTION, m_proportion );
}


/**
 * CoProportionalSizeSpec constructor comment.
 */
public CoProportionalSizeSpec( boolean useTopLevelAncestorAsBase, double maxProportionValue )
{
	this( useTopLevelAncestorAsBase, maxProportionValue, 0.5 );
}


/**
 * CoProportionalSizeSpec constructor comment.
 */
public CoProportionalSizeSpec( boolean useTopLevelAncestorAsBase, double maxProportionValue, double proportion )
{
	super();

	m_useTopLevelAncestorAsBase = useTopLevelAncestorAsBase;
	m_maxProportionValue = maxProportionValue;
	
	m_proportion = proportion;
}


protected CoSizeSpec.MutableProxy createMutableProxy()
{
	return new CoProportionalSizeSpec.MutableProxy();
}


public boolean equals( Object o )
{
	if ( this == o ) return true;
	if
		( getClass().isInstance( o ) )
	{
		CoProportionalSizeSpec s = (CoProportionalSizeSpec) o;
		
		return m_isSticky == s.m_isSticky && m_proportion == s.m_proportion && m_useTopLevelAncestorAsBase == s.m_useTopLevelAncestorAsBase;
	} else {
		return super.equals( o );
	}
}


public double getProportion ( ) {
	return m_proportion;
}


protected CoLayoutableIF getProportionBaseSupplyingParent( CoLayoutableIF layoutable )
{
	if
		( m_useTopLevelAncestorAsBase )
	{
		CoLayoutableContainerIF top = layoutable.getLayoutParent();

		if ( top == null ) return null;
		
		while
			( top.getLayoutParent() != null )
		{
			top = top.getLayoutParent();
		}

		if ( isValidParent( top, true ) ) return top;

	} else {
		if ( hasValidParent( layoutable, true ) ) return layoutable.getLayoutParent();
	}

	return null;
}


protected CoLayoutableIF getProportionBaseSupplyingParent( CoShapePageItemIF pi )
{
	return getProportionBaseSupplyingParent( (CoLayoutableIF)pi );
}


public void setProportion (double proportion) {
	m_proportion = proportion;
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-26
 */
 
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	super.xmlVisit( visitor );
	
	visitor.exportAttribute( XML_PROPORTION, Double.toString( m_proportion ) );
}
}