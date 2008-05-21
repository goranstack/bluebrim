package com.bluebrim.text.impl.shared;

/**
 * Tab stop set protocol
 * 
 * @author: Dennis Malmström
 */

 public interface CoTabSetIF extends java.io.Serializable
{
CoTabStopIF addTabStop();
CoTabSetIF copy();
int getIndexOfTabStop( CoTabStopIF t );
CoTabStopIF getTabAfter( float location );
CoTabStopIF getTabStop( int i );
int getTabStopCount();
void removeTabStop( CoTabStopIF tabStop );
}
