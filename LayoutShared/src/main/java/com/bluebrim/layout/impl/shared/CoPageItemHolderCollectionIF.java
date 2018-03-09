package com.bluebrim.layout.impl.shared;
import java.util.*;

import com.bluebrim.layout.shared.*;

/**
 * Interface for any object that holds a mutable collection of com.bluebrim.layout.shared.CoPageItemHolderIF.
 *
 * @author: Dennis Malmström
 */

public interface CoPageItemHolderCollectionIF extends CoLayoutHolderCollection
{
List getPageItemHolders();

int indexOf( CoPageItemHolderIF pi );
}