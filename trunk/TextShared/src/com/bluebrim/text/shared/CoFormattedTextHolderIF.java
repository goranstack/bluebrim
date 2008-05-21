package com.bluebrim.text.shared;

import java.rmi.*;

import com.bluebrim.base.shared.*;

/**
 * Protocol for objects that hold a CoFormattedTextIF
 *
 * @author: Dennis
 */
public interface CoFormattedTextHolderIF extends Remote, CoObjectIF, CoNamed {
	public static final String TEXT_PROPERTY = "text_property";

	public interface Context {
		String KEY = "CoFormattedTextHolderIF.Context";

		CoTextStyleApplier getTextStyleApplier();
	};

	public String getTextExtract(int charCount);

	public CoFormattedText getFormattedText(CoFormattedTextHolderIF.Context c);

	public CoFormattedText getMutableFormattedText( CoFormattedTextHolderIF.Context c);

	public String getWriter();

	public void setFormattedText(CoFormattedText doc);

}