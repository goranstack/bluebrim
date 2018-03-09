package com.bluebrim.gui.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.bluebrim.swing.client.CoButton;
import com.bluebrim.swing.client.CoButtonPanelLayout;
import com.bluebrim.swing.client.CoPanel;

/**
 * A userinterface meant to be used in a modal dialog.
 * It has a button row at the bottom containing a Cancel
 * and a Ok button.
 */
public abstract class CoDialogUserInterface extends CoUserInterface
{
	private CoButton m_okButton;
	private CoButton m_cancelButton;

	protected void closedByCancel()
	{
		sendCloseDialog(CoDialog.CLOSED_BY_CANCEL);
	}
	protected void closedByOk()
	{
		sendCloseDialog(CoDialog.CLOSED_BY_OK);
	}
protected CoPanel createButtonPanel(CoUserInterfaceBuilder builder)
{
	CoPanel tPanel = basicCreateButtonPanel(builder);
	
	String label = CoUIStringResources.getName(OK);
	tPanel.add(m_okButton = builder.createButton(label, null, OK), CoButtonPanelLayout.FROM_RIGHT_BOTTOM);
	m_okButton.setMnemonic(label.charAt(0));
	
	label = CoUIStringResources.getName(CANCEL);
	tPanel.add(m_cancelButton = builder.createButton(label, null, CANCEL), CoButtonPanelLayout.FROM_RIGHT_BOTTOM);
	m_cancelButton.setMnemonic(label.charAt(0));

	return tPanel;
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
		panel.add(createCenterComponent(builder), BorderLayout.CENTER);
		panel.add(createButtonPanel(builder), BorderLayout.SOUTH);
	}
protected final CoButton getCancelButton()
{
	return m_cancelButton;
}
/**
 * Declared public so it can be accessed
 * from inner subclasses.
 */
public final CoButton getOkButton()
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

protected CoPanel basicCreateButtonPanel(CoUserInterfaceBuilder builder)
{
	return builder.createPanel(new CoButtonPanelLayout(), false, new Insets(4, 4, 4, 4));
}
}