package com.bluebrim.time.shared;

import com.bluebrim.base.shared.*;

/**
 * Interface for universal times, rather than descriptive  times.
 * The major difference is that universal times care about such
 * things as time zone and daylight savings. In other words,
 * universal times map directly towards UTC.
 *
 * NOTE: This is a work in progress.
 *
 * @author Markus Persson 2000-05-16
 */
public interface CoUniversalTime extends CoTimeConstants, CoStringExportable {
}
