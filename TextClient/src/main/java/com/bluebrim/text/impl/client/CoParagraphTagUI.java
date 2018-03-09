package com.bluebrim.text.impl.client;

// Calvin imports

import java.util.*;

import javax.swing.text.*;

import com.bluebrim.text.impl.client.actions.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 *
 */
 
public class CoParagraphTagUI extends CoAbstractTagUI
{


























	















































           







public CoParagraphTagUI()
{
	this( null );
}

public CoParagraphTagUI( CoAbstractTextEditor editor )
{
	super();

	buildForComponent();
	
	setEditor( editor );
}

protected boolean didAttributesChange( CoAttributeEvent e )
{
	return e.didParagraphChange();
}

protected AttributeSet getAttributes( CoAttributeEvent e )
{
	return e.getParagraphAttributes();
}

protected AttributeSet getSelectedAttributes()
{
	return m_editor.getSelectedParagraphAttributes();
}

protected String getTag( AttributeSet as )
{
	return CoStyleConstants.getParagraphTag( as );
}

protected List getTags( CoTextEditorContextIF context )
{
	return context.getParagraphTagNames();
}

protected String getTitle()
{
	return CoTextStringResources.getName( CoTextConstants.PARAGRAPH_TAG );
}

protected void setTag( String tag, boolean doClearLocalAttributes )
{
	if
		( m_editor != null )
	{
		if ( doClearLocalAttributes ) CoPlainParagraphAction.doit( m_editor, m_editor.getCoStyledDocument() );
		CoTagParagraphAction.doit( m_editor, m_editor.getCoStyledDocument(), tag );
		m_editor.repaint();
	}
}
}