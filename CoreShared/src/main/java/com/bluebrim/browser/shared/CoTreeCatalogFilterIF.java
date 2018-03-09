package com.bluebrim.browser.shared;

import java.util.*;
/**
 	Interface f�r klasser som anv�nds f�r att filtrera en 
 	tr�dstruktur som hanteras av en CoFilteredTreeAspectAdaptor.<br>
 	Metoderna #addElements,#removeElements och #getElements �r t�nkta att anv�ndas
 	f�r att uppdatera en cache av filtrerade element. P� s� s�tt kan vi minska antalet
 	instansieringar av kollektioner p� bekostnad av ett st�rre minneskrav. Extremfallet �r om
 	filtret inte filtrerar bort ett enda element. D� f�r vi en cache som inneh�ller lika 
 	m�nga element som den struktur som skulle filtreras. <br>
 	F�r ett exempel p� hur cachen fungerar se CoPublishingRuleFilter.
 	@see CoPublishingRuleFilter
 * 
 */
public interface CoTreeCatalogFilterIF {
/**
	Implementeras f�r att adderar 'elements' - resultatet av en filtrering av 
	tr�delementet 'elements' element till mottagarens cache.
 	@param elements List
 	@param element com.bluebrim.browser.shared.CoTreeCatalogElementIF
 */
public void addElements ( List elements, CoTreeCatalogElementIF element);
/**
 	Applicera mottagaren p� tr�dkatalogelementet 'element' och svara med 
 	true om det uppfyller filtrets villkor f�r att ing� i den visade tr�dstrukturen.
 	@return boolean
	@param element com.bluebrim.browser.shared.CoTreeCatalogElementIF
 */
public boolean applyOn( CoTreeCatalogElementIF element);
/**
 */
public String getDescription ();
/**
	Svarar med den cachade struktur som ev finns upplagd f�r 'element'.
 */
public List getElements (CoTreeCatalogElementIF element);
/**
 * @param elements Vector
 * @param element com.bluebrim.browser.shared.CoTreeCatalogElementIF
 */
public void removeElements (CoTreeCatalogElementIF element);
/**
	Implementeras f�r att ta bort alla cachad Vectorer.
 */
public void resetElements();
/**
	Implementeras f�r att ta bort en ev cachad Vector f�r
	tr�delementet 'element'
 * @param element com.bluebrim.browser.shared.CoTreeCatalogElementIF
 */
public void resetElementsFrom (CoTreeCatalogElementIF element);
}
