package com.bluebrim.layout.impl.server;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/*
 * By Helena Rankegård
 *
 * A holder of CoTrapetsesColumn instanses (CoColumnIFs).
 */

public class CoTrapetsesColumns extends com.bluebrim.text.shared.CoAbstractColumnGeometry
{
	private List m_columns = new ArrayList();
// stores a new CoTrapetsesColumn containing the trapetses 
// that describe one column with a certain bounds

public void add( List trapetses, Rectangle2D.Float bounds)
{
	if
		(trapetses != null && trapetses.size() != 0)
	{
		m_columns.add(new CoTrapetsesColumn(trapetses, bounds));
	}
}
// Returns the CoTrapetsesColumn stored at pos i
// (see interface com.bluebrim.text.shared.CoColumnGeometryIF)

public com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF getColumn(int i)
{
	try
	{
		return (com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF) m_columns.get(i);
	}
	catch (Exception e)
	{
		return null;
	}
}
// Returns the number of stored CoTrapetsesColumn instanses
// (see interface com.bluebrim.text.shared.CoColumnGeometryIF)

public int getColumnCount()
{
	return m_columns.size();
}
}
