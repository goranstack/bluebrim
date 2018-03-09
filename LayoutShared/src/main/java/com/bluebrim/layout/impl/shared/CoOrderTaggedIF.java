package com.bluebrim.layout.impl.shared;

/**
 * Interface defining the mutable protocol for objects that have an order tag.
 * 
 * @author: Dennis Malmstr�m
 */

public interface CoOrderTaggedIF extends CoImmutableOrderTaggedIF
{
void setOrderTag( int i );
}
