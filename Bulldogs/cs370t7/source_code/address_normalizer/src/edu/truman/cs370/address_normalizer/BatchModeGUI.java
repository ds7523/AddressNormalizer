package edu.truman.cs370.address_normalizer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

import com.skovalenko.geocoder.address_parser.ParsedUsAddress;

/**
 * This class displays the batch file mode frame.
 * 
 * @author Bulldogs
 * @version 04/26/2016
 */
@SuppressWarnings("serial")
public class BatchModeGUI extends JFrame {
	private JLabel batchInputFileLabel;
	private JTextField batchInputFileText;
	private JButton batchInputFileButton;
	private JLabel batchParsedOutputFileLabel;
	private JTextField batchParsedOutputFileText;
	private JButton batchParsedFileButton;
	private JLabel batchUnparsedOutputFileLabel;
	private JTextField batchUnparsedOutputFileText;
	private JButton batchUnparsedFileButton;

	private JButton normalizeButton;
	private JLabel outputLabel;
	private JTextArea outputTextArea;
	private JScrollPane scrollPane;
	private NormalizeButtonHandler normalizeButtonHandler;
	private InputFileHandler inputFileHandler;
	private ParsedFileHandler parsedFileHandler;
	private UnparsedFileHandler unparsedFileHandler;
	private JPanel containerPanel;
	private JButton homeButton;
	private JButton exitButton;
	private HomeButtonHandler homeButtonHandler;
	private ExitButtonHandler exitButtonHandler;

	private String batchInputFilePath;
	private String batchParsedOutputFilePath;
	private String batchUnparsedOutputFilePath;

	private ArrayList<String> addressElements;
	private StatisticReport report;

	private int scrollPaneWidth = 200;
	private int scrollPaneHeight = 200;
	private int panelWidth = 500;
	private int panelHeight = 800;

	private JProgressBar progressBar;

	/**
	 * Constructor
	 * 
	 * @param addressElements
	 *            an arraylist that contains the elements (parts) of an address
	 * 
	 * @param report
	 *            a statistic report
	 */
	public BatchModeGUI(ArrayList<String> addressElements, StatisticReport report) {
		batchInputFileLabel = new JLabel("Batch Input File:");
		batchParsedOutputFileLabel = new JLabel("Batch Output File (Normalized Addresses):");
		batchUnparsedOutputFileLabel = new JLabel("Batch Output File (Unnormalized Addresses):");
		outputLabel = new JLabel("Output:");

		batchInputFileText = new JTextField();
		batchParsedOutputFileText = new JTextField();
		batchUnparsedOutputFileText = new JTextField();

		batchInputFileButton = new JButton("Select File");
		inputFileHandler = new InputFileHandler();
		batchInputFileButton.addActionListener(inputFileHandler);
		batchInputFileButton.setToolTipText("Select the source file for batch processing");

		batchParsedFileButton = new JButton("Select File");
		parsedFileHandler = new ParsedFileHandler();
		batchParsedFileButton.addActionListener(parsedFileHandler);
		batchParsedFileButton.setToolTipText("Select where you want to save the normalized addresses");

		batchUnparsedFileButton = new JButton("Select File");
		unparsedFileHandler = new UnparsedFileHandler();
		batchUnparsedFileButton.addActionListener(unparsedFileHandler);
		batchUnparsedFileButton.setToolTipText("Select where you want to save non-normalized addresses");

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

		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setMinimum(0);
		progressBar.setStringPainted(true);

		containerPanel = new JPanel();
		panelSetUp(containerPanel);
		this.add(containerPanel);

		this.addressElements = addressElements;
		this.report = report;

		formatDisplay();
		exitSetUp();
	}

