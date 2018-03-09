package com.bluebrim.collection.shared;

import java.rmi.*;
import java.util.*;

/**
 * A remote extension of <code>Iterator</code>. The implementation of
 * this interface <code>RemoteIteratorImpl</code> is used in those methods
 * in server classes that returns an iterator over an serializable collection.
 *
 * Creation date: (1999-10-13 11:51:15)
 * @author: Lasse S
 */
public interface CoRemoteIteratorIF extends Remote, Iterator {
}
