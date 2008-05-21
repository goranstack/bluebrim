package com.bluebrim.text.shared;

import java.rmi.*;

/**
 * @author Göran Stäck
 */
public interface CoTextServer extends Remote
{
    public static final String RMIname = "TextServer";
    
    public CoTextContentIF createTextContent(CoFormattedText text) throws RemoteException;

    public CoTextContentIF createTextContent(String name, String writer, String taggedText, int id, String idAlgorithm) throws RemoteException;

    public CoFormattedText createFormattedText(String taggedText) throws RemoteException;

    public CoTextMeasurementPrefsIF getTextMeasurementPrefs() throws RemoteException;

    public CoHyphenationPatternCollectionIF getHyphenationPatterns() throws RemoteException;

    public CoStyledTextPreferencesIF getDefaultStyledTextPreferences() throws RemoteException;
}