package com.bluebrim.content.shared;

/**
 * <code>CoWorkPieceProjector</code> is implemented by objects that has the ability
 * to project work pieces in a specified layout. <br>
 * This is an attempt to create an interface that can replace 
 * <code>CoLayoutAreaIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * layout domain. 
 * <br>
 * @author: Göran Stäck 2002-05-21
 */

public interface CoWorkPieceProjector {

	void distribute();

}
