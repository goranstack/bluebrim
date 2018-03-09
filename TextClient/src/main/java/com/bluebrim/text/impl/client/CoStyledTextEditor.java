package com.bluebrim.text.impl.client;

// Calvin imports
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.text.*;

import com.bluebrim.text.impl.client.actions.*;
import com.bluebrim.text.shared.swing.*;

/**
 * Subklass av CoAbstractTextPane som visar text i kolumner och är skalbar.
 * Kolumngeometrin kan anges på två sätt.
 * Antingen anges kolumnantal, kolumnbredd, kolumnhöjd samt utrymmet mellen kolumnerna.
 * Dessa parametrar har alltid giltiga värden.
 * Alternativt så definieras kolumnerna av en sekvens av rektanglar.
 * Om en sådan definition är given så gäller den före det första sättet att ange kolumngeometrin.
 * Det flexibilitet som det senare sättet ger betalas naturligtvis med något försämrade prestanda.
 */
 
public class CoStyledTextEditor extends CoAbstractTextEditor implements CoViewGeometryProviderIF
{





	
	/*
	public static final RenderingHints.Key PAINT_OVERFLOW_INDICATOR = new RenderingHints.Key( 0 )
	{
		public boolean isCompatibleValue( Object v )
		{
			return ( v == PAINT_OVERFLOW_INDICATOR_ON ) || ( v == PAINT_OVERFLOW_INDICATOR_OFF );
		}
	};
	public static final Object PAINT_OVERFLOW_INDICATOR_ON 	= Boolean.TRUE;
	public static final Object PAINT_OVERFLOW_INDICATOR_OFF 	= Boolean.FALSE;
*/

	
	// kolumngeometri i form av rektanglar [pixels]
	protected com.bluebrim.text.shared.CoColumnGeometryIF m_columnGeometry = new DefaultColumnGeometry( 100.0f, 100.0f );
	protected com.bluebrim.text.shared.CoBaseLineGeometryIF m_baseLineGeometry = new DefaultBaseLineGeometry();
	protected com.bluebrim.text.shared.CoTextGeometryIF m_textGeometry = new DefaultTextGeometry();
	
	private class DefaultTextGeometry implements com.bluebrim.text.shared.CoTextGeometryIF
	{
		public float getFirstBaselineOffset() { return Float.NaN; }
		public String getFirstBaselineType() { return null; }
		public float getVerticalAlignmentMaxInter() { return Float.NaN; }
		public String getVerticalAlignmentType() { return null; }
	}
	
	private class DefaultBaseLineGeometry implements com.bluebrim.text.shared.CoBaseLineGeometryIF
	{
		public float getY0() { return 0; }
		public float getDeltaY() { return 0; }
		public boolean isEquivalentTo( com.bluebrim.text.shared.CoBaseLineGeometryIF g )
		{
			return g.getDeltaY() == 0;
		}
	}

	private class DefaultColumnGeometry extends com.bluebrim.text.shared.CoAbstractColumnGeometry//implements com.bluebrim.text.shared.CoColumnGeometryIF
	{
		private com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF m_column;

		public DefaultColumnGeometry( float w, float h )
		{
			m_column = new com.bluebrim.text.shared.CoRectangularColumn( 0, 0, w, h );
		}

		public int getColumnCount()
		{
			return 1;
		}
		
		public com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF getColumn( int index )
		{
			return m_column;
		}

			public boolean isRectangular() { return m_column.isRectangular(); }
	};

	protected boolean m_columnsSet = false;

	// övriga utseende-attribut
	protected Color m_columnBorderColor = null;
	protected boolean m_expandOnOverflow = false;
	
	protected boolean m_hasFocus = false;
	// flagga som sätts när attribut som påverkar utseendet ändras.
	private boolean m_columnGeometryDirty = true;



	// flaggor som styr debug- och testbeteenden
	private static final boolean m_propertyDialogEnabled = true;







