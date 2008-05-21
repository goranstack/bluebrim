package com.bluebrim.transact.shared;

import javax.swing.*;

import com.bluebrim.base.shared.*;

/**
 	Det finns tv� abstrakta metoder definierade i CoCommand: 
 	<ul>
 	<li><code>execute</code> implementerar sj�lva algoritmen, dvs det som skall h�nda n�r kommdot exekveras
 	<li><code>getName</code> svarar med ett beskrivande namn p� kommandot.
 	</ul>
 	<code>CoAbstractCommand</code> �r en <code>AbstractAction</code> och implementerar 
 	<code>actionPeformed</code> s� att <code>execute</code> anropas.
 */
public abstract class CoAbstractCommand extends AbstractAction implements CoRenameable {
	public CoAbstractCommand() {
		super();
	}
	
	public CoAbstractCommand(String name) {
		super(name);
	}
	
	public CoAbstractCommand(String name, Icon icon) {
		super(name, icon);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		execute();
	}
	
	public abstract void execute();
	
	public String getName() {
		return (String) getValue(Action.NAME);
	}
	
	public boolean isRenameable() {
		return true;
	}
	
	public final void setName(String name) {
		putValue(Action.NAME, name);
	}
}