package com.bluebrim.text.impl.client;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

//

public class CoAtomAwareCaret extends DefaultCaret
{
	private boolean m_isDragging = false;
	private int m_oldDot;
	public CoAtomAwareCaret()
	{
		super();
		setBlinkRate( 500 );
	}
	private int adjustDot( int dot, int old, boolean moving )
	{
		m_oldDot = dot;
		
		com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF)getComponent().getDocument();

		Element e = doc.getCharacterElement( dot );
		if
				( doc.isAtomic( e ) )
		{
			int start = e.getStartOffset();
			int end = e.getEndOffset();
			
			boolean onFirstHalf = ( ( dot - start ) < ( end - dot ) );

			if
				( moving )
			{
				if      ( ! m_isDragging && ( dot == start + 1 ) ) dot = end;
				else if ( ! m_isDragging  && ( dot == end - 1 ) )   dot = start;
				else if ( onFirstHalf ) dot = start;
				else dot = end;
			} else {
				if      ( ( old == start ) && ( dot == start + 1 ) ) dot = end;
				else if ( ( old == end ) && ( dot == end - 1 ) ) dot = start;
				else if ( onFirstHalf ) dot = start;
				else dot = end;
			}
		}
		return dot;
	}
protected synchronized void damage(java.awt.Rectangle r)
{
	super.damage( r );

	// make sure selection shadows are properly drawn when scale != 1.0
	if
		( component != null )
	{
		component.repaint();
	}
}
	public void mouseClicked(MouseEvent e)
	{
		if
			( ( e.getModifiers() & InputEvent.BUTTON1_MASK ) != 0 )
		{
			if
				( e.getClickCount() == 2 )
			{
				com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF)getComponent().getDocument();
				Element elem = doc.getCharacterElement( m_oldDot );
				if
						( doc.isAtomic( elem ) )
				{
					setDot( elem.getStartOffset() );
					moveDot( elem.getEndOffset() );
					return;
				}
			}
		}
		super.mouseClicked( e );
	}
	public void mouseDragged( MouseEvent e )
	{
		m_isDragging = true;
		super.mouseDragged( e );
	}
	// can select text with the shift-button
	public void mousePressed(MouseEvent e)
	{
		if
			( SwingUtilities.isLeftMouseButton( e ) )
		{
			if
				( e.isShiftDown() )
			{
				moveCaret( e );
			} else {
		  	positionCaret( e );
			}
		  if
		  	( getComponent().isEnabled() )
		  {
				getComponent().requestFocus();
		  }
		}
	}
	public void mouseReleased( MouseEvent e )
	{
		m_isDragging = false;
		super.mouseReleased( e );
	}
	protected void moveDot( int dot, Position.Bias dotBias )
	{
		super.moveDot( adjustDot( dot, getDot(), true ), dotBias );
	}
	protected void setDot( int dot, Position.Bias dotBias )
	{
		super.setDot( adjustDot( dot, getDot(), false ), dotBias );
	}
}
