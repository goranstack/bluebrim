package com.bluebrim.base.shared;

import java.rmi.*;
import java.util.*;

/**
 * Interface implemented by client classes listening for changes
 * in a server object, dispatched by an ui servant.
 */
public interface CoChangedObjectStateListener extends EventListener, Remote {
public void objectStateChanged(CoChangedObjectStateEvent e);
}