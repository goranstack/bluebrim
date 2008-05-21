package com.bluebrim.swing.client;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.resource.shared.*;


/**
 * Replacement for JComboBox (more lightweight, can't be editable).
 * Creation date: (2000-11-03 11:14:39)
 * @author: Dennis
 */

public class CoOptionMenu extends JLabel implements ItemSelectable, ListDataListener, CoAsIsCapable, KeyListener
{
	private JList m_list = new JList();
	
	private static JPopupMenu m_popup = new JPopupMenu();
	private ComboBoxModel m_model;
	private Object m_selection;
	private int m_popupDeltaY;

	private int m_iconSize;

	private boolean m_asIs = false;


	private Object m_nullItem = null;
	private int m_nullIndex = -1;

	private boolean m_keepQuiet = false;
	
	
	private ListCellRenderer m_renderer = new DefaultRenderer();


	public static class DefaultRenderer extends JLabel implements ListCellRenderer
	{
		private Dimension m_size;

		public DefaultRenderer()
		{
			setOpaque( true );
		}

		public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
		{
			String s = null;
			if
				( value instanceof String )
			{
				s = (String) value;
			} else if
				( value instanceof CoNamed )
			{
				s = ( (CoNamed) value ).getName();
			} else if
				( value instanceof Number )
			{
				s = m_numberFormat.format( value );
			} else {
				s = ( value == null ) ? null : value.toString();
			}

			setText( s );
			this.setFont( list.getFont() );

			if
				( isSelected )
			{
				setBackground( list.getSelectionBackground() );
				setForeground( list.getSelectionForeground() );
			} else {
				setBackground( list.getBackground() );
				setForeground( list.getForeground() );
			}

			setOpaque( index != -1 );
			
			return this;
		}
		
		public Dimension getPreferredSize()
		{
			Dimension size;
			
			if
				( ( getText() == null ) || ( getText().equals( "" ) ) )
			{
				setText( " " );
				size = super.getPreferredSize();
				setText( "" );
			} else {
				size = super.getPreferredSize();
			}
			
			return size;
		}
	};
	

	public static class TranslatingRenderer extends DefaultRenderer
	{
		private ListCellRenderer m_delegate;
		private CoOldResourceBundle m_bundle;

		public TranslatingRenderer( CoOldResourceBundle bundle )
		{
			this( bundle, null );
		}

		private TranslatingRenderer( CoOldResourceBundle bundle, ListCellRenderer delegate )
		{
			m_delegate = delegate;
			m_bundle = bundle;
		}
		
		private Object getName( Object key )
		{
			if ( key == null ) return null;
			
			try
			{
				return m_bundle.getString( key.toString() );
			}
			catch ( java.util.MissingResourceException ex )
			{
				return key;
			}			
		}
		
		public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
		{
			if
				( m_delegate == null )
			{
				return super.getListCellRendererComponent( list, getName( value ), index, isSelected, cellHasFocus );
			} else {
				return m_delegate.getListCellRendererComponent( list, getName( value ), index, isSelected, cellHasFocus );
			}
		}
	};



	private static final NumberFormat m_numberFormat = NumberFormat.getInstance( Locale.getDefault() );


public CoOptionMenu()
{
	this( new DefaultComboBoxModel() );
}
public CoOptionMenu( ComboBoxModel model )
{
	super();
	
	setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder( 0, 3, 0, 0 ) ) );
	
	setForeground( Color.black );

	setModel( model );

	initListeners();

	updateIconSize();


	m_list.setRequestFocusEnabled( false );
	m_list.setModel( m_model );
	m_list.setBackground( getBackground() );
	m_list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
	m_list.setFont( getFont() );
