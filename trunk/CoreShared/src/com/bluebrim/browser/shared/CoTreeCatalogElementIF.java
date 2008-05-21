package com.bluebrim.browser.shared;

import java.util.*;

/**
 	Interface för de verksamhetsobjekt somskall kunna visa upp sig 
 	som noder i ett träd. Utvidgar CoCatalogElementIF.
 	Klasser som implementerar detta protokoll måste implementera följande metoder:
 	<ul>
 	<li>	#getElements svarar med en lista innehållande objektets barn.
 	</ul>
 	Objektets måste i sin tur också implementera CoTreeCatalogElementIF
 */
public interface CoTreeCatalogElementIF extends CoCatalogElementIF  {
public List getElements();
public CoTreeCatalogElementIF getTreeCatalogElement();
}
