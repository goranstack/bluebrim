package com.bluebrim.base.shared.debug;

import java.lang.reflect.*;

/**
 * This interface must be implemented by any class that should be compared
 * using <code>CoDiff</code>.  It is used for getting around access restrictions
 * (private, friendly, protected) in the <code>CoDiffable</code> classes.
 * <p>
 * Creation date: (2001-03-06 10:44:40)
 * @see CoDiff
 * @author: Dennis
 */

public interface CoDiffable
{
	/**
	 * Implementation for classes directly implementing this interface:
	 * <pre>	
	 * 	public Object getAttributeValue( java.lang.reflect.Field d ) throws IllegalAccessException
	 * 	{
	 * 		return d.get( this );
	 * 	}
	 * </pre>
	 * <p>
	 * Implementation for subclasses of a class implementing this interface:
	 * <pre>
	 * 	public Object getAttributeValue( java.lang.reflect.Field d ) throws IllegalAccessException
	 * 	{
	 * 		try
	 * 		{
	 * 			return d.get( this );
	 * 		}
	 * 		catch ( IllegalAccessException ex )
	 * 		{
	 * 			return super.getAttributeValue( d );
	 * 		}
	 * 	}
	 * </pre>
	 */
	
	/*friendly*/ Object getAttributeValue( Field d ) throws IllegalAccessException;
}