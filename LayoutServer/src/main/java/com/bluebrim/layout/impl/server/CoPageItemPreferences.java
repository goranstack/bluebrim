package com.bluebrim.layout.impl.server;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.page.shared.*;
import com.bluebrim.pagesize.server.*;
import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.stroke.impl.shared.*;
import com.bluebrim.stroke.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.text.impl.server.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/*
*/

public class CoPageItemPreferences extends CoObject implements CoPageItemPreferencesIF, CoLayoutParametersProvider, CoPropertyChangeListener, CoMarkDirtyListener, CoLayoutServerConstants  {

	public static final String XML_CAPTION_BOX_PROTOTYPE = "caption-box-prototype";
	public static final String XML_FONT_FAMILY_NAMES = "font-family-names";
	public static final String XML_IMAGE_BOX_PROTOTYPE = "image-box-prototype";
	public static final String XML_KERN_ABOVE = "kern-above";
	public static final String XML_LAYOUT_AREA_PROTOTYPE = "layout-area-prototype";
	public static final String XML_TAG = "page-item-preferences";
	public static final String XML_TEXT_BOX_PROTOTYPE = "text-box-prototype";
	public static final String XML_USE_QXP_JUSTIFICATION = "qxp-justification";
	public static final String XML_COLORS = "colors";
	public static final String XML_HYPHENATIONS = "hyphenations";
	public static final String XML_STROKES = "strokes";
	public static final String XML_TAG_CHAINS = "tag-chains";
	public static final String XML_TAG_GROUPS = "tag-groups";
	public static final String XML_PAGE_SIZES = "page-sizes";

	private transient boolean dirtyFlag;

	private CoPageItemPrototype defaultTextboxPrototype;
	private CoPageItemPrototype defaultImageboxPrototype;
	private CoPageItemPrototype defaultCaptionboxPrototype;

	private List colors = new ArrayList(); // [ CoColorIF ]
	private List hyphenations = new ArrayList(); // [ CoHyphenationIF ]
	private List fontFamilyNames = new ArrayList(); // [ String ]
	private List tagChains = new ArrayList(); // [ CoTagChainIF ]
	private List tagGroups = new ArrayList(); // [ CoTagGroupIF ]
	private List strokes = new ArrayList(); // [ CoStrokeIF ]

	private List allColors; // [ CoColorIF ]
	private List allStrokes; // [ CoStrokeIF ]

	private CoDesktopLayoutArea desktop = new CoDesktopLayoutArea(this);
	private CoPageSizeCollectionIF pageSizes = new CoPageSizeCollection();

	private CoTextStyleApplier textStyleApplier = new CoTextStyleApplier();

	private List paragraphTagNames; // [ String ]

	private CoPropertyChangeListener propertyListener = createPropertyListener();
	private final CoTypographyRule typographyRule = createTypographyRule();

	private CoPageItemPrototype defaultLayoutAreaPrototype;

	private transient List allFontFamilyNames; // [ String ]
	private transient List allHyphenations; // [ CoHyphenationIF ]
	private transient List allParagraphTagNames; // [ String ]
	private transient List allTagChains; // [ CoTagChainIF ]
	private transient List allTagGroups; // [ CoTagGroupIF ]
	private transient int immutableFontFamilyCount;
	private CoPageItemPreferences resolver;

	private float kernAboveSize = Float.NaN;
	private Boolean useQxpJustification = null;

	public CoPageItemPreferences() {
		this(true);
	}
	
	public CoPageItemPreferences(CoPageItemPreferencesIF resolver, CoPageSizeCollectionIF pageSizes) {
		this(resolver);
		pageSizes = pageSizes;
	}

	public CoPageItemPreferences(CoPageItemPreferencesIF resolver) {
		CoAssertion.assertTrue(resolver != null, "resolver == null");

		setResolver(resolver);
	}

