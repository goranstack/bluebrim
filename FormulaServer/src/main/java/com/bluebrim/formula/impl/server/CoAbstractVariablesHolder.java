package com.bluebrim.formula.impl.server;
import java.beans.*;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.formula.shared.*;

/*
 * Takes care of the variables to use in CoFormula
 *
 * m_variables - holds all variables that are editable
 * m_variablesOnlyReadable - holds all variables that are not editable
 * m_dirtyFlag - is set every time a notify must be sent
 * m_listenerList - for the objects listening to changes on variable names String[] (getNames())
 * m_name - för värdeobjektets namn
 */
 
public abstract class CoAbstractVariablesHolder extends CoObject implements CoAbstractVariablesHolderIF {
	// stores all instances of CoVariableIF
	protected List 		m_variables 				= new ArrayList();
	protected List 		m_variablesOnlyReadable 	= new ArrayList();
	protected boolean 	m_dirtyFlag 				= true;
	
	protected String 	m_name 						= CO_ABSTRACT_VARIABLES_HOLDER_VALUEABLE;
	protected CoAbstractVariablesHolderIF m_parent = null;
public CoAbstractVariablesHolderIF getParent() {
	return m_parent;
}	

public CoAbstractVariablesHolder() {
	this(null);
}


public CoAbstractVariablesHolder(CoAbstractVariablesHolderIF parent) {
	super();
	m_parent = parent;
}


private int _getSize () {
	return m_variables.size() + m_variablesOnlyReadable.size();
}


public abstract void add ();


public void addVariable(CoAbstractVariableIF v)
{
	if (v != null)
	{
		if (v.isWriteable())
			m_variables.add(v);
		else
			m_variablesOnlyReadable.add(v);
		markDirty();
	}
}


protected String createUniqueName () {
	String s =  CoFormulaResources.getName(CoFormulaResources.DEFAULT_VARIABLE_NAME);
	
	int i = 0;
	while (!isLegal(s + i))
		i++;
			
	return s + i;
}


public String getFactoryKey () {
	return "CoFormulaParserCoAbstractVariablesHolder";
}


public String[] getNames(boolean inherited)
{
	String[] parentNames = null;
	if(!isRoot(inherited)) {
		parentNames = m_parent.getNames(inherited);
	}
	String[] names = new String[m_variables.size() + m_variablesOnlyReadable.size()];
	for (int i = 0; i < m_variablesOnlyReadable.size(); i++)
	{
		CoAbstractVariableIF v 	= (CoAbstractVariableIF) m_variablesOnlyReadable.get(i);
		names[i] 				= v.getName();
	}
	for (int i = 0; i < m_variables.size(); i++)
	{
		CoAbstractVariableIF v 						= (CoAbstractVariableIF) m_variables.get(i);
		names[i + m_variablesOnlyReadable.size()] 	= v.getName();
	}
	return parentNames == null ? names : CoBaseUtilities.concatenate(parentNames, names);
}


public int getNumberOfInherited(boolean inherited) {
	return isRoot(inherited) ? 0 : m_parent.getSize(inherited);
}


// asked by the list model
public int getSize (boolean inherited) {
	return _getSize() + (isRoot(inherited) ? 0 : m_parent.getSize(inherited));
}


public Object getValue (boolean inherited) {
	return getNames(inherited);
}


public String getValueName() {
	return m_name;
}


public CoAbstractVariableIF getVariable (int index, boolean inherited) {
	CoAbstractVariableIF var = null;
	if(!isRoot(inherited)) {
		var = m_parent.getVariable(index, inherited);
		index -= m_parent.getSize(inherited);
	}
	if(var != null) return var;
		
	if (index < 0) return null;
	
	if (index < m_variablesOnlyReadable.size())
		return (CoAbstractVariableIF)m_variablesOnlyReadable.get(index);
		
	if (index < m_variablesOnlyReadable.size() + m_variables.size())
		return (CoAbstractVariableIF)m_variables.get(index - m_variablesOnlyReadable.size());
		
	return null;
}


/**
 *	First look in this holder, then in parent, thus implementing
 *  variable 'override'
 *
 */
public CoAbstractVariableIF getVariable (String name, boolean inherited) {
	for (int i = 0; i < m_variables.size(); i++) {
		CoAbstractVariableIF v = (CoAbstractVariableIF)m_variables.get(i);
		if (v.getName().equals(name))
			return v;
	}
	
	for (int i = 0; i < m_variablesOnlyReadable.size(); i++) {
		CoAbstractVariableIF v = (CoAbstractVariableIF)m_variablesOnlyReadable.get(i);
		if (v.getName().equals(name))
			return v;
	}

	// Ok - did not find a variable in this holder - try parent
	if(!isRoot(inherited)) {
		return m_parent.getVariable(name, inherited);
	}
	return null;
}


// asked by the list model
public String getVariableName (int index, boolean inherited) {
	String name = null;
	if(!isRoot(inherited)) {
		name = m_parent.getVariableName(index, inherited);
		index -= m_parent.getSize(inherited);
	}
	if(CoBaseUtilities.stringIsNotEmpty(name)) return name;
		
	if (index < 0) return "";
	
	if (index < m_variablesOnlyReadable.size()) 
		return ((CoAbstractVariableIF) m_variablesOnlyReadable.get(index)).getName();
		
	if (index < m_variablesOnlyReadable.size() + m_variables.size())
		return ((CoAbstractVariableIF) m_variables.get(index - m_variablesOnlyReadable.size())).getName();

	return "";
}


/**
 *	Does _not_ check for uniqueness with regard to its parent
 *  This allows for variable overloading
 *
 */
public boolean isLegal (String name) {
	if (name == null || name.equals("")) return false;
	
	for (int i = 0; i < m_variables.size(); i++) {
		CoAbstractVariableIF v = (CoAbstractVariableIF)m_variables.get(i);
		if (v.getName().equals(name))
			return false; // already used
	}
	
	for (int i = 0; i < m_variablesOnlyReadable.size(); i++) {
		CoAbstractVariableIF v = (CoAbstractVariableIF)m_variablesOnlyReadable.get(i);
		if (v.getName().equals(name))
			return false; // already used
	}
	
	return true && (!isRoot(true) ? m_parent.isLegal(name) : true);
}


protected boolean isRoot(boolean inherited) {
	return m_parent == null || (m_parent != null && !inherited);
}


protected boolean isXmlExportable(CoAbstractVariableIF variable) {
	return true;
}


private void markDirty () {
	m_dirtyFlag = !m_dirtyFlag;
}


public void remove (int index, boolean inherited) {
	if(!isRoot(inherited)) {
		if(index < m_parent.getSize(inherited)) {
			// Ok - it is one of the parents' variables that are being removed
			throw new UnsupportedOperationException("Du kan inte ta bort ärvda variabler här.");
		}
		index -= m_parent.getSize(inherited);
	}
	m_variables.remove(index - m_variablesOnlyReadable.size());
	markDirty();
}


public void setValue (Object value) {
	// do nothing, not implemented
}


protected void setValueName(String name) {
	m_name = name;
}


public boolean setVariableName(CoAbstractVariableIF variable, String name)
{
	if (isLegal(name))
	{
		variable.setName(name);
		markDirty();
		return true;
	}
	return false;
}


public String toString () {
	String s = "";
	
	Iterator elem = m_variablesOnlyReadable.iterator();
	while (elem.hasNext()) {
		s += elem.next().toString() + "\n";
	}
	
	elem = m_variables.iterator();
	while (elem.hasNext()) {
		s += elem.next().toString() + "\n";
	}
	
	return s;
}


public void validate(Object newValue) throws PropertyVetoException {
	// do nothing, not implemented
}


public void xmlAddSubModel(String parameter,
                           Object subModel,
                           com.bluebrim.xml.shared.CoXmlContext context)
{
  if (subModel instanceof String && "name".equals(parameter))
  {
    	m_name = (String)subModel;
  }
  else if (subModel instanceof CoAbstractVariableIF)
  {		
	  CoAbstractVariableIF var = (CoAbstractVariableIF)subModel;
	  addVariable(var);
  }
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	
}


public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
	visitor.exportString("name",  m_name);
	Iterator iter = m_variables.iterator();
	while(iter.hasNext()) {
		CoAbstractVariableIF var = (CoAbstractVariableIF)iter.next();
		if(isXmlExportable(var)) {
			visitor.export(var);
		}
	}
	iter = m_variablesOnlyReadable.iterator();
	while(iter.hasNext()) {
		CoAbstractVariableIF var = (CoAbstractVariableIF)iter.next();
		if(isXmlExportable(var)) {
			visitor.export(var);
		}
	}
}
}