package com.bluebrim.content.impl.server.xml;
import org.w3c.dom.*;

import com.bluebrim.text.impl.shared.xml.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * 
 * Creation date: (2001-04-19 10:17:43)
 * @author: Dennis
 */

public abstract class CoContentModelBuilder extends CoSimpleModelBuilder
{

/**
 * CoContentModelBuilder constructor comment.
 * @param parser CoXmlParserIF
 */
public CoContentModelBuilder(CoXmlParserIF parser) {
	super(parser);
}


/**
 * This method substitutes all <code>&lt;/p></code> with <code>\n&lt;/p></code>
 * except the last one, which is untouched.
 * <p>
 * This is done here to accomodate the string format excpected by the styled
 * document.
 *
 * @return The <code>base</code> string with the above substitutions.
 */
private String augmentParagraphs(String base) {
	if (base == null)
	{
		base = "";
	}
	
	String search = "</p>";
	String replace = "\n</p>";

	// First do replace on substring
	String trailer = "";
	if(base.indexOf(search) != -1) {
		int lastOccurence = base.lastIndexOf(search);
		trailer = base.substring(lastOccurence);
		base = base.substring(0, lastOccurence);
	}

	StringBuffer buf = new StringBuffer();
	int i = 0;
	int j = 0;
	int sl = search.length();
	while (j > -1) {
		j = base.indexOf(search, i);
		if (j > -1) {
			buf.append(base.substring(i, j));
			buf.append(replace);
			i = j + sl;
		}
	}
	buf.append(base.substring(i, base.length()));
	// Add the trailing data
	buf.append(trailer);
	return buf.toString();
}





/**
 * From the string <code>text</code>, if it contains at least two
 * <code>#</code> characters, return the substring between them.
 * Otherwise, return the original string.
 */
private String trimData(String text)
{
	if (text == null)
	{
		return null;
	}
	
	int fIdx = text.indexOf("#") + 1;
	int lIdx = text.lastIndexOf("#");
	if (fIdx == -1 || lIdx == -1 || (fIdx > lIdx))
	{
		return text;
	}
	
	return text.substring(fIdx, lIdx);
}

protected final CoFormattedText createFormattedTextFrom( Node node )
{
	try
	{
		CoXmlTextImporter importer = new CoXmlTextImporter();
		return new CoFormattedText( importer.doImport( node, null ) );
	}
	catch ( CoXmlReadException ex )
	{
		return null;
	}
}
}