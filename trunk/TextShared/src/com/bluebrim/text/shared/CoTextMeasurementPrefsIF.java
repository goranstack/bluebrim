package com.bluebrim.text.shared;
import java.rmi.*;
import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * Interface defining the protocol of text editor user preferences.
 * The protocol consists of text measurement properties.
 * 
 * @author: Dennis Malmström
 */

public interface CoTextMeasurementPrefsIF extends CoObjectIF, Remote {
	double getMeasurementColumnWidth();
	List getMeasurementTags();
	void setMeasurementColumnWidth(double w);
	void setMeasurementTags(List tags);
}
