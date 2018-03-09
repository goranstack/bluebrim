package com.bluebrim.text.impl.client;
import com.bluebrim.base.shared.*;
import com.bluebrim.content.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.observable.*;

public class CoTextContentUI extends CoAbstractContentUI implements CoContextAcceptingUI {
	public final static String DOCUMENT_TAB = "CoTextContentUI.document_tab";

	private CoFormattedTextHolderUI m_textHolderUI;

	public CoTextContentUI(CoObjectIF textContent, CoUIContext uiContext) {
		this(uiContext);
		setDomain(textContent);
	}

	public CoGenericUIContext getCopyOfCurrentRequiredUIContext() {
		return m_textHolderUI.getCopyOfCurrentRequiredUIContext();
	}

	public void setUIContext(CoUIContext context) {
		m_textHolderUI.setUIContext(context);
	}


	public class MyTabData extends CoAbstractContentUI.MyTabData {
		public MyTabData(CoDomainUserInterface ui) {
			super(ui);
		}

		public void initializeTabData() {
			addTabData(new CoAbstractTabUI.TabData(DOCUMENT_TAB, null, CoTextClientResources.getName(DOCUMENT_TAB)) {
				protected CoDomainUserInterface createUserInterface() {
					return m_textHolderUI;
				}
				public Object getValueFor(CoObjectIF subject) {
					return subject;
				}

			});


			super.initializeTabData();
		}
	}

	public CoTextContentUI(CoUIContext uiContext ) {
		super();
		m_textHolderUI = new CoFormattedTextHolderUI(uiContext);
		new CoDefaultServerObjectListener(m_textHolderUI).initialize();
	}

	protected CoServerTabData getTabData() {
		CoServerTabData tabData = new MyTabData(this);
		tabData.initializeTabData();
		return tabData;
	}

	protected Object[] getTabOrder() {
		return new Object[] { DOCUMENT_TAB };

	}
}