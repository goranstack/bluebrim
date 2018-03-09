package com.bluebrim.base.shared;


/**
	Localized string resources.
 	<blockquote><pre>
 		CoStringResources.getName(UNTITLED);
 	</pre>
 	</blockquote>
	
 */
public class CoStringResources_sv extends CoStringResources {
	static final Object[][] contents =
	{
		{UNTITLED,"Namnlös"},
		{UNDEFINED,"Odefinierad"},				
		{PAGES,"sidor"},
		{NAME, "Namn"},
		{DESCRIPTION, "Beskrivning"}, 
		{ADD_ITEM,"Lägg till"},
		{EDIT_ITEM,"Ändra"},
		{ADD_ITEM_ELLIPSIS,"Lägg till ..."},
		{EDIT_ITEM_ELLIPSIS,"Ändra ..."},
		{REMOVE_ITEM,"Ta bort"},
		{MOVE_ITEM,"Flytta"},
		{ADD_ITEM_LABEL,"Lägg till ny {0}"},
		{REMOVE_ITEM_LABEL,"Ta bort markerad {0}"},
		{CREATION_DATE, "Skapad"},
		{NONE_CHOSEN, "(Ingen vald)"},
		
		// Validation
		{VALIDATION_ERROR,"{0} är inte ett giltigt värde för {1}"},
		{EMPTY_VALIDATION_ERROR,"{0} måste ha ett värde"},


		{PRINTED, "Utskriven av {0}, {1}"},

		{Boolean.TRUE.toString(),                       "Ja"},
		{Boolean.FALSE.toString(),                      "Nej"},
	};	
/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}