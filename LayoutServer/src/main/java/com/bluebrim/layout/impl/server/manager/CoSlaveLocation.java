package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoLocationSpecIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.impl.shared.CoSlaveLocationIF;
import com.bluebrim.layout.shared.*;

/**
 * Location spec that is assigned to page items that are slaves.
 * Since slave geoemtry depends on its master, this location spec does nothing.
 *
 * @author: Dennis
 */

public class CoSlaveLocation extends CoLocationSpec implements CoSlaveLocationIF
{
	public static final String XML_TAG = "slave-location";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-25
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoSlaveLocation();
}

CoSlaveLocation()
{
}


public boolean equals( Object o )
{
	return ( this == o ) || ( o instanceof CoSlaveLocation ) || super.equals( o );
}


public String getFactoryKey()
{
	return SLAVE_LOCATION;
}


public double getHeightAfterLocation ( CoLayoutableIF layoutable )
{
	return 0;
}


public String getIconName()
{
	return "CoSlaveLocation.gif";
}


public static CoSlaveLocation getInstance()
{
	return (CoSlaveLocation) getFactory().getSlaveLocation();
}


public CoLocationSpecIF getLocationSpecAfterReshape( boolean dx0, boolean dy0, boolean dx1, boolean dy1 )
{
	return null;
}


public String getType()
{
	return CoPageItemStringResources.getName(SLAVE_LOCATION);
}


public double getWidthAfterLocation( CoLayoutableIF layoutable )
{
	return 0;
}


public String getXmlTag()
{
	return XML_TAG;
}


public boolean isAbsolutePosition()
{
	return false;
}


public boolean isNull()
{
	return true;
}


public void placeLayoutable( CoLayoutableIF layoutable )
{
	layoutable.setLayoutSuccess( true );
}
}