/*
	m_list.setCellRenderer(
		new DefaultListCellRenderer()
		{
			public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
			{
				Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
				m_renderer.configure( value, this, list, index, isSelected, cellHasFocus );
				return this;
			}
		}
	);
*/
	m_list.setCellRenderer( m_renderer );

	addKeyListener( this );
}
public void addActionListener( ActionListener l )
{
	listenerList.add( ActionListener.class, l );
}
public void addItem( Object item )
{
	if
		( m_model instanceof MutableComboBoxModel )
	{
		( (MutableComboBoxModel) m_model ).addElement( item );
	}
}
public void addItem(Object anObject, boolean quiet)
{
	m_keepQuiet	= quiet;
	addItem(anObject);
	m_keepQuiet	= false;
}
public void addItemListener( ItemListener l )
{
	listenerList.add( ItemListener.class, l );
}
public void addNullItem( Object nullItem )
{
	m_nullItem = nullItem;
	m_nullIndex = m_model.getSize();
	addItem( nullItem );
}
public void addNullItem( Object anObject, boolean quiet )
{
	m_keepQuiet	= quiet;
	addNullItem( anObject );
	m_keepQuiet	= false;
}
public void contentsChanged( ListDataEvent ev )
{
	if
		( ev.getIndex0() == -1 )
	{
		selectionChanged();
		return;
	}

	update();

	if
		( m_selection == null )
	{
		m_model.setSelectedItem( m_model.getElementAt( 0 ) );
	} else {
		m_model.setSelectedItem( m_selection );
	}
}

protected void fireActionEvent( String command )
{
	ActionEvent e = null;
	// Guaranteed to return a non-null array
	Object[] listeners = listenerList.getListenerList();
	// Process the listeners last to first, notifying
	// those that are interested in this event
	for
		( int i = listeners.length - 2; i >= 0; i -= 2 )
	{
		if
			( listeners[i] == ActionListener.class )
		{
			if (e == null) e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command );
			((ActionListener) listeners[i + 1]).actionPerformed(e);
		}
	}
}
protected void fireItemStateChanged( ItemEvent e )
{
	// Guaranteed to return a non-null array
	Object[] listeners = listenerList.getListenerList();
	// Process the listeners last to first, notifying those that are interested in this event
	for
	(int i = listeners.length - 2; i >= 0; i -= 2)
	{
		if
		(listeners[i] == ItemListener.class)
		{
			// Lazily create the event:
			// if (changeEvent == null)
			// changeEvent = new ChangeEvent(this);
		 	((ItemListener) listeners[i + 1]).itemStateChanged(e);
		}
	}
}
public Object getItem( int i )
{
	return m_model.getElementAt( i );
}
public int getItemCount()
{
	return m_model.getSize();
}
public ComboBoxModel getModel()
{
	return m_model;
}
public Dimension getPreferredSize()
{
	return m_preferredSize;
}

