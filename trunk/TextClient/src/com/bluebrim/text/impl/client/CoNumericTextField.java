package com.bluebrim.text.impl.client;


import com.bluebrim.base.shared.*;
import com.bluebrim.swing.client.*;

//

public class CoNumericTextField extends CoTextField
{
	private CoLengthUnitSet m_unitSet;
	
	private boolean m_useDefaultUnit = false;
public CoNumericTextField( CoLengthUnitSet us )
{
	super();

	m_unitSet = us;
}
public CoNumericTextField( CoLengthUnitSet us, int columns )
{
	super( columns );

	m_unitSet = us;
}
protected void fireActionPerformed()
{
	m_useDefaultUnit = true;
	super.fireActionPerformed();
	setText( getText() );
	m_useDefaultUnit = false;
}
public String getText()
{
	if
		( m_useDefaultUnit )
	{
		String s = super.getText();
		try
		{
			s = CoLengthUnitSet.format( CoLengthUnitSet.parse( s, m_unitSet ) );
		}
		catch ( java.text.ParseException ex )
		{
			return "";
		}
		return s;
	} else {
		return super.getText();
	}
}
public void setText( String s )
{
	try
	{
		double d = CoLengthUnitSet.parse( s );
		String str = CoLengthUnitSet.format(	d, m_unitSet );
		s = str;
	}
	catch ( java.text.ParseException ex )
	{
	}
	
	super.setText( s );
}
}
