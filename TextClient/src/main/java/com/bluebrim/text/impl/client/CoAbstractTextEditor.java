package com.bluebrim.text.impl.client;

import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.TextAction;
import javax.swing.text.Utilities;
import javax.swing.text.View;

import com.bluebrim.base.shared.CoLengthUnit;
import com.bluebrim.base.shared.CoPropertyChangeEvent;
import com.bluebrim.base.shared.CoPropertyChangeListener;
import com.bluebrim.font.shared.CoUnicode;
import com.bluebrim.gui.client.CoGUI;
import com.bluebrim.swing.client.CoUndoHandler;
import com.bluebrim.text.impl.client.actions.CoDeltaFloatCharacterOrParagraphAction;
import com.bluebrim.text.impl.shared.CoHyphenationConstants;
import com.bluebrim.text.impl.shared.CoStyleConstants;
import com.bluebrim.text.impl.shared.CoTextSearcher;
import com.bluebrim.text.shared.CoTextConstants;
import com.bluebrim.text.shared.swing.CoCommentView;

/**
 * Subklass av JTextPane som arbetar mot en com.bluebrim.text.shared.CoStyledDocumentIF-instans.
 * Följande har ändrats jämfört med JTextPane:
 *  - Metoden replaceSelection har skrivtis om så att det som stoppas 
 *    in "ärver" textutforming från det som ersätts.
 *  - Copy-paste av textutformning har lagts till.
 *  - Navigeringsmekanismerna känner till atomära strängar.
 * vojne ....
 */
 
public abstract class CoAbstractTextEditor extends JTextPane implements com.bluebrim.text.shared.CoTextEditorIF
{
	private CoCommentWindow m_commentWindow;
	private long m_openTime;
	private Element m_commentElement;
	
	private static final int SET_DOT = 1;
	private static final int MOVE_DOT = 2;
	private static final int FORCED = 3;
	private static final int CHANGE = FORCED;



	private int m_reason;
	private Element m_paragraphElement;
	private Element m_characterElement;
	
	private CoAttributeEvent m_event = new CoAttributeEvent( this );



	private boolean m_attributeNotificationEnabled = true;

	protected CoStyledTextPopupMenu m_popupMenu;

//	private CoSpellingSession m_ssce = new CoSpellingSession();

	private CoTextSearcher m_searcher;
	private CoTextSearchDialog m_searchDialog;


	private static ClipboardOwner m_defaultClipboardOwner = new ClipboardOwner()
	{
		public void lostOwnership( Clipboard clipboard, Transferable contents )
		{
		}
	};

	

	class AttributeEventDispatcher implements Runnable
	{
		private Object[] m_listeners;
		private CoAttributeEvent m_event;

		public void prepare( CoAttributeEvent e, Object[] listeners )
		{
			m_listeners = listeners;
			m_event = e;
		}
		
		public void run()
		{
			for
				(	int i = m_listeners.length - 2; i >= 0; i -= 2 )
			{
				if
					( m_listeners[ i ] == CoAttributeListenerIF.class )
				{
					fire( (CoAttributeListenerIF) m_listeners[ i + 1 ], m_event );
				}
			}
		}
	};

	private AttributeEventDispatcher m_attributeEventDispatcher = new AttributeEventDispatcher();


	
	
	private DocumentListener m_documentListener;


	

	



	private static class SearchForwardAction extends TextAction
	{
		public static final String KEY = "SEARCH_FORWARD_ACTION";
		
		public SearchForwardAction()
		{
			super( KEY);
		}

		public void actionPerformed( ActionEvent e )
		{
			CoAbstractTextEditor target = (CoAbstractTextEditor) e.getSource();
			if
				( target != null )
			{
				if
					( ( target.m_searchDialog != null ) && target.m_searchDialog.isVisible() )
				{
					target.search( true );
				} else {
					target.startSearch( true );
				}
			}
		}
	}

	private static class SearchBackwardAction extends TextAction
	{
		public static final String KEY = "SEARCH_BACKWARD_ACTION";
		
		public SearchBackwardAction()
		{
			super( KEY );
		}

		public void actionPerformed( ActionEvent e )
		{
			CoAbstractTextEditor target = (CoAbstractTextEditor) e.getSource();
			if
				( target != null )
			{
				if
					( ( target.m_searchDialog != null ) && target.m_searchDialog.isVisible() )
				{
					target.search( false );
				} else {
					target.startSearch( false );
				}
			}
		}
	}


	private static abstract class TransformWordsAction extends TextAction
	{
		public TransformWordsAction( String name )
		{
			super( name );
		}

		protected abstract String transform( String str );
		
