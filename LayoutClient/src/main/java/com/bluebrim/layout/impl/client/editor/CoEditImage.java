package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Layout editor operation: Active image editor.
 * PENDING: NOT IMPLEMENTED YET
 * 
 * @author: Dennis
 */
 
public class CoEditImage extends CoExternalUIDialogAction
{
public CoEditImage( String name, CoLayoutEditor e )
{
	super( name, e );

	setEnabled( false );
}


public CoEditImage( CoLayoutEditor e )
{
	super( e );

	setEnabled( false );
}


public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	/*
	CoPageItemAbstractImageContentView v = m_editor.getCurrentImageContentView();
	CoImageIF i = ( (CoPageItemAbstractImageContentIF) v.getPageItem() ).getCoImage();

	File f = null;
	FileOutputStream s = null;
	try
	{
		f = File.createTempFile( "zzzzzzzzzzzzzzzz", i.getSuggestedFileSuffix() );
		s = new FileOutputStream( f );
	}
	catch ( IOException ex )
	{
		System.err.println( "Create file failed." );
		return;
	}

	i.writeImage( s );

	try
	{
		s.flush();
		s.close();
	}
	catch ( IOException ex )
	{
		System.err.println( "Flush file failed." );
		return;
	}


	try
	{
		Runtime.getRuntime().exec( "a program.exe" ).waitFor();
	}
	catch ( IOException ex )
	{
		System.err.println( "edit file failed." );
		return;
	}
	catch ( InterruptedException ex )
	{
		System.err.println( "edit file interrupted." );
		return;
	}
	
	try
	{
		f = new File( "c:/temp/zzzzzzzzzzzzzzzz.png" );
		com.sun.media.jai.codec.FileSeekableStream s2 = new com.sun.media.jai.codec.FileSeekableStream( f );

		com.bluebrim.gemstone.shared.CoSession.getSessionManager().beginTransaction();
		i.reloadFrom( s2 );
		com.bluebrim.gemstone.shared.CoSession.getSessionManager().commitTransaction( v.getPageItem() );
	}
	catch ( IOException ex )
	{
		System.err.println( "read file failed." );
		return;
	}
	*/
}





void setContext( CoPageItemEditorContextIF c )
{
}

public void prepare( CoShapePageItemView v )
{
	setEnabled( false );
}
}