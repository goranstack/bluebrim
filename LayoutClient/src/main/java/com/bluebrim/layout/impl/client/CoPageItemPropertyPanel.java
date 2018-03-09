package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.formula.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.transact.shared.*;

/**
 * Abstract superclass for page item property panels.
 *
 * @author: Dennis
 */

public abstract class CoPageItemPropertyPanel extends CoPanel
{
	public final static String COLOR = "CoPageItemPanel.COLOR";
	public final static String SHADE = "CoPageItemPanel.SHADE";
	public final static String OPEN_ADM_UI = "CoPageItemPanel.OPEN";

	protected CoShapePageItemView m_domain; // model

	protected boolean m_updateInProgress = false;

	protected static final CoFormula m_formula = new CoFormula();
	protected static final NumberFormat m_numberFormatter = NumberFormat.getInstance( Locale.getDefault() );


;


	// adm ui stuff
	protected abstract static class AdmUI
	{
		protected CoDomainUserInterface m_ui = null;

		public final void open( CoShapePageItemView v )
		{
			CoObjectIF d = getDomain( v );
			if
				( d != null )
			{
				if ( m_ui == null ) m_ui = create();
				m_ui.setDomain( d );
				prepare( v );
				m_ui.openInWindow();
			}
		}

		protected void prepare( CoShapePageItemView v ) {}
		protected abstract CoDomainUserInterface create();
		protected abstract CoObjectIF getDomain( CoShapePageItemView domain );
	};
	
	
	protected static Map m_admUIMap = new HashMap();
	static 
	{
		/*
		AdmUI tmp = 
			new AdmUI()
			{
				protected CoDomainUserInterface create()
				{
					return new com.bluebrim.image.impl.client.CoImageContentUI();
				}
				protected CoObjectIF getDomain( CoShapePageItemView domain )
				{
					return ( (CoPageItemImageContentIF) ( (CoContentWrapperPageItemView) domain ).getContentView().getPageItem() ).getCoImage();
				}
			};
			          
		m_admUIMap.put( CoPageItemImageContentView.class, tmp );
		*/
/*
		m_admUIMap.put( CoLayoutAreaView.class,
			              new AdmUI()
			              {
				              protected CoDomainUserInterface create() { return new se.corren.calvin.editorial.userinterface.CoWorkPieceUI(); }
				              protected CoObjectIF getDomain( CoShapePageItemView domain ) { return ( (CoLayoutAreaIF) m_domain.getPageItem() ).getWorkPiece(); }
				            }
			            );
			            */
	}





protected CoFormLayout createFormLayout()
{
	return createFormLayout( true );
}
protected CoFormLayout createFormLayout( boolean sticky )
{
	return new CoFormLayout( 5, 0, sticky );
}
protected ActionListener createOpenAdmUIAction()
{
	return
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				openAdmUI();
			}
		};
}