	/**
	   * Subklass av javax.swing.text.StyledEditorKit.
	   */
	static class CoColumnStyledEditorKit extends CoStyledEditorKit
	{
		public ViewFactory getViewFactory()
		{
			return CoStyledTextViewFactory.getInstance();
		}
	}










	
	public class CoColumnAwareCaret extends CoAtomAwareCaret
	{
		public CoColumnAwareCaret()
		{
			super();
//			setBlinkRate( 0 );
		}
		
		public void setVisible(boolean e)
		{
			super.setVisible(e);
		}

	
		public void paint( Graphics g )
		{
			// Caret geometry is scaled (so is g), compensate by "unscaling" g.
			Graphics2D G = (Graphics2D) g;
			AffineTransform t0 = G.getTransform();
			AffineTransform t = (AffineTransform) t0.clone();
			double s = 1 / com.bluebrim.base.shared.CoBaseUtilities.getXScale( t0 );
			t.scale( s, s );	
			G.setTransform( t );
			super.paint( g );
			G.setTransform( t0 );
		}

		/**
		  * Överriden från javax.swing.text.DefaultCaret.
		  * @return instans av ColumnTextPaneCaret.DefaultHighlightPainter.
		  */
		protected Highlighter.HighlightPainter getSelectionPainter()
		{
			return new CoDefaultHighlightPainter(getComponent().getSelectionColor());
		}

		/**
		 * Implementation av javax.swing.text.Highlighter.HighlightPainter.
		 * Implementerar skuggning av skalad text i kolumner.
		 */
		public class CoDefaultHighlightPainter implements Highlighter.HighlightPainter
		{
			private Color m_color;
//			private float m_scale = 1.0f;
			public CoDefaultHighlightPainter( Color c )
			{
				m_color = c;
			}

			/**
			 * Rita selekteringskuggan.
			 * @param g       Graphics-instans att rita med.
			 * @param offs0   Början av selekteringen.
			 * @param offs1   Slutet på selekteringen.
			 * @param bounds  används ej.
			 * @param c       JColumnTextPane-instansen som ska skuggas.
			 */
			public void paint(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c)
			{
				// klargör Graphics-instansen
				g.setColor(m_color);
				
				TextUI ui = c.getUI();

				try
				{
					ui.modelToView( c, offs0 );
				}
				catch ( BadLocationException e )
				{
					return;
				}
				
				com.bluebrim.text.shared.swing.CoSectionView sectionView = (com.bluebrim.text.shared.swing.CoSectionView) ui.getRootView( c ).getView( 0 );
				sectionView.paintSelectionShadow( (Graphics2D) g, offs0, offs1, bounds );
			}
		}
	}



	private static class ColorItem
	{
		public Color m_color;
		public String m_name;
		
		public ColorItem( Color c, String name )
		{
			m_color = c;
			m_name = name;
		}

		public String toString()
		{
			return m_name;
		}
	};

	private static class ColorComboBox extends JComboBox
	{
		public ColorComboBox()
		{
			super();

			addItem( new ColorItem( null, "none" ) );
			addItem( new ColorItem( Color.red, "red" ) );
			addItem( new ColorItem( Color.green, "green" ) );
			addItem( new ColorItem( Color.blue, "blue" ) );
			addItem( new ColorItem( Color.cyan, "cyan" ) );
			addItem( new ColorItem( Color.magenta, "magenta" ) );
			addItem( new ColorItem( Color.yellow, "yellow" ) );
			addItem( new ColorItem( Color.gray, "gray" ) );
			addItem( new ColorItem( Color.black, "black" ) );
			addItem( new ColorItem( Color.white, "white" ) );
			setMaximumRowCount( 10 );
		}
	};

