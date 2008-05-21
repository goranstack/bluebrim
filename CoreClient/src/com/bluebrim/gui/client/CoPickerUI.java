package com.bluebrim.gui.client;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;

import com.bluebrim.browser.client.CoCatalogListCellRenderer;
import com.bluebrim.swing.client.CoButton;
import com.bluebrim.swing.client.CoListBox;
import com.bluebrim.swing.client.CoPanel;

/**
 * Used to pick an item from a list of possible items
 */
public class CoPickerUI extends CoUserInterface {

	private Object m_chosen;
	private List m_choosables;
	private String m_label;

	private CoPickerUI(List choosables, String label) {
		super();
		m_choosables = choosables;
		m_label = label;
	}

	protected void createListeners() {
		super.createListeners();
		final CoButton tOKButton = (CoButton) getNamedWidget("OK");
		tOKButton.setEnabled(false);

		((CoListValueable) getNamedValueModel("ITEMS")).addSelectionListener(new CoSelectionListener() {
			public void selectionChange(CoSelectionEvent e) {
				tOKButton.setEnabled(hasSelectedItem());
			}
		});

		tOKButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_chosen = getSelectedItem();
				sendCloseDialog(CoDialog.CLOSED_BY_OK);
			}
		});
		((CoButton) getNamedWidget("CANCEL")).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendCloseDialog(CoDialog.CLOSED_BY_CANCEL);
			}
		});
	}

	protected void createValueModels(CoUserInterfaceBuilder builder) {
		super.createValueModels(builder);
		builder.createListBoxAdaptor(builder.addListValueModel(new CoListValueModel("ITEMS", m_choosables)), (CoListBox) getNamedWidget("ITEMS"));
	}

	protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder builder) {
		super.createWidgets(aPanel, builder);
		aPanel.add(builder.createLabel(m_label == null ? "" : m_label), BorderLayout.NORTH);
		aPanel.add(builder.createListBox(new CoCatalogListCellRenderer(), "ITEMS"), BorderLayout.CENTER);

		CoPanel tButtonPanel = builder.createPanel(new GridLayout(1, 2), false, new Insets(4, 4, 4, 4));
		tButtonPanel.add(builder.createButton(CoUIStringResources.getName("CANCEL"), null, "CANCEL"));
		tButtonPanel.add(builder.createButton(CoUIStringResources.getName("OK"), null, "OK"));
		aPanel.add(tButtonPanel, BorderLayout.SOUTH);
	}

	private Object getSelectedItem() {
		return ((CoListBox) getNamedWidget("ITEMS")).getList().getSelectedValue();
	}

	private boolean hasSelectedItem() {
		return getSelectedItem() != null;
	}

	public static Object pickItem(JFrame ownerFrame, List choosables, String label) {
		CoPickerUI ui = new CoPickerUI(choosables, label);
		Rectangle bounds = CoGUI.centerOnScreen(180, 400);
		CoDialog dialog = new CoDialog(ownerFrame, true, ui);
		dialog.setBounds(bounds);
		dialog.show();
		dialog.dispose();
		return ui.getSelectedItem();
	}
}