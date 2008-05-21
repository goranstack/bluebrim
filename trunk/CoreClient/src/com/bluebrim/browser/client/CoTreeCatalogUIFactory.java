package com.bluebrim.browser.client;

import javax.swing.Action;

import com.bluebrim.browser.shared.CoAddElementData;
import com.bluebrim.browser.shared.CoTreeCatalogElementFactoryIF;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.gui.client.CoUIFactoryManager;
import com.bluebrim.gui.client.CoUserInterfaceFactory;
import com.bluebrim.menus.client.CoMenu;
import com.bluebrim.resource.shared.CoResourceLoader;
/**
 	En abstrakt superklass till de factoryklasser som hanterar gr�nssnitt f�r
 	verksamhetsobjekt som implementerar CoTreeCatalogElement. <br>
 	En konkret subklass av CoTreeCatalogUIFactory kan svara p� 
 	hur motsvarande verksamhetsobjekt skall visas upp i en meny mm.<br> 
 */
public abstract class CoTreeCatalogUIFactory extends CoUserInterfaceFactory implements CoTreeCatalogUIFactoryIF {

public void buildAddElementMenuFor (CoTreeCatalogElementIF catalogElement, CoAbstractTreeCatalogEditor editor, CoMenu menu ) 
{
	doBuildAddElementMenuFor(catalogElement, getElementFactory().getAllowedElements(),editor,menu );
}
/**
 Adderar ett menyval f�r sin elementklass till "L�gg till" menyn 'aMenu'.
 */
public Action createAddElementAction(CoAbstractTreeCatalogEditor editor) {
	CoTreeCatalogElementFactoryIF tElementFactory	=  getElementFactory();
	return new CoAddElementAction(	tElementFactory.getElementType().toLowerCase(),
										 	CoResourceLoader.loadIcon(tElementFactory.getIconResourceAnchor(), tElementFactory.getElementIconName()),
											editor,
											new CoAddElementData(tElementFactory.getElementKey()));
}
/**
 */
protected void doBuildAddElementMenuFor (CoTreeCatalogElementIF element, String tAllowedChildrenKeys[],CoAbstractTreeCatalogEditor editor,CoMenu menu ) {
	
	if (tAllowedChildrenKeys == null)
		return;
	int nmbrOfItems	= tAllowedChildrenKeys.length - 1;
	for (int i = 0; i<=nmbrOfItems; i++)
	{
		String tFactoryKey 							= tAllowedChildrenKeys[i];
		CoTreeCatalogUIFactoryIF tFactory 			= (CoTreeCatalogUIFactoryIF )CoUIFactoryManager.getFactory(tFactoryKey);
		Action tMenuAction 							= editor.getMenuAction(tFactoryKey);
		if (tMenuAction == null)
		{	
			tMenuAction = tFactory.createAddElementAction(editor);
			editor.putMenuAction(tFactoryKey,tMenuAction);
		}	
		editor.getMenuBuilder().addMenuItem(menu,tMenuAction);
	}	
}
/**
	Defualtbeteende f�r att enabla/disabla "L�gg till" menyn i tr�dvyn. I praktiken kr�vs en mera 
	sofistikerad analys d�r man m�ste titta p� varje enskilt menyval och g�ra en bed�mning om det 
	skall enablas eller disablas. Se t ex CoPublicationElementUIFactory 
	@see CoPublicationElementUIFactory#enableAddElementMenuFor
*/

public void enableElementMenuFor(CoTreeCatalogElementIF element, CoAbstractTreeCatalogEditor editor, CoMenu addElementMenu)
{
}
/**
 */
public abstract CoTreeCatalogElementFactoryIF getElementFactory ( );
}
