package com.bluebrim.layout.shared;
/**
 * The context used when editing a layout
 * <br>
 * This is an attempt to create an interface that can replace 
 * <code>CoPageItemEditorContextIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * layout domain. 
 * <br>
 * The working strategi is to replace all external references to code>CoPageItemEditorContextIF</code>
 * with this interface and add requested methods until everyone is happy.  
 * <br>
 * 
 * @author Göran Stäck 2002-04-22
 */
public interface CoLayoutEditorContext {
	
	String KEY = "CoLayoutEditorContext";
	
	public CoLayoutParameters getLayoutParameters();
	
}
