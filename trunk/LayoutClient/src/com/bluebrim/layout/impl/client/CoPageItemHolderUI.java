package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.transact.shared.*;

/**
 * UI for CoPageItemHolderIF.
 * The page item is editable only if a layout editor dialog is supplied.
 * A layout editor can be supplied using a CoLayoutEditor.LayoutEditorDialog or
 *  by supplying a CoUIContext containing the mapping:
 *    CoLayoutEditorConstants.LAYOUT_EDITOR_CONFIGURATION -> CoLayoutEditorConstants.LAYOUT_EDITOR_*_CONFIGURATION
 *
 * Note: If a layout editor dialog is supplied, the CoUIContext must contain the mapping:
 *    CoPageItemEditorContextIF.KEY -> CoPageItemEditorContextIF
 *    CoDesktopLayoutAreaIF.DESKTOP_LAYOUT -> CoDesktopLayoutAreaIF
 *    CoLayoutEditorConstants.LAYOUT_EDITOR_CONFIGURATION -> CoLayoutEditorConfiguration
 * 
 * @author: Dennis
 */
public class CoPageItemHolderUI extends CoDomainUserInterface implements CoContextAcceptingUI {
	public static final String DESCRIPTION = "CoPageItemHolderUI.DESCRIPTION";
	public static final String NAME = "CoPageItemHolderUI.NAME";

