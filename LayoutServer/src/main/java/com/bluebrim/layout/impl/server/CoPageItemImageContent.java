package com.bluebrim.layout.impl.server;

import java.awt.geom.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.geom.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.image.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * A page item image content projecting an image content.
 * The image content can be assigned directly or by specifying the image order tag.
 * See CoLayoutArea for details on the later case.
 * 
 * @author: Dennis Malmström
 */
public class CoPageItemImageContent extends CoPageItemBoundedContent implements CoPageItemImageContentIF, CoPropertyChangeListener {
	private int m_imageTag = -1; // tag deciding which image in the workpiece to project, -1 means project no image
	private CoImageContentIF m_imageContent;

	private CoColorIF m_imageColor = (CoColorIF) CoFactoryManager.createObject(CoNoColorIF.NO_COLOR);
	private float m_imageColorShade = 100; // %

	// xml tag constants
	public static final String XML_COLOR = "color";
	public static final String XML_ORDER_TAG = "tag";
	public static final String XML_SHADE = "shade";
	public static final String XML_TAG = "page-item-image-content";
	public static final String XML_IMAGE_CONTENT = "image-content";

	public CoPageItemImageContent() {
		super();
	}
	public CoPageItemImageContent(CoImageContentIF image) {
		super();

		setImageContent(image);
	}
	private boolean canSetImage(CoImageContentIF imageContent) {
		if (m_contentLock == FIXED)
			return false; // forbidden
		if (imageContent == m_imageContent)
			return false; // not needed
		if ((m_contentLock == LOCKED) && (imageContent != null) && (m_imageContent != null))
			return false; // forbidden

		return true;
	}

	public CoPageItemIF.State createState() {
		return new CoPageItemImageContentIF.State();
	}

	protected final void doAfterOrderTagChanged() {
		postImageTagChanged();
		markDirty("setOrderTag");
	}
	public Object getAttributeValue(java.lang.reflect.Field d) throws IllegalAccessException {
		try {
			return d.get(this);
		} catch (IllegalAccessException ex) {
			return super.getAttributeValue(d);
		}
	}

	/**
	 * Returns the height of the image in user coordinate space.
	 *
	 * @author: Helena Åberg <helena.aberg@appeal.se> (2001-05-02 10:52:14)
	 */
	protected double getContentHeight() {
		return m_imageContent.getHeight();
	}
	/**
	 * Returns the width of the image in user coordinate space.
	 *
	 * @author: Helena Åberg <helena.aberg@appeal.se> (2001-05-02 10:56:18)
	 */
	protected double getContentWidth() {
		return m_imageContent.getWidth();
	}

	public float getImageColorShade() {
		return m_imageColorShade;
	}
	public CoImageContentIF getImageContent() {
		return m_imageContent;
	}

	public String getName() {
		if (m_imageContent != null) 
			return m_imageContent.getName();
		else
			return "";
	}
	
	public int getOrderTag() {
		return m_imageTag;
	}

	protected String getType() {
		return CoPageItemStringResources.getName(IMAGE_CONTENT);
	}

	/**
	 * Return type name that can be used by a wrapper object.
	 */
	protected String getWrappersType() {
		return CoLayoutServerResources.getName(CoLayoutServerConstants.WRAPPED_IMAGE_CONTENT);
	}

	// Find the workpiece of the first (from here and up) workpiece projecting layout area in the page item ancestor sequence.

	private CoWorkPieceIF getWorkPiece() {
		CoLayoutArea area = (CoLayoutArea) m_owner.getAncestor(CoLayoutAreaIF.class);

		while (area != null) {
			if (area.getWorkPiece() != null)
				return area.getWorkPiece();

			CoCompositePageItem pi = (CoCompositePageItem) area.getParent();

			if (pi == null)
				break;

			area = (CoLayoutArea) pi.getAncestor(CoLayoutAreaIF.class);
		}

		return null;
	}
	protected boolean hasContent() {
		return m_imageContent != null;
	}
	/**
	 * Returns true if the directed acyclic graph of image operations
	 * is empty, false otherwise.
	 *
	 * @author: Helena Åberg <helena.aberg@appeal.se> (2001-05-16 15:05:44)
	 */
	public boolean isEmpty() {
		//return getImageOperationDag() == null;
		return m_imageContent == null;
	}
	private boolean isWorkPieceImageContent() {
		return m_imageTag >= 0;
	}

	// image tag changed

	protected void postImageTagChanged() {
		if (m_owner != null) {
			m_owner.postDistributionBasisChange();
		}
	}

	// image content property changed

	public void propertyChange(CoPropertyChangeEvent ev) {
		doAfterContentChanged();
	}

	public void setImageColorShade(float s) {
		if (m_imageColorShade == s)
			return;

		m_imageColorShade = s;

		markDirty("setImageColorShade");
	}

	public final void setImageContent(CoImageContentIF imageContent) {
		if (!canSetImage(imageContent))
			return;
		if (m_imageContent == imageContent)
			return;

		if (m_imageContent != null) {
			m_imageContent.removePropertyChangeListener(this);
		}

		m_imageContent = imageContent;

		if (m_imageContent != null) {
			m_imageContent.addPropertyChangeListener(this);
		}

		updateCaption();

		doAfterContentChanged();
	}


