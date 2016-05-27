package edu.truman.cs370.address_normalizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.skovalenko.geocoder.address_parser.ParsedUsAddress;
import com.skovalenko.geocoder.address_parser.us.UsAddressParserDataStrings;

/**
 * This class represents the normalizable address.
 * 
 * @author Bulldogs
 * @version 04/26/2016
 */
public class Address {
	private String subUnitName, subUnitNum, street, city, state, zip5, zip4;
	private static Map<String, String> states = new HashMap<String, String>();
	private static Map<String, String> unitAbbreviations = new HashMap<String, String>();
	private static Map<String, String> cityAbbreviations = new HashMap<String, String>();
	private static boolean codesSet = false;

	/**
	 * Constructor
	 */
	public Address() {
		if (!codesSet) {
			this.setCodes();
		}
	}

	/**
	 * Constructor
	 * 
	 * @param parseAddress
	 *            the parsed address
	 */
	public Address(ParsedUsAddress parseAddress) {
		this.subUnitName = parseAddress.getSubUnitName();
		this.subUnitNum = parseAddress.getSubUnitNumber();
		this.street = parseAddress.getFullStreet();
		this.city = parseAddress.getCity();
		this.state = parseAddress.getState();
		this.zip5 = parseAddress.getZip();
		this.zip4 = parseAddress.getZip4();
		if (!codesSet) {
			this.setCodes();
		}
	}

	/**
	 * Check if an address element is a direction
	 * 
	 * @return whether an address element is a direction
	 */
	public boolean isDirection(String element) {
		return UsAddressParserDataStrings.DIRECTION.containsKey(element);
	}

	/**
	 * Get the abbreviation for a direction
	 * 
	 * @return the abbreviation for a direction
	 */
	public String dirAbbrivationExists(String dir) {
		return UsAddressParserDataStrings.DIRECTION.get(dir);
	}

	/**
	 * Normalize the direction
	 */
	public void normalizeDirection() {
		ArrayList<String> addressElementsHolder = new ArrayList<String>(Arrays.asList(getStreet().split(" ")));
		int size = addressElementsHolder.size();
		String lastElement = addressElementsHolder.get(size - 1);
		String secondElement = addressElementsHolder.get(1);
		String newStreet = "";
		if (isDirection(lastElement)) {
			if (!isDirection(secondElement)) {
				addressElementsHolder.remove(size - 1);
				addressElementsHolder.add(1, dirAbbrivationExists(lastElement));
			} else {
				addressElementsHolder.set(size - 1, dirAbbrivationExists(lastElement));
			}
			for (String element : addressElementsHolder) {
				newStreet += element + " ";
			}
			setStreet(newStreet.replaceAll("\\s+$", ""));
		}
	}

	/**
	 * Get the abbreviation for a unit (secondary address)
	 * 
	 * @return the abbreviation for a unit
	 */
	public boolean unitAbbreviationExists(String unit) {
		return unitAbbreviations.containsKey(unit);
	}

	/**
	 * Normalize the unit
	 */
	public void normalizeSubUnit() {
		String normalizedSubUnit = "";
		String abbreviationCheck = "";
		String currentSubUnit = this.getSubUnitName();
		char currentLetter;
		boolean lastLetter = false;
		int subUnitNameLength = currentSubUnit.length();
		for (int i = 0; i < subUnitNameLength; i++) {
			if (i == subUnitNameLength - 1)
				lastLetter = true;
			currentLetter = currentSubUnit.charAt(i);
			if (!(currentLetter == '.')) {
				abbreviationCheck = abbreviationCheck + currentLetter;
				if (unitAbbreviationExists(abbreviationCheck)) {
					normalizedSubUnit = normalizedSubUnit + unitAbbreviations.get(abbreviationCheck);
					abbreviationCheck = "";
				}
				if (lastLetter) {
					normalizedSubUnit = normalizedSubUnit + abbreviationCheck;
				}
			}
		}
		this.setSubUnitName(normalizedSubUnit);
	}

	/**
	 * Normalize the unit number
	 */
	public void normalizeSubUnitNumber() {
		String normalizedSubUnitNumber = "";
		String currentSubUnitNumber = this.getSubUnitNum();
		int subUnitNumberLength = currentSubUnitNumber.length();
		char currentChar;
		for (int i = 0; i < subUnitNumberLength; i++) {
			currentChar = currentSubUnitNumber.charAt(i);
			if (!(currentChar == ' ')) {
				if (currentChar == '#') {
					normalizedSubUnitNumber = normalizedSubUnitNumber + "# ";
				} else
					normalizedSubUnitNumber = normalizedSubUnitNumber + currentChar;
			}
		}
		setSubUnitNum(normalizedSubUnitNumber);
	}

