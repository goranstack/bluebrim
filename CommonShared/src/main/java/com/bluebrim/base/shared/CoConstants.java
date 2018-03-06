package com.bluebrim.base.shared;

/**
 * Constants.
 */
public interface CoConstants {
	// NOTE: "public final static" is implicit in interfaces. /Markus

	String UNTITLED 				= "UNTITLED";
	String UNDEFINED 				= "UNDEFINED";	
	String PAGES 					= "PAGES";
	String NAME 					= "name";
	String SHORT_NAME				= "short_name";
	String DESCRIPTION 				= "description";
	String ADD_ITEM 				= "ADD_ITEM";
	String EDIT_ITEM 				= "EDIT_ITEM";
	String ADD_ITEM_ELLIPSIS 		= "ADD_ITEM_ELLIPSIS";
	String EDIT_ITEM_ELLIPSIS 		= "EDIT_ITEM_ELLIPSIS";
	String REMOVE_ITEM 				= "REMOVE_ITEM";
	String MOVE_ITEM 				= "MOVE_ITEM";
	String ADD_ITEM_LABEL 			= "ADD_ITEM_LABEL";
	String MOVE_ITEM_LABEL 			= "MOVE_ITEM_LABEL";
	String REMOVE_ITEM_LABEL 		= "REMOVE_ITEM_LABEL";
	String VALIDATION_ERROR 		= "VALIDATION_ERROR";
	String EMPTY_VALIDATION_ERROR 	= "EMPTY_VALIDATION_ERROR";
	String IDENTITY					= "identity";
	String CREATION_DATE			= "creation_date";
	String LAST_MODIFIED_DATE		= "last_modified_date";
	String CREATED_BY				= "created_by";
	String LAST_MODIFIED_BY			= "last_modified_by";
	String PERCENT					= "%";
	String PRINTED					= "printed";
	int	   UNDEFINED_POSITIVE_INT	= -1;
	
	String NONE_CHOSEN				= "none_chosen";

	String DOMAIN					= "domain";
	// Used as marker in renderers. Must be accessible from "base" code,
	// since serializable views should be able to return it (but never
	// serialize it, since we compare by identity). /Markus 2001-02-07
	Object PENDING_VALUE = "*PENDING*";
}