	private CoPageItemViewRendererFactory m_pageItemViewRendererFactory = CoDefaultPageItemViewRendererFactory.INSTANCE;
	private CoShapePageItemViewPane m_viewPane;
	private CoPopupMenu m_popupMenu;
	private CoLayoutEditorDialog m_editorDialog;
	private WindowListener m_windowListener = new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
			stoppedEditing(m_editorDialog.getEditor());
		}
	};
	private CoUIContext m_uiContext;
	private CoCommand m_touchPageItemHolderCommand;
	private int m_viewDetailMode = CoPageItemView.DETAILS_EVERYTHING;

	public CoPageItemHolderUI(CoObjectIF pageItemHolder, CoUIContext uiContext) {
		this(null, uiContext, null, null);
		setDomain(pageItemHolder);
	}

	public CoPageItemHolderUI(CoLayoutEditorDialog ed, CoUIContext uiContext, CoShapePageItemViewPane pivp, CoPageItemViewRendererFactory pivRendererFactory) {
		setUIContext(uiContext);

		m_editorDialog = (ed != null) ? ed : createLayoutEditorDialogFromUiContext();
		m_viewPane = pivp;
		if (pivRendererFactory != null)
			m_pageItemViewRendererFactory = pivRendererFactory;
	}

	private double calculateFittingScale() {
		Rectangle r = m_viewPane.getParent().getParent().getBounds();

		double h = m_viewPane.getViewsHeight() * CoViewPanel.m_screenResolution;
		double w = m_viewPane.getViewsWidth() * CoViewPanel.m_screenResolution;
		Insets i = m_viewPane.getInsets();

		double sx = (r.getWidth() - i.left - i.right - 2) / w;
		double sy = (r.getHeight() - i.top - i.bottom - 2) / h;

		return Math.min(sx, sy);
	}

	private CoLayoutEditorDialog createLayoutEditorDialogFromUiContext() {
		CoLayoutEditorConfiguration config = getLayoutEditorConfiguration();

		if (config == null)
			return null;

		return CoLayoutEditor.createLayoutEditorDialog(config);
	}

	protected void createListeners() {
		super.createListeners();

		if (m_editorDialog != null) {
			m_viewPane.addMouseListener(new CoPopupGestureListener(m_popupMenu) {
				protected void showPopup(MouseEvent e) {
					if (isEnabled())
						super.showPopup(e);
				}
			});
		}
	}

	protected void createWidgets(CoPanel p, CoUserInterfaceBuilder b) {
		super.createWidgets(p, b);

		if (m_viewPane == null)
			m_viewPane = new CoShapePageItemViewPane(null, null, null, 1.0, false, false);

		if (m_viewPane.getFixedViewSize() != null) {
			p.add(m_viewPane, BorderLayout.CENTER);
		} else {
			class TempPanel extends CoPanel implements Scrollable {
				public TempPanel(LayoutManager layoutManager) {
					super(layoutManager);
				}
				public Dimension getPreferredScrollableViewportSize() {
					return null;
				}
				public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
					if (orientation == SwingConstants.HORIZONTAL) {
						return (int) visibleRect.getWidth();
					} else
						return (int) visibleRect.getHeight();

				}
				public boolean getScrollableTracksViewportHeight() {
					return false;
				}
				public boolean getScrollableTracksViewportWidth() {
					return false;
				}
				public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
					if (orientation == SwingConstants.HORIZONTAL) {
						return (int) (visibleRect.getWidth() / 10);
					} else
						return (int) (visibleRect.getHeight() / 10);

				}
			}
			CoPanel tmp = new TempPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			tmp.add(m_viewPane);
			m_viewPane.setLocation(0, 0);
			JScrollPane scrollTemp = new JScrollPane(tmp);
			p.add(scrollTemp, BorderLayout.CENTER);
			scrollTemp.getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);

			CoZoomPanel zoomPanel = new CoZoomPanel(b, "Skala", 1, new double[] { 1600, 800, 400, 200, 125, 100, 50, 25, 12.5, 0 }, "Anpassa");
			zoomPanel.setZoomable(new CoZoomPanel.ZoomableProxy(m_viewPane) {
				public void setScale(double scale) {
					if (scale == 0)
						scale = calculateFittingScale();
					super.setScale(scale);
				}
			}, 100.0);

			p.add(zoomPanel, BorderLayout.NORTH);
			zoomPanel.setScale(100.0);
		}

		if (m_editorDialog != null) {
			CoMenuBuilder mb = getMenuBuilder();

			m_popupMenu = mb.createPopupMenu();
			mb.addPopupMenuItem(m_popupMenu, "Redigera ...", new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					edit();
				}
			});
		}

	}

	protected CoShapePageItemView createViewFor(CoShapePageItemIF pi) {
		return CoPageItemView.create(pi, m_pageItemViewRendererFactory, m_viewDetailMode);
	}

	protected List createViewsFor(CoLayoutHolder layoutHolder) {
		List pis = layoutHolder.getLayouts();

		List l = new ArrayList();

		int I = pis.size();
		for (int i = 0; i < I; i++) {
			l.add(createViewFor((CoShapePageItemIF) pis.get(i)));
		}

		return l;
	}

	private void edit() {
		if (m_editorDialog.isVisible()) {
			stoppedEditing(m_editorDialog.getEditor());
		}

		m_editorDialog.getEditor().setConfiguration(getLayoutEditorConfiguration());
		m_editorDialog.getEditor().setContext(getPageItemEditorContext());

		CoLayoutHolder layoutHolder = (CoLayoutHolder) getDomain();

		m_editorDialog.getEditor().setModel(new CoLayoutEditorModel(layoutHolder.getLayouts(), ""), getDesktop());

		prepareEditor(m_editorDialog.getEditor());

		m_editorDialog.open(getPanel(), layoutHolder.getName());
		m_editorDialog.getCoDialog().addWindowListener(m_windowListener);
	}
	public CoGenericUIContext getCopyOfCurrentRequiredUIContext() {
		return null;
	}

	protected CoDesktopLayoutAreaIF getDesktop() {
		if (m_uiContext == null)
			return null;

		return (CoDesktopLayoutAreaIF) m_uiContext.getStateFor(CoDesktopLayoutAreaIF.DESKTOP_LAYOUT);
	}

	protected CoLayoutEditorConfiguration getLayoutEditorConfiguration() {
		if (m_uiContext == null)
			return null;

		Object x = m_uiContext.getStateFor(CoLayoutEditorConstants.LAYOUT_EDITOR_CONFIGURATION);
		if (x == null)
			return null;

		if (x instanceof CoLayoutEditorConfiguration) {
			return (CoLayoutEditorConfiguration) x;
		} else {
			return CoLayoutEditorConfiguration.lookup((String) x);
		}
	}

	protected CoPageItemEditorContextIF getPageItemEditorContext() {
		if (m_uiContext == null)
			return null;

		return (CoPageItemEditorContextIF) m_uiContext.getStateFor(CoPageItemEditorContextIF.KEY);
	}

	public CoShapePageItemViewPane getPageItemViewPane() {
		return m_viewPane;
	}

	public CoShapePageItemView getView() {
		return (CoShapePageItemView) m_viewPane.getView();
	}

	protected void postDomainChange(CoObjectIF d) {
		super.postDomainChange(d);

		updateView();
	}

	protected void prepareEditor(CoLayoutEditor editor) {
	}

	public void setRenderingHints(Map h) {
		m_viewPane.setRenderingHints(h);
	}

	public void setUIContext(CoUIContext context) {
		m_uiContext = context;
	}

	protected void stoppedEditing(CoLayoutEditor editor) {
		m_editorDialog.getEditor().setModels(null, null);
		m_editorDialog.getCoDialog().removeWindowListener(m_windowListener);

		if (getDomain() == null)
			return;

		if (m_touchPageItemHolderCommand == null) {
			m_touchPageItemHolderCommand = new CoCommand("TOUCH PAGE ITEM HOLDER") {
				public boolean doExecute() {
					((CoLayoutHolder) getDomain()).layoutsChanged();
					return true;
				}
			};
		}

		CoTransactionUtilities.execute(m_touchPageItemHolderCommand, null);
	}

	private void updateView() {
		if (getDomain() == null) 
			m_viewPane.setView(null);

		else 
			m_viewPane.setViews(createViewsFor((CoLayoutHolder) getDomain()));
	}

	public void valueHasChanged() {
		super.valueHasChanged();

		updateView();
	}
}