package com.bluebrim.xml.shared;
/**
 * Implementors of this interface can be exported to XML.  If you also want to be imported from XML,
 * implement <code>CoXmlEnabled</code> instead.
 */
public interface CoXmlExportEnabledIF {

	/**
	 *	Used by a <code>CoXmlVisitorIF</code> instance when visiting an object.
	 *  The object then uses the <code>CoXmlVisitorIF</code> interface to feed the
	 *  visitor with state information
	 */
	public void xmlVisit(CoXmlVisitorIF visitor) throws CoXmlWriteException;
}