		public void actionPerformed( ActionEvent e )
		{
			CoAbstractTextEditor target = (CoAbstractTextEditor) e.getSource();
			if
				( target != null )
			{
				try
				{
					int a0 = target.getSelectionStart();
					int b0 = target.getSelectionEnd();
					int a1 = Utilities.getWordStart( target, a0 );
					int b1 = Utilities.getWordEnd( target, b0 );
					target.select( a1, b1 );
					target.replaceSelection( transform( target.getText( a1, b1 - a1 ) ) );
					target.select( a0, b0 );
				}
				catch ( BadLocationException ex )
				{
				}
			}
		}
	}

	


	private static class LowerWordsAction extends TransformWordsAction
	{
		public static final String KEY = "lower-words";
		
		public LowerWordsAction()
		{
			super( KEY );
		}

		protected String transform( String str )
		{
			int I = str.length();
			
			if ( I == 0 ) return str;

			StringBuffer sb = new StringBuffer( I );
			for
				( int i = 0; i < I; i++ )
			{
				char c = str.charAt( i );
				sb.append( Character.toLowerCase( c ) );
			}
			
			return sb.toString();
		}
	}

	


	private static class RaiseWordsAction extends TransformWordsAction
	{
		public static final String KEY = "raise-words";
		
		public RaiseWordsAction()
		{
			super( KEY );
		}

		protected String transform( String str )
		{
			int I = str.length();
			
			if ( I == 0 ) return str;

			StringBuffer sb = new StringBuffer( I );
			for
				( int i = 0; i < I; i++ )
			{
				char c = str.charAt( i );
				sb.append( Character.toUpperCase( c ) );
			}
			
			return sb.toString();
		}
	}

	


	private static class CapitalizeWordsAction extends TransformWordsAction
	{
		public static final String KEY = "capitalize-words";
		
		public CapitalizeWordsAction()
		{
			super( KEY );
		}

		protected String transform( String str )
		{
			int I = str.length();
			
			if ( I == 0 ) return str;

			StringBuffer sb = new StringBuffer( I );
			char c = str.charAt( 0 );
			sb.append( Character.toUpperCase( c ) );
			for
				( int i = 1; i < I; i++ )
			{
				if
					( Character.isWhitespace( c ) )
				{
					c = str.charAt( i );
					if
						( ! Character.isWhitespace( c ) )
					{
						c = Character.toUpperCase( c );
					} else {
						c = Character.toLowerCase( c );
					}
				} else {
					c = Character.toLowerCase( str.charAt( i ) );
				}
				sb.append( c );
			}
			
			return sb.toString();
		}
	}


	

	
	private static class InsertSpecialAction extends TextAction
	{
		protected String m_insertString;
		
		public InsertSpecialAction( String name, String i )
		{
			super( name );
			m_insertString = i;
		}

		public void actionPerformed( ActionEvent e )
		{
			if ( m_insertString == null ) return;
			
			CoAbstractTextEditor target = (CoAbstractTextEditor) e.getSource();
			if
				( target != null )
			{
				target.replaceSelection( m_insertString );
			}
		}
	}

	private CoUndoHandler m_undoHandler = new CoUndoHandler();

	public static final String INSERT_HYPHENATION_POINT = "CoAbstractTextEditor.INSERT_HYPHENATION_POINT";
	public static final String INSERT_LINEFEED = "CoAbstractTextEditor.INSERT_LINEFEED";
	public static final String INSERT_NON_BREAK_SPACE = "CoAbstractTextEditor.INSERT_NON_BREAK_SPACE";	
	public static final String INSERT_TEST_TEXT = "CoAbstractTextEditor.INSERT_TEST_TEXT";
	private static final String TEST_TEXT = "Fantomens förintelse\nFörra fredagen fiskade Fantomen foreller för fullt. Fule Frans från Fritsla fräste fränt förbi. Frans frågade försynt:\n- Får Fantomen fisk?\nFantomen flinade fräckt:\n- Fyra förträffligt fina fiskar!\nFrans följde Fantomen förbi Fridas flaskaffär, fotbollsplanen, fallskärmshopparklubben framför fantomengrottan. Fantomen friterade fiskarna förstås. Frans förtärde Fantomens friterade fiskar.\n-Fy Fabian för fet fisk, förklarade Frans. Fantomen fräste:\n-Förgrymmade fä! Fantomen friterar fullt förtärbara fiskar, fina foreller!\nFrans fifflade fram fjorton frätande förintelsepiller. Frans fäste fast förintelsepillren framför Fantomens fotsvettiga, formlösa fötter. Förintelsepillren forsade frätande fram. Frans flinade finurligt.\nFantomen förintades!\n";

