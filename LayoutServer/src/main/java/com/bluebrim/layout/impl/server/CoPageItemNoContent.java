package com.bluebrim.layout.impl.server;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoContentWrapperPageItemIF;
import com.bluebrim.layout.impl.shared.CoPageItemContentIF;
import com.bluebrim.layout.impl.shared.CoPageItemNoContentIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.impl.shared.view.CoContentWrapperPageItemView;
import com.bluebrim.layout.impl.shared.view.CoPageItemContentView;
import com.bluebrim.layout.impl.shared.view.CoPageItemNoContentView;
import com.bluebrim.layout.shared.*;

/**
 * A page item content representing lack of content.
 * Typical use is line art (lines, boxes, ovals, ...).
 * 
 * @author: Dennis Malmström
 */
 
public class CoPageItemNoContent extends CoPageItemContent implements CoPageItemNoContentIF
{

	// xml tag constants
	public static final String XML_TAG = "page-item-no-content";

public static CoContentWrapperPageItemIF createContentWrapper()
{
	return new CoContentWrapperPageItem( new CoPageItemNoContent() );
}


CoPageItemContentView createView( CoContentWrapperPageItemView owner )
{
	return new CoPageItemNoContentView( owner, this, (CoPageItemContentIF.State) getState( null ) );
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


// content height = owner height

double getHeight()
{
	return m_owner.getCoShape().getHeight();
}


public String getName()
{
	return "";
}


protected String getType()
{
	return CoPageItemStringResources.getName( NO_CONTENT );
}

/**
 * Return type name that can be used by a wrapper object. The type name for
 * an empty page item fetches from its shape.
 */
protected String getWrappersType() {
	return CoPageItemStringResources.getName(m_owner.getCoShape().getFactoryKey());
}


// content width = owner width

double getWidth()
{
	return m_owner.getCoShape().getWidth();
}


public boolean isEmpty()
{
	return true;
}


public void visit( CoPageItemVisitor visitor, Object anything, boolean goDown )
{
	visitor.doToNoContent( this, anything, goDown );
}


/*
 * Used at XML import
 * Helena Rankegård 2001-10-23
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) {
	
	return new CoPageItemNoContent();
}
}