public int getSelectedIndex()
{
	Object sObject = m_model.getSelectedItem();
	int i, c;
	Object obj;
	for (i = 0, c = m_model.getSize(); i < c; i++)
	{
		obj = m_model.getElementAt(i);
		if (obj.equals(sObject))
			return i;
	}
	return -1;
}
public Object getSelectedItem()
{
	return ( m_selection == m_nullItem ) ? null : m_selection;
}
public Object[] getSelectedObjects()
{
	return new Object [] { m_selection };
}
private void initListeners()
{
	class Listener implements MouseListener, MouseMotionListener, FocusListener, KeyListener
	{
		private boolean m_didDrag;
		private boolean m_recentlyOpened;
		
		public void mouseClicked( MouseEvent ev ) {}
		public void mouseEntered( MouseEvent ev ) {}
		public void mouseExited( MouseEvent ev ) {}
		public void mouseMoved( MouseEvent ev ) {}
		public void keyTyped( KeyEvent ev ) {}

		public void mousePressed( MouseEvent ev )
		{
//			System.err.println( "mousePressed" );
			if
				( ! m_popup.isVisible() )
			{
				m_didDrag = false;
				m_recentlyOpened = true;
//				System.err.println( "  postMenu" );
				postMenu();
			}
		}

		public void mouseReleased( MouseEvent ev )
		{
//			System.err.println( "mouseReleased" );
			if
				( ! m_recentlyOpened && m_popup.isVisible() )
			{	
//				System.err.println( "  unpostMenu" );
				unpostMenu();
				return;
			}
			
			m_recentlyOpened = false;
			
			if
				( m_didDrag && m_popup.isVisible() )
			{	
				select();
//				System.err.println( "  unpostMenu" );
				unpostMenu();
			}
		}

		public void mouseDragged( MouseEvent ev )
		{
			if 
				( m_popup.isVisible() )
			{	
				m_didDrag = true;
				updateListBoxSelectionForEvent( ev, m_popupDeltaY, false );
			}
		}
		
		public void focusGained( FocusEvent e )
		{	
			( (Component) e.getSource() ).repaint();
		}
			
		public void focusLost( FocusEvent e )
		{
			if ( e.isTemporary() ) return;
//			System.err.println( "focusLost" );
			if
				( m_popup.isVisible() )
			{	
				unpostMenu();
			}
			( (Component) e.getSource() ).repaint();
		}
		
		public void keyPressed( KeyEvent ev )
		{
			if ( ! m_popup.isVisible() ) return;
			
			if
				( ev.getKeyCode() == KeyEvent.VK_UP )
			{
				m_list.setSelectedIndex( m_list.getSelectedIndex() - 1 );
			} else if
				( ev.getKeyCode() == KeyEvent.VK_DOWN )
			{
				m_list.setSelectedIndex( Math.min( m_model.getSize() - 1, m_list.getSelectedIndex() + 1 ) );
			} else if
				( ev.getKeyCode() == KeyEvent.VK_SPACE || ev.getKeyCode() == KeyEvent.VK_ENTER )
			{
				select();
			}

		}

		public void keyReleased( KeyEvent ev )
		{
			if
				( ev.getKeyCode() == KeyEvent.VK_SPACE || ev.getKeyCode() == KeyEvent.VK_ENTER )
			{
				if
					( m_popup.isVisible() )
				{
					unpostMenu();
				} else {
					postMenu();
				}
			}
		}
	};


	Listener l = new Listener();
		
	addMouseListener( l );
	addMouseMotionListener( l );
	addFocusListener( l );
	addKeyListener( l );









	class ListListener implements MouseListener, MouseMotionListener
	{
		public void mouseClicked( MouseEvent ev ) {}
		public void mouseEntered( MouseEvent ev ) {}
		public void mouseExited( MouseEvent ev ) {}
		public void mousePressed( MouseEvent ev ) {}
		public void mouseDragged( MouseEvent ev ) {}

		public void mouseReleased( MouseEvent ev )
		{
//			System.err.println( "LIST : mouseReleased" );
			select();
			unpostMenu();
		}

		public void mouseMoved( MouseEvent ev )
		{
			updateListBoxSelectionForEvent( ev, 0, false );
		}

	};

	
	ListListener ll = new ListListener();

	m_list.addMouseMotionListener( ll );
	m_list.addMouseListener( ll );


}
public void intervalAdded( ListDataEvent ev )
{
	contentsChanged( ev );
}
public void intervalRemoved( ListDataEvent ev )
{
	contentsChanged( ev );
}
public boolean isAsIs()
{
	return m_asIs;
}
public boolean isFocusable()
{
	return true;
}
public boolean isQuiet()
{
	return m_keepQuiet;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-11-03 11:51:26)
 * @param args java.lang.String[]
 */