	public static final String INSERT_ANTI_HYPHENATION_POINT = "CoAbstractTextEditor.INSERT_ANTI_HYPHENATION_POINT";
	private static final Object [] [] INSERT_CHARACTERS =
		new Object [] []
		{
			//              inserted string                                        name                            keystroke
			//                                                                     null == inserted string         optional
			new Object [] { "\r",                                                  INSERT_LINEFEED,                KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, KeyEvent.SHIFT_MASK ) },
			new Object [] { CoTextConstants.NO_BREAK_SPACE_STRING,                 INSERT_NON_BREAK_SPACE,         KeyStroke.getKeyStroke( KeyEvent.VK_SPACE, KeyEvent.SHIFT_MASK | KeyEvent.CTRL_MASK ) },
			new Object [] { CoHyphenationConstants.HYPHENATION_POINT_STRING,       INSERT_HYPHENATION_POINT,       KeyStroke.getKeyStroke( KeyEvent.VK_MINUS, KeyEvent.CTRL_MASK ) },
			new Object [] { CoHyphenationConstants.ANTI_HYPHENATION_POINT_STRING,  INSERT_ANTI_HYPHENATION_POINT,  KeyStroke.getKeyStroke( KeyEvent.VK_MINUS, KeyEvent.SHIFT_MASK | KeyEvent.CTRL_MASK ) },
			new Object [] { null }, // separator
			new Object [] { "" + CoUnicode.DEGREE_SIGN,                            null,                           KeyStroke.getKeyStroke( KeyEvent.VK_G, KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK ) },
			new Object [] { "" + CoUnicode.MULTIPLICATION_SIGN,                    null,                           KeyStroke.getKeyStroke( KeyEvent.VK_MULTIPLY, KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK ) },
			new Object [] { "" + CoUnicode.DIVISION_SIGN,                          null,                           KeyStroke.getKeyStroke( KeyEvent.VK_DIVIDE, KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK ) },
			new Object [] { "" + CoUnicode.NOT_SIGN,                               null,                           null },
			new Object [] { "" + CoUnicode.BULLET,                                 null,                           KeyStroke.getKeyStroke( KeyEvent.VK_PERIOD, KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK ) },
			new Object [] { "" + CoUnicode.EN_DASH,                                null,                           null },
			new Object [] { "" + CoUnicode.EM_DASH,                                null,                           null },
			new Object [] { "" + CoUnicode.PLUS_MINUS_SIGN,                        null,                           KeyStroke.getKeyStroke( KeyEvent.VK_PLUS, KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK ) },
			new Object [] { "" + CoUnicode.PER_MILLE_SIGN,                         null,                           KeyStroke.getKeyStroke( KeyEvent.VK_5, KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK ) },
			new Object [] { null }, // separator
			new Object [] { TEST_TEXT,                                             INSERT_TEST_TEXT,               KeyStroke.getKeyStroke( KeyEvent.VK_T, KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK ) },
		};
  

/**
 * 
 */
public CoAbstractTextEditor()
{
	super( );

	init();
}
/**
 * 
 */
