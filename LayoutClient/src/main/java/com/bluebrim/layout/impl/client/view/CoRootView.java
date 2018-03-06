package com.bluebrim.layout.impl.client.view;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.observable.*;

/**
 * Proxy for CoDesktopLayoutArea.
 * CoRootView is always the root of its view tree (it can't have a parent).
 * It is the link between the views and the swing component that displays them.
 * Unlike most page item views, the root view is created on the client.
 *  
 * CoRootView also has a set of page items called models and maintains views for them.
 * These page items however are not children of the desktop page item.
 * It is the task of the desktop view to display both the desktop page items and its children together with
 * the model page items.
 * While the true children of the desktop can be moved around like any page item children, the model page item views
 * are positioned by a layout manager.
 * Model views are drawn after the desktop, so they always appear to be on top of the desktop children.
 *
 * CoRootView implements a set of custom grid lines.
 *
 * It is the responsibility of the root view to handle structural changes (addition and removal of children) in the page item trees of the desktop and the models.
 * 
 * @author: Dennis Malmström
 */

public class CoRootView extends CoCompositePageItemView implements CoPageItemView.Listener {
	private static final CoRootViewRenderer m_renderer = new CoRootViewRenderer();
	private CoPageItemViewRendererFactory m_rendererFactory;

	protected CoPageItemEditorPanel m_container; // link to swing-component

	protected List m_modelViews = new ArrayList();

	// custom grid stuff
	protected CoImmutableCustomGridIF m_customGrid;

	protected CoGridRenderer m_customGridRenderer;

	private boolean m_doShowEntireDesktop = false;
	// dimension the view by the model views and desktop children (true) or by the models views only (false)

	private Dimension2D m_modelSize = new CoDimension2D(); // dimensions used by the laid out model views
	private Dimension2D m_preferredSize = new CoDimension2D(400, 300); // preferred size of the root view

	protected List m_recentlyCreatedPageItems = new ArrayList();
	// list of page item recently created by the layout editor owning this root view

	// model view layout manager stuff
	private CoRootViewLayoutManager m_layoutManager = new CoVerticalRootViewLayoutManager();


	public void addRecentlyCreatedPageItem(CoPageItemIF pi) {
		m_recentlyCreatedPageItems.add(pi);
	}

	public void attachContainer(CoPageItemEditorPanel container) {
		m_container = container;
	}
	// desktop is considered infinite

	protected boolean containsPoint(Point2D point) {
		return true;
	}
	public CoPageItemViewRenderer createRenderer(CoPageItemViewRendererFactory f) {
		return m_renderer;
	}
	public void dettachContainer() {
		m_container = null;
	}
	// propagate to model views

	public void dispose() {
		super.dispose();

		Iterator e = m_modelViews.iterator();
		while (e.hasNext()) {
			((CoShapePageItemView) e.next()).dispose();
		};

	}

	// desktop has no clipping

	protected Shape getClipping() {
		return null;
	}

	public boolean getDoShowEntireDesktop() {
		return m_doShowEntireDesktop;
	}
	// get graphics object for hit testing

	protected Graphics2D getHitTestGraphics() {
		if (m_container == null)
			return null;
		Graphics2D g = (Graphics2D) m_container.getGraphics();

		return g;
	}
	public List getModels() {
		return m_layoutEditorModels;
	}
	public Dimension2D getModelSize() {
		return m_modelSize;
	}
	public List getModelViews() {
		return m_modelViews;
	}
	public Dimension2D getPreferredSize() {
		return m_preferredSize;
	}
	protected CoPageItemViewRenderer getRenderer() {
		return m_renderer;
	}
	public CoPageItemViewRendererFactory getRendererFactory() {
		return m_rendererFactory;
	}
	// find the reshape handle containing a given point

