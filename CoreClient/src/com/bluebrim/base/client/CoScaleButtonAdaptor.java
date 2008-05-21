package com.bluebrim.base.client;

// JDK imports
import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.gui.client.*;



/**
 * Subklass till CoButtonAdaptor som multiplicerar v�rdemodellen med ett givet tal.
 * CoDeltaButtonAdaptor kan hantera f�ljande v�rdetyper: Long, Integer, Short, Byte
 * Float, Double och String. I fallet String f�ruts�tts str�ngen inneh�lla ett heltal.
 */
public class CoScaleButtonAdaptor extends CoButtonAdaptor
{
	double m_factor = 1;

/**
 * Konstruktor.
 * @param aValueModel v�rdemodellen
 * @param button knappen
 * @param factor faktorn som v�rdemodellen multiplicera med
 */
public CoScaleButtonAdaptor( CoValueable aValueModel, AbstractButton button, double factor )
{
	super( aValueModel, button );
	setFactor( factor );
}
/**
 * Addera deltav�rdet till v�rdemodellens v�rdeobjekt.
 */
public void actionPerformed( ActionEvent e )
{
		// h�mta v�rdeobjektet och dess klass
	Object value = getValueModel().getValue();
	Class  valueClass = value.getClass();
	
		// multiplicera v�rdeobjektet med faktorn
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
		// ok�nd typ, g�r ingenting
	}	
	
		// l�gg in det nya v�rdeobjektet i v�rdemodellen
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
 * Access-metod f�r att byta multiplikationsfaktorn.
 * @param f den nya multiplikationsfaktorn
 */
public double setFactor( double f )
{
	return m_factor = f;
}
}
