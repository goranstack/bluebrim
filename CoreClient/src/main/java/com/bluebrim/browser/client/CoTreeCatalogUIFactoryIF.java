package com.bluebrim.browser.client;

import javax.swing.Action;

import com.bluebrim.browser.shared.CoTreeCatalogElementFactoryIF;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.menus.client.CoMenu;
/**
 	Interface f�r factoryklasser som hanterar gr�nssnitt
 	f�r verksamhetsobjekt av typen CoTreeCatalogElement
 */
public interface CoTreeCatalogUIFactoryIF {
/**
	Bygger upp "L�gg till" menyn 'menu' f�r tr�dkatalogseditorn 'editor'.<br>
	Menyn �r kontextk�nslig, dvs ser annorlunda ut beroende p� vilket element
	som �r selekterat. 'catalogElement' �r en referens till detta element.<br>
	Det skapas ett menyval f�r varje typ av element som kan vara barn till 'catalogElement'".
 */
public abstract void buildAddElementMenuFor (CoTreeCatalogElementIF catalogElement, CoAbstractTreeCatalogEditor editor, CoMenu menu );
/**
 */
public Action createAddElementAction(CoAbstractTreeCatalogEditor editor);
/**
 */
public void enableElementMenuFor(CoTreeCatalogElementIF element, CoAbstractTreeCatalogEditor editor, CoMenu addElementMenu);
/**
 */
public  CoTreeCatalogElementFactoryIF getElementFactory ( );
}
