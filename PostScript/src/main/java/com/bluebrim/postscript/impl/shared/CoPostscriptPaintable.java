package com.bluebrim.postscript.impl.shared;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.font.shared.*;
import com.bluebrim.image.server.*;
import com.bluebrim.postscript.impl.server.drawingops.*;
import com.bluebrim.stroke.shared.*;

/**
 * A recording implementation of CoPaintable, which records all paint operations as a list of
 * CoDrawingOperations, and which can return this list. In this way, a complete painting of a
 * CoPaintable can be stored for later processing, such as postscript generation.
 *
 * <p><b>Creation date:</b> 2001-05-30
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 * @author Dennis
 */
public class CoPostscriptPaintable extends CoAbstractPaintable {
	// m_operations is where all recorded operations are stored
	private java.util.List m_operations = new ArrayList(); // [CoDrawingOperation]

	// Internal graphics state
	private CoGraphicsState m_requestedState = new CoGraphicsState();
	private CoGraphicsState m_currentState = new CoGraphicsState();
	private Stack m_graphicsStateStack = new Stack(); // [CoGraphicsState]

	private Shape m_clip; // needed?

	private RenderingHints m_renderingHints = new RenderingHints(new HashMap());

	public void addComment(String comment) {
		addOperation(new CoCommentOp(comment));
	}

	private void addOperation(CoDrawingOperation op) {
		m_operations.add(op);
	}

	/**
	 * Does an actual clip. Note that clip(Shape) does _not_ change the locally stored clip,
	 * i.e. calling clip does not affect the result of getClip(). Only setClip(Shape) does
	 * that. On the other hand, setClip has no other effect - i.e. does NOT change clipping
	 * on the postscript device - than that.
	 *
	 * Creation date: (2001-08-08 12:56:17)
	 * @author Magnus Ihse (magnus.ihse@appeal.se)
	 */

	public void clip(Shape clip) {
		if (clip instanceof Rectangle2D) {
			// since most clipping is rectangular, emphasize this for better readability and higher efficiency
			addOperation(new CoRectangularClipOp((Rectangle2D) clip));
		} else {
			addOperation(new CoGenericClipOp(clip));
		}
	}

	public void draw(Shape shape) {
		setRequestedStrokeIfNeeded();
		setRequestedPaintIfNeeded();

		Stroke stroke = m_requestedState.getStroke();
		if ((stroke instanceof BasicStroke) || (stroke instanceof CoBasicStrokeProxy)) {
			addOperation(new CoDrawShapeWithBasicStrokeOp(shape));
		} else {
			addOperation(new CoDrawShapeWithGenericStrokeOp(shape, m_currentState.getStroke()));
		}
	}

	public void drawChar(char ch, float x, float y) {
		boolean paintChanged = setRequestedPaintIfNeeded();
		boolean fontChanged = setRequestedFontIfNeeded();

		// refactorize, move collecting algorithm from Op to here.

		if (fontChanged || paintChanged) { // Check if font has changed
			// if font or paint really has changed, just add the char as a new op
			addOperation(new CoDrawTextOp(ch, x, y));
		} else { // otherwise try to optimise chars on the same line
			// is last entry also a drawCharOp?
			CoDrawingOperation drawOp = (CoDrawingOperation) m_operations.get(m_operations.size() - 1);

			if (drawOp instanceof CoDrawTextOp) {
				// If it is text, test if it is on the same line...
				CoDrawTextOp previous = (CoDrawTextOp) drawOp;
				if (previous.sameLine(y)) {
					// then include this character too
					previous.addChar(ch, x);
				} else {
					addOperation(new CoDrawTextOp(ch, x, y));
				}
			} else {
				// just add it
				addOperation(new CoDrawTextOp(ch, x, y));
			}
		}
	}

