package com.bluebrim.swing.client;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JScrollPane;
import javax.swing.JTree;
/**
 * En subklass till JSrollPane som innehåller ett CoTree. 
 * Skall betraktas som en tillfällig lösning på problemet
 * att VCE inte betraktar JScrollPane som en ScrollPane .
 * @author Lasse Svadängs 971001.
 *
 */
public class CoTreeBox extends JScrollPane {
	private JTree 			m_treeView;
	private FocusListener 	m_focusListener;
public CoTreeBox() {
	this(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
}
public CoTreeBox(int vsbPolicy, int hsbPolicy) {
	super(vsbPolicy, hsbPolicy);
	m_focusListener = new FocusListener () {
			public void focusGained(FocusEvent e)
			{
				repaint();
			}
			public void focusLost(FocusEvent e)
			{
				repaint();
			}
		};
	setTreeView(new CoTree());
}
public Dimension getMinimumSize() 
{
	return super.getMinimumSize();
}
/**
  * @return javax.swing.JTree
 */
public JTree getTreeView ( ) {
	return m_treeView;
}
public final void setTreeView (JTree aTreeView ) {
	if (m_treeView != aTreeView)
	{
		if (m_treeView != null)
			m_treeView.removeFocusListener(m_focusListener);
		m_treeView = aTreeView;
		m_treeView.setShowsRootHandles(true);		
		setViewportView(m_treeView);
		if (m_treeView != null)
			m_treeView.addFocusListener(m_focusListener);
	}
}
}
