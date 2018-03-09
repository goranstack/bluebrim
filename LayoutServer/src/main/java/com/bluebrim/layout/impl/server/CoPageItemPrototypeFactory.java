package com.bluebrim.layout.impl.server;

import java.util.HashMap;


import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.CoFactoryElementIF;
import com.bluebrim.base.shared.CoFactoryManager;
import com.bluebrim.base.shared.geom.CoCubicBezierCurveShape;
import com.bluebrim.base.shared.geom.CoLine;
import com.bluebrim.base.shared.geom.CoOrthogonalLine;
import com.bluebrim.base.shared.geom.CoPolygonShape;
import com.bluebrim.base.shared.geom.CoRectangleShape;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoContentWrapperPageItemIF;
import com.bluebrim.layout.impl.shared.CoPageItemImageContentIF;
import com.bluebrim.layout.impl.shared.CoPageItemLayoutContentIF;
import com.bluebrim.layout.impl.shared.CoPageItemPrototypeFactoryIF;
import com.bluebrim.layout.shared.*;

/**
 * Factroy class for creating prototype page items.
 *
 * @author: Dennis
 */
 
public class CoPageItemPrototypeFactory implements CoPageItemPrototypeFactoryIF
{
	private HashMap m_prototypeItemCreators = new HashMap();
	
	abstract class ItemCreator
	{
		public abstract CoShapePageItemIF create();
	}

public CoPageItemPrototypeFactory()
{
	super();
	init();
}
public CoFactoryElementIF createObject() {
	return null;
}
public CoShapePageItemIF getPrototypeItem(String key)
{
	ItemCreator c = (ItemCreator) m_prototypeItemCreators.get( key );
	
	return (CoShapePageItemIF) ( ( c != null ) ? c.create() : null );
}
private void init()
{
	m_prototypeItemCreators.put(
		LAYOUT_AREA,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				CoShapePageItemIF prototype = new CoLayoutArea();
				prototype.setDoRunAround( true );
				return prototype;
			}
		}
	);

	
	final float _2cm = CoLengthUnit.CM.from( 2 );
	
	m_prototypeItemCreators.put(
		RECTANGLE,
		new ItemCreator()
		{
		public CoShapePageItemIF create ()
			{
				CoContentWrapperPageItemIF prototype = new CoContentWrapperPageItem();
				prototype.setCoShape( new CoRectangleShape( 0, 0, _2cm, _2cm ) );
				prototype.setContent( new CoPageItemNoContent() );
				prototype.getMutableStrokeProperties().setWidth( 1 );
				prototype.getMutableStrokeProperties().setForegroundColor( (com.bluebrim.paint.shared.CoColorIF) CoFactoryManager.createObject( com.bluebrim.paint.impl.shared.CoProcessBlackIF.PROCESS_BLACK ) );
				return prototype;
			}
		}
	);