public static void main(String[] args)
{
	JFrame f = new JFrame();
	f.getContentPane().setLayout( new FlowLayout( FlowLayout.LEFT ) );

	CoOptionMenu m = new CoOptionMenu();
	m.addItem( "ål" );
	m.addItem( "gädda" );
	m.addItem( "abborre" );
	m.addItem( "gös" );
	m.addItem( "sutare" );
	m.addNullItem( "löja" );
	f.getContentPane().add( m );
	
	JButton b = new JButton( " set selected = null" );
	final CoOptionMenu M = m;
	b.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				M.removeAllItems();
	M.addItem( "ål" );
	M.addItem( "gädda" );
	M.addItem( "abborre" );
	M.addItem( "gös" );
	M.addItem( "sutare" );
	M.addNullItem( "löja" );
			}
		}
	);
	f.getContentPane().add( b );

	final JLabel l = new JLabel( "_____________" );
	f.getContentPane().add( l );
	M.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent ev )
			{
				l.setText( "" + ( (CoOptionMenu) ev.getSource() ).getSelectedItem() );
			}
		}
	);
	M.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				l.setText( "set " + ev.getActionCommand() );
			}
		}
	);

	
	JComboBox cb = new JComboBox();
	cb.addItem( "ål" );
	cb.addItem( "gädda" );
	cb.addItem( "abborre" );
	cb.addItem( "gös" );
	cb.addItem( "sutare" );
	f.getContentPane().add( cb );
	
	m = new CoOptionMenu();
	m.addItem( "mete" );
	m.addItem( "kast" );
	m.addItem( "pimpel" );
	m.addItem( "trolling" );
	m.addItem( "fluga" );
	f.getContentPane().add( m );
