package com.bluebrim.font.impl.client;

//import java.awt.BorderLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.bluebrim.font.impl.shared.CoFontResources;
//import com.bluebrim.gui.client.CoRowLayout;
//import com.bluebrim.gui.client.CoUserInterfaceBuilder;
//import com.bluebrim.menus.client.CoMenuBuilder;
//import com.bluebrim.swing.client.CoButton;
//import com.bluebrim.swing.client.CoPanel;
//import com.bluebrim.swing.client.CoTabbedPane;
//
//
//  /**
//   * 
//   * 
//   * Creation date: (2001-01-19 16:48:37)
//   * @author: Dennis
//   */
// 
//public class CoFontManagerPanel extends CoPanel
//{
//  	private List m_faces = new ArrayList();
//  	private Map m_map = new HashMap();
//
//  	private CoFontFacePanel m_facePanel;
//  	private CoFontMapPanel m_mapPanel;
//
//  void getServerState()
//  {
//  	CoServerFontManagerIF.State s = CoFontManager.getInstance().getServer().getState();
//  	s = (CoServerFontManagerIF.State) com.bluebrim.base.shared.debug.CoAssertion.fakeSerialization( s );
//
//  	m_faces.clear();
//  	m_faces.addAll( s.m_faces );
//
//  	m_map.clear();
//  	m_map.putAll( s.m_map );
//
//  	m_facePanel.set( m_faces );
//  	m_mapPanel.set( m_map, m_faces );
//  }
//
//  void setServerState()
//  {
//  	com.bluebrim.base.client.command.CoCommand c =
//  		new com.bluebrim.base.client.command.CoCommand( "SET FONT SERVER STATE" )
//  		{
//  			public boolean doExecute()
//  			{
//  				CoServerFontManagerIF.State s = new CoServerFontManagerIF.State();
//  				s.m_faces = m_faces;
//  				s.m_map = m_map;
//				
//  				CoFontManager.getInstance().getServer().setState( s );
//  				return true;
//  			}
//  		};
//
//  	com.bluebrim.font.shared.CoFontFace.DATA_IS_TRANSIENT = false;
//  	com.bluebrim.gemstone.impl.client.CoTransactionUtilities.execute( c, null );
//  	com.bluebrim.font.shared.CoFontFace.DATA_IS_TRANSIENT = true;
//  }
//
//  public boolean start()
//  {
//  	if
//  		( CoFontManager.getInstance().getServer().lock() )
//  	{
//  		getServerState();
//  		return true;
//  	} else {
//  		return false;
//  	}
//  }
//
//  public void stop()
//  {
//  	setServerState();
//  	CoFontManager.getInstance().getServer().unlock();
//  }
//
//  public CoFontManagerPanel( CoUserInterfaceBuilder builder, CoMenuBuilder menuBuilder )
//  {
//  	super( new BorderLayout() );
//
//  	builder.preparePanel( this );
//	
//	
//
//  	CoTabbedPane tp = builder.createTabbedPane();
//  	add( tp, BorderLayout.CENTER );
//	
//  	m_facePanel = new CoFontFacePanel( builder, menuBuilder );
//  	tp.add( m_facePanel, CoFontResources.getName( "FACES" ) );
//	
//  	m_mapPanel = new CoFontMapPanel( builder );
//  	tp.add( m_mapPanel, CoFontResources.getName( "FONTS" ) );
//	
//  	{
//  		CoPanel p = builder.createPanel( new CoRowLayout() );
//		
//  		CoButton 
//  		b = builder.createButton( CoFontResources.getName( "COMMIT" ), null );
//  		p.add( b );
//  		b.addActionListener(
//  			new ActionListener()
//  			{
//  				public void actionPerformed( ActionEvent ev )
//  				{
//  					setServerState();
//  				}
//  			}
//  		);
//		
//  		b = builder.createButton( CoFontResources.getName( "ROLLBACK" ), null );
//  		p.add( b );
//  		b.addActionListener(
//  			new ActionListener()
//  			{
//  				public void actionPerformed( ActionEvent ev )
//  				{
//  					getServerState();
//  				}
//  			}
//  		);
//
//  		tp.add( p, CoFontResources.getName( "SERVER" ) );
//  	}
//
//  	m_facePanel.setFontMapPanel( m_mapPanel );
//  }
//
//
//  public static void main(String[] args) 
//  {
//  	/* // från CoLayoutEditor
//  				final CoFontManagerPanel x =
//  				new CoFontManagerPanel(getUIBuilder(), getMenuBuilder());
//  			JFrame f = new JFrame();
//  			f.addWindowListener(new WindowAdapter() {
//  				public void windowClosing(WindowEvent evt) {
//  					x.stop();
//  				}
//  			});
//  			f.getContentPane().add(x);
//  			f.pack();
//
//  			x.start();
//  			f.show();
//  */
//  }
// }
