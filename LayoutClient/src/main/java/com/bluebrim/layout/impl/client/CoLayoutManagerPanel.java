package com.bluebrim.layout.impl.client;

import java.awt.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.transact.shared.*;

/**
 * Abstract superclass for layout manager property panels.
 *
 * @author: Dennis
 */
 
public abstract class CoLayoutManagerPanel extends CoPanel
{
	protected boolean m_updateInProgress = false;
	
	protected CoPageItemLayoutManagerPanel m_domainHolder;

	
;

	protected class ButtonCommandAdapter extends CommandAdapter
	{
		String m_value;
		
		public ButtonCommandAdapter( CoUndoableCommandExecutor commandExecutor, CoShapePageItemSetObjectCommand command )
		{
			super( commandExecutor, command );
		}

		protected boolean isNewValue( ActionEvent ev )
		{
			m_value = ev.getActionCommand();
			return false;
		}
		
		protected void prepare()
		{
			( (CoShapePageItemSetObjectCommand) m_command ).prepare( getDomain(), m_value );
		}
	}

	/*
	protected abstract class Command extends CoEnhancedCommand
	{
		public Command( String name )
		{
			super( name );
		}

		protected boolean isUpdateInProgress() { return m_updateInProgress; }
		protected boolean isCorrectDomain() { return getDomain() != null; }
		protected boolean isNewValue() { return true; }
		protected void handleInvalidValue() { doUpdate(); }
	};
*/

	
	protected class CommandAdapter extends CoCommandAdapter
	{
		public CommandAdapter( CoUndoableCommandExecutor commandExecutor, CoUndoableCommand command )
		{
			super( commandExecutor, command );
		}
		
		protected boolean isUpdateInProgress( ActionEvent ev ) { return m_updateInProgress; }
		protected boolean isCorrectDomain( ActionEvent ev ) { return getDomain() != null; }
		protected void handleInvalidValue( ActionEvent ev ) { doUpdate(); }
		
		protected void prepare()
		{
			( (CoShapePageItemCommand) m_command ).prepare( getDomain() );
		}
	}

	protected class DoubleTextFieldCommandAdapter extends CommandAdapter
	{
		double m_value;
		boolean m_useLengthUnits;
		
		public DoubleTextFieldCommandAdapter( CoUndoableCommandExecutor commandExecutor, CoShapePageItemSetDoubleCommand command )
		{
			this( commandExecutor, command, true );
		}
		
		public DoubleTextFieldCommandAdapter( CoUndoableCommandExecutor commandExecutor, CoShapePageItemSetDoubleCommand command, boolean useLengthUnits )
		{
			super( commandExecutor, command );

			m_useLengthUnits = useLengthUnits;
		}
		
		protected boolean isValueValid( ActionEvent ev ) { return ! Double.isNaN( m_value ); }

		protected boolean isNewValue( ActionEvent ev )
		{
			m_value = parse( ( (CoTextField) ev.getSource() ).getText() );
			return isNotEqual( m_value, getCurrentValue() );
		}

		protected double getCurrentValue()
		{
			return ( (CoShapePageItemSetDoubleCommand) m_command ).getDouble( getDomain() );
		}
		
		protected double parse( String str )
		{
			return m_useLengthUnits ? CoLengthUnitSet.parse( str, Double.NaN, CoLengthUnit.LENGTH_UNITS ) : CoLengthUnitSet.parse( str, Double.NaN );
		}
		
		protected void prepare()
		{
			( (CoShapePageItemSetDoubleCommand) m_command ).prepare( getDomain(), m_value );
		}
	}


	
;

public CoLayoutManagerPanel( CoUserInterfaceBuilder b, CoPageItemLayoutManagerPanel domainHolder )
{
	super();

	m_domainHolder = domainHolder;
	
	b.preparePanel( this );
}
public final void domainHasChanged()
{
	if ( getDomain() == null ) return;
	if ( getParent() == null ) return;

	m_updateInProgress = true;

	doUpdate();
	
	m_updateInProgress = false;
}
protected abstract void doUpdate();

public void postSetDomain()
{
}

protected CoCompositePageItemView getDomain()
{
	return (CoCompositePageItemView) m_domainHolder.getDomain();
}
}