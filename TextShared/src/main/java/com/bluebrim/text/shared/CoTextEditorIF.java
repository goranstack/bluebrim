package com.bluebrim.text.shared;
import java.awt.*;

import javax.swing.*;

/**
 * Interface defining the protocol of a text editor.
 *
 * @author: Dennis Malmström
 */

public interface CoTextEditorIF
{
// the swing component implementing the text editor

JComponent getComponent();
com.bluebrim.text.shared.CoStyledDocumentIF getCoStyledDocument();
// was the text changes since last call to unsetDirty ?

boolean isDirty();
void setCoStyledDocument( com.bluebrim.text.shared.CoStyledDocumentIF doc );
void setExpandOnOverflow( boolean b );
void setGeometry( com.bluebrim.text.shared.CoColumnGeometryIF doc, com.bluebrim.text.shared.CoBaseLineGeometryIF bl, com.bluebrim.text.shared.CoTextGeometryIF tg );
// see isDirty()

void unsetDirty();
	public static final RenderingHints.Key PAINT_OVERFLOW_INDICATOR = new RenderingHints.Key( 0 )
	{
		public boolean isCompatibleValue( Object v )
		{
			return ( v == PAINT_OVERFLOW_INDICATOR_ON ) || ( v == PAINT_OVERFLOW_INDICATOR_OFF );
		}
	};			public static final Color PAINT_OVERFLOW_INDICATOR_COLOR = Color.red;			public static final Object PAINT_OVERFLOW_INDICATOR_OFF = Boolean.FALSE;			public static final Object PAINT_OVERFLOW_INDICATOR_ON 	= Boolean.TRUE;
}