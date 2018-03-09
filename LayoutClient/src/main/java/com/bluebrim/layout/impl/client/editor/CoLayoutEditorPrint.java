package com.bluebrim.layout.impl.client.editor;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.*;
import java.util.List;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.postscript.client.*;
import com.bluebrim.postscript.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Command that make use of Java Print Service API
 * @author Göran Stäck
 *
 */
public class CoLayoutEditorPrint extends CoLayoutEditorAction {

	CoPostscriptTarget m_target = new CoPostscriptTarget.DesktopPrinter() {
		protected void initialize() {
			setCenterHorizontally(false);
			setCenterVertically(false);
			setEnlargeToFill(true);
			setShrinkToFit(true);
		}
	};

	private PrintRequestAttributeSet m_printRequestAttributeSet = new HashPrintRequestAttributeSet();
	private PrintService m_printService;

	public CoLayoutEditorPrint(CoLayoutEditor e) {
		super(e);
	}

	public void actionPerformed(ActionEvent ev) {
		CoCommand c = new CoCommand("PRINT") {
			public boolean doExecute() {
				doPrint();
				return true;
			}
		};
		m_editor.getCommandExecutor().doit(c, null);
	}

	private void doPrint() {
		List namedViewables = m_editor.getNamedViewables();
		m_printRequestAttributeSet.add(new Copies(1));
		showPrinterSelectionDialog(new PageRanges(1, namedViewables.size()));
		if (m_printService == null)
			return;

		DocPrintJob printJob = m_printService.createPrintJob();

		try {
			Doc doc;
			if (m_printService.isDocFlavorSupported(DocFlavor.INPUT_STREAM.POSTSCRIPT))
				doc = new SimpleDoc(getPostScript(namedViewables), DocFlavor.INPUT_STREAM.POSTSCRIPT, null);
			else
				doc = new SimpleDoc(getPrintable(namedViewables), DocFlavor.SERVICE_FORMATTED.PRINTABLE, null);
				
			printJob.print(doc, m_printRequestAttributeSet);

			/*
			* Do not explicitly call System.exit() when print returns.
			* Printing can be asynchronous so may be executing in a
			* separate thread.
			* If you want to explicitly exit the VM, use a print job 
			* listener to be notified when it is safe to do so.
			*/

		} catch (PrintException e) {
			System.err.println(e);
		}

	}

	/**
	 * Return a <code>Printable</code> that is used as the printdata argument when
	 * printing 2DGraphics
	 */
	private Printable getPrintable(final List namedViewables) {
		return new Printable() {
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				if (pageIndex < 0 || pageIndex >= namedViewables.size())
					return Printable.NO_SUCH_PAGE;
				else {
					CoLayoutEditorPrint.this.print((CoNamedViewable) namedViewables.get(pageIndex), graphics, pageFormat);
					return Printable.PAGE_EXISTS;
				}
			}

		};

	}

	/**
	 * Most of this code is copied from <code>CoPostScriptPage</code> which indicates that
	 * a refactorization should be done.
	 */	
	private void print(CoNamedViewable namedViewable, Graphics graphics, PageFormat pageFormat) {
		CoView view = namedViewable.getView();
		CoPaintable paintable = CoScreenPaintable.wrap(graphics);
	
		double targetWidth = pageFormat.getImageableWidth();
		double targetHeight = pageFormat.getImageableHeight();
		double graphicsWidth = view.getWidth();
		double graphicsHeight = view.getHeight();
		double pageScaleFactor = Math.min(targetWidth / graphicsWidth, targetHeight / graphicsHeight);
	
		double printedWidth = graphicsWidth * pageScaleFactor;
		double printedHeight = graphicsHeight * pageScaleFactor;
	
		double pageTranslateX = (targetWidth - printedWidth) / 2 + pageFormat.getImageableX();
		double pageTranslateY = (targetHeight - printedHeight) / 2 + pageFormat.getImageableY();
	
		paintable.translate(pageTranslateX, pageTranslateY);
		paintable.scale(pageScaleFactor, pageScaleFactor);
		
		view.paint(paintable);
		
	} 

	private void showPrinterSelectionDialog(PageRanges pageRanges) {
		//		PrintService[] pservices = PrintServiceLookup.lookupPrintServices(m_flavor, m_printRequestAttributeSet);
		PrintService[] pservices = PrintServiceLookup.lookupPrintServices(null, null);
		if (pservices.length > 0) {
			m_printRequestAttributeSet.add(pageRanges);
			// We add this afterwards since we don't require it, but would
			// like to get as close as possible. /Markus
			m_printRequestAttributeSet.add(new MediaPrintableArea(0, 0, 210, 297, Size2DSyntax.MM));
			m_printService = ServiceUI.printDialog(null, 100, 100, pservices, null, null, m_printRequestAttributeSet);
		} else
			System.out.println("No print service available");

	}

	private InputStream getPostScript(List namedViewables) {
		// Create a pageCollection to print
		PageRanges pageRanges = (PageRanges) m_printRequestAttributeSet.get(PageRanges.class);
		if (pageRanges == null)
			pageRanges = new PageRanges(1, namedViewables.size());

		CoPostscriptClientPrintJob collector = new CoPostscriptClientPrintJob(m_target, namedViewables, pageRanges);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		collector.generatePostscript(out);

		//		saveToFile(out); // Just for debugging

		return new ByteArrayInputStream(out.toByteArray());
	}

	private void saveToFile(ByteArrayOutputStream out) {
		JFileChooser fc = new JFileChooser(new File("test.ps"));
		fc.setDialogTitle("Spara Postscriptfil");
		fc.setApproveButtonText("Spara");

		fc.showSaveDialog(null);
		File file = fc.getSelectedFile();

		if (file == null)
			return;
		try {
			FileOutputStream stream = new FileOutputStream(file, false);
			stream.write(out.toByteArray());
			stream.close();

		} catch (IOException e) {
		}

	}

}