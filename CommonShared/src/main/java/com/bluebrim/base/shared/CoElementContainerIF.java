package com.bluebrim.base.shared;

import java.util.*;
/**
 	Interface för verksamhetobjekt som fungerar som behållare i en komposition, 
 	dvs byggs upp av andra verksamhetsobjekt som det har liggande i en lista. 
 */
public interface CoElementContainerIF {
/**
	Implementeras så att den svarar med en kollektion som 
	innehåller mottagarens 'barn'.
*/
public Vector getElements ();
}
