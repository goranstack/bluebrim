package com.bluebrim.paint.impl.shared;
import java.awt.*;

/**
 * Representerar passmärkesfärgen. Objekt med denna färg printas på alla plåtar.
 * Är svart från början men är tillåten att ändra.
 *
 * Corrected for GS/J 4.1 by Markus Persson 2000-11-28 
 */
public class CoRegistrationColor extends com.bluebrim.paint.impl.shared.CoColor implements com.bluebrim.paint.impl.shared.CoRegistrationColorIF {
	private int m_previewRGB;
	private transient Color m_cachedPreview;
/**
 * Du kan ändra färgen på passmärkesfärgen men den är svart från början.
 */
public CoRegistrationColor() {
	super();
	m_previewRGB = 0x000000;
}
public boolean canBeDeleted( ) 
{
	return false;
}
public boolean canBeEdited( ) 
{
	return true;
}
public void copyFrom(com.bluebrim.paint.impl.shared.CoEditableColorIF source) {
	setColor(((com.bluebrim.paint.impl.shared.CoRegistrationColorIF) source).getPreviewColor());
}
public com.bluebrim.paint.impl.shared.CoEditableColorIF createObject()
{
	return new CoRegistrationColor();
}
public boolean equals(Object o) {
	return (o instanceof CoRegistrationColor)
		&& (((CoRegistrationColor) o).m_previewRGB == m_previewRGB);
}
public String getFactoryKey (){
	return REGISTRATION_COLOR;
}
public String getName ( )
{
	return com.bluebrim.paint.impl.shared.CoColorResources.getName(REGISTRATION_COLOR);
}
public Color getPreviewColor() {
	if (m_cachedPreview == null) {
		return m_cachedPreview = new Color(m_previewRGB);
	}
	return m_cachedPreview;
}
public String getType ( )
{
	return REGISTRATION_COLOR;
}
public final void setColor(Color c) {
	m_cachedPreview = c;
	m_previewRGB = c.getRGB();
	markDirty();
	//PENDING (Dennis,2001-02-27): what about setting cyanPercentage, magentaPercentage, yellowPercentage, blackPercentage ?
}

public float getBlackPercentage()
{
	return 100;
}

public float getCyanPercentage()
{
	return 100;
}

public float getMagentaPercentage()
{
	return 100;
}

public float getYellowPercentage()
{
	return 100;
}
}