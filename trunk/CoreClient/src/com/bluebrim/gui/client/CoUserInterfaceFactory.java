package com.bluebrim.gui.client;

import com.bluebrim.base.shared.CoFactoryElementIF;
/**
	
 	En abstrakt superklass till de klasser som ansvarar f�r att 
 	skapa r�tt gr�nssnitt f�r ett verksamhetsobjekt. <br>
 	Det finns �ven metoder somm skapar ett gr�nssnitt och �ppnar
 	det f�r ett givet verksamhetsobjekt.
 */
public abstract class CoUserInterfaceFactory {

/**
 */
public CoUserInterfaceFactory () {
}
/**
	Subklassen implementerar och svarar med ett gr�nssnitt f�r 'element'.<br>
	'key' kan anv�ndas f�r att s�rskilja olika gr�nssnitt om 'element' kan visas
	upp i fler �n ett.<br>
 	'element' blir INTE installerat som dom�nobjekt i det nya gr�nssnittet.
*/
public CoDomainUserInterface createUserInterfaceFor (CoFactoryElementIF element, String key )
{
		return createUserInterfaceFor(element, false, key);
}
/**
	Subklassen implementerar och svarar med ett gr�nssnitt f�r 'element'.<br>
	'key' kan anv�ndas f�r att s�rskilja olika gr�nssnitt om 'element' kan visas
	upp i fler �n ett.<br> 
	Om 'element' skall installeras som dom�nsobjekt i det nya gr�nssnittet skall 
	'install' vara satt till true.
 */
protected abstract CoDomainUserInterface createUserInterfaceFor(CoFactoryElementIF element,boolean install, String key );
/**
	Subklassen implementerar och svarar med ett gr�nssnitt f�r 'element'.<br>
	'key' kan anv�ndas f�r att s�rskilja olika gr�nssnitt om 'element' kan visas
	upp i fler �n ett.
	'element' blir installerat som dom�nobjekt i det nya gr�nssnittet.
 */
public CoDomainUserInterface createUserInterfaceForAndInstall (CoFactoryElementIF element, String key )
{
	return createUserInterfaceFor(element, true, key);
}
/**
	Skapa ett nytt gr�nssnitt f�r 'element' och �ppna upp det i ett eget f�nster.
 */
public CoDomainUserInterface openUserInterfaceOn (CoFactoryElementIF element, String key )
{
	CoDomainUserInterface tUserInterface = createUserInterfaceForAndInstall(element, key);
	tUserInterface.openInWindow();
	return tUserInterface;
}
}
