package com.bluebrim.pagesize.client;
import javax.swing.*;


import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.page.shared.*;
import com.bluebrim.swing.client.*;

/**
 */
public class CoPageSizeUI extends CoDomainUserInterface implements CoConstants, CoPageSizeClientConstants {

	public CoPageSizeUI() {
		super();
	}

	public CoPageSizeUI(CoObjectIF aDomainObject) {
		super(aDomainObject);
	}

    protected CoUserInterfaceBuilder createUserInterfaceBuilder()
    {
        return new CoNumberUserInterfaceBuilder(this);
    }

    protected void createValueModels(CoUserInterfaceBuilder builder) {
		super.createValueModels(builder);
		CoNumberUserInterfaceBuilder numberBuilder = (CoNumberUserInterfaceBuilder)builder;
		// NAMN
		builder.createTextFieldAdaptor(builder.addAspectAdaptor(new CoGsAspectAdaptor(this, NAME) {
			protected Object get(CoObjectIF subject) {
				return ((CoPageSizeIF) subject).getName();
			}
			public void set(CoObjectIF subject, Object newValue) {
				((CoPageSizeIF) subject).setName(
					(newValue != null) ? newValue.toString() : CoStringResources.getName(CoConstants.UNTITLED));
			}
		}), (CoTextField) getNamedWidget(NAME));

		// WIDTH
		numberBuilder.createNumberFieldAdaptor(builder.addAspectAdaptor(new CoGsAspectAdaptor(this, WIDTH) {
			protected Object get(CoObjectIF subject) {
				return new Float(((CoPageSizeIF) subject).getWidth());
			}
			public void set(CoObjectIF subject, Object newValue) {
				CoPageSizeIF tPageSize = (CoPageSizeIF) subject;
				((CoPageSizeIF) subject).setSize(
					(newValue != null) ? ((Number) newValue).floatValue() : 0f,
					tPageSize.getHeight());
			}
		}), (CoTextField) getNamedWidget(WIDTH), CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);

		// HEIGHT
		numberBuilder.createNumberFieldAdaptor(builder.addAspectAdaptor(new CoGsAspectAdaptor(this, HEIGHT) {
			protected Object get(CoObjectIF subject) {
				return new Float(((CoPageSizeIF) subject).getHeight());
			}
			public void set(CoObjectIF subject, Object newValue) {
				CoPageSizeIF tPageSize = (CoPageSizeIF) subject;
				((CoPageSizeIF) subject).setSize(
					tPageSize.getWidth(),
					(newValue != null) ? ((Number) newValue).floatValue() : 0f);
			}
		}), (CoTextField) getNamedWidget(HEIGHT), CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);

	}

	protected void createWidgets(CoPanel panel, CoUserInterfaceBuilder builder) {
		super.createWidgets(panel, builder);

		panel.setLayout(new CoAttachmentLayout());

		CoTextField name = builder.createTextField(NAME);
		name.setName(CoStringResources.getName(NAME));
		CoLabeledBorder.TRANSPARENT.applyTo(name);

		CoTextField w = builder.createTextField(WIDTH);
		w.setColumns(10);
		w.setHorizontalAlignment(SwingConstants.RIGHT);
		w.setName(CoPageSizeClientResources.getName(WIDTH));
		CoLabeledBorder.TRANSPARENT.applyTo(w);

		CoTextField h = builder.createTextField(HEIGHT);
		h.setColumns(10);
		h.setHorizontalAlignment(SwingConstants.RIGHT);
		h.setName(CoPageSizeClientResources.getName(HEIGHT));
		CoLabeledBorder.TRANSPARENT.applyTo(h);

		panel.add(
			name,
			new CoAttachmentLayout.Attachments(
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_CONTAINER, 0),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_NO),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_CONTAINER, 0),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_COMPONENT_RIGHT, 0, h)));
		panel.add(
			w,
			new CoAttachmentLayout.Attachments(
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 5, name),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_NO),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_CONTAINER, 0),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_NO)));
		panel.add(
			h,
			new CoAttachmentLayout.Attachments(
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.TOP_COMPONENT_TOP, 0, w),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.BOTTOM_NO),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, w),
				new CoAttachmentLayout.AttachmentSpec(CoAttachmentLayout.RIGHT_NO)));

	}

	protected void doAfterCreateUserInterface() {
		super.doAfterCreateUserInterface();

		CoLengthUnit.LENGTH_UNITS.addPropertyChangeListener(new CoPropertyChangeListener() {
			public void propertyChange(CoPropertyChangeEvent ev) {
				valueHasChanged();
			}
		});
	}
}