package com.bluebrim.layout.impl.client.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.tools.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.stroke.shared.*;

/**
 * "Secret" dialog for controlling debugging behavior for page items and layout editor
 * 
 * @author Dennis Malmström
 */
public class CoViewTestUI extends JFrame {
	public static CoViewTestUI m_instance;

	private CoViewTestUI() {
		super("View test state");
		CoGUI.brand(this);

		Container p = getContentPane();

		p.setLayout(new CoColumnLayout());

		{
			JCheckBox cb = new JCheckBox("Paint page item id");
			cb.setSelected(CoPageItemViewRenderer.DO_PAINT_PI);
			cb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ev) {
					CoPageItemViewRenderer.DO_PAINT_PI = ((JCheckBox) ev.getSource()).isSelected();
				}
			});
			p.add(cb);
		}

		{
			JCheckBox cb = new JCheckBox("Paint view id");
			cb.setSelected(CoPageItemViewRenderer.DO_PAINT_PIV);
			cb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ev) {
					CoPageItemViewRenderer.DO_PAINT_PIV = ((JCheckBox) ev.getSource()).isSelected();
				}
			});
			p.add(cb);
		}

		{
			JCheckBox cb = new JCheckBox("Paint geometry");
			cb.setSelected(CoPageItemViewRenderer.DO_PAINT_GEOMETRY);
			cb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ev) {
					CoPageItemViewRenderer.DO_PAINT_GEOMETRY = ((JCheckBox) ev.getSource()).isSelected();
				}
			});
			p.add(cb);
		}

		{
			JCheckBox cb = new JCheckBox("Optimized paint");
			cb.setSelected(CoPageItemViewRenderer.DO_OPTIMIZE_PAINT);
			cb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ev) {
					CoPageItemViewRenderer.DO_OPTIMIZE_PAINT = ((JCheckBox) ev.getSource()).isSelected();
				}
			});
			p.add(cb);
		}

		{
			JCheckBox cb = new JCheckBox("Optimized paint children");
			cb.setSelected(CoPageItemViewRenderer.DO_OPTIMIZE_PAINT_CHILDREN);
			cb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ev) {
					CoPageItemViewRenderer.DO_OPTIMIZE_PAINT_CHILDREN = ((JCheckBox) ev.getSource()).isSelected();
				}
			});
			p.add(cb);
		}

		{
			JCheckBox cb = new JCheckBox("trace optimized paint");
			cb.setSelected(CoPageItemViewRenderer.DO_TRACE_OPTIMIZED_PAINT);
			cb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ev) {
					CoPageItemViewRenderer.DO_TRACE_OPTIMIZED_PAINT = ((JCheckBox) ev.getSource()).isSelected();
				}
			});
			p.add(cb);
		}

		{
			JCheckBox cb = new JCheckBox("trace workspace paint");
			cb.setSelected(CoPageItemEditorPanel.TRACE_PAINTING);
			cb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ev) {
					CoPageItemEditorPanel.TRACE_PAINTING = ((JCheckBox) ev.getSource()).isSelected();
				}
			});
			p.add(cb);
		}

		{
			JCheckBox cb = new JCheckBox("trace hit testing");
			cb.setSelected(CoSelectionTool.DO_TRACE_HIT_TESTING);
			cb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ev) {
					CoSelectionTool.DO_TRACE_HIT_TESTING = ((JCheckBox) ev.getSource()).isSelected();
				}
			});
			p.add(cb);
		}

		{
			JCheckBox cb = new JCheckBox("Use antialiasing when rendering strokes");
			cb.setSelected(CoStrokeRenderer.USE_ANTIALIASING);
			cb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ev) {
					CoStrokeRenderer.USE_ANTIALIASING = ((JCheckBox) ev.getSource()).isSelected();
				}
			});
			p.add(cb);
		}

		{
			JCheckBox cb = new JCheckBox("use repaint area separation");
			cb.setSelected(CoPageItemEditorPanel.USE_REPAINT_AREA_SEPARATION);
			cb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ev) {
					CoPageItemEditorPanel.USE_REPAINT_AREA_SEPARATION = ((JCheckBox) ev.getSource()).isSelected();
				}
			});
			p.add(cb);
		}

		pack();
	}
	public static void open() {
		if (m_instance == null) {
			m_instance = new CoViewTestUI();
		}

		m_instance.show();
	}
}