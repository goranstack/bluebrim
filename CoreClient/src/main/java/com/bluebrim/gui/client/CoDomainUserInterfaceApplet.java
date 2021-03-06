package com.bluebrim.gui.client;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JApplet;
/**
 * Abstrakt appletklass som subklassas f�r att k�ra ett CoDomainUserInterface som applet.
 * <br> 
 * Abstrakta etoder som m�ste implementeras:
 * <ul>
 * <li>getDomainObject - svarar med det verksamhetsobjekt som skall visas
 * <li>getUserInterfaceFor - svarar med en instans av det CoDomainUserInterface
 * som skall anv�ndas f�r att visa upp verksamhetsobjektet.
 * </ul>
 * @author Lasse Svad�ngs 971008
 *
 */
public abstract class CoDomainUserInterfaceApplet extends JApplet {
/**
 */
public abstract void doInit();
/**
 * Information about this applet.
 */
public String getAppletInfo() {
	return "UserInterfaceApplet\n" + 
		"\n" + 
		"This applet was generated by a VisualAge SmartGuide.\n" + 
		"";
}
/**
 * Abstrakt metod som i subklassen skall svara med det verksamhetsobjekt
 * jag skall visa upp i mitt gr�nssnitt.
 */
public abstract Object getDomainObject();
/**
	Abstrakt metod som i subklassen skall svara med det CoDomainUserInterface
	jag skall visa upp.
	@param aDomainObject Object
 */
public abstract CoUserInterface getUserInterfaceFor(Object aDomainObject);
/**
 * L�gger ut gr�nssnittet f�r ett CoDomainUserInterface. 
 */
public void init() {
	super.init();
	doInit();
	this.getContentPane().setLayout(new BorderLayout());
	CoUserInterface tUserInterface = getUserInterfaceFor(getDomainObject());
	tUserInterface.buildForComponent();
	this.getContentPane().add(tUserInterface.getPanel());
}
/**
 * paint() draws the text on the drawing area.
 */
public void paint(Graphics g) {
	super.paint(g);
}
}
