package com.bluebrim.menus.client;

/**
 * @author Markus Persson 2000-06-15
 */
public class CoSimpleMenuGenerator implements CoMenuGenerator {
	private static CoMenuGenerator EMPTY_GEN;
public CoSimpleMenuGenerator() {
}
public void generateWith(CoPopupMenu menu, CoMenuBuilder builder) {
}
/**
 * Override this if you want to include the default menu within your menu.
 */
public void generateWithDefault(CoPopupMenu menu, CoMenuBuilder builder, CoMenuGenerator defGen) {
	generateWith(menu, builder);
}
public static CoMenuGenerator getEmptyGenerator() {
	if (EMPTY_GEN == null) {
		EMPTY_GEN = new CoSimpleMenuGenerator();
	}

	return EMPTY_GEN;
}
}
