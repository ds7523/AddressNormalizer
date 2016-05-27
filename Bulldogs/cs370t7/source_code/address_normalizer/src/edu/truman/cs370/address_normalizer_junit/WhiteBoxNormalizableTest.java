package edu.truman.cs370.address_normalizer_junit;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.truman.cs370.address_normalizer.Parser;

/**
 * This class perform the white box test to test if an addressElements is
 * normalizable.
 * 
 * @author Bulldogs
 * @version 04/28/2016
 */
public class WhiteBoxNormalizableTest {
	private Parser testParser;
	private ArrayList<String> addressElements;

	@Before
	public void setUp() throws Exception {
		addressElements = new ArrayList<String>();
		testParser = new Parser(addressElements);
	}

	@Test
	public void testNormalizable() {
		assertEquals(false, testParser.verifyMinimumRequiredData());
		assertEquals(false, testParser.isNormalizable());

		addressElements.add("");
		assertEquals(false, testParser.verifyStreet());
		addressElements.set(0, null);
		assertEquals(false, testParser.verifyStreet());
		addressElements.set(0, "\\N");
		assertEquals(false, testParser.verifyStreet());
		addressElements.set(0, "515South Elson Street Apartment 6");
		assertEquals(false, testParser.verifyStreet());
		addressElements.set(0, "515 South Elson Street Apartment 6");
		assertEquals(true, testParser.verifyStreet());
		addressElements.set(0, " 515 South Elson Street Apartment 6");
		assertEquals(true, testParser.verifyStreet());
		addressElements.set(0, "515 South Elson Street Apartment 6 ");
		assertEquals(true, testParser.verifyStreet());
		assertEquals(false, testParser.verifyMinimumRequiredData());
		assertEquals(false, testParser.isNormalizable());

		addressElements.add("");
		assertEquals(false, testParser.verifyCity());
		addressElements.set(1, null);
		assertEquals(false, testParser.verifyCity());
		addressElements.set(1, "\\N");
		assertEquals(false, testParser.verifyCity());
		addressElements.set(1, "Kirksville");
		assertEquals(true, testParser.verifyCity());
		assertEquals(false, testParser.verifyMinimumRequiredData());
		assertEquals(false, testParser.isNormalizable());

		addressElements.add("");
		assertEquals(false, testParser.verifyState());
		addressElements.set(2, null);
		assertEquals(false, testParser.verifyState());
		addressElements.set(2, "\\N");
		assertEquals(false, testParser.verifyState());
		addressElements.set(2, "Missouri");
		assertEquals(true, testParser.verifyState());
		assertEquals(false, testParser.verifyMinimumRequiredData());
		assertEquals(false, testParser.isNormalizable());

		addressElements.add("");
		assertEquals(false, testParser.verifyZip5());
		addressElements.set(3, "6050");
		assertEquals(false, testParser.verifyZip5());
		addressElements.set(3, "63501");
		assertEquals(true, testParser.verifyZip5());
		assertEquals(true, testParser.verifyMinimumRequiredData());
		assertEquals(true, testParser.isNormalizable());

		addressElements.add("");
		assertEquals(false, testParser.verifyZip4());
		addressElements.set(4, "123");
		assertEquals(false, testParser.verifyZip4());
		addressElements.set(4, "1234");
		assertEquals(true, testParser.verifyZip4());
		assertEquals(true, testParser.verifyMinimumRequiredData());
		assertEquals(true, testParser.isNormalizable());
	}
}
