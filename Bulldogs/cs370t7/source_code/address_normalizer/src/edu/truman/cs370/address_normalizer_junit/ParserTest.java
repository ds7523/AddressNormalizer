package edu.truman.cs370.address_normalizer_junit;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;

import edu.truman.cs370.address_normalizer.Parser;
import edu.truman.cs370.address_normalizer.StatisticReport;
import edu.truman.cs370.address_normalizer.Terminal;

import org.junit.Test;

import com.skovalenko.geocoder.address_parser.ParsedUsAddress;

/**
 * This class tests the parser.
 * 
 * @author Bulldogs
 * @version 04/26/2016
 */
public class ParserTest {
	int testResult;
	Parser testParser;
	ArrayList<String> testElements;
	String testInputAddress;
	ParsedUsAddress testParsedAddress;
	Terminal testTerminal;
	StatisticReport testReport;

	@Before
	public void setUp() throws Exception {
		testElements = new ArrayList<String>();
		testParser = new Parser(testElements);
		testReport = new StatisticReport();
		testInputAddress = "";
	}

	@Test
	public void testParser() {
		testElements.add("123 ABC");
		testElements.add("KIRKSVILLE");
		testElements.add("MO");
		testElements.add("63501");
		testParsedAddress = testParser.createParsedUsAddress();
		assertEquals(testParsedAddress.getState(), "MO");
		assertEquals(testParsedAddress.getCity(), "KIRKSVILLE");
		assertEquals(testParsedAddress.getStreetNumber(), "123");
		assertEquals(testParsedAddress.getStreetName(), "ABC");
		testElements.clear();
		testElements.add("321 cba");
		testElements.add("st louis");
		testElements.add("mo");
		testElements.add("12345");
		testParsedAddress = testParser.createParsedUsAddress();
		assertEquals(testParsedAddress.getState(), "MO");
		assertEquals(testParsedAddress.getCity(), "ST LOUIS");
		assertEquals(testParsedAddress.getStreetNumber(), "321");
		assertEquals(testParsedAddress.getStreetName(), "CBA");
	}

	@Test
	public void testSingleEntry() {
		testElements.add("123 ABC");
		testElements.add("KIRKSVILLE");
		testElements.add("MO");
		testElements.add("63501");
		testTerminal = new Terminal(testElements, testInputAddress, testReport);
		testTerminal.setFullAddress("123 ABC" + "\t" + "KIRKSVILLE" + "\t" + "MO" + "\t" + "63501" + "\t" + "");
		testTerminal.performSingleEntryMode(testElements, testInputAddress, testReport);
		testResult = testTerminal.getReport().getSuccess();
		assertEquals(testResult, 1);
		testTerminal.getReport().setSuccess(0);
		testElements.clear();
		testElements.add("321 cba");
		testElements.add("st louis");
		testElements.add("mo");
		testElements.add("12345");
		testTerminal = new Terminal(testElements, testInputAddress, testReport);
		testTerminal.setFullAddress("321 cba" + "\t" + "st louis" + "\t" + "mo" + "\t" + "12345" + "\t" + "");
		testTerminal.performSingleEntryMode(testElements, testInputAddress, testReport);
		testResult = testTerminal.getReport().getSuccess();
		assertEquals(testResult, 1);
		testTerminal = new Terminal(testElements, testInputAddress, testReport);
		testTerminal.setFullAddress("321 cba" + "\t" + "st louis" + "\t" + "" + "\t" + "12345" + "\t" + "");
		testTerminal.performSingleEntryMode(testElements, testInputAddress, testReport);
		testResult = testTerminal.getReport().getFailure();
		assertEquals(testResult, 1);
	}
}
