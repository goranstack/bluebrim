package com.bluebrim.gui.client;


/**
 	Abstrakt implementering av CoValueable som ej kan läsas. Saknar stöd för att registrera och meddela objekt
	som är intresserade av ändringar av dess värde. Metoden för att sätta värdet är abstrakt.<br>
	Instansvariabler
	<ul>
	<li>	name (String)för värdeobjektets namn
	</ul>
	@author Dennis Malmström 990407
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
