package com.bluebrim.layout.impl.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bluebrim.text.impl.shared.CoDocumentCollector;
import com.bluebrim.text.shared.CoFormattedText;
import com.bluebrim.text.shared.CoTextContentIF;

/**
 * Representation of a text distribution.
 * 
 * @author: Dennis Malmström
 */

public class CoTextDistribution {

	private CoTextContentIF m_source; // the text that was distributed
	private List m_destination; // [ CoPageItemWorkPieceTextContent ], the page items to which the text was distributed
	private CoFormattedText m_leftOvers; // paragraphs that were left over

	public CoTextContentIF getSource() {
		return m_source;
	}

	public boolean isNull() {
		return false;
	}

	public void updateDocument() {
		CoFormattedText doc = collect();
		m_source.setFormattedText( doc);
	}


	public CoTextDistribution(
		CoTextContentIF source,
		List destination,
	// [ CoPageItemWorkPieceTextContent ]
	CoFormattedText leftovers) {
		m_destination = destination;
		m_source = source;
		m_leftOvers = leftovers;
	}

	private CoFormattedText collect() {
		CoDocumentCollector collector = new CoDocumentCollector();

		List sourceDocuments = new ArrayList();
		Iterator iter = m_destination.iterator();
		while (iter.hasNext()) {
			sourceDocuments.add(((CoPageItemWorkPieceTextContent) iter.next()).getFormattedText());
		}
		if (!sourceDocuments.contains(m_leftOvers))
			sourceDocuments.add(m_leftOvers);

		CoFormattedText doc = m_source.getMutableFormattedText( null);
		doc.clear();
		collector.collect(doc, sourceDocuments);

		return doc;
	}
}