package edu.truman.cs370.address_normalizer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

import com.skovalenko.geocoder.address_parser.ParsedUsAddress;

/**
 * This class displays the single entry mode frame.
 * 
 * @author Bulldogs
 * @version 04/26/2016
 */
@SuppressWarnings("serial")
public class SingleModeGUI extends JFrame {
	private JLabel streetLabel;
	private JTextField streetText;
	private JLabel cityLabel;
	private JTextField cityText;
	private JLabel stateLabel;
	private JTextField stateText;
	private JLabel zip5Label;
	private JTextField zip5Text;
	private JLabel zip4Label;
	private JTextField zip4Text;
	private JButton normalizeButton;
	private JButton homeButton;
	private JButton exitButton;
	private JLabel outputLabel;
	private JTextArea outputTextArea;
	private JScrollPane scrollPane;
	private NormalizeButtonHandler normalizeButtonHandler;
	private HomeButtonHandler homeButtonHandler;
	private ExitButtonHandler exitButtonHandler;
	private JPanel containerPanel;

	private String streetName;
	private String cityName;
	private String stateName;
	private String zip5;
	private String zip4;

	private ArrayList<String> addressElements;
	private StatisticReport report;

	private int scrollPaneWidth = 200;
	private int scrollPaneHeight = 150;
	private int panelWidth = 500;
	private int panelHeight = 800;

	/**
	 * Constructor
	 * 
	 * @param addressElements
	 *            an arraylist that contains the elements (parts) of an address
	 * 
	 * @param report
	 *            a statistic report
	 */
	public SingleModeGUI(ArrayList<String> addressElements, StatisticReport report) {
		streetLabel = new JLabel("Street Address: ");
		cityLabel = new JLabel("City: ");
		stateLabel = new JLabel("State: ");
		zip5Label = new JLabel("Zip(5-digit): ");
		zip4Label = new JLabel("Zip(4-digit): ");
		outputLabel = new JLabel("Output: ");

		streetText = new JTextField();
		streetText.setToolTipText("Enter address street information ");
		cityText = new JTextField();
		cityText.setToolTipText("Enter city name");
		stateText = new JTextField();
		stateText.setToolTipText("Enter state name (full name or abbreviation)");
		zip5Text = new JTextField();
		zip5Text.setToolTipText("Enter zip code (5 digit)");
		zip4Text = new JTextField();
		zip4Text.setToolTipText("Enter zip code (4 digit)");

		normalizeButton = new JButton("Normalize");
		normalizeButtonHandler = new NormalizeButtonHandler();
		normalizeButton.addActionListener(normalizeButtonHandler);
		normalizeButton.setToolTipText("Press to start the normalization process");

		homeButton = new JButton("Home");
		homeButtonHandler = new HomeButtonHandler();
		homeButton.addActionListener(homeButtonHandler);
		homeButton.setToolTipText("Press to return back to main screen");

		exitButton = new JButton("Exit");
		exitButtonHandler = new ExitButtonHandler();
		exitButton.addActionListener(exitButtonHandler);
		exitButton.setToolTipText("Press to exit program");

		outputTextArea = new JTextArea("Results will be printed here...");
		outputTextArea.setLineWrap(true);
		outputTextArea.setEditable(false);
		outputTextArea.setToolTipText("Output will be displayed here once process completes.");

		scrollPane = new JScrollPane(outputTextArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, scrollPaneHeight));

		containerPanel = new JPanel();
		panelSetUp(containerPanel);
		this.add(containerPanel);

		this.addressElements = addressElements;
		this.report = report;

