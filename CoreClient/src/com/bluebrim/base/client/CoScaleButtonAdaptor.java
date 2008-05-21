package com.bluebrim.base.client;

// JDK imports
import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.gui.client.*;



/**
 * Subklass till CoButtonAdaptor som multiplicerar värdemodellen med ett givet tal.
 * CoDeltaButtonAdaptor kan hantera följande värdetyper: Long, Integer, Short, Byte
 * Float, Double och String. I fallet String förutsätts strängen innehålla ett heltal.
 */
public class CoScaleButtonAdaptor extends CoButtonAdaptor
{
	double m_factor = 1;

/**
 * Konstruktor.
 * @param aValueModel värdemodellen
 * @param button knappen
 * @param factor faktorn som värdemodellen multiplicera med
 */
public CoScaleButtonAdaptor( CoValueable aValueModel, AbstractButton button, double factor )
{
	super( aValueModel, button );
	setFactor( factor );
}
/**
 * Addera deltavärdet till värdemodellens värdeobjekt.
 */
public void actionPerformed( ActionEvent e )
{
		// hämta värdeobjektet och dess klass
	Object value = getValueModel().getValue();
	Class  valueClass = value.getClass();
	
		// multiplicera värdeobjektet med faktorn
	if
		( valueClass == Integer.class )
	{
		value = new Integer( (int) ( ( (Integer) value ).intValue() * m_factor ) );
	} else if
		( valueClass == Float.class )
	{
		value = new Float( (float) ( ( (Float) value ).floatValue() * m_factor ) );
	} else if
		( valueClass == Double.class )
	{
		value = new Double( (double) ( ( (Double) value ).doubleValue() * m_factor ) );
	} else if
		( valueClass == String.class )
	{
		value = Integer.toString( (int) ( Integer.valueOf( (String) value ).intValue() * m_factor ) );
	} else if
		( valueClass == Long.class )
	{
		value = new Long( (long) ( ( (Long) value ).longValue() * m_factor ) );
	} else if
		( valueClass == Short.class )
	{
		value = new Short( (short) ( ( (Short) value ).shortValue() * m_factor ) );
	} else if
		( valueClass == Byte.class )
	{
		value = new Byte( (byte) ( ( (Byte) value ).byteValue() * m_factor ) );
	} else {
		// okänd typ, gör ingenting
	}	
	
		// lägg in det nya värdeobjektet i värdemodellen
	getValueModel().setValue( value );
}
/**
 * Access-metod till multiplikationsfaktorn.
 * @return multiplikationsfaktorn
 */
public double getFactor()
{
	return m_factor;
}
/**
 * Access-metod för att byta multiplikationsfaktorn.
 * @param f den nya multiplikationsfaktorn
 */
public double setFactor( double f )
{
	return m_factor = f;
}
}
