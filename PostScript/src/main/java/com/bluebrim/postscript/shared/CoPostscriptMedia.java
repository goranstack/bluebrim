package com.bluebrim.postscript.shared;
import java.awt.*;

/**
 * Representation of a physical media. This class responsibility is overlapping with several other classes, and
 * a redesign is needed to remove all but one of them.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see com.bluebrim.papermedia.impl.server.CoSheetType
 * @see com.bluebrim.page.impl.server.CoPageSize
 */
public class CoPostscriptMedia {
	public final static CoPostscriptMedia A4 = new CoPostscriptMedia(new Dimension(595, 842), "A4");
	public final static CoPostscriptMedia NEWSPAPER = new CoPostscriptMedia(new Dimension(1125, 1587), "unknown", "Newspaper page");

	private Dimension m_size;
	private String m_postscriptDescriptor;
	private String m_name;
	
	public String getName() {
		return m_name;
	}

	public String getPostscriptDescriptor() {
		return m_postscriptDescriptor;
	}

	public Dimension getSize() {
		return m_size;
	}

	public double getHeight() {
		return m_size.getHeight();
	}

	public double getWidth() {
		return m_size.getWidth();
	}

	public CoPostscriptMedia(Dimension size, String postscriptDescriptor) {
		this(size, postscriptDescriptor, postscriptDescriptor);
	}

	public CoPostscriptMedia(Dimension size, String postscriptDescriptor, String name) {
		m_size = size;
		m_postscriptDescriptor = postscriptDescriptor;
		m_name = name;
	}
}