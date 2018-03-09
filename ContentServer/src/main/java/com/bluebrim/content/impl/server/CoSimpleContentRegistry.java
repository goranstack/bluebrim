package com.bluebrim.content.impl.server;

import java.util.Iterator;

import com.bluebrim.content.shared.*;
import com.bluebrim.content.shared.CoContentCollectionIF;
import com.bluebrim.content.shared.CoContentIF;
import com.bluebrim.content.shared.CoWorkPieceIF;
import com.bluebrim.image.shared.CoImageContentIF;
import com.bluebrim.layout.shared.CoLayoutContentIF;
import com.bluebrim.system.shared.CoGOI;
import com.bluebrim.text.shared.CoTextContentIF;

/**
 * Simple <code>CoPageContext</code> that wraps a <code>CoContentCollectionIF</code>
 * and do seqvencial search in the content list.
 * 
 * @author Göran Stäck 2002-09-29
 *
 */
public class CoSimpleContentRegistry implements CoContentRegistry {
	
	private CoContentCollectionIF m_contentCollection;
	
	public CoSimpleContentRegistry(CoContentCollectionIF contentCollection) {
		m_contentCollection = contentCollection;
	}

	public CoLayoutContentIF lookupLayoutContent(CoGOI goi) {
		return (CoLayoutContentIF)getContent(goi);
	}

	public CoImageContentIF lookupImageContent(CoGOI goi) {
		return (CoImageContentIF)getContent(goi);
	}

	public CoTextContentIF lookupTextContent(CoGOI goi) {
		return (CoTextContentIF)getContent(goi);
	}

	public CoWorkPieceIF lookupWorkPiece(CoGOI goi) {
		return (CoWorkPieceIF)getContent(goi);
	}

	private CoContentIF getContent(CoGOI goi) {
		Iterator iter = m_contentCollection.getContents().iterator();
		while (iter.hasNext()) {
			CoContentIF content = (CoContentIF)iter.next();
			if (content.getGOI().equals(goi))
				return content;
		}
		return null;		
	}
}
