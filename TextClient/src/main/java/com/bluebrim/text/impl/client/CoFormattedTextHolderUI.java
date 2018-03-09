package com.bluebrim.text.impl.client;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.client.*;
import com.bluebrim.text.impl.client.swing.*;
import com.bluebrim.text.shared.*;

public class CoFormattedTextHolderUI extends CoDomainUserInterface implements CoContextAcceptingUI {
	public static final String SCALE = "CoDocumentHolderUI.SCALE";

	private static final String MEASUREMENT = "MEASUREMENT";

	private CoPopupMenu m_popupMenu;
	private CoStyledTextRendererComponent m_styledtextRendererComponent;
	private CoFormattedTextHolderDialog m_textEditor;
	private CoUIContext m_uiContext;
	private static String RENDERER = "renderer";

	public CoFormattedTextHolderUI(CoObjectIF documentHolder, CoUIContext uiContext) {
		this(uiContext);

		setDomain(documentHolder);
	}

	public CoFormattedTextHolderUI(CoUIContext uiContext) {
		super();

		setUIContext(uiContext);
	}

	protected void createListeners() {
		super.createListeners();

		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				measureText();
			}
		};

		CoGUI.createKeyboardBinding(m_styledtextRendererComponent, l, KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK), true);

		m_styledtextRendererComponent.addMouseListener(new CoPopupGestureListener(m_popupMenu));

	}

	private void createPopupMenu() {
		CoMenuBuilder b = getMenuBuilder();

		m_popupMenu = b.createPopupMenu();

		class EditAction extends AbstractAction {

			public EditAction() {
			}

			public void actionPerformed(ActionEvent ev) {
				editText();
			}
		};
		/*	
			m_popupMenu.addAction( 
				new AbstractAction(CoUIStringResources.getName( CoUIConstants.ARCHIVE ))
				{
					public void actionPerformed( ActionEvent ev )
					{
						try {
							addToArchive( (CoContent)getDomain() );
						} catch (Exception e) {
							// FIXME: We should notify the user that the addition failed
						}
					}
				}
			);
			//the "Archive" action has been moved to CoSourceSimpleTableViewUI
			//Piotr
		*/
		m_popupMenu.addAction(new AbstractAction(CoUIStringResources.getName(CoUIConstants.EDIT)) {
			public void actionPerformed(ActionEvent ev) {
				editText();
			}
		});

		CoMenu menu = b.addPopupSubMenu(m_popupMenu, CoUIStringResources.getName(CoUIConstants.EDIT));

		menu.add(new EditAction());

	}

	protected void createValueModels(CoUserInterfaceBuilder builder) {
		super.createValueModels(builder);

		// SCALE
		builder.createTextFieldAdaptor(new CoDoubleConverter(builder.addAspectAdaptor(new CoAspectAdaptor(this, SCALE) {
			protected Object get(CoObjectIF subject) {
				return new Double(100 * getScale());
			}
			public void set(CoObjectIF subject, Object value) {
				setScale(((Double) value).doubleValue() / 100.0);
			}
		})), (CoTextField) getNamedWidget(SCALE));

		CoReadOnlyAspectAdaptor aspectAdaptor = new CoReadOnlyAspectAdaptor(this, RENDERER) {
			protected Object get(CoObjectIF subject) {
				return subject != null ? ((CoFormattedTextHolderIF) subject).getFormattedText( getDocumentContext()).createStyledDocument() : null;
			}
		};
		builder.addNamedValueModel(RENDERER, aspectAdaptor);

		new CoStyledTextRendererAdapter(aspectAdaptor, m_styledtextRendererComponent);

	}

	protected void createWidgets(CoPanel outerPanel, CoUserInterfaceBuilder builder) {
		super.createWidgets(outerPanel, builder);
		outerPanel.setLayout(new BorderLayout(5, 5));

		{
			CoPanel topPanel = builder.createPanel(new CoAttachmentLayout());
			outerPanel.add(topPanel, BorderLayout.NORTH);

			CoLabel l = builder.createLabel("  " + CoTextClientResources.getName(SCALE), SCALE);
			CoTextField scaleText = builder.createSlimTextField(CoTextField.RIGHT, 4, SCALE);

			CoTextMeasurementPane mp = new CoTextMeasurementPane(builder);
			builder.addNamedWidget(MEASUREMENT, mp);

			topPanel.add(
				l,
				new CoAttachmentLayout.Attachments(
					new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_COMPONENT_TOP, 0, scaleText),
					new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, scaleText),
					new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_CONTAINER, 0),
					new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_NO)));
			topPanel.add(
				scaleText,
				new CoAttachmentLayout.Attachments(
					new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_CONTAINER, 0),
					new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_NO),
					new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, l),
					new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_NO)));
			topPanel.add(
				mp,
				new CoAttachmentLayout.Attachments(
					new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_NO),
					new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_CONTAINER, 0),
					new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_NO),
					new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_CONTAINER, 0)));

		}

		outerPanel.add(new JScrollPane(m_styledtextRendererComponent = new CoStyledTextRendererComponent() {
			public Dimension getMinimumSize() {
				return new Dimension(10, 10);
			}
		}, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		createPopupMenu();

	}

	public CoGenericUIContext getCopyOfCurrentRequiredUIContext() {
		return null;
	}

	protected Insets getDefaultPanelInsets() {
		return null;
	}

	public double getScale() {
		return m_styledtextRendererComponent.getScale();
	}

	private void measureText() {
		CoTextMeasurementPane mp = (CoTextMeasurementPane) getUIBuilder().getNamedWidget("MEASUREMENT");

		CoFormattedTextHolderIF t = (CoFormattedTextHolderIF) getDomain();
		mp.setDocument((CoImmutableStyledDocumentIF) m_styledtextRendererComponent.getDocument()); // t == null ) ? null : t.getDocument( getDocumentVariant(), getDocumentContext() ) );

		CoTextMeasurementPrefsIF p;
        try
        {
            p = CoTextClient.getTextServer().getTextMeasurementPrefs();
        } catch (RemoteException e)
        {
            throw new RuntimeException(e);
        }
        //	mp.setTags( p.getMeasurementTags() );
		mp.setColumnWidth(p.getMeasurementColumnWidth());

		mp.measure();
	}

	public void postDomainChange(CoObjectIF d) {
		super.postDomainChange(d);
		measureText();

	}

	public void setScale(double scale) {
		m_styledtextRendererComponent.setScale(scale);
	}

	public void setUIContext(CoUIContext context) {
		m_uiContext = context;
	}

	public void valueHasChanged() {
		super.valueHasChanged();
		measureText();
	}

	private void editText() {
		if (m_textEditor == null) {
			CoSimpleTextEditorPane textEditorPane = new CoSimpleTextEditorPane();
			textEditorPane.setScale(2.0);
			textEditorPane.setMargin(30);
			m_textEditor = new CoFormattedTextHolderDialog( "Text",  textEditorPane, getUIBuilder());
			//	m_textEditor = new CoFormattedTextHolderDialog( "Text", true, new CoStyledTextEditorPane(), getUIBuilder() );
			m_textEditor.pack();
			m_textEditor.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		}

		m_textEditor.start((CoFormattedTextHolderIF) getDomain(), getDocumentContext());

		//	valueHasChanged();
	}

	public CoFormattedTextHolderIF.Context getDocumentContext() {
		if (m_uiContext == null)
			return null;

		return (CoFormattedTextHolderIF.Context) m_uiContext.getStateFor(CoFormattedTextHolderIF.Context.KEY);
	}
}