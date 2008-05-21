package com.bluebrim.gui.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.swing.client.*;

/**
 * A userinterface meant to be used in a modal dialog.
 * It has a button row at the bottom containing a Cancel
 * and a Ok button.
 */

public abstract class CoDialogDomainUserInterface extends CoDomainUserInterface
{
	private CoButton m_okButton;
	private CoButton m_cancelButton;
public CoDialogDomainUserInterface() {
	super();
}
public CoDialogDomainUserInterface(CoObjectIF domain) {
	super(domain);
}
	protected void closedByCancel()
	{
		sendCloseDialog(CoDialog.CLOSED_BY_CANCEL);
	}
	protected void closedByOk()
	{
		sendCloseDialog(CoDialog.CLOSED_BY_OK);
	}
	private CoPanel createButtonPanel(CoUserInterfaceBuilder builder)
	{
		// PENDING: This solution is very ugly, but it's a workaround for a bug in CoButtonLayout that seems to
		// add insets to bottom insted of to the top.
		CoPanel yPanel = builder.createBoxPanel(BoxLayout.Y_AXIS);
		
		CoPanel tPanel = builder.createPanel(new CoButtonPanelLayout(), false, new Insets(0, 0, 0, 0));
		
		String label = CoUIStringResources.getName(OK);
		tPanel.add(m_okButton = builder.createButton(label, null, OK), CoButtonPanelLayout.FROM_RIGHT_BOTTOM);
		m_okButton.setMnemonic(label.charAt(0));

		label = CoUIStringResources.getName(CANCEL);
		tPanel.add(m_cancelButton = builder.createButton(label, null, CANCEL), CoButtonPanelLayout.FROM_RIGHT_BOTTOM);
		m_cancelButton.setMnemonic(label.charAt(0));
		
		yPanel.add(Box.createVerticalStrut(10));
		yPanel.add(tPanel);	
		return yPanel;
	}
	protected abstract Component createCenterComponent(CoUserInterfaceBuilder builder);
	protected void createListeners()
	{
		super.createListeners();
		m_okButton.setDefaultCapable(true);
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				 closedByCancel();
			}
		});
		m_okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				closedByOk();
			}
		});
		m_okButton.setEnabled(false);
	}
	protected void createWidgets(CoPanel panel, CoUserInterfaceBuilder builder)
	{
		super.createWidgets(panel, builder);
		panel.add(createCenterComponent(builder), java.awt.BorderLayout.CENTER);
		panel.add(createButtonPanel(builder), java.awt.BorderLayout.SOUTH);
	}
protected final CoButton getCancelButton()
{
	return m_cancelButton;
}
protected final CoButton getOkButton()
{
	return m_okButton;
}
public boolean isLightWeightPopupEnabled()
{
	return false;
}
	public void prepareDialog(CoDialog dialog) {
		super.prepareDialog(dialog);

		if (dialog != null)
			dialog.getRootPane().setDefaultButton(m_okButton);
	}
}
