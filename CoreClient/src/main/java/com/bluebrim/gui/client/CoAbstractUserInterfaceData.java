package com.bluebrim.gui.client;

import javax.swing.Icon;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.base.shared.debug.CoAssertion;

/**
 * Abstract class used to represent an ui model to be, i e 
 * an ui where we want to delay the creation until it's asked for. 
 * It's for example used.by a tab ui model where the ui in a tab
 * isn't created until the corresponding tab is selected.
 * <br>
 * Creation date: (2000-03-15 08:19:56)
 * @author: Lasse Svadängs
 */
public abstract class CoAbstractUserInterfaceData {
	private Icon   					m_icon;
	private Object 					m_key;
	private String 					m_name;
	private CoDomainUserInterface 	m_userInterface;
	private	boolean					m_prebuild;
/**
 * CoAbstractUserInterfaceData constructor comment.
 */
public CoAbstractUserInterfaceData() {
	super();
}
public CoAbstractUserInterfaceData(Object key, Icon icon, String name)
{
	this(key, icon ,name, false);
}
public CoAbstractUserInterfaceData(Object key, Icon icon, String name, boolean prebuild)
{
	m_key = key;
	m_icon = icon;
	m_name = name;
	if (prebuild)
		prebuildUserInterface();
}
private synchronized void buildUserInterface()
{
	m_userInterface = createUserInterface();
	m_userInterface.buildForComponent();

}
	protected abstract CoDomainUserInterface createUserInterface();
	public final Icon getIcon()
	{
		return m_icon;
	}
	public final Object getKey()
	{
		return m_key;
	}
	public final String getName()
	{
		return m_name;
	}
private synchronized CoDomainUserInterface getPrebuiltUserInterface()
{
	return m_userInterface;
}
public CoDomainUserInterface getUserInterface()
{
	if (m_prebuild)
		return getPrebuiltUserInterface();
	
	if (m_userInterface == null)
	{
		if (CoAssertion.TRACE)
			CoAssertion.trace("Creating userinterface for " + m_key);
		m_userInterface = createUserInterface();
	}
	return m_userInterface;
}
	public Object getValueFor(CoObjectIF subject)
	{
		return subject;
	}
public boolean isUserInterfaceCreated()
{
	return m_userInterface != null;
}
private void prebuildUserInterface()
{
	Thread prebuildThread = new Thread(new Runnable() {
		public void run()
		{
			if (CoAssertion.TRACE)
				System.out.println("Starting prebuild of "+getName());
			try
			{
				m_prebuild = true;
				buildUserInterface();
			}
			finally
			{
				m_prebuild = false;
			}
			if (CoAssertion.TRACE)
				System.out.println("Finished prebuild of "+getName());
		}
	});
	prebuildThread.start();

}
public final boolean userInterfaceIsCreated()
{
	return m_userInterface != null;
}
}