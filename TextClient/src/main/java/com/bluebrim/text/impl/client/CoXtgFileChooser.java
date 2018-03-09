package com.bluebrim.text.impl.client;

import java.awt.*;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

/**
 * Customization of JFileChooser.
 *
 * @author Dennis, 2000-08-25
 */
 
public class CoXtgFileChooser extends JFileChooser
{
	private static CoXtgFileChooser m_instance;
private CoXtgFileChooser()
{
	this( null );
}
private CoXtgFileChooser( File currentDirectory )
{
	super( currentDirectory );
	
	setDialogTitle( "Hämta text" );
	setApproveButtonText( "Öppna" );

	addChoosableFileFilter( 
		new FileFilter()
		{
			public String getDescription()
			{
				return "Text files (*.txt)";
			}

			public boolean accept( File file )
			{
				if ( file.isDirectory() ) return true;
				String name = file.getName().toLowerCase();
				return name.endsWith(".txt");
			}
		}
	);

	addChoosableFileFilter( 
		new FileFilter()
		{
			public String getDescription()
			{
				return "Rich text format files (*.rtf)";
			}

			public boolean accept( File file )
			{
				if ( file.isDirectory() ) return true;
				String name = file.getName().toLowerCase();
				return name.endsWith(".rtf");
			}
		}
	);

	addChoosableFileFilter( 
		new FileFilter()
		{
			public String getDescription()
			{
				return "Xtg files (*.xtg)";
			}

			public boolean accept( File file )
			{
				if ( file.isDirectory() ) return true;
				String name = file.getName().toLowerCase();
				return name.endsWith(".xtg");
			}
		}
	);
	
//	setAccessory( new JButton( "#######" ) );
}
public static CoXtgFileChooser getInstance()
{
	if
		( m_instance == null )
	{
		m_instance = new CoXtgFileChooser();
	}

	return m_instance;
}
public int showDialog( Component parent )
{
	return showDialog( parent, null );
}
}
