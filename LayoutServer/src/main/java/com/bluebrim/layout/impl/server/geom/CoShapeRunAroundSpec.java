package com.bluebrim.layout.impl.server.geom;
import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.server.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of shape run around specification.
 * 
 * @author: Dennis Malmström
 */

public class CoShapeRunAroundSpec extends CoRunAroundSpec implements CoShapeRunAroundSpecIF
{
	private boolean m_useStroke = true;
	private double m_margin = 0;
	protected class MutableProxy extends CoRunAroundSpec.MutableProxy implements CoRemoteShapeRunAroundSpecIF
	{
		public double getMargin(){ return CoShapeRunAroundSpec.this.getMargin(); }

		public void setMargin( double m )
		{
				if ( m == CoShapeRunAroundSpec.this.getMargin() ) return;
	    	CoShapeRunAroundSpec.this.setMargin( m ); 
	    	notifyOwner();
		}
	}
		

	public static final String XML_MARGIN = "margin";
	// xml tags
	public static final String XML_TAG = "shape-run-around-spec";
	public static final String XML_USE_STROKE = "use-stroke";

/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoShapeRunAroundSpec( node, context );
}


public CoShapeRunAroundSpec() 
{
	super();
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-26
 */
 
protected CoShapeRunAroundSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super();

	// xml init
	NamedNodeMap map = node.getAttributes();
	
	m_useStroke = CoModelBuilder.getBoolAttrVal( map, XML_USE_STROKE, m_useStroke );
	m_margin = CoModelBuilder.getDoubleAttrVal( map, XML_MARGIN, m_margin );
}


protected CoRunAroundSpec.MutableProxy createMutableProxy()
{
	return new MutableProxy();	
}


public boolean doUseStroke()
{
	return m_useStroke;
}


public boolean equals( Object s )
{
	if ( s == this ) return true;
	
	if ( ! ( s instanceof CoShapeRunAroundSpec ) ) return false;

	CoShapeRunAroundSpec S = (CoShapeRunAroundSpec) s;
	
	return ( m_useStroke == S.m_useStroke ) && ( m_margin == S.m_margin );
}


public double getBottomMargin()
{
	return m_margin;
}


public String getFactoryKey()
{
	return SHAPE_RUN_AROUND_SPEC;
}


public double getLeftMargin()
{
	return m_margin;
}


public double getMargin()
{
	return m_margin;
}


public double getRightMargin()
{
	return m_margin;
}


public CoImmutableShapeIF getRunAroundShape( CoShapePageItem pi )
{
	CoImmutableShapeIF s = null;

	if
		( m_useStroke )
	{
		s = pi.getExteriorShape();
	} else {
		s = pi.getCoShape();
	}

	if
		( m_margin != 0 )
	{
		s = s.createExpandedInstance( m_margin );
	}
	
	return s;
}


public double getTopMargin()
{
	return m_margin;
}


public void setMargin( double m )
{
	m_margin = m;
}


public void setUseStroke( boolean b )
{
	m_useStroke = b;
}


public String xmlGetTag()
{
	return XML_TAG;
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-26
 */
 
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	super.xmlVisit( visitor );
	
	visitor.exportAttribute( XML_USE_STROKE, ( m_useStroke ? Boolean.TRUE : Boolean.FALSE ).toString() );
	visitor.exportAttribute( XML_MARGIN, Double.toString( m_margin ) );
}
}