public final void domainHasChanged()
{
	if ( m_domain == null ) return;
	if ( getParent() == null ) return;

	m_updateInProgress = true;

	doUpdate();
	
	m_updateInProgress = false;
}
protected abstract void doUpdate();
public final CoShapePageItemView getDomain()
{
	return m_domain;
}
protected void openAdmUI()
{
	AdmUI aui = null;
	
	if
		( m_domain instanceof CoContentWrapperPageItemView )
	{
		aui = (AdmUI) m_admUIMap.get( ( (CoContentWrapperPageItemView) m_domain ).getContentView().getClass() );
	} else {
		aui = (AdmUI) m_admUIMap.get( m_domain.getClass() );
	}

	if
		( aui != null )
	{
		aui.open( m_domain );
	}
}
public void postSetDomain()
{
}
public void setContext( CoPageItemEditorContextIF context )
{
}
public void setDomain( CoShapePageItemView domain )
{
	m_domain = domain;
	
	postSetDomain();
	
	domainHasChanged();
}

	protected class BooleanButtonCommandAdapter extends CommandAdapter
	{
		boolean m_value;
		
		public BooleanButtonCommandAdapter( CoUndoableCommandExecutor commandExecutor, CoShapePageItemSetBooleanCommand command )
		{
			super( commandExecutor, command );
		}

		protected boolean isNewValue( ActionEvent ev )
		{
			m_value = ( (AbstractButton) ev.getSource() ).isSelected();
			return m_value != getCurrentValue();
		}

		protected boolean getCurrentValue()
		{
			return ( (CoShapePageItemSetBooleanCommand) m_command ).getBoolean( m_domain );
		}
		
		protected void prepare()
		{
			( (CoShapePageItemSetBooleanCommand) m_command ).prepare( m_domain, m_value );
		}
	}

	protected class CommandAdapter extends CoCommandAdapter
	{
		public CommandAdapter( CoUndoableCommandExecutor commandExecutor, CoUndoableCommand command )
		{
			super( commandExecutor, command );
		}
		
		protected boolean isUpdateInProgress( ActionEvent ev ) { return m_updateInProgress; }
		protected boolean isCorrectDomain( ActionEvent ev ) { return m_domain != null; }
		protected void handleInvalidValue( ActionEvent ev ) { doUpdate(); }
		
		protected void prepare()
		{
			( (CoShapePageItemCommand) m_command ).prepare( m_domain );
		}
	}

	protected class DoubleTextFieldCommandAdapter extends CommandAdapter
	{
		double m_value;
		double m_scale;
		double m_offset;
		boolean m_useLengthUnits;
		
		public DoubleTextFieldCommandAdapter( CoUndoableCommandExecutor commandExecutor, CoShapePageItemSetDoubleCommand command )
		{
			this( commandExecutor, command, true, 1, 0 );
		}
		
		public DoubleTextFieldCommandAdapter( CoUndoableCommandExecutor commandExecutor, CoShapePageItemSetDoubleCommand command, boolean useLengthUnits, double scale, double offset )
		{
			super( commandExecutor, command );

			m_useLengthUnits = useLengthUnits;
			m_scale = scale;
			m_offset = offset;
		}
		
		protected boolean isValueValid( ActionEvent ev ) { return ! Double.isNaN( m_value ); }

		protected boolean isNewValue( ActionEvent ev )
		{
			m_value = parse( ( (CoTextField) ev.getSource() ).getText() );
			return isNotEqual( m_value, getCurrentValue() );
		}

		protected double getCurrentValue()
		{
			return ( (CoShapePageItemSetDoubleCommand) m_command ).getDouble( m_domain );
		}
		
		protected double parse( String str )
		{
			return m_offset + m_scale * ( m_useLengthUnits ? CoLengthUnitSet.parse( str, Double.NaN, CoLengthUnit.LENGTH_UNITS ) : CoLengthUnitSet.parse( str, Double.NaN ) );
		}
		
		protected void prepare()
		{
			( (CoShapePageItemSetDoubleCommand) m_command ).prepare( m_domain, m_value );
		}
	}

	protected class IntegerTextFieldCommandAdapter extends CommandAdapter
	{
		int m_value;
		int m_invalidValue;
		
		public IntegerTextFieldCommandAdapter( CoUndoableCommandExecutor commandExecutor, CoShapePageItemSetIntegerCommand command, int invalidValue )
		{
			super( commandExecutor, command );
			m_invalidValue = invalidValue;
		}
		
		protected boolean isValueValid( ActionEvent ev ) { return m_value != m_invalidValue; }

		protected boolean isNewValue( ActionEvent ev )
		{
			m_value = parse( ( (CoTextField) ev.getSource() ).getText() );
			return m_value != getCurrentValue();
		}

		protected int getCurrentValue()
		{
			return ( (CoShapePageItemSetIntegerCommand) m_command ).getInteger( m_domain );
		}
		
		protected int parse( String str )
		{
			return CoLengthUnitSet.parse( str, m_invalidValue );
		}
		
		protected void prepare()
		{
			( (CoShapePageItemSetIntegerCommand) m_command ).prepare( m_domain, m_value );
		}
	}

	protected class OptionMenuCommandAdapter extends CommandAdapter
	{
		Object m_value;
		Object m_invalidValue;
		
		public OptionMenuCommandAdapter( CoUndoableCommandExecutor commandExecutor, CoShapePageItemCommand command, Object invalidValue )
		{
			super( commandExecutor, command );
			m_invalidValue = invalidValue;
		}
		
		protected boolean isValueValid( ActionEvent ev ) { return m_value != m_invalidValue; }

		protected boolean isNewValue( ActionEvent ev )
		{
			m_value = ( (CoOptionMenu) ev.getSource() ).getSelectedItem();
			return ! m_value.equals( getCurrentValue() );
		}

		protected Object getCurrentValue()
		{
			return ( (CoShapePageItemSetObjectCommand) m_command ).getObject( m_domain );
		}
		
		protected void prepare()
		{
			( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, m_value );
		}
	}

	protected class TextFieldCommandAdapter extends CommandAdapter
	{
		String m_value;
		
		public TextFieldCommandAdapter( CoUndoableCommandExecutor commandExecutor, CoShapePageItemSetObjectCommand command )
		{
			super( commandExecutor, command );
		}
		
		protected boolean isValueValid( ActionEvent ev ) { return m_value != null; }

		protected boolean isNewValue( ActionEvent ev )
		{
			m_value = parse( ( (CoTextField) ev.getSource() ).getText() );
			return ! m_value.equals( getCurrentValue() );
		}

		protected String getCurrentValue()
		{
			return (String) ( (CoShapePageItemSetObjectCommand) m_command ).getObject( m_domain );
		}
		
		protected String parse( String str )
		{
			return str;
		}
		
		protected void prepare()
		{
			( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, m_value );
		}
	}

	protected class ToggleButtonCommandAdapter extends CommandAdapter
	{
		String m_value;
		
		public ToggleButtonCommandAdapter( CoUndoableCommandExecutor commandExecutor, CoShapePageItemSetObjectCommand command )
		{
			super( commandExecutor, command );
		}

		protected boolean isNewValue( ActionEvent ev )
		{
			m_value = ( (JToggleButton) ev.getSource() ).getActionCommand();
			return ! m_value.equals( getCurrentValue() );
		}

		protected String getCurrentValue()
		{
			return ( (CoShapePageItemSetObjectCommand) m_command ).getObject( m_domain ).toString();
		}
		
		protected void prepare()
		{
			( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, m_value );
		}
	}

public CoPageItemPropertyPanel( CoUserInterfaceBuilder b, LayoutManager l, CoUndoableCommandExecutor commandExecutor )
{
	super( l );
	
	b.preparePanel( this );

	create( b, commandExecutor );
}

protected abstract void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor );
}