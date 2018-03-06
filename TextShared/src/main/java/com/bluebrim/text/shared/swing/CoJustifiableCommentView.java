package com.bluebrim.text.shared.swing;

import java.awt.*;

import javax.swing.text.*;

import com.bluebrim.base.shared.*;

/**
 * Subclass of CoCommentView that implements all the interfaces nesseccarry for use in the styled text editor.
 * 
 * @author: Dennis Malmström
 */

public class CoJustifiableCommentView extends CoCommentView implements CoPaintableView, CoJustifiableView, com.bluebrim.text.shared.swing.CoHighlightableView, CoBreakableView, com.bluebrim.text.shared.swing.CoReuseableView
{
public CoJustifiableCommentView( Element elem )
{
	super( elem );
}
public void clearSpacePadding()
{
}
public float getAscent()
{
	return 0;
}
public float getDescent()
{
	return 0;
}
public float getFontSize()
{
	return 0;
}
public float getHyphenWidth()
{
	return 0;
}


public void paint(Graphics2D g, Shape a, boolean broken )
{
	paint( g, a );
}
public void paintSelectionShadow( Graphics2D g, int from, int to, Shape allocation )
{
}
public void release()
{
}


public float getMinimalPartialSpan( float x0, int p0, int p1, float minimumRelativeSpaceWidth )
{
	return getPreferredSpan( X_AXIS );
}

public float getMinimalSpan( float x0, float minimumRelativeSpaceWidth )
{
	return getMinimalPartialSpan( x0, getStartOffset(), getEndOffset(), minimumRelativeSpaceWidth );
}

public float getSpacePadding()
{
	return 0;
}

public void paint( CoPaintable g, Shape a )
{
	paint( g, a, false );
}

public void paint( CoPaintable g, Shape a, boolean broken )
{
	if ( g.getGraphics2D() != null ) paint( g.getGraphics2D(), a, broken );
}

public float setSpacePadding( float p, float minimumRelativeSpaceWidth )
{
	return 0;
}
}