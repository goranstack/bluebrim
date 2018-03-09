package com.bluebrim.browser.client;

import javax.swing.Action;
import javax.swing.JComponent;

import com.bluebrim.browser.shared.CoAddElementData;
import com.bluebrim.browser.shared.CoCatalogElementIF;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoPopupMenu;
import com.bluebrim.transact.shared.CoCommand;

public class CoListCatalogEditor extends CoAbstractCatalogEditor {
	protected CoCatalogUI 	m_catalogUI 				= null;	
	protected Action 		m_addElementAction			= null;
	protected Action		m_removeElementAction		= null;
/**
 * This method was created by a SmartGuide.
 */
public CoListCatalogEditor (CoCatalogUI catalogUI) {
	m_catalogUI = catalogUI;
	m_editMenu 	= createEditMenu(catalogUI.getCatalogListBox().getList());
}
/**
	Svarar med 
 */
protected JComponent createAddItem (CoMenuBuilder menuBuilder, CoPopupMenu menu) {
	m_addElementAction	= new CoAddElementAction(getCatalogUI().getAddItemLabel(),this,null);
	return menuBuilder.addPopupMenuItem( menu, m_addElementAction);
}
protected void createListeners () {
	super.createListeners();
	getCatalogComponent().addMouseListener(createMouseListener());
}
/**
 */
protected JComponent createRemoveItem (CoMenuBuilder menuBuilder, CoPopupMenu menu) {
	m_removeElementAction	= new CoRemoveElementAction(getCatalogUI().getRemoveItemLabel(),this);
	return menuBuilder.addPopupMenuItem(menu, m_removeElementAction);
}
/**
 */
public void enableDisableMenuItems () {
	if (m_addElementAction != null)
		m_addElementAction.setEnabled(getCatalogComponent().isEnabled());
	if (m_removeElementAction != null)
		m_removeElementAction.setEnabled( getCatalogComponent().isEnabled() && getCatalogUI().hasSelectedCatalogElements());
}
/**
 */
public final JComponent getCatalogComponent() {
	return getCatalogUI().getCatalogListBox().getList();
}
/**
 */
public final CoCatalogUI getCatalogUI() {
	return m_catalogUI;
}
/**
 */
public CoMenuBuilder getMenuBuilder () {
	return getCatalogUI().getUIMenuBuilder();
}
public void handleAddElementAction (CoAddElementData elementData) {
	getAddElementCommand(elementData).execute();
}
public void handleRemoveElementAction () {
	getRemoveElementsCommand().execute();
}
	/**
	 * The default command used when adding an element
	 * the catalog.
	 * <br>
	 * The catalog only supports one type of element and
	 * the action is therefore dispatched to the catalog ui.
	 */
	public class AddElementCommand extends CoCommand  {
		
		protected CoCatalogElementIF m_addedElement;
		
		public AddElementCommand(String name)
		{
			super(name);
		}
		public boolean doExecute()
		{
			m_addedElement = getCatalogUI().addElement();
			return (m_addedElement != null);
		}
		public void finish()
		{
			super.finish();
			getCatalogUI().getCatalogHolder().listHasChanged(this);
			getCatalogUI().selectElement(m_addedElement);
		}
	}	/**
	 * The default command used when removing elements
	 * from the catalog.
	 */
	public class RemoveElementsCommand extends CoCommand {
		
		private Object[] m_selectedElements = getCatalogUI().getSelectedCatalogElements();

		public RemoveElementsCommand(String name)
		{
			super(name);
		}
		public void prepare()
		{
			getCatalogUI().clearSelection();
			super.prepare();
		}
		public boolean doExecute()
		{
			getCatalogUI().removeElements(m_selectedElements);
			return true;
		}
		public void finish()
		{
			super.finish();
			getCatalogUI().getCatalogHolder().listHasChanged(this);
			getCatalogUI().postRemoveElements();
		}
	}
/**
 * This is the default behvaior when adding an element
 * the catalog ui, eg only one type of element is permitted
 * and the <code>elementData</code> argument isn't used.
 * <br>
 * In a subclass where it's possible to add several different types
 * of elements this method can be overriden and different behavior
 * exercised dependent on the value of <code>elementData</code>
 */
public CoCommand getAddElementCommand (CoAddElementData elementData) {

	return new AddElementCommand(getCatalogUI().getAddItemLabel());
}
/**
 * This is the default command used when 
 * the remove action is triggered via the 
 * object menu.
 */
protected CoCommand getRemoveElementsCommand () {
	return new RemoveElementsCommand(getCatalogUI().getRemoveItemLabel());
};

}