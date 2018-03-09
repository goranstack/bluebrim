package com.bluebrim.browser.client;

import javax.swing.Action;

import com.bluebrim.browser.shared.CoTreeCatalogElementFactoryIF;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.menus.client.CoMenu;
/**
 	Interface för factoryklasser som hanterar gränssnitt
 	för verksamhetsobjekt av typen CoTreeCatalogElement
 */
public interface CoTreeCatalogUIFactoryIF {
/**
	Bygger upp "Lägg till" menyn 'menu' för trädkatalogseditorn 'editor'.<br>
	Menyn är kontextkänslig, dvs ser annorlunda ut beroende på vilket element
	som är selekterat. 'catalogElement' är en referens till detta element.<br>
	Det skapas ett menyval för varje typ av element som kan vara barn till 'catalogElement'".
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
