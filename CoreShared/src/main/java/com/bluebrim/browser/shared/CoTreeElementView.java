package com.bluebrim.browser.shared;

import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * View class for server classes that either implements <code>CoTreeCatalogElementIF</code>
 * or want to act like a tree catalog element.
 * Used to cache information on the client to decrease the roundtrips.
 */
public class CoTreeElementView extends CoAbstractTreeCatalogElementProxy
{
	private CoTreeElementView m_childViews[];
public CoTreeElementView(CoTreeCatalogElementIF treeElement)
{
	super(treeElement);
	List elements = treeElement.getElements();
	m_childViews =  (elements == null || elements.isEmpty())
						? m_childViews =null 
						: new CoTreeElementView[elements.size()];
}
public CoTreeElementView(CoTreeCatalogElementIF treeElement, String identity)
{
	this(treeElement);
	m_name = identity;
}
public final void buildChildViews(int depth, int max)
{
	List elements = getTreeCatalogElement().getElements();
	if (elements == null || elements.isEmpty())
		m_childViews = null;
	else
	{
		int size		= elements.size();
		if (m_childViews == null || size != m_childViews.length)
			m_childViews 	= new CoTreeElementView[size];
		int count		= Math.min(size, max);
		for (int i=0; i<count; i++)
		{
			CoTreeElementView view =  createChildViewFor((CoTreeCatalogElementIF )elements.get(i));
			m_childViews[i] = view;
			if (depth > 0)
				view.buildChildViews(depth-1, max);
		}
		sortChildViews(m_childViews);
	}
}
public CoTreeElementView createChildViewFor(CoTreeCatalogElementIF element)
{
	return new CoTreeElementView(element);
}
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		try
		{
			CoTreeElementView _element = (CoTreeElementView) o;
			return _element.getTreeCatalogElement().equals(getTreeCatalogElement());
		}
		catch (ClassCastException e)
		{
			return false;
		}
	}
public int getChildCount()
{
	return m_childViews != null ? m_childViews.length : 0;
}
public CoTreeElementView[] getChildViews()
{
	return m_childViews;
}
public List getElements() { return null; }
public final CoTreeCatalogElementIF getTreeCatalogElement ()
{
	return (CoTreeCatalogElementIF )m_element;
}
public CoTreeElementView getViewAt(int index)
{
	return m_childViews[index];
}
	public int hashCode()
	{
		return m_element.hashCode();
	}
public void insertViews(int index, CoTreeElementView views[])
{
	System.arraycopy(views, 0,m_childViews, index, views.length);
	sortChildViews(m_childViews);
}
public boolean isLeafElement()
{
	return getChildCount() == 0;
}
public void setChildViews(CoTreeElementView childViews[])
{
	m_childViews = childViews;
	sortChildViews(m_childViews);
}
protected void sortChildViews(CoTreeElementView childViews[])
{
	// NOP
}
	public String toString()
	{
		return "TreeElementView for " + CoBaseUtilities.debugInfo(m_element);
	}
}
