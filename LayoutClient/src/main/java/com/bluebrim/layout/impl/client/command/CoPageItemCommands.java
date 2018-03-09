package com.bluebrim.layout.impl.client.command;

import com.bluebrim.base.shared.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.image.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layoutmanager.*;
import com.bluebrim.page.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.stroke.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Collection of all page item commands
 * 
 * @author: Dennis
 */

public final class CoPageItemCommands {
	// reparenting
	public static final CoShapePageItemReparentCommand REPARENT_PAGE_ITEM = new CoShapePageItemReparentCommand();
	public static final CoShapePageItemsReparentCommand REPARENT_PAGE_ITEMS = new CoShapePageItemsReparentCommand();

	// geometry
	public static final CoShapePageItemSetPositionCommand SET_POSITION = new CoShapePageItemSetPositionCommand(CoCommandStringResources.getName(CoCommandConstants.SET_POSITION));
	public static final CoShapePageItemSetDimensionsCommand SET_DIMENSIONS = new CoShapePageItemSetDimensionsCommand(CoCommandStringResources.getName(CoCommandConstants.SET_DIMENSIONS));
	public static final CoShapePageItemReshapeCommand RESHAPE = new CoShapePageItemReshapeCommand(CoCommandStringResources.getName(CoCommandConstants.RESHAPE));
	public static final CoShapePageItemSetBooleanCommand SET_DO_RUN_AROUND;
	public static final CoShapePageItemSetDoubleCommand SET_ROTATION;

	// misc.
	public static final CoShapePageItemSetBooleanCommand SET_LOCATION_LOCKED;
	public static final CoShapePageItemSetBooleanCommand SET_DIMENSIONS_LOCKED;
	public static final CoShapePageItemSetBooleanCommand SET_SUPRESS_PRINTOUT;

	// layout managers
	public static final CoShapePageItemSetObjectCommand SET_LAYOUT_MANAGER;
	public static final CoShapePageItemSetDoubleCommand SET_ROW_LAYOUT_MANAGER_GAP;
	public static final CoShapePageItemSetDoubleCommand SET_RECTANGLE_LAYOUT_MANAGER_GAP;
	public static final CoShapePageItemSetObjectCommand SET_DISTANCE_CALCULATOR;

	// layout specs
	public static final CoShapePageItemSetObjectCommand SET_LOCATION_SPEC;
	public static final CoShapePageItemSetObjectCommand SET_WIDTH_SPEC;
	public static final CoShapePageItemSetObjectCommand SET_HEIGHT_SPEC;
	public static final CoShapePageItemSetBooleanCommand SET_LOCATION_SPEC_AGGRESSIVE;
	public static final CoShapePageItemSetIntegerCommand SET_CORNER_LOCATION_SPEC_INSET_X;
	public static final CoShapePageItemSetIntegerCommand SET_ABSOLUTE_WIDTH_SPEC_DISTANCE;
	public static final CoShapePageItemSetDoubleCommand SET_CONTENT_WIDTH_SPEC_ABSOLUTE_OFFSET;
	public static final CoShapePageItemSetDoubleCommand SET_CONTENT_WIDTH_SPEC_RELATIVE_OFFSET;
	public static final CoShapePageItemSetDoubleCommand SET_PROPORTIONAL_WIDTH_SPEC_PROPORTION;
	public static final CoShapePageItemSetDoubleCommand SET_ABSOLUTE_HEIGHT_SPEC_DISTANCE;
	public static final CoShapePageItemSetDoubleCommand SET_CONTENT_HEIGHT_SPEC_ABSOLUTE_OFFSET;
	public static final CoShapePageItemSetDoubleCommand SET_CONTENT_HEIGHT_SPEC_RELATIVE_OFFSET;
	public static final CoShapePageItemSetDoubleCommand SET_PROPORTIONAL_HEIGHT_SPEC_PROPORTION;

	// fill
	public static final CoShapePageItemSetObjectCommand SET_FILL_STYLE;
	public static final CoShapePageItemSetBooleanCommand SET_CYCLIC_BLEND;
	public static final CoShapePageItemSetDoubleCommand SET_FILL_SHADE;
	public static final CoShapePageItemSetDoubleCommand SET_BLEND_SHADE;
	public static final CoShapePageItemSetDoubleCommand SET_BLEND_ANGLE;
	public static final CoShapePageItemSetDoubleCommand SET_BLEND_CYCLE_LENGTH;
	public static final CoShapePageItemSetObjectCommand SET_FILL_PATTERN;
	public static final CoShapePageItemSetObjectCommand SET_FILL_COLOR;
	public static final CoShapePageItemSetObjectCommand SET_BLEND_COLOR;

	// run around
	public static final CoShapePageItemSetObjectCommand SET_RUN_AROUND_SPEC;
	public static final CoShapePageItemSetBooleanCommand SET_RUN_AROUND_USE_STROKE;
	public static final CoShapePageItemSetDoubleCommand SET_SHAPE_RUN_AROUND_MARGIN;
	public static final CoShapePageItemSetDoubleCommand SET_BOUNDS_RUN_AROUND_LEFT_MARGIN;
	public static final CoShapePageItemSetDoubleCommand SET_BOUNDS_RUN_AROUND_RIGHT_MARGIN;
	public static final CoShapePageItemSetDoubleCommand SET_BOUNDS_RUN_AROUND_TOP_MARGIN;
	public static final CoShapePageItemSetDoubleCommand SET_BOUNDS_RUN_AROUND_BOTTOM_MARGIN;

	// stroke
	public static final CoShapePageItemSetObjectCommand SET_STROKE_ALIGNMENT;
	public static final CoShapePageItemSetObjectCommand SET_STROKE_SYMMETRY;
	public static final CoShapePageItemSetObjectCommand SET_STROKE;
	public static final CoShapePageItemSetBooleanCommand SET_STROKE_EFFECTIVE_SHAPE;
	public static final CoShapePageItemSetDoubleCommand SET_STROKE_WIDTH;
	public static final CoShapePageItemSetObjectCommand SET_STROKE_FOREGROUND_COLOR;
	public static final CoShapePageItemSetDoubleCommand SET_STROKE_FOREGROUND_SHADE;
	public static final CoShapePageItemSetObjectCommand SET_STROKE_BACKGROUND_COLOR;
	public static final CoShapePageItemSetDoubleCommand SET_STROKE_BACKGROUND_SHADE;

