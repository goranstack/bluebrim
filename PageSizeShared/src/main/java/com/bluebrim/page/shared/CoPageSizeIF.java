package com.bluebrim.page.shared;
import java.rmi.Remote;


import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.CoDimension;
import com.bluebrim.browser.shared.CoCatalogElementIF;
import com.bluebrim.system.shared.CoDistinguishable;
import com.bluebrim.xml.shared.CoXmlExportEnabledIF;

/**
 * Paper sizes by the ISO 216 standard
 * Some main applications of the most popular formats can be summarized as:
 * A0,A1 technical drawings, posters
 * A2,A3 drawings, diagrams, large tables
 * A4 letters, magazines, forms, catalogs, laser printer and copying machine
 * output
 * A5 note pads
 * A6 postcards
 * B5,A5,B6,A6 books
 * C4,C5,C6 envelopes for A4 letters: unfolded (C4), folded once (C5), folded
 * twice (C6)
 * B4,A3 newspapers, supported by most copying machines in addition to A4
 * B8,A8 playing cards
 * 
 */
public interface CoPageSizeIF extends CoCatalogElementIF, Remote, CoXmlExportEnabledIF, CoDistinguishable
{
	String ICON_NAME 			= "CoPageSize32.gif";
	String SMALL_ICON_NAME 			= "CoPageSize.gif";

	CoDimension BROADSHEET_SIZE = new CoDimension(CoLengthUnit.MM.from(397), CoLengthUnit.MM.from(560));
	CoDimension TABLOID_SIZE 	= new CoDimension(CoLengthUnit.MM.from(273.812f), CoLengthUnit.MM.from(393.7f));

	CoDimension SIZE_4A0 		= new CoDimension(CoLengthUnit.MM.from(1682),CoLengthUnit.MM.from(2378));
	CoDimension SIZE_2A0 		= new CoDimension(CoLengthUnit.MM.from(1189),CoLengthUnit.MM.from(1682));
	CoDimension A0_SIZE 		= new CoDimension(CoLengthUnit.MM.from(841),CoLengthUnit.MM.from(1189));
	CoDimension A1_SIZE 		= new CoDimension(CoLengthUnit.MM.from(594),CoLengthUnit.MM.from(841));
	CoDimension A2_SIZE 		= new CoDimension(CoLengthUnit.MM.from(420),CoLengthUnit.MM.from(594));
	CoDimension A3_SIZE 		= new CoDimension(CoLengthUnit.MM.from(297),CoLengthUnit.MM.from(420));
	CoDimension A4_SIZE 		= new CoDimension(CoLengthUnit.MM.from(210),CoLengthUnit.MM.from(297));
	CoDimension A5_SIZE 		= new CoDimension(CoLengthUnit.MM.from(148),CoLengthUnit.MM.from(210));
	CoDimension A6_SIZE 		= new CoDimension(CoLengthUnit.MM.from(105),CoLengthUnit.MM.from(148));
	CoDimension A7_SIZE 		= new CoDimension(CoLengthUnit.MM.from(74),CoLengthUnit.MM.from(105));
	CoDimension A8_SIZE 		= new CoDimension(CoLengthUnit.MM.from(52),CoLengthUnit.MM.from(74));
	CoDimension A9_SIZE 		= new CoDimension(CoLengthUnit.MM.from(37),CoLengthUnit.MM.from(52));
	CoDimension A10_SIZE 		= new CoDimension(CoLengthUnit.MM.from(26),CoLengthUnit.MM.from(37));

	String BROADSHEET 	= "Broadsheet";
	String TABLOID 		= "Tabloid";
	String A4 					= "A4";
	String A3 					= "A3";

	
/**
 */
public float getHeight();
public String getName();
/**
 */
public CoDimension getSize();
/**
 */
public float getWidth();
public void setName(String name);
/**
 */
public void setSize(float width, float height );
}