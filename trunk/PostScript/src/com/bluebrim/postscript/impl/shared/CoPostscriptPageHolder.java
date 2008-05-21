package com.bluebrim.postscript.impl.shared;
import java.io.*;
import java.util.*;

import com.bluebrim.postscript.impl.server.color.*;

/**
 * Implementation of CoPostscriptHolder, capable of containing a single page.
 *
 * <p><b>Creation date:</b> 2001-06-01
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoPostscriptHolder
 */
public class CoPostscriptPageHolder implements CoPostscriptHolder {
	private Map m_registeredFunctions = new HashMap(); // [String->String] (function name->definition)
	private Set m_registeredFontFaces = new HashSet();		// [com.bluebrim.font.shared.CoFontFace]
	private CoColorantSet m_registeredColorants = new CoColorantSet();
	private boolean m_trappingUsed = false;
	private CoPostscriptWriter m_psWriter;
	private com.bluebrim.postscript.shared.CoPostscriptTarget m_psTarget;
	private int m_maxLanguageLevel = 2;

	private ByteArrayOutputStream m_setupStream = new ByteArrayOutputStream();
	private CoPostscriptWriter m_setupWriter = new CoPostscriptWriter(m_setupStream);
public void includeComment(String s) {
	if (CoPostscriptGenerator.POSTSCRIPT_DEBUG_OP_LABELS) {
		getWriter().print("%");
		getWriter().println(s);
	}
}

public void registerPostscriptFunction(String name, String definition, String description, String stackPreCond, String stackPostCond) {
	String declaration = "/" + name + " {" + definition + "} bind def";
	m_registeredFunctions.put(name, declaration);
}

public Set getFontFaces() {
	return m_registeredFontFaces;
}


public Map getFunctions() {
	return m_registeredFunctions;
}

public void registerFontFace(com.bluebrim.font.shared.CoFontFace fontFace) {
	m_registeredFontFaces.add(fontFace);
}



public OutputStream getFlushedOutputStream() {
	return m_psWriter.getFlushedOutputStream();
}


public CoPostscriptWriter getWriter() {
	return m_psWriter;
}


public CoPostscriptPageHolder(CoPostscriptWriter writer, com.bluebrim.postscript.shared.CoPostscriptTarget target) {
	super();
	m_psWriter = writer;
	m_psTarget = target;
}


public com.bluebrim.postscript.shared.CoPostscriptTarget getTarget() {
	return m_psTarget;
}


public CoColorantSet getColorants() {
	return m_registeredColorants;
}



public void registerColorant(CoColorant colorant) {
	m_registeredColorants.addColorant(colorant);
}


public void registerLanguageLevel(int level) {
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue(level <= m_psTarget.getLevel(), "Target Postscript language level exceeded");
	
	if (level > m_maxLanguageLevel) {
		m_maxLanguageLevel = level;
	}
}


public int getLanguageLevel() {
	return m_maxLanguageLevel;
}


public ByteArrayOutputStream getSetupStream() {
	m_setupWriter.flush();
	return m_setupStream;
}


public CoPostscriptWriter getSetupWriter() {
	return m_setupWriter;
}


public boolean isTrappingUsed() {
	return m_trappingUsed;
}

public void registerTrappingUse() {
	m_trappingUsed = true;
}
}