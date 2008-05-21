package com.bluebrim.layout.impl.server;

import com.bluebrim.base.shared.CoFactoryElementIF;
import com.bluebrim.base.shared.CoFactoryIF;
import com.bluebrim.layout.impl.server.decorator.CoGradientFill;

/**
 * @author: Dennis
 */
 
public class CoGradientFillFactory implements CoFactoryIF
{
public CoFactoryElementIF createObject()
{
	return new CoGradientFill();
}
}