package com.bluebrim.paint.impl.client;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

/**
 *
 */
public abstract class CoColorChooserUI extends CoEditableColorUI 
{
	protected ChangeListener changeListener;
	protected JColorChooser colorChooser;
public CoColorChooserUI()
{
	super();
}
public CoColorChooserUI( CoObjectIF aDomain )
{
	super( aDomain );
}
protected void colorEdited()
{
	if( colorProxy == null) 
		makeColorProxy();
	else 
		updateColorProxy();
	
	updatePreviewColor();
}
protected CoPanel createColorPanel(CoUserInterfaceBuilder aBuilder)
{
	CoPanel tPanel = aBuilder.createPanel( new BorderLayout() );	
	this.colorChooser = new JColorChooser();
	if(getDomain() != null)
		this.colorChooser.setColor(((com.bluebrim.paint.shared.CoColorIF)getDomain()).getPreviewColor());	
//	this.colorChooser.setPreferredSize(new Dimension(500,350));
	tPanel.add( this.colorChooser, BorderLayout.NORTH);
	return tPanel;
}
protected void createListeners()
{
	super.createListeners();

	this.colorChooser.getSelectionModel().addChangeListener(this.changeListener = new ChangeListener() 
	{
		public void stateChanged(ChangeEvent evt)
		{
			colorEdited();			
		}		
	});
}
protected void doAfterCreateUserInterface ( ) 
{
	super.doAfterCreateUserInterface();
	updateWidgets();
}
protected void setDomainFrom( CoObjectIF selectedValue )
{
	super.setDomainFrom( selectedValue );

	// Ugly!!!
	colorChooser.getSelectionModel().removeChangeListener(this.changeListener);
	updateWidgets();
	colorChooser.getSelectionModel().addChangeListener(this.changeListener);	
}
protected void updateColorProxy()
{
	super.updateColorProxy();
	colorProxy.setColor( this.colorChooser.getColor() );
}
protected void updatePreviewColor()
{
	getNamedWidget(PREVIEW).repaint();
}
protected void updateWidgets()
{
	if( getDomain() != null)
	{
		colorChooser.setColor( ((com.bluebrim.paint.shared.CoColorIF )getDomain()).getPreviewColor() );
	}	
}
}