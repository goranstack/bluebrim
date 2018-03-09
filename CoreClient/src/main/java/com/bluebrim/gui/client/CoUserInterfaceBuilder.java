package com.bluebrim.gui.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import com.bluebrim.base.client.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.swing.client.maskfield.*;

/**
 * <code>CoUserInterface</code> holds an instance of <code>CoUserInterfaceBuilder</code> which
 * is responsible for creating and keeping track of the user inteface components and value models.
 * Namned user interface components and value models are stored in a <code>Hashtable</code> each and
 * can be accessed via <code>getNamedWidget</code> and <code>getNamedValueModel</code>.
 * The same methods are, as a matter of convencience, also implemented in <code>CoUserInterface</code>.
 * <p>
 * Instance variables:
 * <ul>
 * <li>userInterface			<code>CoUserInterface</code> 
 * <li>listenerList				<code>EventListenerList</code> for the listeners after <code>CoEnableDisableEvent</code and <code>CoWindowEvent</code>
 * <li>widgets					<code>Hashtable</code> named widgets
 * <li>valueModels				<code>Hashtable</code> named value models
 * <li>transparentWidgets		<code>boolean</code> true if all user interface should be transparent as default.
 * <li>windowListener			<code>BuilderWindowListener</code> a window listener for the window showing the userinterface
 * <li>internalFrameListener	<code>BuilderInternalFrameListener</code>a window listener for the internal frame showing the userinterface
 * </ul>
 * <br>
 * An instance of <code>BuilderWindowListener</code> is installed as the default window listener 
 * when the userinterface is opened in a window.
 * This listener redispatches all window events as userinterface events. In this way it's possible for,
 * for example, component adaptors to consume a window closing event and in this way cancel the closing.
 * <p>
 * Partially converted to accept as a key any <code>Object</code>, not only a <code>String</code>.</p>
 * @author Markus Persson 1999-05-14
 */
public class CoUserInterfaceBuilder extends CoUserInterfaceAdapter implements CoUIConstants {
	protected transient CoUserInterface userInterface;
	protected transient EventListenerList listenerList = new EventListenerList();
	protected transient Map widgets;
	protected transient Map valueModels;
	protected transient boolean transparentWidgets = false;
	protected transient WindowListener windowListener;
	protected transient InternalFrameListener internalFrameListener;

	private static FocusBorder FOCUS_BORDER;
	private static FocusBorder BUTTON_FOCUS_BORDER;
	private static ScrollFocusBorder SCROLL_FOCUS_BORDER;

	protected class BuilderWindowListener extends WindowAdapter {
		private int m_originalCloseOperation = -1;
		public BuilderWindowListener() {
			super();
		}
		public void windowActivated(WindowEvent e) {
			userInterface.activate();
			fireUserInterfaceEvent(createUserInterfaceEventFrom(e));
		}
		public void windowClosed(WindowEvent e) {
			fireUserInterfaceEvent(createUserInterfaceEventFrom(e));
			Window window = e.getWindow();
			if (getCloseOperationFor(window) == WindowConstants.DISPOSE_ON_CLOSE)
				userInterface.closedBy(window, true);
		}
		private void handleDialogClosing(CoUserInterfaceEvent e, JDialog dialog) {
			if (e.isConsumed()) {
				m_originalCloseOperation = dialog.getDefaultCloseOperation();
				dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			} else {
				if (m_originalCloseOperation != -1) {
					dialog.setDefaultCloseOperation(m_originalCloseOperation);
					m_originalCloseOperation = -1;
				}
				userInterface.closing();
			}
		}
		private void handleFrameClosing(CoUserInterfaceEvent e, JFrame frame) {
			if (e.isConsumed()) {
				m_originalCloseOperation = frame.getDefaultCloseOperation();
				frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			} else {
				if (m_originalCloseOperation != -1) {
					frame.setDefaultCloseOperation(m_originalCloseOperation);
					m_originalCloseOperation = -1;
				}
				userInterface.closing();
			}
		}
		public void windowClosing(WindowEvent e) {
			CoUserInterfaceEvent event = createUserInterfaceEventFrom(e);
			fireUserInterfaceEvent(event);
			Window window = e.getWindow();
			if (isFrame(window))
				handleFrameClosing(event, (JFrame) window);
			else if (isDialog(window))
				handleDialogClosing(event, (JDialog) window);
			else
				userInterface.closing();
		}
		public void windowDeactivated(WindowEvent e) {
			userInterface.deactivate();
			fireUserInterfaceEvent(createUserInterfaceEventFrom(e));
		}
		public void windowOpened(WindowEvent e) {
			Window window = e.getWindow();
			if (!isDialog(window))
				userInterface.opened();
			fireUserInterfaceEvent(createUserInterfaceEventFrom(e));
			m_originalCloseOperation = -1;
		}
		private int getCloseOperationFor(Window window) {
			return isFrame(window) ? ((JFrame) window).getDefaultCloseOperation() : isDialog(window) ? ((JDialog) window).getDefaultCloseOperation() : WindowConstants.DISPOSE_ON_CLOSE;
		}
		private boolean isDialog(Window w) {
			return w instanceof JDialog;
		}
		private boolean isFrame(Window w) {
			return w instanceof JFrame;
		}
	}

	protected class BuilderInternalFrameListener extends InternalFrameAdapter {
		public BuilderInternalFrameListener() {
			super();
		}
		public void internalFrameActivated(InternalFrameEvent e) {
			userInterface.activate();
			fireUserInterfaceEvent(createUserInterfaceEventFrom(e));
		}
		public void internalFrameClosed(InternalFrameEvent e) {
			fireUserInterfaceEvent(createUserInterfaceEventFrom(e));
			userInterface.closed();
		}
		public void internalFrameClosing(InternalFrameEvent e) {
			fireUserInterfaceEvent(createUserInterfaceEventFrom(e));
			userInterface.closing();
		}
		public void internalFrameDeactivated(InternalFrameEvent e) {
			userInterface.deactivate();
			fireUserInterfaceEvent(createUserInterfaceEventFrom(e));
		}
		public void internalFrameOpened(InternalFrameEvent e) {
			userInterface.opened();
			fireUserInterfaceEvent(createUserInterfaceEventFrom(e));
		}
	}

