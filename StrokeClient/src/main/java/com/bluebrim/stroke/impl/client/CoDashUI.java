package com.bluebrim.stroke.impl.client;
import java.awt.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.resource.shared.*;
import com.bluebrim.stroke.shared.*;
import com.bluebrim.swing.client.*;

public class CoDashUI extends CoDomainUserInterface
{
	public final static String SIZES												= "sizes";
	public final static String PREVIEW											= "preview";
	public final static String SEGMENTS											= "segments";
	public final static String CYCLE_UNIT										= "cycle_unit";
	public final static String JOIN_STYLE										= "join_style";
	public final static String JOIN_MITER										= "join_miter";
	public final static String JOIN_ROUND										= "join_round";
	public final static String JOIN_BEVEL										= "join_bevel";
	public final static String CAP_STYLE										= "cap_style";
	public final static String CAP_BUTT											= "cap_butt";
	public final static String CAP_ROUND										= "cap_round";
	public final static String CAP_SQUARE										= "cap_square";
	public final static String CYCLE_LENGTH									= "cycle_length";
	public final static String DASH_CANVAS										= "dash_canvas";
	public final static String DASH_ATTRIBUTES								= "dash_attributes";
	public final static String NUMBER_OF_SEGMENTS							= "number_of_segments";
	public final static String CYCLE_LENGTH_IN_WIDTH						= "cycle_length_in_width";
	public final static String CYCLE_LENGTH_IN_POINTS						= "cycle_length_in_points";


	
	VisibleValue[] m_joins = new VisibleValue[]
	{
		new VisibleValue( CoDashIF.JOIN_MITER, JOIN_MITER, "join_miter.gif" ),
		new VisibleValue( CoDashIF.JOIN_ROUND, JOIN_ROUND, "join_round.gif" ),
		new VisibleValue( CoDashIF.JOIN_BEVEL, JOIN_BEVEL, "join_bevel.gif" ),
	};

	VisibleValue[] m_caps = new VisibleValue[]
	{
		new VisibleValue( CoDashIF.CAP_BUTT, CAP_BUTT, "cap_butt.gif" ),
		new VisibleValue( CoDashIF.CAP_ROUND, CAP_ROUND, "cap_round.gif" ),
		new VisibleValue( CoDashIF.CAP_SQUARE, CAP_SQUARE, "cap_square.gif" ),
	};

	public class VisibleValue implements CoNamed
	{
		private int m_value;
		private String m_nameKey;
		private Icon m_icon;

		public VisibleValue(int value, String nameKey, String iconResource)
		{
			m_value = value;
			m_nameKey = nameKey;
			m_icon = CoResourceLoader.loadIcon(CoDashUI.class , iconResource ); 
		}

		public int getValue()
		{
			return m_value;
		}
		
		public String getName()
		{
			return CoStrokeUIResources.getName( m_nameKey );
		}
		
