package com.bluebrim.swing.client;
/**
 * Interface f�r de textkomponenter varifr�n 
 * en drag & drop operation kan startas.
 */
public interface CoDnDTextComponentIF {
public CoTextDragSourceListener getDragSourceListener();
public void setDragSourceListener(CoTextDragSourceListener listener);
public boolean isSelecting();
}