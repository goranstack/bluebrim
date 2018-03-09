package com.bluebrim.browser.client;

import javax.swing.tree.TreePath;

import com.bluebrim.base.shared.CoFactoryManager;
import com.bluebrim.base.shared.CoStringResources;
import com.bluebrim.browser.shared.CoAddElementData;
import com.bluebrim.browser.shared.CoTreeCatalogElementFactoryIF;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.transact.shared.CoCommand;

/**
	Objektifierar algoritmen för att lägga till ett element i en 
	trädstruktur som visas upp i ett CoTreeCatalogUI.
	Observera att #doExecute, #prepare och #finish är publika i denna klass 
	till skillnad mot i CoCommand. Anledningen till detta är att en instans 
	av CoAddTreeElementCommand används av CoAddTreeElementTransaction för att 
	hantera själva adderandet vilket innebär att dess metoder måste vara publika.
	Var dock medveten om att de bara är publika för denna klass och att något ansvar 
	i övrigt inte tas om dessa metoder anropas utifrån!
 */
public class CoAddTreeElementCommand extends CoCommand{
	protected CoTreeCatalogUI 			treeCatalogUI;
	protected TreePath					selection;
	protected CoAddElementData			elementData;
	protected CoTreeCatalogElementIF	addedElement;
public CoAddTreeElementCommand(CoTreeCatalogUI treeCatalogUI, CoAddElementData elementData)
{
	super(CoStringResources.getName("ADD_ITEM"));
	this.treeCatalogUI 	= treeCatalogUI;
	TreePath tSelectionPaths[] 		= treeCatalogUI.getTreeComponent().getSelectionPaths();
	selection 							= tSelectionPaths[0];
	this.elementData						= elementData;
}
public boolean doExecute()
{
	CoTreeCatalogElementIF tSelectedElement 		= treeCatalogUI.getSingleSelectedTreeElement();
	CoTreeCatalogElementFactoryIF tParentFactory 	= (CoTreeCatalogElementFactoryIF )CoFactoryManager.getFactory(tSelectedElement);
	return ((CoTreeCatalogElementIF )tParentFactory.addElement(tSelectedElement,elementData) != null);
}
public void finish()
{
	treeCatalogUI.getTreeCatalogHolder().reload(selection);
	treeCatalogUI.getTreeComponent().expandPath(selection);

	int		tPathLength									= selection.getPath().length;
	Object tPath[]										= new Object[tPathLength+1];
	System.arraycopy(selection.getPath(),0,tPath,0,tPathLength);
	tPath[tPathLength] 									= addedElement;
	
	treeCatalogUI.getTreeComponent().setSelectionPath(new TreePath(tPath));
}
public void prepare()
{
	if (elementData == null)
		throw new IllegalArgumentException("CoAddTreeElement.prepare - elementData == null");
}
}
