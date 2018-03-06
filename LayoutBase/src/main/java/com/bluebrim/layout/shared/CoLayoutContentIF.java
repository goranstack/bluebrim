package com.bluebrim.layout.shared;
import com.bluebrim.base.shared.*;
import com.bluebrim.content.shared.CoAtomicContentIF;
import com.bluebrim.text.shared.CoFormattedTextHolderIF;

public interface CoLayoutContentIF extends CoAtomicContentIF, CoViewable, CoLayoutHolder
{
	String LAYOUT_CONTENT = "layout_content";
	String FACTORY_KEY = "layout_content";
	String ICON_NAME = "CoLayoutContentIF.gif";

	public CoLayout getLayout();
	
	long getTimeStamp();
	
	public void setLayout( CoLayout layout );
	
	CoFormattedTextHolderIF getCaption();
}