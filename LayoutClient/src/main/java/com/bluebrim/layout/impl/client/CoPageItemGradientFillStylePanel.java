package com.bluebrim.layout.impl.client;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.paint.client.*;
import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Page item fill style property panel.
 *
 * @author: Dennis
 */

public class CoPageItemGradientFillStylePanel extends CoPageItemFillStylePanel {
	public static final String FILL = "CoPageItemGradientFillStylePanel.FILL";
	public static final String BLEND = "CoPageItemGradientFillStylePanel.BLEND";
	public static final String BLEND_LENGTH = "CoPageItemGradientFillStylePanel.BLEND_LENGTH";
	public static final String STYLE = "CoPageItemGradientFillStylePanel.STYLE";
	public static final String ANGLE = "CoPageItemGradientFillStylePanel.ANGLE";
	public static final String SOLID_BLEND = "CoPageItemGradientFillStylePanel.SOLID_BLEND";
	public static final String CYCLIC_BLEND = "CoPageItemGradientFillStylePanel.CYCLIC_BLEND";

	public CoOptionMenu m_color1OptionMenu;
	public CoTextField m_shade1TextField;
	public CoOptionMenu m_styleOptionMenu;
	public CoTextField m_angleTextField;
	public CoTextField m_blendLengthTextField;
	public CoOptionMenu m_color2OptionMenu;
	public CoTextField m_shade2TextField;

	public void doUpdate() {
		if (m_domain == null)
			return;
		if (!isVisible())
			return;

		CoFillStyleIF fs = (CoFillStyleIF) m_domain.getFillStyle();

		if (!(fs instanceof CoGradientFillIF))
			return;

		CoImmutableGradientFillIF f = (CoImmutableGradientFillIF) fs; //m_domain.getFillStyle();

		m_color1OptionMenu.setSelectedItem(f.getColor1());
		m_shade1TextField.setText(Float.toString(f.getShade1()));
		m_styleOptionMenu.setSelectedItem(f.getCyclic() ? CYCLIC_BLEND : SOLID_BLEND);
		m_angleTextField.setText(CoLengthUnitSet.format(f.getAngle()));
		m_color2OptionMenu.setSelectedItem(f.getColor2());
		m_shade2TextField.setText(Float.toString(f.getShade2()));

		double d = f.getBlendLength();
		if (d < 0) {
			m_blendLengthTextField.setText((d * -100) + " %");
		} else {
			m_blendLengthTextField.setText(CoLengthUnitSet.format(d, CoLengthUnit.LENGTH_UNITS));
		}
	}

	public void setContext(CoPageItemEditorContextIF context) {
		// colors
		{
			m_color1OptionMenu.setQuiet(true);
			m_color2OptionMenu.setQuiet(true);

			if (m_color1OptionMenu.getItemCount() > 0)
				m_color1OptionMenu.removeAllItems();
			if (m_color2OptionMenu.getItemCount() > 0)
				m_color2OptionMenu.removeAllItems();

			CoColorIF c = (CoColorIF) CoFactoryManager.createObject(CoNoColorIF.NO_COLOR);
			m_color1OptionMenu.addItem(c);
			m_color2OptionMenu.addItem(c);

			int I = 1;
			CoColorCollectionIF colors = (context == null) ? null : context.getPreferences();

			if (colors != null) {
				Iterator i = colors.getColors().iterator();
				while (i.hasNext()) {
					c = (CoColorIF) i.next();
					if (!c.getFactoryKey().equals(CoNoColorIF.NO_COLOR)) {
						m_color1OptionMenu.addItem(c);
						m_color2OptionMenu.addItem(c);
						I++;
					}
				}
			}

			//		m_color1OptionMenu.setMaximumRowCount( I );
			//		m_color2OptionMenu.setMaximumRowCount( I );

			m_color1OptionMenu.setQuiet(false);
			m_color2OptionMenu.setQuiet(false);
		}

	}

	public CoPageItemGradientFillStylePanel(CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor) {
		super(b, commandExecutor);
	}

