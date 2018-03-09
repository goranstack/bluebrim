package com.bluebrim.text.shared;

/**
 * Interface defining the protocol of text geometry specification
 * Text geometry consists of properties for the position of the first baseline and
 * properties for vertical alignment
 * 
 * @author: Dennis Malmström
 */

 public interface CoTextGeometryIF extends java.io.Serializable
{
	public static final String BASELINE_CAP_HEIGHT	= "BASELINE_CAP_HEIGHT";
	public static final String BASELINE_CAP_ACCENT	= "BASELINE_CAP_ACCENT";	
	public static final String BASELINE_ASCENT 		  = "BASELINE_ASCENT";	

	public static final String ALIGN_TOP		    = "ALIGN_TOP";
	public static final String ALIGN_CENTERED	  = "ALIGN_CENTERED";	
	public static final String ALIGN_BOTTOM 	  = "ALIGN_BOTTOM";	
	public static final String ALIGN_JUSTIFIED 	= "ALIGN_JUSTIFIED";
public float getFirstBaselineOffset();
public String getFirstBaselineType();
public float getVerticalAlignmentMaxInter();
public String getVerticalAlignmentType();
}
