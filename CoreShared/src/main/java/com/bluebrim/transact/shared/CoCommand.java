package com.bluebrim.transact.shared;

import javax.swing.*;

/**
 * 	Abstrakt utvidgning av <code>CoAbstractCommand</code> som i <code>execute</code> 
 * 	definierar en algoritm för kommndoexekvering genom att använda 
 * 	"composed method pattern" (Beck: Smalltalk Best Practice Patterns) :
 * 	<pre>
 *		prepare();
 *		if (doExecute())
 *			finish();
 *		else
 *			abort();
 * 	</pre>
 * 	Tanken är att <code>prepare</code> skall implementera det som behöver göras innan kommandot exekveras,
 *	<code>doExecute</code> utför själva kommandot och returnerar true om det gick bra,
 *	<code>finish</code> avslutar kommandot om det gick bra medans <code>abort</code> städar 
 *	upp om kommandot inte kunde utföras.
 */
public abstract class CoCommand extends CoAbstractCommand {
	public CoCommand(String name) {
		super(name);
	}
	public CoCommand(String name, Icon icon) {
		super(name, icon);
	}
	
	/**
	 * Som default gör <code>abort</code> ingenting.
	 * Omimplementeras vid behov i subklassen.
	 */
	public void abort() {
	}
	public abstract boolean doExecute();

	public void execute() {
		prepare();
		if (doExecute())
			finish();
		else
			abort();
	}

	public void finish() {
	}

	public void prepare() {
	}
}