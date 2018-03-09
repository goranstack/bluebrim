package com.bluebrim.base.shared;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

import com.bluebrim.font.shared.*;

/**
 * CoScreenGraphics is an implementation of CoPaintable that paints on screen, using Java2D.
 * Most of the methods are simply delegated to a Graphics2D object, but some care are taken for
 * bug fixes in AWT and special requirements of the CoPaintable interface.
 *
 * Creation date: (2001-07-31 15:50:18)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 * @author Dennis
 */

public class CoScreenPaintable extends CoAbstractPaintable {
	private Graphics2D m_delegate;
	private static GraphicsConfiguration m_graphicsConfig = null;

	public static final RenderingHints.Key PAINTABLE_KEY = new RenderingHints.Key(0) {
		public boolean isCompatibleValue(Object p) {
			return (p == null) || (p instanceof CoScreenPaintable);
		}
	};

	protected CoScreenPaintable() {
		m_delegate = null;
	}
	protected CoScreenPaintable(Graphics2D delegate) {
		this();
		setDelegate(delegate);
	}
	public void addComment(String comment) {
		// Do nothing.
	}
	public void addRenderingHints(Map hints) {
		m_delegate.addRenderingHints(hints);
	}
	private static String c2s(char c) {
		return "" + c;
	}

	public void clip(Shape clip) {
		m_delegate.clip(clip);
	}
	public void draw(Shape shape) {
		m_delegate.draw(shape);
	}
	public void drawChar(char ch, float x, float y) {
		m_delegate.drawString(c2s(ch), x, y);
	}
	/**
	 * drawDecorationString method comment.
	 */
	public void drawDecorationString(String str, float x, float y, float fontSize) {
		drawDecorationString(str, x, y, m_delegate.getFont().deriveFont(fontSize));
	}
	public void drawDecorationString(String str, float x, float y, Font font) {
		Font f = m_delegate.getFont();

		m_delegate.setFont(font);
		m_delegate.drawString(str, x, y);

		m_delegate.setFont(f);
	}
	/**
	 * Only for testing. Should be removed.
	 */
	public void drawBufferedImage(BufferedImage image) {
//		m_delegate.drawImage(image, new AffineTransform(), null);
		m_delegate.drawImage(image, 0, 0, null, null);
	}

	/**
	 * A bug in JDK 1.5 cause major buffered image render speed regression.
	 * See: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5073407
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4942189
	 * We try to implement the proposed workaround
	 * 
	 * 	if (workaround) {
                    int w = (int) (500 * Trns.getScaleX());
                    int h = (int) (500 * Trns.getScaleY());
                    g2d.drawImage(Img, 0, 0, w, h, this);
		} else {
                    g2d.transform(Trns);
                    g2d.drawImage(Img, 0, 0, this);
		}
	 */
	public void drawBufferedImage(BufferedImage image, double scaleX, double scaleY) {
// This worked fine in jdk1.4
//		m_delegate.drawImage(image, AffineTransform.getScaleInstance(scaleX, scaleY), null); 
// Workaround for jdk 1.5
		AffineTransform trns = AffineTransform.getScaleInstance(scaleX, scaleY);
        int w = (int) (500 * trns.getScaleX());
        int h = (int) (500 * trns.getScaleY());
        m_delegate.drawImage(image, 0, 0, w, h, null);
        System.out.println("drawBufferedImage");
	}


	public void fill(Shape shape) {
		m_delegate.fill(shape);
	}

	public void finalize() {
		if (m_delegate != null)
			m_delegate.finalize();
	}

	public Shape getClip() {
		return m_delegate.getClip();
	}

	public Color getColor() {
		return m_delegate.getColor();
	}

	public Graphics2D getGraphics2D() {
		return m_delegate;
	}
	/**
	 * Returns the graphics configuration used by the screen.
	 * 
	 * @author: Helena Åberg <helena.aberg@appeal.se> (2001-09-27 16:00:22)
	 */
	private static GraphicsConfiguration getGraphicsConfiguration() {
		if (m_graphicsConfig == null)
			m_graphicsConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		return m_graphicsConfig;
	}