	protected void create(CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor) {
		setLayout(new CoColumnLayout(true));

		CoPanel p0 = b.createPanel(createFormLayout());

		CoColorPanel pcp = new CoColorPanel(b);
		m_color1OptionMenu = pcp.getColorOptionMenu();
		m_shade1TextField = pcp.getShadeTextField();
		p0.add(b.createLabel(CoPageItemUIStringResources.getName(COLOR)));
		p0.add(pcp);
		p0.setBorder(BorderFactory.createTitledBorder(CoPageItemUIStringResources.getName(FILL)));
		add(p0);

		CoPanel p1 = b.createPanel(createFormLayout());
		p1.setBorder(BorderFactory.createTitledBorder(CoPageItemUIStringResources.getName(BLEND)));
		add(p1);
		{
			p1.add(b.createLabel(CoPageItemUIStringResources.getName(STYLE)));
			p1.add(m_styleOptionMenu = b.createOptionMenu());
			m_styleOptionMenu.addItem(SOLID_BLEND);
			m_styleOptionMenu.addItem(CYCLIC_BLEND);
			m_styleOptionMenu.setRenderer(new CoOptionMenu.TranslatingRenderer(CoPageItemUIStringResources.getBundle()));

			p1.add(b.createLabel(CoPageItemUIStringResources.getName(BLEND_LENGTH)));
			p1.add(m_blendLengthTextField = b.createSlimTextField());
			m_blendLengthTextField.setHorizontalAlignment(CoTextField.RIGHT);

			p1.add(b.createLabel(CoPageItemUIStringResources.getName(ANGLE)));
			p1.add(m_angleTextField = b.createSlimTextField());
			m_angleTextField.setHorizontalAlignment(CoTextField.RIGHT);

			pcp = new CoColorPanel(b);
			m_color2OptionMenu = pcp.getColorOptionMenu();
			m_shade2TextField = pcp.getShadeTextField();
			p1.add(b.createLabel(CoPageItemUIStringResources.getName(COLOR)));
			p1.add(pcp);
		}

		m_styleOptionMenu.setEnabled(false);
		m_angleTextField.setEnabled(false);
		m_blendLengthTextField.setEnabled(false);
		m_color2OptionMenu.setEnabled(false);
		m_shade2TextField.setEnabled(false);

		m_color1OptionMenu.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if (ev.getStateChange() == ItemEvent.SELECTED) {
					boolean e = ((CoColorIF) m_color1OptionMenu.getSelectedItem()).getPreviewColor() != null;
					m_styleOptionMenu.setEnabled(e);
					m_angleTextField.setEnabled(e);
					m_blendLengthTextField.setEnabled(e);
					m_color2OptionMenu.setEnabled(e);
					m_shade2TextField.setEnabled(e);
				}
			}
		});

		m_color1OptionMenu.addActionListener(new OptionMenuCommandAdapter(commandExecutor, CoPageItemCommands.SET_FILL_COLOR, null));
		m_shade1TextField.addActionListener(
			new DoubleTextFieldCommandAdapter(commandExecutor, CoPageItemCommands.SET_FILL_SHADE, false, 1, 0));

		m_styleOptionMenu.addActionListener(new OptionMenuCommandAdapter(commandExecutor, CoPageItemCommands.SET_CYCLIC_BLEND, null) {
			protected Object getCurrentValue() {
				return ((CoImmutableGradientFillIF) m_domain.getFillStyle()).getCyclic() ? CYCLIC_BLEND : SOLID_BLEND;
			}

			protected void prepare() {
				((CoShapePageItemSetBooleanCommand) m_command).prepare(m_domain, m_value == CYCLIC_BLEND);
			}
		});

		m_angleTextField.addActionListener(
			new DoubleTextFieldCommandAdapter(commandExecutor, CoPageItemCommands.SET_BLEND_ANGLE, false, 1, 0));

		m_blendLengthTextField
			.addActionListener(new DoubleTextFieldCommandAdapter(commandExecutor, CoPageItemCommands.SET_BLEND_CYCLE_LENGTH) {
			protected double parse(String str) {
				if (str.indexOf('%') == -1) {
					return CoLengthUnitSet.parse(str, Double.NaN, CoLengthUnit.LENGTH_UNITS);
				} else {
					return CoLengthUnitSet.parse(str, Double.NaN) / -100;
				}
			}
		});

		m_color2OptionMenu.addActionListener(new OptionMenuCommandAdapter(commandExecutor, CoPageItemCommands.SET_BLEND_COLOR, null));
		m_shade2TextField.addActionListener(
			new DoubleTextFieldCommandAdapter(commandExecutor, CoPageItemCommands.SET_BLEND_SHADE, false, 1, 0));

	}
}