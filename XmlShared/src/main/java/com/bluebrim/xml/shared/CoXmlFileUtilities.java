package com.bluebrim.xml.shared;
import java.io.*;
import java.net.*;

import com.bluebrim.gemstone.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Offers static utility functions
 * Creation date: (2001-02-22 17:10:46)
 * @author: Mikael Printz
 */
public class CoXmlFileUtilities {
	private static void doLoadXml(final InputStream stream, final Object[] models, final CoXmlContext context)
		throws CoTransactionException {
		CoCmdKit.doInTransaction(new CoCommand("XML Import") {
			public boolean doExecute() {
				Object model = models[0] = readModelFrom(stream, context);
				return true;
			}
		});
	}

	private static Object readModelFrom(InputStream stream, CoXmlContext context) {
		// PENDING: Fix error handling! /Markus
		CoXmlConsumer consumer = new CoXmlConsumer(context);
		Object result = null;
		try {
			result = consumer.readModelFrom(stream);
		} catch (CoXmlReadException e) {
		}
		return result;
	}
	
	/**
	Loads an XML file.
	
	@param file The file containing the XML data.
	@return A model that has been constructed from the loaded XML file.
	*/

	public static Object loadXml(File file, CoXmlContext context) {
		try {
			context.setUrl(file.toURL());
			context.setFileNameAndPath(file.getPath());
			return loadXml(new FileInputStream(file), context);
		} catch (MalformedURLException mue) {
			System.err.println("Warning: Couldn't create URL from file " + file);
		} catch (IOException ioe) {
			System.err.println("Warning: Couldn't read file " + file);
		}

		return null;
	}

	/**
	Loads an XML file.
	
	@param file The file containing the XML data.
	@return A model that has been constructed from the loaded XML file.
	*/

	public static Object loadXml(URL url, CoXmlContext context) {
		try {
			context.setUrl(url);
			//		context.setFileNameAndPath(file.getPath());

			return loadXml(new FileInputStream(url.getFile()), context);
		} catch (IOException ioe) {
			System.err.println("Warning: Couldn't read url " + url);
		}

		return null;

	}

	public static void saveXml(File file, final CoXmlContext context, final CoXmlExportEnabledIF topObject) {
		if (file != null) {
			context.setFileNameAndPath(file.getPath());
			try {
				CoFileSaver fileSaver = new CoFileSaver(file) {
					public void writeToStream(OutputStream os) throws Exception {
						Writer writer = null;
						writer = new BufferedWriter(new OutputStreamWriter(os, "UTF8"));
						CoXmlOutputGenerator gen = new CoXmlOutputGenerator(context);
						gen.execute(topObject);
						gen.output(writer);
						writer.flush();
						writer.close(); // Added by Magnus Ihse (magnus.ihse@appeal.se) (2001-09-13 13:56:54)
					}
				};
				fileSaver.save();
			} catch (IOException ioe) {
				ioe.printStackTrace(System.out);
			} catch (Exception e) {
				System.out.println("Something went wrong when generating XML output: " + e.toString());
				e.printStackTrace(System.out);
			} catch (Throwable t) {
				System.err.println("XML save failed: " + t.toString());
				t.printStackTrace(System.out);
			}

		}
	}

	/**
	Loads an XML file.
	
	@param stream The stream containing the XML data.
	@return A model that has been constructed from the loaded XML file.
	*/

	public static Object loadXml(InputStream stream, final CoXmlContext context) {
		try {

			/* FIXME: Why is this an array? */
			final Object[] models = new Object[1];

			try {
				doLoadXml(stream, models, context);
			} catch (CoTransactionException e) {
				System.err.println("Error loading XML: " + e.toString());
				return null;
			}

			return models[0];

		} catch (Throwable t) {
			t.printStackTrace(System.out);
			System.err.println("Warning: Load failed: " + t.toString());
		}

		return null;

	}
}