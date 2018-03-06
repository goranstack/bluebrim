package com.bluebrim.text.shared;
import javax.swing.text.*;

/**
 * Text and document utilities
 * 
 * @author: Dennis Malmstr�m
 */

public class CoTextUtilities
{
	public static Segment TMP_Segment = new Segment();
  /**
   * @param c en bokstav.
   * @return sanningsv�rde som talar om ifall c �r en konsonant eller ej.
   */
  static final boolean isConsonant( char c )
	{
	  if ( ! Character.isLetter( c ) ) return false;
	  return ! isWovel( c );
	}
  /**
   * @param c en bokstav.
   * @return sanningsv�rde som talar om ifall c �r en vokal eller ej.
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
			   ( c == '�' ) ||
			   ( c == '�' ) ||
			   ( c == '�' ) );
	}
}
