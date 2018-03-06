package com.bluebrim.solitarylayouteditor;

import com.bluebrim.resource.shared.CoDefMenuItemResource;
import com.bluebrim.resource.shared.CoMenuItemResource;

/**
 * @author Göran Stäck 2002-12-05
 *
 */
public interface CoSolitaryLayoutEditorConstants {

	public static class MenuItem extends CoDefMenuItemResource {
		private static int NEXT_ORDINAL = 0;
		private MenuItem() {
			super(NEXT_ORDINAL++);
		}
		protected CoMenuItemResource obtainResource() {
			return CoSolitaryLayoutEditorResources.getMenuItem(this);
		}
	}

	MenuItem MENU_HELP_RELEASE_NOTES = new MenuItem();

}
