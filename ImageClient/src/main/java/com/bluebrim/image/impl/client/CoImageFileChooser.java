package com.bluebrim.image.impl.client;

import java.awt.*;
import java.awt.image.*;
import java.beans.*;
import java.io.*;

import javax.imageio.*;
import javax.imageio.spi.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import com.bluebrim.gemstone.client.command.*;
import com.bluebrim.image.client.*;
import com.bluebrim.image.impl.shared.*;
import com.bluebrim.image.shared.*;

/**
 * Customization of JFileChooser.
 *
 * PENDING: Factorization.
 *
 * NOTE: Currently refers to CoImageIF and CoContentProviderIF which both
 * are specific to publication systems. This package should not have such
 * dependencies.
 *
 * @author Markus Persson 1999-11-12
 */
public class CoImageFileChooser extends JComponent {
	private final JFileChooser m_fileChooser;
	private boolean m_prepared;
	private FilePreviewer m_previewer;

	public static void main(String[] params) {
		IIORegistry.getDefaultInstance().registerServiceProvider(new CoEpsImageReaderSpi());
		new CoImageFileChooser().chooseImage(null);
	}

	class FilePreviewer extends JComponent implements PropertyChangeListener {
		Image thumbnail = null;

		public FilePreviewer(JFileChooser fc) {
			setPreferredSize(new Dimension(100, 50));
			fc.addPropertyChangeListener(this);
		}

		public void loadImage(File f) throws IOException {
			if (f == null)
				return;
			BufferedImage image = ImageIO.read(f);

			if (image.getWidth() > 90) 
				thumbnail = image.getScaledInstance(90, -1, Image.SCALE_DEFAULT);
			 else 
				thumbnail = image;
		}

		public void propertyChange(PropertyChangeEvent e) {
			String prop = e.getPropertyName();
			if (prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
				if (isShowing()) {
					try {
						loadImage((File) e.getNewValue());
						repaint();
					} catch (IOException ex) {
					}
				}
			}
		}

		public void paint(Graphics g) {
			if (thumbnail != null) {
				int x = getWidth() / 2 - thumbnail.getWidth(null) / 2;
				int y = getHeight() / 2 - thumbnail.getHeight(null) / 2;
				if (y < 0) {
					y = 0;
				}

				if (x < 5) {
					x = 5;
				}
				g.drawImage(thumbnail,x, y, null);
			}
		}
	}


	public CoImageFileChooser() {
		this(null);
	}

	public CoImageFileChooser(File currentDirectory) {
		m_fileChooser = new JFileChooser(currentDirectory);
		m_previewer = new FilePreviewer(m_fileChooser);

	}

	/**
	 * PENDING: Look the exception handling over!
	 *
	 * @author Markus Persson 1999-11-12
	 */
	public CoImageContentIF chooseImage(Component parent) {

		int returnValue = showDialog(parent);
		final CoImageContentIF[] imageHolder = new CoImageContentIF[1];

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			new CoBasicTransaction("Image creation", null, this) {
				protected void doTransaction() {
					File file = getSelectedFile();

					try {
						
						imageHolder[0] = CoImageClient.getInstance().createImageContent(file);
					} catch (Exception e) {
					}

				}
			}
			.execute();
		}

		return imageHolder[0];
	}


	public File getSelectedFile() {
		return m_fileChooser.getSelectedFile();
	}
	public File[] getSelectedFiles() {
		return m_fileChooser.getSelectedFiles();
	}

	protected void prepareFileChooser(JFileChooser fileChooser) {
		if (!m_prepared) {
			fileChooser.setDialogTitle("Hämta bild");
			fileChooser.setApproveButtonText("Öppna");

			fileChooser.setCurrentDirectory(new File("C:/"));
			fileChooser.setFileFilter(new FileFilter() {
				public String getDescription() {
					return "Image files";
				}

				public boolean accept(File file) {
					if (file.isDirectory())
						return true;
					String name = file.getName().toLowerCase();
					return name.endsWith(".gif")
						|| name.endsWith(".jpg")
						|| name.endsWith(".tif")
						|| name.endsWith(".png")
						|| name.endsWith(".eps");
				}
			});
			m_fileChooser.setAccessory(m_previewer);

			m_prepared = true;
		}
	}

	public void setMultiSelectionEnabled(boolean b) {
		m_fileChooser.setMultiSelectionEnabled(b);
	}

	/**
	 * PENDING: Handle reuse better?
	 *
	 * @author Markus Persson 1999-11-12
	 */
	public int showDialog(Component parent) {
		prepareFileChooser(m_fileChooser);
		return m_fileChooser.showDialog(parent, null);
	}
}
