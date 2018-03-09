package com.bluebrim.text.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.impl.client.actions.*;
import com.bluebrim.text.impl.client.swing.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.text.shared.swing.*;

//
 
public class CoSimpleTextEditor extends CoAbstractTextEditor implements CoHorizontalMarginController.MarginHolder
{
	private double m_scale = 1;
	private int m_horizontalMargin = 20;
	
	private com.bluebrim.text.shared.CoImmutableStyledDocumentIF.FontManager m_fontManager;

	private CoHorizontalMarginController m_marginController;





	
	static class MyLabelView extends javax.swing.text.LabelView
	{
		public MyLabelView( Element e )
		{
			super( e );
		}

		protected void setPropertiesFromAttributes()
		{
			super.setPropertiesFromAttributes();
			
			AttributeSet attr = getAttributes();
			
			setStrikeThrough( CoViewStyleConstants.isStrikeThru( attr ) );
			
			CoEnumValue v = CoViewStyleConstants.getVerticalPosition( attr );
			setSuperscript( v.equals( CoTextConstants.VERTICAL_POSITION_SUPERSCRIPT ) );
			setSubscript( v.equals( CoTextConstants.VERTICAL_POSITION_SUBSCRIPT ) );
			
			v = CoViewStyleConstants.getUnderline( attr );
			setUnderline( ! v.equals( CoTextConstants.UNDERLINE_NONE ) );
		}		
	};
	



	class MyParagraphView extends javax.swing.text.ParagraphView
	{
		protected CoTabSetIF m_tabSet;
		protected boolean m_isLeftJustified;

		private String m_tagLabel;
		private int m_tagLabelWidth;
		private int m_tagLabelY;
		
		
		public MyParagraphView( Element e )
		{
			super( e );
			setPropertiesFromAttributes();
		}
		
		protected CoTabSetIF getCoTabSet()
		{
			return m_tabSet;
		}
		
		protected void setPropertiesFromAttributes()
		{
			AttributeSet attr = getAttributes();
			if
				(attr != null)
			{
		    setInsets( (short) CoViewStyleConstants.getSpaceAbove( attr ),
			             (short) CoViewStyleConstants.getLeftIndent( attr ),
			             (short) CoViewStyleConstants.getSpaceBelow( attr ),
			             (short) CoViewStyleConstants.getRightIndent( attr ) );

		    CoEnumValue v = CoViewStyleConstants.getAlignment( attr );
				if      ( v.equals( CoStyleConstants.ALIGN_LEFT ) )      setJustification( StyleConstants.ALIGN_LEFT );
				else if ( v.equals( CoStyleConstants.ALIGN_JUSTIFIED ) ) setJustification( StyleConstants.ALIGN_LEFT );
				else if ( v.equals( CoStyleConstants.ALIGN_FORCED ) )    setJustification( StyleConstants.ALIGN_LEFT );
				else if ( v.equals( CoStyleConstants.ALIGN_RIGHT ) )     setJustification( StyleConstants.ALIGN_RIGHT );
				else if ( v.equals( CoStyleConstants.ALIGN_CENTER ) )    setJustification( StyleConstants.ALIGN_CENTER );
				m_isLeftJustified = ( v == CoStyleConstants.ALIGN_LEFT );
				
//		    setLineSpacing( StyleConstants.getLineSpacing(attr) );
		    setFirstLineIndent( CoViewStyleConstants.getFirstLineIndent( attr ) );

		    m_tabSet = CoViewStyleConstants.getTabSet( attr );

		    m_tagLabel = null;
			}
		}

