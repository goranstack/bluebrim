package com.bluebrim.layout.impl.server.geom;
import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.server.*;
import com.bluebrim.layout.impl.shared.*;

/**
 * Implementation of none run around specification.
 * 
 * @author: Dennis Malmström
 */

public class CoNoneRunAroundSpec extends CoRunAroundSpec implements CoNoneRunAroundSpecIF
{
	// xml tags
	public static final String XML_TAG = "none-run-around-spec";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-25
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoNoneRunAroundSpec();
}

/**
 * doUseStroke method comment.
 */
public boolean doUseStroke() {
	return false;
}


public boolean equals( Object s )
{
	return s instanceof CoNoneRunAroundSpec;
}


public double getBottomMargin()
{
	return 0;
}


public String getFactoryKey()
{
	return NONE_RUN_AROUND_SPEC;
}


public double getLeftMargin()
{
	return 0;
}


public double getRightMargin()
{
	return 0;
}


public CoImmutableShapeIF getRunAroundShape( CoShapePageItem pi )
{
	return null;
}


public double getTopMargin()
{
	return 0;
}


/**
 * setUseStroke method comment.
 */
public void setUseStroke(boolean b) {}


public String xmlGetTag()
{
	return XML_TAG;
}
}