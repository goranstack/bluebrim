package com.bluebrim.swing.client;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
/**
 * Subklass till JScrollPane som innehåller en CoList.
 * <br>
 * Följande metoder är implementerade: 
 * <ul>
 * <li>CoListBox() - för att ge instanser som används i VCE
 * en initial Border (annars syns de inte!)
 * <li>set- och getList()
 * <li>set- och getCellRenderer för att i VCE kunna peta in den renderer
 * du vill att listan skall ha.
 * </ul>
 * En framtida bättre integrering av Swing i VAJ kommer säker att
 * göra denna klass obsolete.
 * @author Lasse Svadängs 971010
 */
public class CoListBox extends JScrollPane {
	private CoList m_list;

	private FocusListener m_focusListener;
 
/**
 * CJListBox constructor comment.
 */
public CoListBox() {
	this(new CoList());
}


/**
 * CJListBox constructor comment.
 */
public CoListBox(CoList list) {
	super();
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
	setList(list);
}


	/** 
	 */
	public ListCellRenderer getCellRenderer() {
		return getList().getCellRenderer();
	}


	/** 
	 */
	public CoList getList() {
		return m_list;
	}


	/** 
	 */
	public void setCellRenderer(ListCellRenderer aCellRenderer) {
		getList().setCellRenderer(aCellRenderer);
	}


/** 
 */
public final void setList(CoList aList)
{
	if (m_list != aList)
	{
		if (m_list != null)
			m_list.removeFocusListener(m_focusListener);
		m_list = aList;
		setViewportView(m_list);
		if (m_list != null)
			m_list.addFocusListener(m_focusListener);
	}
}
}