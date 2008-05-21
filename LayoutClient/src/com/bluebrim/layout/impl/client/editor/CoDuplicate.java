package com.bluebrim.layout.impl.client.editor;


/**
 * Layout editor operation: Duplicate the selected page items.
 * 
 * @author: Dennis
 */
 
public class CoDuplicate extends CoDuplicateAndRepeat
{
public CoDuplicate( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoDuplicate( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	doDuplicate( 1, 20, 20 );
}
}