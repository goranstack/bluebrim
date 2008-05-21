package com.bluebrim.browser.shared;

import java.util.*;

/**
 	Interface f�r de verksamhetsobjekt somskall kunna visa upp sig 
 	som noder i ett tr�d. Utvidgar CoCatalogElementIF.
 	Klasser som implementerar detta protokoll m�ste implementera f�ljande metoder:
 	<ul>
 	<li>	#getElements svarar med en lista inneh�llande objektets barn.
 	</ul>
 	Objektets m�ste i sin tur ocks� implementera CoTreeCatalogElementIF
 */
public interface CoTreeCatalogElementIF extends CoCatalogElementIF  {
public List getElements();
public CoTreeCatalogElementIF getTreeCatalogElement();
}
