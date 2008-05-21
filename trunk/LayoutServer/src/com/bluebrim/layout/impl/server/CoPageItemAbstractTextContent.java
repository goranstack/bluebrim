package com.bluebrim.layout.impl.server;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.text.shared.swing.*;
import com.bluebrim.xml.shared.*;

/**
 * Abstract class for page items that project formatted text.
 * 
 * @author: Dennis Malmström
 */
 
public abstract class CoPageItemAbstractTextContent extends CoPageItemContent implements CoPageItemAbstractTextContentIF, CoTextGeometryIF
{
	// first baseline offset
	private String m_firstBaselineType = CoPageItemAbstractTextContentIF.ASCENT; // not implemented in view
	private float m_firstBaselineOffset = Float.NaN;

	// vertical alignment
	private String m_verticalAlignmentType = CoPageItemAbstractTextContentIF.ALIGN_TOP;
	private float m_verticalAlignmentMaxInter = Float.NaN;


	// text margins inside owner
	private float m_bottomMargin = 0;
	private float m_leftMargin = 0;
	private float m_rightMargin = 0;
	private float m_topMargin = 0;

	/*
		BEWARE: This attribute has nothing to do with the locking of the text which is used to prevent parallell editing.
		        This lock controls how the projected text can be replaced.
	*/

