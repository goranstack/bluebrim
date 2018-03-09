package com.bluebrim.compositecontent.client;

import java.awt.datatransfer.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.content.shared.*;

public interface CoCompositeContentClientConstants 
{
	DataFlavor WORK_PIECE_FLAVOR		= CoDataTransferKit.domainFlavor(CoWorkPieceIF.class, CoWorkPieceIF.FACTORY_KEY);
}