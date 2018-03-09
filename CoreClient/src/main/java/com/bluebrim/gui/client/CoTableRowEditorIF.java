package com.bluebrim.gui.client;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoPopupMenu;
/**
 * This type was created in VisualAge.
 */
public interface CoTableRowEditorIF {
public Class getColumnClass(int columnIndex);
public CoAccessor getListAccessor();
public CoAbstractListModel getListModelFor(CoAbstractListAspectAdaptor aspectAdaptor);
public Object getValueForAt(CoObjectIF rowSubject, int rowIndex,int columnIndex);
public boolean isCellEditable(int rowIndex, int columnIndex);
public void populateAndEnableTableMenu(CoPopupMenu tableMenu, CoMenuBuilder builder);
public void setValueForAt(CoObjectIF rowSubject, Object aValue, int rowIndex,int columnIndex);
public void startEdit();
public void stopEdit();
}
