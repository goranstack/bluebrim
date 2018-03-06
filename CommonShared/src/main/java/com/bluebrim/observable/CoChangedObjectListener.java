package com.bluebrim.observable;

import java.rmi.*;
import java.util.*;

/**
 * Interface for classes listening to objects changed in the repository.
 */
public interface CoChangedObjectListener extends EventListener, Remote {
	public void serverObjectChanged(CoChangedObjectEvent e);
}
