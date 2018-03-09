package com.bluebrim.paint.impl.client;
import java.awt.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

//import com.bluebrim.mediasystem.impl.server.*;
//import com.bluebrim.mediasystem.impl.shared.*;

//

public class CoProcessColorUI extends com.bluebrim.paint.impl.client.CoColorUI 
{
	public static final String PROCESS_COLOR = "process_color";

	public static final String CYAN 	= "cyan";
	public static final String MAGENTA 	= "magenta";
	public static final String YELLOW 	= "yellow";
	public static final String BLACK 	= "black";


/**
 * CoProcessColorUI constructor comment.
 */
public CoProcessColorUI() 
{
	super();
}


/**
 * CoProcessColorUI constructor comment.
 * @param aDomainObject com.bluebrim.base.shared.CoObjectIF
 */
public CoProcessColorUI(CoObjectIF aDomainObject) 
{
	super(aDomainObject);
}


protected CoUserInterfaceBuilder createUserInterfaceBuilder()
{
    return new CoNumberUserInterfaceBuilder(this);
}

public boolean canBeEnabled()
{
	return false;
}


/**
 * This method was created by a SmartGuide.
 */
protected void createValueModels(CoUserInterfaceBuilder builder){
	
	super.createValueModels(builder);
	CoNumberUserInterfaceBuilder numberBuilder = (CoNumberUserInterfaceBuilder)builder;


	// NAME --------------------------------------------------------------------------------------
	builder.createTextFieldAdaptor(builder.addAspectAdaptor(new CoReadOnlyAspectAdaptor(this, CoConstants.NAME) {
		protected Object get(CoObjectIF subject) {
			return ((com.bluebrim.paint.impl.shared.CoProcessColorIF )subject).getName();
		}		
	}), (CoTextField) getNamedWidget(CoConstants.NAME));	

	// CYAN --------------------------------------------------------------------------------------
	numberBuilder.createNumberFieldAdaptor(builder.addAspectAdaptor(new CoReadOnlyAspectAdaptor(this, CYAN) {
		protected Object get(CoObjectIF subject) {
			return new Float(((com.bluebrim.paint.shared.CoColorIF )subject).getCyanPercentage()  / 100 );
		}		
	}), (CoTextField) getNamedWidget(CYAN), CoNumberConverter.PERCENT);

	// MAGENTA -------------------------------------------------------------------------------------
	numberBuilder.createNumberFieldAdaptor(builder.addAspectAdaptor(new CoReadOnlyAspectAdaptor(this, MAGENTA) {
		protected Object get(CoObjectIF subject) {
			return new Float(((com.bluebrim.paint.shared.CoColorIF )subject).getMagentaPercentage() / 100 );
		}		
	}), (CoTextField) getNamedWidget(MAGENTA), CoNumberConverter.PERCENT);
	
	// YELLOW -------------------------------------------------------------------------------------
	numberBuilder.createNumberFieldAdaptor(builder.addAspectAdaptor(new CoReadOnlyAspectAdaptor(this, YELLOW) {
		protected Object get(CoObjectIF subject) {
			return new Float(((com.bluebrim.paint.shared.CoColorIF )subject).getYellowPercentage()  / 100 );
		}
	}), (CoTextField) getNamedWidget(YELLOW), CoNumberConverter.PERCENT);
	
	// BLACK --------------------------------------------------------------------------------------
	numberBuilder.createNumberFieldAdaptor(builder.addAspectAdaptor(new CoReadOnlyAspectAdaptor(this, BLACK) {
		protected Object get(CoObjectIF subject) {
			return new Float(((com.bluebrim.paint.shared.CoColorIF )subject).getBlackPercentage()  / 100 );
		}
	}), (CoTextField) getNamedWidget(BLACK), CoNumberConverter.PERCENT);	

}


protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder aBuilder)
{	
	super.createWidgets(aPanel, aBuilder);

	CoPanel topPanel = aBuilder.createBoxPanel( BoxLayout.Y_AXIS , false , null );

	createPreviewIcon();	
		
	CoLabel label = aBuilder.createLabel( 
		com.bluebrim.paint.impl.client.CoColorUIResources.getName( PROCESS_COLOR ) ,
		this.colorPreviewIcon ,
		JLabel.CENTER ,
		PREVIEW
	);
	
	label.setFont( CoUIConstants.GARAMOND_18_LIGHT );
	label.setForeground( Color.black );
	topPanel.add( label );
	topPanel.add( Box.createVerticalStrut( 20 ));
	
	CoPanel namePanel = aBuilder.createPanel( new CoFormLayout(10,2) , false , null );	
	namePanel.add( aBuilder.createLabel( CoStringResources.getName(CoConstants.NAME) ) );
	namePanel.add( aBuilder.createTextField( SwingConstants.LEFT , 20 , CoConstants.NAME ) );

	CoPanel borderPanel = aBuilder.createPanel( new BorderLayout() , false , null );
	borderPanel.add( namePanel , BorderLayout.WEST );
	topPanel.add( borderPanel );
	topPanel.add( Box.createVerticalStrut( 10 ));

	CoPanel formPanel = aBuilder.createPanel( new CoFormLayout(10, 2), false, null );
	formPanel.add(aBuilder.createLabel(com.bluebrim.paint.impl.client.CoColorUIResources.getName(CYAN)));	
	formPanel.add(aBuilder.createTextField(SwingConstants.RIGHT, 5, CYAN));
	formPanel.add(aBuilder.createLabel(com.bluebrim.paint.impl.client.CoColorUIResources.getName(MAGENTA)));
	formPanel.add(aBuilder.createTextField(SwingConstants.RIGHT, 5, MAGENTA));
	formPanel.add(aBuilder.createLabel(com.bluebrim.paint.impl.client.CoColorUIResources.getName(YELLOW)));
	formPanel.add(aBuilder.createTextField(SwingConstants.RIGHT, 5, YELLOW));
	formPanel.add(aBuilder.createLabel(com.bluebrim.paint.impl.client.CoColorUIResources.getName(BLACK)));
	formPanel.add(aBuilder.createTextField(SwingConstants.RIGHT, 5, BLACK));

	CoPanel tPanel = aBuilder.createPanel( new BorderLayout(10,0) );
	tPanel.add( formPanel , BorderLayout.WEST );
	topPanel.add( tPanel );

	aPanel.add(topPanel , BorderLayout.NORTH);
}


protected void doAfterCreateUserInterface(){

	super.doAfterCreateUserInterface();
	
	if(getDomain() != null)
	{
//		this.colorPreviewIcon.setColor(((com.bluebrim.paint.impl.shared.CoProcessColorIF)getDomain()).getPreviewColor());
		getNamedWidget(PREVIEW).repaint();
	}
}


protected void setDomainFrom( CoObjectIF selectedValue )
{
	super.setDomainFrom( selectedValue );
	
//	this.colorPreviewIcon.setColor(((com.bluebrim.paint.impl.shared.CoProcessColorIF)getDomain()).getPreviewColor());
	getNamedWidget(PREVIEW).repaint();
}
}