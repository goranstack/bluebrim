package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

/**
 * Abstract superclass for all classes used to build an xtg parse tree
 * 
 * @author: Dennis
 */
 
public abstract class CoXtgParseNode
{
/**
 * CoXtgParseNode constructor comment.
 */
public CoXtgParseNode() {
	super();
}
void checkToken( CoXtgToken t, Class c ) throws CoXtgUnexpectedTokenParseException
{
	if
		( ! c.isInstance( t ) )
	{
		throw new CoXtgUnexpectedTokenParseException( t, c );
	}
}
public abstract void createXtg( PrintStream s );
public abstract void dump( String indent );
abstract void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l );
public abstract void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger l );
abstract boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException;
}
