package com.bluebrim.text.impl.client;

import java.util.*;

/**
 * Interface som definierar gr�nssnittet f�r att lyssna p� den "selekterade typografin"
 * i en CoAbstractTextPane.
 */
 
public interface CoAttributeListenerIF extends EventListener
{
void attributesChanged( CoAttributeEvent event );
}
