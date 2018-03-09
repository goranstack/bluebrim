package com.bluebrim.solitarylayouteditor.test;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import com.bluebrim.solitarylayouteditor.CoFileStoreSupport;
import com.bluebrim.solitarylayouteditor.CoFileable;
import com.bluebrim.xml.impl.shared.CoXmlRuntimeException;

/**
 * @author Göran Stäck 2002-10-11
 *
 */
public class CoFileStoreSupportTest {

	public static void main(String[] args) {
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory())
					return true;
				String name = file.getName().toLowerCase();
				return name.endsWith(".xml");
			}
			public String getDescription() {
				return "XML";
			}
		};

		CoFileable fileable = new CoFileable() {

			public void writeToStream(OutputStream outputStream, List attachments) throws IOException {
				outputStream.write("Detta är en test av CoFiler 22".getBytes());
				File attachmentFile = new File("attachment.txt");
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter(attachmentFile));
					out.write("En bilaga");
					out.close();
				} catch (IOException e) {
				}
				attachments.add(attachmentFile);
			}

			public String getName() {
				return "Namnlös";
			}

			public Component getComponent() {
				return null;
			}

			public void setFileStoreSupport(CoFileStoreSupport fileStoreSupport) {
			}
			
			public String getNameInZipArchive() {
				return "filestoresupporttest";	
			}

		};


		CoFileStoreSupport fileStoreSupport = new CoFileStoreSupport(filter, true);
		fileStoreSupport.setFileable(fileable);
		try {
			fileStoreSupport.save();
		} catch (IOException e) {
			System.out.println("Kunde inte spara");
			e.printStackTrace();
			return;
		} catch (CoXmlRuntimeException e) {
			System.out.println("Kunde inte skapa dataström");
			e.printStackTrace();
			return;
		};
		if (!fileStoreSupport.hasFile())
			System.out.println("Sparandet avbröts");
		else
			System.out.println("Sparat en fil med namnet: " + fileStoreSupport.getFileName());

	}
}
