package com.bluebrim.text.impl.client;
import java.awt.*;

import javax.swing.border.*;
import javax.swing.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.client.swing.*;
import com.bluebrim.text.shared.*;

/**
 * Subklass till CoRemoteComponentParagraphStyleUI som adderar ett textfält där
 * resultatet av aktuell utformningsinställning visas.
 */
public abstract class CoParagraphStylePreviewUI extends CoParagraphStyleUI {

	private CoStyledTextRendererComponent m_feedbackComponent;
	private CoFormattedText m_feedbackText = new CoFormattedText();
	{
		CoStyledDocument d = new CoStyledDocument();
		try {
			d.insertString(0, CoTextStringResources.getName("PARAGRAPH_SAMPLE_TEXT"), null);
		} catch (BadLocationException ex) {
		}
		m_feedbackText.from(d);
	}
	private static CoTextStyleApplier m_nullTextStyleApplier = CoTextStyleApplier.createNullApplier();

	public CoParagraphStylePreviewUI() {
		super();
	}

	protected void createListeners() {
		super.createListeners();

		(new CoDefaultServerObjectListener(this)).initialize();
	}
	/**
	 */
	protected void createWidgets(CoPanel p, CoUserInterfaceBuilder builder) {
		super.createWidgets(p, builder);

		Component c = p.getComponent(0);
		p.remove(c);

		p.setLayout(new CoAttachmentLayout());

		m_feedbackComponent = new CoStyledTextRendererComponent();
		m_feedbackComponent.setOpaque(true);
		m_feedbackComponent.setBackground(Color.white);
		m_feedbackComponent.setBorder(new LineBorder(Color.black, 1));
		m_feedbackComponent.setHorizontalMargin(0);
		m_feedbackComponent.setDocument(m_feedbackText.createStyledDocument());

		p.add(
			c,
			new CoAttachmentLayout.Attachments(
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_CONTAINER, 0),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_NO),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_CONTAINER, 0),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_NO)));
		p.add(
			m_feedbackComponent,
			new CoAttachmentLayout.Attachments(
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 5, c),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_CONTAINER, 0),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_CONTAINER, 0),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_CONTAINER, 0)));

	}

	public void postContextChange(CoTypographyContextIF context) {
		super.postContextChange(context);

		if (context != null) {
			((CoStyledTextPreferencesIF) context).getTextStyleApplier().apply(m_feedbackText);
		} else {
			m_nullTextStyleApplier.apply(m_feedbackText);
		}

		m_feedbackComponent.setDocument(m_feedbackText.createStyledDocument());
	}

	/**
	 */
	public void refreshFeedbackPane() {
		CoParagraphStyleIF style = (CoParagraphStyleIF) getDomain();

		if (style == null)
			return;

		((CoStyledDocumentIF) m_feedbackComponent.getDocument()).setParagraphAttributes(0, 0, style.getEffectiveAttributes(), true);
		m_feedbackComponent.refresh();
		m_feedbackComponent.repaint();
	}
	public void setContext(CoTextParameters context) {
		super.setContext((context == null) ? null : context.getTypographyContext());
	}

	public void setContext(CoTypographyContextIF context) {
		setContext((CoTextParameters) context);
	}

	protected void postDomainChange(CoObjectIF domain) {
		super.postDomainChange(domain);
		refreshFeedbackPane();
	}

	public void valueHasChanged() {
		super.valueHasChanged();
		refreshFeedbackPane();
	}

}