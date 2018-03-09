package com.bluebrim.content.server;

import com.bluebrim.content.shared.CoAtomicContentIF;
import com.bluebrim.system.shared.CoGOI;

/**
 * Abstract super class for objects that can be part of a work piece. The purpose
 * of this class is to prevent recursive composition of work pieces since that is
 * not handled in <code>CoLayoutArea</code>. Work pieces only contains
 * atomic contents.
 * Creation date: (2000-10-27 15:12:16)
 * @author: Göran Stäck
 */
public abstract class CoAtomicContent extends CoContent implements CoAtomicContentIF  {


	public CoAtomicContent() {
		super(null);
	}

	public CoAtomicContent(CoGOI goi) {
		super( goi);
	}

	protected void prepareCopy(CoContent copy) {
		super.prepareCopy(copy);

		CoAtomicContent ac = (CoAtomicContent) copy;

	}


}