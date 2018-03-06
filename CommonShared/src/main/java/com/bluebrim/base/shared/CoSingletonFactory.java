package com.bluebrim.base.shared;


/**
	Factory class that implements the singleton pattern, i.e
	there can be only one instance of the business object the
	factory is responsible for.
	<br>
	This is implemented by having an instance variable which at
	creation time is loaded with an instance of the business object.
	The <code>createObject</code> method then just returns this variable.
*/
public class CoSingletonFactory implements CoFactoryIF {
	protected CoFactoryElementIF singleton;
/**
 * @param pattern CoDatePattern
 */
public CoSingletonFactory ( CoFactoryElementIF singleton) {
	this.singleton = singleton;
}
/**
 * createObject method comment.
 */
public com.bluebrim.base.shared.CoFactoryElementIF createObject() {
	return singleton;
}
}