package com.bluebrim.text.impl.shared;

/**
 * Interface defining the mutable protocol of a linebreaker that allows breaking according to the Liang algorithm.
 * 
 * @author: Dennis Malmström
 */
 
public interface CoMutableLiangLineBreakerIF extends CoLiangLineBreakerIF, CoMutableLineBreakerIF
{
void setMinimumPrefixLength( int m );
void setMinimumSuffixLength( int m );
void setMinimumWordLength( int m );
}
