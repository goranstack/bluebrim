package com.bluebrim.text.impl.client;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.text.shared.swing.*;

/**
 * Creation date: (2000-05-22 15:14:24)
 * @author: Dennis
 */
 
public class CoTextMeasurementPane extends CoPanel
{
	private CoLabel m_label;

	private CoImmutableStyledDocumentIF m_doc;
	



	
/*
	private com.bluebrim.text.shared.CoStyledDocumentIF m_measuredDoc = new com.bluebrim.text.shared.CoStyledDocument();
	private List m_destDocs = new ArrayList();
	{
		m_destDocs.add( m_measuredDoc );
	}
*/
	private double m_columnWidth;



	
//	private static final com.bluebrim.text.shared.CoDocumentDistributer m_distributer = new com.bluebrim.text.shared.CoDocumentDistributer();
	
	private static final com.bluebrim.text.shared.CoBaseLineGeometryIF m_baseLineGeometry =
		new com.bluebrim.text.shared.CoBaseLineGeometryIF()
		{
			public float getDeltaY() { return 0; }
			public float getY0() { return 0; }
			public boolean isEquivalentTo( com.bluebrim.text.shared.CoBaseLineGeometryIF g ) { return false; }
		};
	
	private static final com.bluebrim.text.shared.CoTextGeometryIF m_textGeometry = 
		new com.bluebrim.text.shared.CoTextGeometryIF()
		{
			public float getFirstBaselineOffset() { return 0; }
			public String getFirstBaselineType() { return com.bluebrim.text.shared.CoTextGeometryIF.BASELINE_ASCENT; }
			public float getVerticalAlignmentMaxInter() { return 0; }
			public String getVerticalAlignmentType() { return com.bluebrim.text.shared.CoTextGeometryIF.ALIGN_TOP; }
		};
	

public CoTextMeasurementPane( CoUserInterfaceBuilder b )
{
	super( new CoRowLayout() );

	b.preparePanel( this );
	setBorder( BorderFactory.createEtchedBorder() );

	add( m_label = b.createLabel( " " ) );

	setDocument( null );
}
public void measure()
{
	if ( m_doc == null ) return;
/*
	com.bluebrim.text.shared.CoImmutableStyledDocumentIF doc = null;
	List tags = m_measuredDoc.getAcceptedTags();
	if
		( ( tags == null ) || ( tags.isEmpty() ) )
	{
		doc = m_doc;
	} else {
		m_measuredDoc.clear();
		m_distributer.distribute( m_doc, m_destDocs );
		doc = m_measuredDoc;
	}
	*/
	double h = CoStyledTextMeasurer.getHeight( m_doc, (float) m_columnWidth, m_baseLineGeometry, m_textGeometry );

	int charCount = m_doc.getLength();

	int wordCount = m_doc.getWordCount();

	h = CoLengthUnit.MM.to( h );
	m_label.setText( CoLengthUnitSet.format( h ) + " mm  " + charCount + " tecken  " + wordCount + " ord" );
}
public void setColumnWidth( double columnWidth )
{
	m_columnWidth = columnWidth;
}
public void setDocument( com.bluebrim.text.shared.CoImmutableStyledDocumentIF doc )
{
	if ( m_doc == doc ) return;
	
	m_doc = doc;
	
	if
		( m_doc == null )
	{
		m_label.setText( "" );
//	} else {
//		m_measuredDoc = m_doc.getCopy();//new com.bluebrim.text.shared.CoStyledDocument( m_doc );
//		m_destDocs.clear();
//		m_destDocs.add( m_measuredDoc );
	}
}

}