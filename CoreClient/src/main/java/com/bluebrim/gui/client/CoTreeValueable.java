package com.bluebrim.gui.client;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
/**
 	Interfaceklass för de värdeklasser som jobbar mot en trädvy och
 	innehåller trädets rotobjekt.<br>
 	Ärver TreeModel.
 	@see CoTreeValueHolder.
 	@see CoTreeAspectAdaptor.
 	@author Lasse Svadaängs 971105
 */
public interface CoTreeValueable {
void addEnableDisableListener( CoEnableDisableListener l );
/**
 * @param l CoListSelectionListener
 */
public void addTreeSelectionListener ( TreeSelectionListener l);
/**
 * @param l CoSelectionListener
 */
public void eachChildDo (Object startElement,CoTreeElementVisitor iterator);
/**
 * @param l CoSelectionListener
 */
public void eachChildDo (Object startElement,CoTreeElementVisitor iterator, boolean parentBeforeChildren);
public TreePath getPathToRootFrom(JTree tree, Object element);
/**
 */
public CoAbstractTreeModel getTreeModel ();
boolean isEnabled();
/**
 */
public void reload ();
/**
 */
public void reload (TreePath path);
void removeEnableDisableListener( CoEnableDisableListener l );
/**
 * @param l CoSelectionListener
 */
public void removeTreeSelectionListener ( TreeSelectionListener l);
void setEnabled( boolean e );
}
