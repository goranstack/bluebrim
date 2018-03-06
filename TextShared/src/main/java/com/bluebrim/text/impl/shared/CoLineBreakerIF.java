package com.bluebrim.text.impl.shared;

import java.io.*;

import javax.swing.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Interface defining the immutable protocol for linebreakers
 * Linebreakers are objects that decide where lines of text can be broken.
 * 
 * @author: Dennis Malmström
 */

public interface
	CoLineBreakerIF
extends
	CoObjectIF,
	Serializable,
	CoFactoryElementIF,
	CoXmlEnabledIF
{
	public interface BreakPointIteratorIF
	{
		public boolean hasNext();
		public int next();
	}
// return the breakpoints of a given text

CoLineBreakerIF.BreakPointIteratorIF getBreakPoints( Segment text );
CoMutableLineBreakerIF getMutableProxy();
// full name of the class that implements the ui (CoDomainUserInterface) for the linebreaker

String getUIname();
}