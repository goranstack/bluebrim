package com.bluebrim.content.shared;

import java.rmi.*;

import com.bluebrim.layout.shared.*;

/**
 * @author Göran Stäck
 */
public interface CoCompositeContentServer extends Remote
{
    public static final String RMIname = "CompositeContent";

    public CoWorkPieceIF createWorkPiece( CoLayoutParameters layoutParameters) throws RemoteException;
}