	/**
	 * Get the abbreviation for a city
	 * 
	 * @return the abbreviation for a city
	 */
	public boolean cityAbbreviationExists(String city) {
		return cityAbbreviations.containsKey(city);
	}

	/**
	 * Normalize the city
	 */
	public void normalizeCity() {
		String normalizedCity = "";
		String abbreviationCheck = "";
		String currentCity = this.getCity();
		char currentLetter;
		boolean lastLetter = false;
		int cityNameLength = currentCity.length();
		for (int i = 0; i < cityNameLength; i++) {
			if (i == cityNameLength - 1)
				lastLetter = true;
			currentLetter = currentCity.charAt(i);
			if (!(currentLetter == '.')) {
				abbreviationCheck = abbreviationCheck + currentLetter;
				if (cityAbbreviationExists(abbreviationCheck)) {
					normalizedCity = normalizedCity + cityAbbreviations.get(abbreviationCheck);
					abbreviationCheck = "";
				}
				if (currentLetter == ' ') {
					normalizedCity = normalizedCity + abbreviationCheck;
					abbreviationCheck = "";
				}
			}
			if (lastLetter) {
				normalizedCity = normalizedCity + abbreviationCheck;
			}
		}
		this.setCity(normalizedCity);
	}

	/**
	 * Get the abbreviation for a state
	 * 
	 * @return the abbreviation for a state
	 */
	public boolean stateAbbreviationExists(String state) {
		return states.containsKey(state);
	}

	/**
	 * Check if a state exists
	 * 
	 * @return whether a state exists
	 */
	public boolean stateExists(String state) {
		return states.containsValue(state);
	}

	/**
	 * Normalize the state
	 */
	public void normalizeState() {
		if (state.length() > 2 && stateAbbreviationExists(state)) {
			this.setState(states.get(state));
		}
	}

	/**
	 * Normalize the zip5
	 */
	public void normalizeZip5() {
		this.setZip5(zip5);
	}

	/**
	 * Normalize the zip4
	 */
	public void normalizeZip4() {
		this.setZip4(zip4);
	}

	/**
	 * Perform address normalization process
	 */
	public void performNormalize() {
		normalizeDirection();
		normalizeSubUnit();
		normalizeSubUnitNumber();
		normalizeCity();
		normalizeState();
	}

	/**
	 * Get the map of states
	 * 
	 * @return the map of states
	 */
	public Map<String, String> getStates() {
		return states;
	}

	/**
	 * Get the map of cities
	 * 
	 * @return the map of states
	 */
	public Map<String, String> getCityAbbreviations() {
		return cityAbbreviations;
	}

	/**
	 * Get the map of units
	 * 
	 * @return the map of unit
	 */
	public Map<String, String> getUnitAbbreviations() {
		return unitAbbreviations;
	}

	/**
	 * Set the state, unit, and city abbreviations
	 */
	public void setCodes() {
		this.setStateCodes();
		this.setUnitAbbreviations();
		this.setCityAbbreviations();
		codesSet = true;
	}

	/**
	 * Set the state code
	 */
	public void setStateCodes() {
		states.put("ALABAMA", "AL");
		states.put("ALASKA", "AK");
		states.put("ALBERTA", "AB");
		states.put("AMERICAN SAMOA", "AS");
		states.put("ARIZONA", "AZ");
		states.put("ARKANSAS", "AR");
		states.put("ARMED FORCES (AE)", "AE");
		states.put("ARMED FORCES AMERICAC", "AA");
		states.put("ARMED FORCES PACIFIC", "AP");
		states.put("BRITISH COLUMBIA", "BC");
		states.put("CALIFORNIA", "CA");
		states.put("COLORADA", "CO");
		states.put("CONNETICUT", "CT");
		states.put("DELAWARE", "DE");
		states.put("DISTRICT OF COLUMBIA", "DC");
		states.put("FLORIDA", "FL");
		states.put("GEORGIA", "GA");
		states.put("GUAM", "GU");
		states.put("HAWAII", "HI");
		states.put("IDAHO", "ID");
		states.put("ILLINOIS", "IL");
		states.put("INDIANA", "IN");
		states.put("IOWA", "IA");
		states.put("KANSAS", "KS");
		states.put("KENTUCKY", "KY");
		states.put("LOUISIANA", "LA");
		states.put("MAINE", "ME");
		states.put("MANITOBA", "MB");
		states.put("MARYLAND", "MD");
		states.put("MASSACHUSETS", "MA");
		states.put("MICHIGAN", "MI");
		states.put("MINNESOTA", "MN");
		states.put("MISSISSIPPI", "MS");
		states.put("MISSOURI", "MO");
		states.put("MONTANA", "MT");
		states.put("NEBRASKA", "NE");
		states.put("NEVADA", "NV");
		states.put("NEW BRUNSWICK", "NB");
		states.put("NEW HAMPSHIRE", "NH");
		states.put("NEW JERSEY", "NJ");
		states.put("NEW MEXICO", "NM");
		states.put("NEW YORK", "NY");
		states.put("NEWFOUNDLAND", "NF");
		states.put("NORTH CAROLINA", "NC");
		states.put("NORTH DAKOTA", "ND");
		states.put("NORTHWEST TERRITORIES", "NT");
		states.put("NOVA SCOTIA", "NS");
		states.put("NUNAVUT", "NU");
		states.put("OHIO", "OH");
		states.put("OKLAHOMA", "OK");
		states.put("ONTARIO", "ON");
		states.put("OREGON", "OR");
		states.put("PENNSYLVANIA", "PA");
		states.put("PRINCE EDWARD ISLAND", "PE");
		states.put("PUERTO RICO", "PR");
		states.put("QUEBEC", "QC");
		states.put("RHODE ISLAND", "RI");
		states.put("SASKATCHEWAN", "SK");
		states.put("SOURTH CAROLINA", "SC");
		states.put("SOUTH DAKOTA", "SD");
		states.put("TENNESSEE", "TN");
		states.put("TEXAS", "TX");
		states.put("UTAH", "UT");
		states.put("VERMONT", "VT");
		states.put("VIRGIN ISLANDS", "VI");
		states.put("VIRGINIA", "VA");
		states.put("WASHINGTON", "WA");
		states.put("WEST VIRGINIA", "WV");
		states.put("WISCONSIN", "WI");
		states.put("WYOMING", "WY");
		states.put("YUKON TERRITORY", "YT");
	}

