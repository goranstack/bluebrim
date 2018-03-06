package com.bluebrim.layout.impl.client.editor;

import java.awt.event.*;

/**
 * 
 * 
 * @author: Dennis
 */
 
class CoCreatePostscriptPreview extends CoLayoutEditorAction
{
public CoCreatePostscriptPreview( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed( ActionEvent ev )
{
	doSaveAsPostscriptPreview();
}
private void doCreatePostScriptPreview() 
{
/*	double h, w;
	CoShapePageItemView cw = m_editor.getWorkspace().getSelectionManager().getSelectedView();
	if (cw == null)
	{
		cw = m_editor.getWorkspace().getRootView();
		Dimension d = m_editor.getWorkspace().getModelSize();
		w=d.getWidth();
		h=d.getHeight();
	}
	else
	{
		w=cw.getWidth();
		h=cw.getHeight();
	}
	//((com.bluebrim.mediasystem.shared.CoMediaSystemIF)com.bluebrim.system.shared.CoSystemProxy.getSystem()).getPrintServer().print((CoShapePageItemIF)cw.getPageItem(), CoPrintServerIF.POSTSCRIPT_TO_PREVIEW);
	CoGraphics2D2wayWrapper w2g;
	//Lets create the Graphics that we need to save states in the 2waywrapper
	BufferedImage im = new BufferedImage((int) (w), (int) (h), BufferedImage.TYPE_3BYTE_BGR);
	Graphics2D img = im.createGraphics();
	//Create recDelegate
	CoGraphics2DPostscriptPreview psg = new CoGraphics2DPostscriptPreview(w, h);
	//Creates the 2 way wrapper. one graphics to record and one to hold the state.
	w2g = new CoGraphics2D2wayWrapper(img, psg);

	// Here we get the printer and prints via ports direct to printer
	CoPrinterDialog tDialog = new CoPrinterDialog(CoUIBaseUtilities.getFrameFor(m_editor.getPanel()));
	tDialog.setVisible(true);
	if(tDialog.getClosingReason()==CoDialog.CLOSED_BY_OK)
	{
		if(tDialog.writeToFile())
		{
			File f = null;
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Spara preview");
			fileChooser.setApproveButtonText("Spara");

			fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() 
			{
				public String getDescription() 
				{
					return "PS files";
				}
				public boolean accept(File file) 
				{
					if (file.isDirectory()) return true;
					String name = file.getName().toLowerCase();
					return name.endsWith(".ps");
				}
			});

			if(fileChooser.showSaveDialog(m_editor.getWorkspace())==JFileChooser.APPROVE_OPTION)
			{
				f = fileChooser.getSelectedFile();
				try
				{
					FileOutputStream out = new FileOutputStream(f);
					// Added 2000-08-07 to test the generation of a pageitemviewtree.
					w2g.translate(-cw.getX(), -cw.getY());
					cw.paint(w2g);
					//remove the pageitemview tree from memory.
					cw = null;
					// Create a visitor that generates Postscript from a drawingtree onto a stream.
					//CoPostscriptBuilder psb = new CoPostscriptBuilder(((CoAbstractRecorderGraphics)g.getRecDelegate()).getDrawingTree(), out);
					CoPostscriptBuilder psb = new CoPostscriptBuilder((CoAbstractRecorderGraphics)w2g.getRecDelegate(), out);
					psb.build();
					// Clean up the objects
					w2g = null;
					System.out.println("Finished");
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			Socket printerSocket=null;
			try
			{
				printerSocket = new Socket(((com.bluebrim.printer.shared.CoNetworkPrinterIF)tDialog.getPrinter()).getInetAddress(), 9100);
				OutputStream printerOut = printerSocket.getOutputStream();
				// the actual creation
				w2g.translate(-cw.getX(), -cw.getY());
				cw.paint(w2g);
				//remove the pageitemview tree from memory.
				cw = null;
				// Create a visitor that generates Postscript from a drawingtree onto a stream.
				//CoPostscriptBuilder psb = new CoPostscriptBuilder(((CoAbstractRecorderGraphics)g.getRecDelegate()).getDrawingTree(), out);
				CoPostscriptBuilder psb = new CoPostscriptBuilder((CoAbstractRecorderGraphics)w2g.getRecDelegate(), printerOut);
				psb.build();
				// Clean up the objects
				w2g = null;
				System.out.println("Finished");
				
			}
			catch(ConnectException e)
			{
				m_editor.showErrorMessage(e.getMessage());
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try{printerSocket.close();}catch(Exception e){};
			}
		}
	} */
/*
	Socket printerSocket=null;
	try
	{
		printerSocket = new Socket("172.1.73.7", 9100);
		OutputStream printerOut = printerSocket.getOutputStream();
	// the actual creation
		w2g.translate(-cw.getX(), -cw.getY());
		cw.paint(w2g);
		//remove the pageitemview tree from memory.
		cw = null;
		// Create a visitor that generates Postscript from a drawingtree onto a stream.
		//CoPostscriptBuilder psb = new CoPostscriptBuilder(((CoAbstractRecorderGraphics)g.getRecDelegate()).getDrawingTree(), out);
		CoPostscriptBuilder psb = new CoPostscriptBuilder((CoAbstractRecorderGraphics)w2g.getRecDelegate(), printerOut);
		psb.build();
		// Clean up the objects
		w2g = null;
		System.out.println("Finished");
		
	}
	catch(IOException e)
	{
		e.printStackTrace();
	}
	finally
	{
		try{printerSocket.close();}catch(Exception e){};
	}
	*/
	
	// Here we get the filename from the user via a FileChooser GUI
/*
	File f = null;
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setDialogTitle("Spara preview");
	fileChooser.setApproveButtonText("Spara");

	fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() 
	{
		public String getDescription() 
		{
			return "PS files";
		}
		public boolean accept(File file) 
		{
			if (file.isDirectory()) return true;
			String name = file.getName().toLowerCase();
			return name.endsWith(".ps");
		}
	});

	if(fileChooser.showSaveDialog(m_workspace)==JFileChooser.APPROVE_OPTION)
		f = fileChooser.getSelectedFile();
	try
	{
		FileOutputStream out = new FileOutputStream(f);
		// Added 2000-08-07 to test the generation of a pageitemviewtree.
		w2g.translate(-cw.getX(), -cw.getY());
		cw.paint(w2g);
		//remove the pageitemview tree from memory.
		cw = null;
		// Create a visitor that generates Postscript from a drawingtree onto a stream.
		//CoPostscriptBuilder psb = new CoPostscriptBuilder(((CoAbstractRecorderGraphics)g.getRecDelegate()).getDrawingTree(), out);
		CoPostscriptBuilder psb = new CoPostscriptBuilder((CoAbstractRecorderGraphics)w2g.getRecDelegate(), out);
		psb.build();
		// Clean up the objects
		w2g = null;
		System.out.println("Finished");
	}
	catch(IOException e)
	{
		e.printStackTrace();
	}
	*/
}
private void doSaveAsPostscriptPreview() 
{
	doCreatePostScriptPreview();
	// If you want to generate the preview on server uncomment the next part
	/*
	 * New implementation for the save to the serverbased postscript generation
	 */
/*	 
	CoCommand c = new CoCommand("PRINT_POSTSCRIPT_PREVIEW") {
		public boolean doExecute() {
			createPostScriptPreview();
			return true;
		}
	};
	m_commandManager.doit(c, null);
	*/
}
}