		public void paint( Graphics g, Shape a )
		{
			super.paint( g, a );
			/*if ( com.bluebrim.text.client.swing.CoSectionView.SHOW_TAGS )*/ paintParagraphTag( g, a );
		}

		
		private void paintParagraphTag( Graphics g, Shape a )
		{
	    Font f = ( com.bluebrim.text.shared.swing.CoSectionView.TAG_FONT == null ) ? CoSimpleTextEditor.this.getFont() : com.bluebrim.text.shared.swing.CoSectionView.TAG_FONT;
	    
			AttributeSet attr = getElement().getAttributes();

			if
				( m_tagLabel == null )
			{
		    Object tag = attr.getAttribute( CoTextConstants.PARAGRAPH_TAG );
			  if ( tag == null ) tag = "";//CoTextConstants.DEFAULT_TAG_NAME;
		    m_tagLabel = "<" + tag + ">";
		    m_tagLabelWidth = (int) g.getFontMetrics( f ).getStringBounds( m_tagLabel, g ).getWidth();
			}
			
			double dy = g.getFontMetrics().getLineMetrics( m_tagLabel, g ).getAscent() + CoViewStyleConstants.getSpaceAbove( attr );

			Font oldFont = g.getFont();
			
			g.setFont( f );
			
			g.setColor( com.bluebrim.text.shared.swing.CoSectionView.TAG_COLOR );

			Rectangle2D r = a.getBounds();
			g.drawString( m_tagLabel, (int) ( r.getX() - m_tagLabelWidth - 10 ), (int) ( r.getY() + dy ) );
			
			g.setFont( oldFont );
		}

/*

tabs not honored in JTextPane

		public float nextTabStop(float x, int tabOffset)
		{
			// If the text isn't left justified, offset by 10 pixels!
			if (!m_isLeftJustified) return x + 10.0f;
			
			x -= getTabBase();
			CoTabSetIF tabs = getCoTabSet();
			if(tabs == null)
			{
				// a tab every 72 pixels.
				return (float)(getTabBase() + (((int)x / 72 + 1) * 72));
			}
			
			CoTabStopIF tab = tabs.getTabAfter(x + .01f);
			if(tab == null)
			{
				// no tab, do a default of 5 pixels.
				// Should this cause a wrapping of the line?
				return getTabBase() + x + 5.0f;
			}
			
			int alignment = tab.getAlignment();
			int offset;
			switch(alignment)
			{
				default:
				case CoTabStopIF.ALIGN_LEFT:
					// Simple case, left tab.
					return getTabBase() + tab.getPosition();
			//		case CoTabStopIF.ALIGN_BAR:
					// PENDING: what does this mean?
			//			return m_tabBase + tab.getPosition();
				case CoTabStopIF.ALIGN_RIGHT:
				case CoTabStopIF.ALIGN_CENTER:
					offset = findOffsetToCharactersInString(CoParagraphView.m_tabChars,
															tabOffset + 1);
					break;
				case CoTabStopIF.ALIGN_DECIMAL:
					offset = findOffsetToCharactersInString(CoParagraphView.m_tabDecimalChars,
															tabOffset + 1);
					break;
			}
			if (offset == -1)
			{
				offset = getEndOffset();
			}
			float charsSize = getPartialSize(tabOffset + 1, offset);
			switch(alignment)
			{
				case CoTabStopIF.ALIGN_RIGHT:
				case CoTabStopIF.ALIGN_DECIMAL:
					// right and decimal are treated the same way, the new
					// position will be the location of the tab less the
					// partialSize.
					return getTabBase() + Math.max(x, tab.getPosition() - charsSize);
				case CoTabStopIF.ALIGN_CENTER: 
					// Similar to right, but half the partialSize.
					return getTabBase() + Math.max(x, tab.getPosition() - charsSize / 2.0f);
			}
			// will never get here!
			return x;
		}	

		*/
		
	};
	


		
	public class CoUnstyledEditorKit extends CoStyledEditorKit
	{		
		public CoUnstyledEditorKit()
		{
			super();
		}
		
		public ViewFactory getViewFactory()
		{
			return m_defaultFactory;
		}
		
		private ViewFactory m_defaultFactory = new UnstyledViewFactory();
	
