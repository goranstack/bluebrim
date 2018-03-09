package com.bluebrim.postscript.impl.server.drawingops;
import com.bluebrim.postscript.impl.shared.*;

/**
 * A drawing operation represents (most of the time) a method call in the CoPaintable API.
 * A CoDrawingOperation is used as a "middle format" during Postscript generation. It
 * holds all the argument data associated with the operation and can generate appropriate
 * postscript code, and also do whatever is necessary during the first phase (the "preparePostscript 
 * phase") in the two-phase generation.
 *
 * <p><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Göran Hultgren (original author)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see com.bluebrim.postscript.impl.shared.CoPostscriptPaintable
 */
public abstract class CoDrawingOperation {
public abstract void generatePostscript(CoPostscriptHolder psHolder);


public void preparePostscript(CoPostscriptHolder psHolder) throws CoPostscriptException {
}
}