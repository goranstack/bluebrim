package com.bluebrim.text.shared;
import com.bluebrim.text.impl.shared.*;

/**
 * Interface för klasser som innehåller styckesutformning ( t ex indentering, anfanger, ... )
 */
 
public interface CoParagraphStyleIF extends com.bluebrim.text.shared.CoCharacterStyleIF
{
public Boolean getAdjustToBaseLineGrid();
public CoEnumValue getAlignment();
public CoEnumValue getBottomRulerAlignment();
public Float getBottomRulerFixedWidth();
public Float getBottomRulerLeftInset();
public Float getBottomRulerPosition();
public Float getBottomRulerRightInset();
public CoEnumValue getBottomRulerSpan();
public Float getBottomRulerThickness();
public Boolean getDropCaps();
public Integer getDropCapsCharacterCount();
public Integer getDropCapsLineCount();
public Float getFirstIndent();
public String getHyphenation();
CoEnumValue getHyphenationFallbackBehavior();
public Boolean getKeepLinesTogether();
public Boolean getLastInColumn();

public Float getLeftIndent();
public Float getRegularTabStopInterval();
public Float getRightIndent();
public Float getSpaceAfter();
public Float getSpaceBefore();
public CoTabSetIF getTabSet();
public Boolean getTopOfColumn();
public CoEnumValue getTopRulerAlignment();
public Float getTopRulerFixedWidth();
public Float getTopRulerLeftInset();
public Float getTopRulerPosition();
public Float getTopRulerRightInset();
public CoEnumValue getTopRulerSpan();
public Float getTopRulerThickness();
public Float getTrailingLinesIndent();

public void setAdjustToBaseLineGrid(Boolean state);
public void setAlignment(CoEnumValue alignment);
public void setBottomRulerAlignment( CoEnumValue v );
public void setBottomRulerFixedWidth( Float v );
public void setBottomRulerLeftInset( Float v );
public void setBottomRulerPosition( Float v );
public void setBottomRulerRightInset( Float v );
public void setBottomRulerSpan( CoEnumValue v );
public void setBottomRulerThickness( Float v );
public void setDropCaps(Boolean state);
public void setDropCapsCharacterCount(Integer count);
public void setDropCapsLineCount(Integer count);
public void setFirstIndent(Float indent);
public void setHyphenation( String n );
void setHyphenationFallbackBehavior( CoEnumValue align);
public void setKeepLinesTogether(Boolean state);
public void setLastInColumn(Boolean state);

public void setLeftIndent(Float indent);
public void setRegularTabStopInterval(Float leading);
public void setRightIndent(Float indent);
public void setSpaceAfter(Float space);
public void setSpaceBefore(Float space);
public void setTabSet( CoTabSetIF ts );
public void setTopOfColumn(Boolean state);
public void setTopRulerAlignment( CoEnumValue v );
public void setTopRulerFixedWidth( Float v );
public void setTopRulerLeftInset( Float v );
public void setTopRulerPosition( Float v );
public void setTopRulerRightInset( Float v );
public void setTopRulerSpan( CoEnumValue v );
public void setTopRulerThickness( Float v );
public void setTrailingLinesIndent(Float indent);


public CoLeading getLeading();

public Float getMinimumSpaceWidth();

public Float getOptimumSpaceWidth();

public void setLeading(CoLeading leading);

public void setMinimumSpaceWidth( Float v );

public void setOptimumSpaceWidth( Float v );
}