package com.bluebrim.gui.client;


/**
 	Abstrakt implementering av CoValueable som ej kan l�sas. Saknar st�d f�r att registrera och meddela objekt
	som �r intresserade av �ndringar av dess v�rde. Metoden f�r att s�tta v�rdet �r abstrakt.<br>
	Instansvariabler
	<ul>
	<li>	name (String)f�r v�rdeobjektets namn
	</ul>
	@author Dennis Malmstr�m 990407
 */
public abstract class CoWriteOnlyValueModel extends CoValueModel
{
/**
 */
protected CoWriteOnlyValueModel()
{
}
/**
 * This method was created by a SmartGuide.
 */
public CoWriteOnlyValueModel(String name)
{
	super(name);
}
public Object getValue()
{
	return null;
}
}