	public CoReshapeHandleIF getReshapeHandleContaining(Point2D p) {
		double x = p.getX();
		double y = p.getY();

		Point2D P = new Point2D.Double();

		// only selected views have handles
		int I = getSelectionManager().getSelectedViewCount();
		for (int i = 0; i < I; i++) {
			double X;
			double Y;

			CoReshapeHandleIF[] hs = getSelectionManager().getSelectedView(i).getShape().getReshapeHandles();
			for (int n = 0; n < hs.length; n++) {
				P.setLocation(hs[n].getX(), hs[n].getY());
				getSelectionManager().getSelectedView(i).getView().transformToGlobal(P);
				m_container.transform(P);
				X = P.getX();
				Y = P.getY();
				if (X < x + CoSelectedView.HANDLE_OFFSET - CoSelectedView.HANDLE_RANGE)
					continue;
				if (Y < y + CoSelectedView.HANDLE_OFFSET - CoSelectedView.HANDLE_RANGE)
					continue;
				if (X > x + CoSelectedView.HANDLE_OFFSET + CoSelectedView.HANDLE_RANGE)
					continue;
				if (Y > y + CoSelectedView.HANDLE_OFFSET + CoSelectedView.HANDLE_RANGE)
					continue;
				return hs[n];
			}
		}

		return null;
	}
	protected double getScreenScale() {
		if (m_container == null)
			return 0;
		return m_container.getScreenScale();
	}
	public CoViewSelectionManager getSelectionManager() {
		return m_container.getSelectionManager();
	}
	// find the view that is associated with a given reshape handle

	public CoShapePageItemView getViewOwningReshapeHandle(CoReshapeHandleIF h) {
		int I = getSelectionManager().getSelectedViewCount();
		for (int i = 0; i < I; i++) {
			CoSelectedView v = getSelectionManager().getSelectedView(i);

			CoReshapeHandleIF[] hs = v.getShape().getReshapeHandles();
			for (int n = 0; n < hs.length; n++) {
				if (hs[n] == h)
					return v.getView();
			}
		}

		return null;
	}
	// find the view that is associated with a move handle that contains a given point

	public CoShapePageItemView getViewWithMoveHandleContaining(Point2D p) {
		double x = p.getX();
		double y = p.getY();

		Point2D P = new Point2D.Double();

		int I = getSelectionManager().getSelectedViewCount();
		for (int i = 0; i < I; i++) {
			CoShapePageItemView v = getSelectionManager().getSelectedView(i).getView();

			P.setLocation((double) 0, (double) 0);
			v.transformToGlobal(P);
			m_container.transform(P);

			double X = P.getX() + CoSelectedView.HANDLE_OFFSET - CoSelectedView.MOVE_HANDLE_OFFSET;
			double Y = P.getY() + CoSelectedView.HANDLE_OFFSET;

			if (x < X - CoSelectedView.HANDLE_RANGE)
				continue;
			if (y < Y - CoSelectedView.HANDLE_RANGE)
				continue;
			if (x > X + CoSelectedView.HANDLE_RANGE)
				continue;
			if (y > Y + CoSelectedView.HANDLE_RANGE)
				continue;

			return v;
		}

		return null;
	}

	public boolean isModelView(CoPageItemView v) {
		Iterator e = m_modelViews.iterator();
		while (e.hasNext()) {
			if (e.next() == v)
				return true;
		}

		return false;
	}
	// desktop can't be moved

	public boolean isMoveable() {
		return false;
	}
	// desktop can't be reshaped

	public boolean isReshapeable() {
		return false;
	}
	// desktop can't be selected

	public boolean isSelectable() {
		return false;
	}

	// dispatch reparent events

	protected void postHandleReparent() {
		getSelectionManager().endSelectionTransaction();
	}
	// dispatch reparent events

	protected void preHandleReparent() {
		getSelectionManager().beginSelectionTransaction();
	}
	// propagate to model views and selection manager

	public void refresh() {
		super.refresh();

		if (m_modelViews != null) {
			Iterator e = m_modelViews.iterator();
			while (e.hasNext()) {
				CoShapePageItemView v = (CoShapePageItemView) e.next();
				v.refresh();
			};
		}

		getSelectionManager().refresh();
	}
	public void setDoShowEntireDesktop(boolean b) {
		m_doShowEntireDesktop = b;
		updateDesktopGeometry();
	}

