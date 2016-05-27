package edu.truman.cs370.address_normalizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;

/**
 * This class displays the home frame.
 * 
 * @author Bulldogs
 * @version 04/26/2016
 */
@SuppressWarnings("serial")
public class HomeGUI extends JFrame {
	private JLabel homeTitle;
	private JRadioButton singleAddressButton;
	private JRadioButton batchFileButton;
	private JButton enterButton;
	private JButton exitButton;
	private EnterButtonHandler enterButtonHandler;
	private ExitButtonHandler exitButtonHandler;
	private JPanel containerPanel;

	private ArrayList<String> addressElements;
	private StatisticReport report;

	private int panelWidth = 550;
	private int panelHeight = 250;
	private ImageIcon bulldogsIcon;
	private JLabel iconLabel;

	/**
	 * Constructor
	 * 
	 * @param addressElements
	 *            an arraylist that contains the elements (parts) of an address
	 * 
	 * @param report
	 *            a statistic report
	 */
	public HomeGUI(ArrayList<String> addressElements, StatisticReport report) {
		homeTitle = new JLabel("Bulldogs Address Normalizer");
		singleAddressButton = new JRadioButton("Single Address Entry");
		singleAddressButton.setToolTipText("Manually enter single address to normalize");
		batchFileButton = new JRadioButton("Batch File Entry");
		batchFileButton.setToolTipText("Process batch of addresses by selecting the file location");

		ButtonGroup group = new ButtonGroup();
		group.add(singleAddressButton);
		group.add(batchFileButton);

		enterButton = new JButton("Enter");
		enterButtonHandler = new EnterButtonHandler();
		enterButton.addActionListener(enterButtonHandler);
		enterButton.setToolTipText("Select one of the option above and press enter to begin process");

		exitButton = new JButton("Exit");
		exitButtonHandler = new ExitButtonHandler();
		exitButton.addActionListener(exitButtonHandler);
		exitButton.setToolTipText("Press to exit program");

		bulldogsIcon = new ImageIcon("resources/T_Bulldogs.gif");
		iconLabel = new JLabel("", bulldogsIcon, JLabel.CENTER);

		containerPanel = new JPanel();
		panelSetUp(containerPanel);

		this.addressElements = addressElements;
		this.report = report;

		formatDisplay();
		exitSetUp();
	}

	/**
	 * Run the home GUI frame
	 */
	public void run() {
		this.setTitle("Address Normalizer");
		this.setLayout(new BorderLayout());
		this.add(containerPanel, BorderLayout.SOUTH);
		this.add(iconLabel, BorderLayout.NORTH);
		this.getRootPane().setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	private void addComponents(JPanel containerPanel) {
		containerPanel.add(homeTitle);
		containerPanel.add(singleAddressButton);
		containerPanel.add(batchFileButton);
		containerPanel.add(enterButton);
		containerPanel.add(exitButton);
	}

	private void panelSetUp(JPanel containerPanel) {
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
		containerPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
		addComponents(containerPanel);
	}

	private class EnterButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (singleAddressButton.isSelected()) {
				SingleModeGUI GUI = new SingleModeGUI(addressElements, report);
				GUI.run();
				dispose();
			} else if (batchFileButton.isSelected()) {
				BatchModeGUI GUI = new BatchModeGUI(addressElements, report);
				GUI.run();
				dispose();
			} else {
				JOptionPane.showMessageDialog(containerPanel, "Please select the address input mode!", "Missing input information",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private class ExitButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			confirmExit();
		}
	}

	private void formatLabel() {
		Font titleFont = new Font("Serif", Font.PLAIN, 45);
		homeTitle.setFont(titleFont);
	}

	private void formatButtons() {
		Font buttonFont = new Font("Serif", Font.PLAIN, 25);
		enterButton.setFont(buttonFont);
		exitButton.setFont(buttonFont);
		singleAddressButton.setFont(buttonFont);
		batchFileButton.setFont(buttonFont);
		Dimension optionDimension = new Dimension(100, 40);
		enterButton.setMaximumSize(optionDimension);
		exitButton.setMaximumSize(optionDimension);
		Dimension modeDimension = new Dimension(250, 40);
		singleAddressButton.setMaximumSize(modeDimension);
		batchFileButton.setMaximumSize(modeDimension);
	}

	private void formatDisplay() {
		formatLabel();
		formatButtons();
	}

	private void confirmExit() {
		String[] options = { "Exit", "Cancel" };
		int choice = JOptionPane.showOptionDialog(containerPanel, "Exit Bulldogs Address Normalizer?", "Confirm exit",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (choice == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	private void exitSetUp() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int exitPressed = JOptionPane.showConfirmDialog(null, "Confirm exit?", "Quit", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (exitPressed == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
	}
}