public CoAbstractTextEditor( com.bluebrim.text.shared.CoStyledDocumentIF doc )
{
	super( doc );

	init();
}
public void addAttributeListener( CoAttributeListenerIF l )
{
	listenerList.add( CoAttributeListenerIF.class, l );
}
private void checkSelectedAttributes( int reason )
{
	if ( ! m_attributeNotificationEnabled ) return;

	final boolean TRACE = !true;
	
	long t = System.currentTimeMillis();
	if ( TRACE ) System.err.println( "checkSelectedAttributes  " + reason );
	
	com.bluebrim.text.shared.CoStyledDocumentIF doc = getCoStyledDocument();
		
	int dot = getCaret().getDot();
	int mark = getCaret().getMark();
	int pos = getSelectionStart();
	int len = getSelectionEnd() - pos;
	
	Element e = doc.getParagraphElement( dot );

	if
		( ( reason == FORCED ) ||
			( reason == SET_DOT ) ||
			( ( reason == MOVE_DOT ) &&
				( ( dot == pos + len ) ||
					( mark == pos + len ) ) ) )
	{
		if ( dot > 0 ) dot--;
	}

	if
		( ( reason == MOVE_DOT ) ||
			( reason == FORCED ) ||
			( reason != m_reason ) )
	{
		m_paragraphElement = m_characterElement = null;
	}

	if
		( e != m_paragraphElement )
	{
		m_paragraphElement = e;
		m_characterElement = doc.getCharacterElement( dot );
		
		m_event.set( doc.getParagraphAttributes( pos, len ),
			           getCharacterAttributes( pos, len ),//- 1 ),
			           pos,
			           pos + len,
			           doc.getParagraphElement( pos ) != doc.getParagraphElement( pos + len ),
			           true );
		fireAttributeUpdate( m_event );
	} else {
		e = doc.getCharacterElement( dot );
		if
			( e != m_characterElement )
		{
			m_characterElement = e;
			
			m_event.set( doc.getParagraphAttributes( pos, len ),
				           getCharacterAttributes( pos, len ),//- 1 ),
				           pos,
				           pos + len,
				           doc.getParagraphElement( pos ) != doc.getParagraphElement( pos + len ),
				           false );
			fireAttributeUpdate( m_event );
		}
	}

	m_reason = reason;

	if ( TRACE ) System.err.println( "checkSelectedAttributes << " + ( System.currentTimeMillis() - t ) );

	

//	System.err.println( ( (com.bluebrim.text.shared.CoStyledDocument) doc ).getAttributes( pos, len ) );

}
public void checkSpelling( Object properties )
{
//	m_ssce.update( properties );
//	
//	CoTextPaneWordParser parser =
//		new CoTextPaneWordParser( this,
//			                       m_ssce.getOption( CoSpellingSession.SPLIT_HYPHENATED_WORDS_OPT ),
//			                       false );
//		
//	CoSpellCheckDialog d =
//		new CoSpellCheckDialog( CoGUI.getFrameFor(this),
//			                      m_ssce,
//			                      parser,
//			                      m_ssce.m_comparator,
//			                      m_ssce.m_userLexicons );
//		
//	d.minSuggestDepth = m_ssce.m_minSuggestDepth;
//	d.setVisible( true );
	throw new UnsupportedOperationException("Until Wintertree spell checker is replaced by a GPL spell checker");

}
public void copy()
{
	//	m_clipboard = new NormalCopy();
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	Caret caret = getCaret();
	
	int p0 = Math.min( caret.getDot(), caret.getMark() );
	int p1 = Math.max( caret.getDot(), caret.getMark() );
	if
		( p0 != p1 )
	{
		CoStyledTextSelection contents = new CoStyledTextSelection( getCoStyledDocument(), p0, p1 - p0 );
		clipboard.setContents( contents, m_defaultClipboardOwner );
	}
}
public void copyStyles( AttributeSet a, boolean isParagraphSelection )
{
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	Caret caret = getCaret();
	
	int p0 = Math.min( caret.getDot(), caret.getMark() );
	int p1 = Math.max( caret.getDot(), caret.getMark() );
	if
		( p0 != p1 )
	{
		CoCharacterStyleSelection contents = new CoCharacterStyleSelection( a, isParagraphSelection );
		clipboard.setContents( contents, m_defaultClipboardOwner );
	}
}
public void cut()
{
	//	m_clipboard = new NormalCut();
	if
		( isEditable() && isEnabled() )
	{
		copy();
		try
		{
			Caret caret = getCaret();
			int p0 = Math.min( caret.getDot(), caret.getMark() );
			int p1 = Math.max( caret.getDot(), caret.getMark() );
			getDocument().remove( p0, p1 - p0 );
		}
		catch (BadLocationException e)
		{
		}
	} else {
		getToolkit().beep();
	}
}
private void doImportStyledDocument( File f )
{
//	Cursor c = getCursor();
//	setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
	
	com.bluebrim.text.shared.CoAbstractTextImporter [] importers = com.bluebrim.text.shared.CoAbstractTextImporter.getImporters();
	
	for
		( int i = 0; i < importers.length; i++ )
	{
		try
		{
			Reader r = new FileReader( f );

			// copy current document to make sure we get the tag definitions
			com.bluebrim.text.shared.CoStyledDocument doc = (com.bluebrim.text.shared.CoStyledDocument) getCoStyledDocument().getCopy();//new com.bluebrim.text.shared.CoStyledDocument( getCoStyledDocument() );
			
			try
			{
				doc.clear();
				importers[ i ].doImport( r, doc );
			}
			catch ( Exception ex )
			{
				r.close();
				// try next importer
				continue;
			}

			r.close();

			setCoStyledDocument( doc );
			String log = importers[ i ].getLog();
			if
				( ( log != null ) && ( log.length() > 0 ) )
			{
				JOptionPane.showMessageDialog( this, log, "", JOptionPane.INFORMATION_MESSAGE );
			}
//			setCursor( c );
			return;
		}
		catch ( FileNotFoundException ex )
		{
			JOptionPane.showMessageDialog( this, ex, "", JOptionPane.ERROR_MESSAGE );
			break;
		}
		catch ( IOException ex )
		{
			JOptionPane.showMessageDialog( this, ex, "", JOptionPane.ERROR_MESSAGE );
			break;
		}
	}

	JOptionPane.showMessageDialog( this, "Could not parse file " + f.getName(), "", JOptionPane.ERROR_MESSAGE );
//	setCursor( c );
}
public void editSpellCheckOptions( Frame f, Object properties )
{
//	CoSpellOptionsDialog d = new CoSpellOptionsDialog( f, properties );
//	d.setVisible( true );
	throw new UnsupportedOperationException("Until Wintertree spell checker is replaced by a GPL spell checker");
}

