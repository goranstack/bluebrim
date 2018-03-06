package com.bluebrim.postscript.impl.shared;

import com.bluebrim.font.shared.*;
import com.bluebrim.postscript.impl.server.color.*;
import com.bluebrim.postscript.shared.*;

/**
 * Interface for a class capable of producing Postscript files, containing methods called from
 * the DrawingOperations family.
 *
 * <p><b>Creation date:</b> 2001-06-01
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public interface CoPostscriptHolder {
	public CoPostscriptWriter getSetupWriter();

	public CoPostscriptTarget getTarget();

	public CoPostscriptWriter getWriter();

	public void includeComment(String s);

	public void registerColorant(CoColorant colorant);

	public void registerFontFace(CoFontFace fontFace);

	public void registerLanguageLevel(int level);

	public void registerPostscriptFunction(String name, String definition, String description, String stackPreCond, String stackPostCond);

	public void registerTrappingUse();
}