package com.bluebrim.layout.shared;


/**
 * Implemented by objects that represents layout templates organized in a tree structure for
 * convenient access to a limited number of templates. The folder part of names is just a 
 * metaphore to create an understandable semantic.<br>
 * <br>
 * This is an attempt to create an interface that can replace 
 * <code>CoPageItemPrototypeTreeRootIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the
 * layout domain. 
 * <br>
 * The working strategi is to replace all external references to code>CoPageItemPrototypeTreeRootIF</code>
 * with this interface and add requested methods until everyone is happy.  
 * <br>
 * PENDING: Present implementation of this interface has methods for creating and adding where the
 * message is sent to the root folder and the folder to wich the adding should be done
 * is passed as an argument. Seems a little bit awkward but we have to live with that
 * for now because important things take place in those methods that I don't have the time
 * to analyze and refactor right now.
 * 
 * @author Göran Stäck 2002-04-22
 */
public interface CoLayoutTemplateRootFolder extends CoLayoutTemplateFolder {

	/**
	 * PENDING: The method is used to propagate owners name propably for the sake of the user interface.
	 * That is a bad design and the method should be removed unless used in other
	 * good designs. /Göran S
	 */ 
	public void setName( String name);

	public void addFolder(CoLayoutTemplateFolder subFolder);
	
	public void addTemplate(CoLayoutTemplate layoutTemplate);
	
	/**
	 * Creates and add a folder to a present subfolder. The created folder is returned. <br>
	 */
	public CoLayoutTemplateFolder createAndAddFolderTo(CoLayoutTemplateFolder subFolder,  String folderName);

	/**
	 * Creates a layout template from the layout in the argument and adds it to the specified folder.
	 */
	public void createAndAddLayoutTemplateTo( CoLayoutTemplateFolder folder, String templateName, String templateDescription, CoLayout layout );

	public CoLayoutTemplateFolder getSubFolderByName(String name);
	
}
