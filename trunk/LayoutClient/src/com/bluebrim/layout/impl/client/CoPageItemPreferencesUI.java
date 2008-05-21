package com.bluebrim.layout.impl.client;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.client.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.pagesize.client.*;
import com.bluebrim.paint.impl.client.*;
import com.bluebrim.stroke.impl.client.*;
import com.bluebrim.text.impl.client.*;
import com.bluebrim.text.shared.*;

/**
 * Domain object for this UI is CoPageItemPreferencesIF.
 */

public class CoPageItemPreferencesUI extends CoAbstractTabUI {
	public final static String COLORS_TAB = "CoPageItemPreferencesUI.COLORS_TAB";
	public final static String STROKES_TAB = "CoPageItemPreferencesUI.STROKES_TAB";
	public final static String H_AND_J_TAB = "CoPageItemPreferencesUI.H_AND_J_TAB";
	public final static String AVAILABLE_FONTS_TAB = "CoPageItemPreferencesUI.AVAILABLE_FONTS_TAB";
	public final static String TYPOGRAPHY_RULE_TAB = "CoPageItemPreferencesUI.TYPOGRAPHY_RULE_TAB";
	public final static String TAG_CHAINS_TAB = "CoPageItemPreferencesUI.TAG_CHAINS_TAB";
	public final static String TAG_GROUPS_TAB = "CoPageItemPreferencesUI.TAG_GROUPS_TAB";
	public final static String CHARACTER_PROPERTIES_TAB = "CoPageItemPreferencesUI.CHARACTER_PROPERTIES_TAB";
	public final static String PAGE_SIZES_TAB = "CoPageItemPreferencesUI.PAGE_SIZES";
	private CoStrokeCollectionUI m_strokeCollectionUI = new CoStrokeCollectionUI();

	public class PageItemPreferencesTabData extends CoServerTabData {

		public PageItemPreferencesTabData(CoDomainUserInterface ui) {
			super(ui);
		}

		public void initializeTabData() {

			// CHARACTER_PROPERTIES
			addTabData(new TabData(
				CHARACTER_PROPERTIES_TAB,
				null,
				CoPageItemUIStringResources.getName(CHARACTER_PROPERTIES_TAB)) {
				protected CoDomainUserInterface createUserInterface() {
					return new CoCharacterPropertiesUI();
				}
			});

			// COLORS
			addTabData(new TabData(COLORS_TAB, null, CoPageItemUIStringResources.getName(COLORS_TAB)) {
				protected CoDomainUserInterface createUserInterface() {
					return new CoColorCollectionUI();
				}
			});

			// STROKES
			addTabData(new TabData(STROKES_TAB, null, CoPageItemUIStringResources.getName(STROKES_TAB)) {
				protected CoDomainUserInterface createUserInterface() {
					return m_strokeCollectionUI;
				}
			});

			// H_AND_J_TAB
			addTabData(new TabData(H_AND_J_TAB, null, CoPageItemUIStringResources.getName(H_AND_J_TAB)) {
				protected CoDomainUserInterface createUserInterface() {
					return new CoHyphenationCollectionUI();
				}
			});

			// AVAILABLE_FONTS_TAB
			addTabData(new TabData(
				AVAILABLE_FONTS_TAB,
				null,
				CoPageItemUIStringResources.getName(AVAILABLE_FONTS_TAB)) {
				protected CoDomainUserInterface createUserInterface() {
					return new CoFontFamilyCollectionUI();
				}
			});

			// TYPOGRAPHY_RULE_TAB
			addTabData(new TabData(
				TYPOGRAPHY_RULE_TAB,
				null,
				CoPageItemUIStringResources.getName(TYPOGRAPHY_RULE_TAB)) {
				public Object getValueFor(CoObjectIF subject) {
					return ((CoPageItemPreferencesIF) subject).getTypographyRule();
				}

				protected CoDomainUserInterface createUserInterface() {
					return new CoTypographyRuleUI() {
						protected CoTextParameters getTextParameters() {
							return (CoTextParameters) CoPageItemPreferencesUI.this.getDomain();
						}

					};
				}
			});

			// TAG_CHAINS_TAB
			addTabData(new TabData(TAG_CHAINS_TAB, null, CoPageItemUIStringResources.getName(TAG_CHAINS_TAB)) {
				protected CoDomainUserInterface createUserInterface() {
					CoTagChainCollectionUI ui = new CoTagChainCollectionUI();
					return ui;
				}
			});

			// TAG_GROUPS_TAB
			addTabData(new TabData(TAG_GROUPS_TAB, null, CoPageItemUIStringResources.getName(TAG_GROUPS_TAB)) {
				protected CoDomainUserInterface createUserInterface() {
					CoTagGroupCollectionUI ui = new CoTagGroupCollectionUI();
					return ui;
				}
			});

			// PAGE_SIZES_TAB
			addTabData(new TabData(PAGE_SIZES_TAB, null, CoPageItemUIStringResources.getName(PAGE_SIZES_TAB)) {
				protected CoDomainUserInterface createUserInterface() {
					CoPageSizeCollectionUI ui = new CoPageSizeCollectionUI();
					return ui;
				}

				public Object getValueFor(CoObjectIF subject){
					return ((CoPageItemPreferencesIF)subject).getPageSizeCollection();
				}
			});

		}
	}

	public CoPageItemPreferencesUI() {
		super();
	}

	public String getDefaultWindowTitle() {
		return CoLayoutClientResources.getName(CoLayoutClientConstants.LAYOUT_PARAMETERS);
	}

	protected CoServerTabData getTabData() {
		CoServerTabData tabData = new PageItemPreferencesTabData(this);
		tabData.initializeTabData();
		return tabData;
	}
	protected Object[] getTabOrder() {

		return new Object[] {
			COLORS_TAB,
			STROKES_TAB,
			H_AND_J_TAB,
			AVAILABLE_FONTS_TAB,
			TYPOGRAPHY_RULE_TAB,
			TAG_CHAINS_TAB,
			TAG_GROUPS_TAB,
			CHARACTER_PROPERTIES_TAB,
			PAGE_SIZES_TAB
			};

	}

	protected void initializeTabData() {
		CoServerTabData tabData = new PageItemPreferencesTabData(this);
		tabData.initializeTabData();

		Object tabOrder[] = getTabOrder();

		for (int i = 0; i < tabOrder.length; i++)
			addTabData(tabData.getTabDataFor(tabOrder[i]));

	}

	public void postDomainChange(CoObjectIF d) {
		super.postDomainChange(d);

		if (m_strokeCollectionUI != null)
			m_strokeCollectionUI.setColorCollection((CoPageItemPreferencesIF) getDomain());
	}

	public void valueHasChanged() {
		super.valueHasChanged();

		m_strokeCollectionUI.setColorCollection((CoPageItemPreferencesIF) getDomain());

		final String[] keys = { CHARACTER_PROPERTIES_TAB, TAG_CHAINS_TAB, TAG_GROUPS_TAB, AVAILABLE_FONTS_TAB, };

		for (int i = 0; i < keys.length; i++) {
			CoDomainUserInterface ui = getTabData(keys[i]).getUserInterface();
			if (ui.isBuilt()) {
				ui.valueHasChanged();
			}
		}
	}
}