package com.bluebrim.browser.shared;

import java.io.*;
import java.util.*;

import com.bluebrim.base.shared.*;

/**
	Abstrakt implementering av CoTreeCatalogElementIF vars konkreta subklasser 
	skall representera ett CoCatalogElementIF i en nod i en trädvy. 
	Den kan antingen definiera ett eget namn, ikon och 'iconLoaderClass' 
 	eller använda värden från sitt 'catalogElement'.
 	Ett TreeCatalogElementproxy kan användas antingen som protokoll omvandlare för 
 	ett katalogelementet eller som en mera traditionell proxy för ett element som ligger 
 	på servern och inte skall accessas förrän man vill göra något mer med det än att bara 
 	visa upp det i en trädstruktur.
	Instansvariabler:
	<ul>
 	<li>catalogElement	- CoCatalogElementIF (det objekt den representerar)
 	<li>iconLoaderClass	- String, klassnamnet för den klass som skall användas för att hitta fram till den bildfil som används som ikon.
  	<li>iconName 		- String , namnet för dess ikon relativt den mapp som erhålls av iconLoaderClass, ex "com.bluebrim.publication.impl.server.CoPublication.gif" - kan vara null
 	<li>name				- String, kan vara null.
 	</ul>
 */
public abstract class CoAbstractTreeCatalogElementProxy extends CoObject implements  CoTreeCatalogElementIF, Serializable {
	protected CoCatalogElementIF 	m_element;
	protected String 		m_iconName;
	protected String 		m_name;
	protected String 		m_iconResourceAnchor;
	protected String		m_type;
	protected String 		m_factoryKey;
/**
 * This method was created by a SmartGuide.
 */




public CoAbstractTreeCatalogElementProxy ( ) {
}
/**
 */
public CoAbstractTreeCatalogElementProxy (CoCatalogElementIF element,String iconName, String name, String iconResourceAnchor, String type, String factoryKey ) {
	m_element 				= element;
	m_iconName 				= iconName;
	m_name					= name;
	m_iconResourceAnchor	= iconResourceAnchor;
	m_type					= type;
	m_factoryKey			= factoryKey;
}
/**
 */
public CoAbstractTreeCatalogElementProxy (CoCatalogElementIF catalogElement ) {
	this(	catalogElement,
			catalogElement.getSmallIconName(),
			catalogElement.getIdentity(), 
			catalogElement.getIconResourceAnchor(), 
			catalogElement.getType(),
			catalogElement.getFactoryKey());
}
public int getChildCount()
{
	List elements = getElements();
	return elements != null ? elements.size() : 0;
}
/**
 */
public String getFactoryKey () {
	return m_factoryKey;
}
/**
 */
public String getIconName () {
	return getSmallIconName();
}
public String getIconResourceAnchor()
{
	return m_iconResourceAnchor;
}
/**
 */
public String getIdentity () {
	return m_name;
}
/**
 */
public String getSmallIconName () {
	return m_iconName;
}
public CoTreeCatalogElementIF getTreeCatalogElement ()
{
	return (m_element instanceof CoTreeCatalogElementIF) ? (CoTreeCatalogElementIF )m_element : this;
}
/**
 */
public String getType () {
	return m_type;
}
public boolean isLeafElement()
{
	return getChildCount() == 0;
}
}
