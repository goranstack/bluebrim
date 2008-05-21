package com.bluebrim.layout.impl.client.editor;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;

/**
 * 
 * Creation date: (2001-03-17 11:05:39)
 * @author: Dennis
 */
 
class CoLayerDialog extends CoDialog
{
	private CoLayoutEditor m_layoutEditor;

	private CoList m_modelList;
	private CoList m_layerList;

	private TouchableListModel m_modelListModel = new ModelListModel();
	private TouchableListModel m_layerListModel = new LayerListModel();

	private boolean m_isUpdatingLayerListModel = false;

	private CoLayoutEditorModel m_selectedModel;
	


	private static abstract class TouchableListModel extends AbstractListModel
	{
		public void touch()
		{
			fireContentsChanged( this, 0, getSize() - 1 );
		}
	};

	private class ModelListModel extends TouchableListModel
	{
		public int getSize()
		{
			List l = m_layoutEditor.getModels();
			return ( l == null ) ? 0 : l.size();
		}
		
		public Object getElementAt( int i )
		{
			return ( (CoLayoutEditorModel) m_layoutEditor.getModels().get( i ) ).getName();
		}
	};

	private class LayerListModel extends TouchableListModel
	{
		public int getSize()
		{
			return ( m_selectedModel == null ) ? 0 : m_selectedModel.getLayerCount();
		}
		
		public Object getElementAt( int i )
		{
			return m_selectedModel.getLayer( i ).m_view.getName();
		}
	};
	
public CoLayerDialog( Dialog owner, CoLayoutEditor e )
{
	super( owner, "", false );

	m_layoutEditor = e;

	create();
}
public CoLayerDialog( Frame owner, CoLayoutEditor e )
{
	super( owner, "", false );

	m_layoutEditor = e;

	create();
}
private void create()
{
	CoUserInterfaceBuilder b = m_layoutEditor.getUIBuilder();
	
	Container c = getContentPane();

	CoSplitPane sp = b.createSplitPane( true );
	c.add( sp );


	CoListBox l = b.createListBox();
	sp.setLeftComponent( l );
	m_modelList = l.getList();
	m_modelList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
	m_modelList.setModel( m_modelListModel );
	m_modelList.getSelectionModel().addListSelectionListener(
		new ListSelectionListener()
		{
			public void valueChanged( ListSelectionEvent ev )
			{
				modelSelected();
			}
		}
	);


	l = b.createListBox();
	sp.setRightComponent( l );
	m_layerList = l.getList();
	m_layerList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
	m_layerList.setModel( m_layerListModel );
	m_layerList.getSelectionModel().addListSelectionListener(
		new ListSelectionListener()
		{
			public void valueChanged( ListSelectionEvent ev )
			{
				layerSelected();
			}
		}
	);

	refresh();
}
private void layerSelected()
{
	if ( m_isUpdatingLayerListModel ) return;

	int i = m_layerList.getSelectedIndex();

	m_layoutEditor.getWorkspace().getSelectionManager().unselectAllViews();
	
	m_selectedModel.setActiveLayer( i );
	
	m_layoutEditor.checkDesktop();
	m_layoutEditor.getWorkspace().repaint();
}
private void modelSelected()
{
	int i = m_modelList.getSelectedIndex();
	if
		( i == -1 )
	{
		m_selectedModel = null;
	} else {
		m_selectedModel = (CoLayoutEditorModel) m_layoutEditor.getModels().get( i );
	}

	m_isUpdatingLayerListModel = true;
	
	m_layerList.clearSelection();
	m_layerListModel.touch();
	if ( m_selectedModel != null ) m_layerList.setSelectedIndex( m_selectedModel.getIndexOfActiveLayer() );
	
	m_isUpdatingLayerListModel = false;

}
public void refresh()
{
	m_modelList.clearSelection();
	
	m_modelListModel.touch();

	if
		( m_modelListModel.getSize() > 0 )
	{
		m_modelList.setSelectedIndex( 0 );
	}
}
}