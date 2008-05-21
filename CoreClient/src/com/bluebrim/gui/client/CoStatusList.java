package com.bluebrim.gui.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import com.bluebrim.resource.shared.CoResourceLoader;

/**
 * @author Ali Abida 2000-11-10.
 */
public class CoStatusList extends JPanel {
	// The current position
	private int m_pos = 0;
	
	// The list.
	private final JList m_list = new JList();

	// An empty object array to be used when clearing the list data.
	private final static Object[] emptyData = new Object[]{};

	// Button that displays th previos element in the list.
	private final JButton m_upButton = new JButton(
		CoResourceLoader.loadIcon(getClass(), "up.gif"));

	// Button that displays th next element in the list.
	private final JButton m_downButton = new JButton(
		CoResourceLoader.loadIcon(getClass(), "down.gif"));

	// Inner class used as default renderer.
	public static class DefaultRenderer extends CoListCellRenderer {
		public DefaultRenderer() {
			super();
			setOpaque(false);
		}
		protected void setColorAndFont(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			setForeground(list.getForeground());
			setFont(list.getFont());
		}
	}
public CoStatusList() {
	this(emptyData, new DefaultRenderer());
}
public CoStatusList(Object[] initialElements) {
	this(initialElements, new DefaultRenderer());
}
public CoStatusList(Object[] initialElements, ListCellRenderer renderer) {
	super(new BorderLayout());
	
	initialize();
	_setListDataRenderer(renderer);
	_setListData(initialElements);
}
public CoStatusList(List initialElements) {
	this(initialElements != null ? initialElements.toArray() : emptyData, new DefaultRenderer());
}
public CoStatusList(List initialElements, ListCellRenderer renderer) {
	this(initialElements != null ? initialElements.toArray() : emptyData, renderer);
}
public CoStatusList(ListCellRenderer renderer) {
	this(emptyData, renderer);
}
private void _setListData(Object[] listData) {
	m_list.setListData(listData != null ? listData : emptyData);
	m_downButton.setEnabled(m_list.getModel().getSize() > 1);	
	m_upButton.setEnabled(false);
	m_pos = 0;
	m_list.ensureIndexIsVisible(0);
}
private void _setListDataRenderer(ListCellRenderer renderer) {
	m_list.setCellRenderer(renderer);
}
private void displayNext() {
	int lastIndex = m_list.getModel().getSize() - 1;
	int nextIndex = m_pos + 1;

	if (nextIndex <= lastIndex) {
		// Disable the down button.
		if (nextIndex == lastIndex) {
			m_downButton.setEnabled(false);
		}

		// Enable the up button.
		if (nextIndex == 1) {
			m_upButton.setEnabled(true);
		}
		
		// Display the next element.	
		m_list.ensureIndexIsVisible(nextIndex);
		m_pos = nextIndex;
	}
}
private void displayPrevious() {
	int lastIndex = m_list.getModel().getSize() - 1;
	int nextIndex = m_pos - 1;

	if (nextIndex >= 0) {
		// Disable the up button.
		if (nextIndex == 0) {
			m_upButton.setEnabled(false);
		}

		// Enable the down button.
		if (nextIndex == lastIndex - 1) {
			m_downButton.setEnabled(true);
		}
		
		// Display the next element.	
		m_list.ensureIndexIsVisible(nextIndex);
		m_pos = nextIndex;
	}
	
}
private void initialize() {
	// Make ourselves transparent
	setOpaque(false);

	// Make a lowered bevel border.
	setBorder(BorderFactory.createCompoundBorder(
		BorderFactory.createLoweredBevelBorder(),
			BorderFactory.createEmptyBorder(1,1,1,2)));

	// Set some list and scroll pane properties, and add it to the north.
	m_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	m_list.setVisibleRowCount(1);
	m_list.setOpaque(false);
	m_list.setBorder(null);
	m_list.setAlignmentY(JComponent.CENTER_ALIGNMENT);
	JScrollPane scroller = new JScrollPane(m_list);
	scroller.setOpaque(false);
	scroller.setBorder(null);
	scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
	scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	
	add(scroller, BorderLayout.CENTER);

	// The buttons
	// Move to the next element.
	m_downButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			displayNext();
		}
	});
	m_downButton.setOpaque(false);
	m_downButton.setBorder(null);
	m_downButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
	m_downButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);

	// Move to the previous element.
	m_upButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			displayPrevious();
		}
	});
	m_upButton.setOpaque(false);
	m_upButton.setBorder(null);
	m_upButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
	m_upButton.setAlignmentX(JComponent.RIGHT_ALIGNMENT);

	// Put the buttons in a box layout.	
	JPanel xPanel = new JPanel();
	xPanel.setOpaque(false);
	xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.X_AXIS));
	xPanel.add(m_downButton);
	xPanel.add(m_upButton);
	add(xPanel, BorderLayout.EAST);	
}
public void setListData(Object[] listData) {
	_setListData(listData);
}
public void setListDataFrom(List listData) {
	_setListData(listData != null ? listData.toArray() : emptyData);
}
public void setListDataRenderer(CoListCellRenderer renderer) {
	_setListDataRenderer(renderer);
}
}