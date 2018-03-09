package com.bluebrim.text.impl.shared.xtg;

import java.io.*;
import java.util.*;

import javax.swing.text.*;

import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Xtg attribute parse tree node
 * 
 * @author: Dennis
 */
 
public class CoXtgAttributeParseNode extends CoXtgParseNode
{
	private CoXtgNameParseNode m_nameNode;
	private CoXtgQstringParseNode m_qstringNode;
	private CoXtgNumberParseNode m_numberNode;
	private CoXtgParamsParseNode m_paramsNode;
	private boolean m_isDollar;
	
	private static String m_black;
	private static String m_white;
	private static String m_cyan;
	private static String m_yellow;
	private static String m_magenta;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgAttributeParseNode() {
	super();
}
void collect( StringBuffer s )
{
	s.append( m_nameNode.getName() );

	if      ( m_qstringNode != null ) s.append( "\"" + m_qstringNode.getQstring() + "\"" );
	else if ( m_numberNode != null ) s.append( m_numberNode.getString() );
	else if 
		( m_paramsNode != null )
	{
		s.append( "(" );
		m_paramsNode.collect( s );
		s.append( ")" );
	} else if
		( m_isDollar )
	{
		s.append( "$" );
	}
}
public void createXtg( PrintStream s )
{
	s.print( m_nameNode.getName() );

	if      ( m_qstringNode != null ) s.print( "\"" + m_qstringNode.getQstring() + "\"" );
	else if ( m_numberNode != null ) s.print( m_numberNode.getString() );
	else if 
		( m_paramsNode != null )
	{
		s.print( "(" );
		m_paramsNode.createXtg( s );
		s.print( ")" );
	} else if
		( m_isDollar )
	{
		s.print( "$" );
	}

}
public void dump( String indent )
{
	System.err.print( indent + " " + m_nameNode.getName() );
	if ( m_qstringNode != null ) System.err.print( " \"" + m_qstringNode.getQstring() + "\"" );
	if ( m_numberNode != null ) System.err.print( " " + m_numberNode.getNumber() );
	if
		( m_paramsNode != null )
	{
		System.err.print( "( " );
		m_paramsNode.dump();
		System.err.print( " )" );
	} else if
		( m_isDollar )
	{
		System.err.print( "$" );
	}

	System.err.println();
}
void extract( MutableAttributeSet a, CoXtgLogger l )
{
	String name = m_nameNode.getName();

	if
		( name.equals( "P" ) )
	{
		CoStyleConstants.setStrikeThru( a, Boolean.FALSE );
//		CoStyleConstants.setOutline( a, Boolean.FALSE );
		CoStyleConstants.setShadow( a, Boolean.FALSE );
		CoStyleConstants.setAllCaps( a, Boolean.FALSE );
		CoStyleConstants.setVerticalPosition( a, CoTextConstants.UNDERLINE_NONE );
		CoStyleConstants.setSuperior( a, Boolean.FALSE );
		CoStyleConstants.setWeight( a, com.bluebrim.font.shared.CoFontAttribute.NORMAL_WEIGHT );
		CoStyleConstants.setStyle( a, com.bluebrim.font.shared.CoFontAttribute.NORMAL_STYLE );
//		CoStyleConstants.setStretch( a, com.bluebrim.font.shared.CoFontAttribute.NORMAL_STRETCH );
//		CoStyleConstants.setVariant( a, com.bluebrim.font.shared.CoFontAttribute.NORMAL_VARIANT );
		CoStyleConstants.setUnderline( a, CoTextConstants.UNDERLINE_NONE );
	} else if
		( name.equals( "B" ) )
	{
		com.bluebrim.font.shared.CoFontAttribute w = CoStyleConstants.getWeight( a );
		w = ( ( w == null ) || ( w.getValue() == com.bluebrim.font.shared.CoFontConstants.NORMAL_WEIGHT ) ) ? com.bluebrim.font.shared.CoFontAttribute.BOLD : null;
		CoStyleConstants.setWeight( a, w );
	} else if
		( name.equals( "I" ) )
	{
		com.bluebrim.font.shared.CoFontAttribute w = CoStyleConstants.getStyle( a );
		w = ( ( w == null ) || ( w.getValue() == com.bluebrim.font.shared.CoFontConstants.NORMAL_STYLE ) ) ? com.bluebrim.font.shared.CoFontAttribute.ITALIC : null;
		CoStyleConstants.setStyle( a, w );
		/*
	} else if
		( name.equals( "O" ) )
	{
		Boolean b = CoStyleConstants.getOutline( a );
		b = ( ( b == null ) || ! b.booleanValue() ) ? Boolean.TRUE : null;
		CoStyleConstants.setOutline( a, b );
		*/
	} else if
		( name.equals( "S" ) )
	{
		Boolean b = CoStyleConstants.getShadow( a );
		b = ( ( b == null ) || ! b.booleanValue() ) ? Boolean.TRUE : null;
		CoStyleConstants.setShadow( a, b );
	} else if
		( name.equals( "U" ) )
	{
		CoEnumValue u = CoStyleConstants.getUnderline( a );
		u = ( ( u != null ) && u.equals( CoTextConstants.UNDERLINE_NORMAL ) ) ? null : CoTextConstants.UNDERLINE_NORMAL;
		CoStyleConstants.setUnderline( a, u );
	} else if
		( name.equals( "W" ) )
	{
		CoEnumValue u = CoStyleConstants.getUnderline( a );
		u = ( ( u != null ) && u.equals( CoTextConstants.UNDERLINE_WORD ) ) ? null : CoTextConstants.UNDERLINE_WORD;
		CoStyleConstants.setUnderline( a, u );
	} else if
		( name.equals( "/" ) )
	{
		Boolean b = CoStyleConstants.getStrikeThru( a );
		b = ( ( b == null ) || ! b.booleanValue() ) ? Boolean.TRUE : null;
		CoStyleConstants.setStrikeThru( a, b );
	} else if
		( name.equals( "K" ) )
	{
		Boolean b = CoStyleConstants.getAllCaps( a );
		b = ( ( b == null ) || ! b.booleanValue() ) ? Boolean.TRUE : null;
		CoStyleConstants.setAllCaps( a, b );
		/*
	} else if
		( name.equals( "H" ) )
	{
		com.bluebrim.font.shared.CoFontAttribute w = CoStyleConstants.getVariant( a );
		w = ( ( w == null ) || ( w.getValue() == com.bluebrim.font.shared.CoFontConstants.NORMAL_VARIANT ) ) ? com.bluebrim.font.shared.CoFontAttribute.SMALL_CAPS : null;
		CoStyleConstants.setVariant( a, w );
		*/
	} else if
		( name.equals( "+" ) )
	{
		CoEnumValue v = CoStyleConstants.getVerticalPosition( a );
		v = ( ( v != null ) && v.equals( CoTextConstants.VERTICAL_POSITION_SUPERSCRIPT ) ) ? null : CoTextConstants.VERTICAL_POSITION_SUPERSCRIPT;
		CoStyleConstants.setUnderline( a, v );
	} else if
		( name.equals( "-" ) )
	{
		CoEnumValue v = CoStyleConstants.getVerticalPosition( a );
		v = ( ( v != null ) && v.equals( CoTextConstants.VERTICAL_POSITION_SUBSCRIPT ) ) ? null : CoTextConstants.VERTICAL_POSITION_SUBSCRIPT;
		CoStyleConstants.setUnderline( a, v );
	} else if
		( name.equals( "V" ) )
	{
		Boolean b = CoStyleConstants.getSuperior( a );
		b = ( ( b == null ) || ! b.booleanValue() ) ? Boolean.TRUE : null;
		CoStyleConstants.setSuperior( a, b );
	} else if
		( name.equals( "$" ) )
	{
		l.log( "Generator warning: tag \"" + name + "\" ignored." );
	} else if
		( name.equals( "f" ) )
	{
		CoStyleConstants.setFontFamily( a, m_isDollar ? null : m_qstringNode.getQstring() );
	} else if
		( name.equals( "z" ) )
	{
		CoStyleConstants.setFontSize( a, m_isDollar ? null : new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "c" ) )
	{
		CoStyleConstants.setForegroundColor( a, m_isDollar ? null : m_qstringNode.getQstring() );
	} else if
		( name.equals( "cK" ) )
	{
		CoStyleConstants.setForegroundColor( a, getBlack() );
	} else if
		( name.equals( "cW" ) )
	{
		CoStyleConstants.setForegroundColor( a, getWhite() );
	} else if
		( name.equals( "cC" ) )
	{
		CoStyleConstants.setForegroundColor( a, getCyan() );
	} else if
		( name.equals( "cM" ) )
	{
		CoStyleConstants.setForegroundColor( a, getMagenta() );
	} else if
		( name.equals( "cY" ) )
	{
		CoStyleConstants.setForegroundColor( a, getYellow() );
	} else if
		( name.equals( "s" ) )
	{
		CoStyleConstants.setForegroundShade( a, m_isDollar ? null : new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "h" ) )
	{
		CoStyleConstants.setHorizontalScale( a, m_isDollar ? null : new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "k" ) )
	{
		CoStyleConstants.setTrackAmount( a, m_isDollar ? null : new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "t" ) )
	{
		CoStyleConstants.setTrackAmount( a, m_isDollar ? null : new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "b" ) )
	{
		CoStyleConstants.setBaselineOffset( a, m_isDollar ? null : new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "y" ) )
	{
		CoStyleConstants.setVerticalScale( a, m_isDollar ? null : new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "*L" ) )
	{
		CoStyleConstants.setAlignment( a, CoTextConstants.ALIGN_LEFT );
	} else if
		( name.equals( "*C" ) )
	{
		CoStyleConstants.setAlignment( a, CoTextConstants.ALIGN_CENTER );
	} else if
		( name.equals( "*R" ) )
	{
		CoStyleConstants.setAlignment( a, CoTextConstants.ALIGN_RIGHT );
	} else if
		( name.equals( "*J" ) )
	{
		CoStyleConstants.setAlignment( a, CoTextConstants.ALIGN_JUSTIFIED );
	} else if
		( name.equals( "*F" ) )
	{
		CoStyleConstants.setAlignment( a, CoTextConstants.ALIGN_FORCED );
	} else if
		( name.equals( "*t" ) )
	{
		l.log( "Generator warning: tag \"*t\" (tabstops) ignored." );
	} else if
		( name.equals( "*p" ) )
	{
		extractP( a, l );
	} else if
		( name.equals( "*h" ) )
	{
		l.log( "Generator warning: tag \"*h\" (H&J) ignored." );
	} else if
		( name.equals( "*ra" ) )
	{
		extractRa( a, l );
	} else if
		( name.equals( "*rb" ) )
	{
		extractRb( a, l );
	} else if
		( name.equals( "*d" ) )
	{
		extractD( a, l );
	} else if
		( name.equals( "*kn" ) )
	{
		l.log( "Generator warning: tag \"*kn\" (keep with next) ignored." );
	} else if
		( name.equals( "*kt" ) )
	{
		extractKt( a, l );
	} else {
		l.log( "Generator warning: unknown tag \"" + name + "\" ignored." );
	}
}
void extract( com.bluebrim.text.shared.CoCharacterStyleIF cs, CoXtgLogger l )
{
	String name = m_nameNode.getName();

	if
		( name.equals( "P" ) )
	{
		cs.setStrikeThru( Boolean.FALSE );
//		cs.setOutline( Boolean.FALSE );
		cs.setShadow( Boolean.FALSE );
		cs.setAllCaps( Boolean.FALSE );
		cs.setVerticalPosition( CoTextConstants.UNDERLINE_NONE );
		cs.setSuperior( Boolean.FALSE );
		cs.setWeight( com.bluebrim.font.shared.CoFontAttribute.NORMAL_WEIGHT );
		cs.setStyle( com.bluebrim.font.shared.CoFontAttribute.NORMAL_STYLE );
//		cs.setStretch( com.bluebrim.font.shared.CoFontAttribute.NORMAL_STRETCH );
//		cs.setVariant( com.bluebrim.font.shared.CoFontAttribute.NORMAL_VARIANT );
		cs.setUnderline( CoTextConstants.UNDERLINE_NONE );
	} else if
		( name.equals( "B" ) )
	{
		cs.setWeight( com.bluebrim.font.shared.CoFontAttribute.BOLD );
	} else if
		( name.equals( "I" ) )
	{
		cs.setStyle( com.bluebrim.font.shared.CoFontAttribute.ITALIC );
		/*
	} else if
		( name.equals( "O" ) )
	{
		cs.setOutline( Boolean.TRUE );
		*/
	} else if
		( name.equals( "S" ) )
	{
		cs.setShadow( Boolean.TRUE );
	} else if
		( name.equals( "U" ) )
	{
		cs.setUnderline( CoTextConstants.UNDERLINE_NORMAL );
	} else if
		( name.equals( "W" ) )
	{
		cs.setUnderline( CoTextConstants.UNDERLINE_WORD );
	} else if
		( name.equals( "/" ) )
	{
		cs.setStrikeThru( Boolean.TRUE );
	} else if
		( name.equals( "K" ) )
	{
		cs.setAllCaps( Boolean.TRUE );
		/*
	} else if
		( name.equals( "H" ) )
	{
		cs.setVariant( CoTextConstants.SMALL_CAPS );
		*/
	} else if
		( name.equals( "+" ) )
	{
		cs.setVerticalPosition( CoTextConstants.VERTICAL_POSITION_SUPERSCRIPT );
	} else if
		( name.equals( "-" ) )
	{
		cs.setVerticalPosition( CoTextConstants.VERTICAL_POSITION_SUBSCRIPT );
	} else if
		( name.equals( "V" ) )
	{
		cs.setSuperior( Boolean.TRUE );
	} else if
		( name.equals( "$" ) )
	{
		l.log( "Generator warning: tag \"" + name + "\" ignored." );
	} else if
		( name.equals( "f" ) )
	{
		cs.setFontFamily( m_qstringNode.getQstring() );
	} else if
		( name.equals( "z" ) )
	{
		cs.setFontSize( new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "c" ) )
	{
		cs.setForegroundColor( m_qstringNode.getQstring() );
	} else if
		( name.equals( "cK" ) )
	{
		cs.setForegroundColor( getBlack() );
	} else if
		( name.equals( "cW" ) )
	{
		cs.setForegroundColor( getWhite() );
	} else if
		( name.equals( "cC" ) )
	{
		cs.setForegroundColor( getCyan() );
	} else if
		( name.equals( "cM" ) )
	{
		cs.setForegroundColor( getMagenta() );
	} else if
		( name.equals( "cY" ) )
	{
		cs.setForegroundColor( getYellow() );
	} else if
		( name.equals( "s" ) )
	{
		cs.setForegroundShade( new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "h" ) )
	{
		cs.setHorizontalScale( new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "k" ) )
	{
		cs.setTrackAmount( new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "t" ) )
	{
		cs.setTrackAmount( new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "b" ) )
	{
		cs.setBaselineOffset( new Float( m_numberNode.getNumber() ) );
	} else if
		( name.equals( "y" ) )
	{
		cs.setVerticalScale( new Float( m_numberNode.getNumber() ) );
	} else {
		if      ( name.equals( "*L" ) ) ;
		else if ( name.equals( "*C" ) ) ;
		else if ( name.equals( "*R" ) ) ;
		else if ( name.equals( "*J" ) ) ;
		else if ( name.equals( "*F" ) ) ;
		else if ( name.equals( "*t" ) ) ;
		else if ( name.equals( "*p" ) ) ;
		else if ( name.equals( "*h" ) ) ;
		else if ( name.equals( "*ra" ) ) ;
		else if ( name.equals( "*rb" ) ) ;
		else if ( name.equals( "*d" ) ) ;
		else if ( name.equals( "*kn" ) ) ;
		else if ( name.equals( "*kt" ) ) ;
		else {
			l.log( "Generator warning: unknown tag \"" + name + "\" ignored." );
		}
	}
}
void extract( com.bluebrim.text.shared.CoParagraphStyleIF ps, CoXtgLogger l )
{
	extract( (com.bluebrim.text.shared.CoCharacterStyleIF) ps, l );
	
	String name = m_nameNode.getName();

	if
		( name.equals( "*L" ) )
	{
		ps.setAlignment( CoTextConstants.ALIGN_LEFT );
	} else if
		( name.equals( "*C" ) )
	{
		ps.setAlignment( CoTextConstants.ALIGN_CENTER );
	} else if
		( name.equals( "*R" ) )
	{
		ps.setAlignment( CoTextConstants.ALIGN_RIGHT );
	} else if
		( name.equals( "*J" ) )
	{
		ps.setAlignment( CoTextConstants.ALIGN_JUSTIFIED );
	} else if
		( name.equals( "*F" ) )
	{
		ps.setAlignment( CoTextConstants.ALIGN_FORCED );
	} else if
		( name.equals( "*t" ) )
	{
		l.log( "Generator warning: tag \"*t\" (tabstops) ignored." );
	} else if
		( name.equals( "*p" ) )
	{
		extractP( ps, l );
	} else if
		( name.equals( "*h" ) )
	{
		l.log( "Generator warning: tag \"*h\" (H&J) ignored." );
	} else if
		( name.equals( "*ra" ) )
	{
		extractRa( ps, l );
	} else if
		( name.equals( "*rb" ) )
	{
		extractRb( ps, l );
	} else if
		( name.equals( "*d" ) )
	{
		extractD( ps, l );
	} else if
		( name.equals( "*kn" ) )
	{
		l.log( "Generator warning: tag \"*kn\" (keep with next) ignored." );
	} else if
		( name.equals( "*kt" ) )
	{
		extractKt( ps, l );
	} else {
		if      ( name.equals( "P" ) ) ;
		else if ( name.equals( "B" ) ) ;
		else if ( name.equals( "I" ) ) ;
		else if ( name.equals( "O" ) ) ;
		else if ( name.equals( "S" ) ) ;
		else if ( name.equals( "U" ) ) ;
		else if ( name.equals( "W" ) ) ;
		else if ( name.equals( "/" ) ) ;
		else if ( name.equals( "K" ) ) ;
		else if ( name.equals( "H" ) ) ;
		else if ( name.equals( "+" ) ) ;
		else if ( name.equals( "-" ) ) ;
		else if ( name.equals( "V" ) ) ;
		else if ( name.equals( "$" ) ) ;
		else if ( name.equals( "f" ) ) ;
		else if ( name.equals( "z" ) ) ;
		else if ( name.equals( "c" ) ) ;
		else if ( name.equals( "cK" ) ) ;
		else if ( name.equals( "cW" ) ) ;
		else if ( name.equals( "cC" ) ) ;
		else if ( name.equals( "cM" ) ) ;
		else if ( name.equals( "cY" ) ) ;
		else if ( name.equals( "s" ) ) ;
		else if ( name.equals( "h" ) ) ;
		else if ( name.equals( "k" ) ) ;
		else if ( name.equals( "t" ) ) ;
		else if ( name.equals( "b" ) ) ;
		else if ( name.equals( "y" ) ) ;
		else {
			l.log( "Generator warning: unknown tag \"" + name + "\" ignored." );
		}
	}
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger l )
{
}
private void extractD( MutableAttributeSet a, CoXtgLogger logger )
{
	if
		( m_isDollar )
	{
		CoStyleConstants.setDropCaps( a, null );
		CoStyleConstants.setDropCapsCharacterCount( a, null );
		CoStyleConstants.setDropCapsLineCount( a, null );
		
	} else if
		( m_paramsNode != null )
	{
		List l = m_paramsNode.getParameters();
		if
			( l.size() < 2 )
		{
			logger.log( "Generator warning: to few parameters for tag \"*d\"." );
		} else {
			if
				( l.size() > 2 )
			{
				logger.log( "Generator warning: to many parameters for tag \"*d\", the last " + ( l.size() - 2 ) + " are ignored." );
			}
			try
			{
				float f;
				f = ( (CoXtgParamParseNode) l.get( 0 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setDropCapsCharacterCount( a, new Integer( (int) f ) );
				
				f = ( (CoXtgParamParseNode) l.get( 1 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setDropCapsLineCount( a, new Integer( (int) f ) );
			}
			catch ( ClassCastException ex )
			{
				logger.log( "Generator warning: parameters type clash for tag \"*d\"." );
			}
			
			CoStyleConstants.setDropCaps( a, Boolean.TRUE );
		}
		
	} else if
		( m_numberNode != null )
	{
		if
			( m_numberNode.getNumber() == 0 )
		{
			CoStyleConstants.setDropCaps( a, Boolean.FALSE );
		} else {
			logger.log( "Generator warning: illegal parameter value ( != 0 ) for tag \"*d\"." );
		}
	} else {
		logger.log( "Generator warning: parameters missing for tag \"*d\"." );
	}
}
private void extractD( com.bluebrim.text.shared.CoParagraphStyleIF ps, CoXtgLogger logger )
{
	if
		( m_paramsNode != null )
	{
		List l = m_paramsNode.getParameters();
		if
			( l.size() < 2 )
		{
			logger.log( "Generator warning: to few parameters for tag \"*d\"." );
		} else {
			if
				( l.size() > 2 )
			{
				logger.log( "Generator warning: to many parameters for tag \"*d\", the last " + ( l.size() - 2 ) + " are ignored." );
			}
			try
			{
				float f;
				f = ( (CoXtgParamParseNode) l.get( 0 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setDropCapsCharacterCount( new Integer( (int) f ) );
				
				f = ( (CoXtgParamParseNode) l.get( 1 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setDropCapsLineCount( new Integer( (int) f ) );
			}
			catch ( ClassCastException ex )
			{
				logger.log( "Generator warning: parameters type clash for tag \"*d\"." );
			}
			
			ps.setDropCaps( Boolean.TRUE );
		}
	} else if
		( m_numberNode != null )
	{
		if
			( m_numberNode.getNumber() == 0 )
		{
			ps.setDropCaps( Boolean.FALSE );
		} else {
			logger.log( "Generator warning: illegal parameter value ( != 0 ) for tag \"*d\"." );
		}
	} else {
		logger.log( "Generator warning: parameters missing for tag \"*d\"." );
	}
}
private void extractKt( MutableAttributeSet a, CoXtgLogger logger )
{
	if
		( m_isDollar )
	{
		CoStyleConstants.setKeepLinesTogether( a, null );
		
	} else if
		( m_paramsNode != null )
	{
		List l = m_paramsNode.getParameters();
		if
			( l.size() < 1 )
		{
			logger.log( "Generator warning: to few parameters for tag \"*kt\"." );
		} else {
			if
				( l.size() > 2 )
			{
				logger.log( "Generator warning: to many parameters for tag \"*kt\", the last " + ( l.size() - 2 ) + " are ignored." );
			}
			if
				( l.size() == 2 )
			{
				logger.log( "Generator warning: tag \"*kt(#,#)\" isn't applicable, using \"*kt(A)\" instead." );
			} else {
				try
				{
					if
						( ! ( (CoXtgParamParseNode) l.get( 0 ) ).getName().equals( "A" ) )
					{
						logger.log( "Generator warning: illegal parameter value ( \"" + ( (CoXtgParamParseNode) l.get( 0 ) ).getName() + "\" ) for tag \"*kt\", using \"*kt(A)\" instead." );
					}
				}
				catch ( ClassCastException ex )
				{
					logger.log( "Generator warning: parameters type clash for tag \"*kt\"." );
				}
			}
			
			CoStyleConstants.setKeepLinesTogether( a, Boolean.TRUE );
		}
		
	} else if
		( m_numberNode != null )
	{
		if
			( m_numberNode.getNumber() == 0 )
		{
			CoStyleConstants.setKeepLinesTogether( a, Boolean.FALSE );
		} else {
			logger.log( "Generator warning: illegal parameter value ( != 0 ) for tag \"*kt\"." );
		}
	} else {
		logger.log( "Generator warning: parameters missing for tag \"*kt\"." );
	}
}
private void extractKt( com.bluebrim.text.shared.CoParagraphStyleIF ps, CoXtgLogger logger )
{
	if
		( m_paramsNode != null )
	{
		List l = m_paramsNode.getParameters();
		if
			( l.size() < 1 )
		{
			logger.log( "Generator warning: to few parameters for tag \"*kt\"." );
		} else {
			if
				( l.size() > 2 )
			{
				logger.log( "Generator warning: to many parameters for tag \"*kt\", the last " + ( l.size() - 2 ) + " are ignored." );
			}
			if
				( l.size() == 2 )
			{
				logger.log( "Generator warning: tag \"*kt(#,#)\" isn't applicable, using \"*kt(A)\" instead." );
			} else {
				try
				{
					if
						( ! ( (CoXtgParamParseNode) l.get( 0 ) ).getName().equals( "A" ) )
					{
						logger.log( "Generator warning: illegal parameter value ( \"" + ( (CoXtgParamParseNode) l.get( 0 ) ).getName() + "\" ) for tag \"*kt\", using \"*kt(A)\" instead." );
					}
				}
				catch ( ClassCastException ex )
				{
					logger.log( "Generator warning: parameters type clash for tag \"*kt\"." );
				}
			}
			
			ps.setKeepLinesTogether( Boolean.TRUE );
		}
	} else if
		( m_numberNode != null )
	{
		if
			( m_numberNode.getNumber() == 0 )
		{
			ps.setKeepLinesTogether( Boolean.FALSE );
		} else {
			logger.log( "Generator warning: illegal parameter value ( != 0 ) for tag \"*kt\"." );
		}
	} else {
		logger.log( "Generator warning: parameters missing for tag \"*kt\"." );
	}
}
private void extractP( MutableAttributeSet a, CoXtgLogger logger )
{
	final int pCount = 8;
	
	if
		( m_isDollar )
	{
		CoStyleConstants.setLeftIndent( a, null );
		CoStyleConstants.setFirstLineIndent( a, null );
		CoStyleConstants.setRightIndent( a, null );
		CoStyleConstants.setLeading( a, null );
		CoStyleConstants.setSpaceAbove( a, null );
		CoStyleConstants.setSpaceBelow( a, null );
		CoStyleConstants.setAdjustToBaseLineGrid( a, null );
		
	} else if
		( m_paramsNode != null )
	{
		List l = m_paramsNode.getParameters();
		if
			( l.size() < pCount )
		{
			logger.log( "Generator warning: to few parameters for tag \"*p\"." );
		} else {
			if
				( l.size() > pCount )
			{
				logger.log( "Generator warning: to many parameters for tag \"*p\", the last " + ( l.size() - pCount ) + " are ignored." );
			}
			try
			{
				float f;
				f = ( (CoXtgParamParseNode) l.get( 0 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setLeftIndent( a, new Float( f ) );
				
				f = ( (CoXtgParamParseNode) l.get( 1 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setFirstLineIndent( a, new Float( f ) );
				
				f = ( (CoXtgParamParseNode) l.get( 2 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setRightIndent( a, new Float( f ) );
				
				f = ( (CoXtgParamParseNode) l.get( 3 ) ).getNumber();
				if
					( ! Float.isNaN( f ) )
				{
					if
						( ( f < 0 ) || ( ( (CoXtgParamParseNode) l.get( 3 ) ).isPositiveDelta() ) )
					{
						CoStyleConstants.setLeading( a, new CoLeading( CoLeading.OFFSET, f ) );
					} else {
						if
							( f == 0 )
						{
							CoStyleConstants.setLeading( a, new CoLeading() );
						} else {
							CoStyleConstants.setLeading( a, new CoLeading( CoLeading.ABSOLUTE, f ) );
						}
					}
				}
				
				f = ( (CoXtgParamParseNode) l.get( 4 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setSpaceAbove( a, new Float( f ) );
				
				f = ( (CoXtgParamParseNode) l.get( 5 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setSpaceBelow( a, new Float( f ) );

				String str = ( (CoXtgParamParseNode) l.get( 6 ) ).getName();
				if
					( str.equals( "g" ) )
				{
					CoStyleConstants.setAdjustToBaseLineGrid( a, Boolean.FALSE );
				} else {
					CoStyleConstants.setAdjustToBaseLineGrid( a, Boolean.TRUE );
				}
			}
			catch ( ClassCastException ex )
			{
				logger.log( "Generator warning: parameters type clash for tag \"*p\"." );
			}
		}
	} else {
		logger.log( "Generator warning: parameters missing for tag \"*p\"." );
	}
}
private void extractP( com.bluebrim.text.shared.CoParagraphStyleIF ps, CoXtgLogger logger )
{
	final int pCount = 8;
	
	if
		( m_paramsNode != null )
	{
		List l = m_paramsNode.getParameters();
		if
			( l.size() < pCount )
		{
			logger.log( "Generator warning: to few parameters for tag \"*p\"." );
		} else {
			if
				( l.size() > pCount )
			{
				logger.log( "Generator warning: to many parameters for tag \"*p\", the last " + ( l.size() - pCount ) + " are ignored." );
			}
			try
			{
				float f;
				f = ( (CoXtgParamParseNode) l.get( 0 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setLeftIndent( new Float( f ) );
				
				f = ( (CoXtgParamParseNode) l.get( 1 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setFirstIndent( new Float( f ) );
				
				f = ( (CoXtgParamParseNode) l.get( 2 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setRightIndent( new Float( f ) );
				
				f = ( (CoXtgParamParseNode) l.get( 3 ) ).getNumber();
				if
					( ! Float.isNaN( f ) )
				{
					if
						( ( f < 0 ) || ( ( (CoXtgParamParseNode) l.get( 3 ) ).isPositiveDelta() ) )
					{
						ps.setLeading( new CoLeading( CoLeading.OFFSET, f ) );
					} else {
						if
							( f == 0 )
						{
							ps.setLeading( new CoLeading() );
						} else {
							ps.setLeading( new CoLeading( CoLeading.ABSOLUTE, f ) );
						}
					}
				}
				
				f = ( (CoXtgParamParseNode) l.get( 4 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setSpaceBefore( new Float( f ) );
				
				f = ( (CoXtgParamParseNode) l.get( 5 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setSpaceAfter( new Float( f ) );

				String str = ( (CoXtgParamParseNode) l.get( 6 ) ).getName();
				if
					( str.equals( "g" ) )
				{
					ps.setAdjustToBaseLineGrid( Boolean.FALSE );
				} else {
					ps.setAdjustToBaseLineGrid( Boolean.TRUE );
				}
			}
			catch ( ClassCastException ex )
			{
				logger.log( "Generator warning: parameters type clash for tag \"*p\"." );
			}
		}
	} else {
		logger.log( "Generator warning: parameters missing for tag \"*p\"." );
	}
}
private void extractRa( MutableAttributeSet a, CoXtgLogger logger )
{
	if
		( m_isDollar )
	{
		CoStyleConstants.setTopRulerThickness( a, null );
		CoStyleConstants.setTopRulerLeftIndent( a, null );
		CoStyleConstants.setTopRulerRightIndent( a, null );
		CoStyleConstants.setTopRulerPosition( a, null );
		
	} else if
		( m_paramsNode != null )
	{
		List l = m_paramsNode.getParameters();
		if
			( l.size() < 7 )
		{
			logger.log( "Generator warning: to few parameters for tag \"*ra\"." );
		} else {
			if
				( l.size() > 7 )
			{
				logger.log( "Generator warning: to many parameters for tag \"*ra\", the last " + ( l.size() - 7 ) + " are ignored." );
			}
			try
			{
				float f;
				f = ( (CoXtgParamParseNode) l.get( 0 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setTopRulerThickness( a, new Float( (int) f ) );

				logger.log( "Generator warning: parameters linestyle, color and shadefor tag \"*ra\" ignored." );

				f = ( (CoXtgParamParseNode) l.get( 4 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setTopRulerLeftIndent( a, new Float( (int) f ) );

				f = ( (CoXtgParamParseNode) l.get( 5 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setTopRulerRightIndent( a, new Float( (int) f ) );

				f = ( (CoXtgParamParseNode) l.get( 6 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setTopRulerPosition( a, new Float( (int) f ) );
			}
			catch ( ClassCastException ex )
			{
				logger.log( "Generator warning: parameters type clash for tag \"*ra\"." );
			}
		}
	} else if
		( m_numberNode != null )
	{
		if
			( m_numberNode.getNumber() == 0 )
		{
			CoStyleConstants.setTopRulerThickness( a, new Float( 0 ) );
		} else {
			logger.log( "Generator warning: illegal parameter value ( != 0 ) for tag \"*ra\"." );
		}
	} else {
		logger.log( "Generator warning: parameters missing for tag \"*ra\"." );
	}
}
private void extractRa( com.bluebrim.text.shared.CoParagraphStyleIF ps, CoXtgLogger logger )
{
	if
		( m_paramsNode != null )
	{
		List l = m_paramsNode.getParameters();
		if
			( l.size() < 7 )
		{
			logger.log( "Generator warning: to few parameters for tag \"*ra\"." );
		} else {
			if
				( l.size() > 7 )
			{
				logger.log( "Generator warning: to many parameters for tag \"*ra\", the last " + ( l.size() - 7 ) + " are ignored." );
			}
			try
			{
				float f;
				f = ( (CoXtgParamParseNode) l.get( 0 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setTopRulerThickness( new Float( (int) f ) );

				logger.log( "Generator warning: parameters linestyle, color and shadefor tag \"*ra\" ignored." );

				f = ( (CoXtgParamParseNode) l.get( 4 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setTopRulerLeftInset( new Float( (int) f ) );

				f = ( (CoXtgParamParseNode) l.get( 5 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setTopRulerRightInset( new Float( (int) f ) );

				f = ( (CoXtgParamParseNode) l.get( 6 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setTopRulerPosition( new Float( (int) f ) );
			}
			catch ( ClassCastException ex )
			{
				logger.log( "Generator warning: parameters type clash for tag \"*ra\"." );
			}
		}
	} else if
		( m_numberNode != null )
	{
		if
			( m_numberNode.getNumber() == 0 )
		{
			ps.setTopRulerThickness( new Float( 0 ) );
		} else {
			logger.log( "Generator warning: illegal parameter value ( != 0 ) for tag \"*ra\"." );
		}
	} else {
		logger.log( "Generator warning: parameters missing for tag \"*ra\"." );
	}
}
private void extractRb( MutableAttributeSet a, CoXtgLogger logger )
{
	if
		( m_isDollar )
	{
		CoStyleConstants.setBottomRulerThickness( a, null );
		CoStyleConstants.setBottomRulerLeftIndent( a, null );
		CoStyleConstants.setBottomRulerRightIndent( a, null );
		CoStyleConstants.setBottomRulerPosition( a, null );
		
	} else if
		( m_paramsNode != null )
	{
		List l = m_paramsNode.getParameters();
		if
			( l.size() < 7 )
		{
			logger.log( "Generator warning: to few parameters for tag \"*rb\"." );
		} else {
			if
				( l.size() > 7 )
			{
				logger.log( "Generator warning: to many parameters for tag \"*rb\", the last " + ( l.size() - 7 ) + " are ignored." );
			}
			try
			{
				float f;
				f = ( (CoXtgParamParseNode) l.get( 0 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setBottomRulerThickness( a, new Float( (int) f ) );

				logger.log( "Generator warning: parameters linestyle, color and shadefor tag \"*rb\" ignored." );

				f = ( (CoXtgParamParseNode) l.get( 4 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setBottomRulerLeftIndent( a, new Float( (int) f ) );

				f = ( (CoXtgParamParseNode) l.get( 5 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setBottomRulerRightIndent( a, new Float( (int) f ) );

				f = ( (CoXtgParamParseNode) l.get( 6 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) CoStyleConstants.setBottomRulerPosition( a, new Float( (int) f ) );
			}
			catch ( ClassCastException ex )
			{
				logger.log( "Generator warning: parameters type clash for tag \"*rb\"." );
			}
		}
	} else if
		( m_numberNode != null )
	{
		if
			( m_numberNode.getNumber() == 0 )
		{
			CoStyleConstants.setBottomRulerThickness( a, new Float( 0 ) );
		} else {
			logger.log( "Generator warning: illegal parameter value ( != 0 ) for tag \"*rb\"." );
		}
	} else {
		logger.log( "Generator warning: parameters missing for tag \"*rb\"." );
	}
}
private void extractRb( com.bluebrim.text.shared.CoParagraphStyleIF ps, CoXtgLogger logger )
{
	if
		( m_paramsNode != null )
	{
		List l = m_paramsNode.getParameters();
		if
			( l.size() < 7 )
		{
			logger.log( "Generator warning: to few parameters for tag \"*rb\"." );
		} else {
			if
				( l.size() > 7 )
			{
				logger.log( "Generator warning: to many parameters for tag \"*rb\", the last " + ( l.size() - 7 ) + " are ignored." );
			}
			try
			{
				float f;
				f = ( (CoXtgParamParseNode) l.get( 0 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setBottomRulerThickness( new Float( (int) f ) );

				logger.log( "Generator warning: parameters linestyle, color and shadefor tag \"*rb\" ignored." );

				f = ( (CoXtgParamParseNode) l.get( 4 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setBottomRulerLeftInset( new Float( (int) f ) );

				f = ( (CoXtgParamParseNode) l.get( 5 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setBottomRulerRightInset( new Float( (int) f ) );

				f = ( (CoXtgParamParseNode) l.get( 6 ) ).getNumber();
				if ( ! Float.isNaN( f ) ) ps.setBottomRulerPosition( new Float( (int) f ) );
			}
			catch ( ClassCastException ex )
			{
				logger.log( "Generator warning: parameters type clash for tag \"*rb\"." );
			}
		}
	} else if
		( m_numberNode != null )
	{
		if
			( m_numberNode.getNumber() == 0 )
		{
			ps.setTopRulerThickness( new Float( 0 ) );
		} else {
			logger.log( "Generator warning: illegal parameter value ( != 0 ) for tag \"*rb\"." );
		}
	} else {
		logger.log( "Generator warning: parameters missing for tag \"*rb\"." );
	}
}
private static String getBlack()
{
	if ( m_black == null ) getColorNames();
	return m_black;
}
private static void getColorNames()
{
	m_black = "Black"; // test
	m_white = "White"; // test
	m_cyan = "Cyan"; // test
	m_yellow = "Yellow"; // test
	m_magenta = "Magenta"; // test
	/*
	m_black = ( (com.bluebrim.paint.impl.shared.CoProcessBlackIF) CoFactoryManager.getFactory( com.bluebrim.paint.impl.shared.CoProcessBlackIF.PROCESS_BLACK ).createObject() ).getIdentity();
	m_white = ( (com.bluebrim.paint.impl.shared.CoProcessBlackIF) CoFactoryManager.getFactory( com.bluebrim.paint.impl.shared.CoWhiteColorIF.WHITE_COLOR ).createObject() ).getIdentity();
	m_cyan = ( (com.bluebrim.paint.impl.shared.CoProcessBlackIF) CoFactoryManager.getFactory( com.bluebrim.paint.impl.shared.CoProcessCyanIF.PROCESS_CYAN ).createObject() ).getIdentity();
	m_magenta = ( (com.bluebrim.paint.impl.shared.CoProcessBlackIF) CoFactoryManager.getFactory( com.bluebrim.paint.impl.shared.CoProcessMagentaIF.PROCESS_MAGENTA ).createObject() ).getIdentity();
	m_yellow = ( (com.bluebrim.paint.impl.shared.CoProcessBlackIF) CoFactoryManager.getFactory( com.bluebrim.paint.impl.shared.CoProcessYellowIF.PROCESS_YELLOW ).createObject() ).getIdentity();
	*/
}
private static String getCyan()
{
	if ( m_cyan == null ) getColorNames();
	return m_cyan;
}
private static String getMagenta()
{
	if ( m_magenta == null ) getColorNames();
	return m_magenta;
}
public String getName()
{
	return m_nameNode.getName();
}
private static String getWhite()
{
	if ( m_white == null ) getColorNames();
	return m_white;
}
private static String getYellow()
{
	if ( m_yellow == null ) getColorNames();
	return m_yellow;
}
public boolean isDollar()
{
	return m_isDollar;
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	CoXtgToken t = p.getToken();

	if
		( ! ( t instanceof CoXtgNameToken ) )
	{
		return false;
	}
	
	m_nameNode = new CoXtgNameParseNode( ( (CoXtgNameToken) t ).getString() );
	
	t = p.nextToken();

	if
		( t instanceof CoXtgQuotedStringToken )
	{
		m_qstringNode = new CoXtgQstringParseNode( ( (CoXtgQuotedStringToken) t ).getString() );
		p.nextToken();
		
	} else if
		( t instanceof CoXtgNumberToken )
	{
		m_numberNode = new CoXtgNumberParseNode( ( (CoXtgNumberToken) t ).getString(), ( (CoXtgNumberToken) t ).getFloat() );
		p.nextToken();
		
	} else if
		( t instanceof CoXtgDollarToken )
	{
		m_isDollar = true;
		p.nextToken();
	} else if
		( t instanceof CoXtgNameToken )
	{
		m_isDollar = ( (CoXtgNameToken) t ).getString().equals( "$" );
		if
			( m_isDollar )
		{
			p.nextToken();
		}
	} else if
		( t instanceof CoXtgLeftParenToken )
	{
		p.nextToken();
		m_paramsNode = new CoXtgParamsParseNode();
		m_paramsNode.parse( p, l );
		checkToken( p.getToken(), CoXtgRightParenToken.class );
		p.nextToken();
	}

	return true;
}
public String toString()
{
	StringBuffer s = new StringBuffer();
	collect( s );
	return s.toString();
}
}