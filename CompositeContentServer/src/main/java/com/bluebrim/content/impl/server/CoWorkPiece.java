package com.bluebrim.content.impl.server;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.content.server.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.image.impl.server.*;
import com.bluebrim.image.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.text.impl.server.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Instances represents a piece of work made up of atomic content. A work piece put
 * together text, images, etc in way that produces some kind of message. A work piece is
 * not necessarily intended for a certain media. The idea is that one work piece can be 
 * published in several medias. The atomic content that is added to a work piece is not owned
 * by the work piece.
 * A work piece can be projected in a layout area.
 * has the capacity to distribute atomic contents to the children of the <code>CoLayoutArea</code>
 * Creation date: (2000-10-27 14:13:25)
 * @author: Göran Stäck
 */
public class CoWorkPiece extends CoContent implements CoWorkPieceIF, CoContentReceiver {
	public static final String XML_TAG = "workpiece";
	
	private CoLayoutParameters m_layoutParameters;
	// contents
	private List m_texts = new ArrayList();
	private List m_images = new ArrayList();
	private List m_layouts = new ArrayList();
	private List m_contents = new ArrayList();

	private CoPropertyChangeListener m_miscListener;
	private List m_layoutProjectors; // [ CoWorkPieceProjector ]
	private String m_contentDescription;

	public CoWorkPiece( CoLayoutParameters layoutParameters) {
		m_layoutParameters = layoutParameters;
		init();
	}

	public CoWorkPiece( CoLayoutParameters layoutParameters, CoGOI goi) {
		super( goi);
		m_layoutParameters = layoutParameters;
		init();
	}

	public boolean add(CoImageContentIF c) {
		if (!m_images.contains(c)) {
			m_contents.add(c);
			m_images.add(c);
			c.addPropertyChangeListener(m_miscListener);
			postContentSetChange();
			return true;
		}

		return false;
	}
	public boolean add(CoLayoutContentIF c) {
		if (!m_layouts.contains(c)) {
			m_contents.add(c);
			m_layouts.add(c);
			c.addPropertyChangeListener(m_miscListener);
			postContentSetChange();
			return true;
		}

		return false;
	}
	public boolean add(CoTextContentIF c) {
		if (!m_texts.contains(c)) {
			m_contents.add(c);
			m_texts.add(c);
			c.addPropertyChangeListener(m_miscListener);
			postContentSetChange();
			return true;
		}

		return false;
	}
	public boolean add(CoWorkPieceIF c) {
		assertNotWorkPiece();
		return false;
	}

	public CoContentIF addContent(CoContentIF content) {
		if (add((CoContent) content)) {
			//		requestDistribution();
		}

		return content;
	}

	private void assertAtomic(CoContentIF content) {
		CoAssertion.assertTrue(content instanceof CoAtomicContentIF, "A workpiece can't contain non atomic contents");
	}
	private void assertNotWorkPiece() {
		CoAssertion.assertTrue(true, "A workpiece can't contain another workpiece");
	}

	public void attachLayoutProjector(CoWorkPieceProjector projector) {
		if (m_layoutProjectors == null)
			m_layoutProjectors = new ArrayList();
		m_layoutProjectors.add(projector);
	}

	public void dettachLayoutProjector(CoWorkPieceProjector projector) {
		m_layoutProjectors.remove(projector);
		if (m_layoutProjectors.isEmpty())
			m_layoutProjectors = null;
	}
	public List getContents() {
		return m_contents;
	}
	public String getFactoryKey() {
		return CoWorkPieceIF.FACTORY_KEY;
	}
	public String getIconName() {
		return "CoWorkPieceIF.gif";
	}
	public String getIconResourceAnchor() {
		return CoWorkPieceIF.class.getName();
	}
	public String getIdentity() {
		return getName();
	}
	public List getImages() {
		return m_images;
	}
	public CoLayoutParameters getLayoutParameters() {
		return m_layoutParameters;
	}

	public List getLayouts() {
		return m_layouts;
	}

	public List getTexts() {
		return m_texts;
	}

	private final void init() {

		m_miscListener = new CoPropertyChangeListener() {
			public void propertyChange(CoPropertyChangeEvent ev) {
				String property = ev.getPropertyName();
				if (property.equals(CoTextContent.TEXT_PROPERTY))
					requestDistribution();
			}
		};
		setName(CoCompositeContentServerResources.getName(CoCompositeContentServerConstants.UNTITLED_WORKPIECE));
	}

	// PENDING: See Markus comment in CoArticle, 2000-11-01, Monika
	public void propagateStatusToParent() {
	}

