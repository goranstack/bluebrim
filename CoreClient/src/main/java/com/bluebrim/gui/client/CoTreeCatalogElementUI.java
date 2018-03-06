package com.bluebrim.gui.client;
import java.awt.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.resource.shared.*;
import com.bluebrim.swing.client.*;

/**
 */
public class CoTreeCatalogElementUI extends CoDomainUserInterface{

public CoTreeCatalogElementUI() {
	super();
}

public CoTreeCatalogElementUI(CoObjectIF aDomainObject) {
	super(aDomainObject);
}


protected CoPanel createIconAndTypePanel () {
	CoPanel topPanel	= getUIBuilder().createBoxPanel(BoxLayout.X_AXIS,true, new Insets(4,4,4,4));
	topPanel.add(createLabel());	
	topPanel.setOpaque(false);
	return topPanel;
}


protected CoLabel createLabel () {
	CoLabel label	=	getUIBuilder().createLabel("",null,SwingConstants.LEFT, "TYPE_ICON");
	setIconAndType(label);
	label.setFont(GARAMOND_36_LIGHT);
	label.setOpaque(false);
	label.setForeground(Color.red);
	label.setIconTextGap(15);
	return label;		
}


protected CoPanel createTopPanel () {
		return createIconAndTypePanel();
}


protected void createWidgets (CoPanel aPanel, CoUserInterfaceBuilder builder ) {
	super.createWidgets(aPanel, builder);
		
	aPanel.add(createTopPanel(), BorderLayout.NORTH);
	aPanel.add(createWorkingPanel(), BorderLayout.CENTER);	
}


protected CoPanel createWorkingPanel () {
	CoPanel tPanel = getUIBuilder().createPanel(new BorderLayout(), true, new Insets(4,4,4,4));
	tPanel.setOpaque(false);
	return tPanel;		
}


protected void doAfterCreateUserInterface () 
{
	super.doAfterCreateUserInterface();
	setIconAndType((JLabel)getNamedWidget("TYPE_ICON"));
}


protected void postDomainChange ( CoObjectIF aDomain) 
{
	super.postDomainChange(aDomain);
	setIconAndType((JLabel)getNamedWidget("TYPE_ICON"));
}


protected void setIconAndType (JLabel typeAndIconLabel) {
	if (typeAndIconLabel != null)
	{
		CoTreeCatalogElementIF tElement			=	(CoTreeCatalogElementIF ) getDomain();
		typeAndIconLabel.setText(tElement != null ? tElement.getType(): new String());
		typeAndIconLabel.setIcon(tElement != null ?CoResourceLoader.loadIcon(tElement.getIconResourceAnchor(), tElement.getIconName()) : null);
	}
}
}