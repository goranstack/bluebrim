package com.bluebrim.layout.impl.server;

import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.image.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * A page item that projects a workpiece (article, ad, ...) or just acts as a parent of other page items.
 * The contents of the workpiece are distributed among the children according to a tag system (see method distribute).
 *
 * @author: Dennis Malmström
 */
 
public class CoLayoutArea extends CoCompositePageItem implements CoLayoutAreaIF
{
	// projected content
	private CoWorkPieceIF				m_workPiece;
	private int 						m_workPieceLock = UNLOCKED; // content lock, see CoLayoutAreaIF
	private boolean 					m_acceptsWorkPiece = true;
	private DistributionTargetCollector m_distributionTargetCollector;

	// xml tag constants
	public static final String XML_TAG = "layout-area";
	public static final String XML_WORKPIECE = "workpiece";
	public static final String XML_WORKPIECE_LOCK = "workpiece-lock";
	public static final String XML_ACCEPTS_WORKPIECE = "accepts-workpiece";


	private class DistributionTargetCollector extends CoPageItemVisitor
	{
		public final List m_textContentBucket = new ArrayList();
		public final List m_imageContentBucket = new ArrayList();
		public final List m_layoutContentBucket = new ArrayList();

		public void reset()
		{
			m_textContentBucket.clear();
			m_imageContentBucket.clear();
			m_layoutContentBucket.clear();
		}
		
		public void doToLayoutArea(CoLayoutArea layoutArea, Object anything, boolean goDown )
		{
			if ( ( layoutArea == CoLayoutArea.this ) || ( layoutArea.getWorkPiece() == null ) ) super.doToLayoutArea( layoutArea, anything, goDown );
		}
		
		public void doToWorkPieceTextContent( CoPageItemWorkPieceTextContent textContent, Object anything, boolean goDown )
		{
			m_textContentBucket.add( textContent );
		}
		
		public void doToImageContent( CoPageItemImageContent imageContent, Object anything, boolean goDown )
		{
			if ( imageContent.getOrderTag() >= 0 ) m_imageContentBucket.add( imageContent );
		}
		
