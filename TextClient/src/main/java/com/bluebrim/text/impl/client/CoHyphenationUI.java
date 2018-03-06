package com.bluebrim.text.impl.client;

import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

public class CoHyphenationUI extends CoDomainUserInterface {
	public static final String NAME = "CoHyphenationUI.NAME";
	public static final String LINE_BREAKER = "CoHyphenationUI.LINE_BREAKER";

	private static final String LINE_BREAKER_ATTRIBUTES = "LINE_BREAKER_ATTRIBUTES";
	private static final String HYPHENATION_FALLBACK_BEHAVIOR = CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR;

	private Map m_uiCache = new HashMap();
	public CoHyphenationUI() {
		super();
	}
	protected void createListeners() {
		super.createListeners();

		getNamedValueModel(LINE_BREAKER).addValueListener(new CoValueListener() {
			public void valueChange(CoValueChangeEvent ev) {
				updateLineBreakerAttributePanel();
			}
		});
	}
	protected void createValueModels(CoUserInterfaceBuilder b) {
		super.createValueModels(b);

		b.createTextFieldAdaptor(b.addAspectAdaptor(new CoGsAspectAdaptor(this, NAME) {
			public Object get(CoObjectIF subject) {
				return ((CoHyphenationIF) subject).getName();
			}
			public void set(CoObjectIF subject, Object value) {
				((CoHyphenationIF) subject).setName(value.toString());
			}
		}, NAME), (CoTextField) getNamedWidget(NAME));

		b.createOptionMenuAdaptor(b.addAspectAdaptor(new CoGsAspectAdaptor(this, LINE_BREAKER) {
			public Object get(CoObjectIF subject) {
				return ((CoHyphenationIF) subject).getLineBreakerKey();
			}
			public void set(CoObjectIF subject, Object value) {
				((CoHyphenationIF) subject).setLineBreaker(value.toString());
			}
		}, LINE_BREAKER), (CoOptionMenu) getNamedWidget(LINE_BREAKER));

		b.createOptionMenuAdaptor(b.addAspectAdaptor(new CoGsAspectAdaptor(this, HYPHENATION_FALLBACK_BEHAVIOR) {
			public Object get(CoObjectIF subject) {
				return ((CoHyphenationIF) subject).getFallbackBehavior();
			}
			public void set(CoObjectIF subject, Object value) {
				((CoHyphenationIF) subject).setFallbackBehavior((CoEnumValue) value);
			}
		}, HYPHENATION_FALLBACK_BEHAVIOR), (CoOptionMenu) getNamedWidget(HYPHENATION_FALLBACK_BEHAVIOR));

		b.createSubcanvasAdaptor(b.addAspectAdaptor(new CoReadOnlyAspectAdaptor(this, LINE_BREAKER_ATTRIBUTES) {
			public Object get(CoObjectIF subject) {
				return ((CoHyphenationIF) subject).getMutableLineBreaker();
			}
		}), (CoSubcanvas) getNamedWidget(LINE_BREAKER_ATTRIBUTES));

	}
	protected void createWidgets(CoPanel p, CoUserInterfaceBuilder b) {
		super.createWidgets(p, b);

		p.setLayout(new CoColumnLayout());

		{
			CoPanel P = b.createPanel(new CoFormLayout());
			p.add(P);

			P.add(b.createLabel(CoTextStringResources.getName(NAME)));
			P.add(b.createTextField(NAME));

			P.add(b.createLabel(CoTextStringResources.getName(LINE_BREAKER)));
			CoOptionMenu cb = b.createOptionMenu(LINE_BREAKER);
			P.add(cb);
			cb.addItem(CoAnywhereLineBreakerIF.ANYWHERE_LINE_BREAKER);
			cb.addItem(CoLiangLineBreakerIF.LIANG_LINE_BREAKER);
			cb.addItem(CoWordLineBreakerIF.BETWEEN_WORDS_LINE_BREAKER);
			cb.setRenderer(new CoOptionMenu.TranslatingRenderer(CoTextStringResources.getBundle()));

			P.add(b.createLabel(CoTextStringResources.getName(HYPHENATION_FALLBACK_BEHAVIOR)));
			cb = b.createOptionMenu(HYPHENATION_FALLBACK_BEHAVIOR);
			P.add(cb);
			cb.addItem(CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_FIRST_BREAKPOINT);
			cb.addItem(CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_NO_LINE);
			cb.addItem(CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_NON_BREAKPOINT);
			cb.setRenderer(new CoOptionMenu.TranslatingRenderer(CoTextStringResources.getBundle()));
		}

		p.add(b.createSubcanvas(LINE_BREAKER_ATTRIBUTES));
	}
	private void updateLineBreakerAttributePanel() {
		CoHyphenationIF h = (CoHyphenationIF) getDomain();

		if (h == null) {
			((CoSubcanvas) getNamedWidget(LINE_BREAKER_ATTRIBUTES)).setUserInterface(null);
			return;
		}

		String uiName = h.getLineBreaker().getUIname();

		CoDomainUserInterface ui = (CoDomainUserInterface) m_uiCache.get(uiName);

		if (ui == null) {
			try {
				ui = (CoDomainUserInterface) Class.forName(uiName).newInstance();
				m_uiCache.put(uiName, ui);
			} catch (Exception ex) {
				CoAssertion.assertTrue(false, "Line breaker ui creation failure");
			}
		}

		((CoSubcanvas) getNamedWidget(LINE_BREAKER_ATTRIBUTES)).setUserInterface(ui);
		ui.setDomain(h.getMutableLineBreaker());
	}
}