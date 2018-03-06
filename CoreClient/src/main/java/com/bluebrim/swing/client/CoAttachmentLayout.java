package com.bluebrim.swing.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CoAttachmentLayout implements LayoutManager2
{
		private static final int NOT_CALCULATED = Integer.MAX_VALUE;
		private static final int IN_PROGRESS = Integer.MAX_VALUE - 1;
		private static final int UNDEFINED = Integer.MAX_VALUE - 2;
		
	  // --- Edge ---
	
	static abstract class Edge
	{
		protected abstract void attach( Attachments as, AttachmentSpec a );
		protected abstract int getComponentPosition( Container parent, AttachmentSpec attachment );
		protected abstract int getComponentPosition( Pos p, Container parent, AttachmentSpec attachment );
		protected abstract int getOppositeEdgeComponentPosition( Container parent, Attachments target );
		protected abstract int getOppositeEdgeComponentPosition( Pos p, Container parent, Attachments target );
		protected abstract int getOppositeSpan( Attachments target );
		protected abstract int getSpan( Attachments target );
		protected abstract double getSpan( Container parent );
		protected abstract int getLeadingInset( Container parent );
		protected abstract int getContainerPosition( Container parent, AttachmentSpec attachment );
		protected abstract AttachmentSpec getAttachmentSpec( Attachments target );
		protected abstract AttachmentSpec getOppositeAttachmentSpec( Attachments target );
		protected abstract int getComponentPosition( Attachments target );
		protected abstract boolean sameOrientation( Edge e );
		protected abstract int getInset( Container parent );
		protected abstract Edge getInnerEdge();
		protected abstract Edge getOuterEdge();
		protected abstract Edge getOppositeEdge();
		protected abstract Edge getOppositeDir( Container parent, Attachments target );
	}
	
	
	private static abstract class HorizontalEdge extends Edge
	{
		protected final int getSpan( Attachments target ) { return (int) target.m_component.getPreferredSize().getHeight(); }
		protected final double getSpan( Container parent ) { return parent.getHeight() - parent.getInsets().top - parent.getInsets().bottom; }
		protected final int getLeadingInset( Container parent ) { return parent.getInsets().top; }
		protected final boolean sameOrientation( Edge e ) { return e instanceof HorizontalEdge; }
		protected final Edge getInnerEdge() { return TOP; }
		protected final Edge getOuterEdge() { return BOTTOM; }
	}
	
	private static class TopEdge extends HorizontalEdge
	{
		protected int getInset( Container parent ) { return parent.getInsets().top; }
		protected void attach( Attachments as, AttachmentSpec a ) { as.m_top = a; }
		public String toString() { return "TOP"; }
		protected final int getComponentPosition( Container parent, AttachmentSpec attachment ) { return attachment.m_handle.getY0( parent ); }
		protected final int getComponentPosition( Pos p, Container parent, AttachmentSpec attachment )
		{
			int i = attachment.m_handle.getRy0( parent );
			p.m_dir = attachment.m_handle.m_ry0.m_dir;
			return i;
		}
		protected final int getOppositeEdgeComponentPosition( Container parent, Attachments target ) { return target.getY1( parent ); }
		protected final int getOppositeEdgeComponentPosition( Pos p, Container parent, Attachments target ) { return target.getRy1( parent ); }
		protected final int getOppositeSpan( Attachments target ) { return - getSpan( target ); }
		protected final int getContainerPosition( Container parent, AttachmentSpec attachment )
		{
			return attachment.m_absoluteOffset + parent.getInsets().top;
		}
		protected final AttachmentSpec getAttachmentSpec( Attachments target ) { return target.m_top; }
		protected final AttachmentSpec getOppositeAttachmentSpec( Attachments target ) { return target.m_bottom; }
		protected final int getComponentPosition( Attachments target )
		{
			return target.m_component.getY();
		}
		protected final Edge getOppositeDir( Container parent, Attachments target )
		{
			target.getRy1( parent );
			return target.m_ry1.m_dir;
		}
		protected final Edge getOppositeEdge() { return BOTTOM; }
	}
	
	private static class BottomEdge extends HorizontalEdge
	{
		protected int getInset( Container parent ) { return parent.getInsets().bottom; }
		protected void attach( Attachments as, AttachmentSpec a ) { as.m_bottom = a; }
		public String toString() { return "BOTTOM"; }
		protected final int getComponentPosition( Container parent, AttachmentSpec attachment ) { return attachment.m_handle.getY1( parent ); }
		protected final int getComponentPosition( Pos p, Container parent, AttachmentSpec attachment )
		{
			int i = attachment.m_handle.getRy1( parent );
			p.m_dir = attachment.m_handle.m_ry1.m_dir;
			return i;
		}
		protected final int getOppositeEdgeComponentPosition( Container parent, Attachments target ) { return target.getY0( parent ); }
		protected final int getOppositeEdgeComponentPosition( Pos p, Container parent, Attachments target ) { return target.getRy0( parent ); }
		protected final int getOppositeSpan( Attachments target ) { return getSpan( target ); }
		protected final int getContainerPosition( Container parent, AttachmentSpec attachment )
		{
			return parent.getHeight() - parent.getInsets().bottom - attachment.m_absoluteOffset;
		}
		protected final AttachmentSpec getAttachmentSpec( Attachments target ) { return target.m_bottom; }
		protected final AttachmentSpec getOppositeAttachmentSpec( Attachments target ) { return target.m_top; }
		protected final int getComponentPosition( Attachments target )
		{
			return target.m_component.getY() + (int) target.m_component.getPreferredSize().getHeight();
		}
		protected final Edge getOppositeDir( Container parent, Attachments target )
		{
			target.getRy0( parent );
			return target.m_ry0.m_dir;
		}
		protected final Edge getOppositeEdge() { return TOP; }
	}
	
	
	private static abstract class VerticalEdge extends Edge
	{
		protected final int getSpan( Attachments target ) { return (int) target.m_component.getPreferredSize().getWidth(); }
		protected final double getSpan( Container parent ) { return parent.getWidth() - parent.getInsets().left - parent.getInsets().right; }
		protected final int getLeadingInset( Container parent ) { return parent.getInsets().left; }
		protected final boolean sameOrientation( Edge e ) { return e instanceof VerticalEdge; }
		protected final Edge getInnerEdge() { return LEFT; }
		protected final Edge getOuterEdge() { return RIGHT; }
	}
	
	private static class LeftEdge extends VerticalEdge
	{
		protected int getInset( Container parent ) { return parent.getInsets().left; }
		protected void attach( Attachments as, AttachmentSpec a ) { as.m_left = a; }
		public String toString() { return "LEFT"; }
		protected final int getComponentPosition( Container parent, AttachmentSpec attachment ) { return attachment.m_handle.getX0( parent ); }
		protected final int getComponentPosition( Pos p, Container parent, AttachmentSpec attachment )
		{
			int i = attachment.m_handle.getRx0( parent );
			p.m_dir = attachment.m_handle.m_rx0.m_dir;
			return i;
		}
		protected final int getOppositeEdgeComponentPosition( Container parent, Attachments target ) { return target.getX1( parent ); }
		protected final int getOppositeEdgeComponentPosition( Pos p, Container parent, Attachments target ) { return target.getRx1( parent ); }
		protected final int getOppositeSpan( Attachments target ) { return - getSpan( target ); }
		protected final int getContainerPosition( Container parent, AttachmentSpec attachment )
		{
			return attachment.m_absoluteOffset + parent.getInsets().left;
		}
		protected final AttachmentSpec getAttachmentSpec( Attachments target ) { return target.m_left; }
		protected final AttachmentSpec getOppositeAttachmentSpec( Attachments target ) { return target.m_right; }
		protected final int getComponentPosition( Attachments target )
		{
			return target.m_component.getX();
		}
		protected final Edge getOppositeDir( Container parent, Attachments target )
		{
			target.getRx1( parent );
			return target.m_rx1.m_dir;
		}
		protected final Edge getOppositeEdge() { return RIGHT; }
	}
	
	private static class RightEdge extends VerticalEdge
	{
		protected int getInset( Container parent ) { return parent.getInsets().right; }
		protected void attach( Attachments as, AttachmentSpec a ) { as.m_right = a; }
		public String toString() { return "RIGHT"; }
		protected final int getComponentPosition( Container parent, AttachmentSpec attachment ) { return attachment.m_handle.getX1( parent ); }
		protected final int getComponentPosition( Pos p, Container parent, AttachmentSpec attachment )
		{
			int i = attachment.m_handle.getRx1( parent );
			p.m_dir = attachment.m_handle.m_rx1.m_dir;
			return i;
		}
		protected final int getOppositeEdgeComponentPosition( Container parent, Attachments target ) { return target.getX0( parent ); }
		protected final int getOppositeEdgeComponentPosition( Pos p, Container parent, Attachments target ) { return target.getRx0( parent ); }
		protected final int getOppositeSpan( Attachments target ) { return getSpan( target ); }
		protected final int getContainerPosition( Container parent, AttachmentSpec attachment )
		{
			return parent.getWidth() - parent.getInsets().right - attachment.m_absoluteOffset;
		}
		protected final AttachmentSpec getAttachmentSpec( Attachments target ) { return target.m_right; }
		protected final AttachmentSpec getOppositeAttachmentSpec( Attachments target ) { return target.m_left; }
		protected final int getComponentPosition( Attachments target )
		{
			return target.m_component.getX() + (int) target.m_component.getPreferredSize().getWidth();
		}
		protected final Edge getOppositeDir( Container parent, Attachments target )
		{
			target.getRx0( parent );
			return target.m_rx0.m_dir;
		}
		protected final Edge getOppositeEdge() { return LEFT; }
	}
	
	static final Edge TOP = new TopEdge();
	static final Edge BOTTOM = new BottomEdge();
	static final Edge LEFT = new LeftEdge();
	static final Edge RIGHT = new RightEdge();
	
	
	
	
	
	
	
	  // --- Attachment ---
	
	static abstract class Attachment
	{
		protected Edge m_edge;
		
		public Attachment( Edge e )
		{
			m_edge = e;
		}
		
		protected abstract int calculatePosition( Container parent, Attachments target, AttachmentSpec attachment );
		protected abstract int calculatePosition( Pos p, Container parent, Attachments target, AttachmentSpec attachment );

		protected int getEdgeOffset( Container parent, Attachments target, AttachmentSpec attachment )
		{
			return 0;
		}
		
		protected final void attach( Attachments as, AttachmentSpec a )
		{
			m_edge.attach( as, a );
		}
		
		final Attachment getNo()
		{
			return (Attachment) m_noAttachments.get( m_edge );
		}
		
		final Attachment getRelative()
		{
			return (Attachment) m_relativeAttachments.get( m_edge );
		}
		
		final Attachment getContainer( Edge e )
		{
			if ( m_edge.sameOrientation( e ) ) return (Attachment) m_containerAttachments.get( e );
			return null;
		}
		
		final Attachment getComponent( Edge e )
		{
			if ( m_edge.sameOrientation( e ) ) return (Attachment) ( (Map) m_componentAttachments.get( m_edge ) ).get( e );
			return null;
		}
		
		boolean isRelative()
		{
			return false;
		}
		
		boolean isComponent()
		{
			return false;
		}
		
		boolean isContainer()
		{
			return false;
		}
		
		boolean isContainerMargin()
		{
			return false;
		}
		
		boolean isNo()
		{
			return false;
		}
		
		public abstract String getType();
	}



	
	
	private static class NoAttachment extends Attachment
	{
		public NoAttachment( Edge e )
		{
			super( e );
		}
		
		protected final int calculatePosition( Container parent, Attachments target, AttachmentSpec attachment )
		{
			if
				( m_edge.getOppositeAttachmentSpec( target ).m_attachment instanceof NoAttachment )
			{
				return m_edge.getComponentPosition( target );
			} else {
				return m_edge.getOppositeEdgeComponentPosition( parent, target ) + m_edge.getOppositeSpan( target );
			}
		}
		
		protected final int calculatePosition( Pos p, Container parent, Attachments target, AttachmentSpec attachment )
		{
			if
				( m_edge.getOppositeAttachmentSpec( target ).m_attachment instanceof NoAttachment )
			{
				p.m_dir = m_edge.getInnerEdge();
				return m_edge.getComponentPosition( target ) + m_edge.getInset( parent );
			} else {
				p.m_dir = m_edge.getOppositeDir( parent, target );
				return m_edge.getOppositeEdgeComponentPosition( p, parent, target ) + m_edge.getOppositeSpan( target ) + m_edge.getInset( parent );
			}
		}
		
		boolean isNo()
		{
			return true;
		}
		
		public String toString()
		{
			return m_edge + "_NO";
		}
		
		public String getType()
		{
			return "None";
		}
	};
		
	
	
	
	private static class ContainerAttachment extends Attachment
	{
		public ContainerAttachment( Edge e )
		{
			super( e );
		}
		
		protected int calculatePosition( Container parent, Attachments target, AttachmentSpec attachment )
		{
			return m_edge.getContainerPosition( parent, attachment );
		}
		
		protected final int calculatePosition( Pos p, Container parent, Attachments target, AttachmentSpec attachment )
		{
			int i = calculatePosition( parent, target, attachment );
			p.m_dir = m_edge;
			return i;
		}
		
		
		protected final int getEdgeOffset( Container parent, Attachments target, AttachmentSpec attachment )
		{
			return attachment.m_absoluteOffset;
		}
		
		boolean isContainer()
		{
			return true;
		}
		
		public String toString()
		{
			return m_edge + "_CONTAINER";
		}
		
		public String getType()
		{
			return "Container";
		}
	};
	
	
	
	private static class RelativeAttachment extends Attachment
	{
		public RelativeAttachment( Edge e )
		{
			super( e );
		}
		
		protected int calculatePosition( Container parent, Attachments target, AttachmentSpec attachment )
		{
			return m_edge.getLeadingInset( parent ) + (int) ( m_edge.getSpan( parent ) * attachment.m_relativeOffset + attachment.m_absoluteOffset );
		}
		
		protected final int calculatePosition( Pos p, Container parent, Attachments target, AttachmentSpec attachment )
		{
			p.m_dir = m_edge.getOppositeEdge();
			if
				( m_edge.getOppositeAttachmentSpec( target ).m_attachment instanceof NoAttachment )
			{
				return m_edge.getComponentPosition( target );
			} else if
				( m_edge.getOppositeAttachmentSpec( target ).m_attachment instanceof RelativeAttachment )
			{
				p.m_dir = m_edge.getInnerEdge();
				return (int) ( m_edge.getSpan( target ) / Math.abs( attachment.m_relativeOffset - m_edge.getOppositeAttachmentSpec( target ).m_relativeOffset ) );
			} else {
				return m_edge.getOppositeEdgeComponentPosition( p, parent, target ) + m_edge.getOppositeSpan( target );
			}
		}
		
		boolean isRelative()
		{
			return true;
		}
		
		public String toString()
		{
			return m_edge + "_RELATIVE";
		}
		
		public String getType()
		{
			return "Relative";
		}
	};
	
	
	private static class ComponentAttachment extends Attachment
	{
		private Edge m_otherEdge;
		
		public ComponentAttachment( Edge e, Edge oe )
		{
			super( e );
			m_otherEdge = oe;
		}
		
		protected int calculatePosition( Container parent, Attachments target, AttachmentSpec attachment )
		{
			return m_otherEdge.getComponentPosition( parent, attachment ) + attachment.m_absoluteOffset;
		}
		
		protected final int calculatePosition( Pos p, Container parent, Attachments target, AttachmentSpec attachment )
		{
			return m_otherEdge.getComponentPosition( p, parent, attachment ) + attachment.m_absoluteOffset;
		}

		boolean isComponent()
		{
			return true;
		}
		
		public String toString()
		{
			return m_edge + "_COMPONENT_" + m_otherEdge;
		}
		
		public String getType()
		{
			return "Component";
		}
	};
	
	
	public static Attachment TOP_NO = new NoAttachment( TOP );
	public static Attachment BOTTOM_NO = new NoAttachment( BOTTOM );
	public static Attachment LEFT_NO = new NoAttachment( LEFT );
	public static Attachment RIGHT_NO = new NoAttachment( RIGHT );
	
	public static Attachment TOP_CONTAINER = new ContainerAttachment( TOP );
	public static Attachment BOTTOM_CONTAINER = new ContainerAttachment( BOTTOM );
	public static Attachment LEFT_CONTAINER = new ContainerAttachment( LEFT );
	public static Attachment RIGHT_CONTAINER = new ContainerAttachment( RIGHT );
	
	public static Attachment TOP_COMPONENT_TOP = new ComponentAttachment( TOP, TOP );
	public static Attachment TOP_COMPONENT_BOTTOM = new ComponentAttachment( TOP, BOTTOM );
	public static Attachment BOTTOM_COMPONENT_TOP = new ComponentAttachment( BOTTOM, TOP );
	public static Attachment BOTTOM_COMPONENT_BOTTOM = new ComponentAttachment( BOTTOM, BOTTOM );
	public static Attachment LEFT_COMPONENT_LEFT = new ComponentAttachment( LEFT, LEFT );
	public static Attachment LEFT_COMPONENT_RIGHT = new ComponentAttachment( LEFT, RIGHT );
	public static Attachment RIGHT_COMPONENT_LEFT = new ComponentAttachment( RIGHT, LEFT );
	public static Attachment RIGHT_COMPONENT_RIGHT = new ComponentAttachment( RIGHT, RIGHT );
	
	public static Attachment TOP_RELATIVE = new RelativeAttachment( TOP );
	public static Attachment BOTTOM_RELATIVE = new RelativeAttachment( BOTTOM );
	public static Attachment LEFT_RELATIVE = new RelativeAttachment( LEFT );
	public static Attachment RIGHT_RELATIVE = new RelativeAttachment( RIGHT );
	
	private static Map m_noAttachments = new HashMap();
	static
	{
		m_noAttachments.put( TOP, TOP_NO );
		m_noAttachments.put( BOTTOM, BOTTOM_NO );
		m_noAttachments.put( LEFT, LEFT_NO );
		m_noAttachments.put( RIGHT, RIGHT_NO );
	}
	
	private static Map m_relativeAttachments = new HashMap();
	static
	{
		m_relativeAttachments.put( TOP, TOP_RELATIVE );
		m_relativeAttachments.put( BOTTOM, BOTTOM_RELATIVE );
		m_relativeAttachments.put( LEFT, LEFT_RELATIVE );
		m_relativeAttachments.put( RIGHT, RIGHT_RELATIVE );
	}
	
	private static Map m_containerAttachments = new HashMap();
	static
	{
		m_containerAttachments.put( TOP, TOP_CONTAINER );
		m_containerAttachments.put( BOTTOM, BOTTOM_CONTAINER );
		m_containerAttachments.put( LEFT, LEFT_CONTAINER );
		m_containerAttachments.put( RIGHT, RIGHT_CONTAINER );
	}
	
	private static Map m_componentAttachments = new HashMap();
	static
	{
		Map m = new HashMap();
		m_componentAttachments.put( TOP, m );
		m.put( TOP, TOP_COMPONENT_TOP );
		m.put( BOTTOM, TOP_COMPONENT_BOTTOM );
		
		m = new HashMap();
		m_componentAttachments.put( BOTTOM, m );
		m.put( BOTTOM, BOTTOM_COMPONENT_BOTTOM );
		m.put( TOP, BOTTOM_COMPONENT_TOP );
		
		m = new HashMap();
		m_componentAttachments.put( LEFT, m );
		m.put( LEFT, LEFT_COMPONENT_LEFT );
		m.put( RIGHT, LEFT_COMPONENT_RIGHT );
		
		m = new HashMap();
		m_componentAttachments.put( RIGHT, m );
		m.put( RIGHT, RIGHT_COMPONENT_RIGHT );
		m.put( LEFT, RIGHT_COMPONENT_LEFT );
	}
	
	
	
	
	
	
	  // --- AttachmentSpec ---
	
	public static class AttachmentSpec
	{
		Attachment m_attachment;
		int m_absoluteOffset;
		float m_relativeOffset;
		Component m_component;
		Attachments m_handle;
		
		
		public AttachmentSpec( Attachment t )
		{
			this( t, 0, 0, null );
		}
		
		public AttachmentSpec( Attachment t, int absOffset )
		{
			this( t, absOffset, 0, null );
		}
		
		public AttachmentSpec( Attachment t, float relOffset )
		{
			this( t, 0, relOffset, null );
		}
		
		public AttachmentSpec( Attachment t, int absOffset, float relOffset )
		{
			this( t, absOffset, relOffset, null );
		}
		
		public AttachmentSpec( Attachment t, float relOffset, int absOffset )
		{
			this( t, absOffset, relOffset, null );
		}
		
		public AttachmentSpec( Attachment t, Component comp )
		{
			this( t, 0, 0, comp );
		}
		
		public AttachmentSpec( Attachment t, int absOffset, Component comp )
		{
			this( t, absOffset, 0, comp );
		}
		
		
		private AttachmentSpec( Attachment t, int absOffset, float relOffset, Component comp )
		{
			m_attachment = t;
			m_absoluteOffset = absOffset;
			m_relativeOffset = relOffset;
			m_component = comp;
		}
		
		private AttachmentSpec( AttachmentSpec a )
		{
			this( a.m_attachment, a.m_absoluteOffset, a.m_relativeOffset, a.m_component );
		}
		
		
		private int calculatePosition( Container parent, Attachments target )
		{
			return m_attachment.calculatePosition( parent, target, this );
		}
		
		private int calculatePosition( Pos p, Container parent, Attachments target )
		{
			return m_attachment.calculatePosition( p, parent, target, this );
		}
		
		
		private int getEdgeOffset( Container parent, Attachments target )
		{
			return m_attachment.getEdgeOffset( parent, target, this );
		}
	}
	
	
	


	private static class Pos
	{
		public int m_pos;
		public Edge m_dir;

		public String toString()
		{
			String str = m_pos + " (" + m_dir + ")";
			return str;
		}
	};
	
	
	
	  // --- Attachments ---
	
	public static class Attachments
	{
		AttachmentSpec m_top;
		AttachmentSpec m_bottom;
		AttachmentSpec m_left;
		AttachmentSpec m_right;
		
		private Component m_component;

		int m_x0;
		int m_x1;
		int m_y0;
		int m_y1;

		Pos m_rx0 = new Pos();
		Pos m_rx1 = new Pos();
		Pos m_ry0 = new Pos();
		Pos m_ry1 = new Pos();

		public Attachments()
		{
			fill();
		}
		
		public Attachments( AttachmentSpec a0 )
		{
			a0.m_attachment.attach( this, a0 );
			fill();
		}
		
		public Attachments( AttachmentSpec a0, AttachmentSpec a1 )
		{
			a0.m_attachment.attach( this, a0 );
			a1.m_attachment.attach( this, a1 );
			fill();
		}
		
		public Attachments( AttachmentSpec a0, AttachmentSpec a1, AttachmentSpec a2 )
		{
			a0.m_attachment.attach( this, a0 );
			a1.m_attachment.attach( this, a1 );
			a2.m_attachment.attach( this, a2 );
			fill();
		}
		
		public Attachments( AttachmentSpec a0, AttachmentSpec a1, AttachmentSpec a2, AttachmentSpec a3 )
		{
			a0.m_attachment.attach( this, a0 );
			a1.m_attachment.attach( this, a1 );
			a2.m_attachment.attach( this, a2 );
			a3.m_attachment.attach( this, a3 );
			fill();
		}
		
		private void fill()
		{
			if ( m_top == null ) m_top = new AttachmentSpec( TOP_NO );
			if ( m_bottom == null ) m_bottom = new AttachmentSpec( BOTTOM_NO );
			if ( m_left == null ) m_left = new AttachmentSpec( LEFT_NO );
			if ( m_right == null ) m_right = new AttachmentSpec( RIGHT_NO );
		}
		
		
		
		
		private void invalidateLayout()
		{
			m_x0 = m_x1 = m_y0 = m_y1 = NOT_CALCULATED;
		}
		
		private void invalidateRequests()
		{
			m_rx0.m_pos = m_rx1.m_pos = m_ry0.m_pos = m_ry1.m_pos = NOT_CALCULATED;
			m_rx0.m_dir = m_rx1.m_dir = m_ry0.m_dir = m_ry1.m_dir = null;
		}


		private int handleRecursiveAttachment( String edge )
		{
			System.err.println( "WARNING " + getClass() + ": Recursive attachment involving " + edge + " edge of component \"" + m_component.getName() + "\" (" + m_component.getClass() + ")" );
			return 0;
		}
		
		
		private int getRx0( Container parent )
		{
			if ( m_rx0.m_pos == IN_PROGRESS ) return handleRecursiveAttachment( "left" );
			if
				( m_rx0.m_pos == NOT_CALCULATED )
			{
				m_rx0.m_pos = IN_PROGRESS;
				m_rx0.m_pos = m_left.calculatePosition( m_rx0, parent, this );
			}
			return m_rx0.m_pos;
		}
		
		private int getRx1( Container parent )
		{
			if ( m_rx1.m_pos == IN_PROGRESS ) return handleRecursiveAttachment( "right" );
			if
				( m_rx1.m_pos == NOT_CALCULATED )
			{
				m_rx1.m_pos = IN_PROGRESS;
				m_rx1.m_pos = m_right.calculatePosition( m_rx1, parent, this );
			}
			return m_rx1.m_pos;
		}
		
		private int getRy0( Container parent )
		{
			if ( m_ry0.m_pos == IN_PROGRESS ) return handleRecursiveAttachment( "top" );
			if
				( m_ry0.m_pos == NOT_CALCULATED )
			{
				m_ry0.m_pos = IN_PROGRESS;
				m_ry0.m_pos = m_top.calculatePosition( m_ry0, parent, this );
			}
			return m_ry0.m_pos;
		}
		
		private int getRy1( Container parent )
		{
			if ( m_ry1.m_pos == IN_PROGRESS ) return handleRecursiveAttachment( "bottom" );
			if
				( m_ry1.m_pos == NOT_CALCULATED )
			{
				m_ry1.m_pos = IN_PROGRESS;
				m_ry1.m_pos = m_bottom.calculatePosition( m_ry1, parent, this );
			}
			return m_ry1.m_pos;
		}
		
		
		
		private int getX0( Container parent )
		{
			if ( m_x0 == IN_PROGRESS ) return handleRecursiveAttachment( "left" );
			if
				( m_x0 == NOT_CALCULATED )
			{
				m_x0 = IN_PROGRESS;
				m_x0 = m_left.calculatePosition( parent, this );
//                 if
//                     ( m_x1 < IN_PROGRESS )
//                 {
//                     int w = (int) m_component.getMinimumSize().getWidth();
//                     if
//                         ( m_x1 - m_x0 < w )
//                     {
//                         m_x0 = m_x1 - w;
//                     }
//                 }
			}
			return m_x0;
		}
		
		private int getX1( Container parent )
		{
			if ( m_x1 == IN_PROGRESS ) return handleRecursiveAttachment( "right" );
			if
				( m_x1 == NOT_CALCULATED )
			{
				m_x1 = IN_PROGRESS;
				m_x1 = m_right.calculatePosition( parent, this );
//                 if
//                     ( m_x0 < IN_PROGRESS )
//                 {
//                     int w = (int) m_component.getMinimumSize().getWidth();
//                     if
//                         ( m_x1 - m_x0 < w )
//                     {
//                         m_x1 = m_x0 + w;
//                     }
//                 }
			}
			return m_x1;
		}
		
		private int getY0( Container parent )
		{
			if ( m_y0 == IN_PROGRESS ) return handleRecursiveAttachment( "top" );
			if
				( m_y0 == NOT_CALCULATED )
			{
				m_y0 = IN_PROGRESS;
				m_y0 = m_top.calculatePosition( parent, this );
//                 if
//                     ( m_y1 < IN_PROGRESS )
//                 {
//                     int h = (int) m_component.getMinimumSize().getHeight();
//                     if
//                         ( m_y1 - m_y0 < h )
//                     {
//                         m_y0 = m_y1 - h;
//                     }
//                 }
			}
			return m_y0;
		}
		
		private int getY1( Container parent )
		{
			if ( m_y1 == IN_PROGRESS ) return handleRecursiveAttachment( "bottom" );
			if
				( m_y1 == NOT_CALCULATED )
			{
				m_y1 = IN_PROGRESS;
				m_y1 = m_bottom.calculatePosition( parent, this );
//                 if
//                     ( m_y0 < IN_PROGRESS )
//                 {
//                     int h = (int) m_component.getMinimumSize().getHeight();
//                     if
//                         ( m_y1 - m_y0 < h )
//                     {
//                         m_y1 = m_y0 + h;
//                     }
//                 }
			}
			
			return m_y1;
		}
		
		private void calculateBounds( Container parent )
		{
			getX0( parent );
			getX1( parent );
			getY0( parent );
			getY1( parent );
		}
		
		private void calculateRequests( Container parent )
		{
			 getRx0( parent );
			 getRx1( parent );
			 getRy0( parent );
			 getRy1( parent );
		}
		
		private void normalizeRequests( Container parent )
		{
			Insets insets = parent.getInsets();
			
			if
				( m_rx0.m_dir != m_rx1.m_dir )
			{
				if
					( m_rx0.m_dir == LEFT )
				{
					m_rx1.m_dir = LEFT;
					m_rx1.m_pos = m_rx0.m_pos + (int) m_component.getPreferredSize().getWidth();
					if
						( m_right.m_attachment.isContainer() )
					{
						m_rx1.m_pos += m_right.m_absoluteOffset + insets.right;
					} else if
						( m_right.m_attachment.isComponent() )
					{
						Attachments as = m_right.m_handle;
						if
							( ( (ComponentAttachment) m_right.m_attachment ).m_otherEdge == LEFT )
						{
							as.m_rx0.m_dir = LEFT;
							as.m_rx0.m_pos = m_rx1.m_pos - m_right.m_absoluteOffset;
						} else {
							as.m_rx1.m_dir = LEFT;
							as.m_rx1.m_pos = m_rx1.m_pos - m_right.m_absoluteOffset;
						}
						as.normalizeRequests( parent );
					}
				} else {
					m_rx0.m_dir = LEFT;
					m_rx0.m_pos = m_rx1.m_pos + (int) m_component.getPreferredSize().getWidth();
										if
						( m_left.m_attachment.isContainer() )
					{
						m_rx0.m_pos += m_left.m_absoluteOffset + insets.left;
					} else if
						( m_left.m_attachment.isComponent() )
					{
						Attachments as = m_left.m_handle;
						if
							( ( (ComponentAttachment) m_left.m_attachment ).m_otherEdge == LEFT )
						{
							as.m_rx0.m_dir = LEFT;
							as.m_rx0.m_pos = m_rx0.m_pos - m_left.m_absoluteOffset;
						} else {
							as.m_rx1.m_dir = LEFT;
							as.m_rx1.m_pos = m_rx0.m_pos - m_left.m_absoluteOffset;
						}
						as.normalizeRequests( parent );
					}
				}
			}

			if
				( m_ry0.m_dir != m_ry1.m_dir )
			{
				if
					( m_ry0.m_dir == TOP )
				{
					m_ry1.m_dir = TOP;
					m_ry1.m_pos = m_ry0.m_pos + (int) m_component.getPreferredSize().getHeight();
					if
						( m_bottom.m_attachment.isContainer() )
					{
						m_ry1.m_pos += m_bottom.m_absoluteOffset + insets.bottom;
					} else if
						( m_bottom.m_attachment.isComponent() )
					{
						Attachments as = m_bottom.m_handle;
						if
							( ( (ComponentAttachment) m_bottom.m_attachment ).m_otherEdge == TOP )
						{
							as.m_ry0.m_dir = TOP;
							as.m_ry0.m_pos = m_ry1.m_pos - m_bottom.m_absoluteOffset;
						} else {
							as.m_ry1.m_dir = TOP;
							as.m_ry1.m_pos = m_ry1.m_pos - m_bottom.m_absoluteOffset;
						}
						as.normalizeRequests( parent );
					}
				} else {
					m_ry0.m_dir = TOP;
					m_ry0.m_pos = m_ry1.m_pos + (int) m_component.getPreferredSize().getHeight();
										if
						( m_top.m_attachment.isContainer() )
					{
						m_ry0.m_pos += m_top.m_absoluteOffset + insets.top;
					} else if
						( m_top.m_attachment.isComponent() )
					{
						Attachments as = m_top.m_handle;
						if
							( ( (ComponentAttachment) m_top.m_attachment ).m_otherEdge == TOP )
						{
							as.m_ry0.m_dir = TOP;
							as.m_ry0.m_pos = m_ry0.m_pos - m_top.m_absoluteOffset;
						} else {
							as.m_ry1.m_dir = TOP;
							as.m_ry1.m_pos = m_ry0.m_pos - m_top.m_absoluteOffset;
						}
						as.normalizeRequests( parent );
					}
				}
			}

			Insets i = parent.getInsets();
			int W = parent.getWidth();// + i.left + i.right;
			int H = parent.getHeight();// + i.top + i.bottom;
			
			if ( m_rx0.m_dir == RIGHT ) m_rx0.m_pos = W - m_rx0.m_pos;
			if ( m_rx1.m_dir == RIGHT ) m_rx1.m_pos = W - m_rx1.m_pos;
			if ( m_ry0.m_dir == BOTTOM ) m_ry0.m_pos = H - m_ry0.m_pos;
			if ( m_ry1.m_dir == BOTTOM ) m_ry1.m_pos = H - m_ry1.m_pos;

			boolean l = m_left.m_attachment.isRelative();
			boolean r = m_right.m_attachment.isRelative();
			boolean t = m_top.m_attachment.isRelative();
			boolean b = m_bottom.m_attachment.isRelative();
			
			if ( l && ! r ) m_rx0.m_pos /= ( 1 - m_left.m_relativeOffset );
			if ( r && ! l ) m_rx1.m_pos /= m_right.m_relativeOffset;
			if ( t && ! b ) m_ry0.m_pos /= ( 1 - m_top.m_relativeOffset );
			if ( b && ! t ) m_ry1.m_pos /= m_bottom.m_relativeOffset;
		}
		
		private void setBounds()
		{
			m_component.setBounds( m_x0, m_y0, m_x1 - m_x0, m_y1 - m_y0 );
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private List m_attachments = new ArrayList();
	private boolean m_areAttachmentsResolved = true;
	private boolean m_isEnabled = true;
	
	private boolean m_isLayoutValid;
	
	private boolean m_isSizeValid;
	private Dimension m_size = new Dimension();
	private static final Dimension m_minimumSize = new Dimension( 0, 0 );
	private static final Dimension m_maximumSize = new Dimension( Short.MAX_VALUE, Short.MAX_VALUE );
	
	private class Comparator
		{
		private Component m_component;
		
		public Comparator( Component c )
		{
			m_component = c;
		}
		
		public boolean equals( Object o )
		{
			return m_component == ( (Attachments) o ).m_component;
		}
		};
	
	
	
	public void addLayoutComponent( Component component, Object constraints )
	{
		if ( ! m_isEnabled ) return;
		
		Attachments vls = new Attachments();
		vls.m_component = component;
		
		if
			( constraints == null )
		{
			vls.m_top = new AttachmentSpec( TOP_NO );
			vls.m_bottom = new AttachmentSpec( BOTTOM_NO );
			vls.m_left = new AttachmentSpec( LEFT_NO );
			vls.m_right = new AttachmentSpec( RIGHT_NO );
		} else {
			Attachments ls = (Attachments) constraints;
			vls.m_top = new AttachmentSpec( ls.m_top );
			vls.m_bottom = new AttachmentSpec( ls.m_bottom );
			vls.m_left = new AttachmentSpec( ls.m_left );
			vls.m_right = new AttachmentSpec( ls.m_right );
		}
		
		m_attachments.add( vls );
		m_areAttachmentsResolved = false;
		invalidateLayout();
	}
	public void addLayoutComponent( String name, Component component ) {}
	protected void calculateBounds( Container parent )
	{
		if ( m_isLayoutValid ) return;
		
		resolve();
		
		Iterator i = m_attachments.iterator();
		while
			( i.hasNext() )
		{
			Attachments as = (Attachments) i.next();
			as.invalidateLayout();
		}
		
		i = m_attachments.iterator();
		while
			( i.hasNext() )
		{
			Attachments as = (Attachments) i.next();
			as.calculateBounds( parent );
		}
		
		m_isLayoutValid = true;
	}
	protected void checkRequests( Container parent )
	{
		if ( m_isSizeValid ) return;
		
		resolve();
		
		int w = 0;
		int h = 0;
		
		Iterator i = m_attachments.iterator();
		while
			( i.hasNext() )
		{
			Attachments as = (Attachments) i.next();
			as.invalidateRequests();
		}
		
		//System.err.println( "==================" );
		//System.err.println( "________C_________" );
		i = m_attachments.iterator();
		while
			( i.hasNext() )
		{
			Attachments as = (Attachments) i.next();
			as.calculateRequests( parent );
			//System.err.println( as.m_rx0 + "    " + as.m_rx1 );
		}
		
		//System.err.println( "________N_________" );
		i = m_attachments.iterator();
		while
			( i.hasNext() )
		{
			Attachments as = (Attachments) i.next();
			as.normalizeRequests( parent );
			//System.err.println( as.m_rx0 + "    " + as.m_rx1 );
		}
		
		//System.err.println( "________A_________" );
		i = m_attachments.iterator();
		while
			( i.hasNext() )
		{
			Attachments as = (Attachments) i.next();
			//System.err.println( as.m_rx0 + "    " + as.m_rx1 );
			w = Math.max( w, as.m_rx0.m_pos );
			w = Math.max( w, as.m_rx1.m_pos );
			h = Math.max( h, as.m_ry0.m_pos );
			h = Math.max( h, as.m_ry1.m_pos );
		}
		
		m_size.setSize( w, h );
		m_isSizeValid = true;
	}
	Attachments getAttachments( Component c )
	{
		int i = m_attachments.indexOf( new Comparator( c ) );
		return (Attachments) m_attachments.get( i );
	}
	public float getLayoutAlignmentX( Container parent )
	{
		return 0;
	}
	public float getLayoutAlignmentY( Container parent )
	{
		return 0;
	}
	private void invalidateLayout()
	{
		m_isLayoutValid = false;
		m_isSizeValid = false;
	}
	public void invalidateLayout( Container parent )
	{
		invalidateLayout();
	}
	public void layoutContainer( Container parent )
	{
		calculateBounds( parent );
		
		Iterator i = m_attachments.iterator();
		while
			( i.hasNext() )
		{
			Attachments as = (Attachments) i.next();
			as.setBounds();
		}
		
	}
	public Dimension maximumLayoutSize( Container parent )
	{
//		checkRequests( parent );
		return m_maximumSize;
	}
	public Dimension minimumLayoutSize( Container parent )
	{
//		checkRequests( parent );
		return m_minimumSize;
	}
	public Dimension preferredLayoutSize( Container parent )
	{
		checkRequests( parent );
		return m_size;
	}
	public void removeLayoutComponent( Component component )
	{
		if ( ! m_isEnabled ) return;
		int i = m_attachments.indexOf( new Comparator( component ) );
		m_attachments.remove( i );
		m_areAttachmentsResolved = false;
		invalidateLayout();
	}
	protected void resolve()
	{
		if ( m_areAttachmentsResolved ) return;
		
		Iterator i = m_attachments.iterator();
		while
			( i.hasNext() )
		{
			Attachments as = (Attachments) i.next();
			
			int n = 0;
			if
				( as.m_top.m_attachment.isComponent() )
			{
				if
					(
					 ( as.m_top.m_component != null )
					 &&
					 ( (  n = m_attachments.indexOf( new Comparator( as.m_top.m_component ) ) ) != -1 )
					 )
				{
					as.m_top.m_handle = (Attachments) m_attachments.get( n );
				} else {
					as.m_top.m_attachment = TOP_NO;
					as.m_top.m_component = null;
					as.m_top.m_absoluteOffset = 0;
					as.m_top.m_relativeOffset = 0;
					as.m_top.m_handle = null;
				}
			}
			
			if
				( as.m_bottom.m_attachment.isComponent() )
			{
				if
					(
					 ( as.m_bottom.m_component != null )
					 &&
					 ( (  n = m_attachments.indexOf( new Comparator( as.m_bottom.m_component ) ) ) != -1 )
					 )
				{
					as.m_bottom.m_handle = (Attachments) m_attachments.get( n );
				} else {
					as.m_bottom.m_attachment = BOTTOM_NO;
					as.m_bottom.m_component = null;
					as.m_bottom.m_absoluteOffset = 0;
					as.m_bottom.m_relativeOffset = 0;
					as.m_bottom.m_handle = null;
				}
			}
			
			if
				( as.m_left.m_attachment.isComponent() )
			{
				if
					(
					 ( as.m_left.m_component != null )
					 &&
					 ( (  n = m_attachments.indexOf( new Comparator( as.m_left.m_component ) ) ) != -1 )
					 )
				{
					as.m_left.m_handle = (Attachments) m_attachments.get( n );
				} else {
					as.m_left.m_attachment = LEFT_NO;
					as.m_left.m_component = null;
					as.m_left.m_absoluteOffset = 0;
					as.m_left.m_relativeOffset = 0;
					as.m_left.m_handle = null;
				}
			}
			
			if
				( as.m_right.m_attachment.isComponent() )
			{
				if
					(
					 ( as.m_right.m_component != null )
					 &&
					 ( (  n = m_attachments.indexOf( new Comparator( as.m_right.m_component ) ) ) != -1 )
					 )
				{
					as.m_right.m_handle = (Attachments) m_attachments.get( n );
				} else {
					as.m_right.m_attachment = RIGHT_NO;
					as.m_right.m_component = null;
					as.m_right.m_absoluteOffset = 0;
					as.m_right.m_relativeOffset = 0;
					as.m_right.m_handle = null;
				}
			}
		}
		m_areAttachmentsResolved = true;
	}
	public void setEnabled( boolean b )
	{
		if ( m_isEnabled == b ) return;
		m_isEnabled = b;
	}
}
