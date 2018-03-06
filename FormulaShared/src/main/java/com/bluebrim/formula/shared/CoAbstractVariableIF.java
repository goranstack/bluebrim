package com.bluebrim.formula.shared;
import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;

/*
 * 
 */
 
public interface CoAbstractVariableIF extends CoObjectIF, java.rmi.Remote, CoXmlEnabledIF {
public Object bindValue (CoVariableBinderIF variableBinder) throws CoVariableBindingException;


public String getComment ();


public String getName ();


public boolean isWriteable ();


public void setComment (String comment);


public void setName (String name);
}