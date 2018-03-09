package com.bluebrim.base.shared;

/**
 * Simple interface for anything with a status window
 * or similar.
 * 
 * @author Markus Persson 2002-04-26
 */
public interface CoStatusShower {
	public void showStatus(String currentStatus);
	
	public static class NullShower implements CoStatusShower {
		public void showStatus(String currentStatus) {
		}
	}

	public static class StdOutShower implements CoStatusShower {
		public void showStatus(String currentStatus) {
			System.out.println(currentStatus);
		}
	}
}
