package com.bluebrim.browser.client;

import javax.swing.JTree;

import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.gui.client.CoTreeValueable;
import com.bluebrim.menus.client.CoMenuBuilder;

/**
	Interfaceklass för de UI-klasser som skall hantera en katalog i form
	av ett träd där användaren skall kunna lägga till och ta bort element med
	hjälp av en popupmeny. Popupmenyn kommer att ha olika utseende 
	beroende på vilket/vilka element som är selekterat:
	<ul>
	<li> inget seleketerat disablar menyvalet "Lägg till"
	<li> flera element selekterade disablar också menyvalet
	<li> när ett element är selekterat så läggs de möjliga valen av nya element i en submeny.
	<li> kataloggränssnittet ansvarar också för att enabla/disabla "Ta bort"
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
	som visar upp trädet med katalogelement. <br>
 */
public abstract JTree getTreeComponent();
/**
 */
public CoMenuBuilder getUIMenuBuilder();
/**
 */
public abstract boolean hasSelectedTreeElements();
}
