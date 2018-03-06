package com.bluebrim.browser.client;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;

import com.bluebrim.base.shared.CoFactoryManager;
import com.bluebrim.browser.shared.CoAddElementData;
import com.bluebrim.browser.shared.CoTreeCatalogElementFactoryIF;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.menus.client.CoMenu;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoPopupMenu;
import com.bluebrim.menus.client.CoSubMenu;

/**
	A subclass to <code>CoAbstractCatalogEditor</code> that is responsible
	for editing the contents of a tree catalog, i e a tree where the user can
	add or remove elements via a popup menu.
	<br>
	The actions used are held in a Hashtable so they can be easily accessed when 
	the menu items are to be anabled or disabled. The command classes used are 
	defined as inner classes.
  */
public abstract class CoTreeCatalogEditor extends CoAbstractTreeCatalogEditor
{
	CoSubMenu m_addElementMenu 			= null;
	
	public class AddElementCommand extends AbstractAddElementCommand
	{
		protected CoAddElementData 			m_elementData;
		
		public AddElementCommand(CoTreeCatalogUI treeCatalogUI, CoAddElementData elementData)
		{
			super(treeCatalogUI);
			m_elementData = elementData;
		}
		public CoTreeCatalogElementIF addElement()
		{
			CoTreeCatalogElementIF 			selectedElement 	= m_treeCatalogUI.getSingleSelectedTreeElement();
			CoTreeCatalogElementFactoryIF 	parentFactory 		= (CoTreeCatalogElementFactoryIF) CoFactoryManager.getFactory(selectedElement);
			return (CoTreeCatalogElementIF) parentFactory.addElement(selectedElement, m_elementData);
		}
	}

	 public class RemoveElementsCommand extends AbstractRemoveElementsCommand{
		
		public RemoveElementsCommand(CoTreeCatalogUI treeCatalogUI)
		{
			super(treeCatalogUI);
		}	
		protected void doRemoveSelectedElementFrom(CoTreeCatalogElementIF element, CoTreeCatalogElementIF parent) {
			CoTreeCatalogElementFactoryIF elementFactory 	= (CoTreeCatalogElementFactoryIF )CoFactoryManager.getFactory(element);
			elementFactory.removeElementFrom(parent, element);
		}
	}
/**
 */
public CoTreeCatalogEditor (CoTreeCatalogUI treeCatalogUI) {
	super(treeCatalogUI);
}
/**
 */
protected JComponent createAddItem (CoMenuBuilder menuBuilder, CoPopupMenu menu) {
	m_addElementMenu = menuBuilder.addPopupSubMenu( menu, getTreeCatalogUI().getAddElementItemLabel());
	return m_addElementMenu;
}
/**
 */
protected void createListeners() {
	super.createListeners();
	((JTree )getCatalogComponent()).addTreeSelectionListener(createTreeSelectionListener());
}
public  AbstractRemoveElementsCommand createRemoveElementsCommand()
{
	return new RemoveElementsCommand(getTreeCatalogUI());
}
/**
 */
protected abstract TreeSelectionListener createTreeSelectionListener();
/**
	Menyvalen skall enablas/disablas. 
 */
protected abstract void enableDisableMenuItems();
/**
 */
protected final CoMenu getAddElementMenu() {
	return m_addElementMenu;
}
/**
 */
protected JComponent getCatalogComponent() {
	return getTreeCatalogUI().getTreeComponent();
}
/**
 */
public CoMenuBuilder getMenuBuilder () {
	return getTreeCatalogUI().getUIMenuBuilder();
}
/**
	Användaren har gjort ett menyval från "Lägg till"-menyn.
	Via menyvalets instans av CoAddElementAction så anropas denna metod 
	där argumentet bl a innehåller  elementtyp för det element som skall läggas till.
 */
public void handleAddElementAction(CoAddElementData elementData) {
	AddElementCommand tCommand = new AddElementCommand(getTreeCatalogUI(), elementData);
	tCommand.execute();
}
/**
 */
public void handleRemoveElementAction() {
	RemoveElementsCommand tCommand = new RemoveElementsCommand(getTreeCatalogUI());
	tCommand.execute();
}
}
