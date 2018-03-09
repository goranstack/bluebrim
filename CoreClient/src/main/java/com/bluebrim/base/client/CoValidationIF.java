package com.bluebrim.base.client;

import java.beans.*;
/**
 	Interface för klasser som fungerar som värdemängder för värdeobjekt, dvs validerar
 	ett nytt värde för ett värdeobjekt innan det sätts.
 */
public interface CoValidationIF extends VetoableChangeListener{
public abstract boolean validate(Object value);
}
