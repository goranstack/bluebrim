package com.bluebrim.base.shared;

/**
 	Interface for all factory classes, i.e classes that represents a business object class 
 	and are responsible for creating an instance of the business object.
 	<br>
 	A factory class is necessary if the client wants to create an object on the server as
 	constructor calls are not supported over the network.
 	<br>
 	The business object must implement <code>CoFactoryElementIF</code>
 */
public interface CoFactoryIF {
	public CoFactoryElementIF createObject ();
}
