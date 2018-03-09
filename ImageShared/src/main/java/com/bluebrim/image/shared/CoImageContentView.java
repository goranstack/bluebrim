package com.bluebrim.image.shared;
import java.awt.*;

import com.bluebrim.base.shared.*;

/**
 * Image view. Including preliminary server imaging.
 *
 * @author Markus Persson 1999-09-22
 */
public class CoImageContentView extends CoView {
	protected CoImageContentIF m_model;

	public CoImageContentView(CoImageContentIF model) {
		setModel(model);
	}

	public Container getContainer() {
		return null;
	}

	public double getHeight() {
		return (m_model == null) ? 0  :m_model.getHeight();
	}

	public final CoImageContentIF getModel() {
		return m_model;
	}

	public double getWidth() {
		return (m_model == null) ? 0  :m_model.getWidth();
	}

	public boolean hasModel() {
		return m_model != null;
	}

	public void modelChanged() {
		sync();
	}

	public void paint(CoPaintable p) {
	
		m_model.getImage().draw(p);
	
	}

	public final void setModel(CoImageContentIF model) {
		if (m_model != model) {
			m_model = model;
			sync();
		}
	}
	private void sync() {
	}
}