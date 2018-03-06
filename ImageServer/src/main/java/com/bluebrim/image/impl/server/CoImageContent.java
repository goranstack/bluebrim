package com.bluebrim.image.impl.server;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.content.server.*;
import com.bluebrim.image.server.*;
import com.bluebrim.image.shared.*;
import com.bluebrim.text.impl.server.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Class for the administrative objects that represents images.
 *
 * This class is intended to handle all types of images. Currently, this
 * is implemented by composition, but it used to be a class hierarchy rooted
 * in CoImageContent. It is the intention of the authors that the choice of
 * implementation should be transparent to anything outside of this hierarchy.
 *
 * Consequently, all images should be created by using a static createImage
 * method in CoImageContent. To enforce this, all constructors are protected.
 * Note also that CoContentStorage has a factory method that can be used from
 * the client, although this implies the need for some sort of remote stream not
 * yet implemented.
 *
 * @author Göran Stäck
 * @author Markus Persson
 */
public class CoImageContent extends CoAtomicContent implements CoImageContentIF  {

	public static final String XML_TAG = "image-content";

	private CoImage m_image;
	private CoCaption m_caption;
	private boolean m_dirtyFlag;
	private Map m_embeddedPathMap;

	/**
	 * This constructor builds an empty instance.  It should be populated by using the
	 * {@link init(InputStream)} method.  Using the empty instance without populating it
	 * is a bad idea.
	 */
	public CoImageContent() {
		super(null);
		setName(CoImageServerResources.getName(CoImageServerConstants.UNTITLED_IMAGE));
	}
	
	public CoImageContent(File file, BufferedImage image) throws IOException {
		this();
		m_image = new CoFileBasedImage(file, image);
		m_caption = new CoCaption(this);
		setName(file.getName());
	}
	
	public CoImageContent(File file) throws IOException {
		this();
		m_image = new CoFileBasedImage(file);
		m_caption = new CoCaption(this);
		setName(file.getName());		
	}
	
	private static String fileNameToUpperCase(String name) {
		String result;
		if (name == null) {
			return name;
		}
		int index = name.lastIndexOf('/');
		if (index != -1) {
			result = name.substring(index + 1).toUpperCase();
			result = name.substring(0, index + 1) + result;
		} else
			result = name.toUpperCase();
		return result;
	}
	/**
	 * @author Dennis (formatting excluded) 
	 */
	public CoFormattedTextHolderIF getCaption() {
		return m_caption;
	}
	/**
	 * Returns the map of embedded paths in the image or null
	 * if embedded paths are not supported. The map contains
	 * pairs of path names and GeneralPath objects
	 * (@see {@link CoTIFFDecoder})
	 * 
	 * @author: Helena Åberg <helena.aberg@appeal.se> (2001-10-09 14:52:44)
	 */
	public Map getEmbeddedPathMap() {
		return m_embeddedPathMap;
	}
	public String getFactoryKey() {
		return CoImageContentIF.FACTORY_KEY;
	}

	public int getHeight() {
		return m_image.getHeight();
	}
	public String getIconName() {
		return ICON_NAME;
	}
	public String getIconResourceAnchor() {
		return CoImageContentIF.class.getName();
	}
	public String getIdentity() {
		return getName();
	}

	public CoImage getImage() {
		return m_image;
	}

	public int getWidth() {
		return m_image.getWidth();
	}
	
	public CoView getView() {
		return new CoImageContentView(this);
	}
	/**
	 * Returns true if the image contains one or more
	 * embedded paths. Returns false if no paths exist
	 * or if the image does not support embedded paths.
	 *
	 * Creation date: (2001-10-09 16:14:31)
	 * @author Helena Åberg <helena.aberg@appeal.se>
	 */
	public boolean hasEmbeddedPath() {
		return (getEmbeddedPathMap() != null && !getEmbeddedPathMap().isEmpty());
	}

	public void init(File file) {
		try {
			m_image = new CoFileBasedImage(file);
		} catch (IOException e) {
		}
		m_caption = new CoCaption(this);

	}

	/**
	 * This may not be the best solution. See appearanceChange() too. /Markus 2000-06-14
	 *
	 * @author Dennis (formatting excluded)
	 */
	public void markDirty() {
		m_dirtyFlag = !m_dirtyFlag;

		if (CoAssertion.SIMULATION_SUPPORT)
			CoAssertion.addChangedObject(this);
	}
	protected void prepareCopy(CoContent copy) {
		super.prepareCopy(copy);

		CoImageContent imageContent = (CoImageContent) copy;

		imageContent.m_caption = new CoCaption(imageContent);
	}
	
	protected static Map readEmbeddedPaths(InputStream stream) {
		return null;
//		Map pathMap = null;
//		try {
//			stream.reset();
//			CoImageDecoder dec = CoImageCodec.createImageDecoder(stream);
//			pathMap = dec.getEmbeddedPaths();
//			stream.close();
//			return pathMap;
//		} catch (IOException e) {
//			e.printStackTrace(System.err);
//			try {
//				stream.close();
//			} finally {
//				// In case any of the close:s failed, we might still have a map...
//				return pathMap;
//			}
//		}
	}

	
	public void xmlVisit(CoXmlVisitorIF visitor) {
		super.xmlVisit(visitor);
		// Export the image as an attachement file
		m_image.xmlVisit(visitor);

		// Export the caption
		if (m_caption.getFormattedText( null).getText().length() > 0)
			visitor.export(CoXmlWrapperFlavors.NAMED, "caption", m_caption.getFormattedText( null));
	}

    public String getType()
    {
       	return CoImageServerResources.getName( IMAGE_CONTENT );    
    }
}
