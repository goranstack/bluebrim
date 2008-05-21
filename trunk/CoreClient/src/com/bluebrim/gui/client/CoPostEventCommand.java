package com.bluebrim.gui.client;

import java.awt.event.ActionEvent;

import com.bluebrim.base.client.command.CoEvent;
import com.bluebrim.transact.shared.CoAbstractCommand;

/**
	Abstrakt superklass för de kommandoklasser som postar sina event i 
	systemeventkön. Observera att detta inte går att göra hur som helst om 
	koden exekveras i en applet.
 */
public abstract class CoPostEventCommand extends CoAbstractCommand {
	public void actionPerformed(ActionEvent e) {
		execute();
	}
	protected abstract CoEvent createEvent();
	/**
	 * execute method comment.
	 */
	public void execute() {
		CoGUI.postSystemEvent(createEvent());
	}
}