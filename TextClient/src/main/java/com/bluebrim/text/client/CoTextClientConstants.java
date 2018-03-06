package com.bluebrim.text.client;

import java.awt.datatransfer.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.text.shared.*;



public interface CoTextClientConstants 
{

	DataFlavor TEXT_CONTENT_FLAVOR		= CoDataTransferKit.domainFlavor(CoTextContentIF.class, CoTextContentIF.FACTORY_KEY);
}