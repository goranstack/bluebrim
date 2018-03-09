package com.bluebrim.content.shared;
import java.rmi.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Creation date: (2000-10-27 11:31:03)
 * @author: Monika Czerska
 */

public interface CoContentCollectionIF extends CoContentReceiver, CoObjectIF, Remote, CoXmlExportEnabledIF {

	public CoContentIF addContent( CoContentIF content );
		
	public List getContents();
	
	public CoLayoutParameters getLayoutParameters();
	
	public CoContentIF removeContent(CoContentIF content);
	
	public void removeContents(Object [] contents);
	
	public void addContents( Object [] contents, int pos );
	
//	public void handleDrop(Transferable transferable, int pos);
	
}