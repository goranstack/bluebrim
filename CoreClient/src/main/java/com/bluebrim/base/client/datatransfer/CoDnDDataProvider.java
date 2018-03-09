package com.bluebrim.base.client.datatransfer;
import java.awt.datatransfer.Transferable;

/**
 * Interface for providers of data in a Drag and Drop operation.
 *
 * Responsible for providing a transferable. In the future this may be
 * expanded to include an optional drag image and possibly other stuff.
 *
 * @author Markus Persson 2001-09-17
 */
public interface CoDnDDataProvider {
/**
 * Returning a transferable corresponding to the raw selection or null if
 * the operation (drag or cut/copy) for any reason should not be completed.
 *
 * The exact type of elements in the raw selection depends on the component
 * where the operation originated and its data model, both of which should
 * be known to implementors of this interface.
 *
 * After any unwrapping of the elements the transferable can normally be
 * obtained using CoDataTransferKit.getTransferableFor(Object[]).
 *
 *
 * NOTE: If the elements are not wrapped this method shrinks to the single line
 *
 *		return CoDataTransferKit.getTransferableFor(rawSelection);
 *
 * but a better way to achive the same thing is to not implement this interface
 * at all and instead pass null when preparing the drag source component. Only
 * if some other method of this interface needs to be implemented (for drag
 * images perhaps) should that one-liner be here.
 *
 * @author Markus Persson 2001-09-19
 */
public Transferable getTransferableFor(Object[] rawSelection);
}