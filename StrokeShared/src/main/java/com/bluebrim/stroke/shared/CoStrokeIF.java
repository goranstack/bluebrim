package com.bluebrim.stroke.shared;
import java.rmi.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * RMI-enabling interface for class com.bluebrim.stroke.impl.shared.CoStroke.
 * 
 * @author: Dennis Malmström
 */

public interface CoStrokeIF extends CoObjectIF, Remote, Cloneable, CoXmlEnabledIF, CoRenameable, CoFactoryElementIF, CoDistinguishable {

	public final static String FACTORY_KEY = "stroke";
	public final static String SOLID = "solid";

	CoStrokeLayerIF add();

	void clear();

	public CoStrokeIF deepClone();

	CoStrokeLayerIF get(int i);

	int getCount();

	int indexOf(CoStrokeLayerIF ss);

	CoStrokeLayerIF insert(int i);

	void normalize();

	void remove(int i);

	void remove(CoStrokeLayerIF ss);

	boolean isMutable();

	public void setOwner(CoMarkDirtyListener owner);

}