	public void setRenderer(CoPageItemViewRendererFactory f) {
		if (m_rendererFactory == f)
			return;

		m_rendererFactory = f;

		Iterator e = m_children.iterator();
		while (e.hasNext()) {
			((CoShapePageItemView) e.next()).setRenderer(f);
		};

		e = m_modelViews.iterator();
		while (e.hasNext()) {
			((CoShapePageItemView) e.next()).setRenderer(f);
		};

		repaintAll();
	}

	// propagate to model views

	public void updateAbsoluteGeometryCache(double scale) {
		super.updateAbsoluteGeometryCache(scale);

		if (m_modelViews != null) {
			Iterator e = m_modelViews.iterator();
			while (e.hasNext()) {
				CoShapePageItemView v = (CoShapePageItemView) e.next();
				v.updateAbsoluteGeometryCache(scale);
			};
		}
	}
	private void updateDesktopGeometry() {
		m_modelSize = m_layoutManager.layout(this);

		Insets i = m_layoutManager.getInsets();
		if (i != null) {
			double w = m_modelSize.getWidth() + i.left + i.right;
			double h = m_modelSize.getHeight() + i.top + i.bottom;
			m_preferredSize = new CoDimension2D(w, h);
		} else {
			m_preferredSize = m_modelSize;
		}

		m_container.updateSize();

		updateAbsoluteGeometryCache(getScreenScale());
		getSelectionManager().refresh();

		m_container.revalidate();
	}
	protected void updateViews() {
		// destroy previous views
		if (m_modelViews.size() > 0) {
			Iterator e = m_modelViews.iterator();
			while (e.hasNext()) {
				CoShapePageItemView modelView = (CoShapePageItemView) e.next();
				modelView.setParent(null);
				removePageItemListeners(modelView);
				modelView.removeViewListener(this);
				modelView.dispose();
			}
		}
		m_modelViews.clear();

		// insert new views
		Iterator e = m_layoutEditorModels.iterator();
		while (e.hasNext()) {
			CoLayoutEditorModel model = (CoLayoutEditorModel) e.next();
			int I = model.getLayerCount();
			for (int i = 0; i < I; i++) {
				CoLayoutEditorModel.Layer layer = model.getLayer(i);

				CoShapePageItemIF pi = layer.m_pageItem;
				layer.m_view = wrapModelView(CoPageItemView.create(pi, m_rendererFactory, m_detailMode)); //, model.getSize() );
				m_modelViews.add(layer.m_view);
				layer.m_view.setParent(this);
				createPageItemListeners(layer.m_view);
				layer.m_view.addViewListener(this);
			}
		}

		updateDesktopGeometry();

	}

	// propagate to model views

	public boolean visit(CoPageItemViewVisitor visitor) {
		boolean b = super.visit(visitor);

		Iterator e = m_modelViews.iterator();
		while (b && e.hasNext()) {
			b = b && ((CoShapePageItemView) e.next()).visit(visitor);
		}

		return b;
	}

	// models
	private List m_layoutEditorModels = new ArrayList();

	public CoRootView(CoDesktopLayoutAreaIF desktopModel) {
		super(desktopModel, null, (CoCompositePageItemIF.State) desktopModel.getState(null), DETAILS_EVERYTHING);

		// create views for desktop children
		Iterator e = desktopModel.getChildren().iterator();
		while (e.hasNext()) {
			CoShapePageItemIF s = (CoShapePageItemIF) e.next();
			CoShapePageItemView v = CoPageItemView.create(s, m_rendererFactory, m_detailMode);
			add(v);
			v.setParent(this);
		};

		// create server object listeners for entire view tree
		createPageItemListeners(this);
	}

	protected void paintIcon(Graphics2D g) {
	}

	public void setModels(List models) {
		// clear previous models
		m_layoutEditorModels.clear();
		getSelectionManager().unselectAllViews();

		// insert new models
		if (models != null) {
			m_layoutEditorModels.addAll(models);
		}

		updateViews();
	}

