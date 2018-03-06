package com.bluebrim.content.impl.server;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.image.impl.server.*;
import com.bluebrim.image.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 *
 * A collection for contents of various kinds :
 * atomic contents as text, image etc and 
 * work pieces as well.
 * If a content reciever is supplied every content added is added to the content reciever as well.
 * 
 * Creation date: (2000-10-27 14:01:05)
 * @author: Monika Czerska
 */

public class CoContentCollection extends CoSimpleObject implements CoContentCollectionIF, CoXmlEnabledIF {

	public static final String XML_TAG = "content-collection";
	
	private final List m_contents = new ArrayList();
	private boolean m_dirty;
	private CoContentReceiver m_contentReceiver;
	private String m_name;
	private CoLayoutParameters m_layoutParameters;

	public CoContentCollection( CoLayoutParameters layoutParameters, String name) {
		m_layoutParameters = layoutParameters;
		m_name = name;
	}

	public CoContentCollection( CoLayoutParameters layoutParameters, String name, CoContentReceiver contentReceiver) {
		this( layoutParameters, name);
		m_contentReceiver = contentReceiver;
	}

	public CoContentCollection( CoLayoutParameters layoutParameters, CoContentReceiver contentReceiver) {
		this( layoutParameters, "", contentReceiver);
	}

	/**
	 * Used for XML-import
	 */
	private CoContentCollection(Object superModel, Node node, CoXmlContext context) {


	}

	private void addContent(CoContentIF content, int pos) {
		m_contents.add(pos, content);
		markDirty();
	}
	
	/**
	 * Only accepts image files
	 * @param fileList
	 */
	private void addContent(List fileList, int pos) {
		Iterator iter = fileList.iterator();
		while (iter.hasNext()) {
			File file = (File)iter.next();
			try {
				BufferedImage image = ImageIO.read(file);
				if (image != null) {
					addContent(new CoImageContent(file), pos);
				}
			} catch (IOException e) {
				continue;
			}
			
		}
	}

	public CoContentIF addContent(CoContentIF content) {
		if (m_contents.add(content)) {
			if (m_contentReceiver != null)
				CoContentUtility.addContent(m_contentReceiver, content);
			markDirty();
		}
		return content;
	}

	public List getContents() {
		return m_contents;
	}

	public CoLayoutParameters getLayoutParameters() {
		return m_layoutParameters;
	}

	protected void markDirty() {
		m_dirty = !m_dirty;

		if (CoAssertion.SIMULATION_SUPPORT)
			CoAssertion.addChangedObject(this);
	}

	public CoContentIF removeContent(CoContentIF content) {
		if (m_contents.remove(content))
			markDirty();
		return content;
	}

	public void removeContents(Object[] contents) {

		for (int i = 0; i < contents.length; i++)
			m_contents.remove((CoContentIF) contents[i]);
		if (contents.length > 0)
			markDirty();
	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.export(m_contents.iterator());
	}

	public void addContents(Object[] contents, int pos) {
		if (contents.length == 0)
			return;

		for (int i = 0; i < contents.length; i++) {
			int n = m_contents.indexOf(contents[i]);

			if (n == -1)
				continue;
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

			if (m_contentReceiver != null)
				CoContentUtility.addContent(m_contentReceiver, (CoContentIF)contents[i]);

		}

		markDirty();
	}

	public String getName() {
		return m_name;
	}

	public boolean add(CoImageContentIF imageContent) {
		addContent(imageContent);
		return true;
	}

	public boolean add(CoLayoutContentIF layoutContent) {
		addContent(layoutContent);
		return true;
	}

	public boolean add(CoTextContentIF textContent) {
		addContent(textContent);
		return true;
	}

	public boolean add(CoWorkPieceIF workPiece) {
		addContent(workPiece);
		return true;
	}

	public void xmlAddSubModel(String parameter, Object subModel, CoXmlContext context) throws CoXmlReadException {

		if (subModel instanceof CoContentIF)
			addContent((CoContentIF) subModel);

	}

	public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
	}
	
	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		return new CoContentCollection((CoLayoutParameters)context.getValue(CoLayoutParameters.class),
											(CoContentReceiver)context.getValue(CoContentReceiver.class));

	}

//	public void handleDrop(Transferable transferable, int pos) {
//		try {
//			Object content = transferable.getTransferData(CoAbstractContentClientConstants.CONTENT_FLAVOR);
//			if (content instanceof CoContentIF)
//				addContent((CoContentIF)content, pos);
//		} catch (UnsupportedFlavorException e) {
//		} catch (IOException e) {
//		}
//
//		try {
//			Object fileList = transferable.getTransferData(DataFlavor.javaFileListFlavor);
//			if (fileList instanceof List)
//				addContent((List)fileList, pos);
//		} catch (UnsupportedFlavorException e) {
//		} catch (IOException e) {
//		}
//		
//	}


}