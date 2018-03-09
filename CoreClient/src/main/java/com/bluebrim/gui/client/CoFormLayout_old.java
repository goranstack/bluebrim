package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager2;
/**
	Enkel blankettlayout efter en idé av Scott Stanchfield som lägger ut komponenterna 
	i två kolumner och där den plats den första kolumnen tar styrs av bredden hos 
	den längsta av komponenterna.<br>
	En CoFormLayout kan instansieras med två int som anger det horisontella resp vertikala avståndet 
	mellan komponenterna. Defaultvärden för dessa två storheter är 2.
*/
public class CoFormLayout_old implements LayoutManager2 {
  private GridBagLayout 			gbl 	= new GridBagLayout();
  private GridBagConstraints 	gbc 	= new GridBagConstraints();
  private int							hGap	= 2;
  private int							vGap	= 2;
  public CoFormLayout_old() {
		gbc.gridwidth 	= gbc.gridheight = 1;
		gbc.gridx 		= gbc.gridy = 0;
		gbc.fill 			= GridBagConstraints.HORIZONTAL;
		gbc.anchor 		= GridBagConstraints.CENTER;
		gbc.weightx 		= gbc.weighty = 0.0;
		setGap(0,0);
  }  
  public CoFormLayout_old(int hGap, int vGap) {
  		this();
  		setGap(hGap,vGap);
  }  
  public void addLayoutComponent(Component comp, Object constraints) {
	setConstraints(comp);
	gbl.addLayoutComponent(comp, constraints);
  }  
  public void addLayoutComponent(String name, java.awt.Component comp) {
	setConstraints(comp);
	gbl.addLayoutComponent(name, comp);
  }  
  public Dimension getGap() {
		return new Dimension(hGap,vGap);
  }  
  public float getLayoutAlignmentX(Container target) {
	return gbl.getLayoutAlignmentX(target);
  }  
  public float getLayoutAlignmentY(Container target) {
	return gbl.getLayoutAlignmentY(target);
  }  
  public void invalidateLayout(Container target) {
	gbl.invalidateLayout(target);
  }  
  public void layoutContainer(Container parent) {
	gbl.layoutContainer(parent);
  }  
/**
	Genom att inte tillåta en större höjd än preferredSizes så
	håller sig komponenterna på sin plats uppe i högra hörnet.
*/
public java.awt.Dimension maximumLayoutSize(Container target) {
  	Dimension tMaximumSize 	= gbl.maximumLayoutSize(target);
  	Dimension tPrefSize 		= preferredLayoutSize(target);
  	tMaximumSize.height 		= tPrefSize.height;
	return tMaximumSize;
}
  public java.awt.Dimension minimumLayoutSize(Container parent) {
	return gbl.minimumLayoutSize(parent);
  }  
  public java.awt.Dimension preferredLayoutSize(Container parent) {
	return gbl.preferredLayoutSize(parent);
  }  
  public void removeLayoutComponent(Component comp) {
	gbl.removeLayoutComponent(comp);
  }  
private void setConstraints(Component comp) 
{
	gbc.weightx = (gbc.gridx == 0)?0.0:1.0;
	gbl.setConstraints(comp, gbc);
	if (gbc.gridx == 1) 
	{
	  gbc.gridx 		= 0;
	  gbc.gridy++;
	  gbc.insets 	= new Insets(0,0,vGap,0);
	}
	else
	{
	  gbc.gridx++;
	  gbc.insets 	= new Insets(0,hGap,vGap,0);
	} 
}
  public void setGap(int hGap, int vGap) {
		this.hGap 	= hGap;
		this.vGap		= vGap;
		int tHGap		= 0;
		int tVGap		= 0;
		if (gbc.gridx > 0)
			tHGap = hGap;
		if (gbc.gridy > 0)
			tVGap = vGap;
		gbc.insets 	= new Insets(0,tHGap,tVGap,0);
  }  
  public void setGap(Dimension gap) {
		setGap(gap.width,gap.height);
  }  
}
