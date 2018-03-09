package com.bluebrim.layout.impl.server.manager;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.shared.*;

/**
 * Creation date: (2000-06-05 10:12:05)
 * @author: Dennis
 */

public class CoNoLayoutManager extends CoLayoutManager implements CoNoLayoutManagerIF
{

    public boolean doesSetSize()
    {
        return false;
    }

    public String getFactoryKey()
    {
        return NO_LAYOUT_MANAGER;
    }

    public boolean isNull()
    {
        return true;
    }

    public void layout(CoLayoutableContainerIF parent)
    {
    }

    public final static String XML_TAG = "no-layout-manager";

    CoNoLayoutManager()
    {
    }

    public static CoNoLayoutManager getInstance()
    {
        return (CoNoLayoutManager) getFactory().getNoLayoutManager();
    }

    public String xmlGetTag()
    {
        return XML_TAG;
    }
    
    public String getLocalizedName()
    {
        return CoPageItemLayoutResources.getName(NO_LAYOUT_MANAGER);
    }

}