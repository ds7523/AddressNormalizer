package edu.truman.cs370.address_normalizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.skovalenko.geocoder.address_parser.ParsedUsAddress;

/**
 * This class parses the raw data for string address.
 * 
 * @author Bulldogs
 * @version 04/26/2016
 */
public class Terminal {
	private Scanner scan;
	private String street;
	private String city;
	private String state;
	private String zip5;
	private String zip4;
	private String fullAddress;
	private String inputFilePath;
	private String outputParsedAddressPath;
	private String outputUnparsedAddressPath;
	private String option;
	private ArrayList<String> addressElements;
	private String inputAddress;
	private StatisticReport report;
	public static final String SINGLE_ENTRY_MODE = "1";
	public static final String BATCH_FILE_MODE = "2";
	public static final String EXIT = "3";

	/**
	 * Constructor
	 * 
	 * @param addressElements
	 *            an arraylist that contains the elements (parts) of an address
	 * 
	 * @param inputAddress
	 *            an input address
	 * 
	 * @param report
	 *            a statistic report
	 */
	public Terminal(ArrayList<String> addressElements, String inputAddress, StatisticReport report) {
		scan = new Scanner(System.in);
		setAddressElements(addressElements);
		setInputAddress(inputAddress);
		setReport(report);
	}

	private void printPrompt() {
		System.out.println("Select the following option and enter the corresponding number: ");
		System.out.println("1 : single entry mode");
		System.out.println("2 : batch file mode");
		System.out.println("3 : quit");
		System.out.print("Your choice: ");
	}

	/**
	 * Input for street
	 */
	public void streetInput() {
		System.out.print("Enter the street address: ");
		setStreet(getScan().nextLine());
	}

	/**
	 * Input for city
	 */
	public void cityInput() {
		System.out.print("Enter the city: ");
		setCity(getScan().nextLine());
	}

	/**
	 * Input for state
	 */
	public void stateInput() {
		System.out.print("Enter the state: ");
		setState(getScan().nextLine());
	}

	/**
	 * Input for zip5
	 */
	public void zip5Input() {
		System.out.print("Enter the zip5: ");
		setZip5(getScan().nextLine());
	}

	/**
	 * Input for zip4
	 */
	public void zip4Input() {
		System.out.print("Enter the zip4: ");
		setZip4(getScan().nextLine());
	}

	/**
	 * Perform single entry mode
	 */
	public void singleEntry() {
		streetInput();
		cityInput();
		stateInput();
		zip5Input();
		zip4Input();
		setFullAddress(getStreet() + "\t" + getCity() + "\t" + getState() + "\t" + getZip5() + "\t" + getZip4());
	}

	/**
	 * Generate the parse address
	 * 
	 * @param inputAddress
	 *            an input address
	 * 
	 * @param addressElements
	 *            an arraylist that contains the elements (parts) of an address
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
	 * Perform fail normalize single entry mode response
	 * 
	 * @param inputAddress
	 *            an input address
	 * 
	 * @param report
	 *            a statistic report
	 */
	public void failNormalizeSingleEntry(String inputAddress, StatisticReport report) {
		System.out.println("Fail-unnormalized address:");
		System.out.println(inputAddress);
		report.failed();
	}

	/**
	 * Perform single entry mode
	 * 
	 * @param addressElements
	 *            an arraylist that contains the elements (parts) of an address
	 * 
	 * @param inputAddress
	 *            an input address
	 * 
	 * @param report
	 *            a statistic report
	 */
	public void performSingleEntryMode(ArrayList<String> addressElements, String inputAddress, StatisticReport report) {
		inputAddress = getFullAddress();
		ParsedUsAddress parsedAddress = generateParsedAddress(inputAddress, addressElements);
		if (parsedAddress != null) {
			Address address = new Address(parsedAddress);
			address.performNormalize();
			if (address.stateExists(address.getState())) {
				System.out.println("Success-normalized Address:");
				System.out.println(address.toString());
				report.succeed();
			} else {
				failNormalizeSingleEntry(inputAddress, report);
			}
		} else {
			failNormalizeSingleEntry(inputAddress, report);
		}
		addressElements.clear();
	}

	/**
	 * Input for input file path
	 */
	public void inputFilePathInput() {
		System.out.print("Enter the location of the batch file: ");
		setInputFilePath(getScan().nextLine());
	}

	/**
	 * Input for output parsed address path
	 */
	public void outputParsedAddressPathInput() {
		System.out.print("Enter the location of the output file(parsed addresses): ");
		setOutputParsedAddressPath(getScan().nextLine());
	}

	/**
	 * Input for output unparsed address path
	 */
	public void outputUnparsedAddressPathInput() {
		System.out.print("Enter the location of the output file(unparsed addresses): ");
		setOutputUnparsedAddressPath(getScan().nextLine());
	}

	/**
	 * Perform all input for file paths
	 */
	public void batchFile() {
		inputFilePathInput();
		outputParsedAddressPathInput();
		outputUnparsedAddressPathInput();
	}

