package com.bluebrim.text.impl.client.actions;

import java.util.*;

import javax.swing.*;

/**
 * Text editor action utilities.
 * 
 * @author: Dennis Malmström
 */
 
public class CoActionUtilities
{
// Put actions in Map

public static Map actionsToHashtable( Action[] actions ) // [ String -> Action ]
{
	Map table = new HashMap();
	if 
		( actions != null )
	{
		for
			( int i = 0; i < actions.length; i++ )
		{
			Action a = actions[ i ];
			table.put( a.getValue( Action.NAME ), a );
		}
	}
	
	return table;
}
}
