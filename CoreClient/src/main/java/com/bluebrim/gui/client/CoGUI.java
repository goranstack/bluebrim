package com.bluebrim.gui.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import com.bluebrim.resource.shared.CoResourceLoader;

/**
 	Utilityklass som aldrig instansieras utan bara har ett antal
 	klassmetoder som man kan anropa för diverse tjänster.
 */
public class CoGUI {
	public static final String QUESTION = "question";
	public static final String WARNING = "warning";
	public static final String MESSAGE = "message";
	public static final String ERROR = "error";

	/** @see #exitOnClosing(Window) */
	private static final WindowAdapter EXIT_ON_CLOSE_LISTENER = new WindowAdapter() {
		public void windowClosed(WindowEvent e) {
			System.exit(0);
		}
	};

	/** @see #brand(Frame) */
	private static Image BLUEBRIM_ICON_IMAGE;

	static {
		/* PENDING: Somehow figure out which image size
		 * to use since this may vary among platforms
		 * and some doesn't support any at all.
		 */
		// WINDOWS: 32x32 (Although 16x16 works and perhaps looks best
		// in the window title bar, it looks blocky when using Alt-Tab
		// to switch between windows.)
		Icon icon = CoResourceLoader.loadIcon(CoGUI.class, "BlueBrimLogo32.png", false);
		if (icon instanceof ImageIcon) {
			BLUEBRIM_ICON_IMAGE = ((ImageIcon) icon).getImage(); 
		}
	}


	/**
	 * Tries to put the child window to the right of the parent window. If
	 * it cannot fit on the screen a (in the future) user selectable policy
	 * should be used to adjust the sizes and locations of (possibly) both
	 * windows. This is very loosely specified for now since the implemented
	 * policy does not ever change the parent window. In the future, more
	 * parameters could be required to allow more user control.
	 *
	 * @author Markus Persson 2001-03-13
	 */
	public static void alignNextToOrAdjust(Window child, Window parent) {
		// PENDING: Fix so it is not assumed screenSize starts at (0,0).
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle screenSize = ge.getMaximumWindowBounds();
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension childSize = child.getSize();
		Rectangle parentBounds = parent.getBounds();

		int spaceLeft = parentBounds.x;
		int spaceRight = screenSize.width - (parentBounds.x + parentBounds.width);
		int cw = childSize.width;
		int x;
		if ((cw <= spaceRight) || (spaceRight >= spaceLeft)) {
			x = Math.min(screenSize.width - cw, spaceLeft + parentBounds.width);
		} else {
			x = Math.max(0, spaceLeft - cw);
		}

		// These are a little different ...
		int spaceUp = parentBounds.y + parentBounds.height;
		int spaceDown = screenSize.height - parentBounds.y;
		int ch = childSize.height;
		int y;
		if ((ch <= spaceDown) || (spaceDown >= spaceUp)) {
			y = Math.min(screenSize.height - ch, parentBounds.y);
		} else {
			y = Math.max(spaceUp - ch, 0);
		}
		child.setLocation(x, y);
	}

	/**
	 * Adapt the given frame to our application branding.
	 * <p>
	 * Currently this sets the "icon image" of the frame. An icon image
	 * is shown when the window is minimized, if supported by the underlying
	 * platform, but may also (on MS Windows) be shown in the window title.
	 * </p><p>
	 * Icon images can only be set on frames. Other window types (dialogs
	 * and windows) are always ultimately owned by a frame. The icon image
	 * of this frame is then used (often just for the title since such
	 * windows seldom can be minimized). Swing allows the owner frame to
	 * be omitted in which case it uses a shared hidden frame.
	 * </p><p>
	 * This method is used from within the constructors of all our Frame
	 * subclasses (mainly CoFrame), so there is no need to explicitly brand
	 * such frames. Similarily, Swing's hidden frame is branded in the
	 * startup of CoApplication so dialogs and windows owned by any such
	 * frame are already branded.
	 * </p><p>
	 * However, sometimes windows created by third party code are used.
	 * Since there doesn't seem to be any way to change the default for
	 * these (short of replacing the files in the JRE), the owning frames
	 * can be located and given to this method. (While it is possible to
	 * let this method take a Window argument and do this locating, the
	 * benefits of doing so does not yet seem to outweigh the drawbacks.)
	 * In any case, this method should rarely be used.
	 * </p><p>
	 * This method is NOT meant to add components to the content pane or
	 * equivalent since doing so might confuse some code.
	 * </p>
	 * @author Markus Persson 2002-06-07
	 */
	public static Frame brand(Frame frame) {
		if (BLUEBRIM_ICON_IMAGE != null) {
			frame.setIconImage(BLUEBRIM_ICON_IMAGE);
		}
		return frame;
	}
	
