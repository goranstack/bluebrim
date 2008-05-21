package com.bluebrim.postscript.impl.server.drawingops;


import com.bluebrim.postscript.impl.shared.*;

/**
 * Sets the font. The font must be correctly set in the current graphics context, before any calls to
 * CoDrawTextOp is made. Note that any calls to Push/Pop GraphicsState will destroy the font setting.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoDrawTextOp
 */
public class CoSetFontOp extends CoDrawingOperation {
	private com.bluebrim.font.shared.CoFont m_font;
public CoSetFontOp(com.bluebrim.font.shared.CoFont font) {
	m_font = font;
}

public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoSetFontOp font -> " + m_font);

	String fontName = m_font.getFace().getPostscriptData().getPostscriptName();
	float fontSize = m_font.getFontSize();

	psHolder.getWriter().println("/" + fontName + " " + CoPostscriptUtil.psLength(fontSize) + "selectfont");
}


public void preparePostscript(CoPostscriptHolder psHolder) {
	psHolder.registerPostscriptFunction("f", 
		"dup findfont /ISOLatin1Encoding ChangeEncoding definefont exch makefont setfont", 
		"Change font face and size", "<fontSizeMatrix> <fontName>", "-");

	// tell holder that this font is needed by this document
	psHolder.registerFontFace(m_font.getFace());
}
}