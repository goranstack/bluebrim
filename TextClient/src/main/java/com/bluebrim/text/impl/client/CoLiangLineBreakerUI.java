package com.bluebrim.text.impl.client;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-09-26 09:09:26)
 * @author: Dennis
 */
 
public class CoLiangLineBreakerUI extends CoAbstractLineBreakerUI
{
	public static final String MINIMUM_PREFIX_LENGTH = "CoLiangLineBreakerUI.MINIMUM_PREFIX_LENGTH";
	public static final String MINIMUM_SUFFIX_LENGTH = "CoLiangLineBreakerUI.MINIMUM_SUFFIX_LENGTH";
	public static final String MINIMUM_WORD_LENGTH = "CoLiangLineBreakerUI.MINIMUM_WORD_LENGTH";
/**
 * CoLiangLineBreakerUI constructor comment.
 */
public CoLiangLineBreakerUI() {
	super();
}

protected CoUserInterfaceBuilder createUserInterfaceBuilder()
{
    return new CoNumberUserInterfaceBuilder(this);
}

protected void createValueModels( CoUserInterfaceBuilder b )
{
	super.createValueModels( b );
	CoNumberUserInterfaceBuilder numberBuilder = (CoNumberUserInterfaceBuilder)b;

	CoAspectAdaptor ad;

	ad = new CoGsAspectAdaptor( this, MINIMUM_PREFIX_LENGTH )
	{
		public Object get( CoObjectIF subject ) 
		{
			return new Integer( ( (CoLiangLineBreakerIF) subject ).getMinimumPrefixLength() );
		}
		public void set( CoObjectIF subject, Object value )
		{
			( (CoMutableLiangLineBreakerIF) subject ).setMinimumPrefixLength( ( (Integer) value ).intValue() );
		}
	};
	b.addAspectAdaptor( ad );
	numberBuilder.createNumberFieldAdaptor(	ad, (CoTextField) getNamedWidget( MINIMUM_PREFIX_LENGTH ), CoNumberConverter.INTEGER );

	
	ad = new CoGsAspectAdaptor( this, MINIMUM_SUFFIX_LENGTH )
	{
		public Object get( CoObjectIF subject ) 
		{
			return new Integer( ( (CoLiangLineBreakerIF) subject ).getMinimumSuffixLength() );
		}
		public void set( CoObjectIF subject, Object value )
		{
			( (CoMutableLiangLineBreakerIF) subject ).setMinimumSuffixLength( ( (Integer) value ).intValue() );
		}
	};
	b.addAspectAdaptor( ad );
	numberBuilder.createNumberFieldAdaptor(	ad, (CoTextField) getNamedWidget( MINIMUM_SUFFIX_LENGTH ), CoNumberConverter.INTEGER );

	
	ad = new CoGsAspectAdaptor( this, MINIMUM_WORD_LENGTH )
	{
		public Object get( CoObjectIF subject ) 
		{
			return new Integer( ( (CoLiangLineBreakerIF) subject ).getMinimumWordLength() );
		}
		public void set( CoObjectIF subject, Object value )
		{
			( (CoMutableLiangLineBreakerIF) subject ).setMinimumWordLength( ( (Integer) value ).intValue() );
		}
	};
	b.addAspectAdaptor( ad );
	numberBuilder.createNumberFieldAdaptor(	ad, (CoTextField) getNamedWidget( MINIMUM_WORD_LENGTH ), CoNumberConverter.INTEGER );
}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	super.createWidgets( p, b );

	p.setLayout( new CoFormLayout() );

	p.add( b.createLabel( CoTextStringResources.getName( MINIMUM_PREFIX_LENGTH ) ) );
	p.add( b.createTextField( CoTextField.RIGHT, 6, MINIMUM_PREFIX_LENGTH ) );

	p.add( b.createLabel( CoTextStringResources.getName( MINIMUM_SUFFIX_LENGTH ) ) );
	p.add( b.createTextField( CoTextField.RIGHT, 6, MINIMUM_SUFFIX_LENGTH ) );

	p.add( b.createLabel( CoTextStringResources.getName( MINIMUM_WORD_LENGTH ) ) );
	p.add( b.createTextField( CoTextField.RIGHT, 6, MINIMUM_WORD_LENGTH ) );
}

public void setDomain( CoObjectIF d )
{
	if
		( ! ( d instanceof CoLiangLineBreakerIF ) )
	{
		d = null;
	}

	super.setDomain( d );
}
}