package com.bluebrim.text.impl.shared;


/**
 * 
 * 
 * @author: Dennis
 */
 
public class CoTextSearcher
{
	public static final int DONE = -9999;
	
	protected com.bluebrim.text.shared.CoImmutableStyledDocumentIF m_document;
//	protected String m_text;

	protected String m_target;
	protected int m_position;

	protected boolean m_forward = false;
public CoTextSearcher()
{
}
public void deactivate()
{
	m_document = null;
//	m_text = null;
}
public String getTarget()
{
	return m_target;
}
public boolean isActive()
{
	return m_document != null;//m_text != null;
}
public int search()
{
	if ( m_document == null ) return DONE;
	if ( m_target == null ) return DONE;
	if ( m_position == DONE ) return DONE;
	
	if ( m_forward ) m_position++; else m_position--;

	m_position = m_document.search( m_target, m_position, m_forward, false, false );

	if ( m_position == -1 ) m_position = DONE;
	
	return m_position;
}
public void setDocument( com.bluebrim.text.shared.CoStyledDocumentIF document )
{
	m_document = document;
}
public void setForward( boolean forward )
{
	if ( m_forward == forward ) return;

	m_forward = forward;

	if ( m_forward ) m_position -= 2; else m_position += 2;
}
public void setPosition( int pos )
{
	m_position = pos;
	if ( m_forward ) m_position--; else m_position++;
}
public void setTarget( String target )
{
	m_target = target;
}
}