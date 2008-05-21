package com.bluebrim.xml.shared;
/**
 * Implement this interface if you want your class to be able to export / import itself to /from an XML file.
 * <p>
 * Creation date: (2001-06-13 16:45:35)
 * 
 * @author Johan Walles
 *
 * @see com.bluebrim.xml.shared.CoXmlImportEnabledIF
 * @see com.bluebrim.xml.shared.CoXmlExportEnabledIF
 */
public interface CoXmlEnabledIF extends CoXmlExportEnabledIF, CoXmlImportEnabledIF
{
}
