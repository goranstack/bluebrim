package com.bluebrim.gui.client;

import java.beans.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.base.client.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.formula.shared.*;

/**
	Basklass för de konverteringsklasser som konverterar en sträng till ett numeriskt värde 
	och vice versa. Klassmetoden #newNumberConverter svarar med en instans av rätt 
	konverteringsklass utifrån värdet på argumentet 'style'.<br>
	Exempel
	<code><pre>
		CoNumberConverter tConverter = CoNumberConverter.newNumberConverter(tAspectAdaptor, CoNumberConverter.INTEGER);
	</pre></code>
	'style' kan anta följande värden
	<ul>
	<li>	CoNumberConverter.INTEGER => CoIntegerConverter
	<li>	CoNumberConverter.LONG	=> CoLongConverter
	<li>	CoNumberConverter.FLOAT => CoFloatConverter
	<li>	CoNumberConverter.DOUBLE => CoDoubleConverter
	<li>	CoNumberConverter.CURRENCY => CoNumberConverter
	<li> CoNumberConverter.PERCENT => CoNumberConverter
	</ul>
 */
public abstract class CoNumberConverter extends CoFormatConverter {
	private Format formatter;
	public final static int INTEGER = 0;
	public final static int LONG = 1;
	public final static int FLOAT = 2;
	public final static int DOUBLE = 3;
	public final static int CURRENCY = 10;
	public final static int PERCENT = 20;

	private CoConvertibleUnitSet m_units;

	private static CoFormula m_formula = new CoFormula();

	protected CoNumberConverter(CoValueModel valueModel) {
		this(valueModel, null);
	}

	protected CoNumberConverter(CoValueModel valueModel, CoConvertibleUnitSet units) {
		super(valueModel);

		m_units = units;
	}

	protected final String doFormat(Object aValue) {
		if (m_units != null) {
			double d = m_units.to(((Number) aValue).doubleValue());
			aValue = new Double(d);
			((NumberFormat) getFormatter()).setMaximumFractionDigits(m_units.getViewDecimalCount());
		}

		String str = getFormatter().format(aValue);

		if (m_units != null) {
			String unitStr = m_units.getName();
			if (unitStr.length() > 0) {
				str += " " + unitStr;
			}
		}

		return str;
	}

	/**
	 	Låt formatteringsobjektet parsa 'newValue'. Svarar med null om 
	 	parsningen misslyckas.
	 */
	protected final Object doParse(String stringValue) throws ParseException {
		if (stringValue.length() == 0)
			return null;

		try {
			m_formula.setUnits(m_units);
			m_formula.evaluate(stringValue);
			stringValue = getFormatter().format(m_formula.getResult());
		} catch (CoFormulaException ex) {
		}

		return getFormatter().parseObject(stringValue);
	}

	public static void formatTest(CoValueModel client, int style, Object value) {

		CoNumberConverter tConverter = newNumberConverter(client, style);
		System.out.println(tConverter.valueToString(value));
	}

	protected Format getDefaultFormatter() {
		return NumberFormat.getInstance(Locale.getDefault());
	}

	protected Format getFormatter() {
		if (formatter == null)
			formatter = getDefaultFormatter();
		return formatter;
	}

	protected String getType() {
		return CoUIStringResources.getName("NUMBER");
	}

	/**
	 * @param client com.bluebrim.base.client.CoValueModel
	 * @param style int
	 * @return CoNumberConverter
	 */
	public static CoNumberConverter newNumberConverter(CoValueModel client, int style) {

		CoNumberConverter tConverter = null;
		switch (style) {
			case CoNumberConverter.INTEGER :
				tConverter = new CoIntegerConverter(client);
				tConverter.setFormatter(NumberFormat.getInstance(Locale.getDefault()));
				break;
			case CoNumberConverter.LONG :
				tConverter = new CoLongConverter(client);
				tConverter.setFormatter(NumberFormat.getInstance(Locale.getDefault()));
				break;
			case CoNumberConverter.FLOAT :
				tConverter = new CoFloatConverter(client);
				tConverter.setFormatter(NumberFormat.getInstance(Locale.getDefault()));
				break;
			case CoNumberConverter.DOUBLE :
				tConverter = new CoDoubleConverter(client);
				tConverter.setFormatter(NumberFormat.getInstance(Locale.getDefault()));
				break;
			case CoNumberConverter.CURRENCY :
				tConverter = new CoCurrencyConverter(client);
				tConverter.setFormatter(NumberFormat.getCurrencyInstance(Locale.getDefault()));
				break;
			case CoNumberConverter.PERCENT :
				tConverter = new CoPercentConverter(client);
				tConverter.setFormatter(NumberFormat.getPercentInstance(Locale.getDefault()));
				break;
			default :
				throw new IllegalArgumentException("CoNumberConverter.newNumberConverter: Style " + style + " unsupported");
		}

		return tConverter;
	}

	/**
	 * @return CoNumberConverter
	 * @param client com.bluebrim.base.client.CoValueModel
	 * @param style int
	 */
	public static CoNumberConverter newNumberConverter(CoValueModel client, CoConvertibleUnitSet units, int style) {

		CoNumberConverter tConverter = null;
		switch (style) {
			case CoNumberConverter.INTEGER :
				tConverter = new CoIntegerConverter(client, units);
				tConverter.setFormatter(NumberFormat.getInstance(Locale.getDefault()));
				break;
			case CoNumberConverter.LONG :
				tConverter = new CoLongConverter(client, units);
				tConverter.setFormatter(NumberFormat.getInstance(Locale.getDefault()));
				break;
			case CoNumberConverter.FLOAT :
				tConverter = new CoFloatConverter(client, units);
				tConverter.setFormatter(NumberFormat.getInstance(Locale.getDefault()));
				break;
			case CoNumberConverter.DOUBLE :
				tConverter = new CoDoubleConverter(client, units);
				tConverter.setFormatter(NumberFormat.getInstance(Locale.getDefault()));
				break;
			case CoNumberConverter.CURRENCY :
				tConverter = new CoCurrencyConverter(client);
				tConverter.setFormatter(NumberFormat.getCurrencyInstance(Locale.getDefault()));
				break;
			case CoNumberConverter.PERCENT :
				tConverter = new CoPercentConverter(client);
				tConverter.setFormatter(NumberFormat.getPercentInstance(Locale.getDefault()));
				break;
			default :
				throw new IllegalArgumentException("CoNumberConverter.newNumberConverter: Style " + style + " unsupported");
		}

		return tConverter;
	}

	/**
	 * @return CoNumberConverter
	 * @param client com.bluebrim.base.client.CoValueModel
	 * @param style int
	 */
	public static void parseTest(int style, String value) {

		CoNumberConverter tConverter = newNumberConverter(new CoValueHolder(), style);
		try {
			tConverter.setValueFromString(value);
			System.out.println(tConverter.getStringValue());
		} catch (CoConverterParseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
		} catch (PropertyVetoException e) {
		}
	}

	protected void setFormatter(Format format) {
		formatter = format;
	}
}