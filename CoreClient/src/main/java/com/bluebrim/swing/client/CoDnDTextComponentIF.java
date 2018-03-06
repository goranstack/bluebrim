package com.bluebrim.swing.client;
/**
 * Interface för de textkomponenter varifrån 
 * en drag & drop operation kan startas.
 */
public interface CoDnDTextComponentIF {
public CoTextDragSourceListener getDragSourceListener();
public void setDragSourceListener(CoTextDragSourceListener listener);
public boolean isSelecting();
}