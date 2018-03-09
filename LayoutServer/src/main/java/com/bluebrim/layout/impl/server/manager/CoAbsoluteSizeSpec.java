package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

//

public abstract class CoAbsoluteSizeSpec extends CoSizeSpec implements CoAbsoluteSizeSpecIF {

		protected double m_distance;
		// xml tags
		public static final String XML_DISTANCE = "distance";

		protected class MutableProxy extends CoSizeSpec.MutableProxy implements CoRemoteAbsoluteSizeSpecIF
		{
	//		public CoDistanceIF createDistance( String mType ) { return CoAbsoluteSizeSpec.this.createDistance( mType ); }
			public double getDistance() { return CoAbsoluteSizeSpec.this.getDistance(); }
			public void setDistance( double d )
			{
				CoAbsoluteSizeSpec.this.setDistance( d );
				m_owner.notifyOwner();
			}
			public String getDescription()
			{
				return CoAbsoluteSizeSpec.this.getDescription();
			}
	
		}
	
	
	/**
	 * CoAbsoluteSizeSpec constructor comment.
	 */
	public CoAbsoluteSizeSpec()
	{
		super();
	}
	
	
	/**
	 * CoAbsoluteSizeSpec constructor comment.
	 */
	public CoAbsoluteSizeSpec( double distance )
	{
		super();
	
		setDistance( distance );
	}
	
	/**
	 * Contructor used for XML import.
	 * Helena Rankegård 2001-10-25
	 */
	protected CoAbsoluteSizeSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context )
	{
		super( node, context );
		
		// xml init
		NamedNodeMap map = node.getAttributes();
		m_distance = CoModelBuilder.getDoubleAttrVal( map, XML_DISTANCE, m_distance );
	}
	
		
	
	protected CoSizeSpec.MutableProxy createMutableProxy()
	{
		return new CoAbsoluteSizeSpec.MutableProxy();
	
	}
	
	
	public CoSizeSpecIF deepClone ()
	{
		CoAbsoluteSizeSpec tSpec	= (CoAbsoluteSizeSpec )super.deepClone();
	//	if ( m_distance != null ) tSpec.m_distance				= (CoDistanceIF )m_distance.deepClone();
		return tSpec;
	}
	
	
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if
			( getClass().isInstance( o ) )
		{
			CoAbsoluteSizeSpec s = (CoAbsoluteSizeSpec) o;
			
			return m_isSticky == s.m_isSticky && m_distance == s.m_distance;
		} else {
			return super.equals( o );
		}
	}
	
	
	public double getDistance()
	{
		return m_distance;
	}
	
	
	public void setDistance(double distance)
	{
		m_distance = distance;
	}
	
	
	public String toString()
	{
		return m_distance + "";
	}
	
	
	/*
	 * Used at XML export
	 * Helena Rankegård 2001-10-26
	 */
	 
	public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
	{
		super.xmlVisit( visitor );
		
		visitor.exportAttribute( XML_DISTANCE, Double.toString( m_distance ) );
	}
}