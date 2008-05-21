package com.bluebrim.paint.impl.client;
import java.awt.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

//import com.bluebrim.mediasystem.impl.server.*;
//import com.bluebrim.mediasystem.impl.shared.*;

public class CoRegistrationColorUI extends com.bluebrim.paint.impl.client.CoColorChooserUI
{		
	public static final String REGISTRATION_COLOR 	= "registration_color";


/**
 * CoRegistrationColorUI constructor comment.
 */
public CoRegistrationColorUI() 
{
	super();
}


/**
 * CoRegistrationColorUI constructor comment.
 * @param aDomainObject com.bluebrim.base.shared.CoObjectIF
 */
public CoRegistrationColorUI(com.bluebrim.base.shared.CoObjectIF aDomainObject) 
{
	super(aDomainObject);
}


protected CoLabel createLabel( CoUserInterfaceBuilder aBuilder )
{
	CoLabel label = aBuilder.createLabel( 
		com.bluebrim.paint.impl.client.CoColorUIResources.getName( REGISTRATION_COLOR ) ,
		this.colorPreviewIcon ,
		JLabel.CENTER ,
		PREVIEW
	);
	
	label.setFont( CoUIConstants.GARAMOND_18_LIGHT );
	label.setForeground( Color.black );

	return label;
}


protected void createValueModels(CoUserInterfaceBuilder builder){
	
	super.createValueModels(builder);

	// ----------------------------------------------------------------
	builder.createTextFieldAdaptor(builder.addAspectAdaptor(new CoReadOnlyAspectAdaptor(this, CoConstants.NAME) 
	{
		protected Object get(CoObjectIF subject)
		{
			return ((com.bluebrim.paint.impl.shared.CoRegistrationColorIF )subject).getName();
		}
	}), (CoTextField )getNamedWidget(CoConstants.NAME));
	
}


protected void updateWidgets()
{
	super.updateWidgets();
	getNamedWidget(CoConstants.NAME).setEnabled(false);
}
}