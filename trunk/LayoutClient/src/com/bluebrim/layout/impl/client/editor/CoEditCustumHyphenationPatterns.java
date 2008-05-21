package com.bluebrim.layout.impl.client.editor;


/**
 * Layout editor operation: Open the ui for editing the custom hyphenation patterns.
 * 
 * @author: Dennis
 */
 
public class CoEditCustumHyphenationPatterns extends CoLayoutEditorAction
{
public CoEditCustumHyphenationPatterns( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoEditCustumHyphenationPatterns( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	com.bluebrim.text.impl.client.CoHyphenationPatternUI.open();
}
}
