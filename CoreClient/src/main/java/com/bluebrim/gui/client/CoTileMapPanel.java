package com.bluebrim.gui.client;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import com.bluebrim.base.client.datatransfer.*;

/**
 * Draft of widget that visualizes a collection like a map of tiles.
 * Similar in some respects to JList.
 *
 * PENDING: Fix the following scroll related bugs:
 *		* Scrollbar takes an additional resize to appear/disappear.
 *		  Fix with layoutmanager in scrollpane/viewport.
 *		* Upper left item should remain upper left after resize.
 *		  Fix in syncXXX method.
 *		* When scrolling, all visible tiles are repainted every time.
 *
 * PENDING: Move to CalvinBase?
 *
 * @author Markus Persson 1999-09-06
 */
public class CoTileMapPanel extends JComponent implements Scrollable, DragGestureListener {
	protected int m_preferredTilesPerRow = 4;
	protected int m_tilesPerRow = m_preferredTilesPerRow;
	protected int m_tileWidth = 80;
	protected int m_tileHeight = 64;
	protected int m_tiles = 4;

	protected TileRenderer m_renderer;
	protected ListModel m_listModel;
	protected ListSelectionModel m_selectionModel;

	private MouseInputListener m_mouseInputListener;
	private ListSelectionListener m_listSelectionListener;
	private ListDataListener m_listDataListener;
	private CoDnDDataProvider m_dragDataProvider;

	// Inner interface
	private static interface IntIterator {
		public boolean hasNext();
		public int next();
	};

	// Inner interface
	public static interface TileRenderer {
		public void render(Graphics2D g2d, Object object, int width, int height, boolean selected, int tile);
	};

	// Inner class
	public static class DefaultTileRenderer implements TileRenderer {
		public void render(Graphics2D g2d, Object object, int width, int height, boolean selected, int tile) {
			g2d.drawRect(1, 1, width-3, height-3);
			g2d.drawString("Tile " + tile, width/2-30, height/2+4);
			if (selected)
				g2d.drawRect(0, 0, width-1, height-1);
		}
		
	}

public CoTileMapPanel() {
	this(new DefaultTileRenderer());
}


public CoTileMapPanel(TileRenderer renderer) {
	m_renderer = renderer;
	m_selectionModel = new DefaultListSelectionModel();
	installListeners();
}


protected ListDataListener createListDataListener() {
	return new ListDataListener() {
		public void intervalAdded(ListDataEvent e) {
			int minIndex = Math.min(e.getIndex0(), e.getIndex1());
			int maxIndex = Math.max(e.getIndex0(), e.getIndex1());

			// Sync the SelectionModel with the DataModel.
			ListSelectionModel sm = getSelectionModel();
			if (sm != null) {
				// PENDING: DANGER: Inserted interval is selected if minIndex is.
				// This may be dangerous if one for instance selects items to remove.
				// Fix by unselecting minIndex or override selection model. /Markus 2000-03-11
				sm.insertIndexInterval(minIndex, maxIndex - minIndex, true);
			}

			// Sync internal states with the DataModel.
			syncInternalWithModel();
			// PENDING: Handle resize. (revalidate?)
			invalidate();
			revalidate();

			/* Repaint the panel, from the origin of
			 * the first added tile, to the last tile.
			 */
			repaintTiles(minIndex, m_tiles-1);
		}

		public void intervalRemoved(ListDataEvent e) {
			int minIndex = Math.min(e.getIndex0(), e.getIndex1());
			int maxIndex = Math.max(e.getIndex0(), e.getIndex1());

			// Sync the SelectionModel with the DataModel.
			ListSelectionModel sm = getSelectionModel();
			if (sm != null) {
				sm.removeIndexInterval(minIndex, maxIndex);
			}

			// Sync internal states with the DataModel.
			syncInternalWithModel();
			// PENDING: Handle resize. (revalidate?)
			invalidate();
			revalidate();

			/* Repaint the panel, from the origin of
			 * the first removed tile, to the last tile.
			 */
			repaintTiles(minIndex, m_tiles-1);
		}

		public void contentsChanged(ListDataEvent e) {
			// PENDING: Try to preserve selected items?

			// Sync internal states with the DataModel.
			syncInternalWithModel();
			// PENDING: Handle resize. (revalidate?)
			invalidate();
			revalidate();

			// Repaint the entire panel.
			repaint();
		}
	};
}


protected ListSelectionListener createListSelectionListener() {
	return new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			repaintTiles(e.getFirstIndex(), e.getLastIndex());
		}	
	};
}


