package com.bluebrim.browser.shared;

import com.bluebrim.base.shared.*;
/**
	Interface för klasser som skall fungera som factoryklasser för 
	verksamhetsobjekt som skall visas upp och editeras i en trädstruktur.
	Definierar bl a metoder för att lägga till och ta bort element samt kan svara 
	med en array som beskriver vilka typer av element verksamhetsobjektet kan 
	ha som "barn". 
 * 
 */
public interface CoTreeCatalogElementFactoryIF extends CoFactoryIF {
	public CoTreeCatalogElementIF addElement(CoTreeCatalogElementIF parent, CoAddElementData elementData);
/**
	Implementeras i subklassen och skall där se till att 'parent'
	får ett nytt "barn" av den klass som factoryklassen svarar för.
	Skall skapa ett nytt element och via double-dispatching skicka
	detta vidare till factorklassen för 'parent'.
 */
public abstract CoTreeCatalogElementIF addNewElementTo (CoTreeCatalogElementIF parent, Object elementData);
	public String[] getAllowedElements ( );
	public String getElementIconName();
	public String getElementKey();
	public String getElementType();
public String getIconResourceAnchor ( );
	/**
	Implementeras i subklassen och skall där se till att 'element' 
	tas bort från 'parent'. 'element' är av den klass som factoryklassen 
	svarar för. Via double-dispatching skickas detta vidare till factoryklassen 
	för 'parent'.<br>
 */
	public CoTreeCatalogElementIF removeElementFrom (CoTreeCatalogElementIF parent, CoTreeCatalogElementIF element);
}
