package com.bluebrim.xml.impl.shared;

/**
 *  Opposite of com.bluebrim.xml.shared.CoXmlParserIF. Interface implemented by all model parsers
 */
public interface CoModelParserIF {
/**
 * Traverses an object hierarchy starting with the 'model' object
 * @return java.lang.Object
 * @param model java.lang.Object
 */
public Object traverse(Object model);
}
