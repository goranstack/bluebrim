package com.bluebrim.browser.shared;

import com.bluebrim.base.shared.*;
/**
	Interface f�r klasser som skall fungera som factoryklasser f�r 
	verksamhetsobjekt som skall visas upp och editeras i en tr�dstruktur.
	Definierar bl a metoder f�r att l�gga till och ta bort element samt kan svara 
	med en array som beskriver vilka typer av element verksamhetsobjektet kan 
	ha som "barn". 
 * 
 */
public interface CoTreeCatalogElementFactoryIF extends CoFactoryIF {
	public CoTreeCatalogElementIF addElement(CoTreeCatalogElementIF parent, CoAddElementData elementData);
/**
	Implementeras i subklassen och skall d�r se till att 'parent'
	f�r ett nytt "barn" av den klass som factoryklassen svarar f�r.
	Skall skapa ett nytt element och via double-dispatching skicka
	detta vidare till factorklassen f�r 'parent'.
 */
public abstract CoTreeCatalogElementIF addNewElementTo (CoTreeCatalogElementIF parent, Object elementData);
	public String[] getAllowedElements ( );
	public String getElementIconName();
	public String getElementKey();
	public String getElementType();
public String getIconResourceAnchor ( );
	/**
	Implementeras i subklassen och skall d�r se till att 'element' 
	tas bort fr�n 'parent'. 'element' �r av den klass som factoryklassen 
	svarar f�r. Via double-dispatching skickas detta vidare till factoryklassen 
	f�r 'parent'.<br>
 */
	public CoTreeCatalogElementIF removeElementFrom (CoTreeCatalogElementIF parent, CoTreeCatalogElementIF element);
}
