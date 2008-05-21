package com.bluebrim.text.impl.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Tab stop protocol
 * 
 * @author: Dennis Malmström
 */

public interface CoTabStopIF extends java.io.Serializable
{
	public static final int ALIGN_LEFT    = 0;
	public static final int ALIGN_CENTER  = 1;
	public static final int ALIGN_RIGHT   = 2;
	public static final int ALIGN_DECIMAL = 3;
	public static final int ALIGN_ON = 4;

	public static final int LEADER_NONE      = 0;
	public static final int LEADER_DOTS      = 1;
	public static final int LEADER_HYPHENS   = 2;
	public static final int LEADER_UNDERLINE = 3;
	public static final int LEADER_THICKLINE = 4;
	public static final int LEADER_EQUALS    = 5;

	public interface Renderer
	{
		void paint( CoPaintable g, CoGlyphVector gv, float y, float x0, float x1, float tracking );
	};
char [] alignOn();
CoTabStopIF copy();
int getAlignment();
int getLeader();
float getPosition();
Renderer getRenderer();
void setAlignment( int a );
void setAlignOn( String s );
void setLeader( int l );
void setPosition( float pos );
}