	/**
	 * Run the batch file mode GUI frame
	 */
	public void run() {
		this.setTitle("Address Normalizer : Batch File Mode");
		this.setLayout(new FlowLayout());
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	private void changeAlignmentX() {
		batchInputFileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		batchParsedOutputFileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		batchUnparsedOutputFileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		batchInputFileText.setAlignmentX(Component.LEFT_ALIGNMENT);
		batchParsedOutputFileText.setAlignmentX(Component.LEFT_ALIGNMENT);
		batchUnparsedOutputFileText.setAlignmentX(Component.LEFT_ALIGNMENT);
		outputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
	}

	private void addComponents(JPanel containerPanel) {
		containerPanel.add(batchInputFileLabel);
		containerPanel.add(batchInputFileText);
		containerPanel.add(batchInputFileButton);
		containerPanel.add(batchParsedOutputFileLabel);
		containerPanel.add(batchParsedOutputFileText);
		containerPanel.add(batchParsedFileButton);
		containerPanel.add(batchUnparsedOutputFileLabel);
		containerPanel.add(batchUnparsedOutputFileText);
		containerPanel.add(batchUnparsedFileButton);
		containerPanel.add(outputLabel);
		containerPanel.add(scrollPane);
		containerPanel.add(progressBar);
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

	private class InputFileHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser("dataset/");
			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				setBatchInputFilePath(selectedFile.getPath());
				batchInputFileText.setText(getBatchInputFilePath());
			}
		}
	}

	private class ParsedFileHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser("outputs/");
			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				setBatchParsedOutputFilePath(selectedFile.getPath());
				batchParsedOutputFileText.setText(getBatchParsedOutputFilePath());
			}
		}
	}

	private class UnparsedFileHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser("outputs/");
			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				setBatchUnparsedOutputFilePath(selectedFile.getPath());
				batchUnparsedOutputFileText.setText(getBatchUnparsedOutputFilePath());
			}
		}
	}

	private class NormalizeButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (getBatchInputFilePath() == null || getBatchParsedOutputFilePath() == null || getBatchUnparsedOutputFilePath() == null) {
				JOptionPane.showMessageDialog(containerPanel, "Please enter batch file location informations!",
						"Missing input information", JOptionPane.INFORMATION_MESSAGE);
			} else {
				setBatchInputFilePath(batchInputFileText.getText());
				setBatchParsedOutputFilePath(batchParsedOutputFileText.getText());
				setBatchUnparsedOutputFilePath(batchUnparsedOutputFileText.getText());
				performBatchEntryModeGUI(addressElements);
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
	public static ParsedUsAddress generateParsedAddress(String inputAddress, ArrayList<String> addressElements) {
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
	 * Perform fail normalize single entry mode response
	 * 
	 * @param inputAddress
	 *            an input address
	 * 
	 * @param report
	 *            a statistic report
	 * 
	 * @param bwUnparsedAddress
	 *            buffered writer
	 */
	public static void failNormalizeBatchFile(String inputAddress, StatisticReport report, BufferedWriter bwUnparsedAddress)
			throws IOException {
		bwUnparsedAddress.write(inputAddress);
		bwUnparsedAddress.newLine();
		bwUnparsedAddress.flush();
		report.failed();
	}

	private int getLineCoutn(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		int lines = 0;
		while (br.readLine() != null) {
			lines++;
		}
		br.close();
		return lines;
	}

	private void newAttemp() throws IOException {
		this.report = new StatisticReport();
		progressBar.setValue(0);
		progressBar.setMaximum(getLineCoutn(getBatchInputFilePath()));
	}

	/**
	 * Perform batch file mode
	 * 
	 * @param addressElements
	 *            an arraylist that contains the elements (parts) of an address
	 */
	public void performBatchEntryModeGUI(ArrayList<String> addressElements) {
		new Thread(new Runnable() {
			public void run() {
				File parsedAddressesFile = new File(getBatchParsedOutputFilePath());
				File unparsedAddressesFile = new File(getBatchUnparsedOutputFilePath());
				try {
					newAttemp();
					@SuppressWarnings("resource")
					BufferedReader br = new BufferedReader(new FileReader(getBatchInputFilePath()));
					BufferedWriter bwParsedAddress = new BufferedWriter(new FileWriter(parsedAddressesFile));
					BufferedWriter bwUnparsedAddress = new BufferedWriter(new FileWriter(unparsedAddressesFile));
					while (true) {
						progressBar.setValue(progressBar.getValue() + 1);
						String inputAddress = br.readLine();
						if (inputAddress == null) {
							break;
						}
						ParsedUsAddress parsedAddress = generateParsedAddress(inputAddress, addressElements);
						if (parsedAddress != null) {
							Address address = new Address(parsedAddress);
							address.performNormalize();
							if (address.stateExists(address.getState())) {
								bwParsedAddress.write(address.toString());
								bwParsedAddress.newLine();
								bwParsedAddress.flush();
								report.succeed();
							} else {
								failNormalizeBatchFile(inputAddress, report, bwUnparsedAddress);
							}
						} else {
							failNormalizeBatchFile(inputAddress, report, bwUnparsedAddress);
						}
						addressElements.clear();
					}
					outputTextArea.setText(report.toString() + "\n\n" + "Normalized addresses file location:\n"
							+ getBatchParsedOutputFilePath() + "\n\n" + "Unnormalized addresses file location:\n"
							+ getBatchUnparsedOutputFilePath());
					bwParsedAddress.close();
					bwUnparsedAddress.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Get batch input file path
	 * 
	 * @return the batch input file path
	 */
	public String getBatchInputFilePath() {
		return batchInputFilePath;
	}

	/**
	 * Set batch input file path
	 * 
	 * @param batchInputFilePath
	 *            a batch input file path
	 */
	public void setBatchInputFilePath(String batchInputFilePath) {
		this.batchInputFilePath = batchInputFilePath;
	}

	/**
	 * Get batch parsed output file path
	 * 
	 * @return the batch parsed output file path
	 */
	public String getBatchParsedOutputFilePath() {
		return batchParsedOutputFilePath;
	}

	/**
	 * Set batch parsed output file path
	 * 
	 * @param batchParsedOutputFilePath
	 *            a batch parsed output file path
	 */
	public void setBatchParsedOutputFilePath(String batchParsedOutputFilePath) {
		this.batchParsedOutputFilePath = batchParsedOutputFilePath;
	}

	/**
	 * Get batch unparsed output file path
	 * 
	 * @return batch unparsed output file path
	 */
	public String getBatchUnparsedOutputFilePath() {
		return batchUnparsedOutputFilePath;
	}

	/**
	 * Set batch unparsed output file path
	 * 
	 * @param batchUnparsedOutputFilePath
	 *            a batch unparsed output file path
	 */
	public void setBatchUnparsedOutputFilePath(String batchUnparsedOutputFilePath) {
		this.batchUnparsedOutputFilePath = batchUnparsedOutputFilePath;
	}

	private void formatLabel() {
		Font titleFont = new Font("Serif", Font.PLAIN, 25);
		batchInputFileLabel.setFont(titleFont);
		batchParsedOutputFileLabel.setFont(titleFont);
		batchUnparsedOutputFileLabel.setFont(titleFont);
		outputLabel.setFont(titleFont);
		Font textAreaFont = new Font("Serif", Font.PLAIN, 17);
		outputTextArea.setFont(textAreaFont);
	}

	private void formatText() {
		Font textFont = new Font("Serif", Font.PLAIN, 25);
		batchInputFileText.setFont(textFont);
		batchParsedOutputFileText.setFont(textFont);
		batchUnparsedOutputFileText.setFont(textFont);
	}

	private void formatButtons() {
		Font buttonFont = new Font("Serif", Font.PLAIN, 20);
		batchInputFileButton.setFont(buttonFont);
		batchParsedFileButton.setFont(buttonFont);
		batchUnparsedFileButton.setFont(buttonFont);
		normalizeButton.setFont(buttonFont);
		homeButton.setFont(buttonFont);
		exitButton.setFont(buttonFont);
		Dimension buttonDimension = new Dimension(125, 40);
		batchInputFileButton.setMaximumSize(buttonDimension);
		batchParsedFileButton.setMaximumSize(buttonDimension);
		batchUnparsedFileButton.setMaximumSize(buttonDimension);
		normalizeButton.setMaximumSize(buttonDimension);
		homeButton.setMaximumSize(buttonDimension);
		exitButton.setMaximumSize(buttonDimension);
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