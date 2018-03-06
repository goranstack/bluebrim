package com.bluebrim.layoutmanager;

import java.util.*;
/**
 * Combines a value(cost) with a intervall and a column.
 * Creation date: (2000-06-07 13:52:33)
 * @author: Arvid Berg & Masod Jalalian 
 */
public class CoSolution implements Comparable 
{
	private List list=null;
	private com.bluebrim.layoutmanager.CoInterval intervall=null;
	private double cost=Double.MAX_VALUE;
	private int column=-1;
/**
 * Solusion constructor comment.
 */
public CoSolution() 
{
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 14:14:57)
 * @param in_list java.util.List
 * @param in_intervall columnalg.server.Intervall
 * @param in_cost double
 */
public CoSolution(int in_column, com.bluebrim.layoutmanager.CoInterval in_intervall, double in_cost) 
{
	column=in_column;
	intervall=in_intervall;
	cost=in_cost;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 14:14:57)
 * @param in_list java.util.List
 * @param in_intervall columnalg.server.Intervall
 * @param in_cost double
 */
public CoSolution(List in_list, com.bluebrim.layoutmanager.CoInterval in_intervall, double in_cost) 
{
	list=in_list;
	intervall=in_intervall;
	cost=in_cost;
}
/**
 * compareTo method comment.
 */
public int compareTo(Object obj) 
{
	return (int)(cost-((CoSolution)obj).cost);
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 14:40:25)
 * @return int
 */
public int getColumn() 
{
	return column;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 13:54:26)
 * @return double
 */
public double getCost() 
{
	return cost;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 13:54:26)
 * @return columnalg.server.Intervall
 */
public com.bluebrim.layoutmanager.CoInterval getIntervall() 
{
	return intervall;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 13:54:26)
 * @return java.util.List
 */
public java.util.List getList() 
{
	return list;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 14:40:25)
 * @param newColumn int
 */
public void setColumn(int newColumn) 
{
	column = newColumn;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 13:54:26)
 * @param newCost double
 */
public void setCost(double newCost) 
{
	cost = newCost;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 13:54:26)
 * @param newIntervall columnalg.server.Intervall
 */
public void setIntervall(com.bluebrim.layoutmanager.CoInterval newIntervall) 
{
	intervall = newIntervall;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-07 13:54:26)
 * @param newList java.util.List
 */
public void setList(java.util.List newList) 
{
	list = newList;
}
}
