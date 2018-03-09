package com.bluebrim.browser.client;

import javax.swing.JTree;

import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.gui.client.CoTreeValueable;
import com.bluebrim.menus.client.CoMenuBuilder;

/**
	Interfaceklass f�r de UI-klasser som skall hantera en katalog i form
	av ett tr�d d�r anv�ndaren skall kunna l�gga till och ta bort element med
	hj�lp av en popupmeny. Popupmenyn kommer att ha olika utseende 
	beroende p� vilket/vilka element som �r selekterat:
	<ul>
	<li> inget seleketerat disablar menyvalet "L�gg till"
	<li> flera element selekterade disablar ocks� menyvalet
	<li> n�r ett element �r selekterat s� l�ggs de m�jliga valen av nya element i en submeny.
	<li> kataloggr�nssnittet ansvarar ocks� f�r att enabla/disabla "Ta bort"
	</ul>
 */
public interface CoTreeCatalogUI {
/**
 */
public abstract String getAddElementItemLabel();
/**
 */
public abstract String getRemoveElementItemLabel();
/**
 */
public abstract CoTreeCatalogElementIF[] getSelectedTreeElements();
/**
 */
public abstract CoTreeCatalogElementIF getSingleSelectedTreeElement();
/**
 */
public CoTreeValueable getTreeCatalogHolder();
/**
	Abstrakt metod som i subklassen skall svara med den instans av JTree
	som visar upp tr�det med katalogelement. <br>
 */
public abstract JTree getTreeComponent();
/**
 */
public CoMenuBuilder getUIMenuBuilder();
/**
 */
public abstract boolean hasSelectedTreeElements();
}
