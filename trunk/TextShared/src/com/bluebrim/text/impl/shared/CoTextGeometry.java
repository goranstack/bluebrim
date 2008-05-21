package com.bluebrim.text.impl.shared;


/**
 * Implementation of text geometry specification
 * 
 * @author: Dennis Malmström
 */

public class CoTextGeometry implements com.bluebrim.text.shared.CoTextGeometryIF
{
	private boolean m_dirty;
	
	private String m_firstBaselineType = null;
	private float m_firstBaselineOffset = Float.NaN;
	private String m_verticalAlignmentType = null;
	private float m_verticalAlignmentMaxInter = Float.NaN;
public float getFirstBaselineOffset()
{
	return m_firstBaselineOffset;
}
public String getFirstBaselineType()
{
	return m_firstBaselineType;
}
public float getVerticalAlignmentMaxInter()
{
	return m_verticalAlignmentMaxInter;
}
public String getVerticalAlignmentType()
{
	return m_verticalAlignmentType;
}
private void markDirty()
{
	m_dirty = ! m_dirty;
}
public void setFirstBaselineOffset( float f )
{
	m_firstBaselineOffset = f;
	markDirty();
}
public void setFirstBaselineType( String t )
{
	m_firstBaselineType = t;
	markDirty();
}
public void setVerticalAlignmentMaxInter( float f )
{
	m_verticalAlignmentMaxInter = f;
	markDirty();
}
public void setVerticalAlignmentType( String t )
{
	m_verticalAlignmentType = t;
	markDirty();
}
}