	private static final CollectViewsForVisitor m_collectViewsForVisitor = new CollectViewsForVisitor();
	private static final CoPageItemViewVisitor m_listenVisitor = new CoPageItemViewVisitor() {
		public boolean visitCompositePageItemView(CoCompositePageItemView v) {
			listen(v);
			return true;
		}

		public boolean visitLeafView(CoShapePageItemView v) {
			listen(v);
			return true;
		}

		public boolean visitContentView(CoPageItemContentView v) {
			return true;
		}

		private void listen(CoShapePageItemView v) {
			CoObservable.addChangedObjectListener(v.createPageItemListener(), v.getPageItem());
		}
	};
	private static final CoPageItemViewVisitor m_unlistenVisitor = new CoPageItemViewVisitor() {
		public boolean visitCompositePageItemView(CoCompositePageItemView v) {
			unlisten(v);
			return true;
		}

		public boolean visitLeafView(CoShapePageItemView v) {
			unlisten(v);
			return true;
		}

		public boolean visitContentView(CoPageItemContentView v) {
			return true;
		}

		private void unlisten(CoShapePageItemView v) {
			CoObservable.removeChangedObjectListener(v.getAndClearPageItemListener(), v.getPageItem());
		}
	};

	private static class CollectViewsForVisitor extends CoPageItemViewVisitor {
		private com.bluebrim.transact.shared.CoRef m_targetId;
		private boolean m_findAll;
		private List m_found = new ArrayList(); // [ CoPageItemView ]

		public void setTarget(com.bluebrim.transact.shared.CoRef targetId, boolean findAll) {
			m_findAll = findAll;
			m_targetId = targetId;
			m_found.clear();
		}

		public List getHits() {
			return m_found;
		}

		public CoPageItemView getHit() {
			if (m_found.size() == 0)
				return null;
			return (CoPageItemView) m_found.get(0);
		}

		public boolean visitLeafView(CoShapePageItemView leaf) {
			return test(leaf);
		}

		public boolean visitCompositePageItemView(CoCompositePageItemView composite) {
			return test(composite);
		}

		private boolean test(CoPageItemView v) {
			if (v.getPageItemId().equals(m_targetId)) {
				m_found.add(v);
				if (!m_findAll)
					return false;
			}
			return true;
		}
	}

	public void checkDesktop(CoDesktopLayoutAreaIF defaultDesktop) {
		CoDesktopLayoutAreaIF desktop = defaultDesktop;

		// single model -> use desktop of active layer
		if (m_layoutEditorModels.size() == 1) {
			CoLayoutEditorModel model = (CoLayoutEditorModel) m_layoutEditorModels.get(0);
			CoShapePageItemIF activeLayer = model.getActiveLayer().m_pageItem;
			desktop = activeLayer.getDesktop();
		}

		setDesktop(desktop, desktop.getId());
	}

	public static void createPageItemListeners(CoPageItemView v) {
		v.visit(m_listenVisitor);
	}

	// find custom grid line closest to a given point

	public double findClosestXCustomGridLine(CoShapePageItemView v, Point2D pos, double range) {
		Point2D P = new Point2D.Double(pos.getX(), range);

		m_container.untransform(P);
		range = P.getY();

		v.transformFromGlobal(P);

		double x = P.getX();

		return v.getCustomGrid().findXPosition(x, range);
	}

	// find custom grid line closest to a given point

	public double findClosestYCustomGridLine(CoShapePageItemView v, Point2D pos, double range) {
		Point2D P = new Point2D.Double(range, pos.getY());

		m_container.untransform(P);
		range = P.getX();

		v.transformFromGlobal(P);

		double y = P.getY();

		return v.getCustomGrid().findYPosition(y, range);
	}

	// traverse the page item view tree and find the views representing a given page item

	public CoPageItemView findFirstViewFor(com.bluebrim.transact.shared.CoRef targetId) {
		m_collectViewsForVisitor.setTarget(targetId, false);
		visit(m_collectViewsForVisitor);
		return m_collectViewsForVisitor.getHit();
	}

	// find a model view containing a given point

	public CoShapePageItemView findModelViewContaining(Point2D point) {
		return findTopMostViewContaining(point, getChildren(), true, true, 1);
	}

