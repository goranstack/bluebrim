package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * Abstract ancestor of all page item view renderer classes.
 * Page item view renderer classes implement page item view renderer behavior.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoPageItemViewRenderer
{
	// fallback renderer, paints nothing
	public static final CoPageItemViewRenderer DEFAULT_RENDERER =
		new CoPageItemViewRenderer()
		{
			public void paint( CoPaintable g, CoPageItemView v, Rectangle bounds ) {}
		};
		
	// debugging/tracing switches
	public static boolean DO_TRACE_NOTIFICATIONS = true; // trace page item notifications on console
	public static boolean DO_TRACE_OPTIMIZED_PAINT = false; // trace optimization on console
	public static boolean DO_PAINT_PI = false; // draw page item id on screen
	public static boolean DO_PAINT_PIV = false; // draw page item view id on screen
	public static boolean DO_PAINT_GEOMETRY = false; // draw page item geometry on screen

	// optimization switches
	public static boolean DO_OPTIMIZE_PAINT = true; // don't draw views that are outside visible part of desktop
	public static boolean DO_OPTIMIZE_PAINT_CHILDREN = false; // stop drawing children at a given parent size (screen coordinates)




	
	// Rendering hints

	// paint column grids
	public static final RenderingHints.Key PAINT_COLUMN_GRID =
		new RenderingHints.Key( 0 )
		{
			public boolean isCompatibleValue( Object v )
			{
				return ( v == PAINT_COLUMN_GRID_ON ) || ( v == PAINT_COLUMN_GRID_OFF );
			}
		};
	public static final Object PAINT_COLUMN_GRID_ON 	= Boolean.TRUE;
	public static final Object PAINT_COLUMN_GRID_OFF 	= Boolean.FALSE;

	// paint baseline grid
	public static final RenderingHints.Key PAINT_BASE_LINE_GRID =
		new RenderingHints.Key( 0 )
		{
			public boolean isCompatibleValue( Object v )
			{
				return ( v == PAINT_BASE_LINE_GRID_ON ) || ( v == PAINT_BASE_LINE_GRID_OFF );
			}
		};
	public static final Object PAINT_BASE_LINE_GRID_ON 	= Boolean.TRUE;
	public static final Object PAINT_BASE_LINE_GRID_OFF 	= Boolean.FALSE;

	// paint indicator on clipped images
	public static final RenderingHints.Key PAINT_IMAGE_CLIP_INDICATOR =
		new RenderingHints.Key( 0 )
		{
			public boolean isCompatibleValue( Object v )
			{
				return ( v == PAINT_IMAGE_CLIP_INDICATOR_ON ) || ( v == PAINT_IMAGE_CLIP_INDICATOR_OFF );
			}
		};
	public static final Object PAINT_IMAGE_CLIP_INDICATOR_ON 	= Boolean.TRUE;
	public static final Object PAINT_IMAGE_CLIP_INDICATOR_OFF 	= Boolean.FALSE;

	// paint outlines
	public static final RenderingHints.Key PAINT_OUTLINE =
		new RenderingHints.Key( 0 )
		{
			public boolean isCompatibleValue( Object v )
			{
				return ( v == PAINT_OUTLINE_ON ) || ( v == PAINT_OUTLINE_OFF );
			}
		};
	public static final Object PAINT_OUTLINE_ON 	= Boolean.TRUE;
	public static final Object PAINT_OUTLINE_OFF 	= Boolean.FALSE;

	// paint outline on models
	public static final RenderingHints.Key PAINT_MODEL_OUTLINE =
		new RenderingHints.Key( 0 )
		{
			public boolean isCompatibleValue( Object v )
			{
				return ( v == PAINT_MODEL_OUTLINE_ON ) || ( v == PAINT_MODEL_OUTLINE_OFF );
			}
		};
	public static final Object PAINT_MODEL_OUTLINE_ON 	= Boolean.TRUE;
	public static final Object PAINT_MODEL_OUTLINE_OFF 	= Boolean.FALSE;

	// outline style
	public static final RenderingHints.Key PAINT_OUTLINE_STYLE =
		new RenderingHints.Key( 0 )
		{
			public boolean isCompatibleValue( Object v )
			{
				return ( v == PAINT_OUTLINE_DASHED ) || ( v == PAINT_OUTLINE_SOLID );
			}
		};	
	public static final Object PAINT_OUTLINE_DASHED = Boolean.TRUE;
	public static final Object PAINT_OUTLINE_SOLID 	= Boolean.FALSE;





	
	// static colors
	public static final Color COLUMN_GRID_COLOR = Color.blue;
	public static final Color BASE_LINE_GRID_COLOR = Color.red;
	public static final Color PAINT_IMAGE_CLIP_INDICATOR_COLOR = Color.red;


	// static rendiring hint sets
	public static final Map ALL_DECORATIONS = new HashMap();
	public static final Map NO_DECORATIONS = new HashMap();

	// paint custom grid
	public static final RenderingHints.Key PAINT_CUSTOM_GRID =
		new RenderingHints.Key( 0 )
		{
			public boolean isCompatibleValue( Object v )
			{
				return ( v == PAINT_CUSTOM_GRID_ON ) || ( v == PAINT_CUSTOM_GRID_OFF );
			}
		};
	public static final Object PAINT_CUSTOM_GRID_OFF 	= Boolean.FALSE;
	public static final Object PAINT_CUSTOM_GRID_ON 	= Boolean.TRUE;	
	public static final Object PAINT_TOPICS_OFF 	= Boolean.FALSE;
	public static final Object PAINT_TOPICS_ON 	= Boolean.TRUE;
	public static final Object PAINT_IMAGE_PLACEHOLDERS_OFF 	= Boolean.FALSE;
	public static final Object PAINT_IMAGE_PLACEHOLDERS_ON 	= Boolean.TRUE;

	// paint image placeholders
	public static final RenderingHints.Key PAINT_IMAGE_PLACEHOLDERS =
		new RenderingHints.Key( 0 )
		{
			public boolean isCompatibleValue( Object v )
			{
				return ( v == PAINT_IMAGE_PLACEHOLDERS_ON ) || ( v == PAINT_IMAGE_PLACEHOLDERS_OFF );
			}
		};
	// paint column grid
	public static final RenderingHints.Key PAINT_TOPICS =
		new RenderingHints.Key( 0 )
		{
			public boolean isCompatibleValue( Object v )
			{
				return ( v == PAINT_TOPICS_ON ) || ( v == PAINT_TOPICS_OFF );
			}
		};
	
	static
	{
		ALL_DECORATIONS.put( PAINT_CUSTOM_GRID, PAINT_CUSTOM_GRID_ON );
		ALL_DECORATIONS.put( PAINT_COLUMN_GRID, PAINT_COLUMN_GRID_ON );
		ALL_DECORATIONS.put( PAINT_BASE_LINE_GRID, PAINT_BASE_LINE_GRID_ON );
		ALL_DECORATIONS.put( PAINT_IMAGE_CLIP_INDICATOR, PAINT_IMAGE_CLIP_INDICATOR_ON );
		ALL_DECORATIONS.put( PAINT_OUTLINE, PAINT_OUTLINE_ON );
		ALL_DECORATIONS.put( PAINT_MODEL_OUTLINE, PAINT_MODEL_OUTLINE_ON );
		ALL_DECORATIONS.put( PAINT_TOPICS, PAINT_TOPICS_ON );
		
		NO_DECORATIONS.put( PAINT_CUSTOM_GRID, PAINT_CUSTOM_GRID_OFF );
		NO_DECORATIONS.put( PAINT_COLUMN_GRID, PAINT_COLUMN_GRID_OFF );
		NO_DECORATIONS.put( PAINT_BASE_LINE_GRID, PAINT_BASE_LINE_GRID_OFF );
		NO_DECORATIONS.put( PAINT_IMAGE_CLIP_INDICATOR, PAINT_IMAGE_CLIP_INDICATOR_OFF );
		NO_DECORATIONS.put( PAINT_OUTLINE, PAINT_OUTLINE_OFF );
		NO_DECORATIONS.put( PAINT_MODEL_OUTLINE, PAINT_MODEL_OUTLINE_OFF );
		NO_DECORATIONS.put( PAINT_TOPICS, PAINT_TOPICS_OFF );
	}
	public static final Color CUSTOM_GRID_COLOR = Color.green.darker();


// paint view

public abstract void paint( CoPaintable g, CoPageItemView v, Rectangle bounds );
}