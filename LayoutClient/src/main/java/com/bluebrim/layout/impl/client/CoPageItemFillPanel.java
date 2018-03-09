package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Page item geometry property panel.
 *
 * @author: Dennis
 */

public class CoPageItemFillPanel extends CoPageItemPropertyPanel
{

public void doUpdate()
{
	CoImmutableFillStyleIF s = m_domain.getFillStyle();
	
	m_fillStyleOptionMenu.setSelectedItem( s.getFactoryKey() );
	
	m_gradientFillStylePanel.doUpdate();
	m_patternFillStylePanel.doUpdate();
}
public void postSetDomain()
{
	super.postSetDomain();
	
	m_gradientFillStylePanel.setDomain( m_domain );
	m_patternFillStylePanel.setDomain( m_domain );
}

	public CardLayout m_fillStyleAttributeCardLayout;
	public CoPanel m_fillStyleAttributePanel;
	public CoOptionMenu m_fillStyleOptionMenu;
	public CoPageItemFillStylePanel m_gradientFillStylePanel;
	public CoPageItemFillStylePanel m_patternFillStylePanel;

public CoPageItemFillPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoAttachmentLayout(), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	m_fillStyleOptionMenu = b.createOptionMenu();
	m_fillStyleAttributePanel = b.createPanel( m_fillStyleAttributeCardLayout = new CardLayout() );


	add( m_fillStyleOptionMenu,
			       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	add( m_fillStyleAttributePanel,
			       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, m_fillStyleOptionMenu ),
				                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );


	
	m_fillStyleAttributePanel.add( m_gradientFillStylePanel = new CoPageItemGradientFillStylePanel( b, commandExecutor ), CoGradientFillIF.GRADIENT_FILL );
	m_fillStyleAttributePanel.add( m_patternFillStylePanel = new CoPageItemPatternFillStylePanel( b, commandExecutor ), CoPatternFillStyleIF.PATTERN_FILL );


	add( m_fillStyleOptionMenu,
							       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
								                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
								                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
								                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );

	
	m_fillStyleOptionMenu.addItem( CoGradientFillIF.GRADIENT_FILL );
	m_fillStyleOptionMenu.addItem( CoPatternFillStyleIF.PATTERN_FILL );
	
	m_fillStyleOptionMenu.setRenderer( new CoOptionMenu.TranslatingRenderer( CoPageItemUIStringResources.getBundle() ) );

	
	m_fillStyleOptionMenu.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent ev )
			{
				if
					( ev.getStateChange() == ItemEvent.SELECTED )
				{
					String key = ev.getItem().toString();
					m_fillStyleAttributeCardLayout.show( m_fillStyleAttributePanel, key );
				}
			}
		}
	);




	m_fillStyleOptionMenu.addActionListener(
		new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.SET_FILL_STYLE, null )
		{
			protected Object getCurrentValue()
			{
				return m_domain.getFillStyle().getFactoryKey();
			}
		
			protected void prepare()
			{
				CoFactoryIF f = CoFactoryManager.getFactory( (String) m_value );
				( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, f.createObject() );
			}
		}
	);

}

public void setContext( CoPageItemEditorContextIF context )
{
	super.setContext( context );
	
	m_gradientFillStylePanel.setContext( context );
	m_patternFillStylePanel.setContext( context );
}
}