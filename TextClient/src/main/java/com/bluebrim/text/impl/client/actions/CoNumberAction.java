package com.bluebrim.text.impl.client.actions;

import java.text.*;
import java.util.*;

import com.bluebrim.base.shared.*;



/**
 * Abstract superclass of all text editor actions that set text attribute numerical values.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoNumberAction extends CoAttributeAction
{
	protected CoLengthUnitSet m_unitSet;

	protected static final Format m_converter = NumberFormat.getInstance( Locale.getDefault() );
public CoNumberAction( Object attribute, String actionName )
{
	super( attribute, actionName );
}
public CoNumberAction( Object attribute, String actionName, CoLengthUnitSet us )
{
	super( attribute, actionName );

	m_unitSet = us;
}
}
