package com.bluebrim.layout.impl.shared;

//import com.bluebrim.layout.impl.server.text.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;

public class CoPageItemStringResources_sv extends CoPageItemStringResources
{
	static final Object[][] contents =
	{
		{CoPageItemImageContentIF.IMAGE_CONTENT,					"Bild"},
		{CoPageItemTextContentIF.TEXT_CONTENT,						"Text"},
		{CoPageItemWorkPieceTextContentIF.WORKPIECE_TEXT_CONTENT,	"Projiserande text"},			
		{CoPageItemNoContentIF.NO_CONTENT,							"Tom"},
		{CoPageItemLayoutContentIF.LAYOUT_CONTENT,					"Layout"},

		{CoLayoutAreaIF.LAYOUT_AREA,				"Layoutyta"},
		{CoDesktopLayoutAreaIF.DESKTOP_LAYOUT,		"Skrivbordsyta"},		
		{CoPageLayoutAreaIF.PAGE_LAYER_LAYOUT_AREA, "Sida"},		

		{ CoRectangleShapeIF.RECTANGLE_SHAPE, "Rektangel" },
		{ CoEllipseShapeIF.ELLIPSE_SHAPE, "Ellips" },
		{ CoRoundedCornerIF.ROUNDED_CORNER, "Runda h�rn" },
		{ CoBeveledCornerIF.BEVELED_CORNER, "Skurna h�rn" },
		{ CoConcaveCornerIF.CONCAVE_CORNER, "Konkava h�rn" },
		{ CoBoxedLineShapeIF.BOXED_LINE, "Blocklinje" },
		{ CoLineIF.LINE, "Linje" },
		{ CoLineIF.ORTHOGONAL_LINE, "Ortogonal linje" },
		{ CoPolygonShapeIF.POLYGON_SHAPE, "Polygon" },
		{ CoCubicBezierCurveShapeIF.CUBIC_BEZIER_CURVE, "Kurva" },
			
		// CoColumnGrid
		{CoImmutableColumnGridIF.COLUMN_GRID,"Kolumnraster"},


		// Text position
		{CoPageItemAbstractTextContentIF.CAP_HEIGHT,		"Versalh�jd"},
		{CoPageItemAbstractTextContentIF.CAP_ACCENT,		"Versal + Accent"},
		{CoPageItemAbstractTextContentIF.ASCENT,			"Uppstaplar"},
		
		{CoPageItemAbstractTextContentIF.ALIGN_TOP,			"�ver"},
		{CoPageItemAbstractTextContentIF.ALIGN_CENTERED,	"Centrerad"},
		{CoPageItemAbstractTextContentIF.ALIGN_BOTTOM,		"Under"},
		{CoPageItemAbstractTextContentIF.ALIGN_JUSTIFIED,	"Utsluten"},

	
		/*
		// CoBlendStyle
		{SOLID_BLEND,"Solid"},
		{LINEAR_BLEND,"Linear Blend"},
		{MID_LINEAR_BLEND,"Mid-Linear Blend"},
		{RECTANGULAR_BLEND,"Rectangular Blend"},
		{DIAMOND_BLEND,"Diamond Blend"},
		{CIRCULAR_BLEND,"Circular Blend"},
		{FULL_CIRCULAR_BLEND,"Full Circular Blend"},
*/

		// V�rden p� instansvariabeln offset i CoFirstBaseline
//		{CoFirstBaseline.CAP_HEIGHT,"Cap Height"},
//		{CoFirstBaseline.CAP_PLUS_ACCENT,"Cap + Accent"},
//		{CoFirstBaseline.ASCENT,"Ascent"},
		
		// V�rden p� instansvariabeln type i CoVerticalAlignment
//		{CoVerticalAlignment.TOP,"Top"},
//		{CoVerticalAlignment.CENTERED,"Centered"},
//		{CoVerticalAlignment.BOTTOM,"Bottom"},
//		{CoVerticalAlignment.JUSTIFIED,"Utsluten"},
		
		// CoPublicationColorCatalog
//		{PUBLICATION_COLOR_CATALOG,"F�rger"},


/*
		// CoFrameStyle
		{SOLID_BLEND,"Solid"},
		{DOUBLE_FRAME,"Double"},
		{THICK_THIN_FRAME,"Thick-Thin"},
		{THICK_THIN_THICK_FRAME,"Thick-Thin-Thick"},
		{THIN_THICK_FRAME,"Thin-Thick"},
		{TRIPLE_FRAME,"Triple"},
		{COUPON_FRAME,"Coupon"},
*/

		// V�rden p� instansvariabeln framing i CoFrameProperties 
//		{CoFramePropertiesIF.FRAME_INSIDE,"Inside"},
//		{CoFramePropertiesIF.FRAME_OUTSIDE,"Outside"},
		
		// CoSizeSpec
		{CoNoSizeSpecIF.NO_SIZE_SPEC,		"Fast storlek i punkter"},
		{CoNoSizeSpecIF.NO_WIDTH_SPEC,		"Fast bredd i punkter"},
		{CoNoSizeSpecIF.NO_HEIGHT_SPEC,		"Fast h�jd i punkter"},
		{CoAbsoluteWidthSpecIF.ABSOLUTE_WIDTH_SPEC,		"Fast bredd"},
		{CoAbsoluteHeightSpecIF.ABSOLUTE_HEIGHT_SPEC,		"Fast h�jd"},
		{CoFillWidthSpecIF.FILL_WIDTH_SPEC,			"Fyll ut p� bredden"},
		{CoFillHeightSpecIF.FILL_HEIGHT_SPEC,			"Fyll ut p� h�jden"},
		{CoContentWidthSpecIF.CONTENT_WIDTH_SPEC,		"Inneh�llet styr bredden"},
		{CoContentHeightSpecIF.CONTENT_HEIGHT_SPEC,		"Inneh�llet styr h�jden"},
		{CoProportionalWidthSpecIF.PROPORTIONAL_WIDTH_SPEC,	"Proportionell bredd"},
		{CoProportionalHeightSpecIF.PROPORTIONAL_HEIGHT_SPEC,	"Proportionell h�jd"},
		
		// CoLocationSpec
		{CoNoLocationIF.NO_LOCATION,					"Var som helst"},
		{CoCenterLocationIF.CENTER_LOCATION,				"I mitten"},
		{CoLeftLocationIF.LEFT_LOCATION,				"Till v�nster"},
		{CoRightLocationIF.RIGHT_LOCATION,				"Till h�ger"},
		{CoTopLocationIF.TOP_LOCATION,					"L�ngst upp"},
//		{CoTopLeftLocationIF.TOP_LEFT_LOCATION,			"Uppe t.v."},
//		{CoTopRightLocationIF.TOP_RIGHT_LOCATION,			"Uppe t.h."},
		{CoBottomLocationIF.BOTTOM_LOCATION,				"L�ngst ner"},
//		{CoBottomLeftLocationIF.BOTTOM_LEFT_LOCATION,		"Nere t.v."},
//		{CoBottomRightLocationIF.BOTTOM_RIGHT_LOCATION,	"Nere t.h."},

		//CoNamedGraphics
//		{NAMED_GRAPHICS, "Namngiven grafik"},

		//CoNamedGraphicsCatalog
//		{NAMED_GRAPHICS_CATALOG, "Komponentkatalog"},

	};	

/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}