		formatDisplay();
		exitSetUp();
	}

	/**
	 * Run the single entry mode GUI frame
	 */
	public void run() {
		this.setTitle("Address Normalizer : Single Entry Mode");
		this.setLayout(new FlowLayout());
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	private void changeAlignmentX() {
		streetLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		cityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		stateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		zip5Label.setAlignmentX(Component.LEFT_ALIGNMENT);
		zip4Label.setAlignmentX(Component.LEFT_ALIGNMENT);
		outputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		streetText.setAlignmentX(Component.LEFT_ALIGNMENT);
		cityText.setAlignmentX(Component.LEFT_ALIGNMENT);
		stateText.setAlignmentX(Component.LEFT_ALIGNMENT);
		zip5Text.setAlignmentX(Component.LEFT_ALIGNMENT);
		zip4Text.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
	}

	private void addComponents(JPanel containerPanel) {
		containerPanel.add(streetLabel);
		containerPanel.add(streetText);
		containerPanel.add(cityLabel);
		containerPanel.add(cityText);
		containerPanel.add(stateLabel);
		containerPanel.add(stateText);
		containerPanel.add(zip5Label);
		containerPanel.add(zip5Text);
		containerPanel.add(zip4Label);
		containerPanel.add(zip4Text);
		containerPanel.add(outputLabel);
		containerPanel.add(scrollPane);
		containerPanel.add(normalizeButton);
		containerPanel.add(homeButton);
		containerPanel.add(exitButton);
	}

	private void panelSetUp(JPanel containerPanel) {
		changeAlignmentX();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
		containerPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
		addComponents(containerPanel);
	}

	private class NormalizeButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (streetText.getText().equals("") || cityText.getText().equals("") || stateText.getText().equals("")
					|| zip5Text.getText().equals("")) {
				JOptionPane.showMessageDialog(containerPanel, "Please enter address, city, state, and zip5 informations!",
						"Missing input information", JOptionPane.INFORMATION_MESSAGE);
			} else {
				setStreetName(streetText.getText());
				setCityName(cityText.getText());
				setStateName(stateText.getText());
				setZip5(zip5Text.getText());
				setZip4(zip4Text.getText());
				performSingleEntryModeGUI(addressElements);
			}
		}
	}

	private class HomeButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			HomeGUI GUI = new HomeGUI(addressElements, report);
			GUI.run();
			dispose();
		}
	}

	private class ExitButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			confirmExit();
		}
	}

	/**
	 * Generate the full address
	 * 
	 * @return the value of a full address
	 */
	public String generateFullAddressFromGUI() {
		String streetNum = this.getStreetName();
		String city = this.getCityName();
		String state = this.getStateName();
		String zip5 = this.getZip5();
		String zip4 = this.getZip4();
		String fullAddress = streetNum + "\t" + city + "\t" + state + "\t" + zip5 + "\t" + zip4;
		return fullAddress;

	}

	/**
	 * Generate the parsed address
	 * 
	 * @param addressElements
	 *            an arraylist that contains the elements (parts) of an address
	 * 
	 * @param inputAddress
	 *            an input address
	 * 
	 * @return the ParsedUsAddress object
	 */
	public ParsedUsAddress generateParsedAddress(String inputAddress, ArrayList<String> addressElements) {
		String[] addressElementsHolder;
		if (inputAddress == null) {
			return null;
		} else {
			addressElementsHolder = inputAddress.split("\t");
			for (String addressElement : addressElementsHolder) {
				addressElements.add(addressElement);
			}
		}
		Parser parser = new Parser(addressElements);
		ParsedUsAddress parsedAddress = parser.createParsedUsAddress();
		return parsedAddress;
	}

	/**
	 * Perform single entry mode
	 * 
	 * @param addressElements
	 *            an arraylist that contains the elements (parts) of an address
	 */
	public void performSingleEntryModeGUI(ArrayList<String> addressElements) {
		this.report = new StatisticReport();
		ParsedUsAddress parsedAddress = generateParsedAddress(this.generateFullAddressFromGUI(), addressElements);
		if (parsedAddress != null) {
			Address address = new Address(parsedAddress);
			address.performNormalize();
			if (address.stateExists(address.getState())) {
				report.succeed();
				outputTextArea.setText(report.toString() + "\n\n" + "Normalized address: \n" + address.toString());
			}
		} else {
			report.failed();
			outputTextArea.setText(report.toString() + "\n\n" + "Unnormalized address: \n" + generateFullAddressFromGUI());

		}
		addressElements.clear();
	}

	/**
	 * Set the street name
	 * 
	 * @param streetName
	 *            the name of the street
	 */
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	/**
	 * Get the street name
	 * 
	 * @return the name of the street
	 */
	public String getStreetName() {
		return this.streetName;
	}

	/**
	 * Set the city name
	 * 
	 * @param cityName
	 *            the name of the city
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * Get the city name
	 * 
	 * @return the name of the city
	 */
	public String getCityName() {
		return this.cityName;
	}

	/**
	 * Set the state name
	 * 
	 * @param stateName
	 *            the name of the state
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * Get the state name
	 * 
	 * @return the name of the state
	 */
	public String getStateName() {
		return this.stateName;
	}

	/**
	 * Set the zip5
	 * 
	 * @param zip5
	 *            the value of zip5
	 */
	public void setZip5(String zip5) {
		this.zip5 = zip5;
	}

	/**
	 * Get the zip5
	 * 
	 * @return the value of zip5
	 */
	public String getZip5() {
		return this.zip5;
	}

	/**
	 * Set the zip4
	 * 
	 * @param zip4
	 *            the value of zip4
	 */
	public void setZip4(String zip4) {
		this.zip4 = zip4;
	}

	/**
	 * Get the zip4
	 * 
	 * @return the value of zip4
	 */
	public String getZip4() {
		return this.zip4;
	}

	private void formatLabel() {
		Font titleFont = new Font("Serif", Font.PLAIN, 25);
		streetLabel.setFont(titleFont);
		cityLabel.setFont(titleFont);
		stateLabel.setFont(titleFont);
		zip5Label.setFont(titleFont);
		zip4Label.setFont(titleFont);
		outputLabel.setFont(titleFont);
	}

	private void formatText() {
		Font textFont = new Font("Serif", Font.PLAIN, 25);
		streetText.setFont(textFont);
		cityText.setFont(textFont);
		stateText.setFont(textFont);
		zip5Text.setFont(textFont);
		zip4Text.setFont(textFont);
		Font textAreaFont = new Font("Serif", Font.PLAIN, 17);
		outputTextArea.setFont(textAreaFont);
	}

	private void formatButtons() {
		Font buttonFont = new Font("Serif", Font.PLAIN, 20);
		homeButton.setFont(buttonFont);
		exitButton.setFont(buttonFont);
		normalizeButton.setFont(buttonFont);
		Dimension buttonDimension = new Dimension(125, 40);
		homeButton.setMaximumSize(buttonDimension);
		exitButton.setMaximumSize(buttonDimension);
		normalizeButton.setMaximumSize(buttonDimension);
	}

	private void formatDisplay() {
		containerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		formatLabel();
		formatText();
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