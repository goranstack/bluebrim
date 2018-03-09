package com.bluebrim.content.shared;

import com.bluebrim.base.shared.CoNamed;
import com.bluebrim.image.shared.CoImageContentIF;
import com.bluebrim.layout.shared.CoLayoutContentIF;
import com.bluebrim.text.shared.CoTextContentIF;

/**
 * @author: Dennis
 */

public interface CoContentReceiver extends CoNamed {

	public boolean add(CoWorkPieceIF workPiece);

	public boolean add(CoImageContentIF imageContent);

	public boolean add(CoLayoutContentIF layoutContent);

	public boolean add(CoTextContentIF textContent);
}