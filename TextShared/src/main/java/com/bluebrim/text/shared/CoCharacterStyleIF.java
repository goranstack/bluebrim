package com.bluebrim.text.shared;

import java.awt.*;
import java.rmi.*;

import javax.swing.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Interface för klasser som innehåller teckenutformning ( t ex typsnitt, stil, färg, ... )
 */
public interface CoCharacterStyleIF extends CoObjectIF, CoNamed, CoCatalogElementIF, Remote, CoXmlEnabledIF {

	public class NameNotUniqueException extends Exception {
	}

	void clear();
	public Boolean getAllCaps();
	public AttributeSet getAttributes();
	public Float getBaselineOffset();
	public Font getFont();
	public String getFontFamily();
	public Float getFontSize();
	public String getForegroundColor();
	public Float getForegroundShade();
	public Float getHorizontalScale();
	public javax.swing.KeyStroke getKeyStroke();

	public Boolean getShadow();

	public Boolean getStrikeThru();
	public com.bluebrim.font.shared.CoFontAttribute getStyle();
	public Boolean getSuperior();
	public Float getTrackAmount();
	public CoEnumValue getUnderline();

	public com.bluebrim.font.shared.CoFontAttribute getWeight();
	public CoEnumValue getVerticalPosition();
	public Float getVerticalScale();
	public void setAllCaps(Boolean state);
	void setAttributes(AttributeSet a);
	public void setBaselineOffset(Float blof);
	public void setFontFamily(String family);
	public void setFontSize(Float size);
	public void setForegroundColor(String c);
	public void setForegroundShade(Float s);
	public void setHorizontalScale(Float size);
	public void setKeyStroke(javax.swing.KeyStroke ks);

	public void setShadow(Boolean state);

	public void setStrikeThru(Boolean state);
	public void setStyle(com.bluebrim.font.shared.CoFontAttribute bold);
	public void setSuperior(Boolean state);
	public void setTrackAmount(Float trackAmount);
	public void setUnderline(CoEnumValue underline);

	public void setWeight(com.bluebrim.font.shared.CoFontAttribute bold);
	public void setVerticalPosition(CoEnumValue underline);
	public void setVerticalScale(Float size);

	CoCharacterStyleIF getBasedOn();

	AttributeSet getEffectiveAttributes();

	boolean getInherit();

	boolean isBasedOn(CoCharacterStyleIF s);

	boolean isDeleted();

	void setBasedOn(CoCharacterStyleIF bo);

	void setDeleted(boolean i);

	void setInherit(boolean i);

	void setName(String name) throws NameNotUniqueException;

	public Float getShadowAngle();

	public String getShadowColor();

	public Float getShadowOffset();

	public Float getShadowShade();

	public void setShadowAngle(Float blof);

	public void setShadowColor(String c);

	public void setShadowOffset(Float blof);

	public void setShadowShade(Float s);
}