package com.bluebrim.base.client.datatransfer;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.InvalidDnDOperationException;

import com.bluebrim.gui.client.CoAutomaticUIKit;
import com.bluebrim.swing.client.CoListSelection;

/**
 * Utility class for drag-and-drop and (sometime in the future) clipboard handling.
 *
 * Data transfer operations are pretty simple. However, to avoid code duplication
 * and massive subclassing simple mechanisms are provided here. This class is also a
 * suitable place to provide workarounds for the limitations of various JDK versions.
 *
 * NOTE: Do not subclass or instantiate this class. We do not want the old mess of
 * CoDnDManager.
 *
 * @author Markus Persson 2001-09-18
 */
public final class CoDataTransferKit {
	public static final Object[] ZERO_OBJECTS = new Object[0];
	public static final DataFlavor DUMMY_FLAVOR = DataFlavor.stringFlavor;
	public static final String DUMMY_FLAVOR_RETURN_VALUE = "Just a dummy value to workaround a bug ...";

	// Special flavor to separate arrays of strings from strings.
	// A better generic solution with a x-java-jvm-local-objectarrayref
	// MIME-type will come, if possible. /Markus
	public static final DataFlavor STRING_ARRAY_FLAVOR = localFlavor(String.class, "Object array with Unicode strings");

	/**
	 * Data flavor extension that also takes the "human presentable name"
	 * into account when comparing for equality, thus making it possible
	 * to create data flavors more narrow than the representation class.
	 *
	 * I'm not sure this is entirely good, but it is used in some places
	 * (insertion requests,  some distribution adresses and a third place
	 * I can't remember right now).
	 *
	 * Note that the equals comparision with "normal" data flavors has
	 * been made symmetric, as is required by the equals contract. This
	 * has undesired implications that is best avoided by using this class
	 * throughout and not with mime-type/representation-class combinations
	 * that could be generated somewhere else.
	 *
	 * @author Markus Persson 2001-09-19
	 */
	private static class NarrowDataFlavor extends DataFlavor {
		public NarrowDataFlavor(Class type, String description) {
			super(type, description);
		}

		public NarrowDataFlavor(String mimeType, String description) {
			super(mimeType, description);
		}

		public boolean equals(DataFlavor dataFlavor) {
			if (dataFlavor instanceof NarrowDataFlavor) {
				return super.equals(dataFlavor) && getHumanPresentableName().equals(dataFlavor.getHumanPresentableName());
			} else {
				return super.equals(dataFlavor);
			}
		}
	}

	/**
	 * Do not subclass or instantiate this class. We do not want the old mess of
	 * CoDnDManager.
	 */
	private CoDataTransferKit() {
	}

	/**
	 * Factory method for creating local JVM flavors. This is the kind of
	 * flavors that normally should be used for data transfer within the
	 * application.
	 *
	 * NOTE: For unknown reason at least the JRE in VAJ3.5.3 seem to require
	 * at least one serializable flavor with a serializable representation
	 * class in transferables for a drop to succeed. This flavor can be ignored.
	 */
	public static DataFlavor domainFlavor(Class type, String description) {
		return localFlavor(type, description);
	}

	public static void dragUsing(DragGestureEvent e, Object[] rawSelection, CoDnDDataProvider dataProvider) {
		Transferable transferable = null;

		if (dataProvider != null) {
			transferable = dataProvider.getTransferableFor(rawSelection);
		} else {
			transferable = getTransferableFor(rawSelection);
		}

		if (transferable != null) {
			try {
				e.startDrag(null, transferable, new CoSimpleDragSourceListener());
			} catch (InvalidDnDOperationException idoe) {
				System.out.println(idoe);
			}
		}
	}

	public static void dragUsing(DragGestureEvent e, Object singleSelection, CoDnDDataProvider dataProvider) {
		// PENDING: Add different method to data provider for single selections? /Markus
		if (singleSelection != null) {
			dragUsing(e, new Object[] { singleSelection }, dataProvider);
		}
	}

