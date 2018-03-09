package com.bluebrim.xml.shared;

/**
 * SPI for mapping xml-tags to classes and classes for marshalling and
 * unmarshalling XML document.
 *
 * @author Göran Stäck 2002-10-24
 */
public interface CoXmlMappingSPI {
	
	public static interface Mapper {

		/**
		 * Maps a class to a XML-tag and a model-builder
		 */
		public void map( Class cls, String tag, Class modelBuilder);

		/**
		 * Maps a class to a XML-tag, model-builder and a xml-builder
		 */
		public void map( Class cls, String tag, Class modelBuilder, Class xmlBuilder);

		/**
		 * Maps a class to XML-tag. No model-builder is used instead the class
		 * should implement CoXmlImportEnabledIF. A default xml-builder is used.
		 */
		public void map( Class cls, String tag );
		

		/**
		 * Maps a XML-tag to a model-builder
		 */
		public void map( String tag, Class modelBuilder);
		

		/**
		 * Maps a class to a xml-builder
		 */
		public void map( Class cls, Class xmlBuilder);
		
	}

	public void collectMappings(Mapper mapper);


}
