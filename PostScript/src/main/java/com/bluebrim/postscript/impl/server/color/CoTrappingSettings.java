package com.bluebrim.postscript.impl.server.color;

import java.io.*;
import java.util.*;

/**
 * Global trapping settings for a Postscript document. This trapping settings do not change in a single
 * Postscript file. This is due both to the Postscript trapping system, and to design choices in the
 * Calvin trapping implementation. Compare this with the trap style, which is possible to change for each
 * trap zone.
 *
 * <p><b>Creation date:</b> 2001-08-03
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoTrapStyle
 */
public class CoTrappingSettings {
	// Generic trapping parameters
	private double m_trapWidth;				// range: >= 0.0, unit: points

	// Black color trapping parameters
	private double m_blackColorLimit;		// range: 0.0 - 1.0
	private double m_blackDensityLimit;		// range: >= 0.0
	private double m_blackWidth;			// range: >= 0.0, unit: multiples of trapWidth

	// Image trapping parameters
	private int	m_imageResolution;

	// Default parameters if not overridden
	private CoColorant m_defaultColorant;
	private CoTrapStyle m_defaultTrapStyle;
/**
 * CoTrapStyle constructor comment.
 */
public CoTrappingSettings() {
	super();
}

public double getBlackColorLimit() {
	return m_blackColorLimit;
}


public double getBlackDensityLimit() {
	return m_blackDensityLimit;
}


public double getBlackWidth() {
	return m_blackWidth;
}


public CoColorant getDefaultColorant() {
	return m_defaultColorant;
}


public CoTrapStyle getDefaultTrapStyle() {
	return m_defaultTrapStyle;
}


public int getImageResolution() {
	return m_imageResolution;
}


public double getTrapWidth() {
	return m_trapWidth;
}


public void setBlackColorLimit(double blackColorLimit) {
	com.bluebrim.base.shared.debug.CoAssertion.preCondition((blackColorLimit >= 0.0) && (blackColorLimit <= 1.0), "Value out of range");
	m_blackColorLimit = blackColorLimit;
}


public void setBlackDensityLimit(double blackDensityLimit) {
	com.bluebrim.base.shared.debug.CoAssertion.preCondition((blackDensityLimit >= 0.0), "Value out of range");
	m_blackDensityLimit = blackDensityLimit;
}


public void setBlackWidth(double blackWidth) {
	com.bluebrim.base.shared.debug.CoAssertion.preCondition((blackWidth >= 0.0), "Value out of range");
	m_blackWidth = blackWidth;
}


public void setDefaultColorant(CoColorant defaultColorant) {
	m_defaultColorant = defaultColorant;
}


public void setDefaultTrapStyle(CoTrapStyle defaultTrapStyle) {
	m_defaultTrapStyle = defaultTrapStyle;
}


public void setImageResolution(int imageResolution) {
	m_imageResolution = imageResolution;
}


public void setTrapWidth(double trapWidth) {
	com.bluebrim.base.shared.debug.CoAssertion.preCondition((trapWidth >= 0.0), "Value out of range");
	m_trapWidth = trapWidth;
}


public void generatePostscript(PrintWriter writer, CoColorantSet colorants) {
	// *** Trapping page device parameters
	
	writer.println("<<");
	writer.println("/Trapping true");
	writer.println("/TrappingDetails <<");
	writer.println(" /Type 1001");

	// Trapping Order	
	String trappingOrder = new String();
	Iterator i = colorants.getTrappingOrder().iterator();
	while (i.hasNext()) {
		trappingOrder += " /" + ((CoColorant) i.next()).getPostscriptName();
	}	
	writer.println(" /TrappingOrder [" + trappingOrder + " ]");

	// Colorant details
	writer.println(" /ColorantDetails <<");
	i = colorants.getColorants().iterator();
	while (i.hasNext()) {
		CoColorant colorant = (CoColorant) i.next();
		writer.println("  /" + colorant.getPostscriptName() + " <<");
		writer.println("   /ColorantName (" + colorant.getName() + ")");		// User defined name, optional
		writer.println("   /ColorantType /" + colorant.getTrappingType().getPostscriptName());
		writer.println("   /NeutralDensity " + colorant.getNeutralDensity() + " >>");
	}
	writer.println(" >>");
	writer.println(">> setpagedevice");

	// *** Trapping parameter dictionary

	writer.println("<<");
	writer.println("/TrapWidth " + m_trapWidth);
	writer.println("/BlackWidth " + m_blackWidth);
	writer.println("/BlackColorLimit " + m_blackColorLimit);
	writer.println("/BlackDensityLimit " + m_blackDensityLimit);
	writer.println("/ImageResolution " + m_imageResolution);

	// Colorant zone details
	writer.println("/ColorantZoneDetails <<");
	i = colorants.getColorants().iterator();
	while (i.hasNext()) {
		CoColorant colorant = (CoColorant) i.next();
		writer.println(" /" + colorant.getPostscriptName() + " <<");
		writer.println("  /StepLimit " + colorant.getTrappingStepLimit());
		writer.println("  /TrapColorScaling " + colorant.getTrappingColorScaling() + " >>");
	}
	writer.println(">>");
	writer.println(">> /Trapping /ProcSet findresource /settrapparams get exec");
}
}