	/**
	 * Perform batch file mode
	 * 
	 * @param addressElements
	 *            an arraylist that contains the elements (parts) of an address
	 * 
	 * @param inputAddress
	 *            an input address
	 * 
	 * @param report
	 *            a statistic report
	 */
	public void performBatchFileMode(ArrayList<String> addressElements, String inputAddress, StatisticReport report) {
		File parsedAddressesFile = new File(getOutputParsedAddressPath());
		File unparsedAddressesFile = new File(getOutputUnparsedAddressPath());
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(getInputFilePath()));
			BufferedWriter bwParsedAddress = new BufferedWriter(new FileWriter(parsedAddressesFile));
			BufferedWriter bwUnparsedAddress = new BufferedWriter(new FileWriter(unparsedAddressesFile));
			while (true) {
				inputAddress = br.readLine();
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
			bwParsedAddress.close();
			bwUnparsedAddress.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Run the terminal mode
	 */
	public void run() {
		while (true) {
			printPrompt();
			setOption(getScan().nextLine());
			if (getOption().equals(SINGLE_ENTRY_MODE)) {
				singleEntry();
				performSingleEntryMode(addressElements, inputAddress, report);
				break;
			} else if (getOption().equals(BATCH_FILE_MODE)) {
				batchFile();
				performBatchFileMode(addressElements, inputAddress, report);
				break;
			} else if (getOption().equals(EXIT)) {
				break;
			}
			System.out.println("Invalid option. Please try again.");
		}
		getScan().close();
	}

	/**
	 * Get the scanner
	 * 
	 * @return the scanner
	 */
	public Scanner getScan() {
		return scan;
	}

	/**
	 * Set the full address
	 * 
	 * @param fullAddress
	 *            the String that contain all of the address elements for an
	 *            address
	 */
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	/**
	 * Get the street
	 * 
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * Set the street
	 * 
	 * @param street
	 *            the value of street
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * Get the city
	 * 
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Set the city
	 * 
	 * @param city
	 *            the value of city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Get the state
	 * 
	 * @return the value of state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Set the state
	 * 
	 * @param state
	 *            the value of state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Get the zip5
	 * 
	 * @return the zip5
	 */
	public String getZip5() {
		return zip5;
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
	 * Get the zip4
	 * 
	 * @return the zip4
	 */
	public String getZip4() {
		return zip4;
	}

	/**
	 * Set the zip4
	 * 
	 * @param zip5
	 *            the value of zip4
	 */
	public void setZip4(String zip4) {
		this.zip4 = zip4;
	}

	/**
	 * Get the full address
	 * 
	 * @return the value of a full address
	 */
	public String getFullAddress() {
		return fullAddress;
	}

	/**
	 * Get the input file path
	 * 
	 * @return the input file path
	 */
	public String getInputFilePath() {
		return inputFilePath;
	}

	/**
	 * Set the input file path
	 * 
	 * @param inputFilePath
	 *            the input file path
	 */
	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	/**
	 * Get the output parsed address file path
	 * 
	 * @return the output parsed address file path
	 */
	public String getOutputParsedAddressPath() {
		return outputParsedAddressPath;
	}

	/**
	 * Set the output parsed address file path
	 * 
	 * @param outputParsedAddressPath
	 *            the output parsed address file path
	 */
	public void setOutputParsedAddressPath(String outputParsedAddressPath) {
		this.outputParsedAddressPath = outputParsedAddressPath;
	}

	/**
	 * Get the output unparsed address file path
	 * 
	 * @return the output unparsed address file path
	 */
	public String getOutputUnparsedAddressPath() {
		return outputUnparsedAddressPath;
	}

	/**
	 * Set the output unparsed address file path
	 * 
	 * @param outputUnparsedAddressPath
	 *            the output parsed address file path
	 */
	public void setOutputUnparsedAddressPath(String outputUnparsedAddressPath) {
		this.outputUnparsedAddressPath = outputUnparsedAddressPath;
	}

	/**
	 * Get option
	 * 
	 * @return the value of option
	 */
	public String getOption() {
		return option;
	}

	/**
	 * Set the option
	 * 
	 * @param option
	 *            the value of option
	 */
	public void setOption(String option) {
		this.option = option;
	}

	/**
	 * Perform fail normalize batch file mode response
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
	public void failNormalizeBatchFile(String inputAddress, StatisticReport report, BufferedWriter bwUnparsedAddress) throws IOException {
		bwUnparsedAddress.write(inputAddress);
		bwUnparsedAddress.newLine();
		bwUnparsedAddress.flush();
		report.failed();
	}

	/**
	 * Get address Elements
	 * 
	 * @return the address elements
	 */
	public ArrayList<String> getAddressElements() {
		return addressElements;
	}

	/**
	 * Set the addressElements
	 * 
	 * @param addressElements
	 *            an arraylist that contains the elements (parts) of an address
	 */
	public void setAddressElements(ArrayList<String> addressElements) {
		this.addressElements = addressElements;
	}

	/**
	 * Get input address
	 * 
	 * @return the input address
	 */
	public String getInputAddress() {
		return inputAddress;
	}

	/**
	 * Set the input address
	 * 
	 * @param inputAddress
	 *            the value of input address
	 */
	public void setInputAddress(String inputAddress) {
		this.inputAddress = inputAddress;
	}

	/**
	 * Get the report
	 * 
	 * @return the report
	 */
	public StatisticReport getReport() {
		return report;
	}

	/**
	 * Set the report
	 * 
	 * @param report
	 *            the statistic report
	 */
	public void setReport(StatisticReport report) {
		this.report = report;
	}
}
