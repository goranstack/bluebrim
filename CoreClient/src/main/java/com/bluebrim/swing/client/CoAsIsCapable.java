package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.SystemColor;

public interface CoAsIsCapable
{
	static final Color m_asIsColor = SystemColor.window.darker().darker();
boolean isAsIs();
void setAsIs();
}
