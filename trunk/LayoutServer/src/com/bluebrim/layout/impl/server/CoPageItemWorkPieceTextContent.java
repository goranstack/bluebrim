package com.bluebrim.layout.impl.server;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.debug.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * A page item that projects part of a text that is part of a workpiece.
 * 
 * 
 * @author: Dennis Malmström
 */

public class CoPageItemWorkPieceTextContent extends CoPageItemAbstractTextContent implements CoPageItemWorkPieceTextContentIF
{
	private CoTextDistribution m_distribution = null; // Object holding information about the text distribution that this page item is involved in.

	private int m_textTag = -1; // tag deciding which text in the workpiece to project


	public static final String XML_ACCEPTED_PARAGRAPH_TAGS = "accepted-paragraph-tags";
	public static final String XML_ORDER_TAG = "tag";
	// xml tag constants
	public static final String XML_TAG = "page-item-work-piece-text-content";


	protected CoFormattedText m_formattedText = new CoFormattedText();

/*
 * Used at XML import
 * Helena Rankegård 2001-10-30
 */
public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
	boolean didHandle = false;

	if (name == null) {
	} else {
		if (subModel instanceof Iterator) {
			if (name.equals(XML_ACCEPTED_PARAGRAPH_TAGS)) {
				Iterator iter = (Iterator) subModel;
				List tags = new ArrayList();
				while (iter.hasNext())
					tags.add(iter.next());
				setAcceptedTags(tags);
				didHandle = true;
			}
		}
	}

	if (!didHandle)
		super.xmlAddSubModel(name, subModel, context);

}

public void addAcceptedTags( List tags )
{
	m_formattedText.getAcceptedTags().addAll( tags );
	
	doAfterAcceptedTagsChanged();
}


protected void collectState( CoPageItemIF.State s, CoPageItemIF.ViewState viewState )
{
	super.collectState( s, viewState );
	
	CoPageItemWorkPieceTextContentIF.State S = (CoPageItemWorkPieceTextContentIF.State) s;

	S.m_textTag = getOrderTag();
	S.m_acceptedTags = getAcceptedTags();
	S.m_workPiece = getWorkPiece();
}


public static CoContentWrapperPageItemIF createContentWrapper()
{
	CoContentWrapperPageItem cw = new CoContentWrapperPageItem( new CoPageItemWorkPieceTextContent() );
	cw.setDoRunAround( true );

	return cw;
}


public CoPageItemIF.State createState()
{
	return new CoPageItemWorkPieceTextContentIF.State();
}


CoPageItemContentView createView( CoContentWrapperPageItemView owner )
{
	return new CoPageItemWorkPieceTextContentView( owner, this, (CoPageItemWorkPieceTextContentIF.State) getState( null ) );
}


protected void deepCopy( CoPageItem copy )
{
	super.deepCopy( copy );
	
	CoPageItemWorkPieceTextContent c = (CoPageItemWorkPieceTextContent) copy;
	
	if ( m_formattedText != null ) c.m_formattedText = m_formattedText.deepClone();
	c.m_distribution = null;

	c.setAcceptedTags( getAcceptedTags() );
}


protected final void doAfterAcceptedTagsChanged()
{
	postAcceptedTagsChanged();
	markDirty( "AcceptedTagsChanged" );
}


protected final void doAfterOrderTagChanged()
{
	postTextTagChanged();
	markDirty( "setOrderTag" );
}


