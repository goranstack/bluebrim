package com.bluebrim.collection.shared;

import java.util.*;

import javax.swing.*;

/**
 * This class is influenced by UIDefaults, but remade to remove
 * UI dependencies.
 *
 * @see UIDefaults
 * @author Markus Persson 1999-06-22
 */
public class CoLazyMap extends HashMap
{
	private static final Object PENDING = new String("Pending");
	
	public static interface SpecialValue {
		public Object getValue(Map map, Object key);
	}
	
	public static abstract class LazyValue implements SpecialValue {
		public Object getValue(Map map, Object key) {
			/*
			 * By doing this, if an exception is thrown,
			 * we'll just put the LazyValue back in the table.
			 * Then we return null.
			 * PENDING: Should be cleaner (and clearer)?
			 */
			Object value = this;
			
			synchronized(map) {
				map.put(key, PENDING);
			}

			try {
				value = createValue();
			} finally {
				synchronized(map) {
					if (value == null) {
						map.remove(key);
					} else {
						map.put(key, value);
					}
					map.notify();
				}
			}
			// If we got the LazyValue back due to exception, return null.
			return (value == this) ? null : value;
		}
		
		protected abstract Object createValue();
	}

	public static abstract class ActiveValue implements SpecialValue {
		public Object getValue(Map map, Object key) {
			return createValue();
		}
		
		protected abstract Object createValue();
	}
/**
 * Create an empty map.
 */
public CoLazyMap() {
	super();
}
/**
 * Create a map initialized with the specified
 * key/value pairs.  For example:
 * <pre>
	Object[] typeEditors = {
		com.bluebrim.properties.shared.CoEnumerationIF.class, new CoEnumerationEditor(),
		CoStringOrEnumerationIF.class, new LazyValue() {
				protected createValue() { return new CoStringOrEnumerationEditor(); }},
	}
	Map propTypeEditors = new CoLazyMap(typeEditors);
 * </pre>
 */
public CoLazyMap(Object[] keyValueList) {
	super(keyValueList.length / 2);
	for(int i = 0; i < keyValueList.length; i += 2) {
		super.put(keyValueList[i], keyValueList[i + 1]);
	}
}
/**
 * Returns the value for key.  If the value is a
 * <code>CoLazyMap.LazyValue</code> then the real
 * value is computed with <code>LazyValue.createValue()</code>,
 * the table entry is replaced, and the real value is returned.
 * If the value is an <code>CoLazyMap.ActiveValue</code>
 * the table entry is not replaced - the value is computed
 * with ActiveValue.createValue() for each get() call.
 *
 * @see SpecialValue
 * @see LazyValue
 * @see ActiveValue
 * @see java.util.Map#get
 */
public Object get(Object key)
{
	// Quickly handle the common case, without grabbing a lock.
	Object value = super.get(key);
	if ((value != PENDING) && !(value instanceof SpecialValue)) {
		return value;
	}

	/* If the LazyValue for key is being constructed by another
	 * thread then grab a lock, wait and then return the new value,
	 * otherwise construct the SpecialValue.
	 * We use the special value PENDING to mark LazyValues that
	 * are being constructed.
	 */
	if (value == PENDING) {
		synchronized(this) {
			do {
				try {
					this.wait();
				}
				catch (InterruptedException e) {
				}
				value = super.get(key);
			} while(value == PENDING);
			// If we got a LazyValue (due to exception
			// in the creating thread), return null.
			return (value instanceof LazyValue) ? null : value;
		}
	} else {
		return ((SpecialValue)value).getValue(this, key);
	}
}
/**
 * If the value of <code>key</code> is a Integer return its
 * integer value, otherwise return 0.
 */
public int getInt(Object key) {
	Object value = get(key);
	return (value instanceof Integer) ? ((Integer)value).intValue() : 0;
}
/**
 * If the value of <code>key</code> is a String return it, otherwise
 * return null.
 */
public String getString(Object key) {
	Object value = get(key);
	return (value instanceof String) ? (String)value : null;
}
/**
 * Set the value of <code>key</code> to <code>value</code>.
 * If value is null, the key is removed from the table.
 *
 * @param key    the unique Object who's value will be used to 
 *               retreive the data value associated with it
 * @param value  the new Object to store as data under that key
 * @return the previous Object value, or null
 * @see java.util.Map#put
 */
public Object put(Object key, Object value) {
	return (value == null) ? super.remove(key) : super.put(key, value);
}
/**
 * Put all of the key/value pairs in the map.
 *
 * @see #put
 * @see java.util.Map#put
 */
public void putAll(Object[] keyValueList) {
	for(int i = 0; i < keyValueList.length; i += 2) {
		Object value = keyValueList[i + 1];
		if (value == null) {
			super.remove(keyValueList[i]);
		}
		else {
			super.put(keyValueList[i], value);
		}
	}
}
}