	private CoPropertyDialog m_propertyDialog = null;


	
	public class CoPropertyDialog extends JDialog
	{
		JComboBox m_columnColorComboBox = new ColorComboBox();
		JComboBox m_paragraphColorComboBox = new ColorComboBox();
		JComboBox m_rowColorComboBox = new ColorComboBox();
		JComboBox m_labelColorComboBox = new ColorComboBox();
		JButton  m_importButton = new JButton( "import xtg" );
		
		public CoPropertyDialog(JFrame f)
		{
			super(f, false);
			
			JPanel properties = new JPanel( new GridLayout( 1, 0 ));
			
			m_columnColorComboBox.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					CoStyledTextEditor.this.setColumnBorderColor(  ( (ColorItem) e.getItem()).m_color );
				}
			});
			properties.add(m_columnColorComboBox);
			
			m_paragraphColorComboBox.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					CoParagraphView.VIEW_BORDER_COLOR = ( (ColorItem) e.getItem()).m_color;
					CoStyledTextEditor.this.repaint();
				}
			});
			properties.add(m_paragraphColorComboBox);
			
			m_rowColorComboBox.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					CoRowView.ROW_VIEW_BORDER_COLOR = ( (ColorItem) e.getItem()).m_color;
					CoStyledTextEditor.this.repaint();
				}
			});
			properties.add(m_rowColorComboBox);
			
			m_labelColorComboBox.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					CoLabelView.VIEW_BORDER_COLOR = ( (ColorItem) e.getItem()).m_color;
					CoStyledTextEditor.this.repaint();
				}
			});
			properties.add(m_labelColorComboBox);
	/*		
			m_importButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					java.io.Reader r = null;

					try
					{
						r = new java.io.FileReader( "d:\\xtg\\saab_text.xtg" );
					}
					catch ( java.io.FileNotFoundException ex )
					{
						System.err.println( ex );
						return;
					}
					
					com.bluebrim.text.impl.shared.xtg.CoXtgParser p = new com.bluebrim.text.impl.shared.xtg.CoXtgParser( r );

					p.parse();

					com.bluebrim.text.shared.CoStyledDocument d = (com.bluebrim.text.shared.CoStyledDocument) getCoStyledDocument();
					setCoStyledDocument( null );
					p.getRoot().extract( d );
					setCoStyledDocument( d );
				}
			});
			properties.add(m_importButton);
			*/

			getContentPane().add(properties, BorderLayout.CENTER);

			pack();
		}

	}
public CoStyledTextEditor()
{
	super();
	init();
}
public CoStyledTextEditor( com.bluebrim.text.shared.CoStyledDocumentIF doc )
{
	super( doc );
	init();
}


public void dispose()
{
	TextUI ui = getUI();
	
	com.bluebrim.text.shared.swing.CoSectionView s = (com.bluebrim.text.shared.swing.CoSectionView) ui.getRootView( this ).getView( 0 );

	s.release();
}
public boolean doesHaveFocus()
{
	return m_hasFocus;
}
public com.bluebrim.text.shared.CoBaseLineGeometryIF getBaseLineGeometry()
{
	return m_baseLineGeometry;
}
public final com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF getColumn( int position )
{
	TextUI ui = getUI();
	
	com.bluebrim.text.shared.swing.CoSectionView s = (com.bluebrim.text.shared.swing.CoSectionView) ui.getRootView( this ).getView( 0 );

	if
		( s.getEndOffset() <= position )
	{
		// text view hasn't been updated yet
		position = s.getEndOffset() - 1;
	}

	int i = s.getViewIndexOfPos( position );
	if
		( i == -1 )
	{
		i = s.getViewCount() - 1;
	}
	
	CoParagraphView v = (CoParagraphView) s.getView( i );

	return v.getColumn();
}
public Color getColumnBorderColor()
{
	return m_columnBorderColor;
}
  /**
   * @return sanninsvärde som talar om ifall kolumnernas gränser ritas eller ej.
   */
  public final Color getColumnBordersColor()
	{
	  return m_columnBorderColor;
	}
/**
 * @return kolumndefinitionen i form av en sekvens av rektanglar [pixels] (om en sådan specification är given annars null)
 */
