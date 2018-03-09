package com.bluebrim.layout.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.transact.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * This is an attempt to create an interface that can replace 
 * <code>CoShapePageItemIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * layout domain. 
 * <br>
 * The working strategi is to replace all external references to code>CoShapePageItemIF</code>
 * with this interface and add requested methods until everyone is happy.  
 * <br>
 * Perhaps we end up with several interfaces. 
 * Some of the references to <code>CoShapePageItemIF</code>
 * is already replaced with the <code>CoInsertionRequestProjectorIF</code> interface. 
 * 
 * @author: Göran Stäck 2002-04-03
 */

public interface CoLayout extends CoXmlExportEnabledIF {
	
	public CoView getView();

	public double getWidth();

	public double getHeight();

	/**
	 * Owners of <code>CoLayout</code>'s must call this method before
	 * deleting a reference to a <code>CoLayout</code>. The reason is
	 * that other objects that refer the same instance must be
	 * informed when the owner deletes the instance.
	 */
	public void destroy();
	
	public CoRef getId();

	public CoLayout copy();

	/**
	 * Apply the specified size on the layout and sets the layout in a state
	 * that prevents change of size. An example of usage is when the advertising
	 * domain creates a layout for an ad. 
	 */ 
	public void setFixedSize( CoDimension2D size );
	
	public void setLayoutParameters( CoLayoutParameters parameters );
	
	public void addLayoutChangeListener(CoLayoutChangeListener listener);

	public void removeLayoutChangeListener(CoLayoutChangeListener listener);

	/**
	 * The layout engine is deactivated as default to avoid heavy layouting when
	 * constructing a layout structure. Since the layout elements don't know
	 * when the constructing is finished its neseccary to activate the layout
	 * engine from the outside.
	 */	
	public void activateLayoutEngine();
	
}
