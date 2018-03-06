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
 
public class CoCharacterTagUI extends CoAbstractTagUI
{


























	















































           







public CoCharacterTagUI()
{
	this( null );
}

public CoCharacterTagUI( CoAbstractTextEditor editor )
{
	super();

	buildForComponent();
	
	setEditor( editor );
}

protected boolean didAttributesChange( CoAttributeEvent e )
{
	return true;
}

protected AttributeSet getAttributes( CoAttributeEvent e )
{
	return e.getCharacterAttributes();
}

protected AttributeSet getSelectedAttributes()
{
	return m_editor.getSelectedCharacterAttributes();
}

protected String getTag( AttributeSet as )
{
	return CoStyleConstants.getCharacterTag( as );
}

protected List getTags( CoTextEditorContextIF context )
{
	return context.getCharacterTagNames();
}

protected String getTitle()
{
	return CoTextStringResources.getName( CoTextConstants.CHARACTER_TAG );
}

protected void setTag( String tag, boolean doClearLocalAttributes )
{
	if
		( m_editor != null )
	{
		if ( doClearLocalAttributes ) CoPlainCharacterAction.doit( m_editor, m_editor.getCoStyledDocument() );
		CoTagCharacterAction.doit( m_editor, m_editor.getCoStyledDocument(), tag );
		m_editor.repaint();
	}
}
}