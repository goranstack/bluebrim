package com.bluebrim.page.impl.server;
import java.util.*;
import com.bluebrim.page.shared.*;
/**
 * The page sequence model is implemented as a composite pattern where this class is the leaf class.
 * Creation date: (2001-11-21 12:07:45)
 * @author: Göran Stäck 
 */
public class CoPageSequence extends CoAbstractPageSequence {
	private List m_pages= new ArrayList(); // [ CoPage ]

public CoPage addPage(CoPage page){
	m_pages.add(page);
	return page;
}
}