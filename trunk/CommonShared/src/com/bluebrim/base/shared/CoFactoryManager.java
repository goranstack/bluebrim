package com.bluebrim.base.shared;

import com.bluebrim.extensibility.shared.*;

/**
	<code>CoFactoryManager</code> is an abstract class that is responsible for
	holding all factory classes.  Each access to a factory class is via static methods in 
	<code>CoFactoryManager</code>. In this way we have a protocol that makes it possible
	to write code that doesn't need to know if it will execute on the server or on the client. 
	The class exists on the client side as well as on the server side and has a static variable 
	that holds a singleton of a concrete subclass in each case.	
	<br>
	Gemstone/J can share VM's between several concurrent sessions and it's not advisable to 
	keep references to persistent objects in static variables. Therefore the static variable is 
	not used for the factory manager installed on the server and a lookup via the session manager
	is used in stead.
	<p>
	There are three concrete subclasses to CoFactoryManager
	<ul>
	<li><code>CoGsServerFactoryManager</code> which is installed in the "server CoFactoryManager"
	<li><code>CoClientFactoryManager</code> which is installed on the client and is a wrapper
	of a remote adaptor to the instance of <code>CoGsServerFactoryManager</code> on the server.
	<li><code>CoLocalFactoryManager</code> which is used when running in a test environment,
	without a server.
	</ul>
	Code example on how to access the factory for a class that implements CoFactoryElement.
	<code><pre>
		CoFactoryIF tFactory = CoFactoryManager.getFactory(tFactoryElement);
	</pre></code>
	To access a factory via the factory key:
	<code><pre>
		CoFactoryIF tFactory = CoFactoryManager.getFactory(PUBLICATION);
	</pre></code>
	The factories are stored in the factory manager via utility classes in each logical project.
	These classes are normally named Co*Factories.
	<p>.
	An example taken from the date pattern project:
	<code>CoDatePatternFactories</code> has a static method
	<pre><code>
		protected static void createFactories(CoFactoryManager factoryManager)
		{

			factoryManager.add(DATE_PATTERN_DEMO, new CoDatePatternDemoFactory());
			
			factoryManager.add(ALL_THE_RED_DAYS, new com.bluebrim.gemstone.shared.CoRemoteSingletonFactory(new CoAllTheRedDays()));

			factoryManager.add(MONDAYS, new com.bluebrim.gemstone.shared.CoRemoteSingletonFactory(new CoMondays()));
			factoryManager.add(TUESDAYS, new com.bluebrim.gemstone.shared.CoRemoteSingletonFactory(new CoTuesdays()));
			factoryManager.add(WEDNESDAYS, new com.bluebrim.gemstone.shared.CoRemoteSingletonFactory(new CoWednesdays()));
			factoryManager.add(THURSDAYS, new com.bluebrim.gemstone.shared.CoRemoteSingletonFactory(new CoThursdays()));
			factoryManager.add(FRIDAYS, new com.bluebrim.gemstone.shared.CoRemoteSingletonFactory(new CoFridays()));
			factoryManager.add(SATURDAYS, new com.bluebrim.gemstone.shared.CoRemoteSingletonFactory(new CoSaturdays()));
			factoryManager.add(SUNDAYS, new com.bluebrim.gemstone.shared.CoRemoteSingletonFactory(new CoSundays()));

			factoryManager.add(EVERY_DAY, new com.bluebrim.gemstone.shared.CoRemoteSingletonFactory(new CoEveryDay()));
			factoryManager.add(NEVER, new com.bluebrim.gemstone.shared.CoRemoteSingletonFactory(new CoNever()));
			...
			...
			...
		}
	</code></pre>
	which instantiates and installs all date pattern factory classes.
	<br>
 */
public abstract class CoFactoryManager implements CoFactoryManagerIF, CoPersistenceConstants {

	private static CoFactoryManager INSTANCE;

	protected CoFactoryManager() {
	}

	public static CoFactoryElementIF createObject(String key) {
		return getFactory(key).createObject();
	}

	public static CoFactoryIF getFactory(String key) {
		CoFactoryIF tFactory = getInstance().getFactoryFor(key);
		if (tFactory == null)
			throw new IllegalArgumentException("Hittar ingen fabrik i CoFactoryManager.getFactory för: " + key);
		return tFactory;
	}

	public static CoFactoryIF getFactory(CoFactoryElementIF anObject) {
		return getFactory(anObject.getFactoryKey());
	}

	public static CoFactoryManager getInstance() {
		return INSTANCE;
	}

	public static void setInstance(CoFactoryManagerIF factoryManager) {
			// Well, an ugly cast. But none of this will remain!
			// /Markus 2002-04-26
			INSTANCE = (CoFactoryManager) factoryManager;
	}
}