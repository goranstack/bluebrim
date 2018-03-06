package com.bluebrim.gemstone.client;

public class CoGsUIStringResources_sv extends CoGsUIStringResources {
	static final Object[][] contents =
	{
		{CONNECTING,"Kopplar upp mot {0}..."},
		{LOCAL_SERVER,"lokal server"},
		{REQUESTED_COMMAND,"begärda funktionen"},
		{UNABLE_TO_CONNECT,"<html>Kunde inte koppla upp mot {0}.<br> {1}</html>"},
		{UNABLE_TO_COMMIT,"Kunde inte uppdatera {0} med det nya värdet"},
		{UNABLE_TO_COMMIT_1,"Kunde inte utföra {0} på grund av *{1}*"},
		{UNABLE_TO_COMMIT_NO_ERROR,"Kunde inte utföra {0}"},
		{UNABLE_TO_SAVE,"Kunde inte spara ändringar i {0} på grund av *{1}*"},
		{USER_ID,"Användarnamn"},
		{OS_USER_ID,"Operativsystemanvändarnamn"},		
		{PASSWORD,"Lösenord"},
		{LOGIN,"Anslut"},
		{CANCEL,"Avbryt"},

		{CHANGE_USER_NAME,"Ändra användarnamn ..."},
		{GIVE_AN_UNIQUE_USER_NAME,"Ange ett unikt användarnamn"},
		{USER_ALREADY_EXISTS,"Det finns redan en användare med detta namn"},
		{DELETE_USER_ACCOUNTS_QUESTION, "En eller flera av de markerade användarna\n"+
										"har användarkonton. Även dessa kommer att tas bort.\n"+
										"Vill du fortsätta?"},
		
		{CHANGE_PASSWORD, "Ändra lösenord"},
		{OLD_PASSWORD, "Gammalt lösenord"},
		{NEW_PASSWORD, "Nytt lösenord"},
		{CONFIRM_NEW_PASSWORD, "Bekräfta nytt lösenord"},


		{ADD_LIST_ELEMENT_TRANSACTION,"Lägg till katalogelement"},
		{REMOVE_LIST_ELEMENTS_TRANSACTION,"Ta bort katalogelement"},
		{ADD_TREE_ELEMENT_TRANSACTION,"Lägg till trädkatalogelement"},
		{REMOVE_TREE_ELEMENTS_TRANSACTION,"Ta bort trädkatalogelement"},
		{SET_PASSWORD_TRANSACTION,"Ändra lösenord"},
		{CHANGE_PASSWORD_TRANSACTION,"Ändra lösenord"},

		{PASSWORDS_NOT_EQUAL_ERROR, "De angivna, nya lösenorden är inte lika"},
		
		{SERVER_NOT_FOUND_ERROR, "Kunde inte logga in. Hittade inte servern."},
		{MALFORMED_URL_ERROR, "Kunde inte logga in. Adressen till servern  är felaktigt angiven"},
		{AUTHENTICATION_ERROR, "Kunde inte logga in. Ogiltigt användarnamn och/eller lösenord"},
		{AUTHENTICATION_ERROR_REASON, "pga felaktigt användarnamn eller lösenord"},
		{CONNECTION_ERROR_REASON, "Servern är tillfälligt nere. Försök igen lite senare."},

		{ADD_LIST_ELEMENT_ERROR,"Kunde inte lägga till ett element pga {0}"},
		{REMOVE_LIST_ELEMENTS_ERROR,"Kunde inte ta bort ett element pga {0}"},
		{ADD_TREE_ELEMENT_ERROR,"Kunde inte lägga till ett element pga {0}"},
		{REMOVE_TREE_ELEMENTS_ERROR,"Kunde inte ta bort ett element pga {0}"},
		{SET_PASSWORD_ERROR,"Kunde inte ändra lösenord för {0} pga {1}"},
		{CHANGE_PASSWORD_ERROR,"Kunde inte ändra lösenord för {0} pga {1}"},
		
		{SYS_ADM,"Systemansvarig"},
		{USER_NOT_UNIQUE_ERROR, "Det finns redan en användare som har användarnamnet {0}!"},
		
		{USER, "Användare"},
		{"PERSON", "Personuppgifter"},
	};





/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}