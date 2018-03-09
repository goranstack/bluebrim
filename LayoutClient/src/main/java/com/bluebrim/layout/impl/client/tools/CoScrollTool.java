package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.editor.*;

/**
 * Implementation of "scroll mode"
 * 
 * @author: Dennis Malmström
 */

public class CoScrollTool extends CoAbstractTool {
	protected int m_x;
	protected int m_y;

	public CoScrollTool(CoLayoutEditor pageItemEditor) {
		super(null, pageItemEditor);
	}

	public void activate(Point2D pos) {
		super.activate(pos);

		m_viewPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public CoTool mouseDragged(MouseEvent e) {
		int x = e.getX() + m_viewPanel.getX();
		int y = e.getY() + m_viewPanel.getY();

		int dx = m_x - x;
		int dy = m_y - y;

		m_x = x;
		m_y = y;

		JScrollBar hsb = ((JScrollPane) m_viewPanel.getParent().getParent()).getHorizontalScrollBar();
		JScrollBar vsb = ((JScrollPane) m_viewPanel.getParent().getParent()).getVerticalScrollBar();

		hsb.setValue(hsb.getValue() + dx);
		vsb.setValue(vsb.getValue() + dy);

		return this;
	}

	public CoTool mousePressed(MouseEvent e) {
		m_x = e.getX() + m_viewPanel.getX();
		m_y = e.getY() + m_viewPanel.getY();

		return this;
	}

	public CoTool mouseReleased(MouseEvent e) {
		return this;
	}

	public String getName() {
		return CoPageItemUIStringResources.getName("SCROLLHAND_TOOL");
	}

}