/*
	m_prototypeItemCreators.put(
		ROUNDED_CORNER_RECTANGLE,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				CoShapePageItemIF prototype = new CoContentWrapperPageItem();
				prototype.setCoShape( new CoRoundedCorner( 0, 0, _2cm, _2cm, _2cm / 4 ) );
				( (CoContentWrapperPageItemIF) prototype ).setContent( new CoPageItemNoContent() );
				prototype.getMutableStrokeProperties().setWidth( 1 );
				prototype.getMutableStrokeProperties().setForegroundColor( (com.bluebrim.paint.shared.CoColorIF)CoFactoryManager.createObject(com.bluebrim.paint.impl.shared.CoProcessBlackIF.PROCESS_BLACK) );
				return prototype;
			}
		}
	);
*/
/*
	m_prototypeItemCreators.put(
		ELLIPSE,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				CoShapePageItemIF prototype = new CoContentWrapperPageItem();
				prototype.setCoShape( new CoEllipseShape( 0, 0, _2cm, _2cm ) );
				( (CoContentWrapperPageItemIF) prototype ).setContent( new CoPageItemNoContent() );
				prototype.getMutableStrokeProperties().setWidth( 1 );
				prototype.getMutableStrokeProperties().setForegroundColor( (com.bluebrim.paint.shared.CoColorIF)CoFactoryManager.createObject(com.bluebrim.paint.impl.shared.CoProcessBlackIF.PROCESS_BLACK) );
				return prototype;
			}
		}
	);
*/

	m_prototypeItemCreators.put(
		LINE,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				CoShapePageItemIF prototype = new CoContentWrapperPageItem();
				prototype.setCoShape( new CoLine( 0, 0, _2cm, _2cm ) );
				( (CoContentWrapperPageItemIF) prototype ).setContent( new CoPageItemNoContent() );
				prototype.getMutableStrokeProperties().setWidth( 1 );
				prototype.getMutableStrokeProperties().setForegroundColor( (com.bluebrim.paint.shared.CoColorIF)CoFactoryManager.createObject(com.bluebrim.paint.impl.shared.CoProcessBlackIF.PROCESS_BLACK) );
				return prototype;
			}
		}
	);
	

	m_prototypeItemCreators.put(
		ORTHOGONAL_LINE,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				CoShapePageItemIF prototype = new CoContentWrapperPageItem();
				prototype.setCoShape( new CoOrthogonalLine( 0, 0, _2cm, 0 ) );
				( (CoContentWrapperPageItemIF) prototype ).setContent( new CoPageItemNoContent() );
				prototype.getMutableStrokeProperties().setWidth( 1 );
				prototype.getMutableStrokeProperties().setForegroundColor( (com.bluebrim.paint.shared.CoColorIF)CoFactoryManager.createObject(com.bluebrim.paint.impl.shared.CoProcessBlackIF.PROCESS_BLACK) );
				return prototype;
			}
		}
	);


	m_prototypeItemCreators.put(
		CURVE,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				CoShapePageItemIF prototype = new CoContentWrapperPageItem();
				prototype.setCoShape( new CoCubicBezierCurveShape() );
				( (CoContentWrapperPageItemIF) prototype ).setContent( new CoPageItemNoContent() );
				prototype.getMutableStrokeProperties().setWidth( 1 );
				prototype.getMutableStrokeProperties().setForegroundColor( (com.bluebrim.paint.shared.CoColorIF)CoFactoryManager.createObject(com.bluebrim.paint.impl.shared.CoProcessBlackIF.PROCESS_BLACK) );
				return prototype;
			}
		}
	);


	m_prototypeItemCreators.put(
		POLYGON,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				CoShapePageItemIF prototype = new CoContentWrapperPageItem();
				prototype.setCoShape( new CoPolygonShape( 5, 50.0, 50.0, false ) );
				( (CoContentWrapperPageItemIF) prototype ).setContent( new CoPageItemNoContent() );
				prototype.getMutableStrokeProperties().setWidth( 1 );
				prototype.getMutableStrokeProperties().setForegroundColor( (com.bluebrim.paint.shared.CoColorIF)CoFactoryManager.createObject(com.bluebrim.paint.impl.shared.CoProcessBlackIF.PROCESS_BLACK) );
				return prototype;
			}
		}
	);





	

	m_prototypeItemCreators.put(
		CLOSED_POLYGON_IMAGE_BOX,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				CoShapePageItemIF prototype = new CoContentWrapperPageItem();
				prototype.setCoShape( new CoPolygonShape( 5, (double)_2cm, (double)_2cm, true ) );
				prototype.getMutableStrokeProperties().setWidth( 0.5f );

				CoPageItemImageContentIF i = new CoPageItemImageContent();
				i.setImageContent( (com.bluebrim.image.shared.CoImageContentIF) null );
				( (CoContentWrapperPageItemIF) prototype ).setContent( i );
				return prototype;
			}
		}
	);
	

	m_prototypeItemCreators.put(
		TEXT_BOX,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				return CoPageItemTextContent.createContentWrapper();
			}
		}
	);

	
	m_prototypeItemCreators.put(
		IMAGE_BOX,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				CoShapePageItemIF prototype =  new CoContentWrapperPageItem();
				CoPageItemImageContentIF i = new CoPageItemImageContent();
				prototype.setCoShape( new CoRectangleShape( 0, 0, _2cm * 3, _2cm * 3 ) );
				i.setImageContent( (com.bluebrim.image.shared.CoImageContentIF) null );
				( (CoContentWrapperPageItemIF) prototype ).setContent( i );
				prototype.getMutableStrokeProperties().setWidth( 0.5f );
				return prototype;
			}
		}
	);


/*
	m_prototypeItemCreators.put(
		ROUND_PICT_BOX,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				CoShapePageItemIF tPrototype =  getPrototypeItem(PICT_BOX);
				tPrototype.setCoShape( new CoRoundedCorner( 0, 0, _2cm, _2cm, _2cm / 4 ) );
				return tPrototype;
			}
		}
	);


	m_prototypeItemCreators.put(
		ELLIPSE_PICT_BOX,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				CoShapePageItemIF tPrototype =  getPrototypeItem(PICT_BOX);
				tPrototype.setCoShape( new CoEllipseShape( 0, 0, _2cm, _2cm ) );
				return tPrototype;
			}
		}
	);
*/

	
	m_prototypeItemCreators.put(
		WORKPIECE_TEXT_BOX,
		new ItemCreator()
		{
			public CoShapePageItemIF create ()
			{
				return CoPageItemWorkPieceTextContent.createContentWrapper();
			}
		}
	);

	
	m_prototypeItemCreators.put(
		LAYOUT_BOX,
		new ItemCreator() 
		{
			public CoShapePageItemIF create ()
			{
				CoShapePageItemIF prototype =  new CoContentWrapperPageItem();
				CoPageItemLayoutContentIF i = new CoPageItemLayoutContent();
				prototype.setCoShape( new CoRectangleShape( 0, 0, _2cm * 3, _2cm * 3 ) );
				i.setLayoutContent( (com.bluebrim.layout.shared.CoLayoutContentIF) null );
				( (CoContentWrapperPageItemIF) prototype ).setContent( i );
				prototype.getMutableStrokeProperties().setWidth( 0.5f );
				return prototype;
			}
		}
	);

}
}