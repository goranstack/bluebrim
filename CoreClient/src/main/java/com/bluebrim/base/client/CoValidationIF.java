package com.bluebrim.base.client;

import java.beans.*;
/**
 	Interface f�r klasser som fungerar som v�rdem�ngder f�r v�rdeobjekt, dvs validerar
 	ett nytt v�rde f�r ett v�rdeobjekt innan det s�tts.
 */
public interface CoValidationIF extends VetoableChangeListener{
public abstract boolean validate(Object value);
}
