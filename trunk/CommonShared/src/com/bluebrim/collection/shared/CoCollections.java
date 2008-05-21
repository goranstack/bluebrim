package com.bluebrim.collection.shared;
import java.util.*;

/**
 * Utility class with static methods that either operate on 
 * or return collections. Some of the methods imitates
 * methods found in the Smalltalk collection classes.
 * 
 * NOTE: There is a similar class called java.util.Collections
 * and also one for iterators called CoIterators. /Markus
 * 
 * @see java.util.Collections
 * @see CoIterators
 * Creation date: (1999-08-23 15:08:15)
 * @author Lasse Svadängs
 */
public abstract class CoCollections {
	public static interface Selector {
		public boolean select(Object element);
	};
	public static interface Detector {
		public boolean detect(Object element);
	};
	public static interface Contains {
		public boolean contains(Object element);
	};
	public static interface Collector {
		public Object collect(Object element);
	};
	public static interface EachDo {
		public void doTo(Object element);
	};
	public static interface Condition {
		public boolean test(Object element);
	};
private static Collection createCollection(Collection collection) {
	Collection result = null;
	try {
		result = (Collection )collection.getClass().newInstance();
	} catch(Exception e) {
		// Failed to instantiate collection of same type.
		// Will create an ArrayList instance as a default
		result = new ArrayList();
	}
	return result;
}


public static List arrayToList(Object elements[])
{
	if (elements == null || elements.length == 0)
		return null;
	List list = new ArrayList(elements.length);
	for (int i=0; i<elements.length; i++)
		list.add(elements[i]);
	return list;
}


/**
 * For each element in <code>collection</code>, add the result of 
 * <code>collector.collect(Object)</code> to a new collection of the
 * same type and return this collection.
 */
public static Collection collect(Collection collection, Collector collector)
{
	try
	{
		Collection 	tCollection = createCollection(collection);
		Iterator	iter		= collection.iterator();
		while(iter.hasNext())
		{
			tCollection.add(collector.collect(iter.next()));
		}
		return tCollection;
	}
	catch (Exception e)
	{
		return null;
	}
}


/**
 * Return true if <code>test.test(Object)</code> answers true for 
 * every element in <code>collection</code>. 
 */
public static boolean complies(Collection collection, Condition test)
{
	Iterator 	iter 			= collection.iterator();
	while(iter.hasNext())
	{
		Object iElement = iter.next();
		if (!test.test(iElement))
			return false;
	}
	return true;
}


/**
 *	Returns a collection which is the concatenation of the two supplied collections
 */
public static Collection concatenate(Collection coll1, Collection coll2) {
	Iterator iter = coll1.iterator();
	Collection list = new ArrayList();
	while(iter.hasNext()) {
		list.add(iter.next());
	}
	iter = coll2.iterator();
	while(iter.hasNext()) {
		list.add(iter.next());
	}
	return list;
}


/**
 * Return true if <code>test.contains(Object)</code> answers true for 
 * at least one element in <code>collection</code>. Return false if 
 * no such element is found.
 */
public static boolean contains(Collection collection, Condition test)
{
	Iterator 	iter 			= collection.iterator();
	while(iter.hasNext())
	{
		Object iElement = iter.next();
		if (test.test(iElement))
			return true;
	}
	return false;
}


/**
 * Return the first element in <code>collection</code> for which
 * <code>detector.detect(Object)</code> returns true. If no element is found,
 * return null.
 */
public static Object detect(Collection collection, Detector detector)
{
	Iterator 	iter 			= collection.iterator();
	while(iter.hasNext())
	{
		Object iElement = iter.next();
		if (detector.detect(iElement))
			return iElement;
	}
	return null;
}


/**
 * Apply <code>doer.doTo</code> on each element in <code>collections</code>.
 */
public static void each(Collection collection, EachDo eachDo)
{
	Iterator 	iter 			= collection.iterator();
	while(iter.hasNext())
	{
		eachDo.doTo(iter.next());
	}
}


/**
 * Answer with the first element iff 
 * <code>coll</code> has exaclty one element, else 
 * answer with null.
 * <br>
 * Take care of the case of <code>coll</code> being null.
 */
public static Object getFirstAndOnlyElement(Collection coll)
{
	if (coll == null || !CoCollections.hasExactlyOneElement(coll))
		return null;
	else
	{
		Iterator iter = coll.iterator();
		return iter.next();
	}
}


public static boolean hasExactlyOneElement(Collection coll)
{
	return coll != null && coll.size() == 1;
}


public static boolean isNullOrEmpty(Collection coll)
{
	return coll == null || coll.isEmpty();
}


public static boolean isNullOrEmpty(Map map)
{
	return map == null || map.isEmpty();
}


/**
 * For each element in <code>collection</code>, add these that returns true 
 * for  <code>selector.select(Object)</code> to a new collection of the
 * same type and return this collection.
 */
public static Collection select(Collection collection, Selector selector)
{
	try
	{
		Collection 	tCollection = createCollection(collection);
		Iterator	iter		= collection.iterator();
		while(iter.hasNext())
		{
			Object iElement = iter.next();
			if (selector.select(iElement))
				tCollection.add(iElement);
		}
		return tCollection;
	}
	catch (Exception e)
	{
		e.printStackTrace();
		return null;
	}
}


/**
 * For each element in <code>collection</code>, send these that returns true 
 * for  <code>selector.select(Object)</code> to <code>collector.collect(Object)</code>
 * and the result to collection of the same type as<code>collection</code>.
 * Return this collection.
 */
public static Collection selectAndCollect(Collection collection, Selector selector, Collector collector)
{
	try
	{
		Collection 	tCollection = createCollection(collection);
		Iterator	iter		= collection.iterator();
		while(iter.hasNext())
		{
			Object iElement = iter.next();
			if (selector.select(iElement))
				tCollection.add(collector.collect(iElement));
		}
		return tCollection;
	}
	catch (Exception e)
	{
		return null;
	}
}
}