package com.bluebrim.text.impl.shared;


/**
 * Abstract class defining the protocol for exporting a com.bluebrim.text.shared.CoStyledDocumentIF
 *
 * @author: Dennis Malmstr�m
 */

public abstract class CoAbstractTextExporter
{
public abstract void doExport( com.bluebrim.text.shared.CoStyledDocumentIF doc ) throws Exception;
}
