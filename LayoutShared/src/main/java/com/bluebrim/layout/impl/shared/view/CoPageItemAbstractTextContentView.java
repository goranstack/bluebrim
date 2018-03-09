package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.text.shared.*;

/**
 * View for CoPageItemAbstractTextContent.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoPageItemAbstractTextContentView extends CoPageItemContentView implements CoTextGeometryIF, CoStyledDocumentView.ContainerHolder
{
	protected final CoStyledDocumentView m_documentView = new CoStyledDocumentView();
	
	protected CoImmutableColumnGridIF m_columnGrid;
	protected CoImmutableBaseLineGridIF m_baseLineGrid;
	protected CoBaseLineGeometryIF m_baseLineGeometry;
	protected CoImmutableShapeIF m_clipping;
	protected String m_firstBaselineType;
	protected float m_firstBaselineOffset = Float.NaN;
	protected String m_verticalAlignmentType;
	protected float m_verticalAlignmentMaxInter = Float.NaN;

	// text editor editing this text
	protected transient CoTextEditorIF m_activeTextEditor;

	// page item rendering stuff
	protected transient CoColumnGridRenderer m_columnGridRenderer;

	protected transient CoImmutableStyledDocumentIF m_cachedDocument;
	// page item state cache
	protected CoFormattedText m_formattedText;
	protected boolean m_isTextLocked;
	private int m_textLock;
	protected long m_textTimestamp;
	private final static long serialVersionUID = -6281179899331491329L;



// propagate to document view

public void dispose()
{
	super.dispose();

	m_documentView.dispose();
}
public CoImmutableBaseLineGridIF getBaseLineGrid()
{
	return m_baseLineGrid;
}
public CoImmutableColumnGridIF getColumnGrid()
{
	return m_columnGrid;
}
public Cursor getContentCursor()
{
	if
		( isTextLocked() )
	{
		return CoPageItemViewClientUtilities.LOCK_CURSOR;
	} else {
		return Cursor.getPredefinedCursor( Cursor.TEXT_CURSOR );
	}
}
public CoImmutableStyledDocumentIF getDocument()
{
	if
		( m_cachedDocument == null )
	{
		m_cachedDocument = m_formattedText.createStyledDocument();
	}
	
	return m_cachedDocument;
}
public float getFirstBaselineOffset()
{
	return m_firstBaselineOffset;
}
public String getFirstBaselineType()
{
	return m_firstBaselineType;
}



public float getVerticalAlignmentMaxInter()
{
	return m_verticalAlignmentMaxInter;
}
public String getVerticalAlignmentType()
{
	return m_verticalAlignmentType;
}



protected void prepare( CoPageItemIF.ViewState s )
{
	super.prepare( s );
	
	CoImmutableStyledDocumentIF doc = getDocument();

	// send timestamp of cached document (if its the same as the timestamp of the models document then the document need not be fetched)
	if
		( doc != null )
	{
		( (CoPageItemAbstractTextContentIF.ViewState) s ).m_documentTimestamp = m_textTimestamp;
	} else {
		( (CoPageItemAbstractTextContentIF.ViewState) s ).m_documentTimestamp = 0;
	}
}
// Start a text editor on the document of this view
// This method must be called in a transaction

public boolean startEdit(CoTextEditorIF editor, boolean isOnWorkspace) {
	CoPageItemAbstractTextContentIF tc = getPageItemAbstractTextContent();

		// prepare text editor
		editor.setExpandOnOverflow(
			((CoPageItemAbstractTextContentIF) m_pageItem).getOwner().getHeightSpec().getFactoryKey().equals(
				CoContentHeightSpecIF.CONTENT_HEIGHT_SPEC));
		editor.setCoStyledDocument(tc.getMutableFormattedText().createMutableStyledDocument());
		editor.setGeometry(m_documentView.getColumnGeometry(), m_documentView.getBaseLineGeometry(), this);

		if (isOnWorkspace) {
			// prepare "in workspace" text editor geometry and open it
			updateTextEditorBounds(editor);
			editor.unsetDirty();
			editor.getComponent().setVisible(true);
			m_activeTextEditor = editor;
		}
		return true;

}
// stop the active text editor
// This method must be called in a transaction

public void stopEdit( CoTextEditorIF editor )
{
	CoPageItemAbstractTextContentIF tc = getPageItemAbstractTextContent();

	// close it
	if
		( m_activeTextEditor != null )
	{
		editor.getComponent().setVisible( false );
	}
	
	m_activeTextEditor = null;

	// commit document changes
	if ( editor.isDirty() ) tc.updateFormattedText( new CoFormattedText( editor.getCoStyledDocument() ) );


	// don't wait for model change notification, sync this view now (otherwise it will first display the old text then update to the new one)
	if ( editor.isDirty() ) sync( (CoPageItemAbstractTextContentIF.State) m_pageItem.getState( null ) );
}
private void sync( CoPageItemAbstractTextContentIF.State d )
{
	boolean doUpdateDoc = false;

	if ( ( m_firstBaselineType != d.m_firstBaselineType ) && ( m_firstBaselineType != null ) && ! m_firstBaselineType.equals( d.m_firstBaselineType ) ) doUpdateDoc = true;
	if ( ( m_verticalAlignmentType != d.m_verticalAlignmentType ) && ( m_verticalAlignmentType != null ) && ! m_verticalAlignmentType.equals( d.m_verticalAlignmentType ) ) doUpdateDoc = true;
	if ( m_firstBaselineOffset != d.m_firstBaselineOffset ) doUpdateDoc = true;
	if ( m_verticalAlignmentMaxInter != d.m_verticalAlignmentMaxInter ) doUpdateDoc = true;
	
	m_firstBaselineType = d.m_firstBaselineType;
	m_firstBaselineOffset = (float) d.m_firstBaselineOffset;
	m_verticalAlignmentType = d.m_verticalAlignmentType;
	m_verticalAlignmentMaxInter = (float) d.m_verticalAlignmentMaxInter;
	m_topMargin = d.m_topMargin;
	m_bottomMargin = d.m_bottomMargin;
	m_leftMargin = d.m_leftMargin;
	m_rightMargin = d.m_rightMargin;

	m_textLock = d.m_textLock;
	
	if
		( d.m_isNull )
	{
		doUpdateDoc = ( m_formattedText != null );
		m_formattedText = null;
		m_cachedDocument = null;
	} else if
		( d.m_text != null )
	{
		doUpdateDoc = true;
		m_formattedText = d.m_text;
		m_cachedDocument = null;
		m_textTimestamp = d.m_text.getTimeStamp();
	}

	boolean textViewWasUpdated = syncGrids( d );
	if
		( doUpdateDoc && ! textViewWasUpdated )
	{
		m_documentView.setDocument( getDocument() );
	}
}
private boolean syncGrids( CoPageItemAbstractTextContentIF.State d )
{
	m_baseLineGrid = d.m_baseLineGrid;
	m_baseLineGeometry = m_baseLineGrid.getBaseLineGeometry( 0 );

	
	m_columnGrid = d.m_columnGrid;
	m_columnGridRenderer = null;

		
	
	boolean doLayoutText = false;
	
	if ( ( m_baseLineGeometry == null ) || ( ! m_baseLineGeometry.isEquivalentTo( m_baseLineGeometry ) ) ) doLayoutText = true; // base line grid geomety changed -> force document view update

	CoColumnGeometryIF cg = m_columnGrid.getColumnGeometry( d.m_columnGridShape );
	if ( ( m_documentView.getColumnGeometry() == null ) || ! m_documentView.getColumnGeometry().isEquivalentTo( cg ) ) doLayoutText = true; // column grid geomety changed -> force document view update
	
	if
		( doLayoutText )
	{
		// grid geoemtry changed -> update document view
		Rectangle2D r = d.m_columnGridShape.getBounds2D();
		if
			( ( cg == null ) || ( cg.getColumnCount() == 0 ) )
		{
			m_documentView.set( getDocument(), r.getWidth(), r.getHeight(), null, m_baseLineGeometry, this );
		} else {
			m_documentView.set( getDocument(), r.getWidth(), r.getHeight(), cg, m_baseLineGeometry, this );
		}
	}

	return doLayoutText; // document view was updated
}
public void updateAbsoluteGeometryCache( double scale )
{
	super.updateAbsoluteGeometryCache( scale );

	// propagate to active text editor
	if
		( m_activeTextEditor != null )
	{
		updateTextEditorBounds( m_activeTextEditor );
	}
}
private void updateTextEditorBounds( CoTextEditorIF editor )
{
	Point2D p0 = new Point2D.Double( 0, 0 );
	Point2D p1 = new Point2D.Double( m_owner.m_shape.getWidth(), m_owner.m_shape.getHeight() );
	m_owner.transformToGlobal( p0 );
	m_owner.transformToGlobal( p1 );
	
	double s = getScreenScale();
	editor.getComponent().setSize( (int) ( s * m_owner.m_shape.getWidth() ), (int) ( s * m_owner.m_shape.getHeight() ) );
	editor.getComponent().setLocation( (int) ( s * p0.getX() ), (int) ( s * p0.getY() ) );

	double dw = Math.abs( p1.getX() - p0.getX() - m_owner.m_shape.getWidth() );
	if
		( dw > 0.5 )
	{
		( (JComponent) editor.getComponent() ).setOpaque( true );
		Color bg = Color.white;
		if ( m_owner.m_fillPaint instanceof Color ) bg = (Color) m_owner.m_fillPaint;
		editor.getComponent().setBackground( bg );
	} else {
		( (JComponent) editor.getComponent() ).setOpaque( false );
	}

}

	protected float m_bottomMargin = Float.NaN;
	protected float m_leftMargin = Float.NaN;
	protected float m_rightMargin = Float.NaN;
	protected float m_topMargin = Float.NaN;


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


protected CoPageItemIF.ViewState getViewState()
{
	return CoPageItemViewClientUtilities.m_pageItemAbstractTextContentViewState;
}


protected void paintContentIcon( Graphics2D g )
{
	Font f = g.getFont();
	g.setFont( CoPageItemViewClientUtilities.m_iconFont );
	g.drawString( "T", CoPageItemViewClientUtilities.m_iconWidth / 2 - CoPageItemViewClientUtilities.m_iconFont.getSize2D() / 4, CoPageItemViewClientUtilities.m_iconHeight / 2 + CoPageItemViewClientUtilities.m_iconFont.getSize2D() / 3 );
	g.setFont( f );
}


public CoPageItemAbstractTextContentView( CoContentWrapperPageItemView owner, CoPageItemIF pageItem, CoPageItemAbstractTextContentIF.State d )
{
	super( owner, pageItem, d );

	sync( d );
}

public CoFormattedText getFormattedText()
{
	return m_formattedText;
}

public CoPageItemAbstractTextContentIF getPageItemAbstractTextContent()
{
	return (CoPageItemAbstractTextContentIF) getPageItem();
}

public int getTextLock()
{
	return m_textLock;
}

public boolean isTextLocked()
{
	return m_isTextLocked;
}

public void modelChanged( CoPageItemIF.State d, CoPageItemView.Event ev )
{
	super.modelChanged( d, ev );
	sync( (CoPageItemAbstractTextContentIF.State) d );
}

protected void prepareForClient()
{
	m_documentView.setContainerHolder( this );
	m_documentView.setDocument( getDocument() ); // m_documentView looses document during serialization
}
}