package com.bluebrim.layout.shared;

import java.rmi.*;
import java.util.*;

import com.bluebrim.page.shared.*;

/**
 * @author Göran Stäck
 */
public interface CoLayoutServer extends Remote
{
    public static final String RMIname = "LayoutServer";

    /**
     * All parameters in points.
     */
    public CoImmutableColumnGridIF createColumnGrid(double width, double height, int noOfCols, double colSpacing, double leftMargin,
            double rigthMargin, double topMargin, double bottomMargin) throws RemoteException;

    public CoLayout createLayout() throws RemoteException;

    public CoDesktopLayout createDesktopLayout(CoLayoutParameters layoutParameters) throws RemoteException;

    public CoLayoutTemplateRootFolder createLayoutTemplateTree(CoLayoutParameters layoutParameters) throws RemoteException;

    public CoLayoutEditorContext createLayoutEditorContext(CoLayoutParameters layoutParameters, CoLayoutTemplateCollection tools,
            List contentRecievers, CoLayoutTemplateRootFolder templateTree) throws RemoteException;

    public CoLayoutParameters createLayoutParameters() throws RemoteException;

    public CoPageLayout createPageLayout(CoPage page) throws RemoteException;

    public CoLayoutParameters getDefaultLayoutParameters() throws RemoteException;

    public CoLayoutContentIF createLayoutContent(CoLayoutParameters layoutParameters) throws RemoteException;
}