	protected boolean remove(CoContentIF c) {
		assertAtomic(c);

		if (m_contents.remove(c)) {
			m_texts.remove(c);
			m_images.remove(c);
			m_layouts.remove(c);
			c.removePropertyChangeListener(m_miscListener);
			markDirty();
			return true;
		}

		return false;
	}

	public CoContentIF removeContent(CoContentIF content) {
		if (remove(content)) {
			postContentSetChange();
		}

		return content;

	}
	public void removeContents(Object[] contents) {
		boolean didRemove = false;

		for (int i = 0; i < contents.length; i++) {
			if (remove((CoContentIF) contents[i])) {
				didRemove = true;
			}
		}

		if (didRemove) {
			postContentSetChange();
		}
	}
	private void requestDistribution() {
		if (m_layoutProjectors != null) {
			Iterator i = m_layoutProjectors.iterator();
			while (i.hasNext()) {
				((CoWorkPieceProjector) i.next()).distribute();
			}
		}
	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		super.xmlVisit(visitor);

		//do we actually want to export atomic contents? Monika, 2001-03-22

		visitor.export(getTexts().iterator()); // GOI's ???
		visitor.export(getImages().iterator()); // GOI's ???
		visitor.export(getLayouts().iterator()); // GOI's ???

		//PENDING: status
	}

	protected boolean add(CoContent c) {
		assertAtomic(c);
		return CoContentUtility.addContent(this, c);
	}

	public void addContents(Object[] contents, int pos) {
		if (contents.length == 0)
			return;

		for (int i = 0; i < contents.length; i++) {
			int n = m_contents.indexOf(contents[i]);

			if (n == -1) {
				((CoContentIF) contents[i]).addPropertyChangeListener(m_miscListener);
				continue;
			}

			m_contents.remove(n);
			if (n < pos)
				pos--;
		}

		for (int i = 0; i < contents.length; i++) {
			if (pos == -1) {
				m_contents.add((CoContentIF) contents[i]);
			} else {
				m_contents.add(pos, (CoContentIF) contents[i]);
				pos++;
			}
		}

		m_texts.clear();
		m_images.clear();
		m_layouts.clear();

		Iterator i = m_contents.iterator();
		while (i.hasNext()) {
			Object o = i.next();
			if (o instanceof CoTextContentIF) {
				m_texts.add(o);
			} else if (o instanceof CoImageContentIF) {
				m_images.add(o);
			} else if (o instanceof CoLayoutContentIF) {
				m_layouts.add(o);
			}
		}

		postContentSetChange();

		markDirty();
	}


	/**
	 * This is a plain copy from CoContentCollection and that's bad design
	 * that should be fixed.
	 */
	private void addContent(List fileList) {
		Iterator iter = fileList.iterator();
		while (iter.hasNext()) {
			File file = (File)iter.next();
			try {
				BufferedImage image = ImageIO.read(file);
				if (image != null) {
					addContent(new CoImageContent(file));
				}
			} catch (IOException e) {
				continue;
			}

		}
	}



	public String getContentDescription() {
		return m_contentDescription;
	}

	protected void postContentSetChange() {
		StringBuffer sb = new StringBuffer();

		sb.append(super.getContentDescription());
		sb.append("\n");
		Iterator i = m_texts.iterator();
		while (i.hasNext()) {
			sb.append("  ");
			sb.append(((CoContentIF) i.next()).getContentDescription());
			sb.append("\n");
		}
		i = m_images.iterator();
		while (i.hasNext()) {
			sb.append("  ");
			sb.append(((CoContentIF) i.next()).getContentDescription());
			sb.append("\n");
		}
		i = m_layouts.iterator();
		while (i.hasNext()) {
			sb.append("  ");
			sb.append(((CoContentIF) i.next()).getContentDescription());
			sb.append("\n");
		}

		m_contentDescription = sb.toString();

		requestDistribution();
	}

	protected void prepareCopy(CoContent copy) {
		super.prepareCopy(copy);

		CoWorkPiece wp = (CoWorkPiece) copy;

		wp.init();

		wp.m_texts = new ArrayList();
		wp.m_images = new ArrayList();
		wp.m_layouts = new ArrayList();
		wp.m_contents = new ArrayList();

		// clone contents ???
		wp.m_texts.addAll(m_texts);
		wp.m_images.addAll(m_images);
		wp.m_layouts.addAll(m_layouts);
		wp.m_contents.addAll(m_contents);


		wp.m_layoutProjectors = null;
	}

    public String getType()
    {
       	return CoCompositeContentServerResources.getName( WORK_PIECE );    
    }


}