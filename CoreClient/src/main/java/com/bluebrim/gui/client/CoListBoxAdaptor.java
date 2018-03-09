package com.bluebrim.gui.client;

import java.awt.Component;

import javax.swing.JList;

import com.bluebrim.swing.client.CoListBox;
/**
 * En CoListBoxAdaptor knyter ihop en CoValueModel som innehåller en lista
 * med en CoListBox som skall visa upp listan.
 * @see CoUserInterfaceBuilder#createListBoxAdaptor
 * @author Lasse Svadängs 97-09-30
 *
 */
public class CoListBoxAdaptor extends CoComponentAdaptor implements CoValueListener {
	CoListBox listBox;
	CoListValueable listValueable;
/**
 * @param aValueable SE.corren.calvin.userinterface.CoListValueable
 * @param aListBox SE.corren.calvin.userinterface.CoListBox
 */
public CoListBoxAdaptor (CoListValueable aValueable, CoListBox aListBox) {
	setListValueable(aValueable);
	setListBox(aListBox);
	aValueable.addValueListener(this);
	aValueable.setModelFor(listBox.getList());
	listBox.getList().getSelectionModel().addListSelectionListener(aValueable);
	updateListBox();
}
public void enableDisable(CoEnableDisableEvent e)
{
	listBox.getList().setEnabled(e.enable);
	super.enableDisable( e );
}
protected Component getComponent ( ) {
	return listBox;
}
/**
 * getElementAt method comment.
 */
public Object getElementAt(int arg1) {
	return getListValueable().getElementAt(arg1);
}
/**
 * @param aValueable SE.corren.calvin.userinterface.CoListBox
 */
public CoListBox getListBox ( ) {
	return listBox;
}
/**
 * @param aValueable SE.corren.calvin.userinterface.CoListValueable
 */
public CoListValueable getListValueable () {
	return listValueable;
}
/**
 * @param aValueable SE.corren.calvin.userinterface.CoListBox
 */
protected void setListBox ( CoListBox aListBox) {
	listBox = aListBox;
	listBox.setEnabled( listValueable.isEnabled() );
}
/**
 * @param aValueable SE.corren.calvin.userinterface.CoListValueable
 */
protected void setListValueable ( CoListValueable aValueable) {
	listValueable = aValueable;
}
/**
 * valueModelChange method comment.
 */
public void updateListBox() {
	JList	tList			= getListBox().getList();
	try
	{
		tList.getSelectedValue();
	}
	catch (ArrayIndexOutOfBoundsException e) 
	{
		tList.clearSelection();
	}
	catch (IndexOutOfBoundsException e) 
	{
		tList.clearSelection();
	}
	getListValueable().listHasChanged(this);	
}
/**
 */
public void valueChange(CoValueChangeEvent anEvent) {
	updateListBox();
}
}
