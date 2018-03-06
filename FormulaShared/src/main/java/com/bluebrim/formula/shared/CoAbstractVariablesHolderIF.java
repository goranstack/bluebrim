package com.bluebrim.formula.shared;
import java.rmi.*;

import com.bluebrim.base.shared.*;
/*
 * 
 */
 
public interface CoAbstractVariablesHolderIF extends CoObjectIF, Remote, com.bluebrim.xml.shared.CoXmlEnabledIF {
	public static final String CO_ABSTRACT_VARIABLES_HOLDER_VALUEABLE = "CoAbstractVariablesHolderCoValueable";
public CoAbstractVariablesHolderIF getParent();	

public void add ();


public void addVariable(CoAbstractVariableIF v);


public String[] getNames (boolean inherited);


public int getNumberOfInherited(boolean inherited);


public int getSize (boolean inherited);


public CoAbstractVariableIF getVariable (int index, boolean inherited);


public CoAbstractVariableIF getVariable (String name, boolean inherited);


public String getVariableName (int index, boolean inherited);


public boolean isLegal(String name);


public void remove (int index, boolean inherited);


public boolean setVariableName (CoAbstractVariableIF variable, String newName);
}