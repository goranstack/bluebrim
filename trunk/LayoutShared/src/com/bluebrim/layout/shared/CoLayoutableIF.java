package com.bluebrim.layout.shared;


/**
 * Any object that is to be laid out by the layout engine in CoLayoutSpec must implement this interface.
*/

public interface CoLayoutableIF
{
double getArea(); // the area of the layoutable
public CoImmutableColumnGridIF getColumnGrid();
double getContentHeight(); // height of layoutables content
double getContentWidth(); // height of layoutables content
boolean getDoRunAround();
public double getInteriorHeight(); // interior height (span between top and bottom margins) of layoutable
public double getInteriorWidth(); // interior width (span between left and right margins) of layoutable
public double getLayoutHeight(); // layoutables height if valid, 0 otherwise
CoLayoutableContainerIF getLayoutParent();

public double getLayoutWidth(); // layoutables width if valid, 0 otherwise
public double getLeftEdge(); // left edge position of the layoutable (in parents coordinate space)
public double getRightEdge(); // right edge position of the layoutable (in parents coordinate space)
public double getTopEdge(); // top edge position of the layoutable (in parents coordinate space)
double getBottomEdge(); // bottom edge position of the layoutable (in parents coordinate space)
public double getX();
public double getY();
boolean hasValidLayout(); // is layout valid
public void invalidateHeight(); // POSTCONDITION: layout height is invalid
public void invalidateWidth(); // POSTCONDITION: layout height is invalid
public void reshapeToContentHeight();
public void reshapeToContentWidth ();
void setLayoutHeight( double height ); // POSTCONDITION: layout height is valid
public void setLayoutLocation( double x, double y ); // POSTCONDITION: layout is valid
public void setLayoutSuccess( boolean b ); // declare the layot a success or failure
public void setLayoutWidth( double width ); // POSTCONDITION: layout width is valid
public void setLayoutX( double x ); // POSTCONDITION: layout is valid
public void setLayoutY( double y ); // POSTCONDITION: layout is valid

CoLayoutSpecIF getLayoutSpec();
}