	private static class FocusBorder extends AbstractBorder {
		public Insets getBorderInsets(Component c) {
			return new Insets(3, 3, 3, 3);
		}
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			try {
				JComponent component = (JComponent) c;
				if (component.hasFocus()) {
					Color origColor = g.getColor();
					g.setColor(UIManager.getColor(CoUIConstants.FOCUS_COLOR));
					g.drawRect(x, y, width - 1, height - 1);
					g.drawRect(x + 1, y + 1, width - 3, height - 3);
					g.setColor(origColor);
				}
			} catch (ClassCastException e) {
			}
		}
	}

	private static class ScrollFocusBorder extends FocusBorder {
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			super.paintBorder(((JScrollPane) c).getViewport().getView(), g, x, y, width, height);
		}
	};

	static {
		FOCUS_BORDER = new FocusBorder();
		BUTTON_FOCUS_BORDER = new FocusBorder();
		SCROLL_FOCUS_BORDER = new ScrollFocusBorder();
	}

	static Insets m_noInsets = new Insets(0, 0, 0, 0);

	/**
	 */
	public CoUserInterfaceBuilder(CoUserInterface userInterface) {
		setUserInterface(userInterface);
	}
	/**
	 */
	public CoAspectAdaptor addAspectAdaptor(CoAspectAdaptor adaptor) {
		return addAspectAdaptor(adaptor, adaptor.getKey());
	}
	/**
	 */
	public CoAspectAdaptor addAspectAdaptor(CoAspectAdaptor adaptor, Object key) {
		addNamedValueModel(key, adaptor);
		return adaptor;
	}
	/**
	 */
	public void addButtonFocusBorderTo(AbstractButton b) {
		Border tBorder = b.getBorder();
		if (tBorder != null && b.isBorderPainted())
			b.setBorder(BorderFactory.createCompoundBorder(BUTTON_FOCUS_BORDER, tBorder));
		else
			b.setBorder(BUTTON_FOCUS_BORDER);
		b.setBorderPainted(true);
	}
	/**
	 * @param l SE.corren.calvin.userinterface.CoEnableDisableListener
	 */
	public synchronized void addEnableDisableListener(CoEnableDisableListener l) {
		listenerList.add(CoEnableDisableListener.class, l);
	}
	/**
	 * Adds a focus border to the component argument.
	 * <br> 
	 * This method is automatically called when creating 
	 * textfields and combo boxes 
	 * but must be explicitly called for all other components.
	 */
	public void addFocusBorderTo(JComponent c) {
		Border border = c.getBorder();
		if (border != null)
			c.setBorder(BorderFactory.createCompoundBorder(FOCUS_BORDER, border));
		else
			c.setBorder(FOCUS_BORDER);
	}
	/**
	 */
	public CoAbstractListAspectAdaptor addListAspectAdaptor(CoAbstractListAspectAdaptor adaptor) {
		return addListAspectAdaptor(adaptor, adaptor.getKey());
	}
	/**
	 */
	public CoAbstractListAspectAdaptor addListAspectAdaptor(CoAbstractListAspectAdaptor adaptor, Object key) {
		addNamedValueModel(key, adaptor);
		return adaptor;
	}
	/**
	 */
	public CoAbstractListValueModel addListValueModel(CoAbstractListValueModel model) {
		addListValueModel(model, model.getKey());
		return model;
	}
	/**
	 */
	public CoAbstractListValueModel addListValueModel(CoAbstractListValueModel model, Object key) {
		addNamedValueModel(key, model);
		return model;
	}

	/**
	 * If valueable has a non-null key, adds a mapping from the key
	 * to the valueable.
	 */
	public synchronized void addValueable(CoValueable valueable) {
		Object key = valueable.getKey();
		if (key != null) {
			addNamedValueModel(key, valueable);
		}
	}

	/**
	 * Adderar 'valueable' till min Map med namngivna valuemodels
	 * med 'key' som nyckel.<br>
	 * ValueModels kan hämtas igen via #getNamedValueModel(Object key)
	 */
	public synchronized void addNamedValueModel(Object key, CoValueable valueable) {
		if (valueModels == null) {
			valueModels = new HashMap();
		}
		
		if (key == null) {
			throw new IllegalArgumentException("Key null in CoUserInterfaceBuilder#addNamedValueModel");
		}

		valueModels.put(key, valueable);
	}

	/**
	 	Adderar 'widget' till min Hashtable med namngivna komponenter
	 	med komponentens namn som nyckel.
	 */
	public synchronized void addNamedWidget(Component widget) {
		addNamedWidget(widget.getName(), widget);
	}
	/**
	 * Adds 'widget' to my Map with "named" components using 'key' as a key.<br>
	 * The component can be retrieved using #getNamedWidget(Object key).
	 *
	 * Before, when only Strings were allowed, almost every method calling
	 * this method used to do null and empty string checking on the key
	 * and conditionally threw an Exception. Those checks are now replaced
	 * with simple null checking here for quite obvious reasons.
	 *
	 * However, this has resulted in the Exception message being less
	 * precise, thereby making the bug harder to track. Some investigations
	 * in execution stacks could probably give better suited output if
	 * anyone cares to try.
	 */
	public synchronized void addNamedWidget(Object key, Component widget) {
		if (widgets == null) {
			widgets = new HashMap();
		}
		if (key == null) {
			throw new IllegalArgumentException("Key null in CoUserInterfaceBuilder#addNamedWidget");
		}
		widgets.put(key, widget);

		// Might just as well do this, but who needs it? /Markus
		String name = null;
		if (key instanceof String) {
			name = (String) key;
		} else if (key instanceof CoNamed) {
			name = ((CoNamed) key).getName();
		}

		if (name != null) {
			widget.setName(name);
			if (widget instanceof JComponent)
				 ((JComponent) widget).getAccessibleContext().setAccessibleName(name);
		}
	}
	public void addScrollFocusBorderTo(JScrollPane b) {
		Border tBorder = b.getBorder();
		if (tBorder != null)
			b.setBorder(BorderFactory.createCompoundBorder(SCROLL_FOCUS_BORDER, tBorder));
		else
			b.setBorder(SCROLL_FOCUS_BORDER);
	}
	/**
	 */
	public CoTableAspectAdaptor addTableAspectAdaptor(CoTableAspectAdaptor adaptor) {
		return addTableAspectAdaptor(adaptor, adaptor.getKey());
	}
	/**
	 */
	public CoTableAspectAdaptor addTableAspectAdaptor(CoTableAspectAdaptor adaptor, Object key) {
		addNamedValueModel(key, adaptor);
		return adaptor;
	}
	/**
	 */
	public CoAbstractTreeAspectAdaptor addTreeAspectAdaptor(CoAbstractTreeAspectAdaptor adaptor) {
		return addTreeAspectAdaptor(adaptor, adaptor.getKey());
	}
	/**
	 */
	public CoAbstractTreeAspectAdaptor addTreeAspectAdaptor(CoAbstractTreeAspectAdaptor adaptor, Object key) {
		addNamedValueModel(key, adaptor);
		return adaptor;
	}
	/**
	 */
	public CoTreeValueHolder addTreeValueHolder(CoTreeValueHolder treeValueHolder, Object key) {
		addNamedValueModel(key, treeValueHolder);
		return treeValueHolder;
	}
	/**
	 */
	public synchronized void addUserInterfaceListener(CoUserInterfaceListener l) {
		listenerList.add(CoUserInterfaceListener.class, l);
	}
	/**
	 */
	public CoValueHolder addValueHolder(CoValueHolder holder) {
		addValueHolder(holder, holder.getKey());
		return holder;
	}
	/**
	 */
	public CoValueHolder addValueHolder(CoValueHolder holder, Object key) {
		addNamedValueModel(key, holder);
		return holder;
	}
	/**
	 */
	public Co3DToggleButton create3DToggleButton(String title, Icon icon, CoButtonGroup buttonGroup, String actionCommand) {
		Co3DToggleButton b;

		if (title == null) {
			b = (icon != null) ? new Co3DToggleButton(icon) : new Co3DToggleButton();
		} else {
			b = (icon != null) ? new Co3DToggleButton(title, icon) : new Co3DToggleButton(title);
		}

		prepareToggleButton(b);

		b.setActionCommand(actionCommand);
		b.getAccessibleContext().setAccessibleName(actionCommand);
		addNamedWidget(actionCommand, b);
		if (buttonGroup != null)
			b.setButtonGroup(buttonGroup);

		return b;
	}

	public Co3DToggleButton create3DToggleButton(String title, CoButtonGroup buttonGroup, String actionCommand) {
		return create3DToggleButton(title, null, buttonGroup, actionCommand);
	}
	public CoSubcanvasAdaptor createAutoSubcanvasAdaptor(CoValueable valueModel, CoSubcanvas subcanvas) {
		return createAutoSubcanvasAdaptor(valueModel, subcanvas, null, null);
	}
	public CoSubcanvasAdaptor createAutoSubcanvasAdaptor(CoValueable valueModel, CoSubcanvas subcanvas, Map uiMap) {
		return createAutoSubcanvasAdaptor(valueModel, subcanvas, uiMap, null);
	}
	public CoSubcanvasAdaptor createAutoSubcanvasAdaptor(CoValueable valueModel, CoSubcanvas subcanvas, Map uiMap, final CoUIContext context) {
		final Map m_uiMap = (uiMap != null) ? uiMap : new HashMap();

		CoSubcanvasAdaptor adaptor = new CoSubcanvasAdaptor(valueModel, subcanvas) {
				// Ugly workaround, see CoSubcanvasAdaptor.
	{
				updateSubcanvas();
			}
			// Ugly workaround, see CoSubcanvasAdaptor.
			protected void initializeSubcanvas() {
			}
			protected CoDomainUserInterface getPreparedUserInterfaceFor(CoDomainUserInterface oldUI, Object value) {
				return CoAutomaticUIKit.getUIForUsing(value, m_uiMap, context);
			}
			public void userInterfaceClosed(CoUserInterfaceEvent e) {
			}

			public void userInterfaceClosing(CoUserInterfaceEvent e) {
			}
		};
		valueModel.addEnableDisableListener(adaptor);
		addUserInterfaceListener(adaptor);
		return adaptor;
	}


	public CoPanel createBoxPanel(int orientation) {
		return createBoxPanel(orientation, false, null);
	}
	/**
	 */
	public CoPanel createBoxPanel(int orientation, String name) {
		return createBoxPanel(orientation, false, null, name);
	}
	/**
	 */
	public CoPanel createBoxPanel(int orientation, boolean isDoubleBuffered, Insets extraInsets) {
		CoPanel tPanel = createPanel(null, isDoubleBuffered, extraInsets);
		BoxLayout tLayout = new BoxLayout(tPanel, orientation);
		tPanel.setLayout(tLayout);
		return tPanel;
	}
	/**
	 */
	public CoPanel createBoxPanel(int orientation, boolean isDoubleBuffered, Insets extraInsets, String name) {
		CoPanel tPanel = createPanel(null, isDoubleBuffered, extraInsets, name);
		BoxLayout tLayout = new BoxLayout(tPanel, orientation);
		tPanel.setLayout(tLayout);
		return tPanel;
	}
	/**
	 */
	public CoButton createButton(String title, Icon icon) {
		return doCreateButton(title, icon);
	}
	/**
	 */
	public CoButton createButton(String title, Icon icon, ActionListener actionListener) {
		CoButton tButton = createButton(title, icon);
		if (actionListener != null)
			tButton.addActionListener(actionListener);
		return tButton;
	}
	/**
	 */
	public CoButton createButton(String title, Icon icon, ActionListener actionListener, Object key) {
		CoButton tButton = createButton(title, icon, actionListener);
		addNamedWidget(key, tButton);
		return tButton;
	}
	/**
	 */
	public CoButton createButton(String title, Icon icon, Object key) {
		CoButton tButton = createButton(title, icon);
		addNamedWidget(key, tButton);
		return tButton;
	}
	/**
	 */
	public CoButton createButton(AbstractAction action) {
		return doCreateButton(action);
	}
	/**
	 */
	public CoButton createButton(AbstractAction action, Object key) {
		CoButton tButton = createButton(action);
		addNamedWidget(key, tButton);
		return tButton;
	}
	/**
	 */
	public CoButtonAdaptor createButtonAdaptor(CoValueable aValueModel, AbstractButton aButton) {
		CoButtonAdaptor tAdaptor = new CoButtonAdaptor(aValueModel, aButton);
		aValueModel.addEnableDisableListener(tAdaptor);
		return tAdaptor;
	}
	/**
	 */
	public CoButtonGroup createButtonGroup() {
		return new CoButtonGroup();
	}
	/**
	 * @return SE.corren.calvin.userinterface.CoButtonGroupAdaptor
	 * @param aValueModel SE.corren.calvin.userinterface.CoValueModel
	 * @param aSubcanvas SE.corren.calvin.userinterface.CoButtonGroup
	 */
	public CoButtonGroupAdaptor createButtonGroupAdaptor(CoValueModel aValueModel, CoButtonGroup aButtonGroup) {
		CoButtonGroupAdaptor tAdaptor = new CoButtonGroupAdaptor(aValueModel, aButtonGroup);
		aValueModel.addEnableDisableListener(tAdaptor);
		return tAdaptor;
	}

	public CoButtonTextAdaptor createButtonTextAdaptor(CoValueModel vm, AbstractButton b) {
		CoButtonTextAdaptor a = new CoButtonTextAdaptor(vm, b);
		vm.addEnableDisableListener(a);
		addUserInterfaceListener(a);
		return a;
	}

	public CoCheckBox createCheckBox(String title, Object key) {
		return createCheckBox(title, null, key);
	}

	public CoCheckBox createCheckBox(String title, Icon icon) {
		CoCheckBox tButton = doCreateCheckBox(title, icon);
		return tButton;
	}

	public CoCheckBox createCheckBox(String title, Icon icon, Object key) {
		CoCheckBox tButton = createCheckBox(title, icon);
		addNamedWidget(key, tButton);
		return tButton;
	}

	public CoChooserPanel createChooserPanel(String sourceLabel, String destinationLabel, int orientation) {
		return createChooserPanel(sourceLabel, destinationLabel, orientation, CoChooserPanel.REMOVE_FROM_SOURCE);
	}

	public CoChooserPanel createChooserPanel(String sourceLabel, String destinationLabel, int orientation, int move) {
		CoChooserPanel tPanel = new CoChooserPanel(sourceLabel, destinationLabel, orientation, move);
		prepareChooserPanel(tPanel);
		return tPanel;
	}

	public CoChooserPanel createChooserPanel(String sourceLabel, String destinationLabel, int orientation, int move, Object key) {
		CoChooserPanel tPanel = createChooserPanel(sourceLabel, destinationLabel, orientation, move);
		addNamedWidget(key, tPanel);
		return tPanel;
	}

	public CoChooserPanel createChooserPanel(String sourceLabel, String destinationLabel, int orientation, Object key) {
		return createChooserPanel(sourceLabel, destinationLabel, orientation, CoChooserPanel.REMOVE_FROM_SOURCE, key);

	}

	public CoComboBox createComboBox() {
		return doCreateComboBox();
	}

	public CoComboBox createComboBox(Object key) {
		CoComboBox tComboBox = createComboBox();
		if (key != null)
			addNamedWidget(key, tComboBox);
		else
			throw new IllegalArgumentException("Key null in CoUserInterfaceBuilder#createComboBox");
		return tComboBox;
	}

	public CoComboBox createComboBox(ListCellRenderer renderer) {
		CoComboBox tComboBox = createComboBox();
		tComboBox.setRenderer(renderer);
		return tComboBox;
	}

	public CoComboBox createComboBox(ListCellRenderer renderer, Object key) {
		CoComboBox tComboBox = createComboBox(renderer);
		if (key != null)
			addNamedWidget(key, tComboBox);
		else
			throw new IllegalArgumentException("Key null in CoUserInterfaceBuilder#createComboBox");
		return tComboBox;
	}

	public CoComboBoxAdaptor createComboBoxAdaptor(CoListValueable listValueable, CoValueModel aValueModel, JComboBox aComboBox) {
		CoComboBoxAdaptor tAdaptor = new CoListComboBoxAdaptor(listValueable, aValueModel, aComboBox);
		aValueModel.addEnableDisableListener(tAdaptor);
		addUserInterfaceListener(tAdaptor);
		return tAdaptor;
	}

	public CoComboBoxAdaptor createComboBoxAdaptor(CoValueModel aValueModel, JComboBox aComboBox) {
		CoComboBoxAdaptor tAdaptor = new CoComboBoxAdaptor(aValueModel, aComboBox);
		aValueModel.addEnableDisableListener(tAdaptor);
		addUserInterfaceListener(tAdaptor);
		return tAdaptor;
	}

	public CoDeltaButtonAdaptor createDeltaButtonAdaptor(CoValueModel aValueModel, AbstractButton aButton, int delta) {
		CoDeltaButtonAdaptor tAdaptor = new CoDeltaButtonAdaptor(aValueModel, aButton, delta);
		aValueModel.addEnableDisableListener(tAdaptor);
		return tAdaptor;
	}
	/**
		Creates and returns a panel with a 
		label above a text field.
	*/
	public CoPanel createFormStylePanelWithComboBox(int alignment, int columns, int maxColumns, String label, String fieldKey, boolean editable, String comboBoxKey) {

		//Panel
		CoPanel tPanel = createPanel(new CoAttachmentLayout(), fieldKey);
		tPanel.setBackground(getColor(LABELED_TEXT_FIELD_BACKGROUND));
		tPanel.setOpaque(true);
		tPanel.setBorder(getBorder(LABELED_TEXT_FIELD_BORDER));
		//Label
		final CoLabel tLabel = createLabel(" " + label);
		tLabel.setFont(getFont(LABELED_TEXT_FIELD_FONT));
		tLabel.setForeground(getColor(LABELED_TEXT_FIELD_FOREGROUND));

		//ComboBox
		CoComboBox comboBox = createSlimComboBox(comboBoxKey); // skicka med nyckel
		comboBox.setForeground(getColor(LABELED_TEXT_FIELD_FOREGROUND));

		tPanel.add(
			tLabel,
			new CoAttachmentLayout.Attachments(
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_CONTAINER, 1),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_NO),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_CONTAINER, 1),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_NO)));

		tPanel.add(
			comboBox,
			new CoAttachmentLayout.Attachments(
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, tLabel),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_CONTAINER, 1),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, tLabel),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_CONTAINER)));
		return tPanel;
	}
	/**
		Creates and returns a panel with a 
		label above a text field.
	*/
	public CoPanel createFormStyleTextFieldPanel(int alignment, int columns, int maxColumns, String label, String fieldKey) {
		return createFormStyleTextFieldPanel(alignment, columns, maxColumns, label, fieldKey, true);
	}
	/**
		Creates and returns a panel with a 
		label above a text field.
	*/
	public CoPanel createFormStyleTextFieldPanel(int alignment, int columns, int maxColumns, String label, String fieldKey, boolean editable) {
		//Panel
		CoPanel tPanel = createPanel(new CoAttachmentLayout());
		tPanel.setBackground(getColor(LABELED_TEXT_FIELD_BACKGROUND));
		tPanel.setOpaque(true);
		tPanel.setBorder(getBorder(LABELED_TEXT_FIELD_BORDER));
		//Label
		final CoLabel tLabel = createLabel(" " + label);
		tLabel.setFont(getFont(LABELED_TEXT_FIELD_FONT));
		tLabel.setForeground(getColor(LABELED_TEXT_FIELD_FOREGROUND));
		//TextField
		CoTextField aTextField = createTextField(alignment, columns, maxColumns, fieldKey);
		aTextField.setBorder(null);
		addFocusBorderTo(aTextField);

		tPanel.add(
			tLabel,
			new CoAttachmentLayout.Attachments(
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_CONTAINER, 1),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_NO),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_CONTAINER, 1),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_CONTAINER, 1)));
		tPanel.add(
			aTextField,
			new CoAttachmentLayout.Attachments(
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, tLabel),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_CONTAINER, 1),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, tLabel),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_COMPONENT_RIGHT, 0, tLabel)));
		aTextField.setEditable(editable);

		aTextField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("enabled"))
					tLabel.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
			}
		});
		return tPanel;
	}
	/**
		Creates and returns a panel with a 
		label above a text field.
	*/
	public CoPanel createFormStyleTextFieldPanel(int alignment, String label, String fieldKey) {
		return createFormStyleTextFieldPanel(alignment, label, fieldKey, true);
	}
	/**
		Creates and returns a panel with a 
		label above a text field.
	*/
	public CoPanel createFormStyleTextFieldPanel(int alignment, String label, String fieldKey, boolean editable) {
		return createFormStyleTextFieldPanel(alignment, 0, 0, label, fieldKey, editable);
	}

	public CoLabel createHeadlineLabel(String headline) {
		CoLabel label = createLabel(headline);
		prepareHeadlineLabel(label);
		return label;
	}
	
	/**
	 */
	public CoPanel createHeadlineLabelPanel(String headlineLabel) {
		CoPanel tTopPanel = createBoxPanel(BoxLayout.X_AXIS, false, null);
		CoLabel tTopLabel = createLabel(headlineLabel);
		tTopLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		prepareHeadlineLabel(tTopLabel);

		//	tTopPanel.add(Box.createHorizontalStrut(10));
		tTopPanel.add(tTopLabel);
		tTopPanel.add(Box.createHorizontalStrut(10));
		tTopPanel.add(Box.createHorizontalGlue());
		tTopPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		CoPanel tMainPanel = createPanel(new BorderLayout(), false, null, "PANEL");
		tMainPanel.add(tTopPanel, BorderLayout.NORTH);

		return tMainPanel;
	}
	/**
	 */
	public CoPanel createHeadlineLabelPanel(String headlineLabel, Color headlineColor) {
		CoPanel tTopPanel = createBoxPanel(BoxLayout.X_AXIS, false, null);
		CoLabel tTopLabel = createLabel(headlineLabel);
		tTopLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		prepareHeadlineLabel(tTopLabel);
		tTopLabel.setForeground(headlineColor);

		//	tTopPanel.add(Box.createHorizontalStrut(10));
		tTopPanel.add(tTopLabel);
		tTopPanel.add(Box.createHorizontalStrut(10));
		tTopPanel.add(Box.createHorizontalGlue());
		tTopPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		CoPanel tMainPanel = createPanel(new BorderLayout(), false, null, "PANEL");
		tMainPanel.add(tTopPanel, BorderLayout.NORTH);

		return tMainPanel;
	}
	/**
	 * Returnerar en panel med en rubrik längst upp och aPanel under.
	 */
	public CoPanel createHeadlineLabelPanel(String headlineLabel, Color headlineColor, JComponent aComponent) {
		CoPanel tTopPanel = createBoxPanel(BoxLayout.X_AXIS, false, null);
		CoLabel tTopLabel = createLabel(headlineLabel);
		tTopLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		prepareHeadlineLabel(tTopLabel);
		tTopLabel.setForeground(headlineColor);

		tTopPanel.add(Box.createHorizontalStrut(10));
		tTopPanel.add(tTopLabel);
		tTopPanel.add(Box.createHorizontalStrut(10));
		tTopPanel.add(Box.createHorizontalGlue());
		tTopPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		CoPanel tMainPanel = createPanel(new BorderLayout(), false, new Insets(2, 2, 2, 2), "PANEL");
		tMainPanel.add(tTopPanel, BorderLayout.NORTH);
		tMainPanel.add(aComponent, BorderLayout.CENTER);
		aComponent.setAlignmentX(Component.LEFT_ALIGNMENT);

		return tMainPanel;
	}
	/**
	 * Returnerar en panel med en rubrik längst upp och aPanel under.
	 */
	public CoPanel createHeadlineLabelPanel(String headlineLabel, JComponent aComponent) {
		CoPanel tTopPanel = createBoxPanel(BoxLayout.X_AXIS, false, null);
		CoLabel tTopLabel = createLabel(headlineLabel);
		tTopLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		prepareHeadlineLabel(tTopLabel);
		tTopLabel.setForeground(Color.red);

		tTopPanel.add(Box.createHorizontalStrut(10));
		tTopPanel.add(tTopLabel);
		tTopPanel.add(Box.createHorizontalStrut(10));
		tTopPanel.add(Box.createHorizontalGlue());
		tTopPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		CoPanel tMainPanel = createPanel(new BorderLayout(), false, new Insets(2, 2, 2, 2));
		tMainPanel.add(tTopPanel, BorderLayout.NORTH);
		tMainPanel.add(aComponent, BorderLayout.CENTER);

		/*
		CoPanel tMainPanel = createBoxPanel(BoxLayout.Y_AXIS, false,null);
		tMainPanel.add(tTopPanel);
		tMainPanel.add(aComponent);
		aComponent.setAlignmentX(Component.LEFT_ALIGNMENT);
		*/

		return tMainPanel;
	}
	/**
	 */
	public CoHorizontalToolbarDockingBay createHorizontalToolbarDockingBay() {
		CoHorizontalToolbarDockingBay tbdb = new CoHorizontalToolbarDockingBay();
		prepareToolbarDockingBay(tbdb);
		return tbdb;
	}
	/**
	 */
	public CoHorizontalToolbarDockingBay createHorizontalToolbarDockingBay(Object key) {
		CoHorizontalToolbarDockingBay tbdb = createHorizontalToolbarDockingBay();
		addNamedWidget(key, tbdb);
		return tbdb;
	}
	public CoIconLabeledTextField createIconLabeledTextField(int columns, Icon icon, String label) {
		CoIconLabeledTextField textField = new CoIconLabeledTextField(columns, icon, label);
		prepareIconLabeledTextField(textField);
		return textField;
	}
	public CoIconLabeledTextField createIconLabeledTextField(Icon icon, String label) {
		CoIconLabeledTextField textField = new CoIconLabeledTextField(icon, label);
		prepareIconLabeledTextField(textField);
		return textField;
	}
	/**
	 */
	public CoLabel createLabel(String label) {
		CoLabel tLabel = new CoLabel(label);
		prepareLabel(tLabel);
		return tLabel;
	}
	public CoLabel createLabel(String label, int alignment) {
		return createLabel(label, null, alignment);
	}
	/**
	 */
	public CoLabel createLabel(String label, Object key) {
		CoLabel tLabel = createLabel(label);
		addNamedWidget(key, tLabel);
		return tLabel;
	}
	/**
	 */
	public CoLabel createLabel(String label, Icon icon, int alignment) {
		CoLabel tLabel = new CoLabel(label, icon, alignment);
		prepareLabel(tLabel);
		return tLabel;
	}
	/**
	 */
	public CoLabel createLabel(String label, Icon icon, int alignment, Object key) {
		CoLabel tLabel = createLabel(label, icon, alignment);
		addNamedWidget(key, tLabel);
		return tLabel;
	}
	/**
	 */
	public CoLabel createLabel(Icon anIcon) {
		CoLabel tLabel = new CoLabel(anIcon);
		prepareLabel(tLabel);
		return tLabel;
	}
	/**
	 */
	public CoLabelAdaptor createLabelAdaptor(CoValueModel aValueModel, CoLabel aLabel) {
		CoLabelAdaptor tAdaptor = new CoLabelAdaptor(aValueModel, aLabel);
		aValueModel.addEnableDisableListener(tAdaptor);
		addUserInterfaceListener(tAdaptor);
		return tAdaptor;
	}
	/**
	 * Creates and returns a bordered panel with a 
	 * label above <code>component</code>.
	 *
	 * Argument changed to Component instead of JComponent, since that's
	 * what's used throughout the builder, except in other stray methods.
	 * /Markus 2000-07-17
	 *
	 * @author Dennis (?)
	 */
	public CoPanel createLabeledPanelFor(Component component, String tLabel) {
		return createLabeledPanelFor(component, tLabel, null);
	}
	/**
	 * Creates and returns a bordered panel with a 
	 * label above <code>component</code>.
	 *
	 * Argument changed to Component instead of JComponent, since that's
	 * what's used throughout the builder, except in other stray methods.
	 * /Markus 2000-07-17
	 *
	 * @author Dennis (?)
	 */
	public CoPanel createLabeledPanelFor(Component component, String label, Object labelKey) {
		//Label
		CoLabel tLabel = null;
		if (labelKey == null) {
			tLabel = createLabel(" " + label);
		} else {
			tLabel = createLabel(" " + label, labelKey);
		}
		tLabel.setFont(getFont(LABELED_TEXT_FIELD_FONT));
		tLabel.setForeground(getColor(LABELED_TEXT_FIELD_FOREGROUND));

		return createLabeledPanelFor(component, tLabel);
	}
	/**
	 * Creates and returns a bordered panel with a 
	 * label above <code>component</code>.
	 *
	 * Argument changed to Component instead of JComponent, since that's
	 * what's used throughout the builder, except in other stray methods.
	 * /Markus 2000-07-17
	 *
	 * @author Dennis (?)
	 */
	public CoPanel createLabeledPanelFor(Component component, final CoLabel tLabel) {
		//Panel
		CoPanel tPanel = createPanel(new CoAttachmentLayout());
		tPanel.setBackground(getColor(LABELED_TEXT_FIELD_BACKGROUND));
		tPanel.setOpaque(true);
		tPanel.setBorder(getBorder(LABELED_TEXT_FIELD_BORDER));

		tPanel.add(
			tLabel,
			new CoAttachmentLayout.Attachments(
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_CONTAINER, 1),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_NO),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_CONTAINER, 1),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_CONTAINER, 1)));
		tPanel.add(
			component,
			new CoAttachmentLayout.Attachments(
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, tLabel),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_CONTAINER, 1),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_COMPONENT_LEFT, 0, tLabel),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_COMPONENT_RIGHT, 0, tLabel)));

		component.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("enabled"))
					tLabel.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
			}
		});

		return tPanel;
	}
	/**
		Creates and returns a panel with a 
		label above a text field.
	*/
	public CoLabeledTextField createLabeledTextField(int columns, int maxColumns, String label, Object fieldKey) {
		CoLabeledTextField textField = new CoLabeledTextField("", columns, maxColumns, label);
		prepareLabeledTextField(textField);
		if (fieldKey != null) {
			addNamedWidget(fieldKey, textField);
		}
		return textField;
	}
	/**
		Creates and returns a panel with a 
		label above a text field.
	*/
	public CoLabeledTextField createLabeledTextField(String label) {
		CoLabeledTextField textField = new CoLabeledTextField(label);
		prepareLabeledTextField(textField);
		return textField;
	}
	/**
		Creates and returns a panel with a label.
	*/
	public CoPanel createLabelPanel(String label) {
		//Panel
		CoPanel tPanel = createPanel(new CoAttachmentLayout());
		tPanel.setOpaque(false);
		//Label
		CoLabel tLabel = createLabel(label);
		//tLabel.setFont(CoUIConstants.ARIAL_10);

		tPanel.add(
			tLabel,
			new CoAttachmentLayout.Attachments(
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_CONTAINER, 1),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_CONTAINER, 1),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_CONTAINER, 1),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_CONTAINER, 1)));

		return tPanel;
	}
	/**
	 */
	public CoListBox createListBox() {
		CoListBox tListBox = doCreateListBox();
		tListBox.getList().setCellRenderer(new CoListCellRenderer());
		return tListBox;
	}
	/**
	 */
	public CoListBox createListBox(Object key) {
		CoListBox tListBox = createListBox();
		addNamedWidget(key, tListBox);
		return tListBox;
	}
	public CoListBox createListBox(ListCellRenderer renderer) {
		CoListBox listBox = doCreateListBox();
		listBox.getList().setCellRenderer(renderer);
		return listBox;
	}
	public CoListBox createListBox(ListCellRenderer renderer, Object key) {
		CoListBox listBox = createListBox(renderer);
		addNamedWidget(key, listBox);
		return listBox;
	}
	public CoListBox createListBox(ListCellRenderer renderer, CoList list) {
		CoListBox listBox = doCreateListBox(list);
		list.setCellRenderer(renderer);
		return listBox;
	}
	public CoListBox createListBox(ListCellRenderer renderer, CoList list, Object key) {
		CoListBox listBox = createListBox(renderer, list);
		addNamedWidget(key, listBox);
		return listBox;
	}
	/**
	 */
	public CoListBox createListBox(CoList list) {
		return createListBox(new CoListCellRenderer(), list);
	}
	/**
	 * @return SE.corren.calvin.userinterface.CoListBoxAdaptor
	 * @param aValueModel SE.corren.calvin.userinterface.ValueModel
	 * @param aButton javax.swing.JToggleButton
	 */
	public CoListBoxAdaptor createListBoxAdaptor(CoListValueable aValueable, CoListBox aListBox) {
		CoListBoxAdaptor tAdaptor = new CoListBoxAdaptor(aValueable, aListBox);
		aValueable.addEnableDisableListener(tAdaptor);
		return tAdaptor;
	}
	/**
	 */
	public CoListValueModel createListValueModel(Object key) {
		return createListValueModel(key, null);
	}
	/**
	 */
	public CoListValueModel createListValueModel(Object key, java.util.List aList) {
		CoListValueModel tModel = new CoListValueModel();
		if (aList != null)
			tModel.setValue(aList);
		addNamedValueModel(key, tModel);
		return tModel;
	}
	/**
	 */
	public JMaskField createMaskField(int alignment, String mask) {
		return createMaskField(alignment, mask, getDefaultMacros());
	}
	/**
	 */
	public JMaskField createMaskField(int alignment, String mask, String name) {
		JMaskField tField = createMaskField(alignment, mask);
		if ((name != null) && (name.length() > 0)) {

			addNamedWidget(name, tField);
		} else
			throw new IllegalArgumentException("Name null or empty in CoUserInterfaceBuilder#createMaskedField");
		return tField;
	}
	/**
	 */
	public JMaskField createMaskField(int alignment, String mask, MaskMacros macros) {
		JMaskField tField = doCreateMaskField(mask, macros);
		tField.setHorizontalAlignment(alignment);
		return tField;
	}
	/**
	 */
	public JMaskField createMaskField(int alignment, String mask, MaskMacros macros, String name) {
		JMaskField tField = createMaskField(alignment, mask, macros);
		if ((name != null) && (name.length() > 0)) {

			addNamedWidget(name, tField);
		} else
			throw new IllegalArgumentException("Name null or empty in CoUserInterfaceBuilder#createMaskedField");
		return tField;
	}
	/**
	 */
	public JMaskField createMaskField(String mask) {
		return createMaskField(JTextField.LEFT, mask, getDefaultMacros());
	}
	/**
	 */
	public JMaskField createMaskField(String mask, String name) {
		return createMaskField(JTextField.LEFT, mask, getDefaultMacros(), name);
	}
	/**
	 */
	public JMaskField createMaskField(String mask, MaskMacros macros) {
		return createMaskField(JTextField.LEFT, mask, macros);
	}
	/**
	 */
	public JMaskField createMaskField(String mask, MaskMacros macros, String name) {
		return createMaskField(JTextField.LEFT, mask, macros, name);
	}
	/**
	 */
	public CoOptionMenu createOptionMenu() {
		return doCreateOptionMenu();
	}
	/**
	 */
	public CoOptionMenu createOptionMenu(Object key) {
		CoOptionMenu m = createOptionMenu();
		if (key != null)
			addNamedWidget(key, m);
		else
			throw new IllegalArgumentException("Key null in CoUserInterfaceBuilder#createComboBox");
		return m;
	}
	/**
	 */
	public CoOptionMenu createOptionMenu(ComboBoxModel m) {
		return doCreateOptionMenu(m);
	}
	public CoOptionMenuAdaptor createOptionMenuAdaptor(CoListValueable lv, CoValueModel vm, CoOptionMenu m) {
		CoOptionMenuAdaptor a = new CoListOptionMenuAdaptor(lv, vm, m);
		vm.addEnableDisableListener(a);
		addUserInterfaceListener(a);
		return a;
	}
	/**
	 */
	public CoOptionMenuAdaptor createOptionMenuAdaptor(CoValueModel aValueModel, CoOptionMenu om) {
		CoOptionMenuAdaptor tAdaptor = new CoOptionMenuAdaptor(aValueModel, om);
		aValueModel.addEnableDisableListener(tAdaptor);
		addUserInterfaceListener(tAdaptor);
		return tAdaptor;
	}
	/**
	 */
	public CoPanel createPanel() {
		return createPanel(new CoBorderLayout(), false, null);
	}
	/**
	 */
	public CoPanel createPanel(LayoutManager layoutManager) {
		return createPanel(layoutManager, false, null);

	}
	/**
	 */
	public CoPanel createPanel(LayoutManager layoutManager, Object key) {
		return createPanel(layoutManager, false, null, key);
	}
	/**
	 */
	public CoPanel createPanel(LayoutManager layoutManager, boolean isDoubleBuffered, Insets extraInsets) {
		CoPanel tPanel = new CoPanel(layoutManager, isDoubleBuffered, extraInsets);
		preparePanel(tPanel);
		return tPanel;

	}
	/**
	 */
	public CoPanel createPanel(LayoutManager layoutManager, boolean isDoubleBuffered, Insets extraInsets, Object key) {
		CoPanel tPanel = createPanel(layoutManager, isDoubleBuffered, extraInsets);
		addNamedWidget(key, tPanel);
		return tPanel;
	}
	/**
	 */
	public JPasswordField createPasswordField(char echoChar, String name) {
		JPasswordField tField = createPasswordField(name);
		tField.setEchoChar(echoChar);
		return tField;
	}
	/**
	 */
	public JPasswordField createPasswordField(String name) {
		JPasswordField tField = doCreatePasswordField();
		if ((name != null) && (name.length() > 0)) {
			addNamedWidget(name, tField);
		} else
			throw new IllegalArgumentException("Name null or empty in CoUserInterfaceBuilder#createPasswordField");
		return tField;
	}
	/**
	 */
	public CoPopupMenuButton createPopupMenuButton(String title, Icon icon) {
		return doCreatePopupMenuButton(title, icon);
	}
	/**
	 */
	public CoPopupMenuButton createPopupMenuButton(String title, Icon icon, ActionListener actionListener) {
		CoPopupMenuButton tButton = createPopupMenuButton(title, icon);
		if (actionListener != null)
			tButton.addActionListener(actionListener);
		return tButton;
	}
	/**
	 */
	public CoPopupMenuButton createPopupMenuButton(String title, Icon icon, ActionListener actionListener, String name) {
		CoPopupMenuButton tButton = createPopupMenuButton(title, icon, actionListener);
		if ((name != null) && (name.length() > 0)) {
			addNamedWidget(name, tButton);
		} else
			throw new IllegalArgumentException("Name null or empty in CoUserInterfaceBuilder#createButton");
		return tButton;
	}
	/**
	 */
	public CoPopupMenuButton createPopupMenuButton(String title, Icon icon, String name) {
		CoPopupMenuButton tButton = createPopupMenuButton(title, icon);
		if ((name != null) && (name.length() > 0)) {
			addNamedWidget(name, tButton);
		} else
			throw new IllegalArgumentException("Name null or empty in CoUserInterfaceBuilder#createButton");
		return tButton;
	}
	/**
	 */
	public CoRadioButton createRadioButton(String title, Icon icon, CoButtonGroup buttonGroup, String actionCommand) {
		CoRadioButton tButton = (icon != null) ? new CoRadioButton(title, icon) : new CoRadioButton(title);
		prepareRadioButton(tButton);
		tButton.setActionCommand(actionCommand);
		tButton.getAccessibleContext().setAccessibleName(actionCommand);
		addNamedWidget(actionCommand, tButton);
		if (buttonGroup != null)
			tButton.setButtonGroup(buttonGroup);
		return tButton;
	}
	/**
	 */
	public CoRadioButton createRadioButton(String title, CoButtonGroup buttonGroup, String actionCommand) {
		return createRadioButton(title, null, buttonGroup, actionCommand);
	}
	/**
	 */
	public CoPanel createRigidBoxPanel(int orientation, boolean isDoubleBuffered, Insets extraInsets) {
		CoPanel tPanel = createRigidPanel(null, isDoubleBuffered, extraInsets);
		BoxLayout tLayout = new BoxLayout(tPanel, orientation);
		tPanel.setLayout(tLayout);
		return tPanel;
	}
	/**
	 */
	public CoPanel createRigidBoxPanel(int orientation, boolean isDoubleBuffered, Insets extraInsets, String name) {
		CoPanel tPanel = createRigidPanel(null, isDoubleBuffered, extraInsets, name);
		BoxLayout tLayout = new BoxLayout(tPanel, orientation);
		tPanel.setLayout(tLayout);
		return tPanel;
	}
	/**
	 */
	public CoPanel createRigidPanel(LayoutManager layoutManager, boolean isDoubleBuffered, Insets extraInsets) {
		CoPanel tPanel = new CoPanel(layoutManager, isDoubleBuffered, extraInsets) {
			public Dimension getMaximumSize() {
				return getPreferredSize();
			}
		};

		if (makeWidgetsTransparent()) {
			tPanel.setOpaque(false);
			tPanel.setDoubleBuffered(false);
		}
		return tPanel;

	}
	/**
	 */
	public CoPanel createRigidPanel(LayoutManager layoutManager, boolean isDoubleBuffered, Insets extraInsets, String name) {
		CoPanel tPanel = createRigidPanel(layoutManager, isDoubleBuffered, extraInsets);
		if ((name != null) && (name.length() > 0)) {
			addNamedWidget(name, tPanel);
		} else
			throw new IllegalArgumentException("Name null or empty in CoUserInterfaceBuilder#createPanel");
		return tPanel;
	}
	/**
	 */
	public CoPanel createRigidPanel(boolean isDoubleBuffered, Insets extraInsets) {
		return createRigidPanel(null, isDoubleBuffered, extraInsets);
	}
	/**
	  */
	public CoScaleButtonAdaptor createScaleButtonAdaptor(CoValueModel aValueModel, AbstractButton aButton, double factor) {
		CoScaleButtonAdaptor tAdaptor = new CoScaleButtonAdaptor(aValueModel, aButton, factor);
		aValueModel.addEnableDisableListener(tAdaptor);
		return tAdaptor;
	}
	public JScrollPane createScrollPane(Component view) {
		return createScrollPane(view, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	public JScrollPane createScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		JScrollPane tScrollPane = new JScrollPane(view, vsbPolicy, hsbPolicy);
		tScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		return tScrollPane;
	}
	/**
	 * @return SE.corren.calvin.userinterface.CoSimpleSubcanvasAdaptor
	 * @param aValueModel SE.corren.calvin.userinterface.CoValueable
	 * @param aSubcanvas SE.corren.calvin.userinterface.CoSubcanvas
	 */
	public CoSimpleSubcanvasAdaptor createSimpleSubcanvasAdaptor(CoValueable aValueModel, CoSubcanvas aSubcanvas) {
		CoSimpleSubcanvasAdaptor tAdaptor = new CoSimpleSubcanvasAdaptor(aValueModel, aSubcanvas);
		if (aValueModel != null)
			aValueModel.addEnableDisableListener(tAdaptor);
		addUserInterfaceListener(tAdaptor);
		return tAdaptor;
	}
	public CoComboBox createSlimComboBox() {
		CoComboBox comboBox = new CoSlimComboBox();
		prepareComboBox(comboBox);
		comboBox.setOpaque(false);
		comboBox.setBorder(null);
		return comboBox;
	}
	public CoComboBox createSlimComboBox(Object key) {
		CoComboBox comboBox = createSlimComboBox();
		if (key != null) {
			addNamedWidget(key, comboBox);
		} else {
			throw new IllegalArgumentException("Key null in CoUserInterfaceBuilder#createSlimComboBox");
		}
		return comboBox;
	}
	/**
	 */
	public CoComboBox createSlimComboBox(ListCellRenderer renderer) {
		CoComboBox tComboBox = createSlimComboBox();
		tComboBox.setRenderer(renderer);
		return tComboBox;
	}
	public CoComboBox createSlimComboBox(ListCellRenderer renderer, Object key) {
		CoComboBox tComboBox = createSlimComboBox(renderer);
		if (key != null)
			addNamedWidget(key, tComboBox);
		else
			throw new IllegalArgumentException("Key null in CoUserInterfaceBuilder#createComboBox");
		return tComboBox;
	}
	public CoLabel createSlimHeadlineLabel(String headline) {
		CoLabel label = createLabel(headline);
		prepareSlimHeadlineLabel(label);
		return label;
	}
	/**
	 */
	public CoRadioButton createSlimRadioButton(String title, CoButtonGroup buttonGroup, String actionCommand) {
		CoRadioButton button = createRadioButton(title, null, buttonGroup, actionCommand);
		button.setBorder(null);
		return button;
	}
	public CoTextField createSlimTextField() {
		CoTextField textField = createTextField();
		textField.setBorder(BorderFactory.createEtchedBorder());
		return textField;
	}
	public CoTextField createSlimTextField(int alignment) {
		CoTextField textField = createTextField(alignment);
		textField.setBorder(BorderFactory.createEtchedBorder());
		return textField;
	}
	public CoTextField createSlimTextField(int alignment, int columns) {
		CoTextField textField = createTextField(alignment, columns);
		textField.setBorder(BorderFactory.createEtchedBorder());
		return textField;
	}
	public CoTextField createSlimTextField(int alignment, int columns, int maxColumns) {
		CoTextField textField = createTextField(alignment, columns, maxColumns);
		textField.setBorder(BorderFactory.createEtchedBorder());
		return textField;
	}
	public CoTextField createSlimTextField(int alignment, int columns, int maxColumns, Object key) {
		CoTextField textField = createTextField(alignment, columns, maxColumns, key);
		textField.setBorder(BorderFactory.createEtchedBorder());
		addNamedWidget(key, textField);
		return textField;
	}
	public CoTextField createSlimTextField(int alignment, int columns, Object key) {
		CoTextField textField = createTextField(alignment, columns, key);
		textField.setBorder(BorderFactory.createEtchedBorder());
		addNamedWidget(key, textField);
		return textField;
	}
	public CoTextField createSlimTextField(int alignment, Object key) {
		CoTextField textField = createTextField(alignment, key);
		textField.setBorder(BorderFactory.createEtchedBorder());
		//	addNamedWidget(key, textField);
		return textField;
	}
	public CoTextField createSlimTextField(Object key) {
		CoTextField textField = createTextField(key);
		textField.setBorder(BorderFactory.createEtchedBorder());
		addNamedWidget(key, textField);
		return textField;
	}
	/**
		Creates and returns a panel with a 
		label above a text field.
	*/
	public CoPanel createSlimTextFieldPanel(int alignment, int columns, int maxColumns, String label, String fieldKey) {
		CoPanel tPanel = createBoxPanel(BoxLayout.Y_AXIS, false, null);
		tPanel.add(createLabel(label));
		tPanel.add(createSlimTextField(alignment, columns, maxColumns, fieldKey));

		return tPanel;
	}
	/**
	 */
	public CoTriStateCheckBox createSlimTriStateCheckBox() {
		CoTriStateCheckBox b = doCreateSlimTriStateCheckBox();
		return b;
	}
	/**
	 */
	public CoTriStateCheckBox createSlimTriStateCheckBox(String title) {
		CoTriStateCheckBox b = doCreateSlimTriStateCheckBox(title);
		return b;
	}
	/**
	 */
	public CoTriStateCheckBox createSlimTriStateCheckBox(String title, Object key) {
		CoTriStateCheckBox b = doCreateSlimTriStateCheckBox(title);
		addNamedWidget(key, b);
		return b;
	}
	/**
	 */
	public CoSplitPane createSplitPane(boolean continuousLayout) {
		CoSplitPane tSplitPane = new CoSplitPane();
		prepareSplitPane(tSplitPane);
		tSplitPane.setContinuousLayout(continuousLayout);
		return tSplitPane;
	}
	/**
	 */
	public CoSplitPane createSplitPane(boolean continuousLayout, Component leftComponent, Component rightComponent) {
		CoSplitPane tSplitPane = createSplitPane(continuousLayout);
		if (leftComponent != null)
			tSplitPane.setLeftComponent(leftComponent);
		if (rightComponent != null)
			tSplitPane.setRightComponent(rightComponent);
		return tSplitPane;

	}
	/**
	 */
	public CoSplitPane createSplitPane(boolean continuousLayout, Component leftComponent, Component rightComponent, String name) {
		CoSplitPane tSplitPane = createSplitPane(continuousLayout);
		if (leftComponent != null)
			tSplitPane.setLeftComponent(leftComponent);
		if (rightComponent != null)
			tSplitPane.setRightComponent(rightComponent);
		if ((name != null) && (name.length() > 0)) {
			addNamedWidget(name, tSplitPane);
		} else
			throw new IllegalArgumentException("Name null or empty in CoUserInterfaceBuilder#createPanel");
		return tSplitPane;

	}
	/**
	 */
	public CoSplitPane createSplitPane(boolean continuousLayout, String name) {
		return createSplitPane(continuousLayout, null, null, name);
	}
	/**
	 */
	public CoSubcanvas createSubcanvas() {
		CoSubcanvas tCanvas = new CoSubcanvas();
		prepareSubcanvas(tCanvas);
		return tCanvas;
	}
	/**
	 */
	public CoSubcanvas createSubcanvas(LayoutManager layoutManager) {
		CoSubcanvas tSubcanvas = new CoSubcanvas(layoutManager);
		prepareSubcanvas(tSubcanvas);
		return tSubcanvas;
	}
	/**
	 */
	public CoSubcanvas createSubcanvas(LayoutManager layoutManager, Object key) {
		CoSubcanvas tSubcanvas = createSubcanvas(layoutManager);
		addNamedWidget(key, tSubcanvas);
		return tSubcanvas;
	}
	/**
		Svara med en CoSubcanvas med gränssnittsmodellen 'userInterface' 
		installerad.
	 */
	public CoSubcanvas createSubcanvas(LayoutManager layoutManager, CoUserInterface userInterface) {
		CoSubcanvas tSubcanvas = createSubcanvas(layoutManager);
		tSubcanvas.setUserInterface(userInterface);
		return tSubcanvas;
	}
	/**
	 */
	public CoSubcanvas createSubcanvas(LayoutManager layoutManager, CoUserInterface userInterface, Object key) {
		CoSubcanvas tSubcanvas = createSubcanvas(layoutManager, userInterface);
		addNamedWidget(key, tSubcanvas);
		return tSubcanvas;
	}
	/**
	 */
	public CoSubcanvas createSubcanvas(Object key) {
		CoSubcanvas tSubcanvas = createSubcanvas();
		addNamedWidget(key, tSubcanvas);
		return tSubcanvas;
	}
	/**
		Svara med en CoSubcanvas med gränssnittsmodellen 'userInterface' 
		installerad.
	 */
	public CoSubcanvas createSubcanvas(CoUserInterface userInterface) {
		CoSubcanvas tSubcanvas = createSubcanvas();
		tSubcanvas.setUserInterface(userInterface);
		return tSubcanvas;
	}
	/**
	 */
	public CoSubcanvas createSubcanvas(CoUserInterface userInterface, Object key) {
		CoSubcanvas tSubcanvas = createSubcanvas(userInterface);
		addNamedWidget(key, tSubcanvas);
		return tSubcanvas;
	}
	/**
	 * @return SE.corren.calvin.userinterface.CoSubcanvasAdaptor
	 * @param aValueModel SE.corren.calvin.userinterface.CoValueable
	 * @param aSubcanvas SE.corren.calvin.userinterface.CoSubcanvas
	 */
	public CoSubcanvasAdaptor createSubcanvasAdaptor(CoValueable aValueModel, CoSubcanvas aSubcanvas) {
		CoSubcanvasAdaptor tAdaptor = new CoSubcanvasAdaptor(aValueModel, aSubcanvas);
		aValueModel.addEnableDisableListener(tAdaptor);
		addUserInterfaceListener(tAdaptor);
		return tAdaptor;
	}
	/**
	 */
	public CoTabbedPane createTabbedPane() {

		return createTabbedPane(SwingConstants.TOP);
	}
	/**
	 */
	public CoTabbedPane createTabbedPane(int tabPlacement) {

		CoTabbedPane tTabbedPane = doCreateTabbedPane(tabPlacement);
		return tTabbedPane;
	}
	/**
	 */
	public CoTabbedPane createTabbedPane(int tabPlacement, String name) {

		CoTabbedPane tTabbedPane = doCreateTabbedPane(tabPlacement);
		if ((name != null) && (name.length() > 0)) {
			addNamedWidget(name, tTabbedPane);
		} else
			throw new IllegalArgumentException("Name null or empty in CoUserInterfaceBuilder#createTabbedPane");
		return tTabbedPane;
	}
	/**
	 */
	public CoTabbedPane createTabbedPane(String name) {

		return createTabbedPane(SwingConstants.TOP, name);
	}
	/**
	 */
	public JTable createTable() {
		return doCreateTable();
	}
	public JTable createTable(Object key) {
		return doCreateTable(key);
	}
	/**
	 * Create a <code>CoTable</code> inside a <code>JScrollPane<code>.
	 */
	public CoTableBox createTableBox() {
		return doCreateTableBox();
	}
	/**
	 * Create a <code>CoTable</code> inside a <code>JScrollPane<code>.
	 */
	public CoTableBox createTableBox(Object key) {
		return doCreateTableBox(key);
	}
	/**
	 */
	public CoTableBoxAdaptor createTableBoxAdaptor(CoTableAspectAdaptor aValueable, CoTableBox aTableBox) {
		CoTableBoxAdaptor tAdaptor = new CoTableBoxAdaptor(aValueable, aTableBox);
		aValueable.addEnableDisableListener(tAdaptor);
		return tAdaptor;
	}
	/**
	 */
	public CoTextArea createTextArea() {
		CoTextArea tTextArea = new CoTextArea();
		prepareTextArea(tTextArea);
		return tTextArea;
	}
	public CoTextArea createTextArea(Object key) {
		CoTextArea tArea = createTextArea();
		addNamedWidget(key, tArea);
		return tArea;
	}
	/**
	 */
	public CoTextAreaAdaptor createTextAreaAdaptor(CoValueModel aValueModel, JTextArea aTextArea) {
		CoTextAreaAdaptor tAdaptor = new CoTextAreaAdaptor(aValueModel, aTextArea);
		aValueModel.addEnableDisableListener(tAdaptor);
		addUserInterfaceListener(tAdaptor);
		return tAdaptor;
	}
	/**
	 */
	public CoTextField createTextField() {
		return doCreateTextField();
	}
	/**
	 */
	public CoTextField createTextField(int alignment) {
		CoTextField tField = createTextField();
		tField.setHorizontalAlignment(alignment);
		return tField;
	}
	public CoTextField createTextField(int alignment, int columns) {
		CoTextField tField = createTextField(alignment);
		tField.setColumns(columns);
		return tField;
	}
	public CoTextField createTextField(int alignment, int columns, int maxColumns) {
		CoTextField field = createTextField(alignment, columns);
		field.setMaxColumns(maxColumns);
		return field;
	}
	public CoTextField createTextField(int alignment, int columns, int maxColumns, Object key) {
		CoTextField field = createTextField(alignment, columns, key);
		field.setMaxColumns(maxColumns);
		return field;
	}
	public CoTextField createTextField(int alignment, int columns, Object key) {
		CoTextField tField = createTextField(alignment, key);
		tField.setColumns(columns);
		return tField;
	}
	/**
	 */
	public CoTextField createTextField(int alignment, Object key) {
		CoTextField tField = createTextField(alignment);
		addNamedWidget(key, tField);
		return tField;
	}
	/**
	 */
	public CoTextField createTextField(Object key) {
		CoTextField tField = createTextField();
		addNamedWidget(key, tField);
		return tField;
	}
	/**
	 */
	public CoTextFieldAdaptor createTextFieldAdaptor(CoConverter aValueModel, JTextField aTextField) {
		return doCreateTextFieldAdaptor(aValueModel, aTextField);
	}
	/**
	 */
	public CoTextFieldAdaptor createTextFieldAdaptor(CoValueModel aValueModel, JTextField aTextField) {
		return doCreateTextFieldAdaptor(new CoNullConverter(aValueModel), aTextField);
	}
	/**
		Creates and returns a panel with a 
		label above a text field.
	*/
	public CoPanel createTextFieldPanel(int alignment, int columns, int maxColumns, String label, String fieldKey) {
		CoPanel tPanel = createBoxPanel(BoxLayout.Y_AXIS, false, null);
		tPanel.add(createLabel(label));
		tPanel.add(createTextField(alignment, columns, maxColumns, fieldKey));

		return tPanel;
	}

	/**
	 */
	public CoTitledBorder createTitledBorder(String title, int titlePosition) {
		CoTitledBorder tTitledBorder = new CoTitledBorder(title, titlePosition);
		prepareTitledBorder(tTitledBorder);
		return tTitledBorder;
	}
	/**
	 */
	public CoToggleButton createToggleButton(String title, Icon icon, CoButtonGroup buttonGroup, String actionCommand) {
		CoToggleButton tButton;
		if (title == null) {
			tButton = (icon != null) ? new CoToggleButton(icon) : new CoToggleButton();
		} else {
			tButton = (icon != null) ? new CoToggleButton(title, icon) : new CoToggleButton(title);
		}
		prepareToggleButton(tButton);
		tButton.setActionCommand(actionCommand);
		tButton.getAccessibleContext().setAccessibleName(actionCommand);
		addNamedWidget(actionCommand, tButton);
		if (buttonGroup != null)
			tButton.setButtonGroup(buttonGroup);
		return tButton;
	}
	/**
	 */
	public CoToggleButton createToggleButton(String title, CoButtonGroup buttonGroup, String actionCommand) {
		return createToggleButton(title, null, buttonGroup, actionCommand);
	}
	/**
	 */
	public CoToggleButtonAdaptor createToggleButtonAdaptor(CoValueModel aValueModel, JToggleButton aButton) {
		CoToggleButtonAdaptor tAdaptor = new CoToggleButtonAdaptor(aValueModel, aButton);
		aValueModel.addEnableDisableListener(tAdaptor);
		return tAdaptor;
	}
	/**
	 */
	public CoToolbar createToolbar() {
		CoToolbar tb = new CoToolbar();
		prepareToolbar(tb);
		return tb;
	}
	/**
	 */
	public CoToolbar createToolbar(String name) {
		CoToolbar tb = createToolbar();
		if ((name != null) && (name.length() > 0)) {
			addNamedWidget(name, tb);
		} else {
			throw new IllegalArgumentException("Name null or empty in CoUserInterfaceBuilder#createToolbar");
		}

		return tb;
	}
	/**
	 */
	public CoToolbar createToolbar(String name, CoToolbarDockingCriteriaIF dc) {
		CoToolbar tb = createToolbar(dc);
		if ((name != null) && (name.length() > 0)) {
			addNamedWidget(name, tb);
		} else {
			throw new IllegalArgumentException("Name null or empty in CoUserInterfaceBuilder#createToolbar");
		}

		return tb;
	}

	public CoToolbar createToolbar(CoToolbarDockingCriteriaIF dc) {
		CoToolbar tb = new CoToolbar(dc);
		prepareToolbar(tb);
		return tb;
	}

	public CoTreeBox createTreeBox() {
		return doCreateTreeBox();
	}

	public CoTreeBox createTreeBox(Object key) {
		return doCreateTreeBox(key);
	}

	public CoTreeBox createTreeBox(TreeCellRenderer renderer) {
		CoTreeBox tTreeBox = doCreateTreeBox();
		tTreeBox.getTreeView().setCellRenderer(renderer);
		return tTreeBox;
	}

	public CoTreeBox createTreeBox(TreeCellRenderer renderer, Object key) {
		CoTreeBox tTreeBox = doCreateTreeBox(key);
		tTreeBox.getTreeView().setCellRenderer(renderer);
		return tTreeBox;
	}
	/**
	 * @return SE.corren.calvin.userinterface.CoTreeBoxAdaptor
	 * @param aValueModel SE.corren.calvin.userinterface.CoTreeValueable
	 * @param aTreeBox SE.corren.calvin.userinterface.CoTreeBox
	  */
	public CoTreeBoxAdaptor createTreeBoxAdaptor(CoTreeValueable treeHolder, CoTreeBox aTreeBox) {
		CoTreeBoxAdaptor tAdaptor = new CoTreeBoxAdaptor(treeHolder, aTreeBox);
		treeHolder.addEnableDisableListener(tAdaptor);
		return tAdaptor;
	}
	/**
	 */
	public CoTriStateCheckBox createTriStateCheckBox() {
		CoTriStateCheckBox b = doCreateTriStateCheckBox();
		return b;
	}
	/**
	 */
	public CoTriStateCheckBox createTriStateCheckBox(String title) {
		CoTriStateCheckBox b = doCreateTriStateCheckBox(title);
		return b;
	}
	/**
	 */
	public CoTriStateCheckBox createTriStateCheckBox(String title, Object key) {
		CoTriStateCheckBox b = doCreateTriStateCheckBox(title);
		addNamedWidget(key, b);
		return b;
	}
	public CoTriStateCheckBoxAdaptor createTriStateCheckBoxAdaptor(CoValueModel vm, CoTriStateCheckBox cb) {
		CoTriStateCheckBoxAdaptor a = new CoTriStateCheckBoxAdaptor(vm, cb);
		vm.addEnableDisableListener(a);
		return a;
	}
	private CoUserInterfaceEvent createUserInterfaceEvent(int eventID) {
		return new CoUserInterfaceEvent(userInterface, eventID);
	}

	private CoUserInterfaceEvent createUserInterfaceEventFrom(WindowEvent e) {
		int tEventID = -1;
		switch (e.getID()) {
			case WindowEvent.WINDOW_CLOSING :
				tEventID = CoUserInterfaceEvent.UI_CLOSING;
				break;
			case WindowEvent.WINDOW_CLOSED :
				tEventID = CoUserInterfaceEvent.UI_CLOSED;
				break;
			case WindowEvent.WINDOW_ACTIVATED :
				tEventID = CoUserInterfaceEvent.UI_ACTIVATED;
				break;
			case WindowEvent.WINDOW_DEACTIVATED :
				tEventID = CoUserInterfaceEvent.UI_DEACTIVATED;
				break;
			case WindowEvent.WINDOW_OPENED :
				tEventID = CoUserInterfaceEvent.UI_OPENED;
				break;
		}

		if (tEventID != -1)
			return new CoUserInterfaceEvent(userInterface, tEventID);
		else
			throw new IllegalArgumentException("WindowEvent with id: " + e.getID() + " is not yet supported");
	}

	private CoUserInterfaceEvent createUserInterfaceEventFrom(InternalFrameEvent e) {
		int tEventID = -1;
		switch (e.getID()) {
			case InternalFrameEvent.INTERNAL_FRAME_CLOSING :
				tEventID = CoUserInterfaceEvent.UI_CLOSING;
				break;
			case InternalFrameEvent.INTERNAL_FRAME_CLOSED :
				tEventID = CoUserInterfaceEvent.UI_CLOSED;
				break;
			case InternalFrameEvent.INTERNAL_FRAME_ACTIVATED :
				tEventID = CoUserInterfaceEvent.UI_ACTIVATED;
				break;
			case InternalFrameEvent.INTERNAL_FRAME_DEACTIVATED :
				tEventID = CoUserInterfaceEvent.UI_DEACTIVATED;
				break;
			case InternalFrameEvent.INTERNAL_FRAME_OPENED :
				tEventID = CoUserInterfaceEvent.UI_OPENED;
				break;
		}

		if (tEventID != -1)
			return new CoUserInterfaceEvent(userInterface, tEventID);
		else
			throw new IllegalArgumentException("InternalFrameEvent with id: " + e.getID() + " is not yet supported");
	}
	/**
	 */
	public CoValueHolder createValueHolder(String name, Object initialValue) {
		CoValueHolder tValueHolder = new CoValueHolder(initialValue, name);
		if (name != null)
			addNamedValueModel(name, tValueHolder);
		return tValueHolder;
	}
	/**
	 */
	public CoVerticalToolbarDockingBay createVerticalToolbarDockingBay() {
		CoVerticalToolbarDockingBay tbdb = new CoVerticalToolbarDockingBay();
		prepareToolbarDockingBay(tbdb);
		return tbdb;
	}
	public CoVerticalToolbarDockingBay createVerticalToolbarDockingBay(Object key) {
		CoVerticalToolbarDockingBay tbdb = createVerticalToolbarDockingBay();
		addNamedWidget(key, tbdb);
		return tbdb;
	}
	/**
	 */
	public CoVisibleComponentAdaptor createVisibleComponentAdaptor(CoValueModel aValueModel, JComponent aComponent) {
		CoVisibleComponentAdaptor tAdaptor = new CoVisibleComponentAdaptor(aValueModel, aComponent);
		aValueModel.addEnableDisableListener(tAdaptor);
		addUserInterfaceListener(tAdaptor);
		return tAdaptor;
	}
	/**
	 */
	protected CoButton doCreateButton(String title, Icon icon) {
		CoButton tButton = new CoButton(title, icon);
		prepareButton(tButton);
		return tButton;
	}
	/**
	 */
	protected CoButton doCreateButton(AbstractAction action) {
		CoButton tButton = new CoButton(action);
		prepareButton(tButton);
		return tButton;
	}
	/**
	 */
	protected CoCheckBox doCreateCheckBox(String title, Icon icon) {
		CoCheckBox tButton = (icon != null) ? new CoCheckBox(title, icon) : new CoCheckBox(title);
		prepareCheckBox(tButton);
		return tButton;
	}
	/**
	 */
	protected CoComboBox doCreateComboBox() {
		CoComboBox tComboBox = new CoComboBox();
		prepareComboBox(tComboBox);
		return tComboBox;
	}
	/**
	 */
	protected CoListBox doCreateListBox() {
		return doCreateListBox(new CoList());
	}
	/**
	 */
	protected CoListBox doCreateListBox(CoList list) {
		CoListBox tListBox = new CoListBox(list);
		prepareListBox(tListBox);
		return tListBox;
	}
	/**
	 */
	protected JMaskField doCreateMaskField(String mask, MaskMacros macros) {
		JMaskField tField = new JMaskField(mask, macros);
		return tField;
	}
	/**
	 */
	protected CoOptionMenu doCreateOptionMenu() {
		CoOptionMenu m = new CoOptionMenu();
		prepareOptionMenu(m);
		return m;
	}
	/**
	 */
	protected CoOptionMenu doCreateOptionMenu(ComboBoxModel model) {
		CoOptionMenu m = new CoOptionMenu(model);
		prepareOptionMenu(m);
		return m;
	}
	/**
	 */
	protected JPasswordField doCreatePasswordField() {
		JPasswordField tField = new JPasswordField() {
			protected void paintComponent(Graphics g) {

				Color parentBkg = getParent().getBackground();
				if (parentBkg != null) {
					Color tmp = g.getColor();
					g.setColor(parentBkg);
					g.fillRect(0, 0, getWidth(), getHeight());
					g.setColor(tmp);
				}

				Border border = this.getBorder();
				if (border != null) {
					boolean wasOpaque = isOpaque();
					try {
						Color tmp = g.getColor();
						g.setColor(getBackground());
						Rectangle rectangle = AbstractBorder.getInteriorRectangle(this, border, 0, 0, getWidth(), getHeight());
						g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
						g.setColor(tmp);
						setOpaque(false);
						super.paintComponent(g);
					} finally {
						setOpaque(wasOpaque);
					}
				} else
					super.paintComponent(g);

			}
		};
		prepareTextField(tField);
		return tField;
	}
	/**
	 */
	protected CoPopupMenuButton doCreatePopupMenuButton(String title, Icon icon) {
		CoPopupMenuButton tButton = new CoPopupMenuButton(title, icon);
		prepareButton(tButton);
		return tButton;
	}
	/**
	 */
	protected CoTriStateCheckBox doCreateSlimTriStateCheckBox() {
		CoTriStateCheckBox b = doCreateTriStateCheckBox();
		b.setMargin(m_noInsets);
		return b;
	}
	/**
	 */
	protected CoTriStateCheckBox doCreateSlimTriStateCheckBox(String title) {
		CoTriStateCheckBox b = doCreateTriStateCheckBox(title);
		b.setMargin(m_noInsets);
		return b;
	}
	/**
	 */
	protected CoTabbedPane doCreateTabbedPane(int tabPlacement) {
		CoTabbedPane tTabbedPane = new CoTabbedPane(tabPlacement);
		prepareTabbedPane(tTabbedPane);
		return tTabbedPane;
	}
	/**
	 */
	protected JTable doCreateTable() {
		JTable tTable = new JTable();
		prepareTable(tTable);
		return tTable;
	}
	protected JTable doCreateTable(Object key) {
		JTable tTable = doCreateTable();
		addNamedWidget(key, tTable);
		return tTable;
	}
	protected CoTableBox doCreateTableBox() {
		CoTableBox tableBox = new CoTableBox() {
			public void addNotify() {
				super.addNotify();
				setBorder(BorderFactory.createCompoundBorder(SCROLL_FOCUS_BORDER, UIManager.getBorder("Table.scrollPaneBorder")));
			}
		};
		prepareTableBox(tableBox);
		return tableBox;
	}
	protected CoTableBox doCreateTableBox(Object key) {
		CoTableBox tableBox = doCreateTableBox();
		addNamedWidget(key, tableBox);
		return tableBox;
	}

	protected CoTextField doCreateTextField() {
		CoTextField tTextField = new CoTextField();
		prepareTextField(tTextField);
		return tTextField;
	}

	protected CoTextFieldAdaptor doCreateTextFieldAdaptor(CoConverter converter, JTextField textField) {
		CoTextFieldAdaptor adaptor = new CoTextFieldAdaptor(converter, textField);
		addValueable(converter);
		converter.addEnableDisableListener(adaptor);
		addUserInterfaceListener(adaptor);
		return adaptor;
	}


	protected CoTreeBox doCreateTreeBox() {
		CoTreeBox tTreeBox = new CoTreeBox();
		prepareTreeBox(tTreeBox);
		return tTreeBox;
	}

	protected CoTreeBox doCreateTreeBox(Object key) {
		CoTreeBox tTreeBox = doCreateTreeBox();
		addNamedWidget(key, tTreeBox);
		return tTreeBox;
	}

	protected CoTriStateCheckBox doCreateTriStateCheckBox() {
		CoTriStateCheckBox b = new CoTriStateCheckBox();
		prepareTriStateCheckBox(b);
		if (makeWidgetsTransparent())
			b.setOpaque(false);
		return b;
	}

	protected CoTriStateCheckBox doCreateTriStateCheckBox(String title) {
		CoTriStateCheckBox b = new CoTriStateCheckBox(title);
		prepareTriStateCheckBox(b);
		if (makeWidgetsTransparent())
			b.setOpaque(false);
		return b;
	}
	/**
	 * Enablar/disablar alla gränssnittskomponenter som editeras
	 * via ComponentAdaptors genom posta ett <code>CoEnableDisableEvent</code>.<br>
	 */
	public void enableDisable(boolean enable) {
		if (valueModels != null) {
			Iterator iter = valueModels.values().iterator();
			while (iter.hasNext()) {
				CoValueable iValueable = (CoValueable) iter.next();
				iValueable.setEnabled(enable);
			}
		}
		fireEnableDisableEvent(enable);
	}
	/**
	 */
	protected void fireEnableDisableEvent(boolean enable) {
		Object tListeners[] = listenerList.getListenerList();
		CoEnableDisableEvent tEvent = null;
		Class tListenerClass = CoEnableDisableListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				if (tEvent == null)
					tEvent = new CoEnableDisableEvent(this, enable);
				((CoEnableDisableListener) tListeners[i + 1]).enableDisable(tEvent);
			}
		}

	}
	/**
	 */
	private void fireUserInterfaceEvent(CoUserInterfaceEvent e) {
		Object tListeners[] = listenerList.getListenerList();
		Class tListenerClass = CoUserInterfaceListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				if (e.isConsumed())
					break;
				((CoUserInterfaceListener) tListeners[i + 1]).processUserInterfaceEvent(e);
			}
		}

	}
	/**
	 */
	private Border getBorder(String key) {
		return UIManager.getBorder(key);
	}
	/**
	 */
	public Color getColor(String key) {
		return UIManager.getColor(key);
	}
	/**
	 */
	private MaskMacros getDefaultMacros() {
		String tNumericalMacros[] = CoUIStringResources.getBundle().getStringArray(DEFAULT_NUMERICAL_MACROS);
		String tAlphaNumericalMacros[] = CoUIStringResources.getBundle().getStringArray(DEFAULT_ALPHA_NUMERICAL_MACROS);

		MaskMacros tMacros = new MaskMacros();
		tMacros.addMacro(tNumericalMacros[0].charAt(0), tNumericalMacros[1]);
		tMacros.addMacro(tAlphaNumericalMacros[0].charAt(0), tAlphaNumericalMacros[1]);

		return tMacros;
	}
	/**
	 */
	public Font getFont(String key) {
		return UIManager.getFont(key);
	}
	/**
	 */
	private Icon getIcon(String key) {
		return UIManager.getIcon(key);
	}
	/**
	 */
	private Insets getInsets(String key) {
		return UIManager.getInsets(key);
	}
	/**
	 */
	public InternalFrameListener getInternalFrameListener() {
		if (internalFrameListener == null)
			internalFrameListener = new BuilderInternalFrameListener();
		return internalFrameListener;
	}
	/**
	 	Svarar med den namngivna valuemodel i min Hashtable
	 	som har nyckeln 'key'. Som nyckel används oftast
	 	dess namn.
	 */
	public CoValueable getNamedValueModel(Object key) {
		return (valueModels != null) ? (CoValueable) valueModels.get(key) : null;
	}
	public Collection getNamedValueModels() {
		return (valueModels != null) ? valueModels.values() : null;
	}
	/**
	 	Svarar med den namngivna komponent i min Hashtable
	 	som har nyckeln 'key'. Som nyckel används oftast
	 	komponentens namn.
	 */
	public final Component getNamedWidget(Object key) {
		return (widgets != null) ? (Component) widgets.get(key) : null;
	}
	public CoUserInterface getUserInterface() {
		return userInterface;
	}
	/**
	 */
	public WindowListener getWindowListener() {
		if (windowListener == null)
			windowListener = new BuilderWindowListener();
		return windowListener;
	}
	/**
	 * Checking for existence of a userinterface.
	 */
	protected boolean isLightWeightPopupEnabled() {
		return userInterface == null || userInterface.isLightWeightPopupEnabled();
	}
	public boolean makeWidgetsTransparent() {
		return false; //defaults.getBoolean(TRANSPARENT_WIDGETS) || transparentWidgets ;
	}
	/**
	 */
	protected void prepareAbstractButton(AbstractButton aButton) {
		aButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		aButton.setFocusPainted(true);
	}
	/**
	 */
	public void prepareButton(JButton aButton) {
		prepareAbstractButton(aButton);
	}
	/**
	 */
	public void prepareCheckBox(JCheckBox aCheckBox) {
		prepareAbstractButton(aCheckBox);
	}
	/**
	 */
	public void prepareChooserPanel(CoChooserPanel aPanel) {
		aPanel.setBackground(getColor(PANEL_BACKGROUND), true);
		aPanel.setForeground(getColor(PANEL_FOREGROUND), true);
		aPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		aPanel.setOpaque(false, true);
	}
	/**
	 */
	public void prepareComboBox(JComboBox aComboBox) {
		aComboBox.setDoubleBuffered(true);
		aComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		addFocusBorderTo(aComboBox);
		aComboBox.setLightWeightPopupEnabled(isLightWeightPopupEnabled());

	}
	public void prepareHeadlineLabel(CoLabel label) {
		label.setFont(getFont(HEADLINE_FONT));
		label.setOpaque(false);
		label.setForeground(getColor(HEADLINE_COLOR));
	}
	/**
	 * Prepare a icon labeled text field.
	 *
	 * PENDING: Unify handling of iconed and non-iconed fields better. /Markus
	 *
	 * NOTE: Since CoIconLabeledTextField handles focus borders differently
	 * than other text fields, prepareTextField() is not called. Instead,
	 * for efficiency, most of the code from there is duplicated below.
	 * /Markus 2001-05-17
	 */
	public void prepareIconLabeledTextField(CoIconLabeledTextField textField) {
		// Copied from prepareTextField().
		textField.setDoubleBuffered(true);
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);
		// Special for labeled text fields.
		textField.setTextFieldBorder(FOCUS_BORDER);
	}
	/**
	 */
	public void prepareLabel(JLabel aLabel) {
		aLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
	}
	/**
	 * Prepare a labeled text field.
	 *
	 * NOTE: Since CoLabeledTextField handles focus borders differently
	 * than other text fields, prepareTextField() is not called. Instead,
	 * for efficiency, most of the code from there is duplicated below.
	 * /Markus 2001-05-16
	 */
	public void prepareLabeledTextField(CoLabeledTextField textField) {
		// Copied from prepareTextField().
		textField.setDoubleBuffered(true);
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);
		// Special for labeled text fields.
		textField.setTextFieldBorder(FOCUS_BORDER);
	}
	/**
	 */
	public void prepareList(JList aList) {
	}
	public void prepareListBox(CoListBox aListBox) {
		prepareList(aListBox.getList());
		aListBox.setDoubleBuffered(true);
		aListBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		addScrollFocusBorderTo(aListBox);
	}
	/**
	 */
	public void prepareOptionMenu(CoOptionMenu m) {
		m.setFont(getFont(OPTION_MENU_FONT));
		m.setBackground(getColor(OPTION_MENU_BACKGROUND));
		m.setForeground(getColor(OPTION_MENU_FOREGROUND));
		m.setDoubleBuffered(true);
		if (makeWidgetsTransparent())
			m.setOpaque(false);
	}

	public void preparePanel(JPanel aPanel) {
		aPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	}

	public void prepareRadioButton(JRadioButton aRadioButton) {
		prepareAbstractButton(aRadioButton);
	}
	public void prepareSlimHeadlineLabel(CoLabel label) {
		label.setFont(getFont(SLIM_HEADLINE_FONT));
		label.setOpaque(false);
		label.setForeground(getColor(HEADLINE_COLOR));
	}

	public void prepareSplitPane(JSplitPane aSplitPane) {
		aSplitPane.setOneTouchExpandable(true);
		aSplitPane.setContinuousLayout(true);
		aSplitPane.setAlignmentX(Component.LEFT_ALIGNMENT);
	}

	public void prepareSubcanvas(CoSubcanvas aSubcanvas) {
		preparePanel(aSubcanvas);
	}

	public void prepareTabbedPane(JTabbedPane aTabbedPane) {
		aTabbedPane.setDoubleBuffered(true);
		aTabbedPane.setAlignmentX(Component.LEFT_ALIGNMENT);
	}

	public void prepareTable(JTable table) {
		table.setDoubleBuffered(true);
		table.setAlignmentX(Component.LEFT_ALIGNMENT);
		table.setOpaque(true);
	}
	public void prepareTableBox(CoTableBox tableBox) {
		prepareTable(tableBox.getTable());
		tableBox.setBackground(getColor(TABLE_BACKGROUND));
		tableBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		tableBox.setOpaque(true);
	}

	public void prepareTextArea(JTextArea aTextArea) {
		aTextArea.setDoubleBuffered(true);
		aTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
	}

	/**
	 * Prepare a text field.
	 *
	 * NOTE: Since CoLabeledTextField handles focus borders differently
	 * this method is NOT called from prepareLabeledTextField(). Instead,
	 * for efficiency, the code below is nearly duplicated there.
	 * /Markus 2001-05-16
	 */
	public void prepareTextField(JTextField textField) {
		textField.setDoubleBuffered(true);
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);
		addFocusBorderTo(textField);
	}

	public void prepareTitledBorder(TitledBorder border) {
	}

	public void prepareToggleButton(JToggleButton aButton) {
		prepareAbstractButton(aButton);
	}

	public void prepareToolbar(CoToolbar tb) {
		preparePanel(tb);
	}

	public void prepareToolbarDockingBay(CoToolbarDockingBay tbdb) {
		preparePanel(tbdb);
	}

	public void prepareTree(JTree aTree) {
	}

	public void prepareTreeBox(CoTreeBox aTreeBox) {
		prepareTree(aTreeBox.getTreeView());
		aTreeBox.setDoubleBuffered(true);
		aTreeBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		addScrollFocusBorderTo(aTreeBox);
	}

	public void prepareTriStateCheckBox(JCheckBox aCheckBox) {
		prepareAbstractButton(aCheckBox);
	}
	/**
	 * @param l se.corren.calvin.userinterface.CoEnableDisableListener
	 */
	public synchronized void removeEnableDisableListener(CoEnableDisableListener l) {
		listenerList.remove(CoEnableDisableListener.class, l);
	}

	protected synchronized void removeNamedValueModel(String key) {
		if (valueModels != null)
			valueModels.remove(key);
		else
			throw new IllegalArgumentException("valueModels null in removeNamedValueModel:" + key);
	}

	protected synchronized void removeNamedWidget(Component widget) {
		if (widgets != null)
			widgets.remove(widget.getName());
	}

	public synchronized void removeUserInterfaceListener(CoUserInterfaceListener l) {
		listenerList.remove(CoUserInterfaceListener.class, l);
	}
	public void setTransparentWidgets(boolean state) {
		transparentWidgets = state;
	}
	private void setUserInterface(CoUserInterface userInterface) {
		this.userInterface = userInterface;
	}

	protected void userInterfaceActivated(CoUserInterfaceEvent e) {
		userInterface.activate();
		fireUserInterfaceEvent(createUserInterfaceEvent(e.getID()));
	}

	protected void userInterfaceClosed(CoUserInterfaceEvent e) {
		userInterface.closed();
		fireUserInterfaceEvent(createUserInterfaceEvent(e.getID()));
	}

	protected void userInterfaceClosing(CoUserInterfaceEvent e) {
		CoUserInterfaceEvent tEvent = createUserInterfaceEvent(e.getID());
		fireUserInterfaceEvent(tEvent);
		if (tEvent.isConsumed())
			e.consume();
		else
			userInterface.closing();
	}

	protected void userInterfaceDeactivated(CoUserInterfaceEvent e) {
		userInterface.deactivate();
		fireUserInterfaceEvent(createUserInterfaceEvent(e.getID()));
	}

	protected void userInterfaceOpened(CoUserInterfaceEvent e) {
		userInterface.opened();
		fireUserInterfaceEvent(createUserInterfaceEvent(e.getID()));
	}

	public void userInterfaceOpenedInSubcanvas() {
		userInterface.opened();
		fireUserInterfaceEvent(createUserInterfaceEvent(CoUserInterfaceEvent.UI_OPENED));
	}

	protected void userInterfaceValidated(CoUserInterfaceEvent e) {
		CoUserInterfaceEvent tEvent = createUserInterfaceEvent(e.getID());
		fireUserInterfaceEvent(tEvent);
		if (tEvent.isConsumed())
			e.consume();
	}
}