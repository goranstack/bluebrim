package com.bluebrim.layout.impl.server;

import com.bluebrim.base.shared.CoFactoryElementIF;
import com.bluebrim.base.shared.CoFactoryIF;
import com.bluebrim.layout.impl.server.decorator.CoPatternFill;

/**
 * @author: Dennis
 */
 
public class CoPatternFillFactory implements CoFactoryIF
{
public CoFactoryElementIF createObject()
{
	return new CoPatternFill();
}
}