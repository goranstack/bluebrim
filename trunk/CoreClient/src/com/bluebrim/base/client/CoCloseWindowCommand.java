package com.bluebrim.base.client;

import com.bluebrim.base.client.command.*;
import com.bluebrim.gui.client.*;
/**
	En kommandoklass som anv�nds f�r att st�nga ett f�nster, t ex fr�n en meny.<br> 
	Om den vanliga eventhanteringen anv�nds f�r att st�ngs f�nstret (dvs via ett event som postas
	direkt fr�n en komponent i f�nstret) s� blir det ofelbart en exception n�r anropsstacken
	rullar tillbaka till en komponent som ligger i ett f�nster som inte l�ngre finns! <br>
	Detta undviks genom att i st�llet exekvera ett kommando som i sin execute-metod postar ett 
	CoCloseWindowEvent till SystemEventQueue. Denna k� tappas p� event i en egen tr�d, 
	fr�nkopplad fr�n gr�nssnittet, och f�r d�rigenom inga bekymmer med stacken.
*/
public class CoCloseWindowCommand extends CoPostEventCommand{
	CoUserInterface userInterface;
/**
 */
public CoCloseWindowCommand (CoUserInterface userInterface ) {
	this.userInterface = userInterface;
}
/**
 */
public CoEvent createEvent () {
	return new CoCloseWindowEvent(userInterface);
}
}
