package com.bluebrim.layout.impl.server.manager;

import java.util.Hashtable;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoLayoutSpecFactoryIF;
import com.bluebrim.layout.impl.shared.CoLocationSpecIF;
import com.bluebrim.layout.shared.*;

/**
 	Factoryclass for getting access to all locationspec singletons.
 */
public class CoLayoutSpecFactory implements CoLayoutSpecFactoryIF
{


	private CoLocationSpecIF m_bottomLocation = new CoBottomLocation();


	private CoLocationSpecIF m_centerLocation = new CoCenterLocation();
	private CoLocationSpecIF m_leftLocation = new CoLeftLocation();
	private CoLocationSpecIF m_noLocation = new CoNoLocation();
	private CoLocationSpecIF m_slaveLocation = new CoSlaveLocation();
	private CoLocationSpecIF m_rightLocation = new CoRightLocation();


	private CoLocationSpecIF m_topLocation = new CoTopLocation();



	
	private CoLocationSpecIF m_insetLocationSpecTL = new CoTopLeftLocationSpec();
	private CoLocationSpecIF m_insetLocationSpecTR = new CoTopRightLocationSpec();
	private CoLocationSpecIF m_insetLocationSpecBL = new CoBottomLeftLocationSpec();
	private CoLocationSpecIF m_insetLocationSpecBR = new CoBottomRightLocationSpec();

	private CoLocationSpecIF m_topOutsideLocationSpec = new CoTopOutsideLocationSpec();
	private CoLocationSpecIF m_topInsideLocationSpec = new CoTopInsideLocationSpec();
	private CoLocationSpecIF m_bottomOutsideLocationSpec = new CoBottomOutsideLocationSpec();
	private CoLocationSpecIF m_bottomInsideLocationSpec = new CoBottomInsideLocationSpec();
	

	private CoSizeSpecIF m_noSizeSpec = new CoNoSizeSpec();
	private CoSizeSpecIF m_slaveSizeSpec = new CoSlaveSizeSpec();
	private CoSizeSpecIF m_absoluteWidthSpec = new CoAbsoluteWidthSpec( 1 );
	private CoSizeSpecIF m_contentWidthSpec = new CoContentWidthSpec();
	private CoSizeSpecIF m_fillWidthSpec = new CoFillWidthSpec();
	private CoSizeSpecIF m_proportionalWidthSpec = new CoProportionalWidthSpec( false, 1 );

	private CoSizeSpecIF m_absoluteHeightSpec = new CoAbsoluteHeightSpec( 200 );
	private CoSizeSpecIF m_contentHeightSpec = new CoContentHeightSpec();
	private CoSizeSpecIF m_fillHeightSpec = new CoFillHeightSpec();
	private CoSizeSpecIF m_proportionalHeightSpec = new CoProportionalHeightSpec( false, 1 );