		public void doToLayoutContent( CoPageItemLayoutContent layoutContent, Object anything, boolean goDown )
		{
			if ( layoutContent.getOrderTag() >= 0 ) m_layoutContentBucket.add( layoutContent );
		}
	}


public CoLayoutArea()
{
	this( null );
}

public CoLayoutArea( CoWorkPieceIF wp )
{
	super();

	setWorkPiece( wp );
}

public CoPageItemIF.State createState()
{
	return new CoLayoutAreaIF.State();
}

/**
 * Perform workpiece content distribution
 */
public void distribute()
{
	boolean old = m_layoutEngineState.m_isLayoutEngineActive;
	doSetLayoutEngineActive( false );

	CoTextDistribution defaultTextDistribution = null;
	
	if 
		( m_workPiece != null )
	{
		defaultTextDistribution = ( (CoPageItemFactory) CoFactoryManager.getFactory( CoPageItemFactoryIF.PAGE_ITEM_FACTORY ) ).getNullTextDistribution();//CoTextDistribution.getNullTextDistribution();
	}

	collectWorkPieceContents();
	
	// Distribute texts

	// clear all text content children
	List allDestinations = m_distributionTargetCollector.m_textContentBucket; // [ CoPageItemWorkPieceTextContentIF ]
	Iterator iter = allDestinations.iterator();
	while
		( iter.hasNext() )
	{
		CoPageItemWorkPieceTextContent atc = (CoPageItemWorkPieceTextContent) iter.next();
		atc.setDistribution( defaultTextDistribution );
	}

	if
		( m_workPiece != null )
	{
		
		// traverse all texts
		List texts = m_workPiece.getTexts();
		int I = texts.size();
		for
			( int textTag = 0; textTag < I; textTag++ )
		{
			List destinations = new ArrayList(); // [ CoPageItemWorkPieceTextContentIF ]
			List destinationTexts = new ArrayList(); // [ CoFormattedText ]

			// collect all text content children (and their documents) having the current order tag
			int N = allDestinations.size();
			for
				( int n = 0; n < N; n++ )
			{
				CoPageItemWorkPieceTextContentIF atc = (CoPageItemWorkPieceTextContentIF) allDestinations.get( n );
				if ( atc.getOrderTag() != textTag ) continue;

				destinations.add( atc );
				CoFormattedText doc = atc.getFormattedText();
				doc.clear();
				destinationTexts.add( doc );
			}

			// distribute text over the collected documents
			CoTextContentIF text = (CoTextContentIF) texts.get( textTag );
			CoFormattedText leftOvers = CoDocumentDistributer.distribute( text.getFormattedText( null ), destinationTexts );

			// create and distribute the information needed to collect the text
			CoTextDistribution td = new CoTextDistribution( text, destinations, leftOvers );
			iter = destinations.iterator();
			while
				( iter.hasNext() )
			{
				( (CoPageItemWorkPieceTextContent) iter.next() ).setDistribution( td );
			}

			// distribute the documents over the text content children
			N = destinations.size();
			for
				( int n = 0; n < N; n++ )
			{
				CoPageItemWorkPieceTextContent atc = (CoPageItemWorkPieceTextContent) destinations.get( n );
				CoFormattedText doc = (CoFormattedText) destinationTexts.get( n );
				atc.setFormattedText( doc );
			}
		}
	}

	
	// Distribute Images
	if
		( m_workPiece != null )
	{		
		List images = m_workPiece.getImages();

		// traverse all image content children
		Iterator i = m_distributionTargetCollector.m_imageContentBucket.iterator();
		while
			( i.hasNext() )
		{
			CoPageItemImageContent imageContent = (CoPageItemImageContent) i.next();

			// fetch and set the appropriate image
			int j = imageContent.getOrderTag();
			if
				( ( j >= 0 ) && ( j < images.size() ) )
			{
				imageContent.setImageContent( (CoImageContentIF) images.get( j ) );
			} else {
				imageContent.setImageContent( (CoImageContentIF) null ); // order tag out of bounds -> no image
			}
		}
	} else {
		
		// no workpiece -> set all images = null
		Iterator i = m_distributionTargetCollector.m_imageContentBucket.iterator();
		while
			( i.hasNext() )
		{
			CoPageItemImageContent imageContent = (CoPageItemImageContent) i.next();
			imageContent.setImageContent( (CoImageContentIF) null );
		}
	}

	
	// Distribute Layouts
	if
		( m_workPiece != null )
	{		
		List layouts = m_workPiece.getLayouts();

		// traverse all layout content children
		Iterator i = m_distributionTargetCollector.m_layoutContentBucket.iterator();
		while
			( i.hasNext() )
		{
			CoPageItemLayoutContent layoutContent = (CoPageItemLayoutContent) i.next();

			// fetch and set the appropriate layout
			int j = layoutContent.getOrderTag();
			if
				( ( j >= 0 ) && ( j < layouts.size() ) )
			{
				layoutContent.setLayoutContent( (CoLayoutContentIF) layouts.get( j ) );
			} else {
				layoutContent.setLayoutContent( (CoLayoutContentIF) null ); // order tag out of bounds -> no layout
			}
		}
	} else {
		
		// no workpiece -> set all layouts = null
		Iterator i = m_distributionTargetCollector.m_layoutContentBucket.iterator();
		while
			( i.hasNext() )
		{
			( (CoPageItemLayoutContent) i.next() ).setLayoutContent( (CoLayoutContentIF) null );
		}
	}

	
	doSetLayoutEngineActive( old );

	// Activate changes 
	doAfterDistribution();
}

protected final void doAfterWorkPieceChanged()
{
	postWorkPieceChanged();
	
	markDirty( "WorkPieceChanged" );
}

public String getName() {
	if (m_workPiece == null)
		return getType();
	else
		return getType() + ": " + m_workPiece.getName();

}

public String getType() {
	return CoPageItemStringResources.getName( LAYOUT_AREA );
}

int getPositionWeight()
{
	return 0;
}

public CoWorkPieceIF getWorkPiece()
{
	return m_workPiece;
}





protected void postWorkPieceChanged()
{
	postDistributionBasisChange();
}



/*
 * Used at XML export
 * Helena Rankegård 2001-10-23
 */
 
public void xmlVisit(CoXmlVisitorIF visitor)
{
	super.xmlVisit( visitor );
	
	visitor.exportAttribute( XML_ACCEPTS_WORKPIECE, ( m_acceptsWorkPiece ? Boolean.TRUE : Boolean.FALSE ).toString() );
	visitor.exportAttribute( XML_WORKPIECE_LOCK, Integer.toString( m_workPieceLock ) );
	
	if ( m_workPiece != null ) visitor.exportAsGOIorObject( XML_WORKPIECE, m_workPiece );
	//PENDING: m_insertionRequest ????????
}



/**
 * Traverse this page item subtree and collect all content projecting children
 */

private void collectWorkPieceContents()
{
	if
		( m_distributionTargetCollector == null )
	{
		m_distributionTargetCollector = new DistributionTargetCollector();
	}
	
	m_distributionTargetCollector.reset();
	visit( m_distributionTargetCollector, null, true );
}

protected final void doAfterAcceptsWorkPieceChanged()
{
	postAcceptsWorkPieceChanged();
	
	markDirty( "AcceptsWorkPieceChanged" );
}

public boolean getAcceptsWorkPiece()
{
	return m_acceptsWorkPiece;
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



protected void postAcceptsWorkPieceChanged()
{
	if ( ! m_acceptsWorkPiece ) setWorkPiece( (CoWorkPieceIF) null );
}





public void setAcceptsWorkPiece( boolean b )
{
	if ( b == m_acceptsWorkPiece ) return;
	m_acceptsWorkPiece = b;
	doAfterAcceptsWorkPieceChanged();
}








private boolean canSetWorkPiece( CoWorkPieceIF wp )
{
	if ( m_workPiece == wp ) return false;
	if ( m_workPieceLock == FIXED ) return false;
	if ( ( m_workPieceLock == LOCKED ) && ( m_workPiece != null ) && ( wp != null ) ) return false;

	return true;
}



protected void deepCopy( CoPageItem copy )
{
	super.deepCopy( copy );
	
	CoLayoutArea c = (CoLayoutArea) copy;
	
//	c.m_distributer = null;
	c.m_workPiece = null;
	c.m_distributionTargetCollector = null;
	
	int tmp = c.m_workPieceLock;
	c.m_workPieceLock = UNLOCKED;
	c.setWorkPiece( m_workPiece );
	c.m_workPieceLock = tmp;

}

// content distribution was performed, layout engine might have to be run

protected final void doAfterDistribution()
{
	postDistribution();

	performLocalLayout();

	markDirty( "distribute" );
}

protected final void doAfterWorkPieceLockChanged()
{
	postWorkPieceLockChanged();
	
	markDirty( "WorkPieceLockChanged" );
}


public int getWorkPieceLock()
{
	return m_workPieceLock;
}

protected void postDistribution()
{
	requestLocalLayout();
}

// distribution basis was changed, redistibute workpiece contents

public void postDistributionBasisChange()
{
	super.postDistributionBasisChange();
	distribute();
}

// subtree was changed, redistibute workpiece contents

public void postSubTreeStructureChange()
{
	super.postSubTreeStructureChange();
	distribute();
}

protected void postWorkPieceLockChanged()
{
}





public void setWorkPieceLock( int l )
{
//	if ( m_workPieceLock == FIXED ) return;
	if ( l == m_workPieceLock ) return;
	
	m_workPieceLock = l;
	doAfterWorkPieceLockChanged();
}

public String toString()
{
	return super.toString() + " (" + m_children.size() + ") " + m_layoutEngineState.m_isLayoutEngineActive;
}



protected void collectState( CoPageItemIF.State s, CoPageItemIF.ViewState viewState )
{
	super.collectState( s, viewState );
	
	CoLayoutAreaIF.State S = (CoLayoutAreaIF.State) s;

	S.m_workPiece = m_workPiece;
	S.m_acceptsWorkPiece = m_acceptsWorkPiece;
	S.m_workPieceLock = m_workPieceLock;
}

public CoShapePageItemView createView( CoCompositePageItemView parent, int detailMode )
{
	if
		( detailMode == CoPageItemView.DETAILS_STOP_AT_FIRST_PROJECTOR )
	{
		if
			( m_acceptsWorkPiece )
		{
			return new CoStopAtFirstProjectorPageItemView( this, parent, (CoShapePageItemIF.State) getState( null ) );
		}
	}

	return super.createView( parent, detailMode );
}

protected void doDestroy()
{
	setWorkPiece( null );

	super.doDestroy();
}


protected CoShapePageItemView newView( CoCompositePageItemView parent, int detailMode )
{
	return new CoLayoutAreaView( this, parent, (CoLayoutAreaIF.State) getState( null ), detailMode );
}


public void setWorkPiece( CoWorkPieceIF wp )
{
	if ( ! canSetWorkPiece( wp ) ) return;
		
	if
		( m_workPiece != null )
	{
		m_workPiece.dettachLayoutProjector( this ); // unregister as workpiece listener
	}
	
	m_workPiece =  wp;
	
	if
		( m_workPiece != null )
	{
		m_acceptsWorkPiece = true;
//		if ( m_distributer == null ) m_distributer = new CoDocumentDistributer();
		m_workPiece.attachLayoutProjector( this ); // register as workpiece listener
	}


	doAfterWorkPieceChanged();
}

public void visit (CoPageItemVisitor visitor, Object anything, boolean goDown)
{
	visitor.doToLayoutArea(this, anything, goDown);
}


/*
 * Used at XML import
 * Helena Rankegård 2001-10-23
 */
 
public static CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, CoXmlContext context ) 
{
	CoLayoutArea area = new CoLayoutArea();
	area.xmlInit( node.getAttributes(), context );
	return area;
}


/*
 * Used at XML import
 * Helena Rankegård 2001-10-23
 */
public void xmlImportFinished( Node node, CoXmlContext context )
{
	super.xmlImportFinished( node, context );
	if (context.useGOI()) {	
		NamedNodeMap map = node.getAttributes();
		String goiStr = CoModelBuilder.getAttrVal( map, XML_WORKPIECE, null );
		if (goiStr != null) {
			CoWorkPieceIF wp = ((CoContentRegistry) context.getValue(CoContentRegistry.class)).lookupWorkPiece(new CoGOI(goiStr));
	
			if (wp == null) {
				System.err.println("Warning: could not find workpiece: \"" + goiStr + " \"");
			} else {
				setWorkPiece(wp);
			}
		}
	}
}

public String getFactoryKey()
{
	return LAYOUT_AREA;
}

/*
 * Used at XML import.
 * Helena Rankegård 2001-10-30
 */
 
public void xmlInit( NamedNodeMap map, CoXmlContext context )
{
	super.xmlInit( map, context );

	// xml init
	m_acceptsWorkPiece = CoModelBuilder.getBoolAttrVal( map, XML_ACCEPTS_WORKPIECE, m_acceptsWorkPiece );
	m_workPieceLock = CoModelBuilder.getIntAttrVal( map, XML_WORKPIECE_LOCK, m_workPieceLock );
}

public void xmlAddSubModel(String parameter, Object subModel, CoXmlContext context) {
	super.xmlAddSubModel(parameter, subModel, context);
	if (parameter == null) {

	} else {

		if (!context.useGOI()) {
			if (parameter.equals(XML_WORKPIECE)) {
				setWorkPiece((CoWorkPieceIF) subModel);
			};
		}
	}

}

}