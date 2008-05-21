package com.bluebrim.layout.impl.server.geom;
import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.server.*;
import com.bluebrim.layout.impl.shared.*;

/**
 * Abstract superclass for run around specifications.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoRunAroundSpec extends CoObject implements CoRunAroundSpecIF, Cloneable, com.bluebrim.xml.shared.CoXmlEnabledIF
{
/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public void xmlAddSubModel( String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context )
{
}
	// mutable proxy
	protected class MutableProxy implements CoRemoteRunAroundSpecIF
	{
		private CoRunAroundSpecIF.Owner m_owner;

		public void setOwner( CoRunAroundSpecIF.Owner owner ) { m_owner = owner; }
		public CoRunAroundSpecIF.Owner getOwner() { return m_owner; }
		
		public boolean doUseStroke() { return CoRunAroundSpec.this.doUseStroke(); }
		public void addPropertyChangeListener( CoPropertyChangeListener l ) { CoRunAroundSpec.this.addPropertyChangeListener( l ); }
	 	public void removePropertyChangeListener( CoPropertyChangeListener l ) { CoRunAroundSpec.this.removePropertyChangeListener( l ); }
	 	public CoRunAroundSpecIF getMutableProxy( CoRunAroundSpecIF.Owner owner ) { return CoRunAroundSpec.this.getMutableProxy( owner ); }
		public CoRunAroundSpecIF deepClone() { return CoRunAroundSpec.this.deepClone(); }
		public String getFactoryKey() { return CoRunAroundSpec.this.getFactoryKey(); }
		public double getTopMargin() { return CoRunAroundSpec.this.getTopMargin(); }
		public double getBottomMargin() { return CoRunAroundSpec.this.getBottomMargin(); }
		public double getLeftMargin() { return CoRunAroundSpec.this.getLeftMargin(); }
		public double getRightMargin() { return CoRunAroundSpec.this.getRightMargin(); }
	  		
		public void setUseStroke( boolean b )
		{
				if ( b == CoRunAroundSpec.this.doUseStroke() ) return;
	    	CoRunAroundSpec.this.setUseStroke( b ); 
	    	notifyOwner();
		}

	 	protected void notifyOwner()
  		{
			m_owner.runAroundSpecChanged();
  		}
	};

protected CoRunAroundSpec.MutableProxy createMutableProxy()
{
	return new MutableProxy();	
}


public CoRunAroundSpecIF deepClone()
{
	CoRunAroundSpec s	= null;
	
	try
	{
		s	= (CoRunAroundSpec) clone();
	}
	catch ( CloneNotSupportedException ex )
	{
		throw new RuntimeException( getClass() + ".clone() failed" );
	}
	
	return s;
}


public CoRunAroundSpecIF getMutableProxy( CoRunAroundSpecIF.Owner owner )
{
	MutableProxy mp = createMutableProxy();
	mp.setOwner( owner );
	return mp;
}


public abstract CoImmutableShapeIF getRunAroundShape( CoShapePageItem pi );


public abstract String xmlGetTag();


/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public void xmlImportFinished( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-26
 */
 
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
}
}