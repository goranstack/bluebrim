package com.bluebrim.browser.client;

import javax.swing.tree.TreePath;

import com.bluebrim.base.shared.CoFactoryManager;
import com.bluebrim.base.shared.CoStringResources;
import com.bluebrim.browser.shared.CoAddElementData;
import com.bluebrim.browser.shared.CoTreeCatalogElementFactoryIF;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.transact.shared.CoCommand;

/**
	Objektifierar algoritmen f�r att l�gga till ett element i en 
	tr�dstruktur som visas upp i ett CoTreeCatalogUI.
	Observera att #doExecute, #prepare och #finish �r publika i denna klass 
	till skillnad mot i CoCommand. Anledningen till detta �r att en instans 
	av CoAddTreeElementCommand anv�nds av CoAddTreeElementTransaction f�r att 
	hantera sj�lva adderandet vilket inneb�r att dess metoder m�ste vara publika.
	Var dock medveten om att de bara �r publika f�r denna klass och att n�got ansvar 
	i �vrigt inte tas om dessa metoder anropas utifr�n!
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
