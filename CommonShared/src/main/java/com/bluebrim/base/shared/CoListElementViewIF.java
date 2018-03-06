package com.bluebrim.base.shared;

import java.io.*;

import javax.swing.*;
/**
 * Interface to a view of a remote object that's displayed in a list or tree.
 * Created and initialized on the server and then moved over to 
 * the client.
 * Creation date: (2000-03-13 10:01:22)
 * @author: Lasse S
 */
public interface CoListElementViewIF extends Serializable {
public Object getElement();
public Icon getIcon();
public String getText();
}
