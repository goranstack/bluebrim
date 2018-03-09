package com.bluebrim.layout.impl.client.transfer;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.compositecontent.client.*;
import com.bluebrim.image.client.*;
import com.bluebrim.layout.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.text.client.*;

/**
 * 
 * Creation date: (2001-08-29 13:24:11)
 * @author Dennis Malmström 
 */
public class CoLayoutEditorDropTargetListener extends CoDropTargetListener {
	private CoLayoutEditor m_editor;

	protected static Stroke m_stroke;
	protected static double m_strokeScale = Double.NaN;

	protected DataFlavor m_currentFlavor = null;
	protected CoAbstractDropOperation m_currentOperation = null;
	protected Point2D m_currentPosition = null;
	protected CoShapePageItemView m_currentTarget = null;

	public static final DataFlavor[] SUPPORTED_FLAVORS =
		new DataFlavor[] {
			CoTextClientConstants.TEXT_CONTENT_FLAVOR,
			CoImageClientConstants.IMAGE_CONTENT_FLAVOR,
			CoLayoutClientConstants.LAYOUT_CONTENT_FLAVOR,
			CoCompositeContentClientConstants.WORK_PIECE_FLAVOR };

	private static Map m_flavor2operatonMap = new HashMap();
	public CoLayoutEditorDropTargetListener(CoLayoutEditor e) {
		super(e.getWorkspace(), SUPPORTED_FLAVORS);

		m_editor = e;
	}

	public boolean canEndDrag(DropTargetDragEvent ev) {
		Point p = ev.getLocation();

		// Get the topmost view that contains current cursor position
		CoShapePageItemView target = m_editor.getWorkspace().getRootView().findTopMostViewContaining(p, null, false, false, -1);

		double bestMatchValue = CoAbstractDropOperation.NO_MATCH;
		CoShapePageItemView bestTarget = null;
		CoAbstractDropOperation bestOperation = null;
		DataFlavor bestFlavor = null;

		Collection availableFlavors = ev.getCurrentDataFlavorsAsList();

		for (int i = 0; i < SUPPORTED_FLAVORS.length; i++) {
			DataFlavor f = SUPPORTED_FLAVORS[i];
			if (availableFlavors.contains(f)) {
				CoAbstractDropOperation op = getDropOperationFor(f);
				if (op == null)
					continue;

				CoShapePageItemView v = target;
				while (v != null) {
					double matchValue = op.operatorMatch(v);
					if (matchValue > bestMatchValue) {
						bestMatchValue = matchValue;
						bestTarget = v;
						bestOperation = op;
						bestFlavor = f;
					}

					v = (CoShapePageItemView) v.getParent();
				}
			}
		}

		performFeedback(bestTarget, bestOperation);

		if (m_currentTarget == null) {
			return false;
		} else {
			m_currentOperation = bestOperation;
			m_currentFlavor = bestFlavor;

			m_currentPosition = new Point2D.Double(p.x, p.y);
			m_editor.getWorkspace().untransform(m_currentPosition);
			((CoShapePageItemView) m_currentTarget).transformFromGlobal(m_currentPosition);
			return m_currentOperation != null;
		}

	}

	public void dragEnter(DropTargetDragEvent ev) {
		m_currentTarget = null;
		m_currentOperation = null;
		m_currentFlavor = null;
		m_currentPosition = null;

		super.dragEnter(ev);
	}

	private void draw(Graphics2D g) {
		if (m_currentTarget != null) {
			Point2D p = new Point2D.Double(0, 0);
			m_currentTarget.transformToGlobal(p);

			g.translate(p.getX(), p.getY());
			g.draw(m_currentTarget.getCoShape().getShape());
			g.translate(-p.getX(), -p.getY());
		}
	}

	private static CoAbstractDropOperation getDropOperationFor(DataFlavor f) {
		return (CoAbstractDropOperation) m_flavor2operatonMap.get(f);

	}

	protected void handleDrop(DropTargetDropEvent ev) {
		try {
			DataFlavor f = m_currentFlavor;
			Object[] droppedData = (f == null) ? null : (Object[]) ev.getTransferable().getTransferData(f);

			if ((m_currentTarget != null) && (m_currentOperation != null) && (droppedData != null) && (droppedData.length == 1)) {
				m_currentOperation.operateOn(m_editor.getCommandExecutor(), m_currentTarget, m_currentPosition, droppedData[0]);
			}
		} catch (UnsupportedFlavorException ex) {
			CoAssertion.assertTrue(false, "Support for this flavor was promised");
		} catch (java.io.IOException ex) {
			CoAssertion.assertTrue(false, "Support for this flavor was promised");
		}
	}

	static {
		// WorkPiece requests
		CoCompoundDropOperation op = new CoCompoundDropOperation();
		op.addOperation(new CoWorkPieceOnLayoutAreaOperation());
		m_flavor2operatonMap.put(CoCompositeContentClientConstants.WORK_PIECE_FLAVOR, op);

		// Text
		op = new CoCompoundDropOperation();
		op.addOperation(new CoTextOnWorkPieceTextContentOperation());
		op.addOperation(new CoTextOnTextContentOperation());
		op.addOperation(new CoTextOnLayoutAreaOperation());
		op.addOperation(new CoTextOnWorkPieceLayoutAreaOperation());
		m_flavor2operatonMap.put(CoTextClientConstants.TEXT_CONTENT_FLAVOR, op);

		// Layout
		op = new CoCompoundDropOperation();
		op.addOperation(new CoLayoutOnLayoutContentOperation());
		op.addOperation(new CoLayoutOnLayoutAreaOperation());
		m_flavor2operatonMap.put(CoLayoutClientConstants.LAYOUT_CONTENT_FLAVOR, op);

		// Image
		op = new CoCompoundDropOperation();
		op.addOperation(new CoImageOnImageContentOperation());
		op.addOperation(new CoImageOnLayoutAreaOperation());
		m_flavor2operatonMap.put(CoImageClientConstants.IMAGE_CONTENT_FLAVOR, op);
	}

	protected void stopFeedback() {
		super.stopFeedback();

		m_editor.setStatusText("");
	}

	protected void performFeedback(CoShapePageItemView newTarget, CoAbstractDropOperation operation) {
		if (m_currentTarget != newTarget) {
			Graphics2D g = m_editor.getWorkspace().getXORGraphics();
			Stroke s = g.getStroke();

			double scale = CoBaseUtilities.getXScale(g.getTransform());
			if ((m_stroke == null) || (scale != m_strokeScale)) {
				m_strokeScale = scale;
				m_stroke = new BasicStroke(5 / (float) m_strokeScale);
			}
			g.setStroke(m_stroke);
			draw(g);
			m_currentTarget = newTarget;
			draw(g);
			g.setStroke(s);

			if (operation == null || newTarget == null) {
				m_editor.setStatusText("");
			} else {
				m_editor.setStatusText(operation.getDescription(newTarget.getName()));
			}

		}

	}
}