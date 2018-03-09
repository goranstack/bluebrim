package com.bluebrim.text.shared.swing;

/**
 * Interface for text views that can be justified
 * 
 * @author: Dennis Malmstr�m
 */
 
interface CoJustifiableView
{
	public class ToTight extends Throwable
	{
	};

	public static final ToTight m_toTight = new ToTight();
void clearSpacePadding();


float getSpacePadding();

float setSpacePadding( float p, float minimumRelativeSpaceWidth ) throws ToTight;
}