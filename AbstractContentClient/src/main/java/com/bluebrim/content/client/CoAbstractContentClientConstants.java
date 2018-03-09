package com.bluebrim.content.client;
import java.awt.datatransfer.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.content.shared.*;

public interface CoAbstractContentClientConstants 
{
	DataFlavor CONTENT_FLAVOR = CoDataTransferKit.domainFlavor(CoContentIF.class, CoContentIF.FACTORY_KEY);	
}