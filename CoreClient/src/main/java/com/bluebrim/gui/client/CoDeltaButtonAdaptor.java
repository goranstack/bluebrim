package com.bluebrim.gui.client;

// JDK imports
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;



/**
 * Subklass till CoButtonAdaptor som adderar ett givet heltal till värdemodellen.
 * CoDeltaButtonAdaptor kan hantera följande värdetyper: Long, Integer, Short, Byte
 * Float, Double och String. I fallet String förutsätts strängen innehålla ett heltal.
 */
public class CoDeltaButtonAdaptor extends CoButtonAdaptor
{
	int m_delta = 1;

/**
 * Konstruktor.
 * @param aValueModel värdemodellen
 * @param button knappen
 * @param delta heltalsvärdet som adderas till värdemodellen
 */
public CoDeltaButtonAdaptor( CoValueable aValueModel, AbstractButton button, int delta )
{
	super( aValueModel, button );
	setDelta( delta );
}
/**
 * Addera deltavärdet till värdemodellens värdeobjekt.
 */
public void actionPerformed( ActionEvent e )
{
		// hämta värdeobjektet och dess klass
	Object value = getValueModel().getValue();
	Class  valueClass = value.getClass();
	
		// addera deltavärdet
	if
		( valueClass == Integer.class )
	{
		value = new Integer( ( (Integer) value ).intValue() + m_delta );
	} else if
		( valueClass == Float.class )
	{
		value = new Float( ( (Float) value ).floatValue() + m_delta );
	} else if
		( valueClass == Double.class )
	{
		value = new Double( ( (Double) value ).doubleValue() + m_delta );
	} else if
		( valueClass == String.class )
	{
		value = Integer.toString( Integer.valueOf( (String) value ).intValue() + m_delta );
	} else if
		( valueClass == Long.class )
	{
		value = new Long( ( (Long) value ).longValue() + m_delta );
	} else if
		( valueClass == Short.class )
	{
		value = new Short( (short) ( ( (Short) value ).shortValue() + m_delta ) );
	} else if
		( valueClass == Byte.class )
	{
		value = new Byte( (byte) ( ( (Byte) value ).byteValue() + m_delta ) );
	} else {
		// okänd typ, gör ingenting
	}	
	
		// lägg in det nya värdeobjektet i värdemodellen
	getValueModel().setValue( value );
}
/**
 * Access-metod till deltavärdet.
 * @return deltavärdet
 */
public int getDelta()
{
	return m_delta;
}
/**
 * Access-metod för att byta deltavärde.
 * @param d det nya deltavärdet
 */
public int setDelta( int d )
{
	return m_delta = d;
}
}
