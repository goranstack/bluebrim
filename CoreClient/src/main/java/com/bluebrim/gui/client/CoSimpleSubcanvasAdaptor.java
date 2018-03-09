package com.bluebrim.gui.client;

import java.awt.Component;

/**
 	En CoSubcanvasAdaptor knyter ihop en CoValueable som representerar ett verksamhetsobjekt 
 	med den CoSubcanvas som skall visa upp detta.
 	Om verksamhetsobjekt saknas så används värdet null istället.
 	Instansieras via CoUserInterfaceBuilder.createSimpleSubcanvasAdaptor
	@see CoUserInterfaceBuilder#createSimpleSubcanvasAdaptor
	@author Dennis Malmström, 2000-09-06
 */
public class CoSimpleSubcanvasAdaptor extends CoComponentAdaptor implements CoValueListener
{
	CoValueable valueModel;
	CoSubcanvas subcanvas;
public CoSimpleSubcanvasAdaptor( CoSubcanvas aSubcanvas )
{
	this( null, aSubcanvas );
}
public CoSimpleSubcanvasAdaptor (CoValueable aValueable, CoSubcanvas aSubcanvas )
{
	setValueModel(aValueable);
	setSubcanvas(aSubcanvas);
}
/**
 * enableDisable method comment.
 */
public void enableDisable(CoEnableDisableEvent e) {
	CoUserInterface tUserInterface = getUI();
	if (tUserInterface != null)
		tUserInterface.setEnabled(e.enable());	
}
protected Component getComponent() {
	return getSubcanvas();
}
/**
 */
public CoSubcanvas getSubcanvas() {
	return subcanvas;
}
/**
 */
protected CoUserInterface getUI()
{
	return getSubcanvas().getUserInterface();
}
/**
 */
public CoValueable getValueModel() {
	return valueModel;
}
/**
 */
public void setSubcanvas(CoSubcanvas aSubcanvas) {
	subcanvas = aSubcanvas;
	subcanvas.setEnabled( ( valueModel == null ) ? true : valueModel.isEnabled() && valueModel.getValue() != null );

}
/**
 */
public void setValueModel(CoValueable aValueModel) {
	if (aValueModel != valueModel)
	{
		if (valueModel != null)
			valueModel.removeValueListener(this);
		valueModel = aValueModel;
		if (valueModel != null)
			valueModel.addValueListener(this);
	}
}
public void userInterfaceActivated(CoUserInterfaceEvent e)
{
	CoUserInterface tUserInterface = getUI();
	if (tUserInterface != null)
		tUserInterface.processUserInterfaceEvent(e);
}
public void userInterfaceClosed(CoUserInterfaceEvent e) 
{
	CoUserInterface tUserInterface = getUI();
	if (tUserInterface != null)
		tUserInterface.processUserInterfaceEvent(e);
}
public void userInterfaceClosing(CoUserInterfaceEvent e) 
{
	CoUserInterface tUserInterface = getUI();
	if (tUserInterface != null)
		tUserInterface.processUserInterfaceEvent(e);
}
public void userInterfaceDeactivated(CoUserInterfaceEvent e)
{
	CoUserInterface tUserInterface = getUI();
	if (tUserInterface != null)
		tUserInterface.processUserInterfaceEvent(e);
}
public void userInterfaceOpened(CoUserInterfaceEvent e) 
{
	CoUserInterface tUserInterface = getUI();
	if (tUserInterface != null)
		tUserInterface.processUserInterfaceEvent(e);
}
public void userInterfaceValidate(CoUserInterfaceEvent e) 
{
	CoUserInterface tUserInterface = getUI();
	if (tUserInterface != null)
		tUserInterface.processUserInterfaceEvent(e);
}
/**
 */
public void valueChange(CoValueChangeEvent anEvent) {
}
}