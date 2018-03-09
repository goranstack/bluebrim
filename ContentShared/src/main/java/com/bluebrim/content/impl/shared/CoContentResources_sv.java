package com.bluebrim.content.impl.shared;


public class CoContentResources_sv extends CoContentResources {
	static final Object[][] contents =
	{

		// CoEditorialPrefs
		{EDITORIAL_PREFS,						"Inställningar redaktion"},
		
		// Property dividers
		{ORIGIN,		"Ursprung"},
		{COPYRIGHT,		"Copyright"},
		{CAPTION,		"Bildtext"},
		{BYLINE,		"Signatur"},
		{KEYWORDS,		"Nyckelord"},
		{CATEGORIES,	"Kategorier"},

		// Text/Article properties
		{CHAR_COUNT,								"Antal tecken"},
		{WORD_COUNT,								"Antal ord"},
		{COLUMN_MM,								"Spalt mm"},
		{WRITER,								"Skribent"},
		{REGISTERED_BY,							"Inlagd av"},			
		{IS_ARCHIVED,							"Arkiverad"},

		// CoHypheantionJustification
		{HYPHENATION_JUSTIFICATION,			"Avstavning och undantag"},
		
		// CoArticle
		{CREATED_BY,								"Skapad av"},
		{TEXT_EXTRACT,						 		"Textutdrag"},
		
	};
/**
 * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}