package com.bluebrim.text.shared;
import javax.swing.text.*;

/**
 * Interface defining the protocol of an operation performed on an text attribute set.
 * 
 * @author: Dennis Malmstr�m
 */

public interface CoAttributeSetOperationIF
{
void apply( MutableAttributeSet as );
}
