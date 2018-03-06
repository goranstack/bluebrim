package com.bluebrim.layout.impl.server;

import java.util.*;

import com.bluebrim.layout.impl.server.geom.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.page.shared.*;

/**
 * @author Göran Stäck 2002-04-05
 */
public class CoLayoutServerImpl implements CoLayoutServer 
{
    private static CoLayoutServerImpl instance;
    private CoPageItemPreferences defaultLayoutParameters;

     
    public CoLayoutServerImpl() 
	{
	}
		
	/**
	 * All parameters in points.
	 */
	public CoImmutableColumnGridIF createColumnGrid(	double width,
														double height, 
														int noOfCols, 
														double colSpacing, 
														double leftMargin, 
														double rigthMargin, 
														double topMargin,
														double bottomMargin) {
		CoRegularColumnGrid columnGrid 	= new CoRegularColumnGrid(noOfCols);
		columnGrid.setBounds(0, 0, width, height, true );
		columnGrid.setColumnSpacing( colSpacing );
		columnGrid.setLeftMargin(  leftMargin );
		columnGrid.setRightMargin( rigthMargin );
		columnGrid.setTopMargin( topMargin );
		columnGrid.setBottomMargin( bottomMargin );
		return columnGrid;
	}
	
	public CoLayout createLayout() {
		return new CoLayoutArea();
	}

	public CoDesktopLayout createDesktopLayout(CoLayoutParameters layoutParameters) {
		return new CoDesktopLayoutArea(layoutParameters);
	}
	
	public CoLayoutTemplateRootFolder createLayoutTemplateTree(CoLayoutParameters layoutParameters) {
		return new CoPageItemPrototypeTreeRoot("",(CoPageItemPreferencesIF)layoutParameters);
	}

	public CoLayoutEditorContext createLayoutEditorContext(CoLayoutParameters layoutParameters, CoLayoutTemplateCollection tools, List contentRecievers, CoLayoutTemplateRootFolder templateTree)  {
		return new CoPageItemEditorContext((CoPageItemPreferencesIF)layoutParameters, (CoPageItemPrototypeCollectionIF)tools, contentRecievers, (CoPageItemPrototypeTreeRootIF)templateTree);
	}
	
	public CoLayoutParameters createLayoutParameters() {
		return new CoPageItemPreferences();
	}

	public CoPageLayout createPageLayout(CoPage page) {
		return new CoPageLayoutArea(page);
	}

	public CoLayoutParameters getDefaultLayoutParameters() {
		return defaultLayoutParameters;
	}

	public CoPageItemPreferences getDefaultPageItemPreferences() {
		return defaultLayoutParameters;
	}

	public CoLayoutContentIF createLayoutContent( CoLayoutParameters layoutParameters) {
		CoLayoutContentIF layoutContent = new CoLayoutContent();
		layoutContent.getLayout().setLayoutParameters(layoutParameters);
		return layoutContent;
	}

    public static CoLayoutServerImpl getInstance()
    {
        return instance;
    }
    
    public static void setInstance(CoLayoutServerImpl instance)
    {
        CoLayoutServerImpl.instance = instance;
    }
    
    public void setDefaultLayoutParameters(CoPageItemPreferences defaultLayoutParameters)
    {
        this.defaultLayoutParameters = defaultLayoutParameters;
    }
}
