package com.bluebrim.text.shared;
import java.rmi.*;

/**
 * Protocol of a custom hyphenation pattern.
 * 
 * @author: Dennis Malmstr�m
 */

public interface CoHyphenationPatternIF extends Remote
{
String getPattern();
void setPattern( String p );
}
