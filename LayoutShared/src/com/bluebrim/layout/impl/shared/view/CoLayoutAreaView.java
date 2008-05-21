package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.content.shared.*;
import com.bluebrim.layout.impl.shared.*;

/**
 * Proxy for CoLayoutArea.
 * 
 * @author: Dennis Malmström
 */

public class CoLayoutAreaView extends CoCompositePageItemView {
	public CoPageItemViewRenderer createRenderer(CoPageItemViewRendererFactory f) {
		return f.create(this);
	}

	public boolean hasWorkPiece() {
		return m_workPiece != null;
	}

	private boolean m_acceptsWorkPiece;

	private CoWorkPieceIF m_workPiece;

	public boolean acceptsWorkPiece() {
		return m_acceptsWorkPiece;
	}

	public CoWorkPieceIF getWorkPiece() {
		return m_workPiece;
	}

	protected boolean isAttachedToWorkPiece() {
		return hasWorkPiece();
	}

	protected void paintContentIcon(Graphics2D g) {
		if (acceptsWorkPiece()) {
			g.setColor(Color.black);
			g.draw(CoPageItemViewClientUtilities.m_layoutAraeIconShape);
		}
	}

	private void sync(CoLayoutAreaIF.State d) {
		m_workPiece = d.m_workPiece;
		m_acceptsWorkPiece = d.m_acceptsWorkPiece;
		m_workPieceLock = d.m_workPieceLock;
	}

	// layout area state cache
	private int m_workPieceLock;
	private final static long serialVersionUID = 259220797450690938L;

	public CoLayoutAreaView(
		CoPageItemIF pageItem,
		CoCompositePageItemView parent,
		CoLayoutAreaIF.State d,
		int detailMode) {
		super(pageItem, parent, d, detailMode);

		sync(d);
	}

	public CoLayoutAreaIF getLayoutArea() {
		return (CoLayoutAreaIF) getPageItem();
	}

	public int getWorkPieceLock() {
		return m_workPieceLock;
	}

	public void modelChanged(CoPageItemIF.State d, CoPageItemView.Event ev) {
		super.modelChanged(d, ev);
		sync((CoLayoutAreaIF.State) d);
	}
}