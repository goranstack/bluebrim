package com.bluebrim.base.shared.debug;

import java.lang.reflect.*;
import java.util.*;


/**
 * This class implemens the operation of comparing two object structures.
 * It recursively traverses all references that implement the interface CoDiffable.
 * References that do not implement the interface CoDiffable are compare using the eqauls method (default behavior).
 * Comparison by identity or a custom equality operations can be forced upon references.
 * <p>
 * Important: The method in CoDiffable MUST be implemented by all subclasses of a class implementing CoDiffable.
 *            The comment in the body CoDiffable contains implementation for the CoDiffable-implementing class and its subclasses.
 *            This might seem strange but it is nesseccarry in order to work around the attribute visibility mechanism.
 * <p>
 * The results of the comparison are sent to an instance of CoDiffReport.
 * <p>
 * Creation date: (2001-03-06 10:44:40)
 * @author: Dennis
 */

public class CoDiff
{
	// see constructor for content classes
	private List m_compareByIdentity = new ArrayList();
	private List m_compareByEquality = new ArrayList();
	private Map m_equalityMap = new HashMap();
	private List m_ignore = new ArrayList();
	private String [] m_ignorePrefixes;
	private String [] m_ignoreSuffixes;
	/** comparison result receiver */
	private CoDiffReport m_report;