		public Icon getIcon()
		{
			return m_icon;
		}
		
	};

public CoDashUI()
{
	super();
}

public CoDashUI(CoDashIF aDomainObject)
{
	super(aDomainObject);
}

protected CoUserInterfaceBuilder createUserInterfaceBuilder()
{
    return new CoNumberUserInterfaceBuilder(this);
}

private CoComboBox createComboBox(Object key) 
{
	CoComboBox combo = getUIBuilder().createComboBox(key);
	
	combo.setRenderer(new CoListCellRenderer() 
	{
		public void setValue(Object value, int index, boolean isSelected, boolean cellHasFocus) 
		{
			if
				(value instanceof VisibleValue) 
			{
				VisibleValue visValue = (VisibleValue)value;
				setText(visValue.getName());
				setIcon(visValue.getIcon());
			} else {
				setText("");
				setIcon(null);
			}
		}
	} );
	combo.setBackground(Color.white);
	return combo;
}
protected void createValueModels(CoUserInterfaceBuilder b) {
	
	super.createValueModels( b );
	CoNumberUserInterfaceBuilder numberBuilder = (CoNumberUserInterfaceBuilder)b;

/*
	// Segments ===========================================================
	b.createNumberFieldAdaptor(
		b.addAspectAdaptor( 
			new CoReadOnlyAspectAdaptor(this, NUMBER_OF_SEGMENTS)
			{
				protected Object get(CoObjectIF subject) 
				{
					float[] dash = ((CoDashIF) subject).getDash();
					return new Float( dash != null ? dash.length : 0f);
				}
			}
		),
		(CoTextField) getNamedWidget( NUMBER_OF_SEGMENTS ),
		CoNumberConverter.FLOAT 
	);
*/
	// Sizes =============================================================
	b.createTextFieldAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor(this, SIZES)
			{
				protected Object get(CoObjectIF subject)
				{
					return makeSizeString((CoDashIF)subject);
				}
				public void set(CoObjectIF subject, Object newValue) 
				{
					parseSizeString( (CoDashIF) subject, (String) newValue );
				}
			}
		),
		(CoTextField )getNamedWidget(SIZES)
	);	

	// Cycle Length===============================================================
	numberBuilder.createNumberFieldAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor(this, CYCLE_LENGTH)
			{
				protected Object get(CoObjectIF subject) 
				{
					return new Float(((CoDashIF) subject).getCycleLength());
				}
				public void set(CoObjectIF subject, Object newValue) 
				{
					((CoDashIF) subject).setCycleLength((newValue != null) ? ((Number)newValue).floatValue() : 0f);
				}
			}
		),
		(CoTextField )getNamedWidget(CYCLE_LENGTH),
		CoNumberConverter.FLOAT
	);	
	
	// Cycle unit ===============================================================
	b.createComboBoxAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor(this, CYCLE_UNIT) 
			{
				public Object get(CoObjectIF subject)
				{
					return ( (CoDashIF )subject ).isCycleLengthInWidth() ? CoStrokeUIResources.getName(CYCLE_LENGTH_IN_WIDTH) : CoStrokeUIResources.getName(CYCLE_LENGTH_IN_POINTS);
				}
				public void set(CoObjectIF subject, Object value)
				{
					( (CoDashIF )subject ).setCycleLengthInWidth( ((String )value).equals(CoStrokeUIResources.getName(CYCLE_LENGTH_IN_WIDTH)) );
				}
			}
		),
		(CoComboBox )getNamedWidget(CYCLE_UNIT)
	);

	// Join style ===============================================================
	b.createComboBoxAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor(this, JOIN_STYLE)
			{
				public Object get(CoObjectIF subject)
				{
					int join = ( (CoDashIF )subject ).getJoin();
					
					for
						(int i = 0; i < m_joins.length; i++)
					{
						if (m_joins[i].getValue() == join) return m_joins[i];
					}
					return null;
				}
				public void set(CoObjectIF subject, Object value)
				{
					VisibleValue visValue = (VisibleValue)value;
					( (CoDashIF )subject ).setJoin(visValue.getValue());
				}
			}
		),
		(CoComboBox )getNamedWidget(JOIN_STYLE)
	);

	// Endcap style ===============================================================
	b.createComboBoxAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor(this, CAP_STYLE)
			{
				public Object get(CoObjectIF subject)
				{
					int cap = ( (CoDashIF )subject ).getCap();
					
					for
						(int i = 0; i < m_joins.length; i++)
					{
						if (m_caps[i].getValue() == cap) return m_caps[i];
					}
					return null;
				}
				public void set(CoObjectIF subject, Object value)
				{
					VisibleValue visValue = (VisibleValue)value;
					( (CoDashIF )subject ).setCap(visValue.getValue());
				}
			}
		),
		(CoComboBox )getNamedWidget(CAP_STYLE)
	);

}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	super.createWidgets( p, b );
	
	p.setLayout( new CoColumnLayout( true ) );
	
	// Segments ----------------------------------------------------------------------
	{
//		CoPanel segPanel = b.createPanel( new CoGridLayout(2,CoGridLayout.LEFT,CoGridLayout.CENTER, 10, 2));
		CoPanel segPanel = b.createPanel( new CoFormLayout() );
		segPanel.setBorder( new TitledBorder(CoStrokeUIResources.getName(SEGMENTS)) );
//		segPanel.add( b.createLabel(CoContentUIResources.getName(NUMBER_OF_SEGMENTS)));
//		segPanel.add( b.createTextField(SwingConstants.RIGHT, 5, NUMBER_OF_SEGMENTS));
		segPanel.add( b.createLabel(CoStrokeUIResources.getName(SIZES)));
		segPanel.add( b.createTextField(SwingConstants.LEFT, 20, SIZES));

		p.add(segPanel);
	}
		
	// Dash Attributes --------------------------------------------------------------
	{
//		CoPanel attrPanel = b.createPanel( new CoGridLayout( 2, CoGridLayout.LEFT, CoGridLayout.CENTER, 10, 2 ) );
		CoPanel attrPanel = b.createPanel( new CoFormLayout() );
		attrPanel.setBorder( new TitledBorder( CoStrokeUIResources.getName( DASH_ATTRIBUTES ) ) );
		attrPanel.add( b.createLabel( CoStrokeUIResources.getName( CYCLE_LENGTH ) ) );

		{
			CoPanel cyclePanel = b.createPanel( new BorderLayout( 0, 0 ) );
			cyclePanel.add( b.createTextField( SwingConstants.RIGHT, 5, CYCLE_LENGTH ), BorderLayout.WEST );
			CoComboBox unitCombo = b.createComboBox( CYCLE_UNIT );
			unitCombo.setBackground( Color.white );		
			unitCombo.addItem( CoStrokeUIResources.getName( CYCLE_LENGTH_IN_WIDTH ) );
			unitCombo.addItem( CoStrokeUIResources.getName( CYCLE_LENGTH_IN_POINTS ) );	
			cyclePanel.add( unitCombo, BorderLayout.CENTER );

			attrPanel.add( cyclePanel );
		}

		{
			attrPanel.add( b.createLabel(CoStrokeUIResources.getName(JOIN_STYLE)));
			CoComboBox miterCombo = createComboBox(JOIN_STYLE);
			for
				(int i = 0; i < m_joins.length; i++)
			{
				miterCombo.addItem(m_joins[i]);
			}
			attrPanel.add(miterCombo);
		}

		{
			attrPanel.add( b.createLabel(CoStrokeUIResources.getName(CAP_STYLE)));
			CoComboBox endcapCombo = createComboBox(CAP_STYLE);
			for
				(int i = 0; i < m_caps.length; i++)
			{
				endcapCombo.addItem(m_caps[i]);
			}
			attrPanel.add(endcapCombo);		
		}
		
		p.add(attrPanel);
	}
		
	/*
	// Preview ------------------------------------------------------------------------
	{
		CoPanel previewPanel = b.createPanel( new CoGridLayout(2,CoGridLayout.LEFT,CoGridLayout.CENTER, 10, 2));
		previewPanel.setBorder(new TitledBorder(CoContentUIResources.getName(PREVIEW)));
		m_preview = new CoDashRenderer()
		{
			{
				setLineWidth(10f);
			}
			public CoDashIF getDash()
			{
				return (CoDashIF)getDomain();
			}
			protected Shape getShape(float w, float h)
			{
				GeneralPath path = new GeneralPath();
				path.moveTo(0, h/2);
				path.lineTo(w/2, h/2);
				path.lineTo(3*w/4, 0);
				path.lineTo(w, h);			
				return path;
			}
		};
		m_preview.setBackground(CoUIConstants.VERY_LIGHT_GRAY);
		previewPanel.add(m_preview);

		p.add(previewPanel);
	}
	*/
}
protected Insets getDefaultPanelInsets()
{
	return null;
}
private String makeSizeString(CoDashIF d) {

	float[] dash = d.getDash();

	if
		( dash == null )
	{
		return "";
	}
	
	StringBuffer buf = new StringBuffer();
	NumberFormat percentFormatter = new DecimalFormat( "##0.0%" );
	
	for
		( int i = 0; i < dash.length; i++ )
	{
		buf.append( percentFormatter.format( dash[ i ] ) );
		if ( i != dash.length - 1 ) buf.append( "; " );
	}
	return buf.toString();
}
private void parseSizeString(CoDashIF subject, String str)
{
	Format format = NumberFormat.getInstance( Locale.getDefault() );
	
	Vector v = new Vector();
	float F = 0;
	
	StringTokenizer tokens = new StringTokenizer( str, ":; \t\n" );
	while
		( tokens.hasMoreTokens() )
	{
		float f;
		try
		{
			String s = tokens.nextToken();
			Object o = format.parseObject( s );
			f = ( (Number) o ).floatValue();
			if
				( f > 1 )
			{
				f = f / 100f;
				v.addElement( new Float( f ) );
			} else {
				v.addElement( o );
			}
		}
		catch ( Exception ex )
		{
			continue;
		}
		F += f;
	}

	int I = v.size();

	if
		( I == 0 )
	{
		subject.setDash( null );
		return;
	}
	
	if
		( I % 2 == 1 )
	{
		I++;
		if
			( F < 1 )
		{
			v.addElement( new Float( 1 - F ) );
			F = 1;
		} else {
			v.addElement( new Float( 0 ) );
		}
	}

	float[] dash = new float [ I ];
	for
		( int i = 0; i < I; i++ )
	{
		dash[ i ] = ( (Number) v.elementAt( i ) ).floatValue() / F;
	}
	
	subject.setDash( dash );
}
protected void postDomainChange(CoObjectIF aDomain){

	super.postDomainChange(aDomain);

//	if ( aDomain != null ) m_preview.repaint();
}
public void setEnabled(boolean enable)
{
	super.setEnabled(enable);
	
	boolean e = enable && canBeEnabled();

//	getNamedValueModel(NUMBER_OF_SEGMENTS).setEnabled(e);
	getNamedValueModel(SIZES).setEnabled(e);		
}
public void valueHasChanged()
{
	super.valueHasChanged();
	
//	m_preview.repaint();
}
}