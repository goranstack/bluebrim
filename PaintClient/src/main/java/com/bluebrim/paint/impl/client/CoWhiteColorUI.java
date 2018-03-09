package com.bluebrim.paint.impl.client;
import java.awt.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

//import com.bluebrim.mediasystem.impl.shared.*;

public class CoWhiteColorUI extends com.bluebrim.paint.impl.client.CoColorUI 
{
	public static final String WHITE_COLOR				= "white_color";
	public static final String AS_LIGHT_AS_POSSIBLE 	= "as_light_as_possible";


/**
 * CoWhiteColorUI constructor comment.
 */
public CoWhiteColorUI() 
{
	super();
}


/**
 * CoWhiteColorUI constructor comment.
 * @param aDomainObject com.bluebrim.base.shared.CoObjectIF
 */
public CoWhiteColorUI(CoObjectIF aDomainObject) 
{
	super(aDomainObject);
}


protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder aBuilder)
{	
	super.createWidgets(aPanel, aBuilder);

	CoPanel topPanel = aBuilder.createBoxPanel( BoxLayout.Y_AXIS , false , null );

	createPreviewIcon();
		
	CoLabel label = aBuilder.createLabel( 
		com.bluebrim.paint.impl.client.CoColorUIResources.getName( WHITE_COLOR ) ,
		this.colorPreviewIcon ,
		JLabel.CENTER ,
		PREVIEW
	);
	
	label.setFont( CoUIConstants.GARAMOND_18_LIGHT );
	label.setForeground( Color.black );
	topPanel.add( label );
	topPanel.add( Box.createVerticalStrut( 20 ));
	
	topPanel.add( aBuilder.createLabel( com.bluebrim.paint.impl.client.CoColorUIResources.getName( AS_LIGHT_AS_POSSIBLE ) ) );

	aPanel.add(topPanel , BorderLayout.NORTH);
}
}