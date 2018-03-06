package com.bluebrim.layout.impl.shared;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Interface for any object that holds a set of page items.
 * See CoPageItemHolderUI
 *
 * @author: Dennis Malmström
 */

public interface CoPageItemHolderIF extends CoObjectIF, CoNamed, CoLayoutHolder
{


public String getDescription();
List getPageItems();
void pageItemsChanged();
}