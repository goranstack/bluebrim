package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Abstrakt superklass för de höjd- och bredd objekt som uttrycker
 * en storlek som är relaterad till storleken på sidelementets innehåll.
 */
 
public abstract class CoContentSize extends CoRelativeSizeSpec implements CoContentSizeIF
{
/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoContentSize( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	super( node, context );
	
	// xml init
	NamedNodeMap map = node.getAttributes();
	m_relativeOffset = CoModelBuilder.getDoubleAttrVal( map, XML_RELATIVE_OFFSET, m_relativeOffset );
	m_absoluteOffset = CoModelBuilder.getDoubleAttrVal( map, XML_ABSOLUTE_OFFSET, m_absoluteOffset );
}
//	protected CoDistanceIF m_absoluteOffset = new CoNoDistance();
	protected double m_absoluteOffset = 0;
	protected double m_relativeOffset = 0;
	protected class MutableProxy extends CoSizeSpec.MutableProxy implements CoRemoteContentSizeIF
	{
		public double getAbsoluteOffset() { return CoContentSize.this.getAbsoluteOffset(); }
		public double getRelativeOffset() { return CoContentSize.this.getRelativeOffset(); }

		public void setAbsoluteOffset( double d )
		{
			CoContentSize.this.setAbsoluteOffset( d );
			m_owner.notifyOwner();
		}
		public void setRelativeOffset( double d )
		{
			CoContentSize.this.setRelativeOffset( d );
			m_owner.notifyOwner();
		}
		public String getDescription()
		{
			return CoContentSize.this.getDescription();
		}
	}


	// xml tags
	public static final String XML_ABSOLUTE_OFFSET = "absolute-offset";
	public static final String XML_RELATIVE_OFFSET = "relative-offset";

public CoContentSize()
{
	super();
}


protected boolean checkSizeSpecMask( int sizeSpecMask )
{
	return ( sizeSpecMask & CoLayoutSpec.CONTENT ) != 0;
}


public void configure( CoShapePageItemIF pi )
{
}


protected CoSizeSpec.MutableProxy createMutableProxy()
{
	return new CoContentSize.MutableProxy();

}


public CoSizeSpecIF deepClone()
{
	CoContentSize s = (CoContentSize) super.deepClone();
//	s.m_minSize = (CoDecimalDistanceIF) m_minSize.deepClone();
//	s.m_absoluteOffset = (CoDistanceIF) m_absoluteOffset.deepClone();
	return s;
}


public boolean equals( Object o )
{
	if ( this == o ) return true;
	if
		( getClass().isInstance( o ) )
	{
		CoContentSize s = (CoContentSize) o;
		
		return m_isSticky == s.m_isSticky && m_relativeOffset == s.m_relativeOffset && m_absoluteOffset == s.m_absoluteOffset;
	} else {
		return super.equals( o );
	}
}


public double getAbsoluteOffset()
{
	return m_absoluteOffset;
}


public double getRelativeOffset()
{
	return m_relativeOffset;
}


public boolean isContentDependent()
{
	return true;
}


public void setAbsoluteOffset( double a )
{
	m_absoluteOffset = a;
}


public void setRelativeOffset( double r )
{
	m_relativeOffset = r;
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-26
 */
 
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	super.xmlVisit( visitor );
	
	visitor.exportAttribute( XML_ABSOLUTE_OFFSET, Double.toString( m_absoluteOffset ) );
	visitor.exportAttribute( XML_RELATIVE_OFFSET, Double.toString( m_relativeOffset ) );
}
}