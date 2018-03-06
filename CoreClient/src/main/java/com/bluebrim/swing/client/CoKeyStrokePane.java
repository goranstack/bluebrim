package com.bluebrim.swing.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Creation date: (2000-11-14 17:05:11)
 * @author: Dennis
 */
 
public class CoKeyStrokePane extends CoLabel implements KeyListener, MouseListener, FocusListener
{
	private KeyStroke m_keyStroke;

	public interface KeyStrokeListener extends java.util.EventListener
	{
		void keyStrokeChanged( KeyStroke ks );
	};
public CoKeyStrokePane()
{
	super();
	updateLabel();

	addKeyListener( this );
	addMouseListener( this );
	addFocusListener( this );
}
public void addKeyStrokeListener( KeyStrokeListener l )
{
	listenerList.add( KeyStrokeListener.class, l );
}
protected void fireKeyStrokeChanged()
{
	// Guaranteed to return a non-null array
	Object[] listeners = listenerList.getListenerList();

	// Process the listeners last to first, notifying those that are interested in this event
	for
		( int i = listeners.length - 2; i >= 0; i -= 2 )
	{
		if
			( listeners[i] == KeyStrokeListener.class )
		{
			( (KeyStrokeListener) listeners[ i + 1 ] ).keyStrokeChanged( m_keyStroke );
		}
	}
}
public void focusGained( FocusEvent e )
{
	repaint();
}
public void focusLost( FocusEvent e )
{
	repaint();
}
public KeyStroke getKeyStroke()
{
	return m_keyStroke;
}
public static String getKeyStrokeString( KeyStroke ks )
{
	StringBuffer s = new StringBuffer( " " );

	if
		( ks.getModifiers() != 0 )
	{
		s.append( KeyEvent.getKeyModifiersText( ks.getModifiers() ) );
		s.append( "-" );
	}
	s.append( KeyEvent.getKeyText( ks.getKeyCode() ) );
	s.append( " " );

	return s.toString();
}
public boolean isFocusTraversable()
{
	return true;
}
public void keyPressed( KeyEvent e )
{
	if
		( isEnabled() )
	{
		int c = e.getKeyCode();
		if      ( Character.isLetterOrDigit( (char) c ) )    setKeyStroke( KeyStroke.getKeyStrokeForEvent( e ) );
		else if ( c == e.VK_DELETE || c == e.VK_BACK_SPACE ) setKeyStroke( null );
	}
}
/**
 * keyReleased method comment.
 */
public void keyReleased(java.awt.event.KeyEvent e) {}
/**
 * keyTyped method comment.
 */
public void keyTyped(java.awt.event.KeyEvent e) {}
/**
 * mouseClicked method comment.
 */
public void mouseClicked(java.awt.event.MouseEvent e) {}
/**
 * mouseEntered method comment.
 */
public void mouseEntered(java.awt.event.MouseEvent e) {}
/**
 * mouseExited method comment.
 */
public void mouseExited(java.awt.event.MouseEvent e) {}
public void mousePressed( MouseEvent e )
{
	requestFocus();
}
/**
 * mouseReleased method comment.
 */
public void mouseReleased(java.awt.event.MouseEvent e) {}
protected void paintComponent( Graphics g )
{
	if
		( hasFocus() )
	{
		g.setColor( UIManager.getColor( "Button.focus" ) );
		Insets i = getInsets();
		g.drawRect( i.left, i.top, getWidth() - i.left - i.right - 1, getHeight() - i.top - i.bottom - 1 );
	}
	
	super.paintComponent( g );
}
public void removeKeyStrokeListener( KeyStrokeListener l )
{
	listenerList.remove( KeyStrokeListener.class, l );
}
public void requestFocus()
{
	if ( isEnabled() ) super.requestFocus();
}
public void setKeyStroke( KeyStroke ks )
{
	m_keyStroke = ks;
	updateLabel();
	fireKeyStrokeChanged();
}
protected void updateLabel()
{
	if
		( m_keyStroke != null )
	{
		setText( getKeyStrokeString( m_keyStroke ) );
	} else {
		setText( "          " );
	}
}
}