public final com.bluebrim.text.shared.CoColumnGeometryIF getColumnGeometry()
{
	return m_columnGeometry;
}
/**
   * @return sanningsvärde som talar om ifall kolumngeometrin har förändrats 
   * sedan föregående anrop till isColumnGeometryDirty.
   */
final boolean getColumnGeometryDirty()
{
	return m_columnGeometryDirty;
}
public String getDummyText()
{
	return null;
}
  /**
   * @return sanninsvärde som talar om ifall kolumnhöjden ska ökas vid behov.
   */
  public final boolean getExpandOnOverflow()
	{
	  return m_expandOnOverflow;
	}
public final com.bluebrim.text.shared.CoTextGeometryIF getTextGeometry()
{
	return m_textGeometry;
}
private void init()
{
  setEditorKit( createDefaultEditorKit() );

  setCaret( new CoColumnAwareCaret() );
  setCaretColor( Color.red );
  super.setBorder( null );

  if
	  ( m_propertyDialogEnabled )
  {
	  addMouseListener( new MouseAdapter()
		  {
			  public void mouseClicked( MouseEvent e )
	  		{
					if ( e.isControlDown() && e.isAltDown() ) openPropertyDialog( e );
	  		}
			} );
  }


  addFocusListener( new FocusListener()
	  {
		  public void focusGained( FocusEvent e )
		  {
				CoStyledTextAction.setTextPane( CoStyledTextEditor.this );
			  m_hasFocus = true;
				//m_columnGeometryDirty = true;
				repaint();
		  }
		  public void focusLost( FocusEvent e )
		  {
			  m_hasFocus = false;
				//m_columnGeometryDirty = true;
				repaint();
		  }
	  } );

  initKeyBindings();
}
public void injectMouseEvent( MouseEvent ev )
{
	ev.translatePoint( - getX(), - getY() );
	processMouseEvent( ev );
}
  /**
   * @return sanningsvärde som talar om ifall kolumngeometrin har förändrats 
   * sedan föregående anrop.
   */
public final boolean isColumnGeometryDirty()
{
  boolean tmp = m_columnGeometryDirty;
  m_columnGeometryDirty = false;

  return tmp;
}
public boolean isDirty()
{
	TextUI ui = getUI();
	
	com.bluebrim.text.shared.swing.CoSectionView s = (com.bluebrim.text.shared.swing.CoSectionView) ui.getRootView( this ).getView( 0 );

	return s.isDirty();
}
  // --- property dialog stuff (debugging purposes only) ---

  private void openPropertyDialog()
	{
	  openPropertyDialog( null );
	}
  private void openPropertyDialog( MouseEvent e )
	{
	  if
	  	( m_propertyDialog == null )
	  {
	    Component c = getParent();
	    while
		  ( c != null )
	    {
		  if
		    ( c instanceof JFrame )
		  {
		    m_propertyDialog = new CoPropertyDialog( (JFrame) c );
		    break;
		  }
		  c = c.getParent();
	    }
	  }

	  if
	    ( e != null )
	  {
		Point p = getLocationOnScreen();
		m_propertyDialog.setLocation( e.getX() + p.x, e.getY() + p.y );
	  }
	  m_propertyDialog.show();
	}
public void rebuild()
{
	m_columnGeometryDirty = true;
  repaint();
}
public void setBorder( Border border ) {}
/**
   * Fångar alla förändringar av komponentens storlek.
   * Kolumnbredd och kolumnhöjd anpassas till den nya storleken.
   * @see java.awt.Component.setBounds.
   */
