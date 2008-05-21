package com.bluebrim.time.shared;

import java.util.*;

/**
 * Simple subclass to GregorianCalendar for the purpose of minimizing
 * overhead when performing conversion between milliseconds since 1970
 * and year, month, day of month. Conversions that for sake of future
 * compatability is best handled by GregorianCalendar. (Mostly with
 * respect to daylight savings that may change in the future.)
 *
 * Although this overhead becomes less significant as more suitable
 * classes for representing dates is adopted, the excessively
 * restrictive API of Calendar is wasteful and has therefore been
 * slightly improved.
 *
 * @author Markus Persson 2000-05-22
 */
public class CoBetterCalendar extends GregorianCalendar {
public CoBetterCalendar() {
	super();
}
public CoBetterCalendar(int year, int month, int date) {
	super(year, month, date);
}
public CoBetterCalendar(int year, int month, int date, int hour, int minute) {
	super(year, month, date, hour, minute);
}
public CoBetterCalendar(int year, int month, int date, int hour, int minute, int second) {
	super(year, month, date, hour, minute, second);
}
public CoBetterCalendar(Locale locale) {
	super(locale);
}
public CoBetterCalendar(TimeZone zone) {
	super(zone);
}
public CoBetterCalendar(TimeZone zone, Locale locale) {
	super(zone, locale);
}
/**
 * Gets this Calendar's current time as a long.
 *
 * This method has been made public for reasons described in
 * the class comment.
 *
 * @return the current time as UTC milliseconds from the epoch.
 * @author Markus Persson 2000-05-22
 */
public long getTimeInMillis() {
	return super.getTimeInMillis();
}
/**
 * Sets this Calendar's current time from the given long value.
 *
 * This method has been made public for reasons described in
 * the class comment.
 *
 * @param date the new time in UTC milliseconds from the epoch.
 * @author Markus Persson 2000-05-22
 */
public void setTimeInMillis(long millis) {
	super.setTimeInMillis(millis);
}
}
