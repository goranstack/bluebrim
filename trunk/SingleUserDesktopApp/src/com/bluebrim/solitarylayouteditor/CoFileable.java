package com.bluebrim.solitarylayouteditor;

import java.awt.*;
import java.io.*;
import java.util.List;

import com.bluebrim.base.shared.*;

/**
 * Implemented by objects that use a <code>CoFileStoreSupport</code> instance to save
 * data in files.
 * 
 * @author Göran Stäck 2002-10-09
 *
 */
public interface CoFileable extends CoNamed {
		
	/**
	 * Write the data to be saved on the <code>outputStream</code>. If the data
	 * has attachment files for example XML data with images in separate files
	 * these files are added to the list in the <code>attachments</code>
	 * 
	 * @param outputStream where the data is written	 
	 * @param supplements list for supplement files.
	 * @throws Exception
	 */
	void writeToStream(OutputStream outputStream, List attachments) throws Exception;


	void setFileStoreSupport(CoFileStoreSupport fileStoreSupport);
	
	/**
	 * Return a <code>Component</code> that can be used as parent in dialogs
	 */
	Component getComponent();
	
	/**
	 * Returns the name to be used as filename for the file inside the zipped file.
	 * The named is also used as a part of attachment file names.
	 * The recommended use of this method is to return a name that indicates the file type.
	 * That enables easy checking of file type when loading data from the filesystem.  
	 */ 
	String getNameInZipArchive();
}