	/**
	 * Return a rectangle of the given width and height so that it is centered
	 * on and cropped within the <em>usable</em> part of the screen.
	 * 
	 * @author Lasse (?)
	 * @author Markus Persson 2002-05-03
	 */
	public static Rectangle centerOnScreen(int width, int height) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point center = ge.getCenterPoint();
		Rectangle rect = new Rectangle(center.x - (width / 2), center.y - (height / 2), width, height);
		return rect.intersection(ge.getMaximumWindowBounds());
	}

	/**
	 * Return a rectangle of the given size so that it is centered
	 * on and cropped within the <em>usable</em> part of the screen.
	 * 
	 * @author Lasse (?)
	 * @author Markus Persson 2002-05-03
	 */
	public static Rectangle centerOnScreen(Dimension size) {
		return centerOnScreen(size.width, size.height);
	}

	/**
	 * Center the given window on the <em>usable</em> part of the screen.
	 * 
	 * @author Markus Persson 2002-05-06
	 */
	public static void centerOnScreen(Window window) {
		window.setBounds(centerOnScreen(window.getSize()));
	}

	/**
	 * Svara med en ny Rectangle som är 'rectangle' centrerad på skärmen.
	 */
	public static Rectangle centerOnScreen(Rectangle rectangle) {
		return centerOnScreen(rectangle.width, rectangle.height);
	}

	/**
	 	Returns a <code>Rectangle</code> that is the <code>rectangle</code> centered
	 	over <code>aFrame</code>
	 */
	public static Rectangle centerRectOverFrame(Rectangle rectangle, Frame aFrame) {
		Rectangle tRectangle = aFrame.getBounds();
		int tWidthDiff = tRectangle.width - rectangle.width;
		int tHeightDiff = tRectangle.height - rectangle.height;
		Point tLocation = new Point(tRectangle.x + tWidthDiff / 2, tRectangle.y + tHeightDiff / 2);
		return new Rectangle(tLocation, rectangle.getSize());
	}

	/**
	 * Let the user make a confirmation. The dialog is centered on the screen.
	 *
	 * @param message Info to the user.
	 * @return boolean True if ok, otherwise false.
	 */
	static public boolean confirm(String message) {
		return confirm(message, null);
	}

	/**
	 * Let the user make a confirmation. Centered on parent.
	 *
	 * @param message Info to the user.
	 * @param parent Parent to put the dialog ontop.
	 * @return boolean  True if ok, otherwise false.
	 */
	static public boolean confirm(String message, Component parent) {
		Object[] options = { CoUIStringResources.getName(CoUIConstants.OK), CoUIStringResources.getName(CoUIConstants.CANCEL)};
		return (JOptionPane.OK_OPTION == JOptionPane.showOptionDialog(parent, message, CoUIStringResources.getName(QUESTION), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]));
	}

	public static void createKeyboardBinding(JComponent c, ActionListener l, KeyStroke keyStroke, boolean doRecurse) {
		c.registerKeyboardAction(l, keyStroke, JComponent.WHEN_FOCUSED);

		if (doRecurse) {
			int I = c.getComponentCount();
			for (int i = 0; i < I; i++) {
				if (c.getComponent(i) instanceof JComponent) {
					createKeyboardBinding((JComponent) c.getComponent(i), l, keyStroke, doRecurse);
				}
			}
		}
	}

	/**
	 * Show an error with only an ok button. The dialog is centered on the screen.
	 *
	 * @param message The message to the user
		*/
	static public boolean error(String message) {
		return error(message, null);
	}

	/**
	 * Show an error with only an ok button.
	 */
	static public boolean error(String message, Component parent) {
		return error(message, CoUIStringResources.getName(CoUIConstants.OK), parent);
	}

	/**
	 * Show an error with only an ok button.
	 */
	static public boolean error(String message, String buttonText, Component parent) {
		Object[] options = { buttonText };
		return (JOptionPane.OK_OPTION == JOptionPane.showOptionDialog(parent, message, CoUIStringResources.getName(ERROR), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]));
	}


	/**
	 * Add a window listener so that the VM will exit when
	 * the given window is closed.
	 * 
	 * PENDING: Since JDK 1.4 there is a default EXIT_ON_CLOSE
	 * closing operation. (That does not work for JDialog.)
	 * Look over the use of this and also the difference betveen
	 * the windowClosing and windowClosed events. However,
	 * centralizing such handling here might still be a good idea
	 * ... /Markus
	 * 
	 * @author Markus Persson 2002-05-08
	 */
	public static void exitOnClosing(Window window) {
		window.addWindowListener(EXIT_ON_CLOSE_LISTENER);
	}

	/**
	 * Depending on <code>exitOnClose</code> makes the VM
	 * exit or not when the given window is closed.
	 * 
	 * @see #exitOnClosing(Window)
	 * @see #dontExitOnClosing(Window)
	 * @author Markus Persson 2002-05-08
	 */
	public static void exitOnClosing(Window window, boolean exitOnClose) {
		if (exitOnClose) {
			exitOnClosing(window);
		} else {
			dontExitOnClosing(window);
		}
	}

	/**
	 * Remove the window listener added by exitOnClosing(Window)
	 * so that the VM will not exit when the given window is closed,
	 * unless some other listener does that.
	 * 
	 * @author Markus Persson 2002-05-08
	 */
	public static void dontExitOnClosing(Window window) {
		window.removeWindowListener(EXIT_ON_CLOSE_LISTENER);
	}

	/**
	 	Svarar med den Frame som 'component' ligger i eller
	 	null om den hänger löst.
	 	@return Frame
	 	@param component Component
	 */
	public static Frame getFrameFor(Component component) {
		if (component != null) {
			for (Container c = component.getParent(); c != null; c = c.getParent()) {
				if (c instanceof Frame)
					return (Frame) c;
			}
		}

		return null;

	}

	public static String getStringFromUser(CoFrame parentFrame, String title, String message) {
		return JOptionPane.showInputDialog(parentFrame, message, title, JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 	Svarar med den Window som 'component' ligger i eller
	 	null om den hänger löst.
	 	@return Window
	 	@param component Component
	 */
	public static Window getWindowFor(Component component) {
		if (component != null) {
			for (Container c = component.getParent(); c != null; c = c.getParent()) {
				if (c instanceof Window)
					return (Window) c;
			}
		}

		return null;
	}

	public static boolean hasValueChanged(Object oldValue, Object newValue) {
		return (newValue != null && !newValue.equals(oldValue)) || (oldValue == null) && (newValue != null) || (newValue == null) && (oldValue != null);
	}

	/**
	 	Svarar med en Point som är positionen för 'component' 
	 	uttryckt i skärmkoordinater.
	 */
	public static Point localToScreen(Component component) {
		Point tPoint = component.getLocationOnScreen();
		return tPoint;
	}

	/**
	 	Svarar med en Point som är koordinaten 'localPoint' i 'component' 
	 	uttryckt i skärmkoordinater.
	 */
	public static Point localToScreen(Component component, Point localPoint) {
		Point tPoint = component.getLocationOnScreen();
		tPoint.translate(localPoint.x, localPoint.y);
		return tPoint;
	}

	/**
	 *	Answer with a <code>Rectangle</code> that is the <code>rectangle</code> 
	 * argument made visible, i e completely on the screen. It's assumed that
	 * its location is on screen.
	 */
	public static Rectangle makeRectVisible(Rectangle rectangle) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		int right = rectangle.x + rectangle.width;
		int bottom = rectangle.y + rectangle.height;

		int yDiff = screenSize.height - (20 + bottom);
		int xDiff = screenSize.width - (20 + right);

		if (yDiff < 0)
			rectangle.y += yDiff;
		if (xDiff < 0)
			rectangle.x += xDiff;

		return rectangle;
	}

	/**
	 * Show a message with only an ok button. The dialog is centered on the screen.
	 *
	 * @param message The message to the user
	 * @return boolean  True if ok, otherwise false.
	 */
	static public boolean message(String message) {
		return message(message, null);
	}

	/**
	 * Show a message with only an ok button.
	 *
	 * @param message The message to the user
	 * @param parent Parent to put the dialog ontop.
	 * @return boolean  True if ok, otherwise false.
	 */
	static public boolean message(String message, Component parent) {
		Object[] options = { CoUIStringResources.getName(CoUIConstants.OK)};
		return (JOptionPane.OK_OPTION == JOptionPane.showOptionDialog(parent, message, CoUIStringResources.getName(MESSAGE), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]));
	}


	public static Object pickItem(java.util.List choosables, String label) {
		return CoPickerUI.pickItem(null, choosables, label);
	}

	public static void postSystemEvent(java.awt.AWTEvent event) {
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(event);
	}

	/**
	 * Ask the user a question. Centered on parent.
	 *
	 * @param message Info to the user.
	 * @param parent Parent to put the dialog ontop.
	 * @return boolean  True if yes, otherwise false.
	 */
	static public boolean question(String message, Component parent) {
		Object[] options = { CoUIStringResources.getName(CoUIConstants.YES), CoUIStringResources.getName(CoUIConstants.NO)};
		return (JOptionPane.YES_OPTION == JOptionPane.showOptionDialog(parent, message, CoUIStringResources.getName(QUESTION), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]));
	}
	/**
	 * Ask the user a question that the user is able to cancel. Centered on parent.
	 * This method is used when "cancel" has a different meaning from "no" for
	 * example when a user close a window and gets the question "Save content before closing?" <br>
	 * "Cancel" means that the user regrets his close command and don't want to close the window. <br>
	 * "No" means that the user do'nt want to save content but still want to close the window.
	 * 
	 * @param message Info to the user.
	 * @param parent Parent to put the dialog ontop.
	 * @return int  Return values as declared in <code>JOptionPane</code> 
	 */	
	static public int cancellableQuestion(String message, Component parent) {
		Object[] options = { CoUIStringResources.getName(CoUIConstants.YES), CoUIStringResources.getName(CoUIConstants.NO),  CoUIStringResources.getName(CoUIConstants.CANCEL)};
		return JOptionPane.showOptionDialog(parent, message, CoUIStringResources.getName(QUESTION), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	}

	/**
	 	Svarar med en Point som är positionen för skärmkoordinaten 'screenPoint' 
	 	uttryckt i koordinatsystemet för 'component'.
	 */
	public static Point screenToLocal(Component component, Point screenPoint) {
		Point tPoint = component.getLocationOnScreen();
		return new Point(screenPoint.x - tPoint.x, screenPoint.y - tPoint.y);
	}

	/**
	 * Show a dialog with a String in a JTextArea.
	 *
	 * @param text The text to show.
	 * @param parent Parent to put the dialog ontop.
	 * @return boolean  True if ok, otherwise false.
	 */
	static public void showTextDialog(String text, Component parent, String title) {
		JTextArea ta = new JTextArea(text);
		ta.setFont(new Font("Dialog", Font.BOLD, 12));
		ta.setBackground(Color.lightGray);
		JOptionPane.showMessageDialog(parent, ta, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Show a warning with only an ok button. The dialog is centered on the screen.
	 *
	 * @param message The warning to the user
	 * @param parent Parent to put the dialog ontop.
	 * @return boolean  True if ok, otherwise false.
	 */
	static public boolean warn(String message) {
		return warn(message, null);
	}

	/**
	 * Show a warning with only an ok button.
	 *
	 * @param message The warning to the user
	 * @param parent Parent to put the dialog ontop.
	 * @return boolean  True if ok, otherwise false.
	 */
	static public boolean warn(String message, Component parent) {
		Object[] options = { CoUIStringResources.getName(CoUIConstants.OK)};
		return (JOptionPane.OK_OPTION == JOptionPane.showOptionDialog(parent, message, CoUIStringResources.getName(WARNING), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]));
	}
}