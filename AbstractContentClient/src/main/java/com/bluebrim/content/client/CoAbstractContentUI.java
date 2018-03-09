package com.bluebrim.content.client;
import com.bluebrim.gui.client.CoAbstractTabUI;
import com.bluebrim.gui.client.CoDomainUserInterface;
import com.bluebrim.gui.client.CoServerTabData;

/**
 * Abstract superclass to UI's that display content
 * Creation date: (2001-02-22)
 * @author: Göran Stäck 
 * @author: Dennis Malmström (rebuild as CoServerTabUI)
 */

public abstract class CoAbstractContentUI extends CoAbstractTabUI {

	public class MyTabData extends CoServerTabData {
		public MyTabData(CoDomainUserInterface ui) {
			super(ui);
		}

		public void initializeTabData() {
		}
	}

	public CoAbstractContentUI() {

	}


}