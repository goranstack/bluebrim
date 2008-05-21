package com.bluebrim.gui.client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import com.bluebrim.resource.shared.CoResourceLoader;

/**
 * Subclass to <code>JFrame</code> used to display a userinterface
 * for editing.
 * As default a <code>CoFrame</code> has its <i>defaultsCloseOperation</i> sat
 * to <code>DISPOSE_ON_CLOSE</code>, i e when the frame is closed the platform specific
 * resources which were allocated are released.
 * <br>
 * This means that a closed frame can not be reused but a new instance must be created
 * if the userinterface needs to be redisplayed.
 * <p>
 * Code example
 * <code>
 * 	CoFrame frame = new CoFrame(userInterface);
 * 	frame.show();
 * </code>
 * <p>
 * NOTE:If the application should be shut down when the frame is closed the
 * helper method {@link CoGUI#exitOnClosing(Window)} should be used
 * to avoid creating myriads of identical inner classes. /Markus
 * 
 * @author Lasse (?)
 * @author Markus Persson 2002-06-14
 */
public class CoFrame extends JFrame {
	public static class TextureRootPane extends JRootPane {
		Image texture;
		public TextureRootPane() {
			super();
		}
		protected void paintComponent(Graphics g) {
			int w = getWidth();
			int h = getHeight();
			if (w > 0 && h > 0) {
				if (texture != null) {
					Image image = createImage(w, h);
					if (image != null) {
						Graphics imG = image.getGraphics();
						int tileW = texture.getWidth(this);
						int tileH = texture.getHeight(this);
						int horizTiles = w / tileW + 1;
						int vertTiles = h / tileH + 1;
						for (int x = 0; x < horizTiles; x++)
							for (int y = 0; y < vertTiles; y++)
								imG.drawImage(texture, x * tileW, y * tileH, tileW, tileH, this);
						super.paintComponent(imG);
						g.drawImage(image, 0, 0, this);
					} else
						super.paintComponent(g);
				} else
					super.paintComponent(g);
			}
		}
		protected void setTexture(Image texture) {
			if (texture != null) {
				MediaTracker mediaTracker = new MediaTracker(this);
				try {
					mediaTracker.addImage(texture, 0);
					mediaTracker.waitForID(0);
					this.texture = texture;
					((JComponent) getContentPane()).setDoubleBuffered(false);
					((JPanel) getContentPane()).setOpaque(false);
					getLayeredPane().setDoubleBuffered(false);
					getLayeredPane().setOpaque(false);
				} catch (InterruptedException e) {
					System.err.println(e);
					e.printStackTrace();
					texture = null;
				}
			} else {
				this.texture = texture;
			}
		}
	}

	public CoFrame(String title, String imagePath, CoUserInterface ui) {
		super(title);
		if (imagePath != null) {
			setTexture(imagePath);
			ui.getUIBuilder().setTransparentWidgets(true);
		}
		CoGUI.brand(this);
		installUI(ui);
	}

	public CoFrame(String title, CoUserInterface ui) {
		this(title, null, ui);

	}

	public CoFrame(CoUserInterface ui) {
		this(ui.getDefaultWindowTitle(), ui);
	}

	public final void setTexture(String imagePath) {
		addTexture(CoResourceLoader.getURL(getClass(), imagePath));
	}

	private void addTexture(URL imageURL) {
		TextureRootPane tRootPane = new TextureRootPane();
		if (imageURL != null) {
			try {
				tRootPane.setTexture(getImage(imageURL));
				setRootPane(tRootPane);
			} catch (Exception e) {
			}
		}
	}

	private Image getImage(URL url) {
		Image img = Toolkit.getDefaultToolkit().getImage(url);
		if (img == null) {
			throw new IllegalArgumentException("Illegal URL in CoFrame.getImage: " + url);
		}
		return img;
	}

	/**
	 * Install <code>ui</code> in the 
	 * recently created frame.
	 * This is done in 4 steps
	 * <ol>
	 * <li>by calling <code>prepareFrame</code> for the ui, a 
	 * method responsible for adding the necessary listeners, 
	 * setting the default title ...
	 * <li>adding the frame to <code>CoWindowList</code>, an object
	 * keeping track of all opened windows
	 * <li>adding a window listener so the ui can be properly uninstalled
	 * when the frame is closed
	 * <li>setting the initial bounds for the frame. This is done by calling
	 * <code>getDefaultBounds</code> for the userinterface. 
	 * </ol>
	 */
	protected final void installUI(final CoUserInterface ui) {
		ui.prepareFrame(this);
		CoWindowList.addWindow(this, ui);

		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				removeUI(ui);
			}
		});
		pack();

		Rectangle bounds = ui.getDefaultBounds();
		boolean centreRect = (bounds == null) || (bounds.getX() == 0);
		setBounds(centreRect ? CoGUI.centerOnScreen((bounds != null) ? bounds : getBounds()) : bounds);
	}

	/**
	 * Called when the frame is closed.
	 * Removes the frame from the global 
	 * window list.
	 */
	protected void removeUI(CoUserInterface ui) {
		CoWindowList.removeWindow(CoFrame.this, ui);
	}

	/**
	 * Overridden to make the content adjust and paint itself.
	 * YIKES: Is this neccessary (or even good)? /Markus 2002-06-14
	 */
	public void setSize(int width, int height) {
		super.setSize(width, height);
		validate();
	}
}