protected MouseInputListener createMouseInputListener() {
	return new MouseInputAdapter() {
		public void mouseDragged(MouseEvent e) {
		    if (!SwingUtilities.isLeftMouseButton(e) || !isEnabled())
		        return;
			
			if (e.isShiftDown() || e.isControlDown()) 
				return;

			int tile = toTileOr(e.getX(), e.getY(), -1);
			if (tile != -1) {
				Rectangle cellBounds = getTileBounds(tile);
				if (cellBounds != null) {
				    scrollRectToVisible(cellBounds);
					getSelectionModel().setSelectionInterval(tile, tile);
				}
			}
		}

		public void mousePressed(MouseEvent e) {
			ListSelectionModel selModel = getSelectionModel();
		    if (!SwingUtilities.isLeftMouseButton(e) || !isEnabled())
		        return;
		    
			int tile = toTileOr(e.getX(), e.getY(), -1);

/*
			if (tile != -1) {
				try {
					fireVetoableChange(CoList.SELECTION_CHANGED, getSelectedValue(), m_listAdaptor.getElementAt(tile));
				} catch (PropertyVetoException exception) {
					e.consume();
					return;
				}
			}
*/
			if (!hasFocus()) {
				requestFocus();
			}

			if (tile != -1) {
				selModel.setValueIsAdjusting(true);
				int anchorIndex = selModel.getAnchorSelectionIndex();
				if (e.isControlDown()) {
					if (selModel.isSelectedIndex(tile)) 
						selModel.removeSelectionInterval(tile, tile);
					else 
						selModel.addSelectionInterval(tile, tile);
				}
				else if (e.isShiftDown() && (anchorIndex != -1))
				    selModel.setSelectionInterval(anchorIndex, tile);
				else 
				    selModel.setSelectionInterval(tile, tile);
			}
		}

		public void mouseReleased(MouseEvent e) {
		    if (!SwingUtilities.isLeftMouseButton(e))
		        return;

			getSelectionModel().setValueIsAdjusting(false);
		}
	};
}


protected TileRenderer createRenderer() {
	return new DefaultTileRenderer();
}


public void dragGestureRecognized(DragGestureEvent e) {
	// PENDING: Check if dragging current selection? /Markus
	// I would prefer if one could drag any object, similar to
	// Windows Explorer, without it being selected. /Markus
	CoDataTransferKit.dragUsing(e, getSelectedValue(), m_dragDataProvider);
}

public Dimension getMaximumSize() {
	return getPreferredSize();
}


public ListModel getModel() {
	return m_listModel;
}


public Dimension getPreferredScrollableViewportSize() {
	return getPreferredSize();
}


public Dimension getPreferredSize() {
	return new Dimension(m_tilesPerRow * m_tileWidth, tileRows() * m_tileHeight);
}


/**
 * The philosophy behind a block scrolling operation is that every
 * tile in a contiguous block of tiles should be fully exposed
 * exactly once, either before or after the scroll. Additionally,
 * to prevent the tile boundaries from "jumping", scrolling should
 * always be done in even multiples of the tile height. (We only
 * scroll vertically.)
 *
 * In other words, detect how many rows are fully visible and
 * scroll by that amount.
 */
public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
	switch (orientation) {
		case SwingConstants.VERTICAL:
			int upperSkip = m_tileHeight - (((visibleRect.y - 1) % m_tileHeight) + 1);
			int lowerSkip = (visibleRect.y + visibleRect.height) % m_tileHeight;
			return (visibleRect.height) - (upperSkip + lowerSkip);
		case SwingConstants.HORIZONTAL:
			/* Note that this should never happen, since
			 * we track the viewport width and thus have
			 * no horizontal scrollbar. Yet, we return
			 * something.
			 */
			return visibleRect.width;
		default:
			return 1;
	}
}


public boolean getScrollableTracksViewportHeight() { return false; }


