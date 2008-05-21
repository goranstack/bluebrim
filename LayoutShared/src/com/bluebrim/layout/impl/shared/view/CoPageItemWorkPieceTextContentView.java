package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.util.List;

import com.bluebrim.layout.impl.shared.*;

/**
 * View for CoPageItemWorkPieceTextContent.
 * 
 * @author: Dennis Malmström
 */

public class CoPageItemWorkPieceTextContentView extends CoPageItemAbstractTextContentView implements CoImmutableOrderTaggedIF
{
	// page item state cache	
	private int m_textTag;
	private List m_acceptedTags;
	private com.bluebrim.content.shared.CoWorkPieceIF m_workPiece;

public CoPageItemViewRenderer createRenderer( CoPageItemViewRendererFactory f )
{
	return f.create( this );
}
public List getAcceptedTags()
{
	return m_acceptedTags;
}
private String getDummyText()
{
	if
		( m_textTag >= 0 )
	{
		return "[" + m_textTag + "]"; // paint order tag
	} else {
		return null;
	}
}
public int getOrderTag()
{
	return m_textTag;
}
public com.bluebrim.content.shared.CoWorkPieceIF getWorkPiece()
{
	return m_workPiece;
}

private void sync( CoPageItemWorkPieceTextContentIF.State d )
{
	m_textTag = d.m_textTag;
	m_acceptedTags = d.m_acceptedTags;
	m_workPiece = d.m_workPiece;

	m_documentView.setDummyText( getDummyText() );
}


protected boolean isAcceptingWorkPiece()
{
	return m_textTag != -1;
}

protected boolean isAttachedToWorkPiece()
{
	return getWorkPiece() != null;
}

protected boolean isProjectingContent()
{
	return m_formattedText != null && m_formattedText.getLength() > 0;
}

protected void paintContentIcon( Graphics2D g )
{
	Font f = g.getFont();
	g.setFont( CoPageItemViewClientUtilities.m_iconFont );
	g.drawString( "A", CoPageItemViewClientUtilities.m_iconWidth / 2 - CoPageItemViewClientUtilities.m_iconFont.getSize2D() / 4, CoPageItemViewClientUtilities.m_iconHeight / 2 + CoPageItemViewClientUtilities.m_iconFont.getSize2D() / 3 );
	g.setFont( f );
}

	private final static long serialVersionUID = 1168333838763763095L;

public CoPageItemWorkPieceTextContentView( CoContentWrapperPageItemView owner, CoPageItemIF pageItem, CoPageItemWorkPieceTextContentIF.State d )
{
	super( owner, pageItem, d );

	sync( d );
}

public CoPageItemWorkPieceTextContentIF getPageItemWorkPieceTextContent()
{
	return (CoPageItemWorkPieceTextContentIF) getPageItem();
}

public void modelChanged( CoPageItemIF.State d, CoPageItemView.Event ev )
{
	super.modelChanged( d, ev );
	sync( (CoPageItemWorkPieceTextContentIF.State) d );
}
}