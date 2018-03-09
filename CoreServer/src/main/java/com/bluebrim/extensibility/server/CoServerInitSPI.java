package com.bluebrim.extensibility.server;

import com.bluebrim.extensibility.shared.*;

/**
 * SPI for server init providers.
 * 
 * @author Markus Persson 2002-02-21
 */
public interface CoServerInitSPI extends CoOrderDependent {
	// Known canonical names used in prerequisites.
	// Typically these correspond to domain names.
	String AGENDA = "agenda";
	String ADDRESS = "address";
	String ADVERTISING = "advertising";
	String BUSINESS = "business";
	String DISTRIBUTION = "distribution";
	String TOPIC = "topic";
	String FONT = "font";

	// The layout domain has tree providers
	String LAYOUT = "layout";
	String LAYOUTTEMPLATE = "layouttemplate";
	String PAPERPAGETEMPLATE = "paperpagetemplate";
	
	String MEDIASYSTEM = "mediasystem";
	String MEDIAPRODUCTION = "mediaproduction";
	String PAGE = "page";
	String PAPERMEDIA = "papermedia";
	String PAYMENT = "payment";
	String PRESS = "press";
	String PROPERTIES = "properties";
	String PUBLICATION = "publication";
	String SUBSCRIPTION = "subscription";
	String SPELLCHECK = "spellcheck";
	String TEXT = "text";
	String TAX = "tax";
	String USER = "user";

	/**
	 * Perform initialization. In addition to the initialization
	 * process being customized by java preferences settings,
	 * that the boolean <code>minimalistic</code> is true should
	 * be interpreted so as to only do the bare minimum of
	 * initialization that will permit the subsystem to work.
	 * This is mostly intended for development where a quick
	 * start is desired.
	 * 
	 * Returns true if the initialization was successful. False
	 * otherwise, for instance due to a missing dependent. Such
	 * errors are deferred to here,	because an initializer may
	 * not actually require all the initializers they claim to
	 * be dependent upon. The ordering of initializers only takes
	 * into account those that exist. Only cyclic dependencies
	 * will cause the ordering to fail.
	 */
	public boolean init(boolean minimalistic);
}
