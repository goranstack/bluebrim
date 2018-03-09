package com.bluebrim.swing.client;

import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Interface enabling the column model to create columns
 * from a TableModel as it pleases. CoTable dispatches
 * to the column model from createDefaultColumnsFromModel(),
 * if it implements this interface.
 *
 * @author Markus Persson 2000-03-15
 */
public interface CoTableColumnModel extends TableColumnModel {
public void createColumnsFrom(TableModel tableModel);
}