/*
	m.setRenderer(
		new CoOptionMenu.AbstractRenderer()
		{
			public void configure( Object value, JLabel label, JList list, int index, boolean isSelected, boolean cellHasFocus )
			{
				label.setText( "### " + value.toString() + " ###" );
			}
		}
	);
*/
	m.setAsIs();
	
	f.pack();
	f.show();
}
public void paint( Graphics g )
{
	Component c = null;
	
	if
		( m_selection != null )
	{
		c = m_renderer.getListCellRendererComponent( m_list, m_selection, -1, false, false );
	} else {
		c = m_renderer.getListCellRendererComponent( m_list, m_nullItem, -1, false, false );
	}

	c.setSize( c.getPreferredSize() );

	super.paint( g );

	Insets i = getInsets();

	g.translate( i.left, i.top );
	c.paint( g );
	g.translate( -i.left, -i.top );

	int x = getWidth() - m_iconSize - i.right;
	int y = i.top;

	g.translate( x, y );
	paintIcon( g, this, m_iconSize, m_iconSize );
	g.translate( -x, -y );

 	if
		( m_asIs )
	{
		g.setColor( m_asIsColor );
		g.fillRect( i.left, i.top, getWidth() - m_iconSize - i.left - i.right, getHeight() - i.top - i.bottom - 1 );
	}
}
public static void paintIcon( Graphics g, Component c, int w, int h )
{
	Color origColor;
	origColor = g.getColor();
//	g.setColor(c.getBackground());
//	g.fillRect(1, 1, w - 2, h - 2);

	if
		( c.hasFocus() )
	{
		g.setColor(UIManager.getColor("Button.focus"));
		g.drawRect(2, 2, w - 5, h - 5);
	}
	
	// If there's no room to draw arrow, bail
	if (h < 5 || w < 5)
	{
		g.setColor(origColor);
		return;
	}

	// Draw the arrow
	int size = Math.min((h - 4) / 3, (w - 4) / 3);
	size = Math.max(size, 2);
	paintTriangle(g, (w - size) / 2, (h - size) / 2, size, SOUTH, c.isEnabled() );

	// Reset the Graphics back to it's original settings
	g.setColor(origColor);
}
public static void paintTriangle( Graphics g, int x, int y, int size, int direction, boolean isEnabled )
{
	Color oldColor = g.getColor();
	int mid, i, j;
	j = 0;
	size = Math.max(size, 2);
	mid = size / 2;
	g.translate(x, y);
	if (isEnabled)
		g.setColor(UIManager.getColor("controlDkShadow"));
	else
		g.setColor(UIManager.getColor("controlShadow"));
	switch (direction)
	{
		case NORTH :
			for (i = 0; i < size; i++)
			{
				g.drawLine(mid - i, i, mid + i, i);
			}
			if (!isEnabled)
			{
				g.setColor(UIManager.getColor("controlLtHighlight"));
				g.drawLine(mid - i + 2, i, mid + i, i);
			}
			break;
		case SOUTH :
			if (!isEnabled)
			{
				g.translate(1, 1);
				g.setColor(UIManager.getColor("controlLtHighlight"));
				for (i = size - 1; i >= 0; i--)
				{
					g.drawLine(mid - i, j, mid + i, j);
					j++;
				}
				g.translate(-1, -1);
				g.setColor(UIManager.getColor("controlShadow"));
			}
			j = 0;
			for (i = size - 1; i >= 0; i--)
			{
				g.drawLine(mid - i, j, mid + i, j);
				j++;
			}
			break;
		case WEST :
			for (i = 0; i < size; i++)
			{
				g.drawLine(i, mid - i, i, mid + i);
			}
			if (!isEnabled)
			{
				g.setColor(UIManager.getColor("controlLtHighlight"));
				g.drawLine(i, mid - i + 2, i, mid + i);
			}
			break;
		case EAST :
			if (!isEnabled)
			{
				g.translate(1, 1);
				g.setColor(UIManager.getColor("controlLtHighlight"));
				for (i = size - 1; i >= 0; i--)
				{
					g.drawLine(j, mid - i, j, mid + i);
					j++;
				}
				g.translate(-1, -1);
				g.setColor(UIManager.getColor("controlShadow"));
			}
			j = 0;
			for (i = size - 1; i >= 0; i--)
			{
				g.drawLine(j, mid - i, j, mid + i);
				j++;
			}
			break;
	}
	g.translate(-x, -y);
	g.setColor(oldColor);
}
private void postMenu()
{
	if ( ! isEnabled() ) return;

	requestFocus();

	m_popup.removeAll();
	m_popup.add( m_list );

	Object item = null;

	if
		( ( m_nullItem != null ) && ( m_selection == null ) )
	{
		item = m_nullItem;
//		m_popupDeltaY = - m_nullIndex * m_list.getCellBounds( 0, 0 ).height;
	} else {
		int I = m_model.getSize();
		for
			( int i = 0; i < I; i++ )
		{
			if
				( m_selection == m_model.getElementAt( i ) )
			{
				item = m_selection;
//				m_popupDeltaY = - i * m_list.getCellBounds( 0, 0 ).height;
				break;
			}
		}
	}

	Rectangle r = m_list.getCellBounds( 0, 0 );
	m_popupDeltaY = ( r == null ) ? 0 : r.height;
	
	m_list.setSelectedValue( item, true );
	m_list.setFixedCellWidth( getWidth() );
	m_popup.pack();
	int y = getLocationOnScreen().y;
	if ( y + m_popupDeltaY < 0 ) m_popupDeltaY = - y;
	m_popup.show( this, 0, m_popupDeltaY );
}
public void removeActionListener( ActionListener l )
{
	listenerList.remove( ActionListener.class, l );
}
public void removeAllItems()
{
	MutableComboBoxModel model = (MutableComboBoxModel) m_model;
	if
		( model.getSize() > 0 )
	{
		if
			( model instanceof DefaultComboBoxModel )
		{
	    ( (DefaultComboBoxModel) model ).removeAllElements();
		} else {
	    for
	    	( int i = 0; i < model.getSize(); ++i )
	    {
	      Object element = model.getElementAt( 0 );
				model.removeElement( element );
	    }
		}
	}
}
public void removeItemListener( ItemListener l )
{
	listenerList.remove( ItemListener.class, l );
}
private void select()
{
	clearShortCutCharacter();
	
	Object item = m_list.getSelectedValue();

	if
		( item != null )
	{
		if
			( ( m_nullItem != null ) && ( m_nullItem.equals( item ) ) )
		{
			item = null;
		}

		m_model.setSelectedItem( item );

		requestFocus();

		fireActionEvent( ( item == null ) ? null : item.toString() );
	}
}
private void selectionChanged()
{
	if ( m_selection == m_model.getSelectedItem() ) return;
	
	fireItemStateChanged( new ItemEvent( this,
		                                   ItemEvent.ITEM_STATE_CHANGED,
		                                   ( ( m_selection == m_nullItem ) && ( m_nullItem != null ) ) ? null : m_selection,
		                                   ItemEvent.DESELECTED ) );
	
	m_selection = m_model.getSelectedItem();
	m_asIs = false;

	fireItemStateChanged( new ItemEvent( this,
		                                   ItemEvent.ITEM_STATE_CHANGED,
		                                   ( ( m_selection == m_nullItem ) && ( m_nullItem != null ) ) ? null : m_selection,
		                                   ItemEvent.SELECTED ) );

	repaint();
}
public void setAsIs()
{
	m_asIs = true;
}
public void setEnabled( boolean q )
{
	super.setEnabled( q );
	if ( ! q ) unpostMenu();
}
public void setFont( Font f )
{
	super.setFont( f );

	updateIconSize();

	if ( m_list != null ) m_list.setFont( f );
}
public void setModel( ComboBoxModel model )
{
	ComboBoxModel old = m_model;
	
	if
		( old != null )
	{	
		old.removeListDataListener( this );
	}
	
	m_model = model;
	m_list.setModel( m_model );
	
	firePropertyChange( "model", old, m_model );
	
	m_model.addListDataListener( this );

//	selectionChanged();
	
	update();
}
public void setQuiet( boolean q )
{
	m_keepQuiet = q;
}

