package com.bluebrim.xml.shared;
import java.util.*;

import org.w3c.dom.*;
import org.xml.sax.*;

public interface CoXmlParserIF {
public void registerBuilder(String element, Class aBuilder);	

	public CoModelBuilderIF getBuilder();


	public HashMap getCreatedModels();


	public void initializeFrom(CoXmlParserIF parser);


public Node pullNode();


/**
 * Let the parser register itself in the cache if it is reusable.
 */
public void registerInCache(HashMap cache, String element);


	public void setBuilder(CoModelBuilderIF aBuilder);


/**
 * Let the parser know about the cache.
 */
public void setParserCache(HashMap cache);


public Object traverse(Object superModel, Node startNode, com.bluebrim.xml.shared.CoXmlContext context)
	throws SAXParseException, com.bluebrim.xml.shared.CoXmlReadException;
}