	private transient CoImmutableStyledDocumentIF m_cachedStyledDocument;
	private int m_textLock = UNLOCKED; // see CoPageItemAbstractTextContentIF
	public static final String XML_BOTTOM_MARGIN = "bottom-margin";
	// xml tag constants
	public static final String XML_FIRST_BASELINE = "first-baseline";
	public static final String XML_FIRST_BASELINE_OFFSET = "first-baseline-offset";
	public static final String XML_LEFT_MARGIN = "left-margin";
	public static final String XML_LOCK = "lock";
	public static final String XML_RIGHT_MARGIN = "right-margin";
	public static final String XML_TOP_MARGIN = "top-marginn";
	public static final String XML_VERTICAL_ALIGNMENT = "vertical-alignment";
	public static final String XML_VERTICAL_ALIGNMENT_MAX_PARAGRAPH_SPACING = "max-paragraph-spacing";


// adjust owner height to fit text
// PENDING: Make it faster.
//          Idea: do not run around / nothing to run around -> create text views for 1 column, then distribute rows among avialable columns

public void adjustHeightToText()
{
	m_owner.makeLayoutSpecAbsolute( false, false, false, true, false, true );
	
	// calculate width
	double dw = getHorizontalPadding();
	double dh = getVerticalPadding();
	
	CoImmutableStyledDocumentIF doc = getStyledDocument();
	if ( doc.getLength() == 0 ) return;

	// calculate available height (column geometry is constrained by parent bounds)
	double availableHeight;
	if
		( m_owner.getParent() == null )
	{
		availableHeight = Double.MAX_VALUE;
	} else {
		CoShapePageItemIF pi = m_owner;
		availableHeight = - pi.getY();

		while
			( pi.getParent() != null )
		{
			pi = (CoShapePageItemIF) pi.getParent();
			availableHeight -= pi.getY();
		}
		availableHeight += pi.getCoShape().getHeight() + pi.getY();
	}

	
	CoBaseLineGeometryIF blg = m_owner.m_baseLineGrid.getBaseLineGeometry( 0 );
	double H = m_owner.getHeight();


	m_owner.setHeight( availableHeight );
	if
		( CoStyledTextMeasurer.doesOverflow( doc, m_owner.m_columnGrid.getColumnGeometry( getColumnGridShape() ), blg, this ) )
	{
		// max available height isn't enough -> give up
		return;
	}

	if
		( m_owner.m_columnGrid.getColumnCount() == 1 )
	{
		m_owner.setHeight( dh + CoStyledTextMeasurer.getHeight( doc, m_owner.m_columnGrid.getColumnGeometry( getColumnGridShape() ), blg, this ) );
		return;
	}

	// extrapolate height to give a better starting point
	m_owner.setHeight( H + dh );
	double i = (double) CoStyledTextMeasurer.getVisiblePortion( doc, m_owner.m_columnGrid.getColumnGeometry( getColumnGridShape() ), blg, this );
	double I = (double) doc.getLength();
	H *= I / i;
	
	// binary search in height space
	double dH = H / 2;	
	m_owner.setHeight( H + dh );
	double sign = CoStyledTextMeasurer.doesOverflow( doc, m_owner.m_columnGrid.getColumnGeometry( getColumnGridShape() ), blg, this ) ? 1 : -1;

	while
		( dH >= 1 )
	{
		H += sign * dH;
		m_owner.setHeight( H + dh );
		sign = CoStyledTextMeasurer.doesOverflow( doc, m_owner.m_columnGrid.getColumnGeometry( getColumnGridShape() ), blg, this ) ? 1 : -1;
		dH /= 2;
	}

	// binary search might yield a height that is a little (<=1) to small, fix it (one iteration should be enough)
	while
		( sign == 1 )
	{
		H += 1;
		m_owner.setHeight( H + dh );
		sign = CoStyledTextMeasurer.doesOverflow( doc, m_owner.m_columnGrid.getColumnGeometry( getColumnGridShape() ), blg, this ) ? 1 : -1;
	}
	
}

public CoPageItemIF.State createState()
{
	return new CoPageItemAbstractTextContentIF.State();
}

protected final void doAfterMarginChanged()
{
	postMarginChanged();
	markDirty( "setMargin" );
}
protected final void doAfterTextChanged()
{
	postTextChanged();
	markDirty("TextChanged");
}

protected CoImmutableShapeIF getColumnGridShape()
{
	if
		( m_owner.getEffectiveCoShape() instanceof CoRectangleShapeIF )
	{
		float d = m_owner.m_strokeProperties.getInsideWidth();
		CoRectangleShapeIF s = (CoRectangleShapeIF) ( (CoShape) m_owner.getEffectiveCoShape() ).createExpandedInstance( - d );

		if
			(
				( m_topMargin != 0 )
			||
				( m_bottomMargin != 0 )
			||
				( m_leftMargin != 0 )
			||
				( m_rightMargin != 0 )
			)
		{
			s = (CoRectangleShapeIF) s.deepClone();
			s.translateBy( m_leftMargin, m_topMargin );
			s.setSize( s.getWidth() - m_leftMargin - m_rightMargin, s.getHeight() - m_topMargin - m_bottomMargin );
		}
		
		return s;
		
	} else {
		
		float d = m_owner.m_strokeProperties.getInsideWidth() + m_topMargin;
		CoImmutableShapeIF s = ( (CoShape) m_owner.getEffectiveCoShape() ).createExpandedInstance( - d );
		return s;
	}

	
	
	
	
	
}
public double getEmptyHeight()
{
	return 40;
}
public float getFirstBaselineOffset()
{
	return m_firstBaselineOffset;
}
public String getFirstBaselineType()
{
	return m_firstBaselineType;
}
// calculate text height given the owners width (does not consider run around effects or constraints due to parent height)

double getHeight()
{
	double dw = getHorizontalPadding();
	double dh = getVerticalPadding();

	CoImmutableStyledDocumentIF doc = getStyledDocument();
	
	double w = m_owner.getWidth() - dw;
	if ( w == 0 ) return 0;

	double h = CoStyledTextMeasurer.getHeight( (CoStyledDocumentIF) doc,
		                                         (float) w,
		                                         m_owner.m_baseLineGrid.getBaseLineGeometry( 0 ),
		                                         this );

	return h + dh;
}

int getPositionWeight()
{
	return 0;
}

public float getVerticalAlignmentMaxInter()
{
	return m_verticalAlignmentMaxInter;
}
public String getVerticalAlignmentType()
{
	return m_verticalAlignmentType;
}
double getWidth()
{
	double dw = 2 * m_owner.getStrokeProperties().getInsideWidth();

	if
		(m_owner.getCoShape() instanceof CoRectangleShapeIF)
	{
		dw += m_leftMargin + m_rightMargin;
	} else {
		dw += 2 * m_topMargin;
	}

	return dw + CoStyledTextMeasurer.getWidth( getStyledDocument(), m_owner.m_baseLineGrid.getBaseLineGeometry( 0 ), this );
}
protected void postMarginChanged()
{
	requestLayout();
}
protected void postTextChanged()
{
	m_cachedStyledDocument = null;
	requestLayout();
}

public void setFirstBaselineOffset( float value )
{
	if ( m_firstBaselineOffset == value ) return;
	
	m_firstBaselineOffset = value;
	
	doAfterFirstBaselinePositionChanged();
}
public void setFirstBaselineType(String value)
{
	if ( m_firstBaselineType == value ) return;
	
	m_firstBaselineType = value;
	
	doAfterFirstBaselinePositionChanged();
}

public void setVerticalAlignmentMaxInter( float value )
{
	if ( m_verticalAlignmentMaxInter == value ) return;
	m_verticalAlignmentMaxInter = value;
	markDirty( "setVerticalAlignmentMaxInter" );
}
public void setVerticalAlignmentType( String value )
{
	if ( m_verticalAlignmentType == value ) return;
	m_verticalAlignmentType = value;
	markDirty( "setVerticalAlignmentType" );
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

public float getBottomMargin()
{
	return m_bottomMargin;
}

public float getLeftMargin()
{
	return m_leftMargin;
}

public float getRightMargin()
{
	return m_rightMargin;
}

public float getTopMargin()
{
	return m_topMargin;
}

public void setBottomMargin( float m )
{
	if ( m_bottomMargin == m ) return;
	
	m_bottomMargin = m;

	doAfterMarginChanged();
}

public void setLeftMargin( float m )
{
	if ( m_leftMargin == m ) return;
	
	m_leftMargin = m;

	doAfterMarginChanged();
}

public void setRightMargin( float m )
{
	if ( m_rightMargin == m ) return;
	
	m_rightMargin = m;

	doAfterMarginChanged();
}

public void setTopMargin( float m )
{
	if ( m_topMargin == m ) return;
	
	m_topMargin = m;

	doAfterMarginChanged();
}













protected void collectState( CoPageItemIF.State s, CoPageItemIF.ViewState viewState )
{
	super.collectState( s, viewState );
	
	CoPageItemAbstractTextContentIF.State S = (CoPageItemAbstractTextContentIF.State) s;

	S.m_textLock = m_textLock;

	CoFormattedText doc = getFormattedText();
	S.m_isNull = ( doc == null );
	
	if
		( S.m_isNull )
	{
		S.m_text = null;
	} else {
		boolean doSendDocument = false;
		if
			( viewState == null )
		{
			doSendDocument = true;
		} else {
			// compare clients timestamp with documents, if same don't send document
			if
				( viewState instanceof CoPageItemAbstractTextContentIF.ViewState )
			{
				if
					( doc.getTimeStamp() == 0 )
				{
					doSendDocument = true;
				} else {
					long timestamp = ( (CoPageItemAbstractTextContentIF.ViewState) viewState ).m_documentTimestamp;
					doSendDocument = ( doc.getTimeStamp() > timestamp );
				}
			} else {
				doSendDocument = true; // view state type clash due to content page item change
			}
		}

		if
			( doSendDocument )
		{
			S.m_text = doc;
		} else {
			S.m_text = null;
		}
	}
	
	S.m_firstBaselineType = getFirstBaselineType();
	S.m_firstBaselineOffset = getFirstBaselineOffset();
	S.m_verticalAlignmentType = getVerticalAlignmentType();
	S.m_verticalAlignmentMaxInter = getVerticalAlignmentMaxInter();

	S.m_columnGrid = m_owner.getColumnGrid();
	S.m_baseLineGrid = m_owner.getBaseLineGrid();

	S.m_columnGridShape = getColumnGridShape();
	S.m_topMargin = getTopMargin();
	S.m_bottomMargin = getBottomMargin();
	S.m_leftMargin = getLeftMargin();
	S.m_rightMargin = getRightMargin();
}

protected final void doAfterFirstBaselinePositionChanged()
{
	postFirstBaselinePositionChanged();
	markDirty( "setFirstBaselinePosition" );
}

protected final void doAfterTextLockChanged()
{
	postTextLockChanged();
	
	markDirty("TextLockChanged");
}

public boolean doesOverflow()
{
	CoImmutableStyledDocumentIF doc = getStyledDocument();
	if ( doc.getLength() == 0 ) return false;
	
	CoBaseLineGeometryIF blg = m_owner.m_baseLineGrid.getBaseLineGeometry( 0 );

	return CoStyledTextMeasurer.doesOverflow( doc, m_owner.m_columnGrid.getColumnGeometry( getColumnGridShape() ), blg, this );
}

private double getHorizontalPadding()
{
	double dw = 2 * m_owner.getStrokeProperties().getInsideWidth();
	
	if
		( m_owner.getCoShape() instanceof CoRectangleShapeIF )
	{
		dw += m_leftMargin + m_rightMargin;
	} else {
		dw += 2 * m_topMargin;
	}

	return dw;
}

protected final CoImmutableStyledDocumentIF getStyledDocument()
{
	if ( m_cachedStyledDocument == null ) m_cachedStyledDocument = getFormattedText().createStyledDocument();;
	return m_cachedStyledDocument;
}

protected int getTextLock()
{
	return m_textLock;
}

public final CoTextStyleApplier getTextStyleApplier()
{
	return m_owner == null ? null : m_owner.getTextStyleApplier();
}

private double getVerticalPadding()
{
	double dh = 0;
	
	if
		( m_owner.getCoShape() instanceof CoRectangleShapeIF )
	{
		dh = m_topMargin + m_bottomMargin;
	} else {
		dh = 2 * m_topMargin;
	}

	if ( ! Double.isNaN( m_firstBaselineOffset ) ) dh += m_firstBaselineOffset;

	return dh;
}

protected void postFirstBaselinePositionChanged()
{
	requestLayout();
}

protected final void postTextLockChanged()
{
}

// apply styles to formatted text

protected void applyStylesToText(CoFormattedText text) {
	CoTextStyleApplier applier = getTextStyleApplier();

	if (applier != null) {
		applier.apply(text);
	}
}

public void setTextLock( int l )
{
	if ( l == m_textLock ) return;
	m_textLock = l;
	doAfterTextLockChanged();
}

public void visit (CoPageItemVisitor visitor, Object anything, boolean goDown)
{
	visitor.doToAbstractTextContent(this, anything, goDown);
}

/*
 * Used at XML export
 * Helena Rankegård 2001-10-23
 */
 
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	super.xmlVisit(visitor);

	visitor.exportAttribute( XML_TOP_MARGIN, Float.toString( m_topMargin ) );
	visitor.exportAttribute( XML_BOTTOM_MARGIN, Float.toString( m_bottomMargin ) );
	visitor.exportAttribute( XML_LEFT_MARGIN, Float.toString( m_leftMargin ) );
	visitor.exportAttribute( XML_RIGHT_MARGIN, Float.toString( m_rightMargin ) );

	visitor.exportAttribute( XML_FIRST_BASELINE, m_firstBaselineType );
	visitor.exportAttribute( XML_FIRST_BASELINE_OFFSET, Float.toString( m_firstBaselineOffset ) );
	visitor.exportAttribute( XML_VERTICAL_ALIGNMENT, m_verticalAlignmentType );
	visitor.exportAttribute( XML_VERTICAL_ALIGNMENT_MAX_PARAGRAPH_SPACING, Float.toString( m_verticalAlignmentMaxInter ) );
	visitor.exportAttribute( XML_LOCK, Integer.toString( m_textLock ) );
}


/*
 * Used at XML import.
 * Helena Rankegård 2001-10-30
 */
 
public void xmlInit( NamedNodeMap map, com.bluebrim.xml.shared.CoXmlContext context )
{
	// xml init
	m_topMargin = CoModelBuilder.getFloatAttrVal( map, XML_TOP_MARGIN, m_topMargin );
	m_bottomMargin = CoModelBuilder.getFloatAttrVal( map, XML_BOTTOM_MARGIN, m_bottomMargin );
	m_leftMargin = CoModelBuilder.getFloatAttrVal( map, XML_LEFT_MARGIN, m_leftMargin );
	m_rightMargin = CoModelBuilder.getFloatAttrVal( map, XML_RIGHT_MARGIN, m_rightMargin );
	
	m_firstBaselineType = CoModelBuilder.getAttrVal( map, XML_FIRST_BASELINE, m_firstBaselineType ); 
	m_firstBaselineOffset = CoModelBuilder.getFloatAttrVal( map, XML_FIRST_BASELINE_OFFSET, m_firstBaselineOffset ); 
	
	m_verticalAlignmentType = CoModelBuilder.getAttrVal( map, XML_VERTICAL_ALIGNMENT, m_verticalAlignmentType );
	m_verticalAlignmentMaxInter = CoModelBuilder.getFloatAttrVal( map, XML_VERTICAL_ALIGNMENT_MAX_PARAGRAPH_SPACING, m_verticalAlignmentMaxInter );
	
	m_textLock = CoModelBuilder.getIntAttrVal( map, XML_LOCK, m_textLock );
}
}