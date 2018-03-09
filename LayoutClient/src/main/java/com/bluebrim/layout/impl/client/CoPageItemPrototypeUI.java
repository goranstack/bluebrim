package com.bluebrim.layout.impl.client;
import java.awt.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.swing.client.*;

/**
 * UI with the layout editor as subcanvas used to
 * display and edit page item prototypes.
 * See CoPageItemUI for details on supplying a layout editor.
 * @author Ali Abida 1999-09-09
 * Dennis, 2000-11-21: replace layout editor with CoPageItemHolderUI
 */
 
public class CoPageItemPrototypeUI extends CoDomainUserInterface implements CoContextAcceptingUI
{
	
	private static final String PAGE_ITEM = "CoPageItemPrototypeUI.page_item";




protected void createValueModels(CoUserInterfaceBuilder aBuilder) {
	super.createValueModels(aBuilder);

	// NAME
	aBuilder.createTextFieldAdaptor(aBuilder.addAspectAdaptor(new CoGsAspectAdaptor(this, CoConstants.NAME) {
		protected Object get(CoObjectIF subject) {
			return ((CoPageItemPrototypeIF )subject).getName();
		}
		public void set(CoObjectIF subject, Object newValue) {
			((CoPageItemPrototypeIF )subject).setName((newValue != null) ? newValue.toString(): "");
		}
	}), (CoTextField )getNamedWidget(CoConstants.NAME));

	// DESCRIPTION
	aBuilder.createTextFieldAdaptor(aBuilder.addAspectAdaptor(new CoGsAspectAdaptor(this, CoConstants.DESCRIPTION) {
		protected Object get(CoObjectIF subject) {
			return ((CoPageItemPrototypeIF )subject).getDescription();
		}
		public void set(CoObjectIF subject, Object newValue) {
			((CoPageItemPrototypeIF )subject).setDescription((newValue != null) ? newValue.toString(): "");
		}
	}), (CoTextField )getNamedWidget(CoConstants.DESCRIPTION));

	// PAGE_ITEM
	aBuilder.createSubcanvasAdaptor(aBuilder.addAspectAdaptor(new CoReadOnlyAspectAdaptor(this, PAGE_ITEM) {	
		public Object get(CoObjectIF subject) {
			return subject;
		}
	}), (CoSubcanvas )getNamedWidget(PAGE_ITEM));	

}
protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder aBuilder) {
	super.createWidgets(aPanel, aBuilder);

	CoPanel outerPanel = aBuilder.createBoxPanel(BoxLayout.Y_AXIS);
	
	CoPanel formPanel = aBuilder.createPanel(new CoGridLayout(2,CoGridFlowLayout.LEFT_FILL,CoGridFlowLayout.CENTER,2,0), false, new Insets(4,4,4,4));
	formPanel.add(aBuilder.createLabel(CoStringResources.getName(CoConstants.NAME)));
	formPanel.add(aBuilder.createTextField(CoTextField.LEFT, 30, CoConstants.NAME));	
	formPanel.add(aBuilder.createLabel(CoStringResources.getName(CoConstants.DESCRIPTION)));
	formPanel.add(aBuilder.createTextField(CoTextField.LEFT, 30, CoConstants.DESCRIPTION));

	// SUBCANVAS
	CoSubcanvas tCanvas = aBuilder.createSubcanvas( m_pageItemHolder, PAGE_ITEM );

	// SPLITPANE
	CoSplitPane split	= aBuilder.createSplitPane(true, formPanel, tCanvas);
	split.setOrientation(JSplitPane.VERTICAL_SPLIT);
	
	outerPanel.add(split);
	aPanel.add(outerPanel, BorderLayout.CENTER);
}
protected Insets getDefaultPanelInsets() {
	return null;
}

protected void postDomainChange ( CoObjectIF d ) 
{
	super.postDomainChange( d );

	if
		( d != null )
	{
		getNamedWidget( CoConstants.NAME ).setEnabled( ( (CoPageItemPrototypeIF) d ).isRenameable() );
	}
}
public void valueHasChanged() 
{
	super.valueHasChanged();

	( (CoDomainUserInterface) ( (CoSubcanvas) getNamedWidget( PAGE_ITEM ) ).getUserInterface() ).valueHasChanged();
}



	private CoPageItemHolderUI m_pageItemHolder;

public CoPageItemPrototypeUI( CoObjectIF pageItemProtoytpe, CoUIContext uiContext )
{
	this( (CoLayoutEditorDialog) null, uiContext );

	setDomain( pageItemProtoytpe );
}



public CoGenericUIContext getCopyOfCurrentRequiredUIContext()
{
	return m_pageItemHolder.getCopyOfCurrentRequiredUIContext();
}

public void setUIContext( CoUIContext context )
{
	m_pageItemHolder.setUIContext( context );
}

public CoPageItemPrototypeUI( CoLayoutEditorDialog e, CoUIContext uiContext )
{
	super();

	m_pageItemHolder = new CoPageItemHolderUI( e, uiContext, null, null );
}
}