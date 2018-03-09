package com.bluebrim.image.impl.client;
import java.awt.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.content.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.image.shared.*;
import com.bluebrim.layout.client.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.text.impl.client.*;

/**
 * Insert the class' description here.
 * 
 * @since 2001-07-06 Helena Åberg: Added tab for image operation editing tool.
 */
public class CoImageContentUI extends CoAbstractContentUI implements CoContextAcceptingUI {
	public final static String CAPTION_TAB = "CoImageContentUI.caption_tab";
	public final static String IMAGE_TAB = "CoImageContentUI.image_tab";
	private CoFormattedTextHolderUI m_captionUI;

	// Inner class for tab data
	public class MyTabData extends CoAbstractContentUI.MyTabData {
		public MyTabData(CoDomainUserInterface ui) {
			super(ui);
		}

		public void initializeTabData() {
			addTabData(new CoAbstractTabUI.TabData(IMAGE_TAB, null, CoImageClientResources.getName(IMAGE_TAB)) {
				CoPopupMenu m_popupMenu;

				protected CoDomainUserInterface createUserInterface() {
					CoDomainUserInterface returnMe = new CoViewableUI() {
						protected CoView getViewFor(CoObjectIF subject) {
							return new CoImageContentView(getImageFor(subject));
						}

						protected void createListeners() {
							super.createListeners();

							getPanel().addMouseListener(new CoPopupGestureListener(m_popupMenu));
						}

						public Object getValueFor(CoObjectIF subject) {
							return subject;
						}
					};
					return returnMe;
				} 
			} 
			); 

			addTabData(new CoAbstractTabUI.TabData(CAPTION_TAB, null, CoImageClientResources.getName(CAPTION_TAB)) {
				protected CoDomainUserInterface createUserInterface() {
					return m_captionUI;
				}
				public Object getValueFor(CoObjectIF subject) {
					return ((CoImageContentIF) subject).getCaption();
				}
			} // end anonymous inner class
			); // end argument list

			super.initializeTabData();
		} // end initialize-method
	} // end inner class for tab data

	public CoImageContentUI(CoUIContext uiContext) {

		m_captionUI = new CoFormattedTextHolderUI(uiContext);
	}
	
	public CoImageContentUI(CoObjectIF imageContent, CoUIContext uiContext) {
		this(uiContext);

		setDomain(imageContent);
	}

	public CoGenericUIContext getCopyOfCurrentRequiredUIContext() {
		return m_captionUI.getCopyOfCurrentRequiredUIContext();
	}

	protected Insets getDefaultPanelInsets() {
		return null;
	}

	private CoImageContentIF getImageFor(CoObjectIF subject) {
		return (CoImageContentIF) subject;
	}

	protected CoServerTabData getTabData() {
		CoServerTabData tabData = new MyTabData(this);
		tabData.initializeTabData();
		return tabData;
	}

	protected Object[] getTabOrder() {
		return new Object[] { IMAGE_TAB, CAPTION_TAB, };

	}

	public void setUIContext(CoUIContext context) {
		m_captionUI.setUIContext(context);
	}


}