	private CoPageItemPreferences(boolean tryToFindResolver) {
		CoPageItemPreferencesIF resolver = null;

		if (tryToFindResolver) {
			CoLayoutParameters tmp = CoLayoutServerImpl.getInstance().getDefaultLayoutParameters();
			if ((tmp != null) && (tmp != this)) {
				resolver = (CoPageItemPreferencesIF) tmp;
			}
		}

		if (resolver == null)
			clearResolver();
		else
			setResolver(resolver);
		addStroke((CoStrokeIF) CoFactoryManager.createObject(CoStrokeIF.SOLID));
	}
	
	protected CoPageItemPreferences(Node node, CoXmlContext context) {
		kernAboveSize = CoXmlUtilities.parseFloat(node.getAttributes().getNamedItem(XML_KERN_ABOVE), Float.NaN);
		useQxpJustification = CoXmlUtilities.parseBoolean(node.getAttributes().getNamedItem(XML_USE_QXP_JUSTIFICATION), null);
	}

	
	public void addChain(CoTagChainIF chain) {
		if (tagChains.add(chain))
			markDirty();
	}

	public void addRGBColors() {
		if (getColor(CoPaintResources.getName(RED)) == null) {
			addColor(new CoSpotColor(Color.red, CoPaintResources.getName(RED)));
		}

		if (getColor(CoPaintResources.getName(GREEN)) == null) {
			addColor(new CoSpotColor(Color.green, CoPaintResources.getName(GREEN)));
		}

		if (getColor(CoPaintResources.getName(BLUE)) == null) {
			addColor(new CoSpotColor(Color.blue, CoPaintResources.getName(BLUE)));
		}

	}
		
	public void addCMYKColors() {
		addColor((CoColorIF) CoFactoryManager.createObject(CoProcessCyanIF.PROCESS_CYAN));
		addColor((CoColorIF) CoFactoryManager.createObject(CoProcessMagentaIF.PROCESS_MAGENTA));
		addColor((CoColorIF) CoFactoryManager.createObject(CoProcessYellowIF.PROCESS_YELLOW));
		addColor((CoColorIF) CoFactoryManager.createObject(CoProcessBlackIF.PROCESS_BLACK));
		addColor((CoColorIF) CoFactoryManager.createObject(CoRegistrationColorIF.REGISTRATION_COLOR));
		addColor((CoColorIF) CoFactoryManager.createObject(CoWhiteColorIF.WHITE_COLOR));
	}

