package com.bluebrim.time.shared;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Preliminary clean implementation.
 *
 * PENDING: Implement more static methods handling millis
 * to make it possible to avoid creating unneccesary objects?
 *
 * @author Markus Persson 2000-05-18
 */
public class CoTimeStamp implements CoUniversalTime, Serializable, Comparable {
	protected static ThreadLocal CALENDAR_HOLDER = new ThreadLocal();
	protected static ThreadLocal FORMATTER_HOLDER = new ThreadLocal();
	protected static ThreadLocal LEGACY_DATE_HOLDER = new ThreadLocal();

	// Persistant state
	private long m_millis;
/**
 * This is where all constructors should end, ideally.
 *
 * PENDING: Should accuracy be seconds or milliseconds?
 * In the future it will be milliseconds, unless we want
 * seconds ... :-)
 *
 * @author Markus Persson 2000-05-18
 */
private CoTimeStamp(long millis) {
	m_millis = millis;
}
public CoTimeStamp addDays(int days) {
	CoBetterCalendar cal = sharedCalendarWith(m_millis);
	cal.add(Calendar.DATE, days);
	return get(cal.getTimeInMillis());
}
public CoTimeStamp addMillis(long millis) {
	return get(m_millis + millis);
}
public final boolean after(long otherMillis) {
	return m_millis > otherMillis;
}
public final boolean after(CoTimeStamp otherStamp) {
	return after(otherStamp.getTimeInMillis());
}
public final boolean afterOrEquals(long otherMillis) {
	return m_millis >= otherMillis;
}
public final boolean afterOrEquals(CoTimeStamp otherStamp) {
	return afterOrEquals(otherStamp.getTimeInMillis());
}
private final Date asSharedLegacyDate() {
	return sharedLegacyDateWith(m_millis);
}
public final boolean before(long otherMillis) {
	return m_millis < otherMillis;
}
public final boolean before(CoTimeStamp otherStamp) {
	return before(otherStamp.getTimeInMillis());
}
public final boolean beforeOrEquals(long otherMillis) {
	return m_millis <= otherMillis;
}
public final boolean beforeOrEquals(CoTimeStamp otherStamp) {
	return beforeOrEquals(otherStamp.getTimeInMillis());
}
public Object clone() {
	try {
		return super.clone();
	} catch (CloneNotSupportedException e) {
		throw new InternalError("Clone not supported!");
	}
}
private static final int compare(long first, long second) {
	return (first < second ? -1 : (first == second ? 0 : 1));
}
public final int compareTo(long otherMillis) {
	return compare(m_millis, otherMillis);
}
public final int compareTo(Object other) {
	return compareTo((CoTimeStamp) other);
}
public final int compareTo(CoTimeStamp otherStamp) {
	return compare(m_millis, otherStamp.getTimeInMillis());
}
public CoTimeStamp copy() {
	return (CoTimeStamp) clone();
}
public final String format() {
	return format(DateFormat.DEFAULT);
}
/**
 * PENDING: Implement better by using the patterns in the formatter!
 *
 * @author Markus Persson 2000-06-16
 */
public final String format(int style) {
	if (style < 4) {
		String[] formatPatterns = CoDayResources.getNames(TIMESTAMP_FORMAT_PATTERNS);
		return sharedFormatterWith(formatPatterns[style]).format(asSharedLegacyDate());
	} else {
		return "Invalid formatting style to CoTimeStamp!";
	}
}
public final String format(String pattern) {
	return sharedFormatterWith(pattern).format(asSharedLegacyDate());
}
public static final CoTimeStamp get(int year, int isoMonth, int dayOfMonth, int hrs, int min) {
	return get(year, isoMonth, dayOfMonth, hrs, min, 0);
}
public static final CoTimeStamp get(int year, int isoMonth, int dayOfMonth, int hrs, int min, int sec) {
	return get(sharedCalendarWith(year, isoMonth, dayOfMonth, hrs, min, sec).getTimeInMillis());
}
public static final CoTimeStamp get(long millis) {
	return new CoTimeStamp(millis);
}
public static final CoTimeStamp get(Date legacyDate) {
	return get(legacyDate.getTime());
}
/**
 * @return number of millisecs since 1970-01-01.
 */
public final long getTimeInMillis() {
	return m_millis;
}
public final CoTimeStamp nextMidnight() {
	return get(nextMidnightAfter(m_millis));
}
/**
 * Static method when an object is unneccessary.
 */
public static final long nextMidnightAfter(long millis) {
	CoBetterCalendar cal = sharedCalendarWith(millis);

	/* set() is better than clear(). clear() will set field timestamps
	 * to UNSET, which is especially dangerous for HOUR_OF_DAY since
	 * the old information is retained in the AM_PM and HOUR fields.
	 */
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	cal.add(Calendar.DATE, 1);
	return cal.getTimeInMillis();
}
public static final CoTimeStamp now() {
	return get(System.currentTimeMillis());

}
public static final long nowMillis() {
	return System.currentTimeMillis();

}
public static final CoTimeStamp nowPlus(long millisDelta) {
	return get(System.currentTimeMillis() + millisDelta);

}
/**
 * Parse time stamps in the locale neutral format produced by
 * the stringExport() method. This is for XML and similar.
 *
 * NOTE: The parseStrict() method first tries to parse
 * with THIS method too. Upon failure, it tries the
 * locale variants. By using that method, other formats
 * could be parsed. While this seems to be good, it
 * may cause "bad" XML to be spread, much like when the
 * early HTML parsers accepted almost anything.
 * PENDING: Decide what to use in XML parsing ...
 *
 * @return the time stamp or null if it couldn't be parsed.
 * @author Markus Persson 2000-08-09
 */
public static final CoTimeStamp parseExported(String timeStamp) {
	if ((timeStamp == null) || (timeStamp.length() == 0)) {
		return null;
	} else {
		// PENDING: Allow parsing of strings without millis?
		Date legacyDate = sharedFormatterWith(TIMESTAMP_EXPORT_PATTERN).parse(timeStamp, new ParsePosition(0));
		return (legacyDate != null) ? get(legacyDate) : null;
	}
}
/**
 * This method is part of the migration to CoDay. See class comment.
 */
protected static final CoBetterCalendar sharedCalendar() {
	CoBetterCalendar calendar = (CoBetterCalendar) CALENDAR_HOLDER.get();
	if (calendar == null)
		CALENDAR_HOLDER.set(calendar = new CoBetterCalendar());

	return calendar;
}
protected static final CoBetterCalendar sharedCalendarWith(int year, int isoMonth, int dayOfMonth, int hour, int minute, int second) {
	CoBetterCalendar calendar = (CoBetterCalendar) CALENDAR_HOLDER.get();
	if (calendar == null) {
		CALENDAR_HOLDER.set(calendar = new CoBetterCalendar(year, isoMonth - 1, dayOfMonth, hour, minute, second));
	} else {
		calendar.clear();
		calendar.set(year, isoMonth - 1, dayOfMonth, hour, minute, second);
	}

	return calendar;
}
/**
 * This method is part of the migration to CoDay. See class comment.
 */
protected static final CoBetterCalendar sharedCalendarWith(long millis) {
	CoBetterCalendar calendar = (CoBetterCalendar) CALENDAR_HOLDER.get();
	if (calendar == null)
		CALENDAR_HOLDER.set(calendar = new CoBetterCalendar());
	calendar.setTimeInMillis(millis);

	return calendar;
}
private static final SimpleDateFormat sharedFormatterWith(String pattern) {
	SimpleDateFormat formatter = (SimpleDateFormat) FORMATTER_HOLDER.get();
	if (formatter == null) {
		FORMATTER_HOLDER.set(formatter = new SimpleDateFormat(pattern));
	} else {
		formatter.applyPattern(pattern);
	}

	return formatter;
}
private static final Date sharedLegacyDateWith(long millis) {
	Date date = (Date) LEGACY_DATE_HOLDER.get();
	if (date == null) {
		LEGACY_DATE_HOLDER.set(date = new Date(millis));
	} else {
		date.setTime(millis);
	}

	return date;
}
/**
 * Return locale neutral complete string representation. Complete
 * in the sense that when the class is known, all state required
 * to recreate the object should be contained in the string.
 *
 * This method is the only supported way of obtaining such a string
 * representation when exporting to XML and similar.
 *
 * NOTE: The returned string should be fully humanly readable.
 * (This is a XML design goal.) However, for end user display,
 * use format() instead since it takes locale into account.
 *
 * @see com.bluebrim.base.shared.CoDate#parseExported(String)
 * @see #format()
 * @author Markus Persson 2000-08-09
 */
public final String stringExport() {
	// PENDING: Only output millis if != 0?
	return sharedFormatterWith(TIMESTAMP_EXPORT_PATTERN).format(asSharedLegacyDate());
}
/**
 * NOTE: Legacy method from com.bluebrim.base.shared.CoDate. Rewrite or (probably) remove! /Markus
 *
 * Returns the day of the week as a string
 * @param bLongForm SHORT_DOW - 0, CHAR3_DOW - 1, LONG_DOW - 2
 * @return the day of the week as a string in one of the following forms
 *         SHORT_DOW - one or two character abbreviation ie. M
 *         CHAR3_DOW - three character abbreviation ie. Mon
 *         LONG_DOW - the full length word for the day of the week ie. Monday
 */
public String toDayOfWeekString(int format) {
	switch(format) {
		case SHORT_DOW:
			return format("EEE").substring(0,1).toUpperCase();
		case CHAR3_DOW:
			return format("EEE");
		case LONG_DOW:
		default:
			return format("EEEE");
	}
}
public final String toString() {
	return format();
}
/**
 * NOTE: Legacy method from com.bluebrim.base.shared.CoDate. Rewrite or (probably) remove! /Markus
 *
 * Return time in string form, conditionaly as a 12 or 24 hour time
 * @param b24Hour true to create string as a 24 hour time, false to use the 12 hour AM/PM format
 * @return the time string
 */
public String toTimeString(boolean b24Hour) {
	return format("HH:mm:ss");
}
}
