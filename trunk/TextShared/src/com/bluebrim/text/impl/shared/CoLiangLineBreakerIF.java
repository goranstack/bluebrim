package com.bluebrim.text.impl.shared;

/**
 * Interface defining the immutable protocol of a linebreaker that allows breaking according to the Liang algorithm.
 * 
 * @author: Dennis Malmström
 */
 
public interface CoLiangLineBreakerIF extends CoLineBreakerIF
{
	public static final String LIANG_LINE_BREAKER = "CoLiangLineBreakerIF.LIANG_LINE_BREAKER";
int getMinimumPrefixLength();
int getMinimumSuffixLength();
int getMinimumWordLength();
}
