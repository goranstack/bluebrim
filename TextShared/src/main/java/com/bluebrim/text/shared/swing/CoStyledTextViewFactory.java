package com.bluebrim.text.shared.swing;

import javax.swing.text.*;

import com.bluebrim.text.shared.*;

/**
 * Element -> view mapping for styled text editor
 * 
 * @author: Dennis Malmström
 */

public class CoStyledTextViewFactory implements ViewFactory
{
	private static final CoStyledTextViewFactory m_instance = new CoStyledTextViewFactory();
protected CoStyledTextViewFactory()
{
}
public View create( Element elem )
{
	String kind = elem.getName();
	if
		( kind != null )
	{
		if      ( kind.equals( AbstractDocument.ContentElementName ) )   return CoLabelView.create( elem );
		else if ( kind.equals( AbstractDocument.ParagraphElementName ) ) return CoParagraphView.create( elem );
		else if ( kind.equals( AbstractDocument.SectionElementName ) )   return com.bluebrim.text.shared.swing.CoSectionView.create( elem );
		else if ( kind.equals( StyleConstants.ComponentElementName ) )   return new ComponentView( elem );
		else if ( kind.equals( StyleConstants.IconElementName ) )        return CoIconView.create( elem );
		else if ( kind.equals( CoTextConstants.CommentElementName ) )    return new CoJustifiableCommentView( elem ); // PENDING: reuse ???
	}
	return CoLabelView.create( elem );
}
public static ViewFactory getInstance()
{
	return m_instance;
}
}