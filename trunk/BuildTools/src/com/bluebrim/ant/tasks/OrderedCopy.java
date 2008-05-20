package com.bluebrim.ant.tasks;

import java.io.*;
import java.util.*;

import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.selectors.*;

public class OrderedCopy extends Copy
{
	public OrderedCopy()
	{
		this.fileCopyMap = new OrderedHashtable();
	}

	protected void doFileOperations()
	{
		super.doFileOperations();
	}

	/**
	 *  
	 */
	private final class OrderedHashtable extends Hashtable
	{
		Vector orderedKeys = new Vector();

		public synchronized Enumeration keys()
		{
			return orderedKeys.elements();
		}

		public synchronized Object put(Object key, Object value)
		{
			Object old = super.put(key, value);
			if (old == null)
				orderedKeys.addElement(key);
			return old;
		}

		public synchronized Object remove(Object key)
		{
			Object value = super.remove(key);
			if (value != null)
				orderedKeys.removeElement(key);
			return value;
		}
	}
}