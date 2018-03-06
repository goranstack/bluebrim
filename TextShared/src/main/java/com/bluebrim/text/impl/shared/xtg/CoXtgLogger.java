package com.bluebrim.text.impl.shared.xtg;

/**
 * Protocol for objects that receive log messages from xtg import process.
 * 
 * @author: Dennis Malmström
 */
 
public interface CoXtgLogger
{
	public class DefaultLogger implements CoXtgLogger
	{
		public void log( String msg )
		{
			System.err.println( msg );
		}
	};
void log( String msg );
}
