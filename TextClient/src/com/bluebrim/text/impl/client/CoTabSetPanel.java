package com.bluebrim.text.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;

// 

public class CoTabSetPanel extends CoPanel {
	public static final String TAB_ADD_STOP = "TAB_ADD_STOP";
	public static final String TAB_DELETE_STOP = "TAB_DELETE_STOP";

	public static final String TAB_ALIGNMENT = "TAB_ALIGNMENT";
	public static final String TAB_ALIGN_LEFT = "TAB_ALIGN_LEFT";
	public static final String TAB_ALIGN_CENTER = "TAB_ALIGN_CENTER";
	public static final String TAB_ALIGN_RIGHT = "TAB_ALIGN_RIGHT";
	public static final String TAB_ALIGN_DECIMAL = "TAB_ALIGN_DECIMAL";
	public static final String TAB_ALIGN_ON = "TAB_ALIGN_ON";
	public static final String TAB_POSITION = "TAB_POSITION";
	public static final String TAB_LEADER = "TAB_LEADER";
	public static final String TAB_LEAD_NONE = "TAB_LEAD_NONE";
	public static final String TAB_LEAD_DOTS = "TAB_LEAD_DOTS";
	public static final String TAB_LEAD_HYPHENS = "TAB_LEAD_HYPHENS";
	public static final String TAB_LEAD_UNDERLINE = "TAB_LEAD_UNDERLINE";
	public static final String TAB_LEAD_THICKLINE = "TAB_LEAD_THICKLINE";
	public static final String TAB_LEAD_EQUALS = "TAB_LEAD_EQUALS";

	public interface TabSetEditor {
		void setTabSet(CoTabSetIF s, boolean isAdjusting);
		void unsetTabSet();
		void setRegularTabStopInterval(float i, boolean isAdjusting);
		void unsetRegularTabStopInterval();
	};

	public static abstract class TextEditorTabSetEditor implements TabSetEditor {
		private MutableAttributeSet m_attributeSet = new com.bluebrim.text.shared.CoSimpleAttributeSet();
		private CoTabSetIF m_dummyTabSet = new CoTabSet();
		private Float m_dummyFloat = new Float(0);

		protected abstract CoAbstractTextEditor getTextEditor();

		public void setTabSet(CoTabSetIF s, boolean isFinal) {
			m_attributeSet.removeAttributes(m_attributeSet);
			CoStyleConstants.setTabSet(m_attributeSet, s);
			getTextEditor().setParagraphAttributes(m_attributeSet, false);
		}

		public void unsetTabSet() {
			m_attributeSet.removeAttributes(m_attributeSet);
			CoStyleConstants.setTabSet(m_attributeSet, m_dummyTabSet);
			getTextEditor().unsetParagraphAttributes(m_attributeSet);
		}

		public void setRegularTabStopInterval(float i, boolean isAdjusting) {
			m_attributeSet.removeAttributes(m_attributeSet);
			CoStyleConstants.setRegularTabStopInterval(m_attributeSet, new Float(i));
			getTextEditor().setParagraphAttributes(m_attributeSet, false);
		}

		public void unsetRegularTabStopInterval() {
			m_attributeSet.removeAttributes(m_attributeSet);
			CoStyleConstants.setRegularTabStopInterval(m_attributeSet, m_dummyFloat);
			getTextEditor().unsetParagraphAttributes(m_attributeSet);
		}
	};

	private TabSetEditor m_editor;

	private CoList m_list;

	private CoOptionMenu m_aligmentOptionMenu;
	private CoTextField m_positionTextField;
	private CoOptionMenu m_leaderOptionMenu;
	private CoTextField m_alignOnTextField;

	// Popupmenu
	private CoPopupMenu m_popupMenu;

	// Actions
	private AbstractAction m_removeAction;
	private AbstractAction m_addAction;

	private CoTabSetIF m_tabSet;
	private CoTabStopIF m_tabStop;

	private class TabSetListModel extends AbstractListModel {
		public int getSize() {
			return (m_tabSet == null) ? 0 : m_tabSet.getTabStopCount();
		}

		public Object getElementAt(int i) {
			return (m_tabSet == null) ? null : m_tabSet.getTabStop(i);
		}

		public void touch() {
			fireContentsChanged(this, 0, getSize() - 1);
		}
	};

	TabSetListModel m_tabSetListModel = new TabSetListModel();