	/** Custom equality operation protocol */
	public interface Equality
	{
		boolean equals( Object o1, Object o2 );
	}

/**
@param compareByIdentity
An array containing String and Class objects. Matching attributes are compared by identity.
Matching is performed both on the attribute name (String match) and on the attribute's class.

@param compareByEquality
An array containing String and Class objects. Matching attributes are compared by equality
(only useful for attributes that implement CoDiffable).
Matching is performed both on the attribute name (String match) and on the attribute's class.

@param ignore
An array containing String and Class objects. Matching attributes are ignored while diffing.
Matching is performed both on the attribute name (String match) and on the attribute's class.

@param ignoreSuffixes
An array of Strings.  Attributes with any of the specified suffixes are ignored.

@param ignorePrefixes
An array of Strings.  Attributes with any of the specified prefixes are ignored.

@param equalities
An array containing String and Class objects. Matching attributes are compared by custom equality operation.
Matching is performed both on the attribute name (String match) and on the attribute's class.
*/
public CoDiff(
	Object [] compareByIdentity, // [ String (attribute name) or Class (attribute type) ], force comparison by identity
	Object [] compareByEquality, // [ String (attribute name) or Class (attribute type) ], force comparison by identity (only useful for attributes that implement CoDiffable)
	Object [] ignore,            // [ String (attribute name) or Class (attribute type) ], don't compare
	String [] ignoreSuffixes,    // [ String (attribute name suffixes) ], don't compare
	String [] ignorePrefixes,    // [ String (attribute name prefixes) ], don't compare
	Object [] equalities )       // [ String (attribute name) or Class (attribute type), Equality, ... ], force comparison by custom equality operation
{
	m_compareByIdentity = Arrays.asList( compareByIdentity );
	m_compareByEquality = Arrays.asList( compareByEquality );
	m_ignore = Arrays.asList( ignore );
	m_ignoreSuffixes = ignoreSuffixes;
	m_ignorePrefixes = ignorePrefixes;

	for
		( int i = 0; i + 1 < equalities.length; i += 2 )
	{
		m_equalityMap.put( equalities[ i ], equalities[ i + 1 ] );
	}
	
}

/**
@param prefix is a <code>String</code> containing only whitespace.  It is used for
indentation of the output.
*/
private void diff( String attributeName, CoDiffable diffable1, CoDiffable diffable2, String prefix ) throws IllegalAccessException
{
	m_report.comparing( prefix, attributeName, diffable1, diffable2 );
	Class c = diffable1.getClass();

	if
		( doIgnore( c, attributeName ) )
	{
	} else if
		( ! c.equals( diffable2.getClass() ) )
	{
		m_report.typeClash( prefix, diffable1, diffable2 );
	} else {
	
		while
			( c != null )
		{
			Field [] fields = c.getDeclaredFields();
			for
				( int i = 0; i < fields.length; i++ )
			{
				Field field = fields[ i ];
				int modifiers = field.getModifiers();
				
				if ( Modifier.isStatic( modifiers ) ) continue;
				
				Class fieldType = field.getType();

				if ( doIgnore( fieldType, field.getName() ) ) continue;
				
				Object value1 = diffable1.getAttributeValue( field );
				Object value2 = diffable2.getAttributeValue( field );

				if
					(! value1.getClass().equals(value2.getClass()))
				{
					m_report.typeClash( prefix, field.getName(), -1, value1, value2 );
				} else if
					( m_compareByIdentity.contains( fieldType ) || m_compareByIdentity.contains( field.getName() ) )
				{
					if
						( value1 != value2 )
					{
						m_report.notIdentical( prefix, field.getName(), -1, value1, value2 );
					}
					
					
				} else if
					( m_compareByEquality.contains( fieldType ) || m_compareByEquality.contains( field.getName() ) )
				{
					if
						( ! equals( fieldType, field.getName(), value1, value2 ) )
					{
						m_report.notEqual( prefix, field.getName(), -1, value1, value2 );
					}
					
					
				} else if
					( field.getType().isArray() )
				{
					Object [] array1 = (Object []) value1;
					Object [] array2 = (Object []) value2;
					
					if
						( ( array1 == null ) && ( array2 == null ) )
					{
						
					} else if
						( ( array1 == null ) || ( array2 == null ) )
					{
						m_report.notIdentical( prefix, field.getName(), -1, array1, array2 );
					} else if
						( array1.length != array2.length )
					{
						m_report.differentArrayLengths( prefix, field.getName(), array1.length, array2.length );
					} else {
						Class arrayType = field.getType().getComponentType();
						
						if
							( m_compareByIdentity.contains( arrayType ) )
						{
							for
								( int n = 0; n < array1.length; n++ )
							{
								if
									( array1[ n ] != array2[ n ] )
								{
									m_report.notIdentical( prefix, field.getName(), n, array1[ n ], array2[ n ] );
								}
							}
						} else if
							( ( getEquality( arrayType, field.getName() ) == null ) && CoDiffable.class.isAssignableFrom( arrayType ) )
						{
							for
								( int n = 0; n < array1.length; n++ )
							{
								diff( field.getName(), (CoDiffable) array1[ n ], (CoDiffable) array2[ n ], prefix + "  " );
							}
						} else {
							for
								( int n = 0; n < array1.length; n++ )
							{
								if
									( ! equals( arrayType, field.getName(), array1[ n ], array2[ n ] ) )
								{
									m_report.notEqual( prefix, field.getName(), n, array1[ n ], array2[ n ] );
								}
							}
						}
					}
					
					
				} else if
					( Collection.class.isAssignableFrom( fieldType )  )
				{
					Collection collection1 = (Collection) value1;
					Collection collection2 = (Collection) value2;
					
					if
						( ( collection1 == null ) && ( collection2 == null ) )
					{
						
					} else if
						( ( collection1 == null ) || ( collection2 == null ) )
					{
						m_report.notIdentical( prefix, field.getName(), -1, collection1, collection2 );
					} else if
						( collection1.size() != collection2.size() )
					{
						m_report.differentCollectionLengths( prefix, field.getName(), collection1.size(), collection2.size() );
					} else {
						Object [] items1 = collection1.toArray();
						Object [] items2 = collection2.toArray();
						
						for
							( int n = 0; n < items1.length; n++ )
						{
							Object item1 = items1[ n ];
							Object item2 = items2[ n ];
							
							if
								( ! item1.getClass().equals( item2.getClass() ) )
							{
								m_report.typeClash( prefix, field.getName(), n, item1, item2 );
								continue;
							}
							
							Class itemClass = item1.getClass();
							if
								( m_compareByIdentity.contains( itemClass ) )
							{
								if
									( item1 != item2 )
								{
									m_report.notIdentical( prefix, field.getName(), n, item1, item2 );
								}
							} else if
								( ( getEquality( itemClass, field.getName() ) == null ) && CoDiffable.class.isAssignableFrom( itemClass ) )
							{
								diff( field.getName(), (CoDiffable) item1, (CoDiffable) item2, prefix + "  " );
							} else {
								if
									( ! equals( itemClass, field.getName(), item1, item2 ) )
								{
									m_report.notEqual( prefix, field.getName(), n, item1, item2 );
								}
							}
						}
					}
					
					
				} else if
					( ( getEquality( fieldType, field.getName() ) == null ) && CoDiffable.class.isInstance( value1 ) && CoDiffable.class.isInstance( value2 ) )
				{
					diff( field.getName(), (CoDiffable) value1, (CoDiffable) value2, prefix + "  " );
					
					
				} else {
					if
						( ! equals( fieldType, field.getName(), value1, value2 ) )
					{
						m_report.notEqual( prefix, field.getName(), -1, value1, value2 );
					}
				}
			}
			
			
			c = c.getSuperclass();
		}        
	}

}

/**
Compare two objects.  If no report destination has been set using <code>setReport</code>,
a null report that ignores all differences will be created.  Before using this method,
you will probably want to call <code>setReport()</code> with a new
<code>CoDefaultDiffReport</code> (that sends the reports to System.err).
*/

public void diff( CoDiffable diffable1, CoDiffable diffable2 )
{
	if ( m_report == null ) m_report = new CoNullDiffReport();

	try
	{
		diff( null, diffable1, diffable2, "" );
	}
	catch ( IllegalAccessException ex )
	{
		m_report.diffFailed( ex );
	}
}

/**
@param type The class whose attribute should possibly be ignored.

@param attributeName the attribute that should possibly be ignored.

@return whether a certain attribute of a certain class should be ignored or not.
*/
private boolean doIgnore( Class type, String attributeName )
{
	if ( m_ignore.contains( type ) ) return true;

	if ( attributeName == null ) return false;
	if ( attributeName.length() == 0 ) return false;
	
	if ( m_ignore.contains( attributeName ) ) return true;
	
	for
		( int i = 0; i < m_ignoreSuffixes.length; i++ )
	{
		if
			( attributeName.endsWith( m_ignoreSuffixes[ i ] ) )
		{
			return true;
		}
	}
	
	for
		( int i = 0; i < m_ignorePrefixes.length; i++ )
	{
		if
			( attributeName.startsWith( m_ignorePrefixes[ i ] ) )
		{
			return true;
		}
	}

	return false;
}

private boolean equals( Class type, String attributeName, Object o1, Object o2 )
{
	Equality e = getEquality( type, attributeName );

	if
		( e != null )
	{
		return e.equals( o1, o2 );
	}

	if ( o1 == o2 ) return true;
	if ( o1 == null ) return true;
	if ( o2 == null ) return true;

	return o1.equals( o2 );
}

private Equality getEquality( Class type, String attributeName )
{
	Equality e = (Equality) m_equalityMap.get( attributeName );
	if ( e == null ) e = (Equality) m_equalityMap.get( type );

	return e;
}

/**
Tell the CoDiff object where to report differences.  Most common is to use a
<code>CoDefaultDiffReport</code> (that sends reports to System.err) for
reporting.
*/
public void setReport( CoDiffReport report )
{
	m_report = report;
}
}