public boolean getScrollableTracksViewportWidth() { return true; }


public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
	switch (orientation) {
		case SwingConstants.VERTICAL:
			int dy = visibleRect.y % m_tileHeight;
			if (dy == 0)
				return m_tileHeight;
			else
				return (direction < 0) ? dy : m_tileHeight - dy;
		case SwingConstants.HORIZONTAL:
			/* Note that this should never happen, since
			 * we track the viewport width and thus have
			 * no horizontal scrollbar. Yet, we return
			 * something.
			 */
			return m_tileWidth;
		default:
			return 1;
	}
}


/**
 * A convenience method that returns the selected index
 * if exactly one item is selected. Otherwise it returns -1.
 *
 * Note that this behaviour differs from that of JList.
 *
 * @return The first selected index.
 * @see #getSelectedValue()
 * @see JList#getSelectedIndex()
 */
public int getSelectedIndex() {
	int min = m_selectionModel.getMinSelectionIndex();
	int max = m_selectionModel.getMaxSelectionIndex();
	return (min == max) ? min : -1;
}


/**
 * A convenience method that returns the selected value
 * if exactly one item is selected. Otherwise it returns null.
 *
 * Note that this behaviour differs from that of JList.
 *
 * @return The selected value.
 * @see #getSelectedIndex()
 * @see JList#getSelectedValue()
 */
public Object getSelectedValue() {
	int i = getSelectedIndex();

	return (i >= 0) ? getModel().getElementAt(i) : null;

	// FIXME: The following line avoids the IndexOutOfBoundsException.  However, since this method
	// is called by com.bluebrim.base.client.CoTilePanelAdaptor.updateTilePanel(), which looks for that very exception I don't
	// want to implement it.  Someone should either implement the below form of the return statement
	// or comment on why it can't be used.
	    //Johan Walles (2001-06-12 14:51:09)

	// return ((i >= 0) && (i < getModel().getSize())) ? getModel().getElementAt(i) : null;
}


public ListSelectionModel getSelectionModel() {
	return m_selectionModel;
}


protected Rectangle getTileBounds(int tile) {
	return new Rectangle(toLeft(tile), toUpper(tile), m_tileWidth, m_tileHeight);
}


protected Rectangle getTileBounds(int first, int last) {
	int x1 = toLeft(first);
	int y1 = toUpper(first);
	int x2 = toLeft(last);
	int y2 = toUpper(last);
	if (y1 != y2)
		return new Rectangle(0, y1, m_tilesPerRow*m_tileWidth, y2-y1+m_tileHeight);
	else
		return new Rectangle(x1, y1, x2-x1+m_tileWidth, m_tileHeight);
}


public int getTileHeight() {
	return m_tileHeight;
}


public int getTileWidth() {
	return m_tileWidth;
}


/**
 * Create and install the listeners for the CoTileMapPanel, its model, and its
 * selectionModel. This method is called at construction time.
 */
private void installListeners() {
	m_mouseInputListener = createMouseInputListener();
	m_listSelectionListener = createListSelectionListener();
	m_listDataListener = createListDataListener();

	addMouseListener(m_mouseInputListener);
	addMouseMotionListener(m_mouseInputListener);

	ListSelectionModel selectionModel = getSelectionModel();
	if (selectionModel != null) {
		selectionModel.addListSelectionListener(m_listSelectionListener);
	}
}


protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D)g;

	Rectangle clip = g2d.getClipBounds();
	IntIterator iterator = tileIterator(clip);

	// DEBUG: Visualize clipping region.
	if (false) {
		clip.grow(-1,-1);
		Color oldColor = g2d.getColor();
		g2d.setColor(Color.red);
		g2d.draw(clip);
		g2d.setColor(oldColor);
	}

	if (iterator != null) {
		while (iterator.hasNext())
			paintTile(g2d, iterator.next());
	}
}


protected void paintTile(Graphics2D g2d, int tile) {
	paintTileAt(g2d, tile, toLeft(tile), toUpper(tile));
}


