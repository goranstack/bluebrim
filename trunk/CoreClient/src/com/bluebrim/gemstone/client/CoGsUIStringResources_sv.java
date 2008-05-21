package com.bluebrim.gemstone.client;

public class CoGsUIStringResources_sv extends CoGsUIStringResources {
	static final Object[][] contents =
	{
		{CONNECTING,"Kopplar upp mot {0}..."},
		{LOCAL_SERVER,"lokal server"},
		{REQUESTED_COMMAND,"beg�rda funktionen"},
		{UNABLE_TO_CONNECT,"<html>Kunde inte koppla upp mot {0}.<br> {1}</html>"},
		{UNABLE_TO_COMMIT,"Kunde inte uppdatera {0} med det nya v�rdet"},
		{UNABLE_TO_COMMIT_1,"Kunde inte utf�ra {0} p� grund av *{1}*"},
		{UNABLE_TO_COMMIT_NO_ERROR,"Kunde inte utf�ra {0}"},
		{UNABLE_TO_SAVE,"Kunde inte spara �ndringar i {0} p� grund av *{1}*"},
		{USER_ID,"Anv�ndarnamn"},
		{OS_USER_ID,"Operativsystemanv�ndarnamn"},		
		{PASSWORD,"L�senord"},
		{LOGIN,"Anslut"},
		{CANCEL,"Avbryt"},

		{CHANGE_USER_NAME,"�ndra anv�ndarnamn ..."},
		{GIVE_AN_UNIQUE_USER_NAME,"Ange ett unikt anv�ndarnamn"},
		{USER_ALREADY_EXISTS,"Det finns redan en anv�ndare med detta namn"},
		{DELETE_USER_ACCOUNTS_QUESTION, "En eller flera av de markerade anv�ndarna\n"+
										"har anv�ndarkonton. �ven dessa kommer att tas bort.\n"+
										"Vill du forts�tta?"},
		
		{CHANGE_PASSWORD, "�ndra l�senord"},
		{OLD_PASSWORD, "Gammalt l�senord"},
		{NEW_PASSWORD, "Nytt l�senord"},
		{CONFIRM_NEW_PASSWORD, "Bekr�fta nytt l�senord"},


		{ADD_LIST_ELEMENT_TRANSACTION,"L�gg till katalogelement"},
		{REMOVE_LIST_ELEMENTS_TRANSACTION,"Ta bort katalogelement"},
		{ADD_TREE_ELEMENT_TRANSACTION,"L�gg till tr�dkatalogelement"},
		{REMOVE_TREE_ELEMENTS_TRANSACTION,"Ta bort tr�dkatalogelement"},
		{SET_PASSWORD_TRANSACTION,"�ndra l�senord"},
		{CHANGE_PASSWORD_TRANSACTION,"�ndra l�senord"},

		{PASSWORDS_NOT_EQUAL_ERROR, "De angivna, nya l�senorden �r inte lika"},
		
		{SERVER_NOT_FOUND_ERROR, "Kunde inte logga in. Hittade inte servern."},
		{MALFORMED_URL_ERROR, "Kunde inte logga in. Adressen till servern  �r felaktigt angiven"},
		{AUTHENTICATION_ERROR, "Kunde inte logga in. Ogiltigt anv�ndarnamn och/eller l�senord"},
		{AUTHENTICATION_ERROR_REASON, "pga felaktigt anv�ndarnamn eller l�senord"},
		{CONNECTION_ERROR_REASON, "Servern �r tillf�lligt nere. F�rs�k igen lite senare."},

		{ADD_LIST_ELEMENT_ERROR,"Kunde inte l�gga till ett element pga {0}"},
		{REMOVE_LIST_ELEMENTS_ERROR,"Kunde inte ta bort ett element pga {0}"},
		{ADD_TREE_ELEMENT_ERROR,"Kunde inte l�gga till ett element pga {0}"},
		{REMOVE_TREE_ELEMENTS_ERROR,"Kunde inte ta bort ett element pga {0}"},
		{SET_PASSWORD_ERROR,"Kunde inte �ndra l�senord f�r {0} pga {1}"},
		{CHANGE_PASSWORD_ERROR,"Kunde inte �ndra l�senord f�r {0} pga {1}"},
		
		{SYS_ADM,"Systemansvarig"},
		{USER_NOT_UNIQUE_ERROR, "Det finns redan en anv�ndare som har anv�ndarnamnet {0}!"},
		
		{USER, "Anv�ndare"},
		{"PERSON", "Personuppgifter"},
	};





/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}