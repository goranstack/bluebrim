package com.bluebrim.paint.impl.client;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.command.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

/**
 *
 */
public abstract class CoEditableColorUI extends com.bluebrim.paint.impl.client.CoColorUI
{
	com.bluebrim.paint.impl.shared.CoEditableColorIF colorProxy;
	com.bluebrim.paint.impl.shared.CoEditableColorIF originalColor;
	
	AbstractAction resetColorAction;
	AbstractAction applyColorAction;

	public static final String APPLY = "apply";
	public static final String RESET = "reset";	

	// Inner class ---------------------------------------------
	protected class copyFromProxyTransaction extends CoBasicTransaction
	{
		public copyFromProxyTransaction(	String name, 
											CoObjectIF target, 
											CoEditableColorUI source )
		{
			super(name, target, source);
		}
		
		protected void doTransaction() 
		{
			originalColor.copyFrom( colorProxy );
		}
	};
public CoEditableColorUI()
{
	super();
}
public CoEditableColorUI(CoObjectIF aDomainObject) 
{
	super(aDomainObject);
}
protected void applyReset( boolean apply )
{
	setDomain(originalColor);

	if(apply)
	{
		new copyFromProxyTransaction( 	colorProxy.getName() ,
							 			getDomain() ,
							 			CoEditableColorUI.this ).execute();
	}
	else
	{
		updateWidgets();
	}

	colorProxy = null;
	enableDisableButtons( false );
}
protected CoPanel createButtons(CoUserInterfaceBuilder aBuilder)
{
	CoPanel buttonPanel = aBuilder.createBoxPanel( BoxLayout.X_AXIS , false , null );	

	buttonPanel.add(aBuilder.createButton( this.resetColorAction = new AbstractAction(  com.bluebrim.paint.impl.client.CoColorUIResources.getName(RESET) )
	{
		public void actionPerformed(ActionEvent e)
		{
			applyReset( false );
		}			
	}, RESET));
	
	buttonPanel.add( Box.createHorizontalStrut( 10 ) );	

	buttonPanel.add( aBuilder.createButton( this.applyColorAction = new AbstractAction( com.bluebrim.paint.impl.client.CoColorUIResources.getName(APPLY) )
	{
		public void actionPerformed(ActionEvent e)
		{
			applyReset( true );
		}	
	}, APPLY));

	CoPanel eastPanel = aBuilder.createPanel( new BorderLayout() );
	eastPanel.add( buttonPanel , BorderLayout.EAST );
	return eastPanel;
}
protected abstract CoPanel createColorPanel(CoUserInterfaceBuilder aBuilder);
protected abstract CoLabel createLabel( CoUserInterfaceBuilder aBuilder );
protected final CoPanel createNamePanel( CoUserInterfaceBuilder aBuilder )
{
	CoPanel namePanel = aBuilder.createPanel( new CoFormLayout(10,2) , false , null );	
	namePanel.add( aBuilder.createLabel( CoStringResources.getName(CoConstants.NAME) ) );
	namePanel.add( aBuilder.createTextField( SwingConstants.LEFT , 20 , CoConstants.NAME ) );
	
	CoPanel borderPanel = aBuilder.createPanel( new BorderLayout() , false , null );
	borderPanel.add( namePanel , BorderLayout.WEST );
	return borderPanel;
}
protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder aBuilder)
{	
	super.createWidgets(aPanel, aBuilder);

	CoPanel boxPanel = aBuilder.createBoxPanel( BoxLayout.Y_AXIS , false , null );

	// Icon
	createPreviewIcon();

	// Label
	boxPanel.add( createLabel(aBuilder) );	
	boxPanel.add( boxPanel.add( Box.createVerticalStrut( 15 ) ) );

	// Name
	boxPanel.add( createNamePanel(aBuilder));	
	boxPanel.add( Box.createVerticalStrut( 10 ) );

	// Color panel
	boxPanel.add(createColorPanel(aBuilder));	
	boxPanel.add( Box.createVerticalStrut( 10 ) );

	// Buttons
	boxPanel.add( createButtons(aBuilder) );
	
	CoPanel panel = aBuilder.createPanel( new BorderLayout() , false , null );
	panel.add( boxPanel , BorderLayout.WEST );
	
	aPanel.add( panel , BorderLayout.NORTH );
}
protected void doAfterCreateUserInterface ( ) 
{
	super.doAfterCreateUserInterface();
	enableDisableButtons( false );
}
protected void enableDisableButtons(boolean enable)
{
	resetColorAction.setEnabled( enable );
	applyColorAction.setEnabled( enable );	
}
protected void makeColorProxy()
{
	this.originalColor = (com.bluebrim.paint.impl.shared.CoEditableColorIF )getDomain();
	this.colorProxy = this.originalColor.createObject();
	this.colorProxy.copyFrom( this.originalColor );
	
	updateColorProxy();
	
	setDomain( this.colorProxy );
	enableDisableButtons( true );
}
protected void setDomainFrom( CoObjectIF selectedValue )
{
	super.setDomainFrom( selectedValue );

	colorProxy = null;	
	enableDisableButtons( false );
	updatePreviewColor();
}
protected void updateColorProxy()
{
}
protected abstract void updatePreviewColor();
protected abstract void updateWidgets();

public void setColorEditable( boolean e )
{
	getNamedWidget( APPLY ).setVisible( e );
}
}