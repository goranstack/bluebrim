package com.bluebrim.image.client;

import java.awt.datatransfer.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.image.shared.*;

public interface CoImageClientConstants 
{

	DataFlavor IMAGE_CONTENT_FLAVOR		= CoDataTransferKit.domainFlavor(CoImageContentIF.class, CoImageContentIF.FACTORY_KEY);
}