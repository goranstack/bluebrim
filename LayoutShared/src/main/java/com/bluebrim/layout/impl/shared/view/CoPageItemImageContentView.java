package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.content.shared.*;
import com.bluebrim.image.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.paint.shared.*;

/**
 * View for CoPageItemImageContentView.
 * 
 * @author: Dennis Malmström
 * @since 2001-05-18 Helena Åberg: added image operation DAG
 */
public class CoPageItemImageContentView extends CoPageItemBoundedContentView implements CoImmutableOrderTaggedIF {
	// page item state cache	
	protected int m_imageTag;
	protected CoWorkPieceIF m_workPiece;

	// page item rendering stuff
	protected transient Color m_color;
	// page item state cache	
	protected CoColorIF m_imageColor;
	protected float m_imageColorShade;
	protected final CoImageContentView m_imageView = // view used to paint image
	new CoImageContentView(null) {
		public Container getContainer() {
			return CoPageItemImageContentView.this.getContainer();
		}
	};

	public CoPageItemViewRenderer createRenderer(CoPageItemViewRendererFactory f) {
		return f.create(this);
	}
	public double getContentHeight() {
		return m_imageView.getHeight();
	}
	public double getContentWidth() {
		return m_imageView.getWidth();
	}
	public CoImageContentIF getImageContent() {
		return m_imageView.getModel();
	}
	public int getOrderTag() {
		return m_imageTag;
	}
	public CoPageItemImageContentIF getPageItemImageContent() {
		return (CoPageItemImageContentIF) getPageItem();
	}
	public CoWorkPieceIF getWorkPiece() {
		return m_workPiece;
	}
	public boolean hasContent() {
		return m_imageView.hasModel();
	}
	protected boolean isAcceptingWorkPiece() {
		return m_imageTag != -1;
	}
	protected boolean isAttachedToWorkPiece() {
		return m_workPiece != null;
	}
	protected boolean isContentClipped() {
		if (m_imageView == null)
			return false;

		boolean isClipped = false;

		if (m_owner != null) {
			if (m_x < 0) {
				isClipped = true;
			} else if (m_y < 0) {
				isClipped = true;
			} else if (m_x + m_imageView.getWidth() * m_scaleX - 0.001 > m_owner.getWidth()) {
				isClipped = true;
			} else if (m_y + m_imageView.getHeight() * m_scaleY - 0.001 > m_owner.getHeight()) {
				isClipped = true;
			}
		}

		return isClipped;
	}
	protected boolean isProjectingContent() {
		return hasContent();
	}
	public void modelChanged(CoPageItemIF.State d, CoPageItemView.Event ev) {
		super.modelChanged(d, ev);
		sync((CoPageItemImageContentIF.State) d);
	}
	protected void paintContentIcon(Graphics2D g) {
		int x0 = CoPageItemViewClientUtilities.m_iconX;
		int x1 = CoPageItemViewClientUtilities.m_iconWidth - CoPageItemViewClientUtilities.m_iconX;
		int y0 = CoPageItemViewClientUtilities.m_iconY;
		int y1 = CoPageItemViewClientUtilities.m_iconHeight - CoPageItemViewClientUtilities.m_iconY;

		if (hasCaption()) {
			y1 -= 4;
			g.drawLine(x0, y1, x1, y1);
			g.drawLine(x0, y1 + 2, x1, y1 + 2);
		}

		g.drawLine(x0, y0, x1, y1);
		g.drawLine(x0, y1, x1, y0);

	}
	/**
	 * Synchronizes the view with the CoPageItemImageContent by 
	 * copying state information from the latter.
	 *
	 * @author: Helena Åberg <helena.aberg@appeal.se> (2001-05-18 15:33:46)
	 * 
	 * @param	state	state of the page item image content this view is a proxy for
	 */
	private void sync(CoPageItemImageContentIF.State state) {

		m_imageView.setModel(state.m_imageContent);

		m_imageColor = state.m_imageColor;
		m_imageColorShade = state.m_imageColorShade;
		m_color = null;

		m_imageTag = state.m_imageTag;
		m_workPiece = state.m_workPiece;
	}

	private final static long serialVersionUID = -5915738770067500737L;

	public CoPageItemImageContentView(
		CoContentWrapperPageItemView owner,
		CoPageItemIF pageItem,
		CoPageItemImageContentIF.State d) {
		super(owner, pageItem, d);

		sync(d);
	}
}