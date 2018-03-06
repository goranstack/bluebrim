package com.bluebrim.postscript.client;
import java.io.*;
import java.util.*;

import javax.print.attribute.standard.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.postscript.impl.shared.*;
import com.bluebrim.postscript.impl.shared.printing.*;
import com.bluebrim.postscript.shared.*;

/**
 * Client class for handling postscript print jobs. Note that this class has nothing to do
 * with the AWT PrintJob class.
 *
 * <p><b>Creation date:</b> 2001-08-22
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoPostscriptClientPrintJob {
	private CoPostscriptTarget m_target;
	private CoPageCollection m_pages;

	public CoPostscriptClientPrintJob(CoPostscriptTarget target, List namedViewables ) {
		this(target, namedViewables, new PageRanges(1, namedViewables.size()));
	}


	public CoPostscriptClientPrintJob(CoPostscriptTarget target, List namedViewables, PageRanges pageRanges) {
		m_target = target;
		m_pages = new CoPageCollection();

		addSelectedViewables(namedViewables, pageRanges);
	}

	private void addSelectedViewables(List viewables, PageRanges pageRanges) {
		int i = -1;
		while ((i = pageRanges.next(i)) != -1) {
			if (i > viewables.size())
				return;
			CoNamedViewable viewable = (CoNamedViewable) viewables.get(i - 1);
			m_pages.addPrintablePage(new CoPostscriptPage(viewable, viewable.getName()));
		}
	}

	public void generatePostscript(CoPostscriptTargetOutput targetOutput) {
		if (!targetOutput.selectOutput()) {
			return;
		}
		OutputStream out = targetOutput.getOutputStream();

		generatePostscript(out);
	}

	public void generatePostscript(File outFile) {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(outFile));
			generatePostscript(out);
		} catch (IOException e) {
			// FIXME: better error handling!
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
	}

	public void generatePostscript(OutputStream out) {
		// PENDING: the connection to the "postscript server" on the server side
		// should probably be made here.
		if (m_target.isIncludePreview()) {
			CoEpsGenerator.createEpsFile(m_pages, out, m_target);
		} else {
			CoPostscriptGenerator.createPostscriptFile(m_pages, out, m_target);
		}
	}

	public CoPageCollection getPages() {
		return m_pages;
	}
}