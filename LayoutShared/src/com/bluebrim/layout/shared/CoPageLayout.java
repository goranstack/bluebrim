package com.bluebrim.layout.shared;

import com.bluebrim.page.shared.*;

/**
 * A <code>CoPageLayout</code> is a layout that is representing the
 * graphical page content on a page or a spread in a page based media.<br>
 * This is an attempt to create an interface that can replace 
 * <code>CoPageLayoutAreaIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * layout domain. 
 * <br>
 * 
 * @author: Göran Stäck 2002-05-22
 */

public interface CoPageLayout extends CoLayout {
	
	public void setPageSize( CoPageSizeIF pageSize );
	
	public void setPage( CoPage page );
	
	public CoPage getPage();
	
	public boolean isSpread();
	
	public void setSpread( boolean s );
	
}