	public static final String[] ALIGNMENT_VALUES =
		new String[] {
			CoTextStringResources.getName(TAB_ALIGN_LEFT),
			CoTextStringResources.getName(TAB_ALIGN_CENTER),
			CoTextStringResources.getName(TAB_ALIGN_RIGHT),
			CoTextStringResources.getName(TAB_ALIGN_DECIMAL),
			CoTextStringResources.getName(TAB_ALIGN_ON),
			};

	public static final String[] LEADER_VALUES =
		new String[] {
			CoTextStringResources.getName(TAB_LEAD_NONE),
			CoTextStringResources.getName(TAB_LEAD_DOTS),
			CoTextStringResources.getName(TAB_LEAD_HYPHENS),
			CoTextStringResources.getName(TAB_LEAD_UNDERLINE),
			CoTextStringResources.getName(TAB_LEAD_THICKLINE),
			CoTextStringResources.getName(TAB_LEAD_EQUALS)};

	public CoTabSetPanel(CoUserInterfaceBuilder b, CoMenuBuilder mb, TabSetEditor editor) {
		super(new BorderLayout());

		m_editor = editor;

		{
			CoListBox l = b.createListBox(new CoListCellRenderer() {
				public Component getListCellRendererComponent(
					JList list,
					Object value,
					int index,
					boolean isSelected,
					boolean cellHasFocus) {
					if (value != null) {
						CoTabStopIF t = (CoTabStopIF) value;
						value =
							CoLengthUnitSet.format(t.getPosition(), CoLengthUnit.LENGTH_UNITS)
								+ "  , "
								+ ALIGNMENT_VALUES[t.getAlignment()]
								+ ", "
								+ LEADER_VALUES[t.getLeader()];
					}
					return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}
			});
			m_list = l.getList();
			m_list.setModel(m_tabSetListModel);
			add(l, BorderLayout.CENTER);
		}

		{
			CoPanel p = b.createPanel(new CoFormLayout());
			add(p, BorderLayout.EAST);

			p.add(b.createLabel(CoTextStringResources.getName(TAB_ALIGNMENT)));
			m_aligmentOptionMenu = b.createOptionMenu();

			p.add(m_aligmentOptionMenu);
			for (int i = 0; i < ALIGNMENT_VALUES.length; i++) {
				m_aligmentOptionMenu.addItem(ALIGNMENT_VALUES[i]);
			}

			p.add(b.createLabel(CoTextStringResources.getName(TAB_POSITION)));
			m_positionTextField = new CoNumericTextField(CoLengthUnit.LENGTH_UNITS);
			b.prepareTextField(m_positionTextField);
			m_positionTextField.setHorizontalAlignment(SwingConstants.RIGHT);
			m_positionTextField.setBorder(BorderFactory.createEtchedBorder());
			p.add(m_positionTextField);

			p.add(b.createLabel(CoTextStringResources.getName(TAB_LEADER)));
			m_leaderOptionMenu = b.createOptionMenu();
			p.add(m_leaderOptionMenu);
			for (int i = 0; i < LEADER_VALUES.length; i++) {
				m_leaderOptionMenu.addItem(LEADER_VALUES[i]);
			}

			p.add(b.createLabel(CoTextStringResources.getName(TAB_ALIGN_ON)));
			p.add(m_alignOnTextField = b.createSlimTextField());

			updateTabStopPanel();
		}

		// listeners
		m_list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				doHandleSelection();
			}
		});

		m_aligmentOptionMenu.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ev) {
				if (m_tabSet == null)
					return;
				int i = m_tabSet.getIndexOfTabStop(m_tabStop);
				m_tabSet = m_tabSet.copy();
				m_tabStop = m_tabSet.getTabStop(i);
				m_tabStop.setAlignment(m_aligmentOptionMenu.getSelectedIndex());
				m_editor.setTabSet(m_tabSet, false);
			}
		});

		m_aligmentOptionMenu.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if (ev.getStateChange() == ItemEvent.SELECTED)
					updateAlignOnTextfield();
			}
		});

		m_positionTextField.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ev) {
				if (m_tabSet == null)
					return;

				Number n = null;
				try {
					n = NumberFormat.getInstance(Locale.getDefault()).parse(m_positionTextField.getText());
				} catch (ParseException ex) {
					return;
				}

				int i = m_tabSet.getIndexOfTabStop(m_tabStop);
				m_tabSet = m_tabSet.copy();
				m_tabStop = m_tabSet.getTabStop(i);
				m_tabStop.setPosition(n.floatValue());
				m_editor.setTabSet(m_tabSet, false);
			}
		});

		m_leaderOptionMenu.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ev) {
				if (m_tabSet == null)
					return;
				int i = m_tabSet.getIndexOfTabStop(m_tabStop);
				m_tabSet = m_tabSet.copy();
				m_tabStop = m_tabSet.getTabStop(i);
				m_tabStop.setLeader(m_leaderOptionMenu.getSelectedIndex());
				m_editor.setTabSet(m_tabSet, false);
			}
		});

		m_alignOnTextField.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ev) {
				if (m_tabSet == null)
					return;
				int i = m_tabSet.getIndexOfTabStop(m_tabStop);
				m_tabSet = m_tabSet.copy();
				m_tabStop = m_tabSet.getTabStop(i);
				m_tabStop.setAlignOn(m_alignOnTextField.getText());
				m_editor.setTabSet(m_tabSet, false);
			}
		});

		// popup menu
		m_addAction = new AbstractAction(CoTextStringResources.getName(TAB_ADD_STOP)) {
			public void actionPerformed(ActionEvent ev) {
				m_tabSet = ((m_tabSet == null) ? new CoTabSet() : m_tabSet.copy());
				CoTabStopIF s = m_tabSet.addTabStop();
				m_editor.setTabSet(m_tabSet, false);
				m_tabSetListModel.touch();
				m_list.setSelectedValue(s, true);
			}
		};
		m_addAction.setEnabled(false);

		m_removeAction = new AbstractAction(CoTextStringResources.getName(TAB_DELETE_STOP)) {
			public void actionPerformed(ActionEvent ev) {
				if (m_tabSet == null)
					return;
				m_tabSet = m_tabSet.copy();

				Object[] t = m_list.getSelectedValues();
				m_list.clearSelection();
				for (int i = 0; i < t.length; i++) {
					m_tabSet.removeTabStop((CoTabStopIF) t[i]);
				}

				if (m_tabSet.getTabStopCount() == 0) {
					m_tabSet = null;
					m_editor.unsetTabSet();
				} else {
					m_editor.setTabSet(m_tabSet, false);
				}
				m_tabSetListModel.touch();
			}
		};
		m_removeAction.setEnabled(false);

		m_popupMenu = mb.createPopupMenu(m_list);
		mb.addPopupMenuItem(m_popupMenu, m_addAction);
		mb.addPopupMenuItem(m_popupMenu, m_removeAction);

		m_list.addMouseListener(new CoPopupGestureListener(m_popupMenu));

	}

	private void doHandleSelection() {
		Object[] o = m_list.getSelectedValues();
		if (o.length == 1) {
			setTabStop((CoTabStopIF) o[0]);
		} else {
			setTabStop(null);
		}

		enableDisableMenus(true);
	}

	private void enableDisableMenus(boolean state) {
		boolean tState = state && isEnabled();
		m_addAction.setEnabled(true);
		int c = m_list.getSelectedValues().length;
		m_removeAction.setEnabled(tState && c > 0);
	}

	public CoTabSetIF getTabSet() {
		return m_tabSet;
	}

	public void setTabSet(CoTabSetIF s) {
		if (m_tabSet != s)
			m_list.clearSelection();
		m_tabSet = s;
		enableDisableMenus(m_tabSet != null);
		m_tabSetListModel.touch();

		updateAlignOnTextfield();
	}

	private void setTabStop(CoTabStopIF s) {
		m_tabStop = s;

		boolean b = (m_tabStop != null);
		m_aligmentOptionMenu.setEnabled(b);
		m_positionTextField.setEnabled(b);
		m_leaderOptionMenu.setEnabled(b);

		updateAlignOnTextfield();

		updateTabStopPanel();
	}

	private void updateAlignOnTextfield() {
		boolean b = (m_tabStop != null) && (m_tabStop.getAlignment() == CoTabStopIF.ALIGN_ON);

		m_alignOnTextField.setEnabled(b);
		m_alignOnTextField.setEditable(b);
	}

	private void updateTabStopPanel() {
		if (m_tabStop == null) {
			m_aligmentOptionMenu.setSelectedItem(null, true);
			m_positionTextField.setText("");
			m_leaderOptionMenu.setSelectedItem(null, true);
			m_alignOnTextField.setText("");
		} else {
			m_aligmentOptionMenu.setSelectedIndex(m_tabStop.getAlignment(), true);
			m_positionTextField.setText(NumberFormat.getInstance(Locale.getDefault()).format(m_tabStop.getPosition()));
			m_leaderOptionMenu.setSelectedIndex(m_tabStop.getLeader(), true);
			m_alignOnTextField.setText(new String(m_tabStop.alignOn()));
		}

	}
}