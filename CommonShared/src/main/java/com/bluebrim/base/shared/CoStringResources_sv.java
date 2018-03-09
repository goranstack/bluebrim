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
		{UNTITLED,"Namnl�s"},
		{UNDEFINED,"Odefinierad"},				
		{PAGES,"sidor"},
		{NAME, "Namn"},
		{DESCRIPTION, "Beskrivning"}, 
		{ADD_ITEM,"L�gg till"},
		{EDIT_ITEM,"�ndra"},
		{ADD_ITEM_ELLIPSIS,"L�gg till ..."},
		{EDIT_ITEM_ELLIPSIS,"�ndra ..."},
		{REMOVE_ITEM,"Ta bort"},
		{MOVE_ITEM,"Flytta"},
		{ADD_ITEM_LABEL,"L�gg till ny {0}"},
		{REMOVE_ITEM_LABEL,"Ta bort markerad {0}"},
		{CREATION_DATE, "Skapad"},
		{NONE_CHOSEN, "(Ingen vald)"},
		
		// Validation
		{VALIDATION_ERROR,"{0} �r inte ett giltigt v�rde f�r {1}"},
		{EMPTY_VALIDATION_ERROR,"{0} m�ste ha ett v�rde"},


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