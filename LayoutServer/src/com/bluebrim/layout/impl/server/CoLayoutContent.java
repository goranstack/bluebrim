package com.bluebrim.layout.impl.server;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.content.server.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.text.impl.server.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

public class CoLayoutContent extends CoAtomicContent implements CoLayoutContentIF
{
	public static final String LAYOUT_PROPERTY = "layout_property";
	public static final String LAYOUT_CHANGED_PROPERTY = "layout_changed_property";
	public static final String XML_TAG = "layout-content";

	private long m_timeStamp = 0;
	private CoCaption m_caption;
	private CoLayout m_layout;

public boolean addTo( CoContentReceiver wp )
{
	return wp.add( this );
}


public String getDescription()
{
	return getName();
}
public String getFactoryKey() {
	return CoLayoutContentIF.FACTORY_KEY;
}
public String getIconName() {
	return ICON_NAME;
}
public String getIconResourceAnchor() {
	return CoLayoutContentIF.class.getName();
}
public String getIdentity() {
	return getName();
}


public long getTimeStamp()
{
	return m_timeStamp;
}

public CoView getView()
{
	return m_layout.getView();
}
public boolean isRenameable() {
	return true;
}


public void xmlVisit( CoXmlVisitorIF visitor )
{
	super.xmlVisit( visitor );
	visitor.export(m_layout);
	visitor.export( m_caption.getFormattedText( null ) );
}


public List getPageItems()
{
	List l = new ArrayList();
	l.add( m_layout );
	return l;
}


public List getLayouts()
{	
	return getPageItems();
}

public void pageItemsChanged()
{
	m_timeStamp = System.currentTimeMillis();
	
	firePropertyChange( LAYOUT_CHANGED_PROPERTY, null, m_layout );

	markDirty();
}


public void layoutsChanged()
{
	pageItemsChanged();
}


public CoLayoutContent()
{
	this( new CoLayoutArea() );
}

public CoLayoutContent( CoShapePageItemIF layout )
{

	init( layout );
}

public CoLayoutContent( CoGOI goi )
{
	super( goi);
	
	init( null );
}


public CoFormattedTextHolderIF getCaption() {
	return m_caption;
}

public CoLayout getLayout()
{
	return m_layout;
}

private void init( CoShapePageItemIF pageItem )
{
	setName(CoLayoutServerResources.getName(CoLayoutServerConstants.UNTITLED_LAYOUT));
	setLayout( pageItem );
//	m_pageItem = pageItem;

	m_caption = new CoCaption( this );
}

protected void prepareCopy( CoContent copy )
{
	super.prepareCopy( copy );

	CoLayoutContent layoutContent	= (CoLayoutContent) copy;

	layoutContent.m_layout = m_layout.copy();
	layoutContent.m_caption = new CoCaption( layoutContent );
}

public void setLayout( CoLayout pageItem )
{
	if
		( pageItem != m_layout )
	{
		m_layout = pageItem;
		m_timeStamp = System.currentTimeMillis();
		firePropertyChange(LAYOUT_PROPERTY, null, m_layout );
		markDirty();
	}
}

public String getType()
{
	return CoLayoutServerResources.getName( LAYOUT_CONTENT );
}

}