	public void addDefaultStrokes() {
		CoStrokeIF spec = null;
		CoDashIF dash = null;
		CoStrokeLayerIF strokeLayer = null;

		if (getStroke(CoStrokeResources.getName(ALL_DOTS)) == null) {
			// All Dots
			spec = createStroke();
			addStroke(spec);
			spec.setName(CoStrokeResources.getName(ALL_DOTS));
			dash = spec.get(0).getDash();
			dash.setCap(CoDashIF.CAP_ROUND);
			dash.setJoin(CoDashIF.JOIN_MITER);
			dash.setCycleLength(2);
			dash.setCycleLengthInWidth(true);
			dash.setDash(new float[] { 0, 1 });
		}

		if (getStroke(CoStrokeResources.getName(DASH_DOT)) == null) {
			// Dash Dots
			spec = createStroke();
			addStroke(spec);
			spec.setName(CoStrokeResources.getName(DASH_DOT));
			dash = spec.get(0).getDash();
			dash.setCap(CoDashIF.CAP_BUTT);
			dash.setJoin(CoDashIF.JOIN_MITER);
			dash.setCycleLength(11);
			dash.setCycleLengthInWidth(true);
			dash.setDash(new float[] { 0.545f, 0.18f, 0.091f, 0.182f });
		}

		if (getStroke(CoStrokeResources.getName(DOTTED)) == null) {
			// Dotted
			spec = createStroke();
			addStroke(spec);
			spec.setName(CoStrokeResources.getName(DOTTED));
			dash = spec.get(0).getDash();
			dash.setCap(CoDashIF.CAP_BUTT);
			dash.setJoin(CoDashIF.JOIN_MITER);
			dash.setCycleLength(5);
			dash.setCycleLengthInWidth(true);
			dash.setDash(new float[] { 0.6f, 0.4f });
		}

		if (getStroke(CoStrokeResources.getName(DOTTED2)) == null) {
			// Dotted2
			spec = createStroke();
			addStroke(spec);
			spec.setName(CoStrokeResources.getName(DOTTED2));
			dash = spec.get(0).getDash();
			dash.setCap(CoDashIF.CAP_BUTT);
			dash.setJoin(CoDashIF.JOIN_MITER);
			dash.setCycleLength(4);
			dash.setCycleLengthInWidth(true);
			dash.setDash(new float[] { 0.75f, 0.25f });
		}

		if (getStroke(CoStrokeResources.getName(DOUBLE)) == null) {
			// Double
			spec = createStroke();
			addStroke(spec);
			spec.clear();
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.333f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.333f);
			strokeLayer.setColor(CoBackgroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.333f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			spec.setName(CoStrokeResources.getName(DOUBLE));
		}

		if (getStroke(CoStrokeResources.getName(THICK_THIN)) == null) {
			// Thick-Thin
			spec = createStroke();
			addStroke(spec);
			spec.clear();
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.5f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.333f);
			strokeLayer.setColor(CoBackgroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.167f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			spec.setName(CoStrokeResources.getName(THICK_THIN));
		}

		if (getStroke(CoStrokeResources.getName(THICK_THIN_THICK)) == null) {
			// Thick-Thin-Thick
			spec = createStroke();
			addStroke(spec);
			spec.clear();
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.286f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.143f);
			strokeLayer.setColor(CoBackgroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.143f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.143f);
			strokeLayer.setColor(CoBackgroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.286f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			spec.setName(CoStrokeResources.getName(THICK_THIN_THICK));
		}

		if (getStroke(CoStrokeResources.getName(ALL_DOTS)) == null) {
			// Thin-Thick
			spec = createStroke();
			addStroke(spec);
			spec.clear();
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.167f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.333f);
			strokeLayer.setColor(CoBackgroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.5f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			spec.setName(CoStrokeResources.getName(THIN_THICK));
		}

		if (getStroke(CoStrokeResources.getName(THIN_THICK_THIN)) == null) {
			// Thin-Thick-Thin
			spec = createStroke();
			addStroke(spec);
			spec.clear();
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.125f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.208f);
			strokeLayer.setColor(CoBackgroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.333f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.208f);
			strokeLayer.setColor(CoBackgroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.125f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			spec.setName(CoStrokeResources.getName(THIN_THICK_THIN));
		}

		if (getStroke(CoStrokeResources.getName(TRIPLE)) == null) {
			// Triple
			spec = createStroke();
			addStroke(spec);
			spec.clear();
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.167f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.25f);
			strokeLayer.setColor(CoBackgroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.167f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.25f);
			strokeLayer.setColor(CoBackgroundDashColor.getInstance());
			strokeLayer = spec.add();
			strokeLayer.setWidthProportion(0.167f);
			strokeLayer.setColor(CoForegroundDashColor.getInstance());
			spec.setName(CoStrokeResources.getName(TRIPLE));
		}
	}
	public void addFontFamily(String font) {
		if (fontFamilyNames.contains(font))
			return;

		fontFamilyNames.add(font);

		updateFontFamilyNames();

		markDirty();
	}
	public void addHyphenation(CoHyphenationIF hs) {
		if (hyphenations.add(hs))
			markDirty();
	}

	public void addStroke(CoStrokeIF s) {
		// PENDING: color, ...
		if (s.isMutable())
			s.setOwner(this);

		if (strokes.add(s)) {
			markDirty();
		}
	}
	public void addTagGroup(CoTagGroupIF g) {
		if (tagGroups.add(g))
			markDirty();
	}
	public void buildTextStyleApplier(CoTextStyleApplier a) {
		if (resolver != null) {
			resolver.buildTextStyleApplier(a);
		}

		a.extractStyles(this);
	}
	private void clearResolver() {
		allColors = colors;
		allStrokes = strokes;
		allHyphenations = hyphenations;
		allTagChains = tagChains;
		allTagGroups = tagGroups;
		allFontFamilyNames = fontFamilyNames;

		createDefaultPrototypes();
	}
	public CoTagChainIF createChain(String name) {
		CoTagChainIF chain = new CoTagChain(name);
		return chain;
	}

	/**
	 * PENDING: Fix proper handling of page sizes instead of simple copying
	 */ 
	public CoLayoutParameters createSubParameters() {
		CoAssertion.assertTrue((pageSizes.getPageSizes().size() > 0), "Inga sidstorlekar!!!");
		return new CoPageItemPreferences(this, pageSizes.copy());
	}

	private void createDefaultPrototypes() {
		CoPageItemFactoryIF f = (CoPageItemFactoryIF) CoFactoryManager.getFactory(CoPageItemFactoryIF.PAGE_ITEM_FACTORY);

		defaultLayoutAreaPrototype = new CoPageItemPrototype(CoLayoutServerResources.getName(LAYOUT_AREA_PROTOTYPE), "", f.createLayoutArea());
		defaultLayoutAreaPrototype.setRenameable(false);
		defaultLayoutAreaPrototype.setDeleteable(false);

		defaultTextboxPrototype = new CoPageItemPrototype(CoLayoutServerResources.getName(TEXT_BOX_PROTOTYPE), "", f.createWorkPieceTextBox());
		defaultTextboxPrototype.setRenameable(false);
		defaultTextboxPrototype.setDeleteable(false);

		defaultImageboxPrototype = new CoPageItemPrototype(CoLayoutServerResources.getName(IMAGE_BOX_PROTOTYPE), "", f.createImageBox());
		defaultImageboxPrototype.setRenameable(false);
		defaultImageboxPrototype.setDeleteable(false);

		CoContentWrapperPageItemIF p = f.createCaptionBox();
		defaultCaptionboxPrototype = new CoPageItemPrototype(CoLayoutServerResources.getName(CAPTION_BOX_PROTOTYPE), "", p);
		defaultCaptionboxPrototype.setRenameable(false);
		defaultCaptionboxPrototype.setDeleteable(false);
		p.setDerivedColumnGrid(false);
		p.getMutableColumnGrid().setColumnCount(1);
		p.setDoRunAround(false);
	}
	public CoHyphenationIF createHyphenation() {
		return new CoHyphenation(CoStringResources.getName(CoConstants.UNTITLED));
	}
	public static CoPageItemPreferences createInstanceWithoutResolver() {
		return new CoPageItemPreferences(false);
	}
	public CoMultiInkColorIF createMultiInkColor() {
		CoExtendedMultiInkColorIF multiInkColor = new CoExtendedMultiInkColor();
		multiInkColor.setName(CoStringResources.getName(CoConstants.UNTITLED));
		return multiInkColor;
	}
	private CoPropertyChangeListener createPropertyListener() {
		return new CoPropertyChangeListener() {
			public void propertyChange(CoPropertyChangeEvent ev) {
				markDirty();
			}
		};
	}
	public CoSpotColorIF createSpotColor() {
		return new CoSpotColor(Color.black, CoStringResources.getName(CoConstants.UNTITLED));
	}
	public CoStrokeIF createStroke() {
		return new CoStroke();
	}
	public CoTagGroupIF createTagGroup(String name) {
		CoTagGroupIF g = new CoTagGroup(name);
		return g;
	}
	
	private final CoTypographyRule createTypographyRule() {

		CoTypographyRule tr = new CoTypographyRule() {
			protected void markDirty() {
				super.markDirty();
				CoPageItemPreferences.this.markDirty();
			}
		};

		return tr;
	}

	
	public void dispose() {
		setResolver(null);
	}

	public boolean doUseQxpJustification() {
		if (useQxpJustification != null)
			return useQxpJustification.booleanValue();
		if (resolver != null)
			return resolver.doUseQxpJustification();
		return false;
	}

	public CoContentWrapperPageItemIF getCaptionboxPrototype() {
		if (defaultCaptionboxPrototype != null)
			return (CoContentWrapperPageItemIF) defaultCaptionboxPrototype.getPageItem();
		else if (resolver != null)
			return resolver.getCaptionboxPrototype();
		else
			return null;
	}

	public java.util.List getChains() {
		return allTagChains;
	}

	public CoDesktopLayout getDesktop() {
		return desktop;
	}

	public CoDesktopLayoutAreaIF getDesktopLayoutArea() {
		return desktop;
	}

	public String getFactoryKey() {
		return null;
	}

	public List getFontFamilyNames() {
		return allFontFamilyNames;
	}

	public CoHyphenationIF getHyphenation(String name) {
		Iterator i = getHyphenations().iterator();
		while (i.hasNext()) {
			CoHyphenationIF c = (CoHyphenationIF) i.next();
			if (c.getName().equals(name))
				return c;
		}

		return null;
	}

	public List getHyphenations() {
		return allHyphenations;
	}

	public CoContentWrapperPageItemIF getImageboxPrototype() {
		if (defaultImageboxPrototype != null)
			return (CoContentWrapperPageItemIF) defaultImageboxPrototype.getPageItem();
		else if (resolver != null)
			return resolver.getImageboxPrototype();
		else
			return null;
	}

	public int getImmutableChainCount() {
		return allTagChains.size() - tagChains.size();
	}

	public int getImmutableColorCount() {
		return allColors.size() - colors.size();
	}

	public int getImmutableFontFamilyCount() {
		return immutableFontFamilyCount;
	}

	public int getImmutableGroupCount() {
		return allTagGroups.size() - tagGroups.size();
	}

	public int getImmutableHyphenationCount() {
		return allHyphenations.size() - hyphenations.size();
	}

	public int getImmutableStrokeCount() {
		return allStrokes.size() - strokes.size();
	}

	public float getKernAboveSize(boolean local) {
		if (local || !Float.isNaN(kernAboveSize)) {
			return kernAboveSize;
		}

		if (resolver != null) {
			return resolver.getKernAboveSize(local);
		}

		return 0;
	}

	public CoLayoutAreaIF getLayoutAreaPrototype() {
		if (defaultLayoutAreaPrototype != null)
			return (CoLayoutAreaIF) defaultLayoutAreaPrototype.getPageItem();
		else if (resolver != null)
			return resolver.getLayoutAreaPrototype();
		else
			return null;

	}

	public CoLayoutParameters getLayoutParameters() {
		return this;
	}


	public List getParagraphTagNames() // [ String ]
	{
		if (paragraphTagNames == null) {
			paragraphTagNames = new ArrayList();

			if (resolver == null)
				paragraphTagNames.add(CoTextConstants.DEFAULT_TAG_NAME);

			Iterator i = typographyRule.getParagraphStyles().iterator();

			while (i.hasNext()) {
				CoParagraphStyleIF p = (CoParagraphStyleIF) i.next();
				paragraphTagNames.add(p.getName());
			}

			if (resolver == null) {
				allParagraphTagNames = paragraphTagNames;
			} else {
				allParagraphTagNames = new CoAppendingListProxy(resolver.getParagraphTagNames(), paragraphTagNames);
			}
		}

		return allParagraphTagNames;
	}

	public CoPageItemPreferences getResolver() {
		return resolver;
	}

	public CoStrokeIF getStroke(String name) {
		Iterator i = getStrokes().iterator();
		while (i.hasNext()) {
			CoStrokeIF c = (CoStrokeIF) i.next();
			if (c.getName().equals(name))
				return c;
		}

		return null;
	}

	/**
	 * PENDING: optimize
	 */
	public CoStrokeIF getStroke(CoGOI goi) {
		CoStrokeIF s = (CoStrokeIF) CoFactoryManager.createObject(CoStrokeIF.SOLID);
		if (goi.equals(s.getGOI()))
			return s;

		int I = allStrokes.size();
		for (int i = 0; i < I; i++) {
			s = (CoStrokeIF) allStrokes.get(i);
			if (goi.equals(s.getGOI()))
				return s;
		}

		return null;
	}

	public List getStrokes() {
		return allStrokes;
	}

	public java.util.List getTagChains() {
		return allTagChains;
	}

	public java.util.List getTagGroups() {
		return allTagGroups;
	}

	public CoContentWrapperPageItemIF getTextboxPrototype() {
		if (defaultTextboxPrototype != null)
			return (CoContentWrapperPageItemIF) defaultTextboxPrototype.getPageItem();
		else if (resolver != null)
			return resolver.getTextboxPrototype();
		else
			return null;
	}

	public CoTextStyleApplier getTextStyleApplier() {
		textStyleApplier.clear();
		buildTextStyleApplier(textStyleApplier);
		textStyleApplier.extract(this);
		return textStyleApplier;
	}

	public CoTypographyContextIF getTypographyContext() {
		return this;
	}

	public CoTypographyRuleIF getTypographyRule() {
		return typographyRule;
	}

	public Boolean getUseQxpJustification() {
		return useQxpJustification;
	}

	public void markDirty() {
		paragraphTagNames = null;
		allParagraphTagNames = null;

		dirtyFlag = !dirtyFlag;

		if (CoAssertion.SIMULATION_SUPPORT)
			CoAssertion.addChangedObject(this);

		firePropertyChange("anything", Boolean.FALSE, Boolean.TRUE);
	}
	// resolver changed

	public void propertyChange(CoPropertyChangeEvent ev) {
		updateFontFamilyNames();
		markDirty();
	}

	public void removeAllFontFamilyNames() {
		if (!fontFamilyNames.isEmpty()) {
			fontFamilyNames.clear();

			updateFontFamilyNames();

			markDirty();
		}
	}

	public void removeChain(CoTagChainIF chain) {
		if (tagChains.remove(chain))
			markDirty();
	}

	public void removeFontFamilyNames(Object[] fonts) {
		boolean b = false;

		for (int i = 0; i < fonts.length; i++) {
			int j = fontFamilyNames.indexOf(fonts[i]);
			if (j != -1) {
				b = true;
				fontFamilyNames.remove(j);
			}
		}

		if (b) {
			updateFontFamilyNames();
			markDirty();
		}
	}

	public void removeHyphenation(CoHyphenationIF hs) {
		if (hyphenations.remove(hs))
			markDirty();
	}

	public void removeStroke(Object[] strokes) {
		boolean b = false;

		for (int i = 0; i < strokes.length; i++) {
			int j = this.strokes.indexOf(strokes[i]);
			if (j != -1) {
				b = true;
				this.strokes.remove(j);
			}
		}

		if (b)
			markDirty();
	}

	public void removeTagGroup(CoTagGroupIF g) {
		if (tagGroups.remove(g))
			markDirty();

	}

	public void setKernAboveSize(float kas) {
		if (kernAboveSize == kas)
			return;

		kernAboveSize = kas;

		markDirty();
	}

	public void setResolver(CoPageItemPreferencesIF resolver) {
		if (resolver == resolver)
			return;

		if (resolver != null) {
			resolver.removePropertyChangeListener(this);
		}

		resolver = (CoPageItemPreferences) resolver;

		if (resolver == null) {
			clearResolver();

		} else {

			allColors = new CoAppendingListProxy(resolver.getColors(), colors);
			allStrokes = new CoAppendingListProxy(resolver.getStrokes(), strokes);
			allHyphenations = new CoAppendingListProxy(((CoTypographyContextIF) resolver).getHyphenations(), hyphenations);
			allTagChains = new CoAppendingListProxy(resolver.getTagChains(), tagChains);
			allTagGroups = new CoAppendingListProxy(resolver.getTagGroups(), tagGroups);

			allFontFamilyNames = new ArrayList();
			updateFontFamilyNames();

			resolver.addPropertyChangeListener(this);
		}

		markDirty();
	}

	public void setUseQxpJustification(Boolean b) {
		if (useQxpJustification == b)
			return;

		useQxpJustification = b;

		markDirty();

	}

	private void updateFontFamilyNames() {
		if (resolver == null)
			return;

		allFontFamilyNames.clear();

		allFontFamilyNames.addAll(resolver.getFontFamilyNames());

		immutableFontFamilyCount = allFontFamilyNames.size();

		int I = fontFamilyNames.size();

		for (int i = 0; i < I; i++) {
			if (!allFontFamilyNames.contains(fontFamilyNames.get(i))) {
				allFontFamilyNames.add(fontFamilyNames.get(i));
			}
		}
	}

	public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
		if (name == null) {
			if (subModel instanceof CoDesktopLayoutArea) {
			} else if (subModel instanceof CoPageItemPreferencesIF) {
				setResolver((CoPageItemPreferencesIF) subModel);
			} else if (subModel instanceof CoTypographyRuleIF) {
				typographyRule.copyFrom((CoTypographyRuleIF) subModel);
			}

		} else {
			if (name.equals(XML_TEXT_BOX_PROTOTYPE)) {
				defaultTextboxPrototype = (CoPageItemPrototype) subModel;
			} else if (name.equals(XML_IMAGE_BOX_PROTOTYPE)) {
				defaultImageboxPrototype = (CoPageItemPrototype) subModel;
			} else if (name.equals(XML_CAPTION_BOX_PROTOTYPE)) {
				defaultCaptionboxPrototype = (CoPageItemPrototype) subModel;
			} else if (name.equals(XML_LAYOUT_AREA_PROTOTYPE)) {
				defaultLayoutAreaPrototype = (CoPageItemPrototype) subModel;
			} else if (name.equals(XML_FONT_FAMILY_NAMES)) {
				fontFamilyNames.clear();
				Iterator i = (Iterator) subModel;
				while (i.hasNext()) {
					fontFamilyNames.add(i.next());
				}
			} else if (name.equals(XML_COLORS)) {
				colors.clear();
				Iterator i = (Iterator) subModel;
				while (i.hasNext()) {
					colors.add(i.next());
				}
			} else if (name.equals(XML_STROKES)) {
				strokes.clear();
				Iterator i = (Iterator) subModel;
				while (i.hasNext()) {
					addStroke((CoStrokeIF) i.next());
				}
			} else if (name.equals(XML_HYPHENATIONS)) {
				hyphenations.clear();
				Iterator i = (Iterator) subModel;
				while (i.hasNext()) {
					hyphenations.add(i.next());
				}
			} else if (name.equals(XML_TAG_CHAINS)) {
				tagChains.clear();
				Iterator i = (Iterator) subModel;
				while (i.hasNext()) {
					tagChains.add(i.next());
				}
			} else if (name.equals(XML_TAG_GROUPS)) {
				tagGroups.clear();
				Iterator i = (Iterator) subModel;
				while (i.hasNext()) {
					tagGroups.add(i.next());
				}
			} else if (name.equals(XML_PAGE_SIZES)) {
				Iterator i = (Iterator) subModel;
				while (i.hasNext()) {
					pageSizes.addPageSize((CoPageSizeIF)i.next());
				}
			}
		}

	}

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, org.w3c.dom.Node node, CoXmlContext context) {
		return new CoPageItemPreferences(node, context);
	}
	
	public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
		clearResolver();
		updateFontFamilyNames();
	}

	/**
	 *	Used by a CoXmlVisitorIF instance when visiting an object.
	 *  The object then uses the CoXmlVisitorIF interface to feed the
	 *  visitor with state information
	 */
	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.export(XML_FONT_FAMILY_NAMES, fontFamilyNames);

		if (!Float.isNaN(kernAboveSize))
			visitor.exportAttribute(XML_KERN_ABOVE, Float.toString(kernAboveSize));
		if (useQxpJustification != null)
			visitor.exportAttribute(XML_USE_QXP_JUSTIFICATION, useQxpJustification.toString());

		visitor.export(XML_COLORS, colors);
		visitor.export(XML_STROKES, strokes);
		visitor.export(XML_HYPHENATIONS, hyphenations);
		visitor.export(XML_TAG_CHAINS, tagChains);
		visitor.export(XML_TAG_GROUPS, tagGroups);
		visitor.export(XML_PAGE_SIZES, pageSizes.getPageSizes());

		visitor.export(typographyRule);

		if (defaultTextboxPrototype != null)
			visitor.export(CoXmlWrapperFlavors.NAMED, XML_TEXT_BOX_PROTOTYPE, defaultTextboxPrototype);
		if (defaultImageboxPrototype != null)
			visitor.export(CoXmlWrapperFlavors.NAMED, XML_IMAGE_BOX_PROTOTYPE, defaultImageboxPrototype);
		if (defaultCaptionboxPrototype != null)
			visitor.export(CoXmlWrapperFlavors.NAMED, XML_CAPTION_BOX_PROTOTYPE, defaultCaptionboxPrototype);
		if (defaultLayoutAreaPrototype != null)
			visitor.export(CoXmlWrapperFlavors.NAMED, XML_LAYOUT_AREA_PROTOTYPE, defaultLayoutAreaPrototype);

		visitor.export(desktop);

		CoLayoutParameters top = CoLayoutServerImpl.getInstance().getDefaultLayoutParameters();
		if (resolver != top)
			visitor.export(resolver);
	}

	public void addColor(CoColorIF color) {
		if (colors.add(color))
			markDirty();
	}

	public CoColorIF getColor(String name) {
		Iterator i = getColors().iterator();
		while (i.hasNext()) {
			CoColorIF c = (CoColorIF) i.next();
			if (c.getName().equals(name))
				return c;
		}

		return null;
	}

	public CoColorCollectionIF getColorCollection() {
		return this;
	}

	/**
	 * PENDING: optimize
	 */

	public CoColorIF getColor(CoGOI goi) {
		CoColorIF s = (CoColorIF) CoFactoryManager.createObject(CoNoColorIF.NO_COLOR);
		if (goi.equals(s.getGOI()))
			return s;

		int I = allColors.size();
		for (int i = 0; i < I; i++) {
			s = (CoColorIF) allColors.get(i);
			if (goi.equals(s.getGOI()))
				return s;
		}

		return null;
	}

	public List getColors() {
		return allColors;
	}

	public void removeColor(Object[] colors) {
		boolean b = false;

		for (int i = 0; i < colors.length; i++) {
			int j = this.colors.indexOf(colors[i]);
			if (j != -1) {
				b = true;
				this.colors.remove(j);
			}
		}

		if (b)
			markDirty();
	}

	/**
	 * PENDING: This is a replacement for previous design with anonymous subclass that
	 * override getPageSizes. But the final solution should perhaps include
	 * a concatenation of page sizes in the resolver chain. /Göran S
	 */
	public CoPageSizeCollectionIF getPageSizeCollection() {
		if (pageSizes != null)
			return pageSizes;
		else
			return (resolver == null) ? null : resolver.getPageSizeCollection();
	}

	}