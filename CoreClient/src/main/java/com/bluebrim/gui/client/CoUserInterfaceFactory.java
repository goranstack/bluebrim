package com.bluebrim.gui.client;

import com.bluebrim.base.shared.CoFactoryElementIF;
/**
	
 	En abstrakt superklass till de klasser som ansvarar för att 
 	skapa rätt gränssnitt för ett verksamhetsobjekt. <br>
 	Det finns även metoder somm skapar ett gränssnitt och öppnar
 	det för ett givet verksamhetsobjekt.
 */
public abstract class CoUserInterfaceFactory {

/**
 */
public CoUserInterfaceFactory () {
}
/**
	Subklassen implementerar och svarar med ett gränssnitt för 'element'.<br>
	'key' kan användas för att särskilja olika gränssnitt om 'element' kan visas
	upp i fler än ett.<br>
 	'element' blir INTE installerat som domänobjekt i det nya gränssnittet.
*/
public CoDomainUserInterface createUserInterfaceFor (CoFactoryElementIF element, String key )
{
		return createUserInterfaceFor(element, false, key);
}
/**
	Subklassen implementerar och svarar med ett gränssnitt för 'element'.<br>
	'key' kan användas för att särskilja olika gränssnitt om 'element' kan visas
	upp i fler än ett.<br> 
	Om 'element' skall installeras som domänsobjekt i det nya gränssnittet skall 
	'install' vara satt till true.
 */
protected abstract CoDomainUserInterface createUserInterfaceFor(CoFactoryElementIF element,boolean install, String key );
/**
	Subklassen implementerar och svarar med ett gränssnitt för 'element'.<br>
	'key' kan användas för att särskilja olika gränssnitt om 'element' kan visas
	upp i fler än ett.
	'element' blir installerat som domänobjekt i det nya gränssnittet.
 */
public CoDomainUserInterface createUserInterfaceForAndInstall (CoFactoryElementIF element, String key )
{
	return createUserInterfaceFor(element, true, key);
}
/**
	Skapa ett nytt gränssnitt för 'element' och öppna upp det i ett eget fönster.
 */
public CoDomainUserInterface openUserInterfaceOn (CoFactoryElementIF element, String key )
{
	CoDomainUserInterface tUserInterface = createUserInterfaceForAndInstall(element, key);
	tUserInterface.openInWindow();
	return tUserInterface;
}
}
