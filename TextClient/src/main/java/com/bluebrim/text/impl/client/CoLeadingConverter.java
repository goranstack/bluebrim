package com.bluebrim.text.impl.client;

import java.text.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Creation date: (2000-06-02 09:23:01)
 * @author Dennis
 */
public class CoLeadingConverter extends CoFloatConverter {
	public CoLeadingConverter(CoValueModel valueModel) {
		super(valueModel);
	}

	public String valueToString(Object v) {
		return (v != null) ? CoLeading.format((CoLeading) v) : "";
	}

	protected Object parseString(String v) throws ParseException {
		if (v == null)
			return null;
		return CoLeading.parse(v.toString());
	}
}