		// kopplar in egendefinierade vyer för utritning av taggar etc
		protected class UnstyledViewFactory implements ViewFactory
		{
			public View create(Element elem)
			{
		    String kind = elem.getName();
		    if (kind != null) {
					if (kind.equals(AbstractDocument.ContentElementName)) {
					    return new MyLabelView( elem );//javax.swing.text.LabelView(elem);
					} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
					    return new MyParagraphView( elem );//javax.swing.text.ParagraphView(elem);
					} else if (kind.equals(AbstractDocument.SectionElementName)) {
					    return new  javax.swing.text.BoxView(elem, View.Y_AXIS);
					} else if (kind.equals(StyleConstants.ComponentElementName)) {
					    return new javax.swing.text.ComponentView(elem);
					} else if (kind.equals(StyleConstants.IconElementName)) {
					    return new javax.swing.text.IconView(elem);
					}	else if ( kind.equals( CoTextConstants.CommentElementName ) ) {
							return new CoCommentView( elem );
					}
	  	  }
	  	  	// default to text display
	    	return new MyLabelView( elem );// javax.swing.text.LabelView(elem);
			}
		}
	}
	

public CoSimpleTextEditor()
{
	super();
	init();
}
public CoSimpleTextEditor (com.bluebrim.text.shared.CoStyledDocumentIF doc )
{
	super( doc );
	init();
}


public JComponent getComponent()
{
	return this;
}
public int getHorizontalMargin()
{
	return m_horizontalMargin;
}
public Insets getInsets()
{
	Insets i = super.getInsets();
	i.left = m_horizontalMargin;
	i.right = m_horizontalMargin;
	return i;
}
public double getScale()
{
	return m_scale;
}
private void init()
{
  setEditorKit( createDefaultEditorKit() );
	
	setBackground( Color.white );
	super.setOpaque( true );
	setCaretColor( Color.red );
	setEditable( true );

	m_fontManager =
		new com.bluebrim.text.shared.CoImmutableStyledDocumentIF.FontManager()
		{
			public Font getFont( AttributeSet a )
			{
				return CoViewStyleConstants.getFont( a, (float) m_scale );
			}
			
			public com.bluebrim.font.shared.CoFont getCoFont( AttributeSet a )
			{
				return CoViewStyleConstants.getCoFont( a, (float) m_scale );
			}
		};

	m_marginController = new CoHorizontalMarginController( this );
	
	addFocusListener(
		new FocusListener()
	  {
		  public void focusGained( FocusEvent e )
		  {
				CoStyledTextAction.setTextPane( CoSimpleTextEditor.this );
		  }
		  public void focusLost( FocusEvent e )
		  {
		  }
	  }
	);
}
public boolean isDirty()
{
	return true;
}
protected void paintComponent( Graphics g )
{
	super.paintComponent( g );

	m_marginController.paint( g );
}
public void refresh()
{
	((com.bluebrim.text.shared.CoStyledDocument) getCoStyledDocument()).stylesChanged();
}
public void setCoStyledDocument( com.bluebrim.text.shared.CoStyledDocumentIF doc )
{
	doc = doc.getCopy(); // PENDING: why ???
	
	com.bluebrim.text.shared.CoStyledDocumentIF old = getCoStyledDocument();
	
	if
		( old != null )
	{
		old.setFontManager( null );
	}
	
	doc.setFontManager( m_fontManager );

	setStyledDocument( doc );
}
public void setExpandOnOverflow( boolean expand )
{
}
public void setGeometry( com.bluebrim.text.shared.CoColumnGeometryIF columns, com.bluebrim.text.shared.CoBaseLineGeometryIF blg, com.bluebrim.text.shared.CoTextGeometryIF tg )
{
  repaint();
}
public void setHorizontalMargin( int m )
{
	if ( m < 0 ) m = 0;
	
	m_horizontalMargin = m;
	revalidate();
	repaint();
}
public void setOpaque( boolean b )
{
}
public void setScale( double s )
{
	m_scale = s;
	refresh();

	repaint();
}
public void unsetDirty()
{
}

protected EditorKit createDefaultEditorKit()
{
	return new CoUnstyledEditorKit();
}
}