package edu.truman.cs370.address_normalizer;

import java.util.ArrayList;

import com.skovalenko.geocoder.address_parser.ParsedUsAddress;
import com.skovalenko.geocoder.address_parser.UnparsedAddress;
import com.skovalenko.geocoder.address_parser.us.UsAddressParser;

/**
 * This class parses the raw data for string address.
 * 
 * @author Bulldogs
 * @version 04/26/2016
 */
public class Parser {
	private ArrayList<String> addressElements;
	private String invalidData = "\\N";
	private final int MIN_REQ_INFO = 4;
	private UsAddressParser parser = new UsAddressParser();

	/**
	 * Constructor
	 * 
	 * @param addressElements
	 *            an arraylist that contains the elements (parts) of an address
	 */
	public Parser(ArrayList<String> addressElements) {
		this.addressElements = addressElements;
	}

	/**
	 * Created the parsed address
	 * 
	 * @return the parsed address
	 */
	public ParsedUsAddress createParsedUsAddress() {
		if (isNormalizable()) {
			ParsedUsAddress parsedAddress = parser.parse(new UnparsedAddress(this.getStreet(), this.getCity(), this.getZip5()));
			parsedAddress.setState(this.getState());
			if (verifyZip4()) {
				parsedAddress.setZip4(this.getZip4());
			}
			return parsedAddress;
		} else {
			return null;
		}
	}

	/**
	 * Verify if the size of address elements is great than minimal required
	 * number
	 * 
	 * @return whether the size of address elements is great than minimal
	 *         required number
	 */
	public boolean verifyMinimumRequiredData() {
		return this.addressElements.size() >= MIN_REQ_INFO;
	}

	/**
	 * Trim the street address
	 * 
	 * @param street
	 *            the value of street
	 * @return the trimmed street address
	 */
	public String trimStreet(String street) {
		street = street.replaceAll("^\\s+", "");
		street = street.replaceAll("\\s+$", "");
		return street;
	}

	/**
	 * Verify if the street address begins with number
	 * 
	 * @return whether the street address begins with number
	 */
	public boolean verifyStreetBeginNum() {
		String street = getStreet();
		street = trimStreet(street);
		String pattern = "\\d+ .*";
		return street.matches(pattern);
	}

	/**
	 * Verify if the street address is normalizable
	 * 
	 * @return whether the street address is normalizable
	 */
	public boolean verifyStreet() {
		String street = getStreet();
		if (street != null && !street.equals(invalidData) && !street.equals("") && verifyStreetBeginNum()) {
			return true;
		}
		return false;
	}

	/**
	 * Verify if the city is normalizable
	 * 
	 * @return whether the city is normalizable
	 */
	public boolean verifyCity() {
		String city = getCity();
		if (city != null && !city.equals(invalidData) && !city.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * Verify if the state is normalizable
	 * 
	 * @return if the state is normalizable
	 */
	public boolean verifyState() {
		String state = getState();
		if (state != null && !state.equals(invalidData) && !state.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * Verify if the zip5 is 5 digits
	 * 
	 * @return whether the zip5 is 5 digits
	 */
	public boolean verifyZip5() {
		return getZip5().length() == 5;
	}

	/**
	 * Verify if the zip4 is 4 digits
	 * 
	 * @return whether the zip4 is 4 digits
	 */
	public boolean verifyZip4() {
		return getZip4().length() == 4;
	}

	/**
	 * Check if an raw data address is normalizable
	 * 
	 * @return whether an raw data address is normalizable
	 */
	public boolean isNormalizable() {
		if (verifyMinimumRequiredData() && verifyStreet() && verifyCity() && verifyState() && verifyZip5()) {
			return true;
		}
		return false;
	}

	/**
	 * Get the street
	 * 
	 * @return the value of street
	 */
	public String getStreet() {
		return this.addressElements.get(0);
	}

	/**
	 * Get the city
	 * 
	 * @return the value of city
	 */
	public String getCity() {
		return this.addressElements.get(1);
	}

	/**
	 * Get the state
	 * 
	 * @return the value of state
	 */
	public String getState() {
		return this.addressElements.get(2);
	}

	/**
	 * Get the zip5
	 * 
	 * @return the value of zip5
	 */
	public String getZip5() {
		return this.addressElements.get(3);
	}

	/**
	 * Get the zip4
	 * 
	 * @return the value of zip4
	 */
	public String getZip4() {
		if (this.addressElements.size() == 5) {
			return this.addressElements.get(4);
		}
		return "";
	}

	/**
	 * Override the toString
	 * 
	 * @return the elements in the parsed address separated by tab
	 */
	public String toString() {
		return this.getStreet() + " " + this.getCity() + " " + this.getState() + " " + this.getZip5() + " " + this.getZip4();
	}
}
