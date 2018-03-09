package com.bluebrim.layout.impl.server;
import com.bluebrim.base.shared.CoFactoryElementIF;
import com.bluebrim.base.shared.CoFactoryIF;
import com.bluebrim.base.shared.CoFactoryManager;
import com.bluebrim.base.shared.CoShapeFactoryIF;
import com.bluebrim.base.shared.CoSingletonFactory;
import com.bluebrim.base.shared.geom.CoShapeFactory;
import com.bluebrim.extensibility.server.CoFactoryInitSPI;
import com.bluebrim.layout.impl.server.manager.CoLayoutManagerFactory;
import com.bluebrim.layout.impl.server.manager.CoLayoutSpecFactory;
import com.bluebrim.layout.impl.shared.CoGradientFillIF;
import com.bluebrim.layout.impl.shared.CoPageItemFactoryIF;
import com.bluebrim.layout.impl.shared.CoPageItemPrototypeIF;
import com.bluebrim.layout.impl.shared.CoPatternFillStyleIF;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.paint.impl.shared.CoExtendedMultiInkColor;
import com.bluebrim.paint.impl.shared.CoExtendedMultiInkColorIF;
import com.bluebrim.paint.impl.shared.CoNoColor;
import com.bluebrim.paint.impl.shared.CoNoColorIF;
import com.bluebrim.paint.impl.shared.CoProcessBlack;
import com.bluebrim.paint.impl.shared.CoProcessBlackIF;
import com.bluebrim.paint.impl.shared.CoProcessCyan;
import com.bluebrim.paint.impl.shared.CoProcessCyanIF;
import com.bluebrim.paint.impl.shared.CoProcessMagenta;
import com.bluebrim.paint.impl.shared.CoProcessMagentaIF;
import com.bluebrim.paint.impl.shared.CoProcessYellow;
import com.bluebrim.paint.impl.shared.CoProcessYellowIF;
import com.bluebrim.paint.impl.shared.CoRegistrationColor;
import com.bluebrim.paint.impl.shared.CoRegistrationColorIF;
import com.bluebrim.paint.impl.shared.CoSpotColorIF;
import com.bluebrim.paint.impl.shared.CoWhiteColor;
import com.bluebrim.paint.impl.shared.CoWhiteColorIF;
import com.bluebrim.stroke.impl.shared.CoBackgroundDashColor;
import com.bluebrim.stroke.impl.shared.CoBackgroundDashColorIF;
import com.bluebrim.stroke.impl.shared.CoForegroundDashColor;
import com.bluebrim.stroke.impl.shared.CoForegroundDashColorIF;
import com.bluebrim.stroke.impl.shared.CoNoDashColor;
import com.bluebrim.stroke.impl.shared.CoNoDashColorIF;
import com.bluebrim.stroke.impl.shared.CoStroke;
import com.bluebrim.stroke.impl.shared.CoStrokeResources;
import com.bluebrim.stroke.shared.CoStrokeIF;

/**
 * Owner of all page item factories
 *
 * @author Dennis
 */
public class CoPageItemFactories implements CoFactoryInitSPI {
	public Object getDependencyKey() {
		return "pageitem";
	}

	public Object[] getPrerequisites() {
		return null;
	}

	public boolean init(boolean minimalistic) {
		CoFactoryManager factoryManager = CoFactoryManager.getInstance();

		factoryManager.add(CoPageItemFactoryIF.PAGE_ITEM_FACTORY, new CoPageItemFactory());
		factoryManager.add(CoShapeFactoryIF.SHAPE_FACTORY, new CoShapeFactory());
		factoryManager.add(CoLayoutSpecIF.LAYOUT_SPEC, new CoLayoutSpecFactory());
		factoryManager.add(CoLayoutManagerIF.LAYOUT_MANAGER, new CoLayoutManagerFactory());

		factoryManager.add(CoNoDashColorIF.NO_DASH_COLOR_SPEC, new CoSingletonFactory(new CoNoDashColor()));
		factoryManager.add(CoForegroundDashColorIF.FOREGROUND_DASH_COLOR_SPEC, new CoSingletonFactory(new CoForegroundDashColor()));
		factoryManager.add(CoBackgroundDashColorIF.BACKGROUND_DASH_COLOR_SPEC, new CoSingletonFactory(new CoBackgroundDashColor()));

		factoryManager.add(CoProcessCyanIF.PROCESS_CYAN, new CoSingletonFactory(new CoProcessCyan()));
		factoryManager.add(CoProcessMagentaIF.PROCESS_MAGENTA, new CoSingletonFactory(new CoProcessMagenta()));
		factoryManager.add(CoProcessYellowIF.PROCESS_YELLOW, new CoSingletonFactory(new CoProcessYellow()));
		factoryManager.add(CoProcessBlackIF.PROCESS_BLACK, new CoSingletonFactory(new CoProcessBlack()));
		factoryManager.add(CoNoColorIF.NO_COLOR, new CoSingletonFactory(new CoNoColor()));
		factoryManager.add(CoWhiteColorIF.WHITE_COLOR, new CoSingletonFactory(new CoWhiteColor()));
		factoryManager.add(CoRegistrationColorIF.REGISTRATION_COLOR, new CoSingletonFactory(new CoRegistrationColor()));
		factoryManager.add(CoSpotColorIF.SPOT_COLOR, new CoFactoryIF() {
			public CoFactoryElementIF createObject() {
				return new com.bluebrim.paint.impl.shared.CoSpotColor();
			}
		});
		factoryManager.add(com.bluebrim.paint.impl.shared.CoCMYKColorIF.FACTORY_KEY, new CoFactoryIF() {
			public CoFactoryElementIF createObject() {
				return new com.bluebrim.paint.impl.shared.CoCMYKColor();
			}
		});
		factoryManager.add(CoExtendedMultiInkColorIF.FACTORY_KEY, new CoFactoryIF() {
			public CoFactoryElementIF createObject() {
				return new CoExtendedMultiInkColor();
			}
		});

		factoryManager.add(CoGradientFillIF.GRADIENT_FILL, new CoGradientFillFactory());
		factoryManager.add(CoPatternFillStyleIF.PATTERN_FILL, new CoPatternFillFactory());

		factoryManager.add(CoPageItemPrototypeIF.FACTORY_KEY, new CoPageItemPrototypeFactory());
		//	factoryManager.add( CoTrappingParameter.TRAPPING_PARAMETER, new CoTrappingParameterFactory());
		// Temporarily removed by Magnus Ihse (magnus.ihse@appeal.se)

		// Solid
		CoStrokeIF spec = new CoStroke(CoStrokeIF.SOLID) {
			public boolean isMutable() {
				return false;
			}
		};
		spec.setName(CoStrokeResources.getName(CoStrokeIF.SOLID));
		factoryManager.add(CoStrokeIF.SOLID, new CoSingletonFactory(spec));

		return true;

	}
}