public void setBounds(int x, int y, int width, int height)
{
	if
		( ! m_columnsSet )
	{
		m_columnGeometry = new DefaultColumnGeometry( width, height );
		m_columnGeometryDirty = true;
	}

	super.setBounds( x, y, width, height );
	repaint();
}
  /**
   * Stäng av/på ritning av kolumngränserna.
   * @param  visible   av/på.
   */
  public void setColumnBorderColor( Color c )
	{
	  if
	  	( m_columnBorderColor != c )
	  {
	    m_columnGeometryDirty = true;
	    m_columnBorderColor = c;
	    repaint();
	  }
	}
  /**
   * Stäng av/på behovsstyrd expansion av kolumnhöjden.
   * @param  expand   av/på.
   */
  public void setExpandOnOverflow( boolean expand )
	{
	  if
	  	( m_expandOnOverflow != expand )
	  {
	    m_columnGeometryDirty = true;

	    m_expandOnOverflow = expand;
	    repaint();
	  }
	}
  /**
   * Ange kolumngeometrin i form av en sekvens av rektanglar (@see java.awt.Rectangle).
   * Alla värden antas vara givna i pixels.
   * Värdet null är giltigt och betyder att denna typ av kolumngeometridefinition ej ska användas.
   * @param  columns   Rektangelsekvensen.
   */
public void setGeometry( com.bluebrim.text.shared.CoColumnGeometryIF columns, com.bluebrim.text.shared.CoBaseLineGeometryIF blg, com.bluebrim.text.shared.CoTextGeometryIF tg )
{
	if
		(
			( columns == m_columnGeometry )
		&&
			( blg == m_baseLineGeometry )
		)
	{
		return;
	}
	
	if
		( columns == null )
	{
		m_columnsSet = false;
		m_columnGeometry = new DefaultColumnGeometry( getWidth(), getHeight() );
	} else {
		m_columnsSet = true;
		m_columnGeometry = columns;
	}
	
	if
		( blg == null )
	{
		m_baseLineGeometry = new DefaultBaseLineGeometry();
	} else {
		m_baseLineGeometry = blg;
	}

	if
		( tg == null )
	{
		m_textGeometry = new DefaultTextGeometry();
	} else {
		m_textGeometry = tg;
	}
	
	m_columnGeometryDirty = true;


  repaint();
}
public void unsetDirty()
{
	TextUI ui = getUI();

	com.bluebrim.text.shared.swing.CoSectionView s = (com.bluebrim.text.shared.swing.CoSectionView) ui.getRootView( this ).getView( 0 );

	s.unsetDirty();
}

protected EditorKit createDefaultEditorKit()
{
	return new CoColumnStyledEditorKit();
}

public void fitParagraph( )
{
	TextUI ui = getUI();

	com.bluebrim.text.shared.swing.CoSectionView s = (com.bluebrim.text.shared.swing.CoSectionView) ui.getRootView( this ).getView( 0 );

	int i0 = s.getViewIndexOfPos( getCaret().getDot() );
	int i1 = s.getViewIndexOfPos( getCaret().getMark() );
	if
		( ( i0 != -1 ) && ( i1 != -1 ) )
	{
		int i = Math.min( i0, i1 );
		int I = Math.max( i0, i1 );
		for
			( ; i <= I; i++ )
		{
			( (CoParagraphView) s.getView( i ) ).fit();
		}
	}
}


private void initKeyBindings()
{
 	JTextComponent.KeyBinding[] defaultBindings =
	{
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_F, KeyEvent.CTRL_MASK ), FIT_PARAGRAPH ),
	};




	Action[] localActions =
	{
		new FitParagraphAction(),
	};

	JTextComponent.loadKeymap( getKeymap(), defaultBindings, TextAction.augmentList( getActions(), localActions ) );
}

  private static final String FIT_PARAGRAPH = "FIT_PARAGRAPH";

	private static class FitParagraphAction extends TextAction
	{
		public FitParagraphAction()
		{
			super( FIT_PARAGRAPH );
		}

		public void actionPerformed( ActionEvent e )
		{
			CoStyledTextEditor target = (CoStyledTextEditor) e.getSource();
			if
				( target != null )
			{
				target.fitParagraph();
			}

		}
	}
}