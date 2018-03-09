package com.bluebrim.time.shared;

/**
 * The place for generic time related global constants.
 *
 * NOTE: This is a work in progress.
 *
 * @author Markus Persson 2000-05-16
 */
public interface CoTimeConstants {
	// NOTE: "public final static" is implicit in interfaces.
	long MILLIS_PER_SECOND = 1000;
	long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
	long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
	long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

	int MINUS_INFINITY_GREGORIAN = Integer.MIN_VALUE;
	int INFINITY_GREGORIAN = Integer.MAX_VALUE;
	long MINUS_INFINITY_MILLIS = Long.MIN_VALUE;
	long INFINITY_MILLIS = Long.MAX_VALUE;
	String MINUS_INFINITY_INDICATOR = "Tidernas begynnelse";	//"-\u221E";
	String INFINITY_INDICATOR = "Obestämd framtid";				//"\u221E";

	// Constants for weekdays
	int SUNDAY = 0;
	int MONDAY = 1;
	int TUESDAY = 2;
	int WEDNESDAY = 3;
	int THURSDAY = 4;
	int FRIDAY = 5;
	int SATURDAY = 6;

	// Constants for months. NOTE: This differs from legacy java.lang.Date!
	int JAN = 1;
	int FEB = 2;
	int MAR = 3;
	int APR = 4;
	int MAY = 5;
	int JUN = 6;
	int JUL = 7;
	int AUG = 8;
	int SEP = 9;
	int OCT = 10;
	int NOV = 11;
	int DEC = 12;

	// Temporary support for legacy java.lang.Date!
	int JAN_LEGACY = 0;
	int FEB_LEGACY = 1;
	int MAR_LEGACY = 2;
	int APR_LEGACY = 3;
	int MAY_LEGACY = 4;
	int JUN_LEGACY = 5;
	int JUL_LEGACY = 6;
	int AUG_LEGACY = 7;
	int SEP_LEGACY = 8;
	int OCT_LEGACY = 9;
	int NOV_LEGACY = 10;
	int DEC_LEGACY = 11;

	// Legacy. Remove? /Markus
	public final static int SHORT_DOW 	= 0;
	public final static int CHAR3_DOW 	= 1;
	public final static int LONG_DOW 	= 2;

	// Constants related to the choice of day zero, which is not final.
	int WEEKDAY_OF_DAY_ZERO = MONDAY;
	int DAYS_PER_400_YEARS = 365 * 400 + 97;
	int MIN_DAYS_PER_100_YEARS = 365 * 100 + 24;
	int MAX_DAYS_PER_4_YEARS = 365 * 4 + 1;
	int MIN_DAYS_PER_YEAR = 365;
	int MAX_DAYS_PER_MONTH = 31;

	// Non localized export constants.
	String DAY_EXPORT_PATTERN = "yyyy-MM-dd";
	String TIMESTAMP_EXPORT_PATTERN = "yyyy-MM-dd HH:mm:ss.S z";

	// Keys for localized data.
	String DAY_FORMAT_PATTERNS = "day_format_patterns";
	String DAY_PARSE_PATTERNS = "day_parse_patterns";
	String TIMESTAMP_FORMAT_PATTERNS = "timestamp_format_patterns";
	// Legacy
	String DATEFORMAT_SEPARATOR = "dateformat_separator";
	String TODAY_SHORTED = "today_shorted";
	String TOMORROW_SHORTED = "tomorrow_shorted";
	String YESTERDAY_SHORTED = "yesterday_shorted";

	// From late CoTimePeriodIF /Markus 2000-10-12
	String ALWAYS = "always";
	String TIME_PERIOD = "time_period";
	String TO_DATE_LESS_THAN_FROM_DATE = "to_date_less_than_from_date";
}