	public Paint getPaint() {
		return m_delegate.getPaint();
	}

	public Object getRenderingHint(RenderingHints.Key hintKey) {
		return m_delegate.getRenderingHint(hintKey);
	}

	public RenderingHints getRenderingHints() {
		return m_delegate.getRenderingHints();
	}

	public double getScale() {
		return CoBaseUtilities.getXScale(m_delegate.getTransform());
	}

	public Stroke getStroke() {
		return m_delegate.getStroke();
	}

	public void popClip(Shape clip) {
		m_delegate.setClip(clip);
	}

	public void popTransform() {
		// Do nothing
	}

	public Shape pushClip() {
		return m_delegate.getClip();
	}

	public void pushTransform() {
		// Do nothing
	}

	public void releaseDelegate() {
		setDelegate(null);
	}

	public void rotate(double theta, double x, double y) {
		m_delegate.rotate(theta, x, y);
	}

	public void scale(double scaleX, double scaleY) {
		m_delegate.scale(scaleX, scaleY);
	}

	public void setClip(Shape newClip) {
		m_delegate.setClip(newClip);
	}

	public void setColor(Color c) {
		m_delegate.setColor(c);
	}
	/**
	 * Declared final since it's called from constructor
	 * @param delegate
	 */
	public final void setDelegate(Graphics2D delegate) {
		if (m_delegate != null) {
			m_delegate.setRenderingHint(PAINTABLE_KEY, null);
		}

		m_delegate = delegate;

		if (m_delegate != null) {
			m_delegate.setRenderingHint(PAINTABLE_KEY, this);
		}
	}
	/**
	 * Set the current font using a CoFont.
	 *
	 * @author Markus Persson 2001-01-08
	 */
	public void setFont(CoFont font) {
		// Get the font from the server if not installed here
		CoAbstractFontMapper.getFontMapper().installAwtFontIfNeeded(font.getFace());

		m_delegate.setFont(font.getAwtFont());
	}

	public void setPaint(Paint paint) {
		m_delegate.setPaint(paint);
	}

	public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
		m_delegate.setRenderingHint(hintKey, hintValue);
	}

	public void setRenderingHints(Map hints) {
		m_delegate.setRenderingHints(hints);
	}

	public void setStroke(Stroke stroke) {
		m_delegate.setStroke(stroke);
	}

	/**
	 * This graphics work on graphical display, non printing
	 * returns false on supress paint.
	 */
	public boolean supressPaint(boolean supressPrintout) {
		return false;
	}
	public String toString() {
		return super.toString() + "[" + m_delegate.toString() + "]";
	}
	public void translate(double x, double y) {
		m_delegate.translate(x, y);
	}
	/**
	 * @author Jörgen Swensån (2000-05-25 14:20:42)
	 * @param p0 double
	 * @param p1 double
	 * @param p2 double
	 */
	public void unrotate(double theta, double x, double y) {
		m_delegate.rotate(-theta, x, y);
	}
	/**
	 * @author Jörgen Swensån (2000-05-25 14:11:26)
	 * @param p1 double
	 * @param p0 double
	 */
	public void unscale(double scaleX, double scaleY) {
		m_delegate.scale(1 / scaleX, 1 / scaleY);
	}
	/**
	 * @author Jörgen Swensån (2000-05-25 14:09:12)
	 * @param p0 double
	 * @param p1 double
	 */
	public void untranslate(double x, double y) {
		m_delegate.translate(-x, -y);
	}
	/**
	 * Wraps a Graphics2D in a CoPaintable.
	 * <b>IMPORTANT!</b> All calls to wrap MUST be followed by a call to releaseDelegate,
	 * when all graphics operations on the Graphics2D is done.
	 */
	public static CoScreenPaintable wrap(Graphics g) {
		return wrap((Graphics2D) g);
	}
	/**
	 * Wraps a Graphics2D in a CoPaintable.
	 * <b>IMPORTANT!</b> All calls to wrap MUST be followed by a call to releaseDelegate,
	 * when all graphics operations on the Graphics2D is done.
	 */
	public static CoScreenPaintable wrap(Graphics2D g) {
		return new CoScreenPaintable(g);
	}

}