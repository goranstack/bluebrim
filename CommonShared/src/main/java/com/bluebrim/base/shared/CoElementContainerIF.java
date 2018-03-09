package com.bluebrim.base.shared;

import java.util.*;
/**
 	Interface f�r verksamhetobjekt som fungerar som beh�llare i en komposition, 
 	dvs byggs upp av andra verksamhetsobjekt som det har liggande i en lista. 
 */
public interface CoElementContainerIF {
/**
	Implementeras s� att den svarar med en kollektion som 
	inneh�ller mottagarens 'barn'.
*/
public Vector getElements ();
}
