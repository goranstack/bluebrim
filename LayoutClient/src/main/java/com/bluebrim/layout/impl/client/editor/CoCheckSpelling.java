package com.bluebrim.layout.impl.client.editor;

import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.bluebrim.layout.impl.client.CoPageItemUIStringResources;
import com.bluebrim.layout.impl.shared.view.CoPageItemAbstractTextContentView;
import com.bluebrim.layout.impl.shared.view.CoPageItemContentView;
import com.bluebrim.layout.impl.shared.view.CoPageItemView;
import com.bluebrim.layout.impl.shared.view.CoPageItemViewVisitor;

/**
 * Layout editor operation: Run spell checker.
 * 
 * @author: Dennis
 */
 
public class CoCheckSpelling extends CoLayoutEditorAction
{
	public static final String SPELL_CHECK_FAILED_MESSAGE = "CoLayoutEditor.SPELL_CHECK_FAILED_MESSAGE";
	public static final String SPELL_CHECK_FAILED_TITLE = "CoLayoutEditor.SPELL_CHECK_FAILED_TITLE";
	public static final String SPELL_CHECK_FAILED_RETRY = "CoLayoutEditor.SPELL_CHECK_FAILED_RETRY";
	public static final String SPELL_CHECK_FAILED_CANCEL = "CoLayoutEditor.SPELL_CHECK_FAILED_CANCEL";
public CoCheckSpelling( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoCheckSpelling( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	if
		( m_editor.isTextEditorActive() )
	{
		m_editor.getTextEditor().checkSpelling( getSpellCheckProperties() );
	} else if
		( m_editor.getWorkspace().getSelectionManager().getSelectedViewCount() == 0 )
	{
		doCheckSpelling( m_editor.getWorkspace().getRootView() );
	} else {
		Iterator e = m_editor.getWorkspace().getSelectionManager().getSelectedViews();
		while
			( e.hasNext() )
		{
			doCheckSpelling( (CoPageItemView) e.next() );
		}
	}
}
private boolean doCheckSpelling( CoPageItemAbstractTextContentView v )
{
	if
		( v.isTextLocked() )
	{
		return false;
	}
	
	if
		( m_editor.startTextEditing( v, (MouseEvent) null ) )
	{
		m_editor.m_styledTextEditor.checkSpelling( getSpellCheckProperties() );
		m_editor.stopTextEditing( v );
		return true;
	} else {
		return false;
	}
}
private void doCheckSpelling( CoPageItemView v )
{
	CoPageItemViewVisitor visitor = new CoPageItemViewVisitor()
	{
		public boolean visitContentView( CoPageItemContentView content )
		{
			if
				( content instanceof CoPageItemAbstractTextContentView )
			{
				CoPageItemAbstractTextContentView tcv = (CoPageItemAbstractTextContentView) content;
				while
					( ! doCheckSpelling( tcv ) )
				{
					// abort/retry ?
					if
						( JOptionPane.YES_OPTION != JOptionPane.showOptionDialog( m_editor.getWorkspace(), 
						              	                                          CoLayouteditorUIStringResources.getName( SPELL_CHECK_FAILED_MESSAGE ),
						              	                                          CoLayouteditorUIStringResources.getName( SPELL_CHECK_FAILED_TITLE ),
						              	                                          JOptionPane.YES_NO_OPTION,
						              	                                          JOptionPane.QUESTION_MESSAGE,
						              	                                          null,
						              	                                          new Object[] { CoLayouteditorUIStringResources.getName( SPELL_CHECK_FAILED_RETRY ),
							              	                                                       CoLayouteditorUIStringResources.getName( SPELL_CHECK_FAILED_CANCEL ), },
						              	                                          CoPageItemUIStringResources.getName( SPELL_CHECK_FAILED_CANCEL ) ) )
					{
						break;
					}
				}
			}
			return true;
		}
	};

	v.visit( visitor );
}
protected Object getSpellCheckProperties()
{
//	try
//    {
//        return CoSpellCheckerClient.getSpellCheckerServer().getSpellCheckProperties();
//    } catch (RemoteException e)
//    {
//        throw new RuntimeException(e);
//    }
	throw new UnsupportedOperationException("Until Wintertree spell checker is replaced by a GPL spell checker");

}
}