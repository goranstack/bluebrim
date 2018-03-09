package com.bluebrim.font.impl.client;

//  package com.bluebrim.calvin.font.userinterfaces;

//  import com.bluebrim.font.impl.shared.metrics.*;
//  import com.bluebrim.swing.client.*;
//  import com.bluebrim.base.client.*;
//  import java.awt.*;
//  import java.awt.event.*;
//  import java.util.*;
//  import java.util.List;
//  import javax.swing.*;
//  import javax.swing.event.*;
//  import javax.swing.table.*;
//  import com.bluebrim.font.impl.shared.*;
//  import java.awt.font.*;
//  import java.io.*;
//  import se.corren.calvin.postscript.tt.*;
//  import com.bluebrim.font.impl.client.*;
//  import com.bluebrim.font.impl.shared.*;
//  import com.bluebrim.menus.client.*;

//  /**
//   * 
//   * 
//   * Creation date: (2001-01-19 16:48:37)
//   * @author: Dennis
//   */
 
public class CoFontFacePanel // extends CoPanel
{
//  	private List m_faces;

//  	private CoButton m_createFontsButton;
//  	private CoFontMapPanel m_fontMapPanel;
	
//  	private class FontFaceTableModel extends AbstractTableModel 
//  		{
//  			public int getColumnCount()
//  			{
//  				return 10;
//  			}
			
//  			public int getRowCount()
//  			{
//  				return ( m_faces == null ) ? 0 : m_faces.size();
//  			}

//  			public String getColumnName( int column )
//  			{
//  				switch
//  					( column )
//  				{
//  					case 0: return CoFontResources.getName( "FAMILY" );
//  					case 1: return CoFontResources.getName( "NAME" );
//  					case 2: return CoFontResources.getName( "WEIGHT" );
//  					case 3: return CoFontResources.getName( "STYLE" );
//  					case 4: return CoFontResources.getName( "VARIANT" );
//  					case 5: return CoFontResources.getName( "STRETCH" );
//  					case 6: return CoFontResources.getName( "SOURCE" );
//  					case 7: return CoFontResources.getName( "FONT NAME" );
//  					case 8: return CoFontResources.getName( "BOLD" );
//  					case 9: return CoFontResources.getName( "ITALIC" );
//  				};
//  				return null;
//  			}

//  			public Class getColumnClass( int column )
//  			{
//  				switch
//  					( column )
//  				{
//  					case 0: return String.class;
//  					case 1: return String.class;
//  					case 2: return String.class;
//  					case 3: return String.class;
//  					case 4: return String.class;
//  					case 5: return String.class;
//  					case 6: return String.class;
//  					case 7: return String.class;
//  					case 8: return Boolean.class;
//  					case 9: return Boolean.class;
//  				};
//  				return null;
//  			}
				
//  			public Object getValueAt( int row, int column )
//  			{
//  				com.bluebrim.font.shared.CoFontFace face = (com.bluebrim.font.shared.CoFontFace) m_faces.get( row );
//  				switch
//  					( column )
//  				{
//  					case 0: return face.getSpec().getFamilyName();
//  //					case 1: return face.getName();  // FIXME: what does getName() do really?
//  					case 1: return face.toString();
//  					case 2: return face.getSpec().getWeight().getName();
//  					case 3: return face.getSpec().getStyle().getName();
//  					case 4: return face.getSpec().getVariant().getName();
//  					case 5: return face.getSpec().getStretch().getName();
//  //					case 6: return face.getFileName();  // FIXME: checkout...
//  					case 6: return CoFontMapper.getSingleton().getFontFileContainer().getFontFileBaseName();
//  					case 7: return face.getAttributes().get( TextAttribute.FAMILY );
//  					case 8:
//  					{
//  						Object o = face.getAttributes().get( TextAttribute.WEIGHT );
//  						return ( ( o != null ) && o.equals( TextAttribute.WEIGHT_BOLD ) ) ? Boolean.TRUE : Boolean.FALSE;
//  					}
//  					case 9:
//  					{
//  						Object o = face.getAttributes().get( TextAttribute.POSTURE );
//  						return ( ( o != null ) && o.equals( TextAttribute.POSTURE_OBLIQUE ) ) ? Boolean.TRUE : Boolean.FALSE;
//  					}
//  				};
//  				return null;
//  			}
			
//  			public boolean isCellEditable( int row, int column )
//  			{
//  				return column > 6;
//  			}

//  			public void setValueAt( Object v, int row, int column )
//  			{
//  				com.bluebrim.font.shared.CoFontFace face = (com.bluebrim.font.shared.CoFontFace) m_faces.get( row );
//  				switch
//  					( column )
//  				{
//  					case 7:
//  						face.setFontName( v.toString() );
//  						break;
						
//  					case 8:
//  						face.setWeight( ( (Boolean) v ).booleanValue() ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR );
//  						break;
						
//  					case 9:
//  						face.setPosture( ( (Boolean) v ).booleanValue() ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR );
//  						break;
//  				};

//  				updatePreview();
//  			}
//  		};

//  	private AbstractTableModel m_faceTableModel = new FontFaceTableModel();

	
//  	private CoTable m_faceTable;
//  	private CoLabel m_previewLabel8;
//  	private CoLabel m_previewLabel12;
//  	private CoLabel m_previewLabel18;
//  	private CoLabel m_previewLabel24;
//  	private CoLabel m_previewLabel36;

//  	private JFileChooser m_fileChooser = new JFileChooser();

//  	private CoUserInterfaceBuilder m_builder;
//  	private JMenuItem m_copyFaceMenuItem;
//  	private CoFontSpecDialog m_copyFontSpecDialog;
//  	private CoPopupMenu m_faceTablePopupMenu;
//  	private CoFontSpecDialog m_pleaseTryAnotherFontSpecDialog;

//  	private CoTrackingCurvePanel m_trackingCurvePanel;
//  	private CoDialog m_trackingDialog;
//  	private JMenuItem m_trackingMenuItem;

//  private void createFonts()
//  {
//  	int [] selectedRows = m_faceTable.getSelectedRows();
	
//  	for
//  		( int i = 0; i < selectedRows.length; i++ )
//  	{
//  		com.bluebrim.font.shared.CoFontFace face = (com.bluebrim.font.shared.CoFontFace) m_faces.get( selectedRows[ i ] );

//  		m_fontMapPanel.createFontFor( face );
//  	}
//  }

//  private com.bluebrim.font.shared.CoFontFace readFace( File f )
//  {
//  	com.bluebrim.font.shared.CoFontFace face = null;

//  	try
//  	{
//  		face = new com.bluebrim.font.shared.CoFontFace( f.getAbsolutePath() );
		
//  		int i = m_faces.indexOf( face );
//  		while
//  			( i != -1 )
//  		{
//  			com.bluebrim.font.shared.CoFontFaceSpec spec = pleaseTryAnotherFaceSpec( face.getSpec() );
//  			if
//  				( spec == null )
//  			{
//  				return null;
//  			} else {
//  				face.setSpec( spec );
//  			}
//  //			face.deriveNewSpec();
//  			i = m_faces.indexOf( face );
//  			// ???	
//  //			return null;
//  		}
//  	}
//  	catch ( IOException ex )
//  	{
//  		System.err.println( ex );
//  		ex.printStackTrace( System.err );
//  		return null;
//  	}

//  	return face;
//  }

//  private void readFile()
//  {
//  	File f = m_fileChooser.getSelectedFile();

//  	if
//  		( f.isDirectory() )
//  	{
//  		if
//  			( readFile( f ) )
//  		{
//  			Collections.sort( m_faces );
//  			m_faceTableModel.fireTableStructureChanged();
//  		}
//  	} else {
//  		com.bluebrim.font.shared.CoFontFace face = readFace( f );

//  		if
//  			( face != null )
//  		{
//  			m_faces.add( face );
//  			Collections.sort( m_faces );
			
//  			int i = m_faces.indexOf( face );
//  			m_faceTableModel.fireTableRowsInserted( i, i );
//  			m_faceTable.getSelectionModel().setSelectionInterval( i, i );
//  		}
//  	}
//  }

//  private boolean readFile( File f )
//  {
//  	File [] fs = f.listFiles();
//  	boolean didAdd = false;

//  	for
//  		( int i = 0; i < fs.length; i++ )
//  	{
//  		if
//  			( fs[ i ].isDirectory() )
//  		{
//  			didAdd |= readFile( fs[ i ] );
//  		} else {
//  			com.bluebrim.font.shared.CoFontFace face = readFace( fs[ i ] );

//  			if
//  				( face != null )
//  			{
//  				m_faces.add( face );
//  				didAdd = true;
//  			}
//  		}
//  	}

//  	return didAdd;
//  }

//  void set( List faces )
//  {
//  	m_faces = faces;
	
//  	m_faceTableModel.fireTableStructureChanged();
//  }

//  public void setFontMapPanel( CoFontMapPanel p )
//  {
//  	m_fontMapPanel = p;
//  	m_createFontsButton.setEnabled( m_fontMapPanel != null );
//  }

//  private void updatePreview()
//  {
//  	int i [] = m_faceTable.getSelectedRows();

//  	if
//  		( i.length != 1 )
//  	{
//  		m_previewLabel8.setForeground( Color.white );
//  		m_previewLabel12.setForeground( Color.white );
//  		m_previewLabel18.setForeground( Color.white );
//  		m_previewLabel24.setForeground( Color.white );
//  		m_previewLabel36.setForeground( Color.white );
//  	} else {
//  		m_previewLabel8.setForeground( Color.black );
//  		m_previewLabel12.setForeground( Color.black );
//  		m_previewLabel18.setForeground( Color.black );
//  		m_previewLabel24.setForeground( Color.black );
//  		m_previewLabel36.setForeground( Color.black );
		
//  		com.bluebrim.font.shared.CoFontFace face = (com.bluebrim.font.shared.CoFontFace) m_faces.get( i[ 0 ] );
//  		Map a = new HashMap( face.getAwtData().getAwtAttributes() );

//  		a.put( TextAttribute.SIZE, new Float( 8 ) );
//  		Font f = Font.getFont( a );
//  /*		
//  System.err.println( f.getFamily() + " ??? " + face.getSpec().getFamilyName() );
//  		if
//  			( ! f.getFamily().equals( face.getSpec().getFamilyName() ) )
//  		{
//  			m_previewLabel8.setForeground( Color.red );
//  			m_previewLabel12.setForeground( Color.red );
//  			m_previewLabel18.setForeground( Color.red );
//  			m_previewLabel24.setForeground( Color.red );
//  			m_previewLabel36.setForeground( Color.red );
//  		}
//  	*/	
//  		m_previewLabel18.setText( f.toString() );
		
//  		m_previewLabel8.setFont( f );

//  		a.put( TextAttribute.SIZE, new Float( 12 ) );
//  		m_previewLabel12.setFont( Font.getFont( a ) );

//  		a.put( TextAttribute.SIZE, new Float( 18 ) );
//  		m_previewLabel18.setFont( Font.getFont( a ) );

//  		a.put( TextAttribute.SIZE, new Float( 24 ) );
//  		m_previewLabel24.setFont( Font.getFont( a ) );

//  		a.put( TextAttribute.SIZE, new Float( 36 ) );
//  		m_previewLabel36.setFont( Font.getFont( a ) );

//  		revalidate();
//  		repaint();
//  	}

	
//  }





//  public CoFontFacePanel( CoUserInterfaceBuilder builder, CoMenuBuilder menuBuilder )
//  {
//  	super( new CoAttachmentLayout() );

//  	m_builder = builder;
//  	m_builder.preparePanel( this );

	
//  	m_fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
	
//  	CoPanel buttons = m_builder.createPanel( new CoRowLayout() );
//  	{
//  		CoButton 
//  		b = m_builder.createButton( CoFontResources.getName( "LOAD" ), null );
//  		buttons.add( b );
//  		b.addActionListener(
//  			new ActionListener()
//  			{
//  				public void actionPerformed( ActionEvent ev )
//  				{
//  					if
//  						( m_fileChooser.APPROVE_OPTION == m_fileChooser.showOpenDialog( CoFontFacePanel.this ) )
//  					{
//  						readFile();
//  					}
//  				}
//  			}
//  		);
		
//  		m_createFontsButton = m_builder.createButton( CoFontResources.getName( "CREATE FONTS FOR SELECTED FACES" ), null );
//  		buttons.add( m_createFontsButton );
//  		m_createFontsButton.addActionListener(
//  			new ActionListener()
//  			{
//  				public void actionPerformed( ActionEvent ev )
//  				{
//  					createFonts();
//  				}
//  			}
//  		);
//  		m_createFontsButton.setEnabled( m_fontMapPanel != null );
//  	}
	
	
//  	m_faceTable = new CoTable( m_faceTableModel );
//  	m_builder.prepareTable( m_faceTable );
//  	JScrollPane sp = new JScrollPane( m_faceTable );

//  	m_faceTablePopupMenu = menuBuilder.createPopupMenu();
//  	/*
//  	m_copyFaceMenuItem = menuBuilder.addPopupMenuItem( m_faceTablePopupMenu, "Copy" );
//  	m_copyFaceMenuItem.setEnabled( false );
//  	m_copyFaceMenuItem.addActionListener(
//  			new ActionListener()
//  			{
//  				public void actionPerformed( ActionEvent ev )
//  				{
//  					copySelectedFace();
//  				}
//  			}
//  		);
//  */
//  	m_trackingMenuItem = menuBuilder.addPopupMenuItem( m_faceTablePopupMenu, "Tracking ..." );
//  	m_trackingMenuItem.setEnabled( false );
//  	m_trackingMenuItem.addActionListener(
//  			new ActionListener()
//  			{
//  				public void actionPerformed( ActionEvent ev )
//  				{
//  					openTrackingCurveUI();
//  				}
//  			}
//  		);

//  	m_faceTable.addMouseListener(
//  		new MouseAdapter()
//  		{
//  			public void mouseClicked( MouseEvent ev ) { doit( ev ); }
//  			public void mousePressed( MouseEvent ev ) { doit( ev ); }
//  			public void mouseReleased( MouseEvent ev ) { doit( ev ); }
//  			private void doit( MouseEvent ev )
//  			{
//  				if
//  					( ev.isPopupTrigger() )
//  				{
//  					m_faceTablePopupMenu.show( m_faceTable, ev.getX(), ev.getY() );
//  				}
//  			}
//  		}
//  	);


//  	CoPanel previewPanel = m_builder.createPanel( new CoColumnLayout() );
//  	{
//  		previewPanel.setBackground( java.awt.Color.white );
//  		previewPanel.setOpaque( true );
//  		previewPanel.setBorder( BorderFactory.createEmptyBorder( 20, 20, 20, 20 ) );
		
//  		previewPanel.add( m_previewLabel8 = m_builder.createLabel( "abcdefghijklmnopqrstuvwxyzåäö" ) );
//  		previewPanel.add( m_previewLabel12 = m_builder.createLabel( "abcdefghijklmnopqrstuvwxyzåäö" ) );
//  		previewPanel.add( m_previewLabel18 = m_builder.createLabel( "abcdefghijklmnopqrstuvwxyzåäö" ) );
//  		previewPanel.add( m_previewLabel24 = m_builder.createLabel( "abcdefghijklmnopqrstuvwxyzåäö" ) );
//  		previewPanel.add( m_previewLabel36 = m_builder.createLabel( "abcdefghijklmnopqrstuvwxyzåäö" ) );
//  	}

	
//  	m_faceTable.getSelectionModel().addListSelectionListener(
//  		new ListSelectionListener()
//  		{
//  			public void valueChanged( ListSelectionEvent ev )
//  			{
//  				updatePreview();
//  				//m_copyFaceMenuItem.setEnabled( m_faceTable.getSelectedRowCount() == 1 );
//  				m_trackingMenuItem.setEnabled( m_faceTable.getSelectedRowCount() == 1 );
//  			}
//  		}
//  	);

//  	add( sp,
//  	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, buttons ),
//  	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_TOP, 0, previewPanel ),
//  	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
//  	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );
//  	add( buttons,
//  	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
//  	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
//  	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_NO ),
//  	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
//  	add( previewPanel,
//  	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_NO ),
//  	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER, 0 ),
//  	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
//  	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );


	
//  	updatePreview();

//  }

//  private com.bluebrim.font.shared.CoFontFaceSpec copyFaceSpec( com.bluebrim.font.shared.CoFontFaceSpec spec )
//  {
//  	if
//  		( m_copyFontSpecDialog == null )
//  	{
//  		Container w = getTopLevelAncestor();
//  		if
//  			( w instanceof Dialog )
//  		{
//  			m_copyFontSpecDialog = new CoFontSpecDialog( m_builder, "Enter new font specification",  (Dialog) w );
//  		} else if
//  			( w instanceof Frame )
//  		{
//  			m_copyFontSpecDialog = new CoFontSpecDialog( m_builder, "Enter new font specification",  (Frame) w );
//  		} else {
//  			m_copyFontSpecDialog = new CoFontSpecDialog( m_builder, "Enter new font specification",  new Frame() );
//  		}
		
//  		m_copyFontSpecDialog.pack();
//  	}
	
//  	return m_copyFontSpecDialog.open( this, spec );
//  }

//  private void copySelectedFace()
//  {
//  	com.bluebrim.font.shared.CoFontFace face = (com.bluebrim.font.shared.CoFontFace) m_faces.get( m_faceTable.getSelectedRow() );
//  	face = face.copy();
//  	face.setSpec( copyFaceSpec( face.getSpec() ) );
	
//  	int i = m_faces.indexOf( face );
//  	while
//  		( i != -1 )
//  	{
//  		com.bluebrim.font.shared.CoFontFaceSpec spec = pleaseTryAnotherFaceSpec( face.getSpec() );
//  		if
//  			( spec == null )
//  		{
//  			return;
//  		} else {
//  			face.setSpec( spec );
//  		}
//  		i = m_faces.indexOf( face );
//  	}

//  	m_faces.add( face );
//  	Collections.sort( m_faces );
	
//  	i = m_faces.indexOf( face );
//  	m_faceTableModel.fireTableRowsInserted( i, i );
//  	m_faceTable.getSelectionModel().setSelectionInterval( i, i );
	
//  }

//  private com.bluebrim.font.shared.CoFontFaceSpec pleaseTryAnotherFaceSpec( com.bluebrim.font.shared.CoFontFaceSpec spec )
//  {
//  	if
//  		( m_pleaseTryAnotherFontSpecDialog == null )
//  	{
//  		Container w = getTopLevelAncestor();
//  		if
//  			( w instanceof Dialog )
//  		{
//  			m_pleaseTryAnotherFontSpecDialog = new CoFontSpecDialog( m_builder, "Font specification is already used, please try another one",  (Dialog) w );
//  		} else if
//  			( w instanceof Frame )
//  		{
//  			m_pleaseTryAnotherFontSpecDialog = new CoFontSpecDialog( m_builder, "Font specification is already used, please try another one",  (Frame) w );
//  		} else {
//  			m_pleaseTryAnotherFontSpecDialog = new CoFontSpecDialog( m_builder, "Font specification is already used, please try another one",  new Frame() );
//  		}
		
//  		m_pleaseTryAnotherFontSpecDialog.pack();
//  	}
	
//  	return m_pleaseTryAnotherFontSpecDialog.open( this, spec );
//  }



//  private void openTrackingCurveUI()
//  {
//  	if
//  		( m_trackingDialog == null )
//  	{
//  		Container w = getTopLevelAncestor();
//  		if
//  			( w instanceof Dialog )
//  		{
//  			m_trackingDialog = new CoDialog( (Dialog) w , "", true );
//  		} else if
//  			( w instanceof Frame )
//  		{
//  			m_trackingDialog = new CoDialog( (Frame) w , "", true );
//  		} else {
//  			m_trackingDialog = new CoDialog( new Frame() , "", true );
//  		}

//  		m_trackingCurvePanel = new CoTrackingCurvePanel();
//  		m_trackingDialog.getContentPane().add( m_trackingCurvePanel );
//  		m_trackingDialog.pack();
//  		m_trackingDialog.setLocationRelativeTo( this );
//  	}

	
//  	com.bluebrim.font.shared.CoFontFace face = (com.bluebrim.font.shared.CoFontFace) m_faces.get( m_faceTable.getSelectedRow() );

//  	com.bluebrim.font.shared.metrics.CoTrackingMetrics tracking = face.getMetrics().getTrackingMetrics();
		
//  	CoTrackingMetricsImplementation implementation = (CoTrackingMetricsImplementation) tracking;
//  //FIXME: this is ugly, but we know that this is true. otherwise we'll take the runtime exception
//  //in the future, we ought to solve this in another way	

//  	m_trackingCurvePanel.setTrackingCurve( implementation );

//  	m_trackingDialog.show();

//  	tracking = m_trackingCurvePanel.getTrackingCurve();
//  	//FIXME: now what? how to replace the old metrics with this one?
//  }
}