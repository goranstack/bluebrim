package com.bluebrim.base.shared;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

import com.bluebrim.font.shared.*;

/**
 * Interface for a paintable surface with all drawing operations used by the layout editor. This is a
 * in part a subset of Graphics2D, but some methods are "new". The intention is that all references to 
 * Graphics2D should be removed, which in turn will free us from the requirement of having method 
 * compatibility with Graphics2D. But as long as this is an requirement, some of the methods will look 
 * a bit strange and not really optimized for the use that the layout editor makes of them.
 *
 * Creation date: (2001-07-31 15:56:39)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

public interface CoPaintable {
	public void addComment(String comment); // not (yet) called from page item rendering code

	void addRenderingHints(Map hints);

	public void clip(Shape clip);

	public void draw(Shape shape);

	public void drawChar(char ch, float x, float y); // called from text rendering code

	void drawDecorationString(String str, float x, float y, float fontSize);

	void drawDecorationString(String str, float x, float y, Font font);

	public void fill(Shape shape);

	public Shape getClip(); // I want to remove this. Magnus Ihse (magnus.ihse@appeal.se)

	public Color getColor(); // deprecated; replace with getPaint()

	Graphics2D getGraphics2D();

	public Paint getPaint(); // I want to remove this. Magnus Ihse (magnus.ihse@appeal.se)

	public Object getRenderingHint(RenderingHints.Key hintKey);

	RenderingHints getRenderingHints();

	double getScale();

	public Stroke getStroke(); // I want to remove this. Magnus Ihse (magnus.ihse@appeal.se)

	public void popClip(Shape clip); // I want to remove this. Magnus Ihse (magnus.ihse@appeal.se)

	public void popTransform(); // I want to remove this. Magnus Ihse (magnus.ihse@appeal.se)

	public Shape pushClip(); // I want to remove this. Magnus Ihse (magnus.ihse@appeal.se)

	public void pushTransform(); // I want to remove this. Magnus Ihse (magnus.ihse@appeal.se)

	public void rotate(double theta, double x, double y);

	public void scale(double scaleX, double scaleY);

	public void setClip(Shape newClip); // called from page item rendering code (workaround for awt bug when rendering text)

	public void setColor(Color color); // deprecated; replace with setPaint(Paint)

	public void setFont(CoFont font); // called from text rendering code

	public void setPaint(Paint paint);

	public void setRenderingHints(Map hints);

	public void setStroke(Stroke stroke);

	public boolean supressPaint(boolean supressPrintout); // needed why?

	public void translate(double x, double y);

	public void unrotate(double theta, double x, double y);

	public void unscale(double scaleX, double scaleY);

	public void untranslate(double x, double y);

	public void drawBufferedImage(BufferedImage image);

	public void drawBufferedImage(BufferedImage image, double scaleX, double scaleY);

	void setRenderingHint( RenderingHints.Key hintKey, Object hintValue );
}