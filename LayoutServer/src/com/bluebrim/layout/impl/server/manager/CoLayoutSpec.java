package com.bluebrim.layout.impl.server.manager;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Every layoutable (implementation of CoLayoutableIF) has a layout specification that defines its requsted layout behavior.
 * Layout behavior is divided into three parts:
 *  - location specification = the requested location of the layoutable.
 *  - width specification = the requested width of the layoutable.
 *  - height specification = the requested height of the layoutable.
 *
 * This class also implements part of the layout engine.
 * It is the task of the layout engine to move and reshape the layoutable according to these requests.
 * However the  requests of the layoutables parent, siblings and children must also be considered and resulting conflicts be resolved.
 * 
 * @author: Dennis Malmström
 */
 
public class CoLayoutSpec extends CoSimpleObject implements CoLayoutSpecIF
{
	// constants used to control the semantics of the layout engine (see method layout)
	public static final int CONTENT = 1;
	public static final int ALL_BUT_CONTENT = 2;
	public static final int ALL = CONTENT | ALL_BUT_CONTENT;

	// location and size specs
	protected CoLocationSpec m_locationSpec;
	protected CoSizeSpec m_heightSpec;
	protected CoSizeSpec m_widthSpec;
	{
		CoLayoutSpecFactoryIF f = (CoLayoutSpecFactoryIF) CoFactoryManager.getFactory( CoLayoutSpecIF.LAYOUT_SPEC );

		m_heightSpec = (CoSizeSpec) f.getNoSizeSpec();
		m_widthSpec = (CoSizeSpec) f.getNoSizeSpec();
		m_locationSpec = (CoLocationSpec) f.getNoLocation();
	}

public CoLayoutSpec()
{
}
public CoLayoutSpecIF deepClone()
{
	CoLayoutSpec ls = null;
	try
	{
		ls = (CoLayoutSpec) clone();
	}
	catch ( CloneNotSupportedException ex )
	{
		throw new RuntimeException( getClass() + ".clone() failed" );
	}
	
	ls.m_locationSpec = (CoLocationSpec) m_locationSpec.deepClone();
	ls.m_heightSpec = (CoSizeSpec) m_heightSpec.deepClone();
	ls.m_widthSpec = (CoSizeSpec) m_widthSpec.deepClone();
	
	return ls;
}
public String getFactoryKey()
{
	return LAYOUT_SPEC;
}
public CoImmutableSizeSpecIF getHeightSpec()
{
	return m_heightSpec;
}
public CoImmutableLocationSpecIF getLocationSpec()
{
	return m_locationSpec;
}
public CoImmutableSizeSpecIF getWidthSpec()
{
	return m_widthSpec;
}
// does location spec specify absolute positioning ?

public boolean hasAbsolutePosition()
{
	return ( m_locationSpec != null ) ? m_locationSpec.isAbsolutePosition() : true;
}
// Does the height or width spec depend on the contents of the layoutable ?

public boolean isContentDependent()
{
	return m_heightSpec.isContentDependent() || m_widthSpec.isContentDependent();
}
/**
 * Lay out a layoutable
 *
 * sizeSpecMask:
 *   CONTENT         = only apply content dependant size specs
 *   ALL_BUT_CONTENT = don't apply content dependant size specs
 *   ALL             = apply all size 
 *
 * skipLocation: don't apply location spec
*/
	
public void layout( CoLayoutableIF layoutable, int sizeSpecMask, boolean skipLocation )
{
	// apply size specs before location
	m_widthSpec.setSizeBeforeLocation( layoutable, sizeSpecMask );
	m_heightSpec.setSizeBeforeLocation( layoutable, sizeSpecMask );

	// apply location spec
	if
		(
			( ! skipLocation )
		&&
			( ( sizeSpecMask & ALL_BUT_CONTENT ) != 0 )
		)
	{
		m_locationSpec.placeLayoutable( layoutable );
	}
	
	// apply size specs after location
	m_widthSpec.setSizeAfterLocation( layoutable, m_locationSpec, sizeSpecMask );
	m_heightSpec.setSizeAfterLocation( layoutable, m_locationSpec, sizeSpecMask );


	// Special case:
	//  Text box + height spec = content height -> depends on width
	//  If width is set after location (width spec = fill) then no valid
	//  width was available when the height was calculated so we must calculate the height again.
	if
		(
			( m_heightSpec instanceof CoContentHeightSpec )
		&&
			( m_widthSpec instanceof CoFillWidthSpec )
		&&
			( layoutable instanceof CoContentWrapperPageItemIF )
		&&
			( ( (CoContentWrapperPageItemIF) layoutable ).getContent() instanceof CoPageItemAbstractTextContentIF )
		)
	{
		// reapply height specs before location
		m_heightSpec.setSizeBeforeLocation( layoutable, sizeSpecMask );
		
		// reapply location spec
		if
			( ! skipLocation )
		{	
			m_locationSpec.placeLayoutable( layoutable );
		}
// 2000-12-07, Dennis		m_locationSpec.placeLayoutable( layoutable );
		
		// reapply height specs after location
		m_heightSpec.setSizeAfterLocation( layoutable, m_locationSpec, sizeSpecMask );
	}
}
public void setHeightSpec( CoImmutableSizeSpecIF heightSpec )
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( heightSpec != null , "....................................." );
	m_heightSpec = (CoSizeSpec) heightSpec;
}
public void setLocationSpec(CoImmutableLocationSpecIF locationSpec)
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( locationSpec != null , "....................................." );
	m_locationSpec = (CoLocationSpec) locationSpec;
}
public void setWidthSpec( CoImmutableSizeSpecIF widthSpec )
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( widthSpec != null , "....................................." );
	m_widthSpec = (CoSizeSpec) widthSpec;
}
public String toString()
{
	return ("CoLayoutSpec \n\t Placering: " + m_locationSpec.toString() + " H*B: " + m_heightSpec.toString() + " " + m_widthSpec.toString());
}
// see CoLocationSpec.usesBottomSpace( CoLayoutableContainerIF )

