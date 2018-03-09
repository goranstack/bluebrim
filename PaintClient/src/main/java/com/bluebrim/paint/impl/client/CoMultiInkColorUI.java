package com.bluebrim.paint.impl.client;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.swing.client.*;

//

public abstract class CoMultiInkColorUI extends CoEditableColorUI {
	// UIConstants
	public static final String COLOR = "color";
	public static final String COLORS = "colors";
	public static final String SHADE = "shade";
	public static final String SPOT_COLOR = "spot_color";
	public static final String PROCESS_COLOR = "process_color";
	public static final String MULTI_INK_COLOR = "multi_ink_color";
	public static final String CYAN = "cyan";
	public static final String MAGENTA = "magenta";
	public static final String YELLOW = "yellow";
	public static final String BLACK = "black";

	// Color table
	private CoTable colorTable;
	private CoTableBox colorTableBox;
	private CoTableAspectAdaptor colorTableAdaptor;
	private CoAbstractListAspectAdaptor colorListAdaptor;
	/**
	 * CoMultiInkColorUI constructor comment.
	 */
	public CoMultiInkColorUI() {
		super();
	}
	/**
	 * CoMultiInkColorUI constructor comment.
	 * @param aDomainObject CoObjectIF
	 */
	public CoMultiInkColorUI(CoObjectIF aDomainObject) {
		super(aDomainObject);
	}
	private int calcTableHeight() {
		int height = this.colorTable.getRowCount() * this.colorTable.getRowHeight();
		return (height += this.colorTable.getRowCount() * this.colorTable.getRowMargin());
	}
	protected CoPanel createColorPanel(CoUserInterfaceBuilder aBuilder) {
		this.colorTableBox = aBuilder.createTableBox();
		this.colorTable = this.colorTableBox.getTable();

		CoPanel tablePanel = aBuilder.createPanel(new BorderLayout());
		tablePanel.add(this.colorTableBox, BorderLayout.WEST);

		return tablePanel;
	}
	protected CoLabel createLabel(CoUserInterfaceBuilder aBuilder) {
		CoLabel label =
			aBuilder.createLabel(
				CoColorUIResources.getName(MULTI_INK_COLOR),
				this.colorPreviewIcon,
				JLabel.CENTER,
				PREVIEW);

		label.setFont(CoUIConstants.GARAMOND_18_LIGHT);
		label.setForeground(Color.black);

		return label;
	}
	protected void createListeners() {
		super.createListeners();

		(new CoServerObjectListener(this) {
			protected Object getServerObjectFrom(CoObjectIF domain) {
				return getColorCollection();
			}

			public void changedServerObject(CoChangedObjectEvent e) {
			     colorListAdaptor.listHasChanged(this);
			}

		}).initialize();

		((CoTextField) getNamedWidget(CoConstants.NAME)).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nameHasChanged();
			}
		});

		((CoTextField) getNamedWidget(CoConstants.NAME)).addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				nameHasChanged();
			}
		});
	}
	protected CoTableAspectAdaptor createTableAdaptor(final CoAbstractListAspectAdaptor listAdaptor) {
		String[] headers = new String[] { CoColorUIResources.getName(COLOR), CoColorUIResources.getName(SHADE)};

		return new CoTableAspectAdaptor(this, null, listAdaptor, headers) {
			public Class getColumnClass(int columnIndex) {
				return (columnIndex < 1) ? CoTrappableColorIF.class : String.class;
			}

			protected Object getValueForAt(CoObjectIF rowSubject, int rowIndex, int columnIndex) {
				if (rowSubject == null)
					return null;
				switch (columnIndex) {
					case 0 :
						// Color
						return rowSubject;

					case 1 :
						// Shade
						float shade =
							((CoExtendedMultiInkColorIF) getDomain()).getShade((CoTrappableColorIF) rowSubject);
						StringBuffer buf = new StringBuffer(String.valueOf(shade));
						return buf.append("%").toString();

					default :
						return null;
				}
			}

			protected void setValueForAt(CoObjectIF rowSubject, Object value, int rowIndex, int columnIndex) {
				if (rowSubject == null)
					return;
				if (columnIndex == 1) {
					if (colorProxy == null)
						makeColorProxy();

					if (trim2float((String) value) != 0f)
						((CoExtendedMultiInkColorIF) colorProxy).setShade(
							(CoTrappableColorIF) rowSubject,
							trim2float((String) value));
					else
						 ((CoExtendedMultiInkColorIF) colorProxy).removeShadeColor((CoTrappableColorIF) rowSubject);

					updatePreviewColor();
				}
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return !(columnIndex == 0);
			}

			public void tableHasChanged() {
				fireTableModelEvent(new TableModelEvent(this, 0, Integer.MAX_VALUE, 1));
			}

		};

	}
	protected void createValueModels(CoUserInterfaceBuilder aBuilder) {
		super.createValueModels(aBuilder);

		// List adaptor
		this.colorListAdaptor = new CoAbstractListAspectAdaptor.Default(this, COLORS, true) {
			protected Object get(CoObjectIF subject) {
				return getTrappableColorsList();
			}

		};

		// Table adaptor
		this.colorTableAdaptor = createTableAdaptor(this.colorListAdaptor);
		aBuilder.createTableBoxAdaptor(this.colorTableAdaptor, this.colorTableBox);
	}
	protected void doAfterCreateUserInterface() {
		super.doAfterCreateUserInterface();

		if (getDomain() != null)
			((CoTextField) getNamedWidget(CoConstants.NAME)).setText(
				((CoExtendedMultiInkColorIF) getDomain()).getName());

		initTableBox(this.colorTableBox);
	}
	private int getMaximumVisibleRowsInTable() {
		return 10;
	}

	protected List getTrappableColorsList() {
		CoColorCollectionIF colorCollection = getColorCollection();
		java.util.List trappableColors = new ArrayList();
		if (colorCollection != null) {
			List colors = colorCollection.getColors();
			for (int i = 0; i < colors.size(); i++) {
				CoColorIF color = (CoColorIF) colors.get(i);
				if (color.getType().equals(SPOT_COLOR)
						|| color.getType().equals(PROCESS_COLOR))
					trappableColors.add(color);
			}
		}
		return trappableColors;
	}
	
	private void initTableBox(CoTableBox tableBox) {
		TableColumnModel colModel = this.colorTable.getColumnModel();
		this.colorTable.setVisibleRowCount(getMaximumVisibleRowsInTable());
		this.colorTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		this.colorTableBox.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.colorTable.setRowHeight(20);
		this.colorTable.setAutoCreateColumnsFromModel(false);
		this.colorTable.createDefaultColumnsFromModel();
		this.colorTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Trappable color renderer -----------------------------------------------------------------------
		class colorRenderer extends DefaultTableCellRenderer {
			// Icon
			private CoColorSampleIcon colorPreviewIcon;

			public Component getTableCellRendererComponent(
				JTable table,
				Object value,
				boolean isSelected,
				boolean hasFocus,
				int row,
				int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				CoTrappableColorIF trapColor = (CoTrappableColorIF) value;
				this.colorPreviewIcon = new CoColorSampleIcon(trapColor, Color.black, new Dimension(18, 18));

				setIcon(this.colorPreviewIcon);
				setText(trapColor.getName());
				return this;
			}
		}
		this.colorTable.setDefaultRenderer(CoTrappableColorIF.class, new colorRenderer());

		// Shade renderer ---------------------------------------------------------------------------------
		CoComboBoxTableCellRender shadeRenderer = new CoComboBoxTableCellRender() {
			public Component getTableCellRendererComponent(
				JTable table,
				Object value,
				boolean isSelected,
				boolean hasFocus,
				int row,
				int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				return this;
			}
		};
		this.colorTable.setDefaultRenderer(String.class, shadeRenderer);

		// Shade celleditor --------------------------------------------------------------------------------
		CoComboBox tEditorBox = getUIBuilder().createComboBox(SHADE);
		for (int i = 0; i <= 10; i++) {
			tEditorBox.addItem(i * 10 + "%");
		}
		tEditorBox.setEditable(true);
		tEditorBox.setMaximumRowCount(11);

		DefaultCellEditor tEditor = new DefaultCellEditor(tEditorBox);
		tEditor.setClickCountToStart(1);

		TableColumn tColumn = this.colorTable.getColumnModel().getColumn(1);
		tColumn.setCellEditor(tEditor);
		tColumn.setCellRenderer(new CoComboBoxTableCellRender());
	}

	private void nameHasChanged() {
		if (colorProxy == null)
			makeColorProxy();
		((CoExtendedMultiInkColorIF) this.colorProxy).setName(
			((CoTextField) getNamedWidget(CoConstants.NAME)).getText());
	}

	protected void setDomainFrom(CoObjectIF selectedValue) {
		super.setDomainFrom(selectedValue);
		updateWidgets();
	}

	private float trim2float(String str) {
		String s = str;
		if (str.charAt(str.length() - 1) == '%')
			s = str.substring(0, str.length() - 1);

		try {
			return Float.valueOf(s).floatValue();
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	protected void updatePreviewColor() {
		this.colorPreviewIcon.setColor((CoColorIF) getDomain());
		getNamedWidget(PREVIEW).repaint();
	}

	private void updateTableSize() {
		this.colorTable.setPreferredScrollableViewportSize(new Dimension(300, calcTableHeight()));
	}

	protected void updateWidgets() {
		this.colorTableAdaptor.tableHasChanged();

		((CoTextField) getNamedWidget(CoConstants.NAME)).setText(((CoExtendedMultiInkColorIF) getDomain()).getName());
		updatePreviewColor();
	}

	protected abstract CoColorCollectionIF getColorCollection();
}