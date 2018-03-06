package com.bluebrim.browser.client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.gui.client.CoDomainUserInterface;
import com.bluebrim.gui.client.CoSubcanvas;
import com.bluebrim.gui.client.CoUserInterfaceBuilder;
import com.bluebrim.swing.client.CoPanel;
import com.bluebrim.swing.client.CoSplitPane;


/**
 * Abstract tree catalog ui, showing the selected element 
 * in a userinterface displayed in the right or lower part 
 * of the ui.
 */
public abstract class CoBasicTreeCatalogUI extends CoBasicTreeUI implements CoTreeCatalogUI {
	
	private Map			m_uiCache;
	private CoSubcanvas m_elementSubcanvas;

/**
 * com.bluebrim.organization.impl.client.CoOrganizationModelUI constructor comment.
 */
public CoBasicTreeCatalogUI() {
	this(null);
}


/**
 */
public CoBasicTreeCatalogUI(CoObjectIF object) {
	super(object);
	m_uiCache = new HashMap();
}


protected final void addUserInterface(CoDomainUserInterface ui, Object key)
{
	m_uiCache.put(key, ui);
}


protected void buildMainPanel(CoPanel aPanel, CoUserInterfaceBuilder aBuilder)
{
	CoSplitPane tSplitPane = aBuilder.createSplitPane(false,createTreeBox(aBuilder) ,m_elementSubcanvas = aBuilder.createSubcanvas());
	m_elementSubcanvas.setEmptySize(new Dimension(500,300));
	aPanel.add(tSplitPane, BorderLayout.CENTER);
}


protected abstract Object elementToKey (CoTreeCatalogElementIF element);


protected final CoSubcanvas getElementSubcanvas()
{
	return m_elementSubcanvas;
}


protected String getTreeBoxConstraints()
{
	return BorderLayout.WEST;
}


/**
 */
protected CoDomainUserInterface getUserInterfaceFor(CoTreeCatalogElementIF element) {
	
	return (CoDomainUserInterface )m_uiCache.get(elementToKey(element));
}


protected void handleMultiSelection ()
{	
	super.handleMultiSelection();
	installUserInterface(null, null);
}


protected void handleNoSelection ()
{
	super.handleNoSelection();
	installUserInterface(null, null);
}


protected void handleOneSelectedElement (CoTreeCatalogElementIF element) {
	super.handleOneSelectedElement(element);
	installUserInterface(getUserInterfaceFor(element), element.getTreeCatalogElement());
}


protected void installUserInterface (CoDomainUserInterface ui, CoTreeCatalogElementIF element)
{
	CoDomainUserInterface tPreviousUI = (CoDomainUserInterface )getElementSubcanvas().getUserInterface();
	if (tPreviousUI != ui)
	{
		if (tPreviousUI != null)
			tPreviousUI.setDomain(null);
		if (ui != null)
		{
			ui.buildForComponent();
			ui.setDomain(element);
		}
		getElementSubcanvas().setUserInterface(ui);
		if (ui != null)
			ui.getUIBuilder().userInterfaceOpenedInSubcanvas();
	}
	else if (ui != null)
		ui.setDomain(element);
}
}