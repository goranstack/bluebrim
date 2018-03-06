package com.bluebrim.layout.impl.shared.layoutmanager;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Creation date: (2000-06-05)
 * @author: Dennis
 */
 
public abstract class CoLayoutManager extends CoSimpleObject implements CoLayoutManagerIF
{
	public interface Owner
	{
		void update();
	}
	
	public class MutableProxy implements CoRemoteLayoutManagerIF
	{
		// Attributes
		private Owner m_owner;

		// Methods
		public void setOwner( Owner owner ) { m_owner = owner; }
		public Owner getOwner() { return m_owner; }

		public void addPropertyChangeListener( CoPropertyChangeListener l ) { CoLayoutManager.this.addPropertyChangeListener( l ); }
	 	public void removePropertyChangeListener( CoPropertyChangeListener l ) { CoLayoutManager.this.removePropertyChangeListener( l ); }
  	public CoLayoutManagerIF deepClone() { return CoLayoutManager.this.deepClone(); }
  	public String getFactoryKey() { return CoLayoutManager.this.getFactoryKey(); }
  	public boolean doesSetSize() { return CoLayoutManager.this.doesSetSize(); }
 		public String getPanelClassName() { return CoLayoutManager.this.getPanelClassName(); }
 		public String getLocalizedName() { return CoLayoutManager.this.getLocalizedName(); }

 		public void xmlVisit( CoXmlVisitorIF visitor ) { CoLayoutManager.this.xmlVisit( visitor ); }
  
  	protected void notifyOwner()
  	{
			m_owner.update();
  	}
	};
protected CoLayoutManager.MutableProxy createMutableProxy()
{
	return new MutableProxy();	
}
public CoLayoutManagerIF deepClone()
{
	return this;
}
public abstract String getLocalizedName();

public final CoLayoutManagerIF getMutableProxy( CoLayoutManager.Owner owner )
{
	MutableProxy mp = createMutableProxy();
	mp.setOwner( owner );
	return mp;
}
public String getPanelClassName()
{
	return null;
}
public boolean isNull()
{
	return false;
}
public abstract void layout( CoLayoutableContainerIF parent );

protected static CoLayoutManagerFactoryIF getFactory()
{
	return (CoLayoutManagerFactoryIF) CoFactoryManager.getFactory( CoLayoutManagerIF.LAYOUT_MANAGER );
}

public abstract String xmlGetTag();

public void xmlInit( java.util.Map attributes )
{
	
}

public void xmlVisit( CoXmlVisitorIF visitor )
{
	
}
}