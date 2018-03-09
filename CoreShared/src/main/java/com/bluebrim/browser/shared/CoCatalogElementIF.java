package com.bluebrim.browser.shared;

import com.bluebrim.base.shared.*;

/**
 	Protokoll för de klasser som kan representeras
	såväl grafiskt som med text i en lista. 
 	Utvidgar CoFactoryElement med metoder som svarar med 
 	typ, identitet samt den ikon som skall representera objektet grafiskt.
 */
public interface CoCatalogElementIF extends CoVisualizableIF, CoFactoryElementIF {
	/**
	 * @return String
	 */
	public String getSmallIconName();

	/**
		Skall svara med en String som anger min typ, dvs ett mera "populär" beteckning
		på min klass. Ex "Utgivare", "Publikation" etc.
	 */
	public String getType();
}
