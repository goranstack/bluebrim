package com.bluebrim.gui.client;

import java.awt.BorderLayout;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import com.bluebrim.menus.client.CoMenuBar;
import com.bluebrim.swing.client.CoAttachmentLayout;
import com.bluebrim.swing.client.CoPanel;

/**
 * A <code>CoSubcanvas</code> is used to display a userinterface model as a 
 * component in another userinterface model. In this way it's possible to have 
 * a composition in the userinterface that maps to the composition in the business object.
 * <br>
 * The panel is taken from the userinterface and added to the subcanvas.
 * A subcanvas is connected to the value model that's responsible for delivering
 * the business object by an instance of <code>CoSubcanvasAdapor</code>.
 * <br>
 * Instance variables
 * <ul>
 * <li>userInterface <code>CoUserInterface</code>
 * <li>transparentWidgets <code>boolean</code> which is sat to true iff the widgets in the userinterface
 * should be transparent
 * <li>userInterfaceConstraint <code>Object</code> the constraints for adding the userinterface. A subcanvas is assumed to 
 * hava a <code>BorderLayout</code> as layout manager and the default value for <code>userInterfaceConstraint</code> is 
 * <code>BorderLayout.CENTER</code>. 
 * </ul>
 * @see CoSubcanvasAdaptor
 * @author Lasse Svadängs 97-10-08
 */
public class CoSubcanvas extends CoPanel {
	private CoUserInterface 	m_userInterface;
	private	boolean				m_transparentWidgets = false;
	private Object				m_userInterfaceConstraint;
/**
 * JSubcanvas constructor comment.
 */
public CoSubcanvas() {
	super();
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
}
/**
 * JSubcanvas constructor comment.
 */
public CoSubcanvas(LayoutManager layoutManager) {
	super(layoutManager);
	m_userInterfaceConstraint = BorderLayout.CENTER;
}
/**
	Adderar 'userInterface' till 'container'. Om gränssnittsmodellen har en menyrad så
	läggs den ut i norr. Metoden förutsätter att 'container' har en BorderLayout.
 */
protected void addUserInterfaceTo( CoUserInterface userInterface, JComponent container) {
	CoMenuBar tMenuBar	= userInterface.getMenuBar();
	if (tMenuBar != null)
	{
		container.add(tMenuBar, BorderLayout.NORTH);
		container.add(userInterface.getPanel(), BorderLayout.CENTER);
	}
	else
		container.add(userInterface.getPanel(), getUserInterfaceConstraint());
	container.revalidate();
}
/**
 * @param aUserInterface SE.corren.calvin.userinterface.CoUserInterface
 */
public CoUserInterface getUserInterface () {
	return m_userInterface;
}
protected Object getUserInterfaceConstraint()
{
	return m_userInterfaceConstraint;
}
/**
 */
protected void removeUserInterface( CoUserInterface aUserInterface) {

	if (aUserInterface != null)
	{
		removeAll();
		repaint();	
	}	
}
public void setAttachmentConstraint()
{
	m_userInterfaceConstraint = new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                          										new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER, 0  ),
	                           										new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0  ),
	                          										new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0  ) );

}
/**
 */
public void setEnabled (boolean state) {
	super.setEnabled(state && (m_userInterface != null ? m_userInterface.canBeEnabled() : false));
	if (m_userInterface != null)
		m_userInterface.setEnabled(state);
}
/**
 */
public void setTransparentWidgets (boolean state) {
	m_transparentWidgets = state;
}
public void setUserInterface ( CoUserInterface userInterface) {
	CoUserInterface oldUserInterface = getUserInterface();
	if (userInterface != oldUserInterface)
	{
		removeUserInterface(oldUserInterface);
		if (userInterface != null)
			userInterface.getUIBuilder().setTransparentWidgets(m_transparentWidgets);
		basicSetUserInterface(userInterface);
		m_userInterface = userInterface;
	}
	repaint();
}
public void setUserInterfaceConstraint(Object constraint)
{
	m_userInterfaceConstraint = constraint;
}


/**
 */
protected void basicSetUserInterface( CoUserInterface userInterface) {

	if (userInterface != null)
	{
		userInterface.buildForComponent();
		addUserInterfaceTo(userInterface, this);
	}	
	revalidate();
}
}