	private Hashtable m_keyToInstanceMap = new Hashtable();
	{
		m_keyToInstanceMap.put( m_bottomLocation.getFactoryKey(), m_bottomLocation );
		m_keyToInstanceMap.put( m_centerLocation.getFactoryKey(), m_centerLocation );
		m_keyToInstanceMap.put( m_leftLocation.getFactoryKey(), m_leftLocation );
		m_keyToInstanceMap.put( m_noLocation.getFactoryKey(), m_noLocation );
		m_keyToInstanceMap.put( m_slaveLocation.getFactoryKey(), m_slaveLocation );
		m_keyToInstanceMap.put( m_rightLocation.getFactoryKey(), m_rightLocation );
		m_keyToInstanceMap.put( m_topLocation.getFactoryKey(), m_topLocation );
		
		m_keyToInstanceMap.put( m_noSizeSpec.getFactoryKey(), m_noSizeSpec );
		m_keyToInstanceMap.put( m_slaveSizeSpec.getFactoryKey(), m_slaveSizeSpec );
		m_keyToInstanceMap.put( m_fillWidthSpec.getFactoryKey(), m_fillWidthSpec );

		m_keyToInstanceMap.put( m_fillHeightSpec.getFactoryKey(), m_fillHeightSpec );
	}


	
	private Hashtable m_keyToPrototypeMap = new Hashtable();
	{

		//m_keyToPrototypeMap.put( m_insetLocationSpec.getFactoryKey(),m_insetLocationSpec);
		
		m_keyToPrototypeMap.put( m_insetLocationSpecTL.getFactoryKey(),m_insetLocationSpecTL);
		m_keyToPrototypeMap.put( m_insetLocationSpecTR.getFactoryKey(),m_insetLocationSpecTR);
		m_keyToPrototypeMap.put( m_insetLocationSpecBL.getFactoryKey(),m_insetLocationSpecBL);
		m_keyToPrototypeMap.put( m_insetLocationSpecBR.getFactoryKey(),m_insetLocationSpecBR);

		m_keyToPrototypeMap.put( m_topOutsideLocationSpec.getFactoryKey(),m_topOutsideLocationSpec);
		m_keyToPrototypeMap.put( m_topInsideLocationSpec.getFactoryKey(),m_topInsideLocationSpec);
		m_keyToPrototypeMap.put( m_bottomOutsideLocationSpec.getFactoryKey(),m_bottomOutsideLocationSpec);
		m_keyToPrototypeMap.put( m_bottomInsideLocationSpec.getFactoryKey(),m_bottomInsideLocationSpec);

		m_keyToPrototypeMap.put( m_absoluteWidthSpec.getFactoryKey(), m_absoluteWidthSpec );
		m_keyToPrototypeMap.put( m_proportionalWidthSpec.getFactoryKey(), m_proportionalWidthSpec );
		m_keyToPrototypeMap.put( m_contentWidthSpec.getFactoryKey(), m_contentWidthSpec );

		m_keyToPrototypeMap.put( m_absoluteHeightSpec.getFactoryKey(), m_absoluteHeightSpec );
		m_keyToPrototypeMap.put( m_proportionalHeightSpec.getFactoryKey(), m_proportionalHeightSpec );
		m_keyToPrototypeMap.put( m_contentHeightSpec.getFactoryKey(), m_contentHeightSpec );
	}


/**
 * getAbsoluteLocation method comment.
 */
public CoLayoutSpecIF createLayoutSpec()
{
	return new CoLayoutSpec();
}
/**
 * createObject method comment.
 */
public com.bluebrim.base.shared.CoFactoryElementIF createObject()
{
	return null;
}
public CoSizeSpecIF getAbsoluteHeightSpec()
{
	return getAbsoluteHeightSpec( 200 );
}
public CoSizeSpecIF getAbsoluteHeightSpec( double d )
{
	return new CoAbsoluteHeightSpec( d );
}
public CoSizeSpecIF getAbsoluteWidthSpec()
{
	return getAbsoluteWidthSpec( 1 );
}
public CoSizeSpecIF getAbsoluteWidthSpec( double d )
{
	return new CoAbsoluteWidthSpec( d );
}

/**
 * getBottomLeftLocation method comment.
 */
public CoLocationSpecIF getBottomLeftLocation()
{
	return new CoBottomLeftLocationSpec();
}
/**
 * getBottomLocation method comment.
 */
public CoLocationSpecIF getBottomLocation() {
	return m_bottomLocation;
}

/**
 * getBottomRightLocation method comment.
 */
public CoLocationSpecIF getBottomRightLocation()
{
	return new CoBottomRightLocationSpec();
}
/**
 * getCenterLocation method comment.
 */
public CoLocationSpecIF getCenterLocation() {
	return m_centerLocation;
}
public CoSizeSpecIF getContentHeightSpec()
{
	return new CoContentHeightSpec();
}
public CoSizeSpecIF getContentWidthSpec()
{
	return new CoContentWidthSpec();
}
public CoSizeSpecIF getFillHeightSpec()
{
	return m_fillHeightSpec;
}
	public CoSizeSpecIF getFillWidthSpec()
	{
		return m_fillWidthSpec;
	}
/**
 * getLeftLocation method comment.
 */
public CoLocationSpecIF getLeftLocation() {
	return m_leftLocation;
}
/**
 * getAbsoluteLocation method comment.
 */
public CoLocationSpecIF getLocationSpec( String key )
{
	CoLocationSpecIF ls = (CoLocationSpecIF) m_keyToInstanceMap.get( key );

	if ( ls != null ) return ls;

	ls = (CoLocationSpecIF) m_keyToPrototypeMap.get( key );

	if ( ls != null ) return (CoLocationSpecIF) ls.deepClone();

	return null;
}
/**
 * getAbsoluteLocation method comment.
 */
public CoLocationSpecIF getNoLocation() {
	return m_noLocation;
}
public CoSizeSpecIF getNoSizeSpec()
{
	return m_noSizeSpec;
}




/**
 * getRightLocation method comment.
 */
public CoLocationSpecIF getRightLocation() {
	return m_rightLocation;
}
public CoSizeSpecIF getSizeSpec( String key, CoShapePageItemIF owner )
{
	CoSizeSpecIF ls = (CoSizeSpecIF) m_keyToInstanceMap.get( key );

	if ( ls != null ) return ls;

	ls = (CoSizeSpecIF) m_keyToPrototypeMap.get( key );
	ls.configure( owner );
	
	if ( ls != null ) return (CoSizeSpecIF) ls.deepClone();

	return null;
}
/**
 * getAbsoluteLocation method comment.
 */
public CoLocationSpecIF getSlaveLocation() {
	return m_slaveLocation;
}
public CoSizeSpecIF getSlaveSizeSpec()
{
	return m_slaveSizeSpec;
}

/**
 * getTopLeftLocation method comment.
 */
public CoLocationSpecIF getTopLeftLocation()
{
	return new CoTopLeftLocationSpec();
}
/**
 * getTopLocation method comment.
 */
public CoLocationSpecIF getTopLocation() {
	return m_topLocation;
}

/**
 * getTopRightLocation method comment.
 */
public CoLocationSpecIF getTopRightLocation()
{
	return new CoTopRightLocationSpec();
}

public CoSizeSpecIF getProportionalHeightSpec( boolean useTopLevelAncestorAsBase, double maxProportionValue )
{
	return new CoProportionalHeightSpec( useTopLevelAncestorAsBase, maxProportionValue );
}

public CoSizeSpecIF getProportionalHeightSpec( boolean useTopLevelAncestorAsBase, double maxProportionValue, double p )
{
	return new CoProportionalHeightSpec( useTopLevelAncestorAsBase, maxProportionValue, p );
}

public CoSizeSpecIF getProportionalWidthSpec(boolean useTopLevelAncestorAsBase, double maxProportionValue )
{
	return new CoProportionalWidthSpec( useTopLevelAncestorAsBase, maxProportionValue );
}

public CoSizeSpecIF getProportionalWidthSpec( boolean useTopLevelAncestorAsBase, double maxProportionValue, double p )
{
	return new CoProportionalWidthSpec( useTopLevelAncestorAsBase, maxProportionValue, p );
}
}