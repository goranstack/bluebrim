package com.bluebrim.browser.shared;

import com.bluebrim.base.shared.*;
/**
 	En abstrakt superklass till de klasser som skall fungera
 	som factoryklasser f�r implementationer av CoTreeCatalogElementIF. <br>
 	En konkret subklass av CoTreeCatalogElementFactory kan svara p�
 	vilka klasser som kan fungera som element f�r dess katalogelementklass.<br> 
 	En factoryklass ansvarar ocks� f�r att l�gga till nya "barn" till en
 	instans av sin klass. Den mekanism som anv�nds f�r detta bygger p� double-dispatching
 	och kr�ver (tyv�rr) ett gemensamt interface f�r alla factoryklasser vars
 	element ing�r i samma tr�dstruktur. Detta interface m�ste definiera 
 	en addElementTo(Object parent XXXXX element)metod f�r varje klass XXX  som kan
 	ing� i strukturen. <br>
 	Av praktiska sk�l �r det b�st att skapa en gemensam superklass
 	som implementerar stubbar f�r alla dessa metoder. P� s� s�tt kan de faktiska
 	factoryklasserna sedan implementera endast den eller de add-metoder de sj�lva beh�ver.<br>
 	F�r ett exempel se CoPressModelFactory.
 	@see CoPressModelFactory
 */
public abstract class CoTreeCatalogElementFactory implements CoTreeCatalogElementFactoryIF{
	CoTreeCatalogElementIF prototypeElement = null;
	private String m_factoryKey;
	private String m_iconResourceAnchor;
	private String m_iconName;
	private String m_type;
/**
 */
public CoTreeCatalogElementFactory () {
	super();
}
/**
 */
public CoTreeCatalogElementFactory (String factoryKey, String iconResourceAnchor, String iconName, String type) {
	m_factoryKey 			= factoryKey;
	m_iconResourceAnchor 	= iconResourceAnchor;
	m_iconName				= iconName;
	m_type					= type;
}
/**
	Ett "barn" vars klass representeras av 'elementData' skall
	l�ggas till 'parent'. Detta �r factoryklassens ansvar.
 */
public CoTreeCatalogElementIF addElement(CoTreeCatalogElementIF parent, CoAddElementData elementData)
{
	CoTreeCatalogElementFactoryIF tFactory = (CoTreeCatalogElementFactoryIF )CoFactoryManager.getFactory(elementData.getElementType());
	return tFactory.addNewElementTo(parent, elementData.getElementData());
}
/**
	Implementeras i subklassen och skall d�r se till att 'parent'
	f�r ett nytt "barn" av den klass som factoryklassen svarar f�r.
	Skall skapa ett nytt element och via double-dispatching skicka
	detta vidare till factorklassen f�r 'parent'.<br>
	Se exempelvis CoSupportedPaperSizeFactory.
	@see CoSupportedPaperSizeFactory#addNewElementTo  
 */
public abstract CoTreeCatalogElementIF addNewElementTo (CoTreeCatalogElementIF parent, Object elementData);
/**
	Subklassen implementerar och svarar med en String[] d�r varje 
	element �r det fullst�ndiga namnet p� en klass vars instanser 
	kan vara "barn" till fabrikens katalogelement. 
	Svara med null om elementen utg�r l�v i strukturen.
*/
public abstract String[] getAllowedElements ( );
/**
 */
public String getElementIconName()
{
	return m_iconName;
}
/**
 */
public String getElementKey()
{
	return m_factoryKey;
}
/**
 */
public String getElementType()
{
	return m_type;
}
/**
 */
public String getIconResourceAnchor ( ) {
	return m_iconResourceAnchor;
}
/**
	Implementeras i subklassen och skall d�r se till att 'element' 
	tas bort fr�n 'parent'. 'element' �r av den klass som factoryklassen 
	svarar f�r. Via double-dispatching skickas detta vidare till factoryklassen 
	f�r 'parent'.<br>
 */
public abstract CoTreeCatalogElementIF removeElementFrom (CoTreeCatalogElementIF parent, CoTreeCatalogElementIF element);
}
