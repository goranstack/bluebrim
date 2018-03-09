package com.bluebrim.layout.impl.shared.view;

import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.layout.shared.*;

/**
 * Used for hiding the fact that a <code>CoLayoutEditorModel</code> 
 * has layers and thereby been able to implement the <code>CoViewable</code>
 * interface
 *
 * Creation date: (2001-03-16 16:31:00)
 * @author: Dennis
 */

public class CoLayoutEditorModel extends CoSimpleObject implements CoNamedViewable {

	private List m_layers = new ArrayList(); // [ local CoLayoutEditorModel.Layer ]
	private int m_indeoxOfActiveLayer = -1;

	private String m_name;

	public class CoViewAggregate extends CoView {
		/*
		 * Return the largest height
		 */
		public double getHeight() {
			double height = 0.0;
			Iterator i = m_layers.iterator();
			while (i.hasNext()) {
				Layer layer = (Layer) i.next();
				height = Math.max(height, layer.m_view.getHeight());
			}
			return height;
		}

		/*
		 * Return the largest width
		 */
		public double getWidth() {
			double width = 0.0;
			Iterator i = m_layers.iterator();
			while (i.hasNext()) {
				Layer layer = (Layer) i.next();
				width = Math.max(width, layer.m_view.getWidth());
			}
			return width;

		}

		public void paint(CoPaintable g) {
			Iterator i = m_layers.iterator();
			while (i.hasNext()) {
				Layer layer = (Layer) i.next();
				layer.m_view.paint(g);
			}
		}

	}


	public class CoFreshViewAggregate extends CoView {
		
		private List m_views = new ArrayList();

		public CoFreshViewAggregate() {
			Iterator i = m_layers.iterator();
			while (i.hasNext()) {
				Layer layer = (Layer) i.next();
				m_views.add(layer.m_pageItem.getView());
			}			
		}
		
		/*
		 * Return the largest height
		 */
		public double getHeight() {
			double height = 0.0;
			Iterator i = m_views.iterator();
			while (i.hasNext()) {
				CoView view = (CoView) i.next();
				height = Math.max(height, view.getHeight());
			}
			return height;
		}

		/*
		 * Return the largest width
		 */
		public double getWidth() {
			double width = 0.0;
			Iterator i = m_views.iterator();
			while (i.hasNext()) {
				CoView view = (CoView) i.next();
				width = Math.max(width, view.getWidth());
			}
			return width;

		}

		public void paint(CoPaintable g) {
			Iterator i = m_views.iterator();
			while (i.hasNext()) {
				CoView view = (CoView) i.next();
				view.paint(g);
			}
		}

	}



	public final class Layer {
		public CoShapePageItemIF m_pageItem;
		public CoShapePageItemView m_view;
	};

	protected CoLayoutEditorModel(String name) {
		CoAssertion.assertTrue(name != null, "Name missing");
		m_name = name;
	}

	public CoLayoutEditorModel(Collection pageItems, String name) // [ CoShapePageItemIF ]
	{
		this(name);

		Iterator i = pageItems.iterator();
		while (i.hasNext()) {
			CoShapePageItemIF pi = (CoShapePageItemIF) i.next();

			Layer l = new Layer();
			l.m_pageItem = pi;

			m_layers.add(l);
		}

		setActiveLayer(m_layers.size() - 1);
	}

	public CoLayoutEditorModel(CoShapePageItemIF pi, String name) {
		this(name);

		Layer l = new Layer();
		l.m_pageItem = pi;

		m_layers.add(l);

		setActiveLayer(0);
	}

	public Layer getActiveLayer() {
		return (m_indeoxOfActiveLayer != -1) ? getLayer(m_indeoxOfActiveLayer) : null;
	}

	public int getIndexOfActiveLayer() {
		return m_indeoxOfActiveLayer;
	}

	public Layer getLayer(int i) {
		return (Layer) m_layers.get(i);
	}

	public int getLayerCount() {
		return m_layers.size();
	}

	public String getName() {
		return m_name;
	}

	public void setActiveLayer(int i) {
		if (i < getLayerCount()) {
			m_indeoxOfActiveLayer = i;
		} else {
			m_indeoxOfActiveLayer = -1;
		}
	}

	/**
	 * Tailored for root view layout manager. Used for position
	 * root views on the desktop
	 */
	public void setViewPosition(double x, double y) {
		int numberOfLayers = getLayerCount();
		for (int i = 0; i < numberOfLayers; i++) {
			CoShapePageItemView layerView = getLayer(i).m_view;
			((CoFixedPositionAdapter) layerView.getAdapter()).setChildPosition(x, y);
		}
	}

	public String toString() {
		return getName() + " (" + super.toString() + ")";
	}

	/**
	 * Return a <code>CoViewAggregate</code> that aggregates the
	 * views presently used in the layout editor. The views state
	 * is affected by their usage as pages on the desktop. This method
	 * was previously used for PostScript generation but gave unwanted
	 * result. Instead of manipulating the state of the views a new method
	 * was created that instantiate new views.
	 */
	public CoView getView() {
		return new CoViewAggregate();
	}

	/**
	 * Return a <code>CoViewAggregate</code> that aggregates views
	 * that is created and has a state that is uneffected by any other
	 * usage.
	 */
	public CoView createView() {
		return new CoFreshViewAggregate();		
	}
}