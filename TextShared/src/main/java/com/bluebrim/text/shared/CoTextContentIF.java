package com.bluebrim.text.shared;
import com.bluebrim.content.shared.*;

public interface CoTextContentIF extends CoAtomicContentIF, CoFormattedTextHolderIF {

	public static String TEXT_CONTENT = "text_content";

    String FACTORY_KEY = "text_content";
	String ICON_NAME = "CoTextContentIF.gif";

	public CoTextContentIF deepClone();

	int getCharCount();

	int getWordCount();

	public String getWriter();

	public void setWriter(String writer);

	double getColumnMM(CoFormattedTextHolderIF.Context c);
}