	public void setOrderTag(int i) {
		if (m_contentLock == FIXED)
			return;
		if (m_imageTag == i)
			return;
		if ((m_contentLock == LOCKED) && (i != -1) && (m_imageTag != -1))
			return;

		m_imageTag = i;

		doAfterOrderTagChanged();
	}

	public void visit(CoPageItemVisitor visitor, Object anything, boolean goDown) {
		visitor.doToImageContent(this, anything, goDown);
	}
	/*
	 * Used at XML export
	 * Helena Rankegård 2001-10-23
	 */

	public void xmlVisit(CoXmlVisitorIF visitor) {
		super.xmlVisit(visitor);

		visitor.exportAttribute(XML_SHADE, Double.toString(getImageColorShade()));
		visitor.exportAsGOIorObject(XML_COLOR, getImageColor());

		if (getOrderTag() < 0) {
			if (getImageContent() != null)
				visitor.exportAsGOIorObject(XML_IMAGE_CONTENT, getImageContent());
		} else {
			visitor.exportAttribute(XML_ORDER_TAG, Integer.toString(getOrderTag()));
		}
	}

	protected void collectState(CoPageItemIF.State s, CoPageItemIF.ViewState viewState) {
		super.collectState(s, viewState);

		CoPageItemImageContentIF.State S = (CoPageItemImageContentIF.State) s;

		S.m_imageContent = getImageContent();

		S.m_imageColor = getImageColor();
		S.m_imageColorShade = getImageColorShade();

		S.m_imageTag = getOrderTag();
		S.m_workPiece = getWorkPiece();
	}

	public static CoContentWrapperPageItemIF createContentWrapper() {
		return new CoContentWrapperPageItem(new CoPageItemImageContent());
	}

	CoPageItemContentView createView(CoContentWrapperPageItemView owner) {
		return new CoPageItemImageContentView(owner, this, (CoPageItemImageContentIF.State) getState(null));
	}

	protected void doDestroy() {
		setImageContent(null);

		super.doDestroy();
	}

	protected CoFormattedTextHolderIF getCaption() {
		return (m_imageContent == null) ? null : m_imageContent.getCaption();
	}

	public CoColorIF getImageColor() {
		return m_imageColor;
	}

	// derive owners shape from an embedded path from the image

	public void setEmbeddedPathShape(String pathName) {
		if (m_imageContent == null)
			return;
		if (!m_imageContent.hasEmbeddedPath())
			return;

		GeneralPath path = (GeneralPath) m_imageContent.getEmbeddedPathMap().get(pathName);

		if (path == null)
			return;

		CoShapeIF shape = new CoCubicBezierCurveShape(path, m_imageContent.getWidth() * m_scaleX, m_imageContent.getHeight() * m_scaleY);

		double x = m_owner.getX() + m_x + shape.getX();
		double y = m_owner.getY() + m_y + shape.getY();

		setPosition(-shape.getX(), -shape.getY());

		shape.setTranslation(0, 0);

		m_owner.setCoShape(shape);
		m_owner.setPosition(x, y);
	}

	public void setImageColor(CoColorIF imageColor) {
		if (m_imageColor == imageColor)
			return;

		m_imageColor = imageColor;

		markDirty("setImageColor");
	}

	/**
	 * Used at XML import
	 * Helena Rankegård 2001-10-23
	 */
	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		CoPageItemImageContent content = new CoPageItemImageContent();
		content.xmlInit(node.getAttributes(), context);
		return content;
	}
	
	

	/**
	 * Used at XML import.
	 * Helena Rankegård 2001-10-30
	 */
	public void xmlInit(NamedNodeMap map, CoXmlContext context) {
		super.xmlInit(map, context);

		// xml init
		m_imageColorShade = CoModelBuilder.getFloatAttrVal(map, XML_SHADE, m_imageColorShade);
		m_imageTag = CoModelBuilder.getIntAttrVal(map, XML_ORDER_TAG, -1);

		if (context.useGOI()) {
			String goiStr = CoModelBuilder.getAttrVal(map, XML_COLOR, null);
			if (goiStr != null) {
				CoColorIF c = ((CoPageItemPreferencesIF)context.getValue(CoLayoutParameters.class)).getColor( new CoGOI( goiStr ) );
	
				if (c == null)
					System.err.println("Warning: could not find color: \"" + goiStr + " \"");
				else
					m_imageColor = c;
			}
	
	
			goiStr = CoModelBuilder.getAttrVal(map, XML_IMAGE_CONTENT, null);
			if (goiStr != null) {
				CoImageContentIF i = ((CoContentRegistry) context.getValue(CoContentRegistry.class)).lookupImageContent(new CoGOI(goiStr));
	
				if (i == null) {
					System.err.println("Warning: could not find image: \"" + goiStr + " \"");
				} else {
					setImageContent(i);
					m_imageTag = -1;
				}
			}
		}
	}

	public void xmlAddSubModel(String parameter, Object subModel, CoXmlContext context) {
		super.xmlAddSubModel(parameter, subModel, context);
		if (!context.useGOI()) {
			if (parameter.equals(XML_COLOR)) {
				m_imageColor = (CoColorIF) subModel;
			} else
				if (parameter.equals(XML_IMAGE_CONTENT)) {
					setImageContent((CoImageContentIF) subModel);
				};
		}
		
	}

}