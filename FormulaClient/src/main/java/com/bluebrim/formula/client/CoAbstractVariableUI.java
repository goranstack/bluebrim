package com.bluebrim.formula.client;
import java.awt.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.formula.impl.client.*;
import com.bluebrim.formula.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

/**
 * 2 domains are used by the UI.
 * Notice: the second domain can be set when the UI is constructed (see setDomains)
 * It is only the first domain that is changed (see setDomain)
 * The first domain is a CoAbstractVariableIF with the variable data.
 * The second domain is a CoAbstractVariablesHolderIF (parent of the variable
 * used to update changes of variables name.
 */
 
public abstract class CoAbstractVariableUI extends CoDomainUserInterface {
	public static final String VARIABLE_TEXT_FIELD_ADAPTOR_NAME = "VariableTextFieldAdaptorName";
	public static final String VARIABLE_COMMENT_TEXT_AREA_ADAPTOR_NAME = "VariableCommentTextAreaAdaptorName";
	
	// shows the name of the variable
	protected CoTextField m_nameTextField;
	// shows the comment
	protected CoTextArea m_commentTextArea;
public CoAbstractVariableUI () {
	super();
}
public CoAbstractVariableUI (CoObjectIF aDomainObject) {
	super(aDomainObject);
}
protected void afterCreateUserInterface (Window aWindow ) {
	super.afterCreateUserInterface(aWindow);
	enableWriteableComponents(isEnabled());
}
//----------------------------------- VARIABLE COMMENT AREA FIELD -----------------------------------------

protected CoPanel createCommentTextAreaWidget (CoUserInterfaceBuilder builder ) {
	CoPanel panel  = builder.createPanel(new BorderLayout(5, 5));
	
	CoLabel tLabel = builder.createLabel(CoFormulaUIResources.getName("COMMENT_LABEL"));
	tLabel.setForeground(Color.red);
	panel.add(tLabel, BorderLayout.NORTH);
	
	m_commentTextArea = builder.createTextArea();
	m_commentTextArea.setEnabled(false);

	JScrollPane scroller = 
		new JScrollPane(
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	JViewport port = scroller.getViewport();
	port.add(m_commentTextArea);
	port.setScrollMode(JViewport.BLIT_SCROLL_MODE);
	port.setPreferredSize(new Dimension(500, 50));
	
	panel.add(scroller, BorderLayout.CENTER);
	
	return panel;
}
/*
Denna metod anropas i #afterPostBuild för att ge en möjlighet att bygga upp 
beroende mellan gränssnittskomponenter/värdeobjekt och själva gränssnittet.
 */

protected void createListeners() {
	super.createListeners();
	addValueListener(new CoValueListener() {
		public void valueChange(CoValueChangeEvent e) {
			setEnabled(e.isWindowClosingEvent() ? false : true);
		}
	});
	addEnableDisableListener(new CoEnableDisableListener() {
		public void enableDisable(CoEnableDisableEvent e) {
			enableWriteableComponents(e.enable());
		}
	});
}
public CoLabel createNameLabel (CoUserInterfaceBuilder builder) {
	return builder.createLabel(CoFormulaUIResources.getName(CoFormulaUIResources.VARIABLE_NAME_LABEL));
}
//----------------------------------- VARIABLE NAME TEXT FIELD -----------------------------------------

protected CoPanel createNameTextFieldWidget (CoUserInterfaceBuilder builder ) {
	CoPanel p  = builder.createPanel(new BorderLayout(5, 5));
	
	p.add(createNameLabel(builder), BorderLayout.NORTH);
	
	m_nameTextField = builder.createTextField();
	m_nameTextField.setEnabled(false);
	p.add(m_nameTextField, BorderLayout.CENTER);
	
	return p;
}
protected void createUIWidgets (CoPanel panel, CoUserInterfaceBuilder builder ) {
	panel.add(createNameTextFieldWidget(builder), BorderLayout.NORTH);
	panel.add(createCommentTextAreaWidget(builder), BorderLayout.CENTER);
}
protected void createValueModels (CoUserInterfaceBuilder builder ) {
	super.createValueModels(builder);

	//----------------------------------- VARIABLE NAME TEXT FIELD -----------------------------------------
	
	builder.createTextFieldAdaptor(
		builder.addAspectAdaptor(
			new CoAspectAdaptor(this, VARIABLE_TEXT_FIELD_ADAPTOR_NAME) {
				public void set(CoObjectIF subject, Object value) {
					if (getVariables() == null) {
						// do nothing (?)
					} else {
						if (getVariables().setVariableName((CoAbstractVariableIF)subject, (String )value)) {
							// the new variable name is set, do nothing more
						} else {
							// the name is not set, inform that the name is not legal (already used)
							JOptionPane.showMessageDialog(
								null, CoFormulaUIResources.getName(CoFormulaUIResources.VARIABLE_NAME_ALREADY_USED), 
								"", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				public void setValue(Object aValue) {
					super.setValue(aValue);
					m_nameTextField.setText(((CoAbstractVariableIF)getSubject()).getName());
				}
				public void initValue(Object aValue) {
					super.initValue(aValue);
					m_nameTextField.setText(((CoAbstractVariableIF)getSubject()).getName());
				}
				public Object get(CoObjectIF subject) {
					return ((CoAbstractVariableIF)subject).getName();
				}}), 
		m_nameTextField);

	//----------------------------------- VARIABLE COMMENT TEXT AREA -----------------------------------------
	
	builder.createTextAreaAdaptor(
		builder.addAspectAdaptor(
			new CoAspectAdaptor(this, VARIABLE_COMMENT_TEXT_AREA_ADAPTOR_NAME) {
				public void set(CoObjectIF subject, Object value) {
					((CoAbstractVariableIF)subject).setComment(m_commentTextArea.getText());
				}
				public Object get(CoObjectIF subject) {
					return ((CoAbstractVariableIF)subject).getComment();
				}}), 
		m_commentTextArea);
}
protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder builder) {
	super.createWidgets(aPanel, builder);
	CoPanel panel = builder.createPanel(new BorderLayout(5, 5));
	aPanel.add(panel, BorderLayout.CENTER);
	createUIWidgets(panel, builder);
}
protected boolean enableWriteableComponents (boolean enabled) {
	boolean e = enabled;
	if (m_nameTextField != null) {
		CoAbstractVariableIF var = (CoAbstractVariableIF)getDomain();
		e = enabled && var != null && var.isWriteable();
		m_nameTextField.setEnabled(e);
		m_commentTextArea.setEnabled(e);
	}
	return e;
}
protected Insets getDefaultPanelInsets() {
	return null;
}
protected abstract com.bluebrim.formula.shared.CoAbstractVariablesHolderIF getVariables();
public void setEnabled(boolean enable)
{
	super.setEnabled(enable);
	enableWriteableComponents(isEnabled());
}
}
