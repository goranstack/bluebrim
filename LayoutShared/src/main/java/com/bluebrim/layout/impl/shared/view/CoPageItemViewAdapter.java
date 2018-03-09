package com.bluebrim.layout.impl.shared.view;


import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;

/**
 * 
 * 
 * @author: Dennis Malmström
 */

public interface CoPageItemViewAdapter
{


// delegate to wrapped view

public abstract Container getContainer( CoShapePageItemView view );

public abstract void postPaint( CoShapePageItemView view, CoPaintable g, Rectangle bounds );

public abstract void prePaint( CoShapePageItemView view, CoPaintable g, Rectangle bounds );

public abstract void prepareAffineTransformFor( AffineTransform a, CoShapePageItemView view );

public abstract void prepareHitTestGraphicsFor( Graphics2D g, CoShapePageItemView view );

public abstract void transformFromParent( CoShapePageItemView view, Point2D p );

public abstract void transformToParent( CoShapePageItemView view, Point2D p );
}