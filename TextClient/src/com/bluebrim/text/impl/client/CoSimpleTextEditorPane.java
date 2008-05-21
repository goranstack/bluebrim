package com.bluebrim.text.impl.client;

import java.awt.*;

//

public class CoSimpleTextEditorPane extends CoAbstractTextEditorPane
{
	private static class SimpleTextEditor extends CoSimpleTextEditor implements ScaleAndMargins
	{
		public SimpleTextEditor( com.bluebrim.text.shared.CoStyledDocumentIF doc )
		{
			super( doc );
		}

		public Dimension getMinimumSize()
		{
			return new Dimension( 10, 10 );
		}

		public void setDocument( javax.swing.text.Document doc )
		{
			super.setDocument(doc);
			revalidate();
		}
	};

public CoSimpleTextEditorPane()
{
	super();
}

protected ScaleAndMargins createScaleAndMargin()
{
	return (ScaleAndMargins) m_textEditor;
}
protected CoAbstractTextEditor createTextEditor( com.bluebrim.text.shared.CoStyledDocumentIF doc )
{
	return new SimpleTextEditor( doc );
}

public CoSimpleTextEditorPane( com.bluebrim.gui.client.CoUserInterfaceBuilder b, com.bluebrim.menus.client.CoMenuBuilder mb )
{
	super( b, mb );
}
}