	// column grid
	public static final CoShapePageItemSetColumnGridDerivedCommand SET_COLUMN_GRID_DERIVED = new CoShapePageItemSetColumnGridDerivedCommand(CoCommandStringResources.getName(CoCommandConstants.SET_COLUMN_GRID_DERIVED));
	public static final CoShapePageItemSetDoubleCommand SET_COLUMN_SPACING;
	public static final CoShapePageItemSetDoubleCommand SET_COLUMN_GRID_LEFT_MARGIN;
	public static final CoShapePageItemSetDoubleCommand SET_COLUMN_GRID_RIGHT_MARGIN;
	public static final CoShapePageItemSetDoubleCommand SET_COLUMN_GRID_TOP_MARGIN;
	public static final CoShapePageItemSetDoubleCommand SET_COLUMN_GRID_BOTTOM_MARGIN;
	public static final CoShapePageItemSetIntegerCommand SET_COLUMN_COUNT;
	public static final CoShapePageItemSetBooleanCommand SET_COLUMN_GRID_LEFT_OUTSIDE_SENSITIVE;
	public static final CoShapePageItemSetBooleanCommand SET_COLUMN_GRID_SPREAD;

	// base line grid
	public static final CoShapePageItemSetBaseLineGridDerivedCommand SET_BASE_LINE_GRID_DERIVED = new CoShapePageItemSetBaseLineGridDerivedCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BASE_LINE_GRID_DERIVED));
	public static final CoShapePageItemSetDoubleCommand SET_BASELINE_GRID_Y0;
	public static final CoShapePageItemSetDoubleCommand SET_BASELINE_GRID_DY;

	// content
	public static final CoShapePageItemSetObjectCommand SET_PAGE_ITEM_CONTENT;

	// text content
	public static final CoShapePageItemAdjustHeightToTextCommand ADJUST_HEIGHT_TO_TEXT = new CoShapePageItemAdjustHeightToTextCommand(CoCommandStringResources.getName(CoCommandConstants.ADJUST_HEIGHT_TO_TEXT));
	public static final CoShapePageItemChangeAcceptedTagsCommand ADD_ACCEPTED_TAGS;
	public static final CoShapePageItemChangeAcceptedTagsCommand REMOVE_ACCEPTED_TAGS;
	public static final CoShapePageItemSetBooleanCommand SET_TEXT_LOCK;
	public static final CoShapePageItemSetObjectCommand SET_VERTICAL_ALIGNMENT_TYPE;
	public static final CoShapePageItemSetObjectCommand SET_FIRST_BASE_LINE_TYPE;
	public static final CoShapePageItemSetDoubleCommand SET_TEXT_FIRST_BASE_LINE_OFFSET;
	public static final CoShapePageItemSetDoubleCommand SET_TEXT_VERTICAL_ALIGNMENT_MAX_INTERVAL;
	public static final CoShapePageItemSetDoubleCommand SET_TEXT_TOP_MARGIN;
	public static final CoShapePageItemSetDoubleCommand SET_TEXT_BOTTOM_MARGIN;
	public static final CoShapePageItemSetDoubleCommand SET_TEXT_LEFT_MARGIN;
	public static final CoShapePageItemSetDoubleCommand SET_TEXT_RIGHT_MARGIN;
	public static final CoShapePageItemSetIntegerCommand SET_TEXT_TAG;
	public static final CoShapePageItemSetObjectCommand SET_TEXT;

	// bounded content
	public static final CoShapePageItemSetDoubleCommand SET_BOUNDED_CONTENT_X;
	public static final CoShapePageItemSetDoubleCommand SET_BOUNDED_CONTENT_Y;
	public static final CoShapePageItemSetDoubleCommand SET_BOUNDED_CONTENT_SCALE_X;
	public static final CoShapePageItemSetDoubleCommand SET_BOUNDED_CONTENT_SCALE_Y;
	public static final CoShapePageItemSetBooleanCommand SET_BOUNDED_CONTENT_FLIP_X;
	public static final CoShapePageItemSetBooleanCommand SET_BOUNDED_CONTENT_FLIP_Y;
	public static final CoShapePageItemSetBooleanCommand SET_BOUNDED_CONTENT_LOCK;
	public static final CoShapePageItemSetBoundedContentPositionCommand SET_BOUNDED_CONTENT_POSITION = new CoShapePageItemSetBoundedContentPositionCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOUNDED_CONTENT_POSITION));
	public static final CoShapePageItemAddCaptionCommand ADD_CAPTION = new CoShapePageItemAddCaptionCommand(CoCommandStringResources.getName(CoCommandConstants.ADD_CAPTION));
	public static final CoShapePageItemRemoveCaptionCommand REMOVE_CAPTION = new CoShapePageItemRemoveCaptionCommand(CoCommandStringResources.getName(CoCommandConstants.REMOVE_CAPTION));
	public static final CoShapePageItemSetIntegerCommand SET_CAPTION_POSITION;
	public static final CoShapePageItemAdjustBoundedContentCommand ADJUST_BOUNDED_CONTENT = new CoShapePageItemAdjustBoundedContentCommand(CoCommandStringResources.getName(CoCommandConstants.ADJUST_BOUNDED_CONTENT));

	// image content
	public static final CoShapePageItemSetIntegerCommand SET_IMAGE_TAG;
	public static final CoShapePageItemSetObjectCommand SET_IMAGE;
	public static final CoShapePageItemSetEmbeddedPathShapeCommand SET_EMBEDDED_PATH_SHAPE = new CoShapePageItemSetEmbeddedPathShapeCommand(CoCommandStringResources.getName(CoCommandConstants.SET_EMBEDDED_PATH_SHAPE));

	// layout content
	public static final CoShapePageItemSetIntegerCommand SET_LAYOUT_TAG;
	public static final CoShapePageItemSetObjectCommand SET_LAYOUT;
	public static final CoShapePageItemSetIntegerCommand SET_RECURSIVE_LEVEL_COUNT;

	// children
	public static final CoShapePageItemSetLayoutOrderCommand SET_LAYOUT_ORDER = new CoShapePageItemSetLayoutOrderCommand(CoCommandStringResources.getName(CoCommandConstants.SET_LAYOUT_ORDER));
	public static final CoShapePageItemSetBooleanCommand SET_CHILDREN_LOCKED;
	public static final CoShapePageItemZOrderCommand BRING_FORWARD;
	public static final CoShapePageItemZOrderCommand BRING_TO_FRONT;
	public static final CoShapePageItemZOrderCommand SEND_BACKWARDS;
	public static final CoShapePageItemZOrderCommand SEND_TO_BACK;

	// shapes
	public static final CoShapePageItemReorderCurveCommand REORDER_CURVE = new CoShapePageItemReorderCurveCommand(CoCommandStringResources.getName(CoCommandConstants.REORDER_CURVE));
	public static final CoShapePageItemSetBooleanCommand SET_CURVE_CLOSED;
	public static final CoShapePageItemSetBooleanCommand SET_BOXED_LINE_HORIZONTAL;
	public static final CoShapePageItemSetDoubleCommand SET_BOXED_LINE_MARGIN;
	public static final CoShapePageItemSetDoubleCommand SET_CORNER_RADIUS;
	public static final CoShapePageItemSetShapeDoubleCommand SET_LINE_X1;
	public static final CoShapePageItemSetShapeDoubleCommand SET_LINE_Y1;
	public static final CoShapePageItemSetShapeDoubleCommand SET_LINE_X2;
	public static final CoShapePageItemSetShapeDoubleCommand SET_LINE_Y2;

	// custom grid
	public static final CoMoveCustomGridLineCommand MOVE_CUSTOM_GRID_LINE = new CoMoveCustomGridLineCommand(CoCommandStringResources.getName(CoCommandConstants.MOVE_CUSTOM_GRID_LINE));

	// layout area
	public static final CoShapePageItemSetBooleanCommand SET_ACCEPTS_WORKPIECE;
	public static final CoShapePageItemSetBooleanCommand SET_WORKPIECE_LOCK;
	public static final CoShapePageItemSetObjectCommand SET_WORKPIECE;

	// layout order
	public static final CoShapePageItemLayoutOrderCommand MOVE_TO_FIRST_LAYOUT_POSITION;
	public static final CoShapePageItemLayoutOrderCommand MOVE_TOWARDS_FIRST_LAYOUT_POSITION;
	public static final CoShapePageItemLayoutOrderCommand MOVE_TO_LAST_LAYOUT_POSITION;
	public static final CoShapePageItemLayoutOrderCommand MOVE_TOWARDS_LAST_LAYOUT_POSITION;

	// page
	public static final CoShapePageItemSetObjectCommand SET_PAGE_SIZE;
	public static final CoShapePageItemSetBooleanCommand SET_PAGE_SPREAD;

	// custom operations
	public static final CoCustomOperationCommand PERFORM_CUSTOM_OPERATION = new CoCustomOperationCommand(CoCommandStringResources.getName(CoCommandConstants.PERFORM_CUSTOM_OPERATION));

	static {
		ADD_ACCEPTED_TAGS = new CoShapePageItemChangeAcceptedTagsCommand(CoCommandStringResources.getName(CoCommandConstants.ADD_ACCEPTED_TAGS)) {
			protected void doit() {
				((CoPageItemWorkPieceTextContentView) ((CoContentWrapperPageItemView) m_targetView).getContentView()).getPageItemWorkPieceTextContent().addAcceptedTags(m_tags);
			}
		};

		REMOVE_ACCEPTED_TAGS = new CoShapePageItemChangeAcceptedTagsCommand(CoCommandStringResources.getName(CoCommandConstants.REMOVE_ACCEPTED_TAGS)) {
			protected void doit() {
				((CoPageItemWorkPieceTextContentView) ((CoContentWrapperPageItemView) m_targetView).getContentView()).getPageItemWorkPieceTextContent().removeAcceptedTags(m_tags);
			}
		};

		SET_COLUMN_GRID_LEFT_OUTSIDE_SENSITIVE = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_COLUMN_GRID_LEFT_OUTSIDE_SENSITIVE)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return targetView.getColumnGrid().isLeftOutsideSensitive();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				target.getMutableColumnGrid().setLeftOutsideSensitive(b);
			}
		};

		SET_COLUMN_GRID_SPREAD = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_COLUMN_GRID_SPREAD)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return targetView.getColumnGrid().isSpread();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				target.getMutableColumnGrid().setSpread(b);
			}
		};

		SET_BOXED_LINE_HORIZONTAL = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOXED_LINE_HORIZONTAL)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return ((CoBoxedLineShapeIF) targetView.getCoShape()).isHorizontal();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				((CoBoxedLineShapeIF) target.getMutableCoShape()).setHorizontal(b);
			}
		};

		SET_TEXT_LOCK = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_TEXT_LOCK)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return ((CoPageItemWorkPieceTextContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getTextLock() != CoPageItemBoundedContentIF.UNLOCKED;
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				CoPageItemWorkPieceTextContentIF t = (CoPageItemWorkPieceTextContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setTextLock(b ? CoPageItemBoundedContentIF.LOCKED : CoPageItemBoundedContentIF.UNLOCKED);
			}
		};

		SET_LOCATION_SPEC_AGGRESSIVE = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_LOCATION_SPEC_AGGRESSIVE)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return ((CoImmutableCornerLocationSpecIF) targetView.getLocationSpec()).isAggressive();
			}
			public void setBoolean(CoShapePageItemIF target, boolean a) {
				((CoCornerLocationSpecIF) target.getMutableLocationSpec()).setAggressive(a);
			}
		};

		SET_SUPRESS_PRINTOUT = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_SUPRESS_PRINTOUT)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return targetView.getSupressPrintout();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				target.setSupressPrintout(b);
			}
		};

		SET_LOCATION_LOCKED = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_LOCATION_LOCKED)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return targetView.isLocationLocked();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				target.setLocationLocked(b);
			}
		};

		SET_DIMENSIONS_LOCKED = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_DIMENSIONS_LOCKED)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return targetView.areDimensionsLocked();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				target.setDimensionsLocked(b);
			}
		};

		SET_CHILDREN_LOCKED = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_CHILDREN_LOCKED)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return ((CoCompositePageItemView) targetView).areChildrenLocked();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				((CoCompositePageItemIF) target).setChildrenLocked(b);
			}
		};

		SET_STROKE_EFFECTIVE_SHAPE = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_STROKE_EFFECTIVE_SHAPE)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return targetView.getStrokeEffectiveShape();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				target.setStrokeEffectiveShape(b);
			}
		};

		SET_RUN_AROUND_USE_STROKE = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_RUN_AROUND_USE_STROKE)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return targetView.getRunAroundSpec().doUseStroke();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				target.getMutableRunAroundSpec().setUseStroke(b);
			}
		};

		SET_WORKPIECE_LOCK = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_WORKPIECE_LOCK)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return ((CoLayoutAreaView) targetView).getWorkPieceLock() == CoLayoutAreaIF.LOCKED;
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				((CoLayoutAreaIF) target).setWorkPieceLock(b ? CoLayoutAreaIF.LOCKED : CoLayoutAreaIF.UNLOCKED);
			}
		};

		SET_ACCEPTS_WORKPIECE = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_ACCEPTS_WORKPIECE)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return ((CoLayoutAreaView) targetView).acceptsWorkPiece();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				((CoLayoutAreaIF) target).setAcceptsWorkPiece(b);
			}
		};

		SET_CYCLIC_BLEND = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_CYCLIC_BLEND)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return ((CoImmutableGradientFillIF) targetView.getFillStyle()).getCyclic();
			}
			public void setBoolean(CoShapePageItemIF target, boolean cyclic) {
				((CoGradientFillIF) target.getMutableFillStyle()).setCyclic(cyclic);
			}
		};

		SET_CURVE_CLOSED = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_CURVE_CLOSED)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return ((CoCurveShapeIF) targetView.getCoShape()).isClosed();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				((CoCurveShapeIF) target.getMutableCoShape()).setClosed(b);
			}
		};

		SET_BOUNDED_CONTENT_LOCK = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOUNDED_CONTENT_LOCK)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return ((CoPageItemBoundedContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getContentLock() != CoPageItemBoundedContentIF.UNLOCKED;
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				CoPageItemBoundedContentIF t = (CoPageItemBoundedContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setContentLock(b ? CoPageItemBoundedContentIF.LOCKED : CoPageItemBoundedContentIF.UNLOCKED);
			}
		};

		SET_BOUNDED_CONTENT_FLIP_X = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOUNDED_CONTENT_FLIP_X)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return ((CoPageItemBoundedContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getFlipX();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				CoPageItemBoundedContentIF t = (CoPageItemBoundedContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setFlipX(b);
			}
		};

		SET_BOUNDED_CONTENT_FLIP_Y = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOUNDED_CONTENT_FLIP_Y)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return ((CoPageItemBoundedContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getFlipY();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				CoPageItemBoundedContentIF t = (CoPageItemBoundedContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setFlipY(b);
			}
		};

		SET_DO_RUN_AROUND = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_DO_RUN_AROUND)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return targetView.getDoRunAround();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				target.setDoRunAround(b);
			}
		};
	}

	static {
		MOVE_TO_FIRST_LAYOUT_POSITION = new CoShapePageItemLayoutOrderCommand(CoCommandStringResources.getName(CoCommandConstants.MOVE_TO_FIRST_LAYOUT_POSITION)) {
			public int setLayoutOrder() {
				return m_targetView.getShapePageItem().moveToFirstLayoutPosition();
			}
		};

		MOVE_TOWARDS_FIRST_LAYOUT_POSITION = new CoShapePageItemLayoutOrderCommand(CoCommandStringResources.getName(CoCommandConstants.MOVE_TOWARDS_FIRST_LAYOUT_POSITION)) {
			public int setLayoutOrder() {
				return m_targetView.getShapePageItem().moveTowardsFirstLayoutPosition();
			}
		};

		MOVE_TO_LAST_LAYOUT_POSITION = new CoShapePageItemLayoutOrderCommand(CoCommandStringResources.getName(CoCommandConstants.MOVE_TO_LAST_LAYOUT_POSITION)) {
			public int setLayoutOrder() {
				return m_targetView.getShapePageItem().moveToLastLayoutPosition();
			}
		};

		MOVE_TOWARDS_LAST_LAYOUT_POSITION = new CoShapePageItemLayoutOrderCommand(CoCommandStringResources.getName(CoCommandConstants.MOVE_TOWARDS_LAST_LAYOUT_POSITION)) {
			public int setLayoutOrder() {
				return m_targetView.getShapePageItem().moveTowardsLastLayoutPosition();
			}
		};

		SET_RECTANGLE_LAYOUT_MANAGER_GAP = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_RECTANGLE_LAYOUT_MANAGER_GAP)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoRectangleLayoutManagerIF) ((CoCompositePageItemView) targetView).getLayoutManager()).getGap();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoRectangleLayoutManagerIF) ((CoCompositePageItemIF) target).getMutableLayoutManager()).setGap(d);
			}
		};

		SET_ROW_LAYOUT_MANAGER_GAP = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_ROW_LAYOUT_MANAGER_GAP)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoRowLayoutManagerIF) ((CoCompositePageItemView) targetView).getLayoutManager()).getGap();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoRowLayoutManagerIF) ((CoCompositePageItemIF) target).getMutableLayoutManager()).setGap(d);
			}
		};

		SET_ABSOLUTE_HEIGHT_SPEC_DISTANCE = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_ABSOLUTE_HEIGHT_SPEC_DISTANCE)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableAbsoluteSizeSpecIF) targetView.getHeightSpec()).getDistance();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoAbsoluteSizeSpecIF) target.getMutableHeightSpec()).setDistance(d);
			}
		};

		SET_CONTENT_HEIGHT_SPEC_ABSOLUTE_OFFSET = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_CONTENT_HEIGHT_SPEC_ABSOLUTE_OFFSET)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableContentSizeIF) targetView.getHeightSpec()).getAbsoluteOffset();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoContentSizeIF) target.getMutableHeightSpec()).setAbsoluteOffset(d);
			}
		};

		SET_CONTENT_HEIGHT_SPEC_RELATIVE_OFFSET = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_CONTENT_HEIGHT_SPEC_RELATIVE_OFFSET)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableContentSizeIF) targetView.getHeightSpec()).getRelativeOffset();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoContentSizeIF) target.getMutableHeightSpec()).setRelativeOffset(d);
			}
		};

		SET_PROPORTIONAL_HEIGHT_SPEC_PROPORTION = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_PROPORTIONAL_HEIGHT_SPEC_PROPORTION)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableProportionalSizeSpecIF) targetView.getHeightSpec()).getProportion();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoProportionalSizeSpecIF) target.getMutableHeightSpec()).setProportion(d);
			}
		};

		SET_CONTENT_WIDTH_SPEC_ABSOLUTE_OFFSET = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_CONTENT_WIDTH_SPEC_ABSOLUTE_OFFSET)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableContentSizeIF) targetView.getWidthSpec()).getAbsoluteOffset();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoContentSizeIF) target.getMutableWidthSpec()).setAbsoluteOffset(d);
			}
		};

		SET_CONTENT_WIDTH_SPEC_RELATIVE_OFFSET = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_CONTENT_WIDTH_SPEC_RELATIVE_OFFSET)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableContentSizeIF) targetView.getWidthSpec()).getRelativeOffset();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoContentSizeIF) target.getMutableWidthSpec()).setRelativeOffset(d);
			}
		};

		SET_PROPORTIONAL_WIDTH_SPEC_PROPORTION = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_PROPORTIONAL_WIDTH_SPEC_PROPORTION)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableProportionalSizeSpecIF) targetView.getWidthSpec()).getProportion();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoProportionalSizeSpecIF) target.getMutableWidthSpec()).setProportion(d);
			}
		};

		SET_TEXT_FIRST_BASE_LINE_OFFSET = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_TEXT_FIRST_BASE_LINE_OFFSET)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoPageItemAbstractTextContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getFirstBaselineOffset();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoPageItemAbstractTextContentIF) ((CoContentWrapperPageItemIF) target).getContent()).setFirstBaselineOffset((float) d);
			}
		};

		SET_TEXT_VERTICAL_ALIGNMENT_MAX_INTERVAL = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_TEXT_VERTICAL_ALIGNMENT_MAX_INTERVAL)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoPageItemAbstractTextContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getVerticalAlignmentMaxInter();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoPageItemAbstractTextContentIF) ((CoContentWrapperPageItemIF) target).getContent()).setVerticalAlignmentMaxInter((float) d);
			}
		};

		SET_TEXT_TOP_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_TEXT_TOP_MARGIN)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoPageItemAbstractTextContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getTopMargin();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoPageItemAbstractTextContentIF) ((CoContentWrapperPageItemIF) target).getContent()).setTopMargin((float) d);
			}
		};

		SET_TEXT_BOTTOM_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_TEXT_BOTTOM_MARGIN)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoPageItemAbstractTextContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getBottomMargin();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoPageItemAbstractTextContentIF) ((CoContentWrapperPageItemIF) target).getContent()).setBottomMargin((float) d);
			}
		};

		SET_TEXT_LEFT_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_TEXT_LEFT_MARGIN)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoPageItemAbstractTextContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getLeftMargin();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoPageItemAbstractTextContentIF) ((CoContentWrapperPageItemIF) target).getContent()).setLeftMargin((float) d);
			}
		};

		SET_TEXT_RIGHT_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_TEXT_RIGHT_MARGIN)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoPageItemAbstractTextContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getRightMargin();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoPageItemAbstractTextContentIF) ((CoContentWrapperPageItemIF) target).getContent()).setRightMargin((float) d);
			}
		};

		SET_STROKE_WIDTH = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_STROKE_WIDTH)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableStrokePropertiesIF) targetView.getStrokeProperties()).getWidth();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				CoStrokePropertiesIF s = (CoStrokePropertiesIF) target.getMutableStrokeProperties();
				s.setWidth((float) d);
			}
		};

		SET_STROKE_FOREGROUND_SHADE = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_STROKE_FOREGROUND_SHADE)) {
			public void setDouble(CoShapePageItemIF target, double shade) {
				((CoStrokePropertiesIF) target.getMutableStrokeProperties()).setForegroundShade((float) shade);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return (float) ((CoImmutableStrokePropertiesIF) targetView.getStrokeProperties()).getForegroundShade();
			}
		};

		SET_STROKE_BACKGROUND_SHADE = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_STROKE_BACKGROUND_SHADE)) {
			public void setDouble(CoShapePageItemIF target, double shade) {
				((CoStrokePropertiesIF) target.getMutableStrokeProperties()).setBackgroundShade((float) shade);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return (float) ((CoImmutableStrokePropertiesIF) targetView.getStrokeProperties()).getBackgroundShade();
			}
		};

		SET_SHAPE_RUN_AROUND_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_SHAPE_RUN_AROUND_MARGIN)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableShapeRunAroundSpecIF) targetView.getRunAroundSpec()).getMargin();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				CoShapeRunAroundSpecIF s = (CoShapeRunAroundSpecIF) target.getMutableRunAroundSpec();
				s.setMargin(d);
			}
		};

		SET_BOUNDS_RUN_AROUND_LEFT_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOUNDS_RUN_AROUND_LEFT_MARGIN)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableBoundingBoxRunAroundSpecIF) targetView.getRunAroundSpec()).getLeft();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				CoBoundingBoxRunAroundSpecIF s = (CoBoundingBoxRunAroundSpecIF) target.getMutableRunAroundSpec();
				s.setLeft(d);
			}
		};

		SET_BOUNDS_RUN_AROUND_RIGHT_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOUNDS_RUN_AROUND_RIGHT_MARGIN)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableBoundingBoxRunAroundSpecIF) targetView.getRunAroundSpec()).getRight();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				CoBoundingBoxRunAroundSpecIF s = (CoBoundingBoxRunAroundSpecIF) target.getMutableRunAroundSpec();
				s.setRight(d);
			}
		};

		SET_BOUNDS_RUN_AROUND_TOP_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOUNDS_RUN_AROUND_TOP_MARGIN)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableBoundingBoxRunAroundSpecIF) targetView.getRunAroundSpec()).getTop();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				CoBoundingBoxRunAroundSpecIF s = (CoBoundingBoxRunAroundSpecIF) target.getMutableRunAroundSpec();
				s.setTop(d);
			}
		};

		SET_BOUNDS_RUN_AROUND_BOTTOM_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOUNDS_RUN_AROUND_BOTTOM_MARGIN)) {
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableBoundingBoxRunAroundSpecIF) targetView.getRunAroundSpec()).getBottom();
			}
			public void setDouble(CoShapePageItemIF target, double d) {
				CoBoundingBoxRunAroundSpecIF s = (CoBoundingBoxRunAroundSpecIF) target.getMutableRunAroundSpec();
				s.setBottom(d);
			}
		};

		SET_BLEND_CYCLE_LENGTH = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BLEND_CYCLE_LENGTH)) {
			public void setDouble(CoShapePageItemIF target, double a) {
				((CoGradientFillIF) target.getMutableFillStyle()).setBlendLength(a);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableGradientFillIF) targetView.getFillStyle()).getBlendLength();
			}
		};

		SET_BLEND_ANGLE = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BLEND_ANGLE)) {
			public void setDouble(CoShapePageItemIF target, double a) {
				((CoGradientFillIF) target.getMutableFillStyle()).setAngle(a);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableGradientFillIF) targetView.getFillStyle()).getAngle();
			}
		};

		SET_FILL_SHADE = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_FILL_SHADE)) {
			public void setDouble(CoShapePageItemIF target, double shade) {
				((CoGradientFillIF) target.getMutableFillStyle()).setShade1((float) shade);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return (float) ((CoImmutableGradientFillIF) targetView.getFillStyle()).getShade1();
			}
		};

		SET_BLEND_SHADE = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BLEND_SHADE)) {
			public void setDouble(CoShapePageItemIF target, double shade) {
				((CoGradientFillIF) target.getMutableFillStyle()).setShade2((float) shade);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return (float) ((CoImmutableGradientFillIF) targetView.getFillStyle()).getShade2();
			}
		};

		SET_BOUNDED_CONTENT_X = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOUNDED_CONTENT_X)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				CoPageItemBoundedContentIF t = (CoPageItemBoundedContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setScaleAndPosition(Double.NaN, Double.NaN, d, Double.NaN);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoPageItemBoundedContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getX();
			}
		};

		SET_BOUNDED_CONTENT_Y = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOUNDED_CONTENT_Y)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				CoPageItemBoundedContentIF t = (CoPageItemBoundedContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setScaleAndPosition(Double.NaN, Double.NaN, Double.NaN, d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoPageItemBoundedContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getY();
			}
		};

		SET_BOUNDED_CONTENT_SCALE_X = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOUNDED_CONTENT_SCALE_X)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				CoPageItemBoundedContentIF t = (CoPageItemBoundedContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setScaleAndPosition(d, Double.NaN, Double.NaN, Double.NaN);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoPageItemBoundedContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getScaleX();
			}
		};

		SET_BOUNDED_CONTENT_SCALE_Y = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOUNDED_CONTENT_SCALE_Y)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				CoPageItemBoundedContentIF t = (CoPageItemBoundedContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setScaleAndPosition(Double.NaN, d, Double.NaN, Double.NaN);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoPageItemBoundedContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getScaleY();
			}
		};

		SET_CORNER_RADIUS = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_CORNER_RADIUS)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoCornerRectangleIF) target.getMutableCoShape()).setCornerRadius(d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoCornerRectangleIF) targetView.getCoShape()).getCornerRadius();
			}
		};

		SET_BOXED_LINE_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BOXED_LINE_MARGIN)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoBoxedLineShapeIF) target.getMutableCoShape()).setMargin(d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoBoxedLineShapeIF) targetView.getCoShape()).getMargin();
			}
		};

		SET_ROTATION = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_ROTATION)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				target.getMutableTransform().setRotation(d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return targetView.getTransform().getRotation();
			}
		};

		SET_COLUMN_SPACING = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_COLUMN_SPACING)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				target.getMutableColumnGrid().setColumnSpacing(d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return targetView.getColumnGrid().getColumnSpacing();
			}
		};

		SET_COLUMN_GRID_LEFT_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_COLUMN_GRID_LEFT_MARGIN)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				target.getMutableColumnGrid().setLeftMargin(d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return targetView.getColumnGrid().getLeftMargin();
			}
		};

		SET_COLUMN_GRID_RIGHT_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_COLUMN_GRID_RIGHT_MARGIN)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				target.getMutableColumnGrid().setRightMargin(d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return targetView.getColumnGrid().getRightMargin();
			}
		};

		SET_COLUMN_GRID_TOP_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_COLUMN_GRID_TOP_MARGIN)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				target.getMutableColumnGrid().setTopMargin(d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return targetView.getColumnGrid().getTopMargin();
			}
		};

		SET_COLUMN_GRID_BOTTOM_MARGIN = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_COLUMN_GRID_BOTTOM_MARGIN)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				target.getMutableColumnGrid().setBottomMargin(d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return targetView.getColumnGrid().getBottomMargin();
			}
		};

		SET_BASELINE_GRID_Y0 = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BASELINE_GRID_Y0)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				target.getMutableBaseLineGrid().setY0(d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return targetView.getBaseLineGrid().getY0();
			}
		};

		SET_BASELINE_GRID_DY = new CoShapePageItemSetDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BASELINE_GRID_DY)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				target.getMutableBaseLineGrid().setDeltaY(d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return targetView.getBaseLineGrid().getDeltaY();
			}
		};

		SET_LINE_X1 = new CoShapePageItemSetShapeDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_LINE_X1)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoLineIF) target.getMutableCoShape()).setX1(d - target.getX());
			}
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableLineIF) targetView.getCoShape()).getX1() + targetView.getX();
			}
		};

		SET_LINE_Y1 = new CoShapePageItemSetShapeDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_LINE_Y1)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoLineIF) target.getMutableCoShape()).setY1(d - target.getY());
			}
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableLineIF) targetView.getCoShape()).getY1() + targetView.getY();
			}
		};

		SET_LINE_X2 = new CoShapePageItemSetShapeDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_LINE_X2)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoLineIF) target.getMutableCoShape()).setX2(d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableLineIF) targetView.getCoShape()).getX2();
			}
		};

		SET_LINE_Y2 = new CoShapePageItemSetShapeDoubleCommand(CoCommandStringResources.getName(CoCommandConstants.SET_LINE_Y2)) {
			public void setDouble(CoShapePageItemIF target, double d) {
				((CoLineIF) target.getMutableCoShape()).setY2(d);
			}
			public double getDouble(CoShapePageItemView targetView) {
				return ((CoImmutableLineIF) targetView.getCoShape()).getY2();
			}
		};

		SET_CAPTION_POSITION = new CoShapePageItemSetIntegerCommand(CoCommandStringResources.getName(CoCommandConstants.SET_CAPTION_POSITION)) {
			public int getInteger(CoShapePageItemView targetView) {
				return targetView.getSlavePosition();
			}
			public void setInteger(CoShapePageItemIF target, int i) {
				CoPageItemBoundedContentIF t = (CoPageItemBoundedContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setCaptionPosition(i);
			}
		};

		SET_ABSOLUTE_WIDTH_SPEC_DISTANCE = new CoShapePageItemSetIntegerCommand(CoCommandStringResources.getName(CoCommandConstants.SET_ABSOLUTE_WIDTH_SPEC_DISTANCE)) {
			public int getInteger(CoShapePageItemView targetView) {
				return (int) ((CoImmutableAbsoluteSizeSpecIF) targetView.getWidthSpec()).getDistance();
			}
			public void setInteger(CoShapePageItemIF target, int i) {
				((CoAbsoluteSizeSpecIF) target.getMutableWidthSpec()).setDistance(i);
			}
		};

		SET_CORNER_LOCATION_SPEC_INSET_X = new CoShapePageItemSetIntegerCommand(CoCommandStringResources.getName(CoCommandConstants.SET_CORNER_LOCATION_SPEC_INSET_X)) {
			public int getInteger(CoShapePageItemView targetView) {
				return ((CoImmutableCornerLocationSpecIF) targetView.getLocationSpec()).getXInset();
			}
			public void setInteger(CoShapePageItemIF target, int i) {
				((CoCornerLocationSpecIF) target.getMutableLocationSpec()).setXInset(i);
			}
		};

		SET_TEXT_TAG = new CoShapePageItemSetIntegerCommand(CoCommandStringResources.getName(CoCommandConstants.SET_TEXT_TAG)) {
			public int getInteger(CoShapePageItemView targetView) {
				return ((CoPageItemWorkPieceTextContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getOrderTag();
			}
			public void setInteger(CoShapePageItemIF target, int i) {
				CoPageItemWorkPieceTextContentIF t = (CoPageItemWorkPieceTextContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setOrderTag(i);
			}
		};

		SET_LAYOUT_TAG = new CoShapePageItemSetIntegerCommand(CoCommandStringResources.getName(CoCommandConstants.SET_LAYOUT_TAG)) {
			public int getInteger(CoShapePageItemView targetView) {
				return ((CoPageItemLayoutContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getOrderTag();
			}
			public void setInteger(CoShapePageItemIF target, int i) {
				CoPageItemLayoutContentIF t = (CoPageItemLayoutContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setOrderTag(i);
			}
		};

		SET_RECURSIVE_LEVEL_COUNT = new CoShapePageItemSetIntegerCommand(CoCommandStringResources.getName(CoCommandConstants.SET_RECURSIVE_LEVEL_COUNT)) {
			public int getInteger(CoShapePageItemView targetView) {
				return ((CoPageItemLayoutContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getRecursiveLevelMaxCount();
			}
			public void setInteger(CoShapePageItemIF target, int i) {
				CoPageItemLayoutContentIF t = (CoPageItemLayoutContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setRecursiveLevelMaxCount(i);
			}
		};

		SET_IMAGE_TAG = new CoShapePageItemSetIntegerCommand(CoCommandStringResources.getName(CoCommandConstants.SET_IMAGE_TAG)) {
			public int getInteger(CoShapePageItemView targetView) {
				return ((CoPageItemImageContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getOrderTag();
			}
			public void setInteger(CoShapePageItemIF target, int i) {
				CoPageItemImageContentIF t = (CoPageItemImageContentIF) ((CoContentWrapperPageItemIF) target).getContent();
				t.setOrderTag(i);
			}
		};

		SET_COLUMN_COUNT = new CoShapePageItemSetIntegerCommand(CoCommandStringResources.getName(CoCommandConstants.SET_COLUMN_COUNT)) {
			public int getInteger(CoShapePageItemView targetView) {
				return targetView.getColumnGrid().getColumnCount();
			}
			public void setInteger(CoShapePageItemIF target, int i) {
				target.getMutableColumnGrid().setColumnCount(i);
			}
		};


		SET_DISTANCE_CALCULATOR = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_DISTANCE_CALCULATOR)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoRectangleLayoutManagerIF) ((CoCompositePageItemView) targetView).getLayoutManager()).getDistanceCalculator().getName();
			}
			public void setObject(CoShapePageItemIF target, Object o) {
				CoRectangleLayoutManagerIF reIF = (CoRectangleLayoutManagerIF) ((CoCompositePageItemIF) target).getMutableLayoutManager();
				CoCalculateDistanceIF dist = reIF.createDistanceCalculator((String) o);
				reIF.setDistanceCalculator(dist);
			}
		};

		SET_PAGE_SIZE = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_PAGE_SIZE)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoPageLayoutAreaView) targetView).getPageSize();
			}
			public void setObject(CoShapePageItemIF target, Object pageSize) {
				((CoPageLayoutAreaIF) target).setPageSize((CoPageSizeIF) pageSize);
			}
		};

		SET_PAGE_SPREAD = new CoShapePageItemSetBooleanCommand(CoCommandStringResources.getName(CoCommandConstants.SET_PAGE_SPREAD)) {
			public boolean getBoolean(CoShapePageItemView targetView) {
				return ((CoPageLayoutAreaView) targetView).isSpread();
			}
			public void setBoolean(CoShapePageItemIF target, boolean b) {
				((CoPageLayoutAreaIF) target).setSpread(b);
			}
		};

		SET_WIDTH_SPEC = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_WIDTH_SPEC)) {
			public Object getObject(CoShapePageItemView targetView) {
				return targetView.getWidthSpec();
			}
			public void setObject(CoShapePageItemIF target, Object ss) {
				target.setLayoutSpecs(null, (CoImmutableSizeSpecIF) ss, null);
			}

			protected CoUndoCommand createUndoCommand(String name, CoShapePageItemView targetView, CoShapePageItemSetObjectCommand command, Object originalObject, Object newObject) {
				return new CoShapePageItemSetLayoutSpecUndoCommand(name, targetView.getShapePageItem(), command, originalObject, newObject);
			}
		};

		SET_HEIGHT_SPEC = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_HEIGHT_SPEC)) {
			public Object getObject(CoShapePageItemView targetView) {
				return targetView.getHeightSpec();
			}
			public void setObject(CoShapePageItemIF target, Object ss) {
				target.setLayoutSpecs(null, null, (CoImmutableSizeSpecIF) ss);
			}

			protected CoUndoCommand createUndoCommand(String name, CoShapePageItemView targetView, CoShapePageItemSetObjectCommand command, Object originalObject, Object newObject) {
				return new CoShapePageItemSetLayoutSpecUndoCommand(name, targetView.getShapePageItem(), command, originalObject, newObject);
			}
		};

		SET_LOCATION_SPEC = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_LOCATION_SPEC)) {
			public Object getObject(CoShapePageItemView targetView) {
				return targetView.getLocationSpec();
			}
			public void setObject(CoShapePageItemIF target, Object ls) {
				target.setLayoutSpecs((CoImmutableLocationSpecIF) ls, null, null);
			}

			protected CoUndoCommand createUndoCommand(String name, CoShapePageItemView targetView, CoShapePageItemSetObjectCommand command, Object originalObject, Object newObject) {
				return new CoShapePageItemSetLayoutSpecUndoCommand(name, targetView.getShapePageItem(), command, originalObject, newObject);
			}
		};

		SET_STROKE_ALIGNMENT = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_STROKE_ALIGNMENT)) {
			public Object getObject(CoShapePageItemView targetView) {
				return new Integer(((CoImmutableStrokePropertiesIF) targetView.getStrokeProperties()).getAlignment());
			}
			public void setObject(CoShapePageItemIF target, Object a) {
				CoStrokePropertiesIF s = (CoStrokePropertiesIF) target.getMutableStrokeProperties();
				s.setAlignment(((Integer) a).intValue());
			}
		};

		SET_VERTICAL_ALIGNMENT_TYPE = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_VERTICAL_ALIGNMENT_TYPE)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoPageItemAbstractTextContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getVerticalAlignmentType();
			}
			public void setObject(CoShapePageItemIF target, Object o) {
				((CoPageItemAbstractTextContentIF) ((CoContentWrapperPageItemIF) target).getContent()).setVerticalAlignmentType((String) o);
			}
		};

		SET_FIRST_BASE_LINE_TYPE = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_FIRST_BASE_LINE_TYPE)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoPageItemAbstractTextContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getFirstBaselineType();
			}
			public void setObject(CoShapePageItemIF target, Object o) {
				((CoPageItemAbstractTextContentIF) ((CoContentWrapperPageItemIF) target).getContent()).setFirstBaselineType((String) o);
			}
		};

		SET_STROKE_SYMMETRY = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_STROKE_SYMMETRY)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoImmutableStrokePropertiesIF) targetView.getStrokeProperties()).getSymmetry();
			}
			public void setObject(CoShapePageItemIF target, Object sym) {
				CoStrokePropertiesIF s = (CoStrokePropertiesIF) target.getMutableStrokeProperties();
				s.setSymmetry((String) sym);
			}
		};

		SET_STROKE = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_STROKE)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoImmutableStrokePropertiesIF) targetView.getStrokeProperties()).getStroke();
			}
			public void setObject(CoShapePageItemIF target, Object stroke) {
				CoStrokePropertiesIF s = (CoStrokePropertiesIF) target.getMutableStrokeProperties();
				s.setStroke((CoStrokeIF) stroke);
			}
		};

		SET_RUN_AROUND_SPEC = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_RUN_AROUND_SPEC)) {
			public Object getObject(CoShapePageItemView targetView) {
				return targetView.getRunAroundSpec();
			}
			public void setObject(CoShapePageItemIF target, Object fillStyle) {
				target.setRunAroundSpec((CoRunAroundSpecIF) fillStyle);
			}
		};

		SET_FILL_PATTERN = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_FILL_PATTERN)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoImmutablePatternFillStyleIF) targetView.getFillStyle()).getPattern();
			}
			public void setObject(CoShapePageItemIF target, Object pattern) {
				((CoPatternFillStyleIF) target.getMutableFillStyle()).setPattern((String) pattern);
			}
		};

		SET_LAYOUT_MANAGER = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_LAYOUT_MANAGER)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoCompositePageItemView) targetView).getLayoutManager();
			}
			public void setObject(CoShapePageItemIF target, Object lm) {
				((CoCompositePageItemIF) target).setLayoutManager((CoLayoutManagerIF) lm);
			}
		};
		/*
				SET_SHAPE =
					new CoShapePageItemSetObjectCommand( CoCommandStringResources.getName( "SET_SHAPE" ) )
					{
						public Object getObject( CoShapePageItemView targetView )
						{
							return targetView.getCoShape();
						}
						public void setObject( CoShapePageItemIF target, Object shape )
						{
							target.setCoShape( (CoImmutableShapeIF) shape );
						}
					};
		*/
		SET_FILL_STYLE = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_FILL_STYLE)) {
			public Object getObject(CoShapePageItemView targetView) {
				return targetView.getFillStyle();
			}
			public void setObject(CoShapePageItemIF target, Object fillStyle) {
				target.setFillStyle((CoFillStyleIF) fillStyle);
			}
		};

		SET_PAGE_ITEM_CONTENT = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_PAGE_ITEM_CONTENT)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoContentWrapperPageItemView) targetView).getContentView().getPageItemContent();
			}
			public void setObject(CoShapePageItemIF target, Object content) {
				((CoContentWrapperPageItemIF) target).setContent((CoPageItemContentIF) content);
			}
		};

		SET_WORKPIECE = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_WORKPIECE)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoLayoutAreaView) targetView).getLayoutArea().getWorkPiece();
			}

			public void setObject(CoShapePageItemIF target, Object o) {
				((CoLayoutAreaIF) target).setWorkPiece((CoWorkPieceIF) o);
			}
		};


		SET_IMAGE = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_IMAGE)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoPageItemImageContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getImageContent();
			}

			public void setObject(CoShapePageItemIF target, Object o) {
				((CoPageItemImageContentIF) ((CoContentWrapperPageItemIF) target).getContent()).setImageContent((CoImageContentIF) o);
			}
		};

		SET_LAYOUT = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_LAYOUT)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoPageItemLayoutContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getLayoutContent();
			}

			public void setObject(CoShapePageItemIF target, Object o) {
				((CoPageItemLayoutContentIF) ((CoContentWrapperPageItemIF) target).getContent()).setLayoutContent((CoLayoutContentIF) o);
			}
		};

		SET_TEXT = new CoShapePageItemSetObjectCommand(CoCommandStringResources.getName(CoCommandConstants.SET_TEXT)) {
			public Object getObject(CoShapePageItemView targetView) {
				return ((CoPageItemTextContentView) ((CoContentWrapperPageItemView) targetView).getContentView()).getPageItemTextContent().getFormattedTextHolder();
			}

			public void setObject(CoShapePageItemIF target, Object o) {
				((CoPageItemTextContentIF) ((CoContentWrapperPageItemIF) target).getContent()).setFormattedTextHolder((CoFormattedTextHolderIF) o);
			}
		};

		SET_STROKE_FOREGROUND_COLOR = new CoShapePageItemSetColorCommand(CoCommandStringResources.getName(CoCommandConstants.SET_STROKE_FOREGROUND_COLOR)) {
			public CoColorIF getColor(CoShapePageItemView targetView) {
				return ((CoImmutableStrokePropertiesIF) targetView.getStrokeProperties()).getForegroundColor();
			}
			public void setColor(CoShapePageItemIF target, CoColorIF color) {
				((CoStrokePropertiesIF) target.getMutableStrokeProperties()).setForegroundColor(color);
			}
		};

		SET_STROKE_BACKGROUND_COLOR = new CoShapePageItemSetColorCommand(CoCommandStringResources.getName(CoCommandConstants.SET_STROKE_BACKGROUND_COLOR)) {
			public CoColorIF getColor(CoShapePageItemView targetView) {
				return ((CoImmutableStrokePropertiesIF) targetView.getStrokeProperties()).getBackgroundColor();
			}
			public void setColor(CoShapePageItemIF target, CoColorIF color) {
				((CoStrokePropertiesIF) target.getMutableStrokeProperties()).setBackgroundColor(color);
			}
		};

		SET_FILL_COLOR = new CoShapePageItemSetColorCommand(CoCommandStringResources.getName(CoCommandConstants.SET_FILL_COLOR)) {
			public CoColorIF getColor(CoShapePageItemView targetView) {
				return ((CoImmutableGradientFillIF) targetView.getFillStyle()).getColor1();
			}
			public void setColor(CoShapePageItemIF target, CoColorIF color) {
				((CoGradientFillIF) target.getMutableFillStyle()).setColor1(color);
			}
		};

		SET_BLEND_COLOR = new CoShapePageItemSetColorCommand(CoCommandStringResources.getName(CoCommandConstants.SET_BLEND_COLOR)) {
			public CoColorIF getColor(CoShapePageItemView targetView) {
				return ((CoImmutableGradientFillIF) targetView.getFillStyle()).getColor2();
			}
			public void setColor(CoShapePageItemIF target, CoColorIF color) {
				((CoGradientFillIF) target.getMutableFillStyle()).setColor2(color);
			}
		};

		BRING_FORWARD = new CoShapePageItemZOrderCommand(CoCommandStringResources.getName(CoCommandConstants.BRING_FORWARD)) {
			public int setZOrder() {
				return m_targetView.getShapePageItem().bringForward();
			}
		};

		BRING_TO_FRONT = new CoShapePageItemZOrderCommand(CoCommandStringResources.getName(CoCommandConstants.BRING_TO_FRONT)) {
			public int setZOrder() {
				return m_targetView.getShapePageItem().bringToFront();
			}
		};

		SEND_BACKWARDS = new CoShapePageItemZOrderCommand(CoCommandStringResources.getName(CoCommandConstants.SEND_BACKWARDS)) {
			public int setZOrder() {
				return m_targetView.getShapePageItem().sendBackwards();
			}
		};

		SEND_TO_BACK = new CoShapePageItemZOrderCommand(CoCommandStringResources.getName(CoCommandConstants.SEND_TO_BACK)) {
			public int setZOrder() {
				return m_targetView.getShapePageItem().sendToBack();
			}
		};

	}

	private CoPageItemCommands() {
	}
}