public void setSelectedIndex( int i )
{
	setSelectedItem( m_model.getElementAt( i ) );
}
public void setSelectedIndex( int i, boolean quiet )
{
	m_keepQuiet = quiet;
	
	setSelectedIndex( i );
	
	m_keepQuiet = false;
}
public void setSelectedItem( Object item )
{
	if
		( m_asIs )
	{
		m_asIs = false;
//		m_selection = m_asIsValue;
	}
	
	if
		( ( m_nullItem != null ) && ( m_nullItem.equals( item ) ) )
	{
		m_model.setSelectedItem( null );
	} else {
		m_model.setSelectedItem( item );
	}
}
public void setSelectedItem( Object anObject, boolean quiet )
{
	m_keepQuiet = quiet;
	
	setSelectedItem( anObject );
	
	m_keepQuiet = false;
}
public void setVisible( boolean q )
{
	super.setVisible( q );
	if ( ! q ) unpostMenu();
}
private void unpostMenu()
{
	MenuSelectionManager manager = MenuSelectionManager.defaultManager();
	
	MenuElement [] selection = manager.getSelectedPath();
	for
		( int i = 0 ; i < selection.length ; i++ )
	{
		if
			( selection[i] == m_popup )
		{
			manager.clearSelectedPath();
			break;
		}
	}
}
private void update()
{
	if ( m_preferredSize == null ) return;
	if ( m_model == null ) return;
	
	m_preferredSize.width = 0;
	m_preferredSize.height = 0;

	int I = m_model.getSize();
	if
		( I > 0 )
	{
		for
			( int i = 0; i < I; i++ )
		{
			Object item = m_model.getElementAt( i );

			Dimension c = m_renderer.getListCellRendererComponent( m_list, item, -1, false, false ).getPreferredSize();
			m_preferredSize.width = Math.max( m_preferredSize.width, c.width );
			m_preferredSize.height = Math.max( m_preferredSize.height, c.height );
		}
	} else {
		Dimension c = m_renderer.getListCellRendererComponent( m_list, " ", -1, false, false ).getPreferredSize();
		m_preferredSize.width = Math.max( m_preferredSize.width, c.width );
		m_preferredSize.height = Math.max( m_preferredSize.height, c.height );
	}

	Insets i = getInsets();
	m_preferredSize.height += i.top + i.bottom;
	m_preferredSize.width += i.left + i.right + m_preferredSize.height;

	m_list.setVisibleRowCount( m_model.getSize() );
	
	revalidate();
}
private void updateIconSize()
{
	String t = getText();
	javax.swing.border.Border b = getBorder();
	Icon i = getIcon();

	setText( "A" );
	setIcon( null );
	setBorder( null );
	
	m_iconSize = super.getPreferredSize().height;

	setText( t );
	setBorder( b );
	setIcon( i );
}
protected void updateListBoxSelectionForEvent( MouseEvent ev, int dy, boolean shouldScroll )
{
	Rectangle r = new Rectangle();
	m_list.computeVisibleRect( r );
	Point p = ev.getPoint();
	p.y -= dy;
	if
		( r.contains( p ) )
	{
		updateListBoxSelectionForEvent( p, shouldScroll );
	}
}
protected void updateListBoxSelectionForEvent( Point location, boolean shouldScroll )
{
	if (m_list == null)
		return;
	int index = m_list.locationToIndex(location);
	if (index == -1)
	{
		if (location.y < 0)
			index = 0;
		else
			index = getModel().getSize() - 1;
	}
	if (m_list.getSelectedIndex() != index)
	{
		m_list.setSelectedIndex(index);
		if (shouldScroll)
			m_list.ensureIndexIsVisible(index);
	}
}

	private Dimension m_preferredSize = new Dimension();
	{
		m_list.setFont( getFont() );
	}

