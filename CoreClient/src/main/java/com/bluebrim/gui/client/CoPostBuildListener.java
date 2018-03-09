package com.bluebrim.gui.client;

/**
 * Interface för klasser som lyssnar efter event av typen
 * CoPostBuildEvent.
 * @author Lars Svadängs
 */
public interface CoPostBuildListener extends java.util.EventListener {
/**
 * @param anEvent CoPostBuildEvent
 */
public void postBuild( CoPostBuildEvent anEvent);
}
