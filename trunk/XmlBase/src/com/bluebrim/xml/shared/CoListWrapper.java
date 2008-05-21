package com.bluebrim.xml.shared;
import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-08-16 11:34:32)
 * @author: Michael Klimczak 
 */
public class CoListWrapper extends CoSimpleObject implements CoXmlExportEnabledIF{


	String m_name;

/**
 * CoListWrapper constructor comment.
 */
public CoListWrapper() {
	super();
	m_list = new ArrayList();
}
public CoListWrapper(Collection c){
	super();
	m_list = new ArrayList(c);
}
public CoListWrapper(List list){
	super();
	m_list = list;
}
	public String getType(){

		return m_name;		
	}
	public void setType(String type){

		m_name = type;		
	}
public void xmlVisit(CoXmlVisitorIF visitor){
	
	visitor.export(m_list.iterator());
}

	List m_list;

public List getList()
{
	return m_list;
}
}