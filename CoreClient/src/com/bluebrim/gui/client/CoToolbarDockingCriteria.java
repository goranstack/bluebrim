package com.bluebrim.gui.client;

/**
 * 
 */
 
public abstract class CoToolbarDockingCriteria implements CoToolbarDockingCriteriaIF
{
	public static final CoToolbarDockingCriteria ANYWHERE =
		new CoToolbarDockingCriteria()
			{
				public boolean isDockable( CoToolbar toolbar, CoToolbarDockingBay dockingBay )
				{
					return true;
				}
			};
	
	public static final CoToolbarDockingCriteria HORIZONTAL =
		new CoToolbarDockingCriteria()
			{
				public boolean isDockable( CoToolbar toolbar, CoToolbarDockingBay dockingBay )
				{
					return dockingBay instanceof CoHorizontalToolbarDockingBay;
				}
			};
	
	public static final CoToolbarDockingCriteria VERTICAL =
		new CoToolbarDockingCriteria()
			{
				public boolean isDockable( CoToolbar toolbar, CoToolbarDockingBay dockingBay )
				{
					return dockingBay instanceof CoVerticalToolbarDockingBay;
				}
			}; 
	
protected CoToolbarDockingCriteria()
{
}
}
