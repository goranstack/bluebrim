package com.bluebrim.base.shared;

/**
	Interface for classes that can be visually represented
	by an icon and a name.
 */
public interface CoVisualizableIF {
/**
 	Returns the name of the icon. This must be relative the path name
 	for the icon resource anchor class.
 */
public String getIconName ( );
/**
	Returns the class name for the class that serves as
	an resource anchor for the icon name.
	I.e the icon name must be relative the pathname for
	this class.
*/
public String getIconResourceAnchor();
/**
 * This method was created by a SmartGuide.
 * @return javax.swing.ImageIcon
 */
public String getIdentity ( );
}
