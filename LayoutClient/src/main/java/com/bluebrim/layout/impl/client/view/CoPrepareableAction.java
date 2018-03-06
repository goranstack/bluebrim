package com.bluebrim.layout.impl.client.view;

import javax.swing.*;

import com.bluebrim.layout.impl.shared.view.*;

/**
 * Subclass of AbstractAction.
 * The indented use is in the object menus of page items views.
 * Before menu is posted, each instance of this class has its prepare method called with the selected view as argument.
 * The action is expected to make itself disabled if it isn't applicable on the selected view.
 * 
 * @author: Dennis
 */
 
public abstract class CoPrepareableAction extends AbstractAction implements CoPrepareable
{
/**
 * CoPrepareableAction constructor comment.
 */
public CoPrepareableAction() {
	super();
}
/**
 * CoPrepareableAction constructor comment.
 * @param name java.lang.String
 */
public CoPrepareableAction(String name) {
	super(name);
}
/**
 * CoPrepareableAction constructor comment.
 * @param name java.lang.String
 * @param icon javax.swing.Icon
 */
public CoPrepareableAction(String name, Icon icon) {
	super(name, icon);
}
// convenience method for checking the class of a view

protected final boolean check( CoPageItemView v, Class c )
{
	if
		( c.isInstance( v ) )
	{
		return true;
	} else {
		setEnabled( false );
		return false;
	}
}


public void prepare( CoShapePageItemView v )
{
	setEnabled( true );
}
}