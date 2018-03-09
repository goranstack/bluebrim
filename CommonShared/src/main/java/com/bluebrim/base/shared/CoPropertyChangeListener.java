package com.bluebrim.base.shared;

import java.util.*;
/**
 Interface f�r de objekt som lyssnar efter CoProperyChangeEvent, dvs �ndring av egenskaper hos ett 
 objekt som implementerar CoObjectIF. Utnyttjas huvudsakligen av en CoAspectAdaptor f�r att den skall 
 f� reda p� om aspekten den representerar �ndras bakom dess rygg.
 @see CoPropertyChangeEvent
 @see com.bluebrim.base.client.CoAspectAdaptor
 */

public interface CoPropertyChangeListener extends EventListener {


void propertyChange(CoPropertyChangeEvent evt);
}