	public void fill(Shape shape) {
		Paint paint = m_requestedState.getPaint();
		setRequestedPaintIfNeeded();

		if (paint instanceof Color) {
			addOperation(new CoFillShapeWithBasicPaintOp(shape));
		} else if (paint instanceof GradientPaint) {
			GradientPaint gradient = (GradientPaint) paint;
			if (gradient.isCyclic()) {
				addOperation(new CoFillShapeWithCyclicGradientPaintOp(shape, gradient));
			} else {
				addOperation(new CoFillShapeWithStaticGradientPaintOp(shape, gradient));
			}
		} else if (paint instanceof TexturePaint) {
			addOperation(new CoFillShapeWithTexturePaintOp(shape, (TexturePaint) paint));
		} else {
			CoAssertion.assertTrue(false, "Unsupported paint operation");
		}
	}

	public void popClip(Shape clip) {
		// FIXME: remove method
		popGraphicsState();
	}

	public void popTransform() {
		// FIXME: remove method
		popGraphicsState();
	}

	public Shape pushClip() {
		// FIXME: remove method
		pushGraphicsState();
		return null;
	}

	public void pushTransform() {
		// FIXME: remove method
		pushGraphicsState();
	}

	public void scale(double scaleX, double scaleY) {
		scaleX = CoPostscriptUtil.normalizeValue(scaleX, 1.0);
		scaleY = CoPostscriptUtil.normalizeValue(scaleY, 1.0);

		if (scaleX != 1.0 || scaleY != 1.0) { // only scale if necessary
			addOperation(new CoScaleOp(scaleX, scaleY));
		}
	}

	/**
	 * Changes the locally stored "clip", but does not change the actual clipping.
	 * Note: this operation should not really be allowed, but is used as a quick and
	 * dirty hack to get away with font rendering problems on screen. On postscript
	 * however, this could safely be ignored.
	 *
	 * Creation date: (2001-08-08 12:58:24)
	 * @author Magnus Ihse (magnus.ihse@appeal.se)
	 */

	public void setClip(Shape clip) {
		m_clip = clip;
	}

	public void setFont(CoFont font) {
		m_requestedState.setFont(font);
	}

	public void setPaint(Paint paint) {
		if (paint == null)
			return; // According to AWT Graphics2D spec
		m_requestedState.setPaint(paint);
	}

	public void setStroke(Stroke stroke) {
		m_requestedState.setStroke(stroke);
	}

	public void translate(double x, double y) {
		x = CoPostscriptUtil.normalizeValue(x, 0.0);
		y = CoPostscriptUtil.normalizeValue(y, 0.0);

		if (!(x == 0.0 && y == 0.0)) { // A translation of zero is of no use
			addOperation(new CoTranslateOp(x, y));
		}
	}

	public void unscale(double scaleX, double scaleY) {
		// Empty per design
	}

	public void untranslate(double x, double y) {
		// Empty per design
	}

	public void rotate(double theta, double x, double y) {
		theta = CoPostscriptUtil.normalizeValue(theta, 0.0);
		x = CoPostscriptUtil.normalizeValue(x, 0.0);
		y = CoPostscriptUtil.normalizeValue(y, 0.0);

		if (theta != 0.0) { // only rotate if necessary
			addOperation(new CoRotateOp(theta, x, y));
		}
	}

	public void unrotate(double theta, double x, double y) {
		// Empty per design
	}

	public Shape getClip() {
		return m_clip;
	}

	public java.util.List getOperations() {
		return m_operations;
	}

	public Paint getPaint() {
		return m_currentState.getPaint();
	}

	public Stroke getStroke() {
		return m_currentState.getStroke();
	}

	public boolean supressPaint(boolean supressPrintout) {
		// Allow suppress on printers
		// (Or??? This is weird, isn't it? Magnus Ihse (magnus.ihse@appeal.se) (2001-07-30 18:24:30))
		return supressPrintout;
	}

	protected void popGraphicsState() {
		addOperation(new CoPopGraphicsStateOp());
		m_currentState = (CoGraphicsState) m_graphicsStateStack.pop();
	}

	protected void pushGraphicsState() {
		addOperation(new CoPushGraphicsStateOp());
		m_graphicsStateStack.push(m_currentState);
		try {
			m_currentState = (CoGraphicsState) m_currentState.clone();
		} catch (CloneNotSupportedException ignored) {
		} // We know clone is supported in GraphicsState
	}

	/**
	 * Realizes the requested font, if it is different from the actual font in use. Returns true if
	 * the font is changed, and false if it is left untouched.
	 * Creation date: (2001-06-29 13:51:12)
	 * @param 
	 * @return 
	 * @author Magnus Ihse (magnus.ihse@appeal.se)
	 */

