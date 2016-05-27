package edu.truman.cs370.address_normalizer;

import java.util.ArrayList;

/**
 * This system performs the address normalizer for both single entry and batch
 * file modes with both terminal and GUI support.
 * 
 * @author Bulldogs
 * @version 04/26/2016
 */
public class AddressNormalizer {
	public static void main(String[] args) {
	ArrayList<String> addressElements = new ArrayList<>();
		String inputAddress = null;
		StatisticReport report = new StatisticReport();
		if (args.length == 0) {
			Terminal terminal = new Terminal(addressElements, inputAddress, report);
			terminal.run();
		} else if (args.length == 1 && args[0].toUpperCase().equals("G")) {
			HomeGUI GUI = new HomeGUI(addressElements, report);
			GUI.run();
		} else {
			System.exit(0);
		}
	}
}