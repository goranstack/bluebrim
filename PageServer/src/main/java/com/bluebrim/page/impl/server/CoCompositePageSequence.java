package com.bluebrim.page.impl.server;
import java.util.*;
/**
 * The page sequence model is implemented as a composite pattern where this class is the composite class.
 * Creation date: (2001-11-14 11:34:08)
 * @author: Göran Stäck
 */
public class CoCompositePageSequence extends CoAbstractPageSequence  {

	private List m_pageSequences = new ArrayList(); // [ CoAbstractPageSequence ]

public CoAbstractPageSequence addPageSequence(CoAbstractPageSequence sequence){
	m_pageSequences.add(sequence);
	return sequence;
}
}