	/**
	 * This works around the problem with drop first found in VAJ3.5.
	 *
	 * @see #domainFlavor()
	 * @author Markus Persson 2001-09-19
	 */
	public static DataFlavor[] fixFlavors(DataFlavor[] flavors) {
		return flavors;
		// Bug no longer present in JDK 1.4? /Markus 2002-09-03
/*
		if (flavors != null) {
			int size = flavors.length;
			for (int i = 0; i < size; ++i) {
				if (flavors[i].isFlavorSerializedObjectType()) {
					// Already contains a flavor of the type that seems
					// to be required. Simply return input.
					return flavors;
				}
			}
			// We need to add our dummy flavor. Note that transferables
			// should support this flavor by returning some string.
			DataFlavor[] newFlavors = new DataFlavor[size + 1];
			System.arraycopy(flavors, 0, newFlavors, 0, size);
			newFlavors[size] = DUMMY_FLAVOR;
			return newFlavors;
		} else {
			return null;
		}
*/
	}

	/**
	 * This works around the problem with drop first found in VAJ3.5.
	 *
	 * @see #domainFlavor()
	 * @author Markus Persson 2001-09-19
	 */
	private static DataFlavor[] fixFlavors(DataFlavor flavor) {
		return new DataFlavor[] { flavor };
		// Bug no longer present in JDK 1.4? /Markus 2002-09-03
/*
		if (flavor.isFlavorSerializedObjectType()) {
			// Is a flavor of the type that seems to be required.
			return new DataFlavor[] { flavor };
		} else {
			// We need to add our dummy flavor. Note that transferables
			// should support this flavor by returning some string.
			return new DataFlavor[] { flavor, DUMMY_FLAVOR };
		}
*/
	}

	/**
	 * Automatically create a suitable transferable for the given objects.
	 * This is primarily intended for calvin domain objects.
	 *
	 * To add support for your own types, for now edit CoAutomaticUIKit.
	 * The implementation of this mechanism will change both due to
	 * improved handling and due to module separation.
	 */
	public static Transferable getTransferableFor(Object[] objects) {
		if ((objects == null) || (objects.length == 0)) {
			return null;
		}

		// Primitive for now (reflecting old usage) ...
		if (objects.length >= 1) {
			DataFlavor[] flavors = CoAutomaticUIKit.getFlavorsFor(objects[0]);
			if (flavors != null) {
				// NOTE: Bad name for this class. (Not too good a class either ...)
				return new CoListSelection(objects, flavors);
			}
		}

		return new CoListSelection(objects);
	}

	/**
	 * Create a suitable transferable for the given objects using the flavors
	 * provided. This is primarily intended as a preliminary method until the
	 * entire dnd usage has been reworked.
	 */
	public static Transferable getTransferableForUsing(Object[] objects, DataFlavor[] flavors) {
		if ((objects == null) || (objects.length == 0)) {
			return null;
		}

		return new CoListSelection(objects, fixFlavors(flavors));
	}

	/**
	 * Create a suitable transferable for the given objects using the single
	 * flavor provided. This is primarily intended as a preliminary method
	 * until the entire dnd usage has been reworked.
	 */
	public static Transferable getTransferableForUsing(Object[] objects, DataFlavor flavor) {
		if ((objects == null) || (objects.length == 0)) {
			return null;
		}

		return new CoListSelection(objects, fixFlavors(flavor));
	}

	/**
	 * Create a suitable transferable for the given objects using the flavor
	 * key provided. This is primarily intended as a preliminary method until
	 * the entire dnd usage has been reworked.
	 */
	public static Transferable getTransferableForUsing(Object[] objects, String factoryKey) {
		if ((objects == null) || (objects.length == 0)) {
			return null;
		}

		return new CoListSelection(objects, CoAutomaticUIKit.getFlavorsForKey(factoryKey));
	}

	/**
	 * Factory method for creating local JVM flavors. This is the kind of
	 * flavors that normally should be used for data transfer within the
	 * application.
	 *
	 * NOTE: For unknown reason at least the JRE in VAJ3.5.3 seem to require
	 * at least one serializable flavor with a serializable representation
	 * class in transferables for a drop to succeed. This flavor can be ignored
	 */
	public static DataFlavor localFlavor(Class type, String description) {
		return new NarrowDataFlavor(DataFlavor.javaJVMLocalObjectMimeType + "; class=" + type.getName(), description);
	}

	/**
	 * This method reduces two lines of code to one. Not much, but ...
	 *
	 * ... this is a possible place to hook in a DragSource with a customized
	 * DragSourceContext to simulate drag images using cursors on Win32 platforms.
	 */
	public static void setupDragGestureRecognizer(Component comp, int permittedActions, DragGestureListener dgl) {
		DragSource dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(comp, permittedActions, dgl);
	}
}