public ListCellRenderer getRenderer()
{
	return m_renderer;
}

public void setRenderer( ListCellRenderer r )
{
	m_renderer = r;
	m_list.setCellRenderer( r );
	update();
}

public Dimension getMaximumSize()
{
	return getPreferredSize();
}

public Dimension getMinimumSize()
{
	return getPreferredSize();
}

public void setBorder( javax.swing.border.Border b )
{
	super.setBorder( b );
	update();

	revalidate();
}

	private StringBuffer m_shortCutString;

private boolean addShortCutCharacter( char c )
{
	if ( m_shortCutString == null ) m_shortCutString = new StringBuffer();

	if ( ( m_shortCutString.length() == 0 ) && ( c == ' ' ) ) return false;

	m_shortCutString.append( c );
	
	updateShortCutSelection();

	return true;
}

private void clearShortCutCharacter()
{
	if ( m_shortCutString != null ) m_shortCutString.setLength( 0 );
}

private void deleteLastShortCutCharacter()
{
	if ( m_shortCutString == null ) m_shortCutString = new StringBuffer();

	int len = m_shortCutString.length();
	if
		( len > 0 )
	{
		m_shortCutString.deleteCharAt( len - 1 );
	}
	
	updateShortCutSelection();
}

public void keyPressed( java.awt.event.KeyEvent ev )
{
	char c = ev.getKeyChar();
	
	if
		( Character.isLetterOrDigit( c ) )
	{
		if ( addShortCutCharacter( c ) ) ev.consume();
	} else if
		(
			( ev.getKeyCode() == KeyEvent.VK_BACK_SPACE )
		||
			( ev.getKeyCode() == KeyEvent.VK_DELETE )
		)
	{
		deleteLastShortCutCharacter();
		ev.consume();
	}
}

	/**
	 * Invoked when a key has been released.
	 */
public void keyReleased(java.awt.event.KeyEvent e) {}

	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 */
public void keyTyped(java.awt.event.KeyEvent e) {}

private void updateShortCutSelection()
{
	if ( ! m_popup.isVisible() ) postMenu();

	String typed = m_shortCutString.toString();
	m_list.clearSelection();

	if
		( typed.length() == 0 )
	{
		m_list.setSelectedIndex( getSelectedIndex() );
	} else {
		int I = m_model.getSize();
		for
			( int i = 0; i < I; i++ )
		{
			Object item = m_model.getElementAt( i );
			Component cmp = m_renderer.getListCellRendererComponent( m_list, item, -1, false, false );
			if
				( cmp instanceof JLabel )
			{
				String str = ( (JLabel) cmp ).getText().toLowerCase();
				if
					( str.startsWith( typed ) )
				{
					m_list.setSelectedIndex( i );
					break;
				}
			} else {
				break;
			}
		}
	}

}
}