	private boolean setRequestedFontIfNeeded() {
		if (m_requestedState.getFont() == null)
			return false; // if no font is requested, do nothing
		if (!m_requestedState.getFont().equals(m_currentState.getFont())) { // is font changed?
			addOperation(new CoSetFontOp(m_requestedState.getFont()));
			m_currentState.setFont(m_requestedState.getFont());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Realizes the requested paint, if it is different from the actual paint in use. Returns true if
	 * the paint is changed, and false if it is left untouched.
	 * Creation date: (2001-06-29 13:51:12)
	 * @param 
	 * @return 
	 * @author Magnus Ihse (magnus.ihse@appeal.se)
	 */

	private boolean setRequestedPaintIfNeeded() {
		if (m_requestedState.getPaint() == null)
			return false; // if no paint is requested, do nothing
		if (!m_requestedState.getPaint().equals(m_currentState.getPaint())) { // is paint changed?
			if (m_requestedState.getPaint() instanceof Color) {
				// We want to change the current paint in postscript
				addOperation(new CoSetBasicPaintOp((Color) m_requestedState.getPaint()));
			}
			m_currentState.setPaint(m_requestedState.getPaint());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Realizes the requested stroke, if it is different from the actual stroke in use. Returns true if
	 * the stroke is changed, and false if it is left untouched.
	 * Creation date: (2001-06-29 13:51:12)
	 * @param 
	 * @return 
	 * @author Magnus Ihse (magnus.ihse@appeal.se)
	 */

	private boolean setRequestedStrokeIfNeeded() {
		Stroke requestedStroke = m_requestedState.getStroke();
		if (requestedStroke == null)
			return false; // if no stroke is requested, do nothing
		if (!requestedStroke.equals(m_currentState.getStroke())) { // is stroke changed?
			if (requestedStroke instanceof BasicStroke) {
				addOperation(new CoSetBasicStrokeOp((BasicStroke) requestedStroke));
			} else if (requestedStroke instanceof CoBasicStrokeProxy) {
				addOperation(new CoSetBasicStrokeOp((CoBasicStrokeProxy) requestedStroke));
			}
			m_currentState.setStroke(requestedStroke);
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return getClass().getName() + "[numRecorded=" + m_operations.size() + ", currentState=" + m_currentState + "]";
	}

	public CoPostscriptPaintable() {
		super();
	}

	public Object getRenderingHint(RenderingHints.Key hintKey) {
		return m_renderingHints.get(hintKey);
	}

	/**
	 * addRenderingHints method comment.
	 */
	public void addRenderingHints(java.util.Map hints) {
		m_renderingHints.add(new RenderingHints(hints));
	}

	public void drawDecorationString(java.lang.String str, float x, float y, float fontSize) {
		// Empty by design, should do nothing on the printer
	}

	public void drawDecorationString(java.lang.String str, float x, float y, java.awt.Font font) {
		// Empty by design, should do nothing on the printer
	}

	public Graphics2D getGraphics2D() {
		//	CoAssertion.assert( false, "Illegal call" );
		// This should not be the case? Huh? /Magnus Ihse (magnus.ihse@appeal.se) (2001-08-09 13:20:35)
		return null;
	}

	public RenderingHints getRenderingHints() {
		return m_renderingHints;
	}

	public double getScale() {
		return 1;
	}

	public void setRenderingHints(java.util.Map hints) {
		m_renderingHints = new RenderingHints(hints);
	}

	public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
		m_renderingHints.put(hintKey, hintValue);
	}

	public void drawEPSimage(CoEpsImage EPSimage) {
		addOperation(new CoDrawEpsImageOp(EPSimage.getPostScript(), EPSimage.getBoundingBox()));
	}

	public void drawBufferedImage(BufferedImage image) {
		addOperation(new CoDrawRenderableImageOp(image));
	}

	public void drawBufferedImage(BufferedImage image, double scaleX, double scaleY) {
		pushTransform();
		scale(scaleX, scaleY);
		drawBufferedImage(image);
		popTransform();		
	}
}