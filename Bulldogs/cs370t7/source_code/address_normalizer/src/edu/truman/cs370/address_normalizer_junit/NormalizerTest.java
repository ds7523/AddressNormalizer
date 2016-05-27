package edu.truman.cs370.address_normalizer_junit;

import static org.junit.Assert.assertEquals;
import edu.truman.cs370.address_normalizer.Address;

import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the normalizer.
 * 
 * @author Bulldogs
 * @version 04/26/2016
 */
public class NormalizerTest {
	private Address testAddress;

	@Before
	public void setUp() throws Exception {
		testAddress = new Address();
	}

	@Test
	public void testNormalizeZip5() {
		testAddress.setZip5("63501");
		testAddress.normalizeZip5();
		assertEquals(testAddress.getZip5(), "63501");
	}

	@Test
	public void testNormalizeZip4() {
		testAddress.setZip4("4415");
		testAddress.normalizeZip4();
		assertEquals(testAddress.getZip4(), "4415");
	}

	@Test
	public void NormalizeState() {
		testAddress.setState("MISSOURI");
		testAddress.normalizeState();
		assertEquals(testAddress.getState(), "MO");
		testAddress.setState("MO");
		testAddress.normalizeState();
		assertEquals(testAddress.getState(), "MO");
		testAddress.setState("MISSOURA");
		testAddress.normalizeState();
		assertEquals(testAddress.getState(), "MISSOURA");
	}

	@Test
	public void testNormalizeCity() {
		testAddress.setCity("ST LOUIS");
		testAddress.normalizeCity();
		assertEquals(testAddress.getCity(), "SAINT LOUIS");
		testAddress.setCity("ST. LOUIS");
		testAddress.normalizeCity();
		assertEquals(testAddress.getCity(), "SAINT LOUIS");
		testAddress.setCity("W ST LOUIS");
		testAddress.normalizeCity();
		assertEquals(testAddress.getCity(), "WEST SAINT LOUIS");
		testAddress.setCity("W ST. LOUIS");
		testAddress.normalizeCity();
		assertEquals(testAddress.getCity(), "WEST SAINT LOUIS");
		testAddress.setCity("SAINT LOUIS");
		testAddress.normalizeCity();
		assertEquals(testAddress.getCity(), "SAINT LOUIS");
		testAddress.setCity("The SAINTLY ST. NORTHERNLY N WESTERNLY W PART OF LOUIS");
		testAddress.normalizeCity();
		assertEquals(testAddress.getCity(), "The SAINTLY SAINT NORTHERNLY NORTH WESTERNLY WEST PART OF LOUIS");
	}

	@Test
	public void testNormalizeSubUnit() {
		testAddress.setSubUnitName("APARTMENT");
		testAddress.normalizeSubUnit();
		assertEquals(testAddress.getSubUnitName(), "APT");
		testAddress.setSubUnitName("APARTMANT");
		testAddress.normalizeSubUnit();
		assertEquals(testAddress.getSubUnitName(), "APARTMANT");
	}

	@Test
	public void testNormalizeSubUnitNumber() {
		testAddress.setSubUnitNum("#123");
		testAddress.normalizeSubUnitNumber();
		assertEquals(testAddress.getSubUnitNum(), "# 123");
		testAddress.setSubUnitNum("# 123");
		testAddress.normalizeSubUnitNumber();
		assertEquals(testAddress.getSubUnitNum(), "# 123");
		testAddress.setSubUnitNum("#  123");
		testAddress.normalizeSubUnitNumber();
		assertEquals(testAddress.getSubUnitNum(), "# 123");
		testAddress.setSubUnitNum("  #  123");
		testAddress.normalizeSubUnitNumber();
		assertEquals(testAddress.getSubUnitNum(), "# 123");
	}
}
