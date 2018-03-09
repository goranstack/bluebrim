package com.bluebrim.gui.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import jozart.swingutils.*;
import jozart.swingutils.SwingWorker;

import com.bluebrim.resource.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Creation date: (1999-10-17 16:42:45)
 * @author Lasse Svadängs
 */
public class CoWorkerMonitor {
	
	private boolean			m_canBeInterrupted;
	private String 			m_message;
	private Worker			m_worker;
	private Runnable		m_updater;
	private Runnable		m_enabler;
	private WorkerProgress 	m_workerProgress;
	private Timer			m_timer;
	private long			m_startTime;

	public static long		DELAY = 3000;
	public static final boolean	INTERRUPTABLE 		= true;
	public static final boolean NOT_INTERRUPTABLE 	= false;
	

	private class WorkerProgress extends JDialog  {
		private JLabel		m_messageLabel;
		private JPanel		m_buttonPanel;
		private JButton 	m_cancelButton;
		
		public WorkerProgress(String message, boolean modal) {
			this(new JFrame(), message,modal);
		}
		private WorkerProgress(JFrame frame, String message, boolean modal) {
			super(frame, modal);
			m_message 	= message;
			init(message);

		}
		protected JPanel createCancelButton() {

			JPanel tPanel		= new JPanel();
			tPanel.setLayout(new BoxLayout(tPanel, BoxLayout.X_AXIS));
			tPanel.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
			tPanel.setOpaque(true);
			tPanel.setBackground(CoUIConstants.VERY_LIGHT_GRAY);
			
			tPanel.add(Box.createHorizontalGlue());
			tPanel.add(m_cancelButton = new CoButton(new AbstractAction(CoUIStringResources.getName(CoUIConstants.CANCEL)) {
				public void actionPerformed(ActionEvent e)
				{
					m_worker.interrupt();
				}
			}));
			tPanel.add(Box.createHorizontalGlue());
			m_cancelButton.setEnabled(m_canBeInterrupted);
			tPanel.setVisible(false);
			return tPanel;
		}
		protected void init(String message) {

			getContentPane().setLayout(new BorderLayout());
			JPanel tMainPanel	= new JPanel(new BorderLayout());
			tMainPanel.setOpaque(true);
			tMainPanel.setBackground(CoUIConstants.VERY_LIGHT_GRAY);
			tMainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			
			JPanel tPanel		= new JPanel();
			tPanel.setLayout(new BoxLayout(tPanel, BoxLayout.X_AXIS));
			tPanel.setOpaque(true);
			tPanel.setBackground(CoUIConstants.VERY_LIGHT_GRAY);

			JLabel tIconLabel	= new JLabel(CoResourceLoader.loadIcon(CoWorkerMonitor.class, "Newball3.gif"));
			tPanel.add(tIconLabel);
			tPanel.add(Box.createRigidArea(new Dimension(5,0)));
						
			m_messageLabel		= new JLabel(message);
			m_messageLabel.setFont(CoUIConstants.HELVETICA_12);
			tPanel.add(m_messageLabel);
			
			tMainPanel.add(tPanel,BorderLayout.CENTER);
			tMainPanel.add(m_buttonPanel = createCancelButton(), BorderLayout.SOUTH);
			
			getContentPane().add(tMainPanel, BorderLayout.CENTER);
		}
		public void updateMessage() {
			m_messageLabel.setText(m_message);
			m_messageLabel.paintImmediately(m_messageLabel.getBounds());
		}
		public void disableCancelButton() {
			System.out.println("disableCancelButton ");
			m_cancelButton.setEnabled(false);
			m_cancelButton.paintImmediately(m_cancelButton.getBounds());
		}
		public void setVisible(boolean state)
		{
			if (state)
			{
				m_buttonPanel.setVisible(m_cancelButton.isEnabled());
				pack();
				Rectangle tBounds = getBounds();
				setBounds(CoGUI.centerOnScreen(tBounds));
			}
			super.setVisible(state);
		}
			
	}

	public static abstract class Worker extends SwingWorker {
		private boolean	 	m_finished;
		private boolean		m_interrupted;
		private boolean		m_disableCancelButton;
		public Worker()
		{
			super();
		}
		protected void finished()
		{
			m_finished = true;
		}
		public boolean isFinished()
		{
			return m_finished;
		}
		public synchronized void interrupt()
		{
			super.interrupt();
			m_interrupted = true;
		}
		public boolean isInterrupted()
		{
			return m_interrupted;
		}	
		public boolean disableCancelButton()
		{
			return m_disableCancelButton;
		}	
		public void setDisableCancelButton(boolean state)
		{
			m_disableCancelButton = state;
		}	
	};
public CoWorkerMonitor(Worker worker, String message )
{
	this(worker, message, NOT_INTERRUPTABLE);
}
public CoWorkerMonitor(Worker worker, String message, boolean interruptable )
{
	m_worker 			= worker;
	m_message			= message;
	m_canBeInterrupted 	= interruptable;

	m_updater		= new Runnable() {
		public void run()
		{
			m_workerProgress.updateMessage();
		}
	};
	m_enabler		= new Runnable() {
		public void run()
		{
			m_workerProgress.disableCancelButton();
		}
	};
	m_timer = new Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent evt) 
		{
			if (m_worker.isInterrupted()) 
			{
				System.out.println("Worker interrupted");
				if (m_workerProgress != null)
				{
					m_workerProgress.setVisible(false);
					m_workerProgress.dispose();
				}
				Toolkit.getDefaultToolkit().beep();
				m_timer.stop();
			}
			else if (m_worker.isFinished())
			{
				breakPoint();
				System.out.println("Worker finished");
				if (m_workerProgress != null)
				{
					m_workerProgress.setVisible(false);
					m_workerProgress.dispose();
				}
				Toolkit.getDefaultToolkit().beep();
				m_timer.stop();
			}
			else 
			{
				if (!m_workerProgress.isVisible() && (System.currentTimeMillis() - m_startTime >= DELAY))
					m_workerProgress.setVisible(true);
				if (m_worker.disableCancelButton())
				{
					enableCancelButton(false);
					m_worker.setDisableCancelButton(false);
				}
			}
		}
	});
	
}
private boolean breakPoint()
{	
	return false;
}
public void enableCancelButton(boolean state)
{
	SwingUtilities.invokeLater(m_enabler);
}
public void setMessage(String message)
{
	SwingUtilities.invokeLater(m_updater);
}
public void start()
{	
	m_workerProgress 	= new WorkerProgress(m_message, false);
	m_startTime			= System.currentTimeMillis();
	m_worker.start();
	m_timer.start();
}
}
