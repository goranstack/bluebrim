package com.bluebrim.layout.impl.client.editor;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.observable.*;
import com.bluebrim.swing.client.*;

/**
 * Combination of a CoPageItemHolderPaletteUI and a workspace displaying the selected CoPageItemHolderIF.
 * See CoPageItemUI for details on supplying a layout editor.
 *
 * @Author: Dennis
 */
public class CoPageItemHolderPaletteWithWorkspaceUI extends CoDomainUserInterface implements CoContextAcceptingUI {
	private CoSubcanvas m_paletteSubcanvas;
	private CoPageItemHolderPaletteUI m_paletteUI;

	private CoSubcanvas m_workspaceSubcanvas;
	private CoPageItemHolderUI m_workspaceUI;

	public CoPageItemHolderPaletteWithWorkspaceUI(CoObjectIF pageItemHolderCollection, CoUIContext uiContext) {
		this(null, null, uiContext, CoPageItemView.DETAILS_EVERYTHING, false, false);

		setDomain(pageItemHolderCollection);
	}

	public CoPageItemHolderPaletteWithWorkspaceUI(
		CoPageItemHolderPaletteUI.IconSizeProducer isp,
		CoLayoutEditorDialog editor,
		CoUIContext uiContext,
		int viewDetailMode,
		boolean isPageItemDragSource,
		boolean isNormalDragSource) {
		m_paletteUI =
			new CoPageItemHolderPaletteUI(
				editor,
				uiContext,
				viewDetailMode,
				isPageItemDragSource,
				isNormalDragSource,
				"",
				isp,
				null) {

			protected CoLayoutHolder getPageItemHolderForEditing(CoLayoutHolder domain) {
				return CoPageItemHolderPaletteWithWorkspaceUI.this.getPageItemHolderForEditing(domain);
			}

			protected void prepareEditor(CoLayoutEditor editor1) {
				super.prepareEditor(editor1);
				CoPageItemHolderPaletteWithWorkspaceUI.this.prepareEditor(editor1);
			}

			protected void stoppedEditing(CoLayoutEditor editor2) {
				super.stoppedEditing(editor2);
				CoPageItemHolderPaletteWithWorkspaceUI.this.stoppedEditing(editor2);
			}

			public JComponent createPalettePanel() {
				JComponent p = super.createPalettePanel();
				class TPanel extends CoPanel implements Scrollable {
					public Dimension getPreferredScrollableViewportSize() {
						//if(getParent() instanceof JViewport && ((JViewport)getParent()).is )
						//	return ((JViewport)getParent()).getViewSize();
						return this.getPreferredSize();
					}
					public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
						if (orientation == SwingConstants.HORIZONTAL)
							return (int) visibleRect.getWidth();
						else
							return (int) visibleRect.getHeight();
					}
					public boolean getScrollableTracksViewportHeight() {
						return false;
					}
					public boolean getScrollableTracksViewportWidth() {
						return true;
					}
					public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
						return getScrollableBlockIncrement(visibleRect, orientation, direction) / 10;
					}
				}
				CoPanel tP = new CoScrollableFlowPanel(); //new TPanel();
				JComponent p2 = CoPageItemHolderPaletteWithWorkspaceUI.this.createPalettePanel(tP);
				if (p2 != null) {
					m_palettePanel = tP;
					return p2;
				} else
					return p;
			}

		};
		m_workspaceUI = new CoPageItemHolderUI(editor, uiContext, null, null) {
			protected CoLayoutHolder getPageItemHolderForEditing(CoLayoutHolder domain) {
				return CoPageItemHolderPaletteWithWorkspaceUI.this.getPageItemHolderForEditing(domain);
			}
			protected void prepareEditor(CoLayoutEditor editor3) {
				super.prepareEditor(editor3);
				CoPageItemHolderPaletteWithWorkspaceUI.this.prepareEditor(editor3);
			}
			protected void stoppedEditing(CoLayoutEditor editor4) {
				super.stoppedEditing(editor4);
				CoPageItemHolderPaletteWithWorkspaceUI.this.stoppedEditing(editor4);
			}
		};
		(new CoDefaultServerObjectListener(m_workspaceUI)).initialize();
	}

	public JComponent createPalettePanel(CoPanel tPanel) {

		return null;
	}

	public void createValueModels(CoUserInterfaceBuilder b) {
		b.createSubcanvasAdaptor(b.addAspectAdaptor(new CoReadOnlyAspectAdaptor(this, "PALETTE") {
			public Object get(CoObjectIF subject) {
				return subject;
			}
		}), m_paletteSubcanvas);
	}

	public void createWidgets(CoPanel p, CoUserInterfaceBuilder b) {
		super.createWidgets(p, b);

		m_paletteSubcanvas = b.createSubcanvas(m_paletteUI);

		m_workspaceSubcanvas = b.createSubcanvas(m_workspaceUI);

		CoSplitPane sp = getUIBuilder().createSplitPane(true, m_paletteSubcanvas, m_workspaceSubcanvas);
		sp.setOrientation(CoSplitPane.VERTICAL_SPLIT);
		p.add(sp);

		m_paletteUI.addSelectionListener(new CoPageItemHolderPaletteUI.SelectionListener() {
			public void selectionChanged(List selected) {
				if (selected.size() == 1) {
					m_workspaceUI.setDomain((CoLayoutHolder) selected.get(0));
				} else {
					m_workspaceUI.setDomain(null);
				}
			}
		});
	}

	public CoGenericUIContext getCopyOfCurrentRequiredUIContext() {
		return m_workspaceUI.getCopyOfCurrentRequiredUIContext();
	}

	protected CoLayoutHolder getPageItemHolderForEditing(CoLayoutHolder domain) {
		return domain;
	}

	public List getSelectedHolders() // [ CoPageIetmHolderIF ]
	{
		return m_paletteUI.getSelectedHolders();
	}

	protected void prepareEditor(CoLayoutEditor editor) {
	}

	public void setActions(
		CoTransActionCommand[] addActions,
		CoTransActionCommand removeAction,
		CoPageItemHolderPaletteUI.Renamer renamer) {
		m_paletteUI.setActions(addActions, removeAction, renamer);
	}

	public void setDecoration(CoViewPanel.Decoration r) {
		m_paletteUI.setDecoration(r);
	}

	public void setUIContext(CoUIContext context) {
		m_workspaceUI.setUIContext(context);
	}

	protected void stoppedEditing(CoLayoutEditor editor) {
	}
}
