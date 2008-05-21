package com.bluebrim.formula.server;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.formula.shared.*;
import com.bluebrim.xml.shared.*;

// A variable properties

public abstract class CoAbstractVariable extends CoObject implements CoAbstractVariableIF {
	// a flag indicating if it is writeable or only readable
	protected boolean m_writeable;
	// the name of the variable
	protected String m_name = "";
	// a comment
	protected String m_comment = "";
	boolean m_dirtyFlag = true;



public CoAbstractVariable (String name, boolean writeable) {
	super();
	m_name = name;
	m_writeable = writeable;
}
public abstract Object bindValue (CoVariableBinderIF variableBinder) throws CoVariableBindingException;
public String getComment () {
	return m_comment;
}
public String getFactoryKey () {
	return "CoFormulaParserCoAbstractVariable";
}
public String getName () {
	return m_name;
}
public boolean isWriteable () {
	return m_writeable;
}
protected void markDirty () {
	// creates a notify
	m_dirtyFlag = !m_dirtyFlag;
}
public void setComment (String comment) {
	m_comment = comment;
	markDirty();
}
public void setName (String name) {
	m_name = name;
	markDirty();
}
public String toString () {
	return m_name;
}


public void xmlAddSubModel(String parameter,
                           Object subModel,
                           com.bluebrim.xml.shared.CoXmlContext context)
{
  if ("name".equals(parameter) && (subModel instanceof String))
  {
    m_name = (String)subModel;
  }
  else if ("comment".equals(parameter) && (subModel instanceof String))
  {
    setComment((String)subModel);
  }
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	m_writeable = CoModelBuilder.getBoolAttrVal(node.getAttributes(), "writeable", false);
}


public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
	visitor.exportString("name", m_name);
	visitor.exportString("comment", m_comment);
	visitor.exportAttribute("writeable", m_writeable ? "true" : "false");
}
}