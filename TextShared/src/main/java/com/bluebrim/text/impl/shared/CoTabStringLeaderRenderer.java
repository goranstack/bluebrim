package com.bluebrim.text.impl.shared;

import com.bluebrim.text.shared.*;


/**
 * Tab stop leader renderer for tab stops with string leader.
 * 
 * @author: Dennis Malmström
 */

public class CoTabStringLeaderRenderer extends CoAbstractTabLeaderRenderer
{




	private char m_leaderChar;

public CoTabStringLeaderRenderer( char c )
{
	m_leaderChar = c;
}

public void paint( com.bluebrim.base.shared.CoPaintable g, CoGlyphVector gv, float y, float x0, float x1, float tracking )
{
	if ( m_leaderChar == '\0' ) return;

	float a = gv.getTabStringWidth( m_leaderChar );
	int count = (int) ( ( x1 - x0 - a ) / ( a + tracking ) );

	float dx = ( x1 - x0 ) - count * ( a + tracking ) - a;
	x0 += dx;
	while
		( count-- >= 0 )
	{
		g.drawChar( m_leaderChar, x0, y );
		x0 += a + tracking;
	}
}

public void setString( char c )
{
	m_leaderChar = c;
}
}