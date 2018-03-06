package com.bluebrim.layout.impl.shared.view;


/**
 * Abstract superclass for page tiem view visitors.
 * This visitor distinguishes layout area views, leaf (content wrapper) views and content views.
 * If any of the methods return false the visiting process is immediately stopped and no further views are visited.
 * 
 * @author: Dennis Malmström
 */
 
public abstract class CoPageItemViewVisitor
{
public boolean visitContentView(CoPageItemContentView content)
{
	return visitView( content );
}


public boolean visitView( CoPageItemView leaf )
{
	return true;
}

public boolean visitCompositePageItemView( CoCompositePageItemView composite )
{
	return visitView( composite );
}

public boolean visitLeafView(CoShapePageItemView leaf)
{
	return visitView( leaf );
}
}