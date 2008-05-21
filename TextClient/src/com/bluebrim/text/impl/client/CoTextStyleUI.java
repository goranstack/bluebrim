package com.bluebrim.text.impl.client;
import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gemstone.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-10-31 11:43:56)
 * @author: Dennis
 */
 
public abstract class CoTextStyleUI extends CoDomainUserInterface
{
	public static final String NAME = "CoTextStyleUI.NAME";
	public static final String SHORTCUT = "CoTextStyleUI.SHORTCUT";
	public static final String DELETED = "CoTextStyleUI.DELETED";
	public static final String INHERIT = "CoTextStyleUI.INHERIT";
	public static final String BASED_ON = "CoTextStyleUI.BASED_ON";

	private CoTextField m_nameTextField;
	private CoKeyStrokePane m_keyStrokePane;
	private CoCheckBox m_deletedCheckBox;
	private CoCheckBox m_inheritCheckBox;
	private CoOptionMenu m_basedOnOptionMenu;

	private CoSubcanvas m_characterStyleSubCanvas;
	private CoSubcanvas m_paragraphStyleSubCanvas;

	private CoCharacterStylePreviewUI m_characterStyleUI;
	private CoParagraphStylePreviewUI m_paragraphStyleUI;

 	private CardLayout m_styleLayout;
	private static final String BASED_ON_NOTHING = " ";
	private SiblingStylesModel m_characterStyleSiblings = new SiblingStylesModel();
 	private CoTypographyRuleIF m_context;
 	private CoChangedObjectListener m_contextListener =
 		new CoChangedObjectListener()
 		{
	 		public void serverObjectChanged( CoChangedObjectEvent e )
	 		{
		 		contextChanged();
	 		}
 		};

	private SiblingStylesModel m_pararaphStyleSiblings = new SiblingStylesModel() ;
	public static final String NAME_IS_TAKEN = "CoTextStyleUI.NAME_IS_TAKEN";

	private class SiblingStylesModel extends AbstractListModel implements ComboBoxModel
	{
		private List m_items = new ArrayList();
		private Object m_selection;
		
 		public Object getSelectedItem()
 		{
	 		return m_selection;
 		}
  		
  	public void setSelectedItem( Object anItem )
  	{
	  	if ( m_selection == anItem ) return;
	  	
	  	m_selection = anItem;
			fireContentsChanged( this, -1, -1 );
  	}
  		
  	public void refresh( List items )
  	{
	  	m_items.clear();
	  	m_items.add( " " );
	  	if
	  		( items != null )
	  	{
		  	Iterator i = items.iterator();
		  	while
		  		( i.hasNext() )
		  	{
			  	CoCharacterStyleIF item = (CoCharacterStyleIF) i.next();
			  	if ( item == getDomain() ) continue;
			  	if ( item.isDeleted() ) continue;
			  	if ( item.isBasedOn( (CoCharacterStyleIF) getDomain() ) ) continue;
			  	m_items.add( item.getName() );
		  	}
	  	}
	  	
			fireContentsChanged( this, 0, getSize() - 1 );
  	}
  	
 		public Object getElementAt( int i )
 		{
	 		return m_items.get( i );
 		}
  		
