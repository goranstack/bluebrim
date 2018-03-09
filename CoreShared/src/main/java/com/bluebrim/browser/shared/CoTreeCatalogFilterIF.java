package com.bluebrim.browser.shared;

import java.util.*;
/**
 	Interface för klasser som används för att filtrera en 
 	trädstruktur som hanteras av en CoFilteredTreeAspectAdaptor.<br>
 	Metoderna #addElements,#removeElements och #getElements är tänkta att användas
 	för att uppdatera en cache av filtrerade element. På så sätt kan vi minska antalet
 	instansieringar av kollektioner på bekostnad av ett större minneskrav. Extremfallet är om
 	filtret inte filtrerar bort ett enda element. Då får vi en cache som innehåller lika 
 	många element som den struktur som skulle filtreras. <br>
 	För ett exempel på hur cachen fungerar se CoPublishingRuleFilter.
 	@see CoPublishingRuleFilter
 * 
 */
public interface CoTreeCatalogFilterIF {
/**
	Implementeras för att adderar 'elements' - resultatet av en filtrering av 
	trädelementet 'elements' element till mottagarens cache.
 	@param elements List
 	@param element com.bluebrim.browser.shared.CoTreeCatalogElementIF
 */
public void addElements ( List elements, CoTreeCatalogElementIF element);
/**
 	Applicera mottagaren på trädkatalogelementet 'element' och svara med 
 	true om det uppfyller filtrets villkor för att ingå i den visade trädstrukturen.
 	@return boolean
	@param element com.bluebrim.browser.shared.CoTreeCatalogElementIF
 */
public boolean applyOn( CoTreeCatalogElementIF element);
/**
 */
public String getDescription ();
/**
	Svarar med den cachade struktur som ev finns upplagd för 'element'.
 */
public List getElements (CoTreeCatalogElementIF element);
/**
 * @param elements Vector
 * @param element com.bluebrim.browser.shared.CoTreeCatalogElementIF
 */
public void removeElements (CoTreeCatalogElementIF element);
/**
	Implementeras för att ta bort alla cachad Vectorer.
 */
public void resetElements();
/**
	Implementeras för att ta bort en ev cachad Vector för
	trädelementet 'element'
 * @param element com.bluebrim.browser.shared.CoTreeCatalogElementIF
 */
public void resetElementsFrom (CoTreeCatalogElementIF element);
}
