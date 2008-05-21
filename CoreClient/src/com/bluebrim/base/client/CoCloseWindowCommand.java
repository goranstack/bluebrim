package com.bluebrim.base.client;

import com.bluebrim.base.client.command.*;
import com.bluebrim.gui.client.*;
/**
	En kommandoklass som används för att stänga ett fönster, t ex från en meny.<br> 
	Om den vanliga eventhanteringen används för att stängs fönstret (dvs via ett event som postas
	direkt från en komponent i fönstret) så blir det ofelbart en exception när anropsstacken
	rullar tillbaka till en komponent som ligger i ett fönster som inte längre finns! <br>
	Detta undviks genom att i stället exekvera ett kommando som i sin execute-metod postar ett 
	CoCloseWindowEvent till SystemEventQueue. Denna kö tappas på event i en egen tråd, 
	frånkopplad från gränssnittet, och får därigenom inga bekymmer med stacken.
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