private CoCommentView findCommentView( View V, int x, int y )
{
	int I = V.getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		View v = V.getView( i );

		if
			( v instanceof CoCommentView )
		{
			if ( ( (CoCommentView) v ).contains( x, y ) ) return (CoCommentView) v;
		}

		CoCommentView tmp = findCommentView( v, x, y );
		if ( tmp != null ) return tmp;
	}

	return null;
}
private void fire( CoAttributeListenerIF l, CoAttributeEvent ev )
{
	l.attributesChanged( ev );
}
protected void fireAttributeUpdate( CoAttributeEvent e )
{
	m_attributeEventDispatcher.prepare( e, listenerList.getListenerList() );

	SwingUtilities.invokeLater( m_attributeEventDispatcher );
}
public AttributeSet getCharacterAttributes( int offset, int length )
{
	MutableAttributeSet as = (MutableAttributeSet) getCoStyledDocument().getCharacterAttributes( offset, length );
	
	if
		( length > 1 )
	{
		Object tracking = getCoStyledDocument().getCharacterAttribute( offset, length - 1, CoTextConstants.TRACK_AMOUNT );
		if ( tracking != null ) as.addAttribute( CoTextConstants.TRACK_AMOUNT, tracking );
	}

	return as;
}
public JComponent getComponent()
{
	return this;
}
public com.bluebrim.text.shared.CoStyledDocumentIF getCoStyledDocument()
{
	return (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument(); 
}
public static String getInsertedString( int i )
{
	Object [] tmp = INSERT_CHARACTERS [ i ];
	return (String) tmp [ 0 ];
}
public static int getInsertedStringCount()
{
	return INSERT_CHARACTERS.length;
}
public static KeyStroke getInsertedStringKeyStroke( int i )
{
	Object [] tmp = INSERT_CHARACTERS [ i ];
	return ( tmp.length > 2 ) ? (KeyStroke) tmp [ 2 ] : null;
}
public static String getInsertedStringName( int i )
{
	Object [] tmp = INSERT_CHARACTERS [ i ];
	String name = ( tmp.length > 1 ) ? (String) tmp [ 1 ] : null;
	if ( name == null ) name = getInsertedString( i );
	return name;
}
public AttributeSet getSelectedCharacterAttributes()
{
	int start = getSelectionStart();
	int length = getSelectionEnd() - start;
	if ( length > 0 ) length--;
	
	return getCharacterAttributes( start, length );
}
public AttributeSet getSelectedParagraphAttributes()
{
	int start = getSelectionStart();
	int length = getSelectionEnd() - start;
	
	return getCoStyledDocument().getParagraphAttributes( start, length );
}
public CoUndoHandler getUndoHandler()
{
	return m_undoHandler;
}
public void importStyledDocument( File f )
{
	// PENDING: check f.length() ???
	
	doImportStyledDocument( f );
}
private void init()
{
  setEditorKit( createDefaultEditorKit() );

  setCaret( new CoAtomAwareCaret() );

  initKeyBindings();


	
	getStyledDocument().addDocumentListener( m_documentListener );

	addCaretListener(
		new CaretListener()
		{
			public void caretUpdate( CaretEvent e )
			{
				if
					( e.getDot() == e.getMark() )
				{
					checkSelectedAttributes( SET_DOT );
				} else {
					checkSelectedAttributes( MOVE_DOT );
				}
			}
		}
	);


	setCursor( Cursor.getPredefinedCursor( Cursor.TEXT_CURSOR ) );

	
	CoLengthUnit.LENGTH_UNITS.addPropertyChangeListener(
		new CoPropertyChangeListener()
		{
			public void propertyChange( CoPropertyChangeEvent ev )
			{
				checkSelectedAttributes( FORCED );
			}
		}
	);

}
private void initKeyBindings()
{
	final String INCREASE_FONT_SIZE = "INCREASE_FONT_SIZE";
	final String DECREASE_FONT_SIZE = "DECREASE_FONT_SIZE";
	final String INSERT_TAG_CHAIN_IGNORING_RETURN = "INSERT_TAG_CHAIN_IGNORING_RETURN";

	 
 	JTextComponent.KeyBinding[] defaultBindings =
	{
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_S, KeyEvent.CTRL_MASK ), SearchForwardAction.KEY ),
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_R, KeyEvent.CTRL_MASK ), SearchBackwardAction.KEY ),
		
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ), DefaultEditorKit.deleteNextCharAction ),
		
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, KeyEvent.ALT_MASK ), INSERT_TAG_CHAIN_IGNORING_RETURN ),
		
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_F9, 0 ), INSERT_HYPHENATION_POINT ),// VAJ
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_F9, KeyEvent.SHIFT_MASK ), INSERT_ANTI_HYPHENATION_POINT ), // VAJ
		
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_F4, 0 ), LowerWordsAction.KEY ),
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_F4, KeyEvent.SHIFT_MASK ), RaiseWordsAction.KEY ),
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_F4, KeyEvent.CTRL_MASK ), CapitalizeWordsAction.KEY ),
		
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_F5, 0 ), INCREASE_FONT_SIZE ),
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_F5, KeyEvent.ALT_MASK ), INCREASE_FONT_SIZE ),
		
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_F6, 0 ), DECREASE_FONT_SIZE ),
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_F6, KeyEvent.ALT_MASK ), DECREASE_FONT_SIZE ),

		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, KeyEvent.ALT_MASK ), (String) m_undoHandler.getUndoAction().getValue( Action.NAME ) ),
		new JTextComponent.KeyBinding( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK ), (String) m_undoHandler.getRedoAction().getValue( Action.NAME ) ),
	};
	
	Action [] localActions =
		new Action []
		{
			new SearchForwardAction(),
			new SearchBackwardAction(),
			new CoAtomAwareDeletePrevAction(),
			new CoAtomAwareDeleteNextAction(),
			new InsertSpecialAction( INSERT_TAG_CHAIN_IGNORING_RETURN, com.bluebrim.text.shared.CoStyledDocument.TAG_CHAIN_IGNORING_RETURN ),
//			new InsertSpecialAction( INSERT_LINEFEED, "\r" ),
//			new InsertSpecialAction( INSERT_NON_BREAK_SPACE, CoTextConstants.NO_BREAK_SPACE_STRING ),
//			new InsertSpecialAction( INSERT_HYPHENATION_POINT, CoHyphenationConstants.HYPHENATION_POINT_STRING ),
//			new InsertSpecialAction( INSERT_ANTI_HYPHENATION_POINT, CoHyphenationConstants.ANTI_HYPHENATION_POINT_STRING ),
			new LowerWordsAction(),
			new RaiseWordsAction(),
			new CapitalizeWordsAction(),
			new CoDeltaFloatCharacterOrParagraphAction( CoTextConstants.FONT_SIZE, INCREASE_FONT_SIZE, 1, 2, 720 ),
			new CoDeltaFloatCharacterOrParagraphAction( CoTextConstants.FONT_SIZE, DECREASE_FONT_SIZE, -1, 2, 720 ),
			m_undoHandler.getUndoAction(),
			m_undoHandler.getRedoAction(),
		};


	int N = 0;
 	for
 		( int i = 0; i < getInsertedStringCount(); i++ )
 	{
	 	if ( getInsertedStringKeyStroke( i ) != null ) N++;
 	}


 	JTextComponent.KeyBinding[] tmpBindings = defaultBindings;
 	defaultBindings = new JTextComponent.KeyBinding [ tmpBindings.length + N ];
 	for
 		( int i = 0; i < tmpBindings.length; i++ )
 	{
	 	defaultBindings[ i ] = tmpBindings[ i ];
 	}

	Action[] tmpActions = localActions;
	localActions = new Action [ tmpActions.length + N ];
 	for
 		( int i = 0; i < tmpActions.length; i++ )
 	{
	 	localActions[ i ] = tmpActions[ i ];
 	}

 	for
 		( int i = 0, n = 0; i < getInsertedStringCount(); i++ )
 	{
		KeyStroke keyStroke = getInsertedStringKeyStroke( i );

		if
			( keyStroke != null )
		{
			String str = getInsertedString( i );
			if ( str == null ) continue;

			String name = getInsertedStringName( i );
			
		 	defaultBindings[ tmpBindings.length + n ] = new JTextComponent.KeyBinding( keyStroke, name );
		 	localActions[ tmpActions.length + n ] = new InsertSpecialAction( name, str );
		 	n++;
		}
 	}



	
	JTextComponent.loadKeymap( getKeymap(), defaultBindings, TextAction.augmentList( getActions(), localActions ) );

}
public void insertComment( String c )
{
	MutableAttributeSet inputAttributes = getInputAttributes();
	inputAttributes.removeAttributes( inputAttributes );
	CoStyleConstants.setComment( inputAttributes, c );
	replaceSelection( " " );
	inputAttributes.removeAttributes( inputAttributes );
}
public void insertIcon( Icon icon )
{
	if
		( ! isEditable() )
	{
		getToolkit().beep();
		return;
	}

	super.insertIcon( icon );
}
protected void paintComponent( Graphics g )
{
	if
		( isOpaque() && ( getBackground() != null ) )
	{
		Graphics2D G = (Graphics2D) g;
		
		double s = com.bluebrim.base.shared.CoBaseUtilities.getXScale( G.getTransform() );

		G.setColor( getBackground() );
		G.fillRect( 0, 0, (int) ( getWidth() / s ), (int) ( getHeight() / s ) );
	}
	
	super.paintComponent( g );
}
public void paste()
{
	Caret caret = getCaret();
	int p0 = Math.min( caret.getDot(), caret.getMark() );
	int p1 = Math.max( caret.getDot(), caret.getMark() );
	
	super.paste();
	
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	Transferable content = clipboard.getContents( this );
/*	
	Iterator i = Arrays.asList( content.getTransferDataFlavors() ).iterator();
	while
		( i.hasNext() )
	{
		DataFlavor df = (DataFlavor) i.next();
		System.err.println( df.getMimeType() );
	}
*/

	if
		( content != null )
	{
		
		try
		{
			CoStyledTextSelection dstData = (CoStyledTextSelection) ( content.getTransferData( CoStyledTextSelection.FLAVOR ) );
			dstData.paste( getCoStyledDocument(), p0 );
		}
		catch( Exception e )
		{
			try
			{
				CoCharacterStyleSelection dstData = (CoCharacterStyleSelection) ( content.getTransferData( CoCharacterStyleSelection.FLAVOR ) );
				dstData.paste( getCoStyledDocument(), p0, p1 - p0 );
			}
			catch( Exception ex )
			{
				getToolkit().beep();
			}
		}
		
	}
}
public void positionCaret( MouseEvent e )
{
	( (CoAtomAwareCaret) getCaret() ).mousePressed( e );
}
private boolean postCommentWindow( int x, int y )
{
	// find view at mouse position
	CoCommentView v = findCommentView( getUI().getRootView( this ).getView( 0 ), x, y );

	// not a comment
	if ( v == null) return false;

	m_commentElement = v.getElement();

	String comment = (String) m_commentElement.getAttributes().getAttribute( CoTextConstants.COMMENT );
	if
		( m_commentWindow == null )
	{
		m_commentWindow = new CoCommentWindow( (JFrame) getTopLevelAncestor() );
	}
	Point p = getLocationOnScreen();
	m_commentWindow.display( comment, p.x + x, p.y + y );
  m_openTime = System.currentTimeMillis();
  return true;
}
private boolean postCommentWindow( MouseEvent e )
{
	return postCommentWindow( e.getX(), e.getY() );
}
protected void preparePopupMenu()
{
	( (CoStyledTextPopupMenu) m_popupMenu ).updatePopupMenu( getSelectedParagraphAttributes(),
		                                                             getSelectedCharacterAttributes(),
		                                                             getSelectionStart(),
		                                                             getSelectionEnd() );
}
protected void processMouseEvent( MouseEvent e )
{
	final boolean TRACE = !true;

	long t = System.currentTimeMillis();

	if ( TRACE ) System.err.println( "processMouseEvent " + e.getID() + "  " + ( System.currentTimeMillis() ) );

	// popup menu
	if
		( ( m_popupMenu != null ) && e.isPopupTrigger() )
	{
		preparePopupMenu();
		m_popupMenu.show( e.getComponent(), e.getX(), e.getY() );
		return;
	}
	

	// comments
	if
		( e.getID() == MouseEvent.MOUSE_PRESSED )
	{
		if
			( postCommentWindow( e ) )
		{		
			return;
		}
	} else if
		( e.getID() == MouseEvent.MOUSE_RELEASED )
	{
		unpostCommentWindow();
	}

	if
		( ( m_commentWindow != null ) && m_commentWindow.isVisible() )
	{
		e.consume();
		return;
	}
	if (e.getSource() instanceof JTextComponent)
		super.processMouseEvent(e);
	
	if ( TRACE ) System.err.println( "processMouseEvent <<<  " + ( System.currentTimeMillis() ) + "  " + ( System.currentTimeMillis() - t ) );
}
protected void processMouseMotionEvent (MouseEvent e)
{
	if ( ( m_commentWindow != null ) && m_commentWindow.isVisible() ) return;
		
	super.processMouseMotionEvent(e);
}
public void removeAttributeListener( CoAttributeListenerIF l )
{
	listenerList.remove( CoAttributeListenerIF.class, l );
}
public void replaceSelection( String content )
{
	if
		( ! isEditable() )
	{
		getToolkit().beep();
		return;
	}
	
	com.bluebrim.text.shared.CoStyledDocumentIF doc = getCoStyledDocument();
	if
		( doc != null )
	{
		try
		{
			Caret caret = getCaret();
			int p0 = Math.min(caret.getDot(), caret.getMark());
			int p1 = Math.max(caret.getDot(), caret.getMark());

			if
				( content != null && content.length() > 0 )
			{
				setCaretPosition( p1 );
				// make sure macro attribute doesn't propagate
				MutableAttributeSet as = getInputAttributes();

				doc.clearAtomAttributes( as );
				
				doc.insertString( p1, content, as );
				caret.setDot( p1 + content.length() );
			}
			if
				( p0 != p1 )
			{
				doc.remove( p0, p1 - p0 );
			}
		}
		catch (BadLocationException e)
		{
			getToolkit().beep();
		}
	}
}
public void search( String target, boolean forward )
{
	if
		( m_searcher == null )
	{
		m_searcher = new CoTextSearcher();
	}
	
	m_searcher.setDocument( getCoStyledDocument() );
	m_searcher.setTarget( target );
	m_searcher.setForward( forward );
	m_searcher.setPosition( Math.min( getCaret().getDot(), getCaret().getMark() ) );

	search( forward );
}
public void search( boolean forward )
{
	if ( ! m_searcher.isActive() ) return;

	m_searcher.setForward( forward );
	int p = m_searcher.search();
	if
		( p == CoTextSearcher.DONE )
	{
		m_searcher.deactivate();
		m_searchDialog.setVisible( false );
		return;
	}

	select( p, p + m_searcher.getTarget().length() );
}
public void setAttributeNotificationEnabled( boolean enabled )
{
	m_attributeNotificationEnabled = enabled;
	if ( m_attributeNotificationEnabled ) checkSelectedAttributes( FORCED );
}
public void setCoStyledDocument( com.bluebrim.text.shared.CoStyledDocumentIF doc )
{
	setStyledDocument( doc );
}
public void setDocument( Document doc )
{
	Document old = getDocument();
	if
		( old != null )
	{	
		old.removeDocumentListener( m_documentListener );
		if(m_undoHandler !=null)
			old.removeUndoableEditListener( m_undoHandler );
		if ( m_undoHandler != null ) m_undoHandler.discardAllEdits();
	}
	
	super.setDocument( doc == null ? new com.bluebrim.text.shared.CoStyledDocument() : doc);

	if 
		( m_documentListener == null )
	{
		m_documentListener = new DocumentListener()
			{
				public void insertUpdate( DocumentEvent e ) {}
				public void removeUpdate( DocumentEvent e ) {}
				public void changedUpdate( DocumentEvent e )
				{
					checkSelectedAttributes( CHANGE );
				}
			};
	}

	if
		(doc != null)
	{
		doc.addDocumentListener( m_documentListener );
		if(m_undoHandler!=null)	
			doc.addUndoableEditListener( m_undoHandler );
	}
}
public void setEditable( boolean e )
{
	super.setEditable( e );

	if
		( m_attributeEventDispatcher != null )
	{
		m_event.set( true );
		fireAttributeUpdate( m_event );
	}
}
public void setPopupMenu( CoStyledTextPopupMenu popupMenu )
{
	m_popupMenu = popupMenu;
}
public void setStyledDocument( StyledDocument doc )
{
	if
		( doc != null && ! ( doc instanceof com.bluebrim.text.shared.CoStyledDocumentIF ) )
	{
		throw new IllegalArgumentException( "CoColumnTextPane.setStyledDocument( StyledDocument doc ), doc must be an com.bluebrim.text.shared.CoStyledDocumentIF." );
	}

	setDocument( doc );
}
public void startSearch( boolean forward )
{
	if
		( m_searchDialog == null )
	{
		Window w = (Window) getTopLevelAncestor();
		if
			( w instanceof Dialog )
		{
			m_searchDialog = new CoTextSearchDialog( (Dialog) w, this );
		} else {
			m_searchDialog = new CoTextSearchDialog( (Frame) w, this );
		}
		m_searchDialog.pack();
		m_searchDialog.addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing( WindowEvent e )
				{
					m_searcher = null;
					requestFocus();
				}
			}
		);
	}

	m_searchDialog.setForward( forward );
	
	Point p = getLocationOnScreen();
	p.y -= m_searchDialog.getPreferredSize().getHeight();
	m_searchDialog.setLocation( p );
	m_searchDialog.setVisible( true );
}
private void unpostCommentWindow()
{
	if ( m_commentWindow == null ) return;
	if ( ! m_commentWindow.isVisible() ) return;
	
	if
		( System.currentTimeMillis() - m_openTime < 200 )
	{
		m_commentWindow.edit(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent e )
				{
					MutableAttributeSet as = new com.bluebrim.text.shared.CoSimpleAttributeSet();
					as.addAttribute( CoTextConstants.COMMENT, e.getActionCommand() );
					getCoStyledDocument().setCharacterAttributes( m_commentElement.getStartOffset(), 1, as, false );
				}
			}
		);
	} else {
		m_commentWindow.setVisible( false );
	}
}
public void unsetCharacterAttributes( AttributeSet attributes )
{
	int p0 = getSelectionStart();
	int p1 = getSelectionEnd();
	if
		( p0 != p1 )
	{
		com.bluebrim.text.shared.CoStyledDocumentIF doc = getCoStyledDocument();
		doc.unsetCharacterAttributes( p0, p1 - p0, attributes );
	}
	else
	{
		MutableAttributeSet inputAttributes = getInputAttributes();
		inputAttributes.removeAttributes( attributes.getAttributeNames() );
	}
}
public void unsetParagraphAttributes( AttributeSet attributes )
{
	int p0 = getSelectionStart();
	int p1 = getSelectionEnd();
	com.bluebrim.text.shared.CoStyledDocumentIF doc = getCoStyledDocument();
	doc.unsetParagraphAttributes( p0, p1 - p0, attributes );
}
}