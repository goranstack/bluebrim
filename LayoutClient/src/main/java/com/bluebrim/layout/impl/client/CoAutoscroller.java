package com.bluebrim.layout.impl.client;

import java.awt.*;

import javax.swing.*;

/**
 * Implementation of layout editor autoscrolling.
 * Works by injecting auto scroll events into the system event que.
 * Autoscroll speed is determined by a delay between each injection.
 *
 * @author: Dennis
 */

public class CoAutoscroller implements CoAutoScrollListener
{
	private JScrollBar m_horizontalScrollbar;
	private JScrollBar m_verticalScrollbar;
	private Component m_eventSource;
	
	private boolean m_isActive = false;

	private Point m_scrollVector;
	private int m_scrollDistance = 1;



	
	private static class WaitThenPost implements Runnable
	{
		CoAutoScrollEvent m_event;
		
		public void run()
		{
			waitBeforePost();
			Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent( m_event );
		}
	};

	private final WaitThenPost m_waitThenPost = new WaitThenPost();

public CoAutoscroller( CoPageItemEditorPanel p, JScrollBar hsb, JScrollBar vsb, int scrollDistance )
{
	m_eventSource = p;
	m_horizontalScrollbar = hsb;
	m_verticalScrollbar = vsb;
	m_scrollDistance = scrollDistance;
}
public void autoScroll( CoAutoScrollEvent ev )
{
	performAutoScroll( ev );
}
public boolean isActive()
{
	return m_isActive;
}
public void performAutoScroll( CoAutoScrollEvent ev )
{
	if
		( m_isActive )
	{
		boolean doRepost = false;
		
		if
			( m_scrollVector.x != 0 )
		{	
			int v = m_horizontalScrollbar.getValue();
			m_horizontalScrollbar.setValue( m_horizontalScrollbar.getValue() + m_scrollDistance * m_scrollVector.x );
			if ( v != m_horizontalScrollbar.getValue() ) doRepost = true;
		}
		
		if
			( m_scrollVector.y != 0 )
		{
			int v = m_verticalScrollbar.getValue();
			m_verticalScrollbar.setValue( m_verticalScrollbar.getValue() + m_scrollDistance * m_scrollVector.y );
			if ( v != m_verticalScrollbar.getValue() ) doRepost = true;
		}
		
		if
			( doRepost )
		{
//			ev = new CoAutoScrollEvent( m_eventSource );
			post( ev );
		} else {
			stop();
		}
	}
}
private void post( CoAutoScrollEvent ev )
{
	m_waitThenPost.m_event = ev;

	EventQueue.invokeLater( m_waitThenPost );
}
public void setScrollVector( Point p )
{
	m_scrollVector = p;
}
public void start()
{
	m_isActive = true;

	CoAutoScrollEvent ev = new CoAutoScrollEvent( m_eventSource );
	post( ev );
}
public void stop()
{
	if
		( m_isActive )
	{
		m_isActive = false;
	}
}

private static void waitBeforePost()
{
	try
	{
		Thread.sleep( 10 ); // ms
	}
	catch ( Throwable ex )
	{
	}

}
}