	public CoShapePageItemView findTopMostViewContaining(
		Point2D point,
		List exclusionSet,
		boolean excludeLeafs,
		boolean ignoreChildLock,
		int maxDepth) {
		// compensate for view scale
		double s = getScreenScale();
		if (s != 1) {
			point = new Point2D.Double(point.getX() / s, point.getY() / s);
		}

		// test children
		CoShapePageItemView v = super.findTopMostViewContaining(point, exclusionSet, excludeLeafs, ignoreChildLock, maxDepth);

		// test model views
		if (v == this) // PENDING: what happens if a model overlaps a child ???
			{
			int I = m_layoutEditorModels.size();
			for (int i = I - 1; i >= 0; i--) {
				CoLayoutEditorModel model = (CoLayoutEditorModel) m_layoutEditorModels.get(i);

				CoShapePageItemView w = model.getActiveLayer().m_view;

				if (w != null) {
					CoShapePageItemView c = w.findTopMostViewContaining(point, exclusionSet, excludeLeafs, ignoreChildLock, maxDepth - 1);

					if (c != null) {
						v = c;
						break;
					}
				}
			}

		}

		return v;
	}

	// traverse the page item view tree and find the views representing a given page item

	public List findViewsFor(com.bluebrim.transact.shared.CoRef targetId) {
		m_collectViewsForVisitor.setTarget(targetId, true);
		visit(m_collectViewsForVisitor);
		return m_collectViewsForVisitor.getHits();
	}

	public final Container getContainer() {
		return m_container;
	}

	public CoImmutableCustomGridIF getCustomGrid() {
		return m_customGrid;
	}

	public CoCustomGridIF getMutableCustomGrid() {
		return ((CoDesktopLayoutAreaIF) getPageItem()).getMutableCustomGrid();
	}

	// dispatch reparent events

	protected void handleReparent(
		CoShapePageItemIF child,
		com.bluebrim.transact.shared.CoRef childId,
		CoCompositePageItemIF newParent,
		com.bluebrim.transact.shared.CoRef newParentId,
		CoCompositePageItemIF oldParent,
		com.bluebrim.transact.shared.CoRef oldParentId) {
		if (child == null) {
			return;
		}

		if (oldParent == null) {
			pageItemCreated(child, childId, newParent, newParentId);

		} else
			if (newParent == null) {
				pageItemDeleted(child, childId);

			} else {

				pageItemReparented(child, childId, oldParent, oldParentId, newParent, newParentId);
			}

		getSelectionManager().fireSelectionChanged();

		//	repaint();
	}

	public boolean isActive(CoShapePageItemView v) {
		// find top view
		while ((v != null) && (v.getParent() != this)) {
			v = v.getParent();
		}

		if (v == null)
			return false;

		// is it active ?
		int I = m_layoutEditorModels.size();
		for (int i = 0; i < I; i++) {
			if (v == ((CoLayoutEditorModel) m_layoutEditorModels.get(i)).getActiveLayer().m_view)
				return true;
		}

		return false;
	}

	public void modelChanged(CoPageItemIF.State d, CoPageItemView.Event ev) {
		super.modelChanged(d, ev);
		sync((CoDesktopLayoutAreaIF.State) d);
	}

	private void pageItemCreated(
		CoShapePageItemIF child,
		com.bluebrim.transact.shared.CoRef childId,
		CoCompositePageItemIF newParent,
		com.bluebrim.transact.shared.CoRef newParentId) {
		if (findFirstViewFor(childId) != null) {
			return; // allready handled
		}

		// traverse the views of the new parent
		List newParents = findViewsFor(newParentId);

		Iterator i = newParents.iterator();
		while (i.hasNext()) {
			CoCompositePageItemView newParentView = (CoCompositePageItemView) i.next();

			// create and prepare child view
			CoShapePageItemView childView = CoPageItemView.create(child, m_rendererFactory, m_detailMode);
			createPageItemListeners(childView);

			// insert child view as child in parent view
			int n = newParent.getIndexOfChild(child);
			newParentView.add(n, childView);

			// select child if created by this client
			if (m_recentlyCreatedPageItems.contains(child)) {
				getSelectionManager().select(childView, false);
				m_recentlyCreatedPageItems.remove(child);
			}

			fireViewChanged(
				newParentView,
				false,
				CoPageItemView.Event.STRUCTURAL_CHANGE_ADD,
				childView,
				newParentView.getIndexOfChild(childView));
			childView.repaint();
		}

	}

