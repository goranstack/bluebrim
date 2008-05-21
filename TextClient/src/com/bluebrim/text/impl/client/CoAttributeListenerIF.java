package com.bluebrim.text.impl.client;

import java.util.*;

/**
 * Interface som definierar gränssnittet för att lyssna på den "selekterade typografin"
 * i en CoAbstractTextPane.
 */
 
public interface CoAttributeListenerIF extends EventListener
{
void attributesChanged( CoAttributeEvent event );
}