	/**
	 * Set the unit abbreviations
	 */
	private void setUnitAbbreviations() {
		unitAbbreviations.put("APARTMENT", "APT");
		unitAbbreviations.put("BUILDING", "BLDG");
		unitAbbreviations.put("FLOOR", "FL");
		unitAbbreviations.put("SUITE", "STE");
		unitAbbreviations.put("UNIT", "UNIT");
		unitAbbreviations.put("ROOM", "RM");
		unitAbbreviations.put("DEPARTMENT", "DEPT");
	}

	/**
	 * Set the city abbreviations
	 */
	private void setCityAbbreviations() {
		cityAbbreviations.put("ST ", "SAINT ");
		cityAbbreviations.put("N ", "NORTH ");
		cityAbbreviations.put("S ", "SOUTH ");
		cityAbbreviations.put("E ", "EAST ");
		cityAbbreviations.put("W ", "WEST ");
	}

	/**
	 * Get the street
	 * 
	 * @return the value of street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * Set the value of street
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
	 * @return the value of city
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
	 * @param city
	 *            the value of state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Get the zip5
	 * 
	 * @return the value of zip5
	 */
	public String getZip5() {
		return zip5;
	}

	/**
	 * Set the zip5
	 * 
	 * @param city
	 *            the value of zip5
	 */
	public void setZip5(String zip5) {
		this.zip5 = zip5;
	}

	/**
	 * Get the zip4
	 * 
	 * @return the value of zip4
	 */
	public String getZip4() {
		return zip4;
	}

	/**
	 * Set the zip4
	 * 
	 * @param city
	 *            the value of zip4
	 */
	public void setZip4(String zip4) {
		this.zip4 = zip4;
	}

	/**
	 * Get the unit number
	 * 
	 * @return the value of unit number
	 */
	public String getSubUnitName() {
		return subUnitName;
	}

	/**
	 * Set the unit number
	 * 
	 * @param subUnitName
	 *            the value of unit number
	 */
	public void setSubUnitName(String subUnitName) {
		this.subUnitName = subUnitName;
	}

	/**
	 * Get the unit name
	 * 
	 * @return the value of unit name
	 */
	public String getSubUnitNum() {
		return subUnitNum;
	}

	/**
	 * Set the unit name
	 * 
	 * @param subUnitNum
	 *            the value of unit name
	 */
	public void setSubUnitNum(String subUnitNum) {
		this.subUnitNum = subUnitNum;
	}

	/**
	 * Override the toString
	 * 
	 * @return the normalized address
	 */
	public String toString() {
		String output = this.getStreet();
		if (this.getSubUnitName() != null && !this.getSubUnitName().equals("") && this.getSubUnitNum() != null
				&& !this.getSubUnitName().equals("")) {
			output += " " + this.getSubUnitName() + " " + this.getSubUnitNum();
		}
		output += ", " + this.getCity() + ", " + this.getState() + ", " + this.getZip5();
		if (this.getZip4() != null && !this.getZip4().equals("")) {
			output += "-" + this.getZip4();
		}
		return output;
	}
}
