package com.bluebrim.page.shared;

import java.util.List;

/**
 * Implemented by objects that represents a page that has different layers. This feature
 * is for example used for pages that is composed of one layer for pagination and one layer
 * for the page content. When this kind of page is opened in the layout editor the user
 * has the possible to select one of layers as active layer where the gestures has effect.
 * Non active layers is visible but locked for manipulation. <br>
 * Since this inteface is implemted by objects that owns <code>CoPage</code>'s it also
 * extends <code>CoPageContext</code> because implementors of <code>CoPage</code>'s expects 
 * that their parents are <code>CoPageContext</code>.
 *  
 * @author Göran Stäck 2002-09-18
 *
 */
public interface CoLayeredPage extends CoPage, CoPageContext {
	public List getLayouts();
	
	public CoPage addLayer(CoPage page);
	
	public List getLayers(); // [ CoPage ]

}
