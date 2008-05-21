package com.bluebrim.text.shared;
import javax.swing.text.*;

/**
 * Text and document utilities
 * 
 * @author: Dennis Malmström
 */

public class CoTextUtilities
{
	public static Segment TMP_Segment = new Segment();
  /**
   * @param c en bokstav.
   * @return sanningsvärde som talar om ifall c är en konsonant eller ej.
   */
  static final boolean isConsonant( char c )
	{
	  if ( ! Character.isLetter( c ) ) return false;
	  return ! isWovel( c );
	}
  /**
   * @param c en bokstav.
   * @return sanningsvärde som talar om ifall c är en vokal eller ej.
   */
  static final boolean isWovel( char c )
	{
	  if ( ! Character.isLetter( c ) ) return false;
	  return ( ( c == 'e' ) ||
			   ( c == 'y' ) ||
			   ( c == 'u' ) ||
			   ( c == 'i' ) ||
			   ( c == 'o' ) ||
			   ( c == 'a' ) ||
			   ( c == 'å' ) ||
			   ( c == 'ö' ) ||
			   ( c == 'ä' ) );
	}
}