	private void pageItemDeleted(CoPageItemIF child, com.bluebrim.transact.shared.CoRef childId) {
		// traverse the views of the child
		Iterator i = findViewsFor(childId).iterator();
		while (i.hasNext()) {
			CoShapePageItemView childView = (CoShapePageItemView) i.next();
			childView.repaint();

			// unselect child view
			getSelectionManager().unselect(childView);
			if (childView instanceof CoCompositePageItemView)
				getSelectionManager().unselectChildren((CoCompositePageItemView) childView, false);

			// remove from parent view
			CoCompositePageItemView oldParentView = childView.getParent();
			int index = oldParentView.getIndexOfChild(childView);
			oldParentView.remove(childView);

			// destroy child view
			removePageItemListeners(childView);
			childView.dispose();

			fireViewChanged(oldParentView, false, CoPageItemView.Event.STRUCTURAL_CHANGE_REMOVE, childView, index);
		}
	}

	private void pageItemReparented(
		CoShapePageItemIF child,
		com.bluebrim.transact.shared.CoRef childId,
		CoCompositePageItemIF oldParent,
		com.bluebrim.transact.shared.CoRef oldParentId,
		CoCompositePageItemIF newParent,
		com.bluebrim.transact.shared.CoRef newParentId) {
		//	List selected = getSelectionManager().getSelectedViews( null );

		// find child view
		//	CoShapePageItemView childView = (CoShapePageItemView) findFirstViewFor( child );

		List childViews = findViewsFor(childId);
		List reinsertableChildViews = new ArrayList();

		Iterator i = childViews.iterator();
		while (i.hasNext()) {
			CoShapePageItemView childView = (CoShapePageItemView) i.next();
			childView.repaint();

			// if model of old parent view is not consistant with reparent operation record then
			// this reparent operation was already handled (the same record is sent to both parents, if they share the same root view then two calls will reach this method)
			CoCompositePageItemView oldParentView = childView.getParent();
			if (!oldParentView.getPageItemId().equals(oldParentId)) {
				// this event has already been handled
				return;
			}

			// remove child view from old parent view
			int index = oldParentView.getIndexOfChild(childView);
			oldParentView.remove(childView);
			fireViewChanged(oldParentView, false, CoPageItemView.Event.STRUCTURAL_CHANGE_REMOVE, childView, index);

			if (getSelectionManager().isSelected(childView)) {
				reinsertableChildViews.add(0, childView);
			} else {
				reinsertableChildViews.add(childView);
			}
		}

		List newParentViews = findViewsFor(newParentId);

		i = newParentViews.iterator();
		while (i.hasNext()) {
			CoCompositePageItemView newParentView = (CoCompositePageItemView) i.next();

			CoShapePageItemView childView = null;
			if (reinsertableChildViews.isEmpty()) {
				childView = CoPageItemView.create(child, m_rendererFactory, m_detailMode);
				createPageItemListeners(childView);
			} else {
				childView = (CoShapePageItemView) reinsertableChildViews.remove(0);
			}

			// insert child view into new parent view
			int n = newParent.getIndexOfChild(child);
			newParentView.add(n, childView);

			fireViewChanged(newParentView, false, CoPageItemView.Event.STRUCTURAL_CHANGE_ADD, childView, n);

			childView.refresh(); // PENDING: only refresh textboxes
		}

		i = reinsertableChildViews.iterator();
		while (i.hasNext()) {
			CoShapePageItemView childView = (CoShapePageItemView) i.next();
			getSelectionManager().unselect(childView);
			if (childView instanceof CoCompositePageItemView)
				getSelectionManager().unselectChildren((CoCompositePageItemView) childView, false);
			removePageItemListeners(childView);
			childView.dispose();
		}
	}

	public static void removePageItemListeners(CoPageItemView v) {
		v.visit(m_unlistenVisitor);
	}

