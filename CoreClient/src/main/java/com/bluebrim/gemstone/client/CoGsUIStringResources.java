package com.bluebrim.gemstone.client;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.bluebrim.gui.client.CoUIConstants;
import com.bluebrim.resource.shared.CoOldResourceBundle;

public class CoGsUIStringResources extends CoOldResourceBundle implements com.bluebrim.gemstone.shared.CoGemstoneConstants, CoGemStoneUIConstants, CoUIConstants {
	public static CoOldResourceBundle rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoGsUIStringResources.class.getName());
	static final Object[][] contents =
	{
		{SYS_ADM,"System administrator"},
		{CONNECTING,"Connecting to {0}..."},
		{LOCAL_SERVER,"local server"},
		{REQUESTED_COMMAND,"requested command"},
		{USER_ID,"User id"},
		{OS_USER_ID,"Operating system user id"},
		{PASSWORD,"Password"},
		{LOGIN,"Connect"},
		{CANCEL,"Cancel"},
		{CHANGE_USER_NAME,"Change user name ..."},
		{GIVE_AN_UNIQUE_USER_NAME,"State a unique user name"},
		{USER_ALREADY_EXISTS,"A user with this user name already exists"},
		{DELETE_USER_ACCOUNTS_QUESTION, "On or more of the selected user has an account\n"+
										"These will be removed as well.\n"+
										"Do you want to continue?"},
		
		{CHANGE_PASSWORD, "Change password"},
		{OLD_PASSWORD, "Old password"},
		{NEW_PASSWORD, "New password"},
		{CONFIRM_NEW_PASSWORD, "Confirm new password"},
		{PASSWORDS_NOT_EQUAL_ERROR, "The new passwords aren't equal"},
		
		{SERVER_NOT_FOUND_ERROR, "Unable to login. Couldn't locate server {0}"},
		{MALFORMED_URL_ERROR, "{0} is not a valid server address"},
		{AUTHENTICATION_ERROR, "Unable to login. Wrong userid and/or password"},
		{AUTHENTICATION_ERROR_REASON, "because of wrong userid or password"},
		{CONNECTION_ERROR_REASON, "The server is down. Try again later on."},

		{ADD_LIST_ELEMENT_TRANSACTION,"Add element"},
		{REMOVE_LIST_ELEMENTS_TRANSACTION,"Remove elements"},
		{ADD_TREE_ELEMENT_TRANSACTION,"Add element"},
		{REMOVE_TREE_ELEMENTS_TRANSACTION,"Remove elements"},
		{SET_PASSWORD_TRANSACTION,"Change password"},
		{CHANGE_PASSWORD_TRANSACTION,"Change password"},

		{UNABLE_TO_CONNECT,"<html>Unable to connect to {0}.<br>{1}</html>"},
		{UNABLE_TO_SAVE,"Unable to save changes to {0} because of *{1}*"},
		{UNABLE_TO_COMMIT,"Unable to update {0} with the new value"},
		{UNABLE_TO_COMMIT_1,"Unable to perform {0} because of *{1}*"},
		{UNABLE_TO_COMMIT_NO_ERROR,"Unable to perform {0}"},
		{USER_NOT_UNIQUE_ERROR, "There already exists a user with username {0}!"},

		{ADD_LIST_ELEMENT_ERROR,"Unable to add element: {0}"},
		{REMOVE_LIST_ELEMENTS_ERROR,"Unable to remove element: {0}"},
		{ADD_TREE_ELEMENT_ERROR,"Unable to add element: {0}"},
		{REMOVE_TREE_ELEMENTS_ERROR,"Unable to remove element: {0}"},
		{SET_PASSWORD_ERROR,"Unable to change password for {0}. Reason: {1}"},
		{CHANGE_PASSWORD_ERROR,"Unable to change password for {0}. Reason: {1}"},

			
		{USER, "User"},
	};





/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	return rb;
}


/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}


/**
  Svara med det namn som hör till nyckeln aKey.
  Saknas en resurs för aKey så svara med denna.
 */
public static String getName(String aKey ) {
	try
	{
		return getBundle().getString(aKey);
	}
	catch (MissingResourceException e)
	{
		return aKey;
	}			
}


/**
  Sätter om rb när Locale har ändrats. 
 */
public static void resetBundle ( ) {
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoGsUIStringResources.class.getName());
	rb.resetLookup();
}
}