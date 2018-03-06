package com.bluebrim.postscript.impl.server.drawingops;


import java.util.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Draw text output to the postscript writer. This drawing operation is mutable, contrary to most (all?) 
 * of the rest. If we want to draw yet another character on the same line as the one(s) already stored in this
 * drawing operation, we can add this character using addChar. This is really a workaround, resulting from
 * the problem that the layouteditor only calls our recording Paintable object with one char at the time,
 * which, if treated separately, would generate extremely clumsy postscript code.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoDrawTextOp extends CoDrawingOperation {
	private float m_yPos;
	private StringBuffer m_chars = new StringBuffer();
	private List m_xPosList = new Vector();
public CoDrawTextOp(char ch, float x, float y) {
	m_yPos = y;
	addChar(ch, x);
}


public void addChar(char ch, float x) {
	m_chars.append(ch);
	m_xPosList.add(new Float(x));
}


protected void createSingleCharacter(CoPostscriptHolder psHolder, char ch, float x) {
	psHolder.includeComment("Char: " + ch);

	psHolder.getWriter().println(CoPostscriptUtil.getGlyphName(ch) + " " + 
		CoPostscriptUtil.psX(x) + 
		CoPostscriptUtil.psY(m_yPos) + "S");
}


protected void createTextString(CoPostscriptHolder psHolder) {
	psHolder.includeComment("Text: " + m_chars);

	float[] xPosList = new float[m_xPosList.size()];
	for (int i = 0; i < xPosList.length; i++) {
		xPosList[i] = ((Float)m_xPosList.get(i)).floatValue();
	}
	
	psHolder.getWriter().print(CoPostscriptUtil.psY(m_yPos) + "[");
	
	for (int i = 0; i < m_xPosList.size(); i++) {
		float x = ((Float)m_xPosList.get(i)).floatValue();
		psHolder.getWriter().print(CoPostscriptUtil.psX(x));
	}
	
	psHolder.getWriter().print("] [");

	// Write sequence of glyph names	
	CoPostscriptUtil.writeGlyphNames(m_chars, psHolder.getWriter());
	
	psHolder.getWriter().println("] T");
}


public void generatePostscript(CoPostscriptHolder psHolder) {
	if (m_chars.length()==1) {
		createSingleCharacter(psHolder, m_chars.charAt(0), ((Float)m_xPosList.get(0)).floatValue());
	} else {
		createTextString(psHolder);
	}
}


public void preparePostscript(CoPostscriptHolder psHolder) {
	psHolder.registerPostscriptFunction("S", 
		"moveto glyphshow", "Single Glyph Show", "<glyph> <yPos> <xPos>", "-");
	psHolder.registerPostscriptFunction("T", 
		"0 1 2 index length 1 sub {dup 3 index exch get 4 index moveto\n" + 
		"1 index exch get glyphshow} for pop pop pop", 
		"Text Show", "<yPos> <[xPosList]> <[glyphList]>", "-");
}


public boolean sameLine(float y) {
	return (m_yPos == y);
}
}