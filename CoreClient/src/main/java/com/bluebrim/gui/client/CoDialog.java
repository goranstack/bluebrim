package com.bluebrim.gui.client;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import com.bluebrim.resource.shared.CoResourceLoader;

/**
	Subklass till JDialog som kan användas för visa upp en gränssnittsmodell i en dialog.
	Som default för en CoDialog som öppnas på ett CoUserInterface sätts defaultCloseOperation till HIDE_ON_CLOSE,
	dvs när dialogen stängs så döjs den bara. #dispose måste alltså anropas explicit.
	<pre>
		CoDialog tDialog = new CoDialog(aFrame, false, aUserInterface);
		tDialog.show();
		...
		...
		tDialog.dispose();
	</pre>
	Ett annat alternativ är
	<pre>
		CoDialog tDialog = new CoDialog(aFrame, false, aUserInterface);
		tDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tDialog.show();
	</pre>
	En CoDialog kan också ges ett bakgrundsmönster via
	<pre>
		tDialog.setTexture("textures/clouds.jpg");
	</pre>
	Observera att sökvägen måste anges relativt CoDialog eller en ev subklass placering i mappstrukturen.
	Självklart kan CoDialog också användas som en "traditionell" dialog, dvs utan att använda en gränssnittsmodell.
*/
public class CoDialog extends JDialog {
	private ClosingReason m_closingReason;
	public static class ClosingReason {
	}
	final public static ClosingReason CLOSED_BY_CANCEL = new ClosingReason();
	final public static ClosingReason CLOSED_BY_OK = new ClosingReason();
	final public static ClosingReason CLOSED_BY_WINDOW = new ClosingReason();

	private CoUserInterface m_ui;

	public CoDialog(Dialog owner) {
		super(owner);
	}

	public CoDialog(Dialog owner, String title) {
		super(owner, title);
	}

	public CoDialog(Dialog owner, String title, CoUserInterface ui) {
		this(owner, title, false, ui);
	}

	public CoDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	public CoDialog(Dialog owner, String title, boolean modal, CoUserInterface ui) {
		this(owner, title, modal);
		installUI(ui);
	}

	public CoDialog(Dialog owner, CoUserInterface ui) {
		this(owner, null, ui);
	}

	public CoDialog(Dialog owner, boolean modal, CoUserInterface ui) {
		this(owner, null, modal, ui);
	}

	public CoDialog(Frame owner) {
		super(owner);
	}

	public CoDialog(Frame frame, String title) {
		super(frame, title);
	}
	

	public CoDialog(Frame frame, String title, CoUserInterface ui) {
		this(frame, title, false, ui);
	}

	public CoDialog(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
	}

	public CoDialog(Frame frame, String title, boolean modal, CoUserInterface ui) {
		this(frame, title, modal);
		installUI(ui);
	}

	public CoDialog(Frame frame, CoUserInterface ui) {
		this(frame, null, ui);
	}

	public CoDialog(Frame frame, boolean modal, CoUserInterface ui) {
		this(frame, null, modal, ui);
	}

	private void addTexture(String imagePath) {
		addTexture(CoResourceLoader.getURL(getClass(), imagePath));
	}

	private void addTexture(URL imageURL) {
		CoFrame.TextureRootPane tRootPane = new CoFrame.TextureRootPane();
		if (imageURL != null) {
			try {
				tRootPane.setTexture(getImage(imageURL));
				setRootPane(tRootPane);
			} catch (Exception e) {
			}
		}
	}

	public ClosingReason getClosingReason() {
		return m_closingReason;
	}

	private Image getImage(String path) {
		URL url = CoResourceLoader.getURL(getClass(), path);
		if (url == null)
			throw new IllegalArgumentException("Illegal path in CoDialog.getImage: " + path);
		return getImage(url);
	}

	private Image getImage(URL url) {
		Image img = Toolkit.getDefaultToolkit().getImage(url);
		if (img == null)
			throw new IllegalArgumentException("Illegal URL in CoDialog.getImage: " + url);
		return img;
	}

	public CoUserInterface getUI() {
		return m_ui;
	}

	public void installUI(CoUserInterface ui) {
		if (m_ui == ui)
			return;

		m_ui = ui;
		
		ui.prepareDialog(this);
		if (!isModal()) {
			CoWindowList.addWindow(this, ui);
		}

		final PropertyChangeListener tListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if ((e.getSource() == m_ui) && e.getPropertyName().equals(m_ui.CLOSE_DIALOG)) {
					setClosingReason((ClosingReason) e.getNewValue());
					setVisible(false);
				}
			}
		};
		ui.addPropertyChangeListener(tListener);

		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				if (!isModal())
					CoWindowList.removeWindow(CoDialog.this, m_ui);
			}
		});
		pack();
		Rectangle bounds = ui.getDefaultBounds();
		setBounds(CoGUI.centerOnScreen(bounds != null ? bounds : getBounds()));
	}
	protected void setClosingReason(ClosingReason reason) {
		m_closingReason = reason;
	}

	public void setTexture(String imagePath) {
		addTexture(CoResourceLoader.getURL(getClass(), imagePath));
	}

	public void show() {
		if (isModal() && (m_ui != null)) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					m_ui.opened();
				}
			});
		}

		super.show();

		if (!isModal() && (m_ui != null)) {
			m_ui.opened();
		}
	}
}