 		public int getSize()
 		{
	 		return m_items.size();
 		}
	}





public CoTextStyleUI()
{
	super();
}
protected void createValueModels( CoUserInterfaceBuilder b )
{
	b.createTextFieldAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor( this, NAME )
			{
				public Object get( CoObjectIF subject )
				{
					return ( (CoCharacterStyleIF) subject ).getName();
				}
				public void set( CoObjectIF subject, Object value )
				{
					try
					{
						( (CoCharacterStyleIF) subject ).setName( value.toString() );
					}
					catch ( CoCharacterStyleIF.NameNotUniqueException ex )
					{
						throw new CoTransactionCommandException( CoTextStringResources.getName( NAME_IS_TAKEN ) );
					}
				}
			}
		),
		m_nameTextField );

	
	CoKeyStrokePaneAdaptor a =
		new CoKeyStrokePaneAdaptor(
			b.addAspectAdaptor(
				new CoGsAspectAdaptor( this, SHORTCUT )
				{
					public Object get( CoObjectIF subject )
					{
						return ( (CoCharacterStyleIF) subject ).getKeyStroke();
					}
					public void set( CoObjectIF subject, Object value )
					{
						 ( (CoCharacterStyleIF) subject ).setKeyStroke( (KeyStroke) value );
					}
				}
			),
			m_keyStrokePane );

	b.addEnableDisableListener( a );
	b.addUserInterfaceListener( a );

	
	b.createToggleButtonAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor( this, DELETED )
			{
				public Object get( CoObjectIF subject )
				{
					return ( (CoCharacterStyleIF) subject ).isDeleted() ? Boolean.TRUE : Boolean.FALSE;
				}
				public void set( CoObjectIF subject, Object value )
				{
					 ( (CoCharacterStyleIF) subject ).setDeleted( ( (Boolean) value ).booleanValue() );
				}
			}
		),
		m_deletedCheckBox );
	
	b.createToggleButtonAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor( this, INHERIT )
			{
				public Object get( CoObjectIF subject )
				{
					return ( (CoCharacterStyleIF) subject ).getInherit() ? Boolean.TRUE : Boolean.FALSE;
				}
				public void set( CoObjectIF subject, Object value )
				{
					 ( (CoCharacterStyleIF) subject ).setInherit( ( (Boolean) value ).booleanValue() );
				}
			}
		),
		m_inheritCheckBox );


	b.createOptionMenuAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor( this, BASED_ON )
			{
				public Object get( CoObjectIF subject )
				{
					CoCharacterStyleIF bo = ( (CoCharacterStyleIF) subject ).getBasedOn();
					return ( bo == null ) ? BASED_ON_NOTHING : bo.getName();
				}
				public void set( CoObjectIF subject, Object value )
				{
					if
						( subject instanceof CoParagraphStyleIF )
					{
						( (CoCharacterStyleIF) subject ).setBasedOn( CoTextStyleUI.this.m_context.getParagraphStyle( (String) value ) );
					} else {
						( (CoCharacterStyleIF) subject ).setBasedOn( CoTextStyleUI.this.m_context.getCharacterStyle( (String) value ) );
					}
				}
			}
		),
		m_basedOnOptionMenu );




	b.createSubcanvasAdaptor(
		b.addAspectAdaptor(
			new CoReadOnlyAspectAdaptor( this, "CHARACTER_STYLE" )
			{
				public Object get( CoObjectIF subject )
				{
					return getCharacterStyle( subject );
				}
			}
		),
		m_characterStyleSubCanvas );

	b.createSubcanvasAdaptor(
		b.addAspectAdaptor(
			new CoReadOnlyAspectAdaptor( this, "PARAGRAPH_STYLE" )
			{
				public Object get( CoObjectIF subject )
				{
					return getParagraphStyle( subject );
				}
			}
		),
		m_paragraphStyleSubCanvas );
	
}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	super.createWidgets( p, b );

	p.setLayout( new CoAttachmentLayout() );

	CoPanel pp = b.createPanel( new CoFormLayout( false ) );
	{
		CoLabel l = b.createLabel( CoTextStringResources.getName( NAME ) );
		m_nameTextField = b.createTextField();
		m_nameTextField.setColumns( 25 );
		pp.add( l );
		pp.add( m_nameTextField );

		l = b.createLabel( CoTextStringResources.getName( SHORTCUT ) );
		pp.add( l );
		m_keyStrokePane = new CoKeyStrokePane();
		m_keyStrokePane.setBorder( BorderFactory.createLoweredBevelBorder() );
		pp.add( m_keyStrokePane );
		
		l = b.createLabel( CoTextStringResources.getName( DELETED ) );
		pp.add( l );
		m_deletedCheckBox = b.createCheckBox( "", null );
		pp.add( m_deletedCheckBox );

		m_deletedCheckBox.addChangeListener(
			new ChangeListener()
			{
				public void stateChanged( ChangeEvent ev )
				{
					deletedStateChanged();
				}
			}
		);
		
		l = b.createLabel( CoTextStringResources.getName( INHERIT ) );
		pp.add( l );
		m_inheritCheckBox = b.createCheckBox( "", null );
		pp.add( m_inheritCheckBox );

		l = b.createLabel( CoTextStringResources.getName( BASED_ON ) );
		pp.add( l );

		m_basedOnOptionMenu = b.createOptionMenu();
		pp.add( m_basedOnOptionMenu );
	}
	
	m_characterStyleUI =
		new CoCharacterStylePreviewUI()
		{
			protected CoTypographyContextIF getInitialContext()
			{
				return getTypographyContext();
			}
		};
	
	m_characterStyleSubCanvas = b.createSubcanvas( m_characterStyleUI );


		
	m_paragraphStyleUI = 
		new CoParagraphStylePreviewUI()
		{
			protected CoTypographyContextIF getInitialContext()
			{
				return getTypographyContext();//CoTextStyleUI.this.getPageItemPreferences().getTypographyContext();
			}
		};
	
	m_paragraphStyleSubCanvas = b.createSubcanvas( m_paragraphStyleUI );


	CoPanel p2 = b.createPanel( m_styleLayout = new CardLayout() );
	p2.add( "neither", new CoLabel() );
	p2.add( "m_characterStyleSubCanvas", m_characterStyleSubCanvas );
	p2.add( "m_paragraphStyleSubCanvas", m_paragraphStyleSubCanvas );

	p.add( pp,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );

	p.add( p2,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 10, pp ),//m_nameTextField ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );

}
private void deletedStateChanged()
{
	updateStyleUI();

	boolean b = ! m_deletedCheckBox.isSelected();
	m_nameTextField.setEnabled( b );
	m_keyStrokePane.setEnabled( b );
	m_inheritCheckBox.setEnabled( b );
	m_basedOnOptionMenu.setEnabled( b );
}
private CoCharacterStyleIF getCharacterStyle( CoObjectIF subject )
{
	return ( subject instanceof CoParagraphStyleIF ) ? null : (CoCharacterStyleIF) subject;
}
protected Insets getDefaultPanelInsets()
{
	return null;
}
protected abstract CoTextParameters getTextParameters();
private CoParagraphStyleIF getParagraphStyle( CoObjectIF subject )
{
	return ( subject instanceof CoParagraphStyleIF ) ? (CoParagraphStyleIF) subject : null;
}