public boolean usesBottomSpace(CoLayoutableContainerIF layoutArea)
{
	return m_locationSpec.usesBottomSpace(layoutArea);
}
// see CoLocationSpec.usesLeftSpace( CoLayoutableContainerIF )

public boolean usesLeftSpace(CoLayoutableContainerIF layoutArea)
{
	return m_locationSpec.usesLeftSpace(layoutArea);
}
// see CoLocationSpec.usesRightSpace( CoLayoutableContainerIF )

public boolean usesRightSpace(CoLayoutableContainerIF layoutArea)
{
	return m_locationSpec.usesRightSpace(layoutArea);
}
// see CoLocationSpec.usesTopSpace( CoLayoutableContainerIF )

public boolean usesTopSpace(CoLayoutableContainerIF layoutArea)
{
	return m_locationSpec.usesTopSpace(layoutArea);
}

public boolean equals( Object o )
{
	if ( this == o ) return true;
	if
		( o instanceof CoLayoutSpecIF )
	{
		CoLayoutSpecIF l = (CoLayoutSpecIF) o;
		
		return 
			(
				( m_locationSpec.equals( l.getLocationSpec() ) )
			&&
				( m_heightSpec.equals( l.getHeightSpec() ) )
			&&
				( m_widthSpec.equals( l.getWidthSpec() ) )
			);
	} else {
		return super.equals( o );
	}
}

public int hashCode()
{
	return m_locationSpec.hashCode();
}

public void set( CoImmutableLocationSpecIF locationSpec, CoImmutableSizeSpecIF widthSpec, CoImmutableSizeSpecIF heightSpec )
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( widthSpec != null , "....................................." );
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( locationSpec != null , "....................................." );
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( heightSpec != null , "....................................." );

	m_widthSpec = (CoSizeSpec) widthSpec;
	m_locationSpec = (CoLocationSpec) locationSpec;
	m_heightSpec = (CoSizeSpec) heightSpec;
}

public boolean isNull()
{
	return m_locationSpec.isNull() && m_heightSpec.isNull() && m_widthSpec.isNull();
}
}