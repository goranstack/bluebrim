package com.bluebrim.text.impl.shared.xml;

import java.util.*;

import javax.swing.text.*;
import javax.swing.text.AttributeSet;

import org.apache.crimson.tree.*;
import org.w3c.dom.*;

import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 *	Exports a com.bluebrim.text.shared.CoStyledDocument as XML. The XML is added to the node that is supplied
 *  to the constructor.
 *
 */
public class CoXmlTextExporter extends CoAbstractTextExporter {


	// The XML document which is the context
	private XmlDocument m_xmlDocument;




public CoXmlTextExporter (XmlDocument doc, Node aNode) {
	m_xmlDocument = doc;
	m_baseNode = aNode;
}
public void doExport(com.bluebrim.text.shared.CoStyledDocumentIF doc) throws com.bluebrim.xml.shared.CoXmlWriteException
{
	export(doc);
}
private void export(com.bluebrim.text.shared.CoStyledDocumentIF doc) throws com.bluebrim.xml.shared.CoXmlWriteException
{
  javax.swing.text.Element section = doc.getRootElements()[ 0 ];
  
  int paragraphCount = section.getElementCount();
  for(int idx = 0; idx < paragraphCount; idx++) {
	  exportParagraph(section.getElement(idx));
  }
}
	/**
	 *	An element represents an area within which one or more formats are
	 *  defined. This is represented in XML with formatting nodes contained
	 *  in eachother. Example: the element bold, italic (23, 92) indicates that
	 *  the characters between indices 23 and 92 are bold and italic. This is represented in XML as:
	 *  <b><i>text between 23 and 92</i></b>
	 *	
	 */
private Node exportElement(javax.swing.text.Element el) throws com.bluebrim.xml.shared.CoXmlWriteException
{
	// An element can have nested formattings
	AttributeSet as = el.getAttributes();
	Enumeration enumer = as.getAttributeNames();
	Node currentNode = null;
	Node topNode = null;


	// Add the character tag if there is one (only first iteration)
	String charTag = (String) as.getAttribute(CoTextConstants.CHARACTER_TAG );
	if
		( charTag != null )
	{
		currentNode = topNode = m_xmlDocument.createElement( "ctag" );
		( (ElementNode) currentNode ).setAttribute( "style", charTag );
	}
	
	while
		(enumer.hasMoreElements())
	{
		Object attr = enumer.nextElement();

		CoAttributeTranslator translator = CoAttributeTranslator.getTranslator(attr);
		if ( translator == null ) continue;

		// Create subnodes in the XML representation to represent style
		Node aNode = makeSubFormattingNode(attr, as, translator);
		if
			(currentNode != null)
		{
			currentNode.appendChild(aNode);
		} else {
			topNode = aNode;
		}
		currentNode = aNode;
	}
	
	// Add the text to the current node
	try {
		String text = el.getDocument().getText(el.getStartOffset(), el.getEndOffset() - el.getStartOffset());
		// Don't export line feeds
		text = stripLineFeed(text);
		Node tNode = null;
		
		tNode = m_xmlDocument.createTextNode(text);
		
		if
			(currentNode != null)
		{
			currentNode.appendChild(tNode);
		} else {
			// No formatting node. Export as plain text node
			topNode = tNode;
		}
			
	}
		catch(BadLocationException ble)
	{
		ble.printStackTrace(System.out);
		throw new com.bluebrim.xml.shared.CoXmlWriteException("Error when exporting text: " + ble.toString());
	}
	
	return topNode;
}
/**
 *	Exports a paragraph. The paragraph format is exported as the 'class' attribute
 *  for a paragraph node to adhere to the html(xhtml) css standard.
 */
private void exportParagraph(javax.swing.text.Element paragraph) throws com.bluebrim.xml.shared.CoXmlWriteException
{
	Node currentNode = null;
	
	// paragraph tag
	if
		(paragraph == null)
	{
		return;
	}

	AttributeSet paragraphAtts = paragraph.getAttributes();
	String paragraphTag = (String) paragraphAtts.getAttribute(CoTextConstants.PARAGRAPH_TAG);
	// We use the html (xhtml) tag name for paragraphs. The style name is supplied as the class attribute
	if (m_xmlDocument != null) {
		currentNode = m_xmlDocument.createElement("p");
	} else {
		currentNode = new ElementNode("p");
	}
	
	if (paragraphTag != null)
	{
		((ElementNode)currentNode).setAttribute("class", makeValidNodeName(paragraphTag));
	}

	
	// Iterate through the paragraph attributes
	java.util.Enumeration e = paragraphAtts.getAttributeNames();
	while	
		( e.hasMoreElements() )
	{
		Object name = e.nextElement();

		CoAttributeTranslator translator = CoAttributeTranslator.getTranslator( name );
		if ( translator == null ) continue;
		
		Object value = paragraphAtts.getAttribute( name );
		if ( value == null ) continue;

		( (ElementNode) currentNode ).setAttribute( translator.getTag(), (String) CoXmlTextConstants.getMappedForText( translator.value2string( value ) ) );
	}

	/*

	// Inner class overriding the writeXml method to avoid pretty printing of the XML representation of the text
	// since pretty printing adds unnecessary white space to the XML

	// FIXME: The pretty printing problem must be resolved.
	
	Node nodeWrapper = new ElementNode()
		{
			Node m_node;

			{
				m_node = CoXmlTextExporter.this.currentNode;
			}
			
			public void writeXml(XmlWriteContext context) throws IOException
			{
				XmlWriteContext wContext = CoXmlTextExporter.this.m_xmlDocument.createWriteContext(context.getWriter());
				((ElementNode)m_node).writeXml(wContext);
			}
		};

	m_node.appendChild(nodeWrapper);
	*/

	if (currentNode != null)
	{
		Iterator iter = resolveCharacterAttributes(paragraph);	
		while
			(iter.hasNext())
		{
			Node childNode = (Node)iter.next();
			if
				(childNode != null)
			{
				currentNode.appendChild(childNode);
			}
		}
	}

	m_baseNode.appendChild(currentNode);
}


/**
 * Handles the representation of font attributes such as face, size, color, shade.
 */
private Node handleFont(String tag, Object val)
{
	Node aNode = null;
	String attrName = null;
	
	if
		(tag.equals("font-family"))
	{
		attrName = "face";
	} else if
		(tag.equals("font-size"))
	{
		attrName = "size";
	} else if
		(tag.equals("font-color"))
	{
		attrName = "color";
		//		m_usedColors.add(val.toString());
	} else if
		(tag.equals("font-shade"))
	{
		attrName = "shade";
	}
	
	if
		(attrName != null)
	{
		aNode = m_xmlDocument.createElement("font");
		((ElementNode) aNode).setAttribute(attrName, val.toString());
	}
	
	return aNode;
}

/**
 *	Creates XML nodes representing StyledDocument formatting. Some conversion depending on what attribute
 *  is being processed is made in order to make the XML format as we want it
 */
private Node makeSubFormattingNode(Object attr, AttributeSet as, CoAttributeTranslator translator) throws com.bluebrim.xml.shared.CoXmlWriteException
{
	Object val = null;
	
	try
	{
		val = as.getAttribute(attr);
	}
	catch (Exception e)
	{
		throw new com.bluebrim.xml.shared.CoXmlWriteException("Failed getting attribute value for: " + translator + ", " + attr.toString());
	}
	
	// Here we handle font related subnodes, which we want to format in a specific way for the XML representation
	String tag = translator.getTag();
	
	Node aNode = handleFont(tag, val);
	
	if 
		( aNode == null )
	{
		// In the XML format, we have a system of tag and un_tag (e.g. b and un_b) where the un_-tag represents
		// the 'inverse' of its associated tag. This is used to override a paragraph formatting for characters.
		// Example is when a paragraph is bold by definition and certain chars within the paragraph are defined as
		// 'unbold' thus overriding the bold property of the paragraph formatting
		aNode = m_xmlDocument.createElement( ( Boolean.FALSE.equals( val ) ? "un_" : "" ) + translator.getTag());

		if
			(!(val instanceof Boolean))
		{
			((ElementNode) aNode).setAttribute( "val", translator.value2string( val ) );
		}
	}
	
	return aNode;
}
	private String makeValidNodeName(String name) {
		if(1==1) return name;
		// We have to discuss what kinds of names we should allow
		StringBuffer sb = new StringBuffer();
		int top = name.length();
		for(int idx = 0; idx < top; idx++) {
			char ch = name.charAt(idx);
			switch(ch) {
				case 'å': ch = 'a'; break;
				case 'ä': ch = 'a'; break;
				case 'ö': ch = 'o'; break;
				case 'Å': ch = 'A'; break;
				case 'Ä': ch = 'A'; break;
				case 'Ö': ch = 'O'; break;
			}
			if(ch != ' ') {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
/**
 *	Resolves the hierarchial representation of styled document character attributes into
 *  a corresponding XML structure 
 */
private Iterator resolveCharacterAttributes(javax.swing.text.Element el) throws com.bluebrim.xml.shared.CoXmlWriteException
{
	int start = el.getStartOffset();
	int end = el.getEndOffset();
	int elCount = el.getElementCount();
	ArrayList nodes = new ArrayList();

	int lastElementEnd = start;

	for
		(int idx = 0; idx < elCount; idx++)
	{
		javax.swing.text.Element sEl = el.getElement(idx);
		nodes.add( exportElement( sEl ) );
		lastElementEnd = sEl.getEndOffset();
	}
	return nodes.iterator();
}
	private String stripLineFeed(String text) {
		if(text.endsWith("\n")) {
			int len = text.length();
			if(len == 1) {
				return "";
			}
			return text.substring(0, len - 1);
		}
		return text;
	}

	// The node below which the XML representation of the text should be put
	private Node m_baseNode;
}