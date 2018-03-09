package com.bluebrim.gui.client;

import java.awt.Insets;

import com.bluebrim.base.shared.CoObjectIF;

/**
 * Subclass to CoDomainUserInterface with zero insets.
 */
public abstract class CoInsetlessUI extends CoDomainUserInterface {
public CoInsetlessUI()
{
	super();
}
public CoInsetlessUI( CoObjectIF domain )
{
	super( domain );
}
protected Insets getDefaultPanelInsets() {
	return null;
}
}
