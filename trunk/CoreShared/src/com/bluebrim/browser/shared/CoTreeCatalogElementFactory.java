package com.bluebrim.browser.shared;

import com.bluebrim.base.shared.*;
/**
 	En abstrakt superklass till de klasser som skall fungera
 	som factoryklasser för implementationer av CoTreeCatalogElementIF. <br>
 	En konkret subklass av CoTreeCatalogElementFactory kan svara på
 	vilka klasser som kan fungera som element för dess katalogelementklass.<br> 
 	En factoryklass ansvarar också för att lägga till nya "barn" till en
 	instans av sin klass. Den mekanism som används för detta bygger på double-dispatching
 	och kräver (tyvärr) ett gemensamt interface för alla factoryklasser vars
 	element ingår i samma trädstruktur. Detta interface måste definiera 
 	en addElementTo(Object parent XXXXX element)metod för varje klass XXX  som kan
 	ingå i strukturen. <br>
 	Av praktiska skäl är det bäst att skapa en gemensam superklass
 	som implementerar stubbar för alla dessa metoder. På så sätt kan de faktiska
 	factoryklasserna sedan implementera endast den eller de add-metoder de själva behöver.<br>
 	För ett exempel se CoPressModelFactory.
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
	läggas till 'parent'. Detta är factoryklassens ansvar.
 */
public CoTreeCatalogElementIF addElement(CoTreeCatalogElementIF parent, CoAddElementData elementData)
{
	CoTreeCatalogElementFactoryIF tFactory = (CoTreeCatalogElementFactoryIF )CoFactoryManager.getFactory(elementData.getElementType());
	return tFactory.addNewElementTo(parent, elementData.getElementData());
}
/**
	Implementeras i subklassen och skall där se till att 'parent'
	får ett nytt "barn" av den klass som factoryklassen svarar för.
	Skall skapa ett nytt element och via double-dispatching skicka
	detta vidare till factorklassen för 'parent'.<br>
	Se exempelvis CoSupportedPaperSizeFactory.
	@see CoSupportedPaperSizeFactory#addNewElementTo  
 */
public abstract CoTreeCatalogElementIF addNewElementTo (CoTreeCatalogElementIF parent, Object elementData);
/**
	Subklassen implementerar och svarar med en String[] där varje 
	element är det fullständiga namnet på en klass vars instanser 
	kan vara "barn" till fabrikens katalogelement. 
	Svara med null om elementen utgör löv i strukturen.
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
	Implementeras i subklassen och skall där se till att 'element' 
	tas bort från 'parent'. 'element' är av den klass som factoryklassen 
	svarar för. Via double-dispatching skickas detta vidare till factoryklassen 
	för 'parent'.<br>
 */
public abstract CoTreeCatalogElementIF removeElementFrom (CoTreeCatalogElementIF parent, CoTreeCatalogElementIF element);
}
