package com.bluebrim.menus.client;

/**
 * There is a lot of subclassing merely to change some menus.
 *
 * I find this annoying and inflexible for many reasons. A few
 * being that it prevents UI instance reuse and dynamic UI
 * composition.
 *
 * This interface and implementation is a quick solution since
 * I have a few other things to do before my vacation. There
 * really should be some more modelling here.
 *
 * If this doesn't make any sense to you, ask me! I don't have
 * time to explain it all now.
 *
 * @author Markus Persson 2000-06-15
 */
public interface CoMenuGenerator {
/**
 * Generate menu.
 *
 * A quick thought regarding menu builders: Wouldn't it
 * be better with separate builders for normal and popup
 * menus? Or with a builder that automatically created
 * the right type of items?
 */
public void generateWith(CoPopupMenu menu, CoMenuBuilder builder);
}
