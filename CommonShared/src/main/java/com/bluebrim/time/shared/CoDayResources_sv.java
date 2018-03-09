package com.bluebrim.time.shared;


public class CoDayResources_sv extends CoDayResources {
	// IMPORTANT: Do NOT change here just like that. The parsing
	// routines unfortunately have a few quirks that makes them
	// very sensitive to changes here. They should be rewritten.
	// /Markus 2001-03-26
	static final Object[][] contents =
	{
		{DAY_FORMAT_PATTERNS,
			new String[] {
				"EEEE d MMMM yyyy",	// Full
				"d MMMM yyyy",	// Long
				"yyyy-MM-dd",	// Medium (Default)
				"yyyy-MM-dd",	// Short
			}
		},
		{DAY_PARSE_PATTERNS,
			new String[] {	// In order to attempt parsing
				"yy-MM-dd",
				"yyyy-MM-dd",	// Unlike the same format used by parseExported, this may be used leniently.
				"yyyyMMdd",
				"yyMMdd",
				"d/M-yy",
			}
		},
		// Both for formatting and parsing.
		{TIMESTAMP_FORMAT_PATTERNS,
			new String[] {
				"'den 'd MMMM yyyy 'kl 'HH:mm:ss",	// Full
				"yyyy-MMM-dd HH:mm:ss",	// Long
				"yyyy-MM-dd HH:mm:ss",	// Medium
				"yyyy-MM-dd HH:mm",	// Short
			}
		},

		// Shortcuts when interacting. Currently in lowercase and no whitespaces.
		{TODAY_SHORTED, "idag"},
		{TOMORROW_SHORTED, "imorgon"},
		{YESTERDAY_SHORTED, "igår"},

		// Legacy, remove.
		{DATEFORMAT_SEPARATOR, "-"},
	};
/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}
