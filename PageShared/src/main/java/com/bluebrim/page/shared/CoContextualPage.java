package com.bluebrim.page.shared;

/**
 * Implemented by pages that has a <code>CoPageContext</code>
 * @author Göran Stäck 2002-09-29
 *
 */
public interface CoContextualPage extends CoPage {
	
	public CoPageContext getPageContext();

}