protected void paintTileAt(Graphics2D g2d, int tile, int x, int y) {
	Object tileObject = m_listModel.getElementAt(tile);

	AffineTransform oldTransform = g2d.getTransform();
	g2d.translate(x, y);
	// PENDING: Set clip too.

	m_renderer.render(g2d, tileObject, m_tileWidth, m_tileHeight, m_selectionModel.isSelectedIndex(tile), tile);

	g2d.setTransform(oldTransform);
}


public void prepareDrag(int permittedActions, CoDnDDataProvider dataProvider) {
	m_dragDataProvider = dataProvider;
	CoDataTransferKit.setupDragGestureRecognizer(this, permittedActions, this);
}

protected void processMouseEvent(MouseEvent e) {
	super.processMouseEvent(e);
}


protected void processMouseMotionEvent(MouseEvent e) {
	super.processMouseMotionEvent(e);
}


protected void repaintTile(int tile) {
	repaint(getTileBounds(tile));
}


protected void repaintTiles(int first, int last) {
	repaint(getTileBounds(first, last));
}


/**
 * Calls super.reshape(), and is overridden simply to detect changes in 
 * our bounds. After reshaping we recompute how many tiles fit in a row
 * (similar to trigging a layout).
 *
 * PENDING: Now also adjust the height according to the width, which only
 * works so so and should be cleaned up by fixing layout managers.
 *
 * @see JTable#reshape(int,int,int,int)
 */
public void reshape(int x, int y, int width, int height) {
	boolean widthChanged = (getWidth() != width);
	if (widthChanged) {
		m_tilesPerRow = Math.max(1, width/m_tileWidth);
		height = tileRows()*m_tileHeight;
//		invalidate();
//		revalidate();
	}
	super.reshape(x, y, width, height);
}


public void setModel(ListModel model) {
	if (m_listModel != null)
		m_listModel.removeListDataListener(m_listDataListener);

	m_listModel = model;
	syncInternalWithModel();

	if (m_listModel != null)
		m_listModel.addListDataListener(m_listDataListener);
}


public void setTileHeight(int height) {
	m_tileHeight = height;
}


public void setTileWidth(int width) {
	m_tileWidth = width;
}


private void syncInternal() {
	int width = getWidth();
	
	if (getModel() != null)
		m_tiles = getModel().getSize();
	m_tilesPerRow = Math.max(1, width/m_tileWidth);
	int height = tileRows()*m_tileHeight;
}


protected void syncInternalWithModel() {
	if (getModel() != null)
		m_tiles = getModel().getSize();
}


/**
 * Creates IntIterator that iterates through all tiles with
 * any part inside rect.
 *
 * PENDING: Improve to match description above.
 */
protected IntIterator tileIterator(Rectangle rect) {
	int x1 = Math.max(0,rect.x);
	int y1 = Math.max(0,rect.y);
	int x2 = Math.min(m_tilesPerRow*m_tileWidth - 1, rect.x + rect.width);
	int y2 = Math.min(tileRows()*m_tileHeight - 1, rect.y + rect.height);
	if ((x1 > x2) || (y1 > y2))
		return null;
		
	final int first = toTileOr(x1, y1, m_tiles);
	final int last = toTileOr(x2, y2, m_tiles - 1);
//	System.out.println("first = "+first+", last = "+last);

	return new IntIterator() {
		private int i = first;
		public boolean hasNext() { return (i <= last); }
		public int next() {
			if (hasNext())
				return i++;
			else
				throw new NoSuchElementException();
		}
	};
}


/**
 * Returns the number of rows that contains at least one tile.
 */
protected final int tileRows() {
	return (m_tiles-1)/m_tilesPerRow + 1;
}


protected final int toLeft(int tile) {
	return (tile%m_tilesPerRow) * m_tileWidth;
}


/**
 * Returns the tile number which contains the point (x,y).
 * If there is no tile at (x,y), returns outOfBoundsValue.
 */
protected final int toTileOr(int x, int y, int outOfBoundsValue) {
	int row, col, tile;
	if (((col = x/m_tileWidth) >= m_tilesPerRow) ||
		((row = y/m_tileHeight) >= tileRows()) ||
		((tile = row*m_tilesPerRow + col) >= m_tiles))
		return outOfBoundsValue;
	else
		return tile;
}


protected final int toUpper(int tile) {
	return (tile/m_tilesPerRow) * m_tileHeight;
}
}