private void contextChanged()
{
	m_characterStyleSiblings.refresh( ( m_context == null ) ? null : m_context.getCharacterStyles() );
	m_pararaphStyleSiblings.refresh( ( m_context == null ) ? null : m_context.getParagraphStyles() );
}

public void postDomainChange( CoObjectIF d )
{
	super.postDomainChange( d );

	m_characterStyleSiblings.refresh( ( m_context == null ) ? null : m_context.getCharacterStyles() );
	m_pararaphStyleSiblings.refresh( ( m_context == null ) ? null : m_context.getParagraphStyles() );

	updateStyleUI();
}

public void setContext( CoTypographyRuleIF context )
{
	if ( m_context == context ) return;

	if
		( m_context != null )
	{
		CoObservable.removeChangedObjectListener( m_contextListener, m_context );
	}
	
	m_context = context;

	if
		( m_context != null )
	{
		CoObservable.addChangedObjectListener( m_contextListener, m_context );
	}

	contextChanged();
}

private void updateStyleUI()
{
	CoObjectIF d = getDomain();
	
	if
		( d != null && ! m_deletedCheckBox.isSelected() )
	{
		CoCharacterStyleIF s = (CoCharacterStyleIF) d;
		
		if
			( s instanceof CoParagraphStyleIF )
		{
			m_styleLayout.show( m_paragraphStyleSubCanvas.getParent(), "m_paragraphStyleSubCanvas" );
			m_basedOnOptionMenu.setModel( m_pararaphStyleSiblings );
		} else {
			m_styleLayout.show( m_characterStyleSubCanvas.getParent(), "m_characterStyleSubCanvas" );
			m_basedOnOptionMenu.setModel( m_characterStyleSiblings );
		}
	} else {
			m_styleLayout.show( m_characterStyleSubCanvas.getParent(), "neither" );
	}


}

protected CoTypographyContextIF getTypographyContext()
{
	CoTextParameters p = getTextParameters();
	
	return ( p == null ) ? null : p.getTypographyContext();
}

public void setContext( CoTypographyContextIF context )
{
	m_characterStyleUI.setContext( context );
	m_paragraphStyleUI.setContext( context );
}
}