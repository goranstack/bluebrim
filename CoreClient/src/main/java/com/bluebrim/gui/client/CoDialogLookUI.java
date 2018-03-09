package com.bluebrim.gui.client;


/**
 *  Sublasserna har en gemensam "look".
 */

 public class CoDialogLookUI extends CoDomainUserInterface {
/**
 * CoDialogLookUI constructor comment.
 */
public CoDialogLookUI() {
	super();
}
/**
 * CoDialogLookUI constructor comment.
 * @param aDomainObject com.bluebrim.base.shared.CoObjectIF
 */
public CoDialogLookUI(com.bluebrim.base.shared.CoObjectIF aDomainObject) {
	super(aDomainObject);
}
public void prepareDialog(CoDialog dialog)
{
	dialog.setTexture("bg146.gif");
	super.prepareDialog(dialog);
}
public void prepareFrame(CoFrame frame)
{
	frame.setTexture("bg146.gif");
	super.prepareFrame(frame);
}
}
