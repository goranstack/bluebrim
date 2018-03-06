package com.bluebrim.postscript.impl.server.color;
import java.util.*;

/**
 * Representation of sets of colorants with some special ordering. A colorant set includes, in addition
 * to information about the included colorants, also a trapping order (representing the colorants in
 * the order the trapping is to be performed) and a separation order (representing the colorants in the
 * order they are going to be printed on the paper).
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoColorantSet {
	private Set m_colorants = new HashSet();	// [CoColorant]
	private LinkedList m_separationOrder = new LinkedList();	// [CoColorant]
	private LinkedList m_trappingOrder = new LinkedList();		// [CoColorant]
public CoColorantSet() {
	super();
}

public Set getColorants() {
	return m_colorants;
}


public List getSeparationOrder() {
	return m_separationOrder;
}


public List getTrappingOrder() {
	return m_trappingOrder;
}


public void setColorants(Set colorants) {
	m_colorants = colorants;
}


public void setSeparationOrder(List separationOrder) {
	if (separationOrder instanceof LinkedList) {
		m_separationOrder = (LinkedList) separationOrder;
	} else {
		m_separationOrder = new LinkedList(separationOrder);
	}
}


public void setTrappingOrder(List trappingOrder) {
	if (trappingOrder instanceof LinkedList) {
		m_trappingOrder = (LinkedList) trappingOrder;
	} else {
		m_trappingOrder = new LinkedList(trappingOrder);
	}
}


public void addColorant(CoColorant colorant) {
	if (!m_colorants.contains(colorant)) {
		m_colorants.add(colorant);
		m_separationOrder.add(colorant);
		m_trappingOrder.add(colorant);
	}
}


public void addAllColorants(CoColorantSet colorants) {
	Iterator i = colorants.getColorants().iterator();
	while (i.hasNext()) {
		CoColorant colorant = (CoColorant) i.next();
		addColorant(colorant);
	}
}


public void removeColorant(CoColorant colorant) {
	m_colorants.remove(colorant);
	m_separationOrder.remove(colorant);
	m_trappingOrder.remove(colorant);
}


public void separateFirst(CoColorant colorant) {
	if (!m_colorants.contains(colorant)) return;
	m_separationOrder.remove(colorant);
	m_separationOrder.addFirst(colorant);
}


public void separateLast(CoColorant colorant) {
	if (!m_colorants.contains(colorant)) return;
	m_separationOrder.remove(colorant);
	m_separationOrder.add(colorant);
}


public void trapFirst(CoColorant colorant) {
	if (!m_colorants.contains(colorant)) return;
	m_trappingOrder.remove(colorant);
	m_trappingOrder.addFirst(colorant);
}


public void trapLast(CoColorant colorant) {
	if (!m_colorants.contains(colorant)) return;
	m_trappingOrder.remove(colorant);
	m_trappingOrder.add(colorant);
}
}