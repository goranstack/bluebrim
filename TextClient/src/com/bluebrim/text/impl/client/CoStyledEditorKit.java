package com.bluebrim.text.impl.client;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.impl.client.actions.*;

/**
 * EditorKit för CoAbstractTextPane.
 * Innehåller actions som använder attributen i CoTextConstants.
 */
public class CoStyledEditorKit extends StyledEditorKit implements CoActionConstantsIF
{
public Document createDefaultDocument()
{
	return new com.bluebrim.text.shared.CoStyledDocument();
}
public Action[] getActions()
{
	return TextAction.augmentList( super.getActions(), CoActionConstantsIF.ACTIONS );
}

public CoStyledEditorKit()
{
}
}