public List getAcceptedTags()
{
	return m_formattedText.getAcceptedTags();
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


public CoFormattedText getFormattedText()
{
	CoFormattedText doc = m_formattedText;
	
	applyStylesToText( doc );
		
	return doc;
}


public CoFormattedText getMutableFormattedText() {
	CoAssertion.assertTrue(
		m_distribution != null,
		getClass() + ".getMutableFormattedText() called for instance without workpiece reference.");

	applyStylesToText(m_formattedText);
	return m_formattedText;
}


public String getName() {
	if (m_distribution != null)
		if (m_distribution.getSource() != null)
			return m_distribution.getSource().getName();
	return "";
}


public int getOrderTag()
{
	return m_textTag;
}


public CoTextDistribution getTextDistribution()
{
	return m_distribution;
}


protected String getType()
{
	return CoPageItemStringResources.getName( WORKPIECE_TEXT_CONTENT );
}

/**
 * Return type name that can be used by a wrapper object.
 */
protected String getWrappersType() {
	return CoLayoutServerResources.getName(CoLayoutServerConstants.WRAPPED_WORKPIECE_TEXT_CONTENT);
}


public CoWorkPieceIF getWorkPiece()
{
	CoLayoutArea area = (CoLayoutArea) m_owner.getAncestor( CoLayoutAreaIF.class );

	while
		( area != null )
	{
		if ( area.getWorkPiece() != null ) return area.getWorkPiece();

		CoCompositePageItem pi = (CoCompositePageItem) area.getParent();

		if ( pi == null ) break;
		
		area = (CoLayoutArea) pi.getAncestor( CoLayoutAreaIF.class );
	}
	
	return null;
}


public boolean isEmpty()
{
	return ( m_distribution == null ) || m_distribution.isNull();
}


protected void postAcceptedTagsChanged()
{
	if
		( m_owner != null )
	{
		m_owner.postDistributionBasisChange();
	}
}


protected void postAddTo( CoCompositePageItem parent )
{
	super.postAddTo( parent );

	CoLayoutArea wpla = null;
	while
		( parent != null )
	{
		if
			( parent instanceof CoLayoutArea )
		{
			wpla = (CoLayoutArea) parent;
			break;
		}
		parent = parent.m_parent;
	}

	if
		( wpla != null )
	{
		if ( m_textTag == -1 ) m_textTag = 0;
	}
}


protected void postTextTagChanged()
{
	if
		( m_owner != null )
	{
		m_owner.postDistributionBasisChange();
	}
}


protected void bindTextVariableValues( Map values )
{
	super.bindTextVariableValues( values );

	if
		( m_distribution != null )
	{
		if
			( m_distribution.getSource() != null )
		{
			values.put( "Skribent", m_distribution.getSource().getWriter() );
		}
	}
}


public void removeAcceptedTags( List tags )
{
	m_formattedText.getAcceptedTags().removeAll( tags );
	
	doAfterAcceptedTagsChanged();
}


public void setAcceptedTags( List tags )
{
	m_formattedText.getAcceptedTags().clear();
	addAcceptedTags( tags );
}


public void setDistribution( CoTextDistribution distribution )
{
	m_distribution = distribution;

	if
		( m_distribution == null || m_distribution.isNull() )
	{
		m_formattedText.clear();
		m_formattedText.setTimeStamp( System.currentTimeMillis() );
	}
	
	markDirty("setDistribution");
}


synchronized void setFormattedText( CoFormattedText doc )
{
	m_formattedText = doc;
	m_formattedText.setTimeStamp( System.currentTimeMillis() );
}


public void setOrderTag( int i )
{
	if ( getTextLock() == FIXED ) return;
	if ( m_textTag == i ) return;
	if ( ( getTextLock() == LOCKED ) && ( i != -1 ) && ( m_textTag != -1 ) ) return;
	
	m_textTag = i;
	doAfterOrderTagChanged();
}


public synchronized void updateFormattedText( CoFormattedText doc )
{
	m_formattedText = doc;
	
	if
		( m_distribution != null )
	{
		m_distribution.updateDocument();
	}
	
	doAfterTextChanged();
}


public void visit(CoPageItemVisitor visitor, Object anything, boolean goDown)
{
	visitor.doToWorkPieceTextContent( this, anything, goDown ); 
}


/*
 * Used at XML import
 * Helena Rankegård 2001-10-23
 */
 
public static CoXmlImportEnabledIF xmlCreateModel( Object superModel, Node node, CoXmlContext context ) 
{
	CoPageItemWorkPieceTextContent content = new CoPageItemWorkPieceTextContent();
	content.xmlInit( node.getAttributes(), context );
	return content;
}


/*
 * Used at XML import.
 * Helena Rankegård 2001-10-30
 */
 
public void xmlInit( NamedNodeMap map, CoXmlContext context )
{
	super.xmlInit( map, context );

	// xml init
	m_textTag = CoModelBuilder.getIntAttrVal( map, XML_ORDER_TAG, m_textTag );

/*	String str = CoModelBuilder.getAttrVal( map, XML_ACCEPTED_PARAGRAPH_TAGS, null );
	if
		( str != null )
	{
		List l = new ArrayList();
		StringTokenizer t = new StringTokenizer( str, ", " );
		while
			( t.hasMoreTokens() )
		{
			l.add( t.nextToken() );
		}

		setAcceptedTags( l );
	}*/
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-23
 */
 
public void xmlVisit(CoXmlVisitorIF visitor)
{
	super.xmlVisit(visitor);
	
	visitor.exportAttribute( XML_ORDER_TAG, Integer.toString( m_textTag ) );

	/*
	StringBuffer b = new StringBuffer();
	List l = getAcceptedTags();
	int I = l.size();
	for
		( int i = 0; i < I; i++ )
	{
		if ( i > 0 ) b.append( ", " );
		b.append( l.get( i ) );
	}
	
	visitor.exportAttribute( XML_ACCEPTED_PARAGRAPH_TAGS, b.toString() );
	*/
	visitor.export( XML_ACCEPTED_PARAGRAPH_TAGS, getAcceptedTags() );
}
}