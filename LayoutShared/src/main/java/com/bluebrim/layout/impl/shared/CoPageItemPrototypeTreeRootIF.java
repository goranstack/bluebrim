package com.bluebrim.layout.impl.shared;

import com.bluebrim.layout.shared.*;

/**
 * RMI-enbling interface for CoPageItemPrototypeTreeRoot.
 * 
 * @author: Dennis Malmström
 */
 
public interface CoPageItemPrototypeTreeRootIF extends CoPageItemPrototypeTreeNodeRIF, CoLayoutTemplateRootFolder
{




void remove( java.util.List nodes );

void rename( CoPageItemPrototypeIF node, String name );




CoPageItemPrototypeTreeNodeRIF addTo( CoPageItemPrototypeTreeNodeRIF parent, String name );

void addTo( CoPageItemPrototypeTreeNodeRIF parent, String name, String description, CoShapePageItemIF pageItem );

void addTo( CoPageItemPrototypeTreeNodeRIF parent, CoPageItemPrototypeIF  prototype );

CoPageItemPrototypeTreeNodeRIF addTo( CoPageItemPrototypeTreeNodeRIF parent, CoPageItemPrototypeTreeNodeRIF  node );

java.util.List getStructure();

void removeFrom( CoPageItemPrototypeTreeNodeRIF parent, CoPageItemPrototypeTreeNodeRIF Child );

void rename( CoPageItemPrototypeTreeNodeRIF node, String name );
}