	/**
	 * PENDING: Changed this method to avoid NullPointerException without
	 * finding the real cause for null in m_container /Göran S 
	 */
	public void repaint(int x, int y, int w, int h, int frameWidth) {
		if (m_container == null) // Should this happend? 
			return;
		if (frameWidth == 0) {
			// compensate for selection handles

			int d = (int) Math.ceil(CoSelectedView.HANDLE_SIZE / 2f);

			x -= CoSelectedView.MOVE_HANDLE_OFFSET + d;
			y -= d;
			w += CoSelectedView.MOVE_HANDLE_OFFSET + CoSelectedView.HANDLE_SIZE;
			h += CoSelectedView.HANDLE_SIZE;

			m_container.repaint(x, y, w, h);
		} else {

			x -= frameWidth / 2;
			y -= frameWidth / 2;

			m_container.repaint(
				x - CoSelectedView.MOVE_HANDLE_OFFSET,
				y,
				w + frameWidth + CoSelectedView.MOVE_HANDLE_OFFSET - 1,
				frameWidth);
			m_container.repaint(x, y + h - 1, w + frameWidth - 1, frameWidth);

			y += frameWidth;
			h -= frameWidth + 1;

			m_container.repaint(x, y, frameWidth, h);
			m_container.repaint(x + w - 1, y, frameWidth, h);

		}
	}

	/**
	 * PENDING: Changed this method to avoid NullPointerException without
	 * finding the real cause for null in m_container /Göran S 
	 */
	protected void repaintAll() {
		if (m_container != null)
			m_container.repaint();
		else
			repaint();
	}

	private void setDesktop(CoDesktopLayoutAreaIF desktop, com.bluebrim.transact.shared.CoRef desktopId) {
		if (desktop == null) {
			//????
			return;
		}

		if (desktopId.equals(getPageItemId()))
			return;

		removePageItemListeners(this);

		Iterator i = getChildren().iterator();
		while (i.hasNext()) {
			CoShapePageItemView v = (CoShapePageItemView) i.next();
			v.dispose();
		};
		m_children.clear();

		m_pageItem = desktop;

		// create views for desktop children
		Iterator e = desktop.getChildren().iterator();
		while (e.hasNext()) {
			CoShapePageItemIF s = (CoShapePageItemIF) e.next();
			CoShapePageItemView v = CoPageItemView.create(s, m_rendererFactory, m_detailMode);
			add(v);
			v.setParent(this);
		};

		createPageItemListeners(this);

		/*
		super.refresh();
		getSelectionManager().refresh();
		*/
	}

	public void setLayoutManager(CoRootViewLayoutManager l) {
		m_layoutManager = l;
		updateDesktopGeometry();
	}

	public Point2D snap(double x, double y, double width, double height, double range, Point2D delta) {
		return getCustomGrid().snap(x, y, width, height, range, delta);
	}

	public Point2D snap(double x, double y, double range, int edgeMask, int dirMask, boolean useEdges, Point2D delta) {
		return getCustomGrid().snap(x, y, range, edgeMask, dirMask, useEdges, delta);
	}

	private void sync(CoDesktopLayoutAreaIF.State d) {
		//	m_customGrid.setSize( m_width, m_height );
		m_customGrid = d.m_customGrid;
		m_customGridRenderer = null;
	}

	public void transformFromGlobal(Point2D p) {
		transformFromParent(p);
	}

	protected void transformFromParent(Point2D p) {
	}

	public void transformToGlobal(Point2D p) {
		transformToParent(p);
	}

	protected void transformToParent(Point2D p) {
	}

	public void viewChanged(
		CoPageItemView.Event ev) //CoPageItemView view, boolean boundsChanged, int structuralChange, CoShapePageItemView child, int index )
	{
		if (ev.didBoundsChange()) {
			updateDesktopGeometry();
		}
	}

	// models are wrapped in CoFixedPositionWrappers to keep them in place

	protected CoShapePageItemView wrapModelView(CoShapePageItemView v) {
		v.setAdapter(new CoFixedBoundsAdapter(null, null, null, false, false, false));
		return v;
	}
}