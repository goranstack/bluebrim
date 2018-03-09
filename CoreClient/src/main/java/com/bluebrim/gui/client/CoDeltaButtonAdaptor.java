package com.bluebrim.gui.client;

// JDK imports
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;



/**
 * Subklass till CoButtonAdaptor som adderar ett givet heltal till v�rdemodellen.
 * CoDeltaButtonAdaptor kan hantera f�ljande v�rdetyper: Long, Integer, Short, Byte
 * Float, Double och String. I fallet String f�ruts�tts str�ngen inneh�lla ett heltal.
 */
public class CoDeltaButtonAdaptor extends CoButtonAdaptor
{
	int m_delta = 1;

/**
 * Konstruktor.
 * @param aValueModel v�rdemodellen
 * @param button knappen
 * @param delta heltalsv�rdet som adderas till v�rdemodellen
 */
public CoDeltaButtonAdaptor( CoValueable aValueModel, AbstractButton button, int delta )
{
	super( aValueModel, button );
	setDelta( delta );
}
/**
 * Addera deltav�rdet till v�rdemodellens v�rdeobjekt.
 */
public void actionPerformed( ActionEvent e )
{
		// h�mta v�rdeobjektet och dess klass
	Object value = getValueModel().getValue();
	Class  valueClass = value.getClass();
	
		// addera deltav�rdet
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
		// ok�nd typ, g�r ingenting
	}	
	
		// l�gg in det nya v�rdeobjektet i v�rdemodellen
	getValueModel().setValue( value );
}
/**
 * Access-metod till deltav�rdet.
 * @return deltav�rdet
 */
public int getDelta()
{
	return m_delta;
}
/**
 * Access-metod f�r att byta deltav�rde.
 * @param d det nya deltav�rdet
 */
public int setDelta( int d )
{
	return m_delta = d;
}
}
