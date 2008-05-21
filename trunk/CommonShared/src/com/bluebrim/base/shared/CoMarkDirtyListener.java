package com.bluebrim.base.shared;

/**
 * Implemented by objects with components who calls the owners
 * markDirty-method when they change. In some cases this was accomplished
 * by inner subclasses but that solution was hard to combine with the
 * XML-framework because there is no reference to the owner in the factory method:
 * <pre>
 *   public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context);
 * </pre>
 * when a list of components is exported as a named collection of objects.
 * But this interface is also a bad solution because we expose the markDirty method
 * in the owner. Some remodeling of the XML framework and/or the notification by
 * markDirty design should solve the problem. <br>
 * (If this interface will last just for a while, we must think of a better name.)  
 *  
 * @author Göran Stäck
 *
 */
public interface CoMarkDirtyListener {
	void markDirty();
}
