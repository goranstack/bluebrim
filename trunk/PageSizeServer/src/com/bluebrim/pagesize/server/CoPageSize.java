package com.bluebrim.pagesize.server;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.page.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.transact.shared.*;
import com.bluebrim.xml.shared.*;

/**
  Representerar ett pappersformat. Har namn ex "tabloid" och storlek.
 	Internt lagras storheterna i punkter.
 */
public class CoPageSize extends CoObject implements CoPageSizeIF
{
	public static final String XML_GOI = "goi";
	public static final String XML_HEIGHT = "height";
	public static final String XML_NAME = "name";
	public static final String XML_TAG = "page-size";
	public static final String XML_WIDTH = "width";
	private CoGOI m_goi;
	private String 		name;
	private final CoDimension size	= new CoDimension(0.0f, 0.0f);
	private boolean m_dirty;

public CoPageSize () {
	this(CoStringResources.getName(CoConstants.UNTITLED), 0.0f, 0.0f);
}

public CoPageSize (String name, float width, float height)
{
	this(CoRefService.createGOI());

	setName(name);
	setSize(width,height);
}

public CoPageSize (String name, CoDimension size ) {
	this(name,size.getWidth(), size.getHeight());
}

public boolean equals(Object obj) {
	if (this == obj)
		return true;
	else
	{
		if (obj != null && obj instanceof CoPageSizeIF )
		{
			return getName().equals(((CoPageSizeIF )obj).getName());
		}
		else
			return false; 
	}
		
}

public final String getFactoryKey() {
	return CoPageSizeConstants.PAGE_SIZE;
}

public final float getHeight() {
	 return getSize().getHeight();
}
public String getIconName() {
	return ICON_NAME;
}
public String getIconResourceAnchor() {
	return CoPageSizeIF.class.getName();
}

public final String getIdentity() {
	return getName();
}

public String getName() {
	return name;
}

public final CoDimension getSize() {
	 return size;
}
public String getSmallIconName() {
	return SMALL_ICON_NAME;
}

public final String getType() {
	return CoPageSizeServerResources.getName(CoPageSizeConstants.PAGE_SIZE);
}

public final float getWidth() {
	 return getSize().getWidth();
}

public int hashCode() {
	return getName().hashCode();
}
private void markDirty()
{
	m_dirty = ! m_dirty;
}

public final void setName(String aName) {
	this.name 		= aName;
}

public final void setSize(float width, float height ) {
	size.setWidth( width );
	size.setHeight( height );
	markDirty();
}


public CoPageSize( String singletonId )
{
	this( new CoGOI( new CoSpecificContext( "pageSizeSingleton", "anySystem", singletonId ), 0 ) );
}

private CoPageSize( CoGOI goi )
{
	m_goi = goi;
}

public long getCOI()
{
	return m_goi.getCoi();
}

public CoGOI getGOI()
{
	return m_goi;
}

public void xmlInit( Map attributes )
{
	setName( CoXmlUtilities.parseString( (String) attributes.get( XML_NAME ), getName() ) );

	float w = CoXmlUtilities.parseFloat( (String) attributes.get( XML_WIDTH ), getWidth() );
	float h = CoXmlUtilities.parseFloat( (String) attributes.get( XML_HEIGHT ), getHeight() );
	setSize( w, h );
	
	String tmp = CoXmlUtilities.parseString( (String) attributes.get( XML_GOI ), null );
	if
		( tmp != null )
	{
		m_goi = new CoGOI( tmp );
	}
}

public void xmlVisit(CoXmlVisitorIF visitor)
{
	visitor.exportAttribute( XML_NAME, getName() );
	visitor.exportAttribute( XML_WIDTH, Float.toString( getWidth() ) );
	visitor.exportAttribute( XML_HEIGHT, Float.toString( getHeight() ) );
	visitor.exportGOIAttribute( XML_GOI, m_goi);
}
}