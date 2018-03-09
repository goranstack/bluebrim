package com.bluebrim.layout.impl.server;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * 
 * Creation date: (2001-08-27 10:50:25)
 * @author: Dennis
 */

public interface CoPageItemPrototypeTreeNodeIF extends CoPageItemPrototypeTreeNodeRIF
{
CoPageItemPrototypeTreeNodeRIF add( String name );
CoPageItemPrototypeIF add( String name, String description, CoShapePageItemIF pageItem );
void add( CoPageItemPrototypeIF prototype );
CoPageItemPrototypeTreeNodeRIF add( CoPageItemPrototypeTreeNodeRIF node );
void remove( CoPageItemPrototypeTreeNodeRIF child );
void removePageItemPrototypes( java.util.List prototypes );
}
