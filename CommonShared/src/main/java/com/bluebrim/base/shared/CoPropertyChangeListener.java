package com.bluebrim.base.shared;

import java.util.*;
/**
 Interface för de objekt som lyssnar efter CoProperyChangeEvent, dvs ändring av egenskaper hos ett 
 objekt som implementerar CoObjectIF. Utnyttjas huvudsakligen av en CoAspectAdaptor för att den skall 
 få reda på om aspekten den representerar ändras bakom dess rygg.
 @see CoPropertyChangeEvent
 @see com.bluebrim.base.client.CoAspectAdaptor
 */

public interface CoPropertyChangeListener extends EventListener {


void propertyChange(CoPropertyChangeEvent evt);
}
