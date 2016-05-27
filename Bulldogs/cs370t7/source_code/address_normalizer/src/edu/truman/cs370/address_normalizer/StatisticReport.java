package edu.truman.cs370.address_normalizer;

import java.text.NumberFormat;

/**
 * This class generate the statistic report for both single entry and batch file
 * modes.
 * 
 * @author Bulldogs
 * @version 04/26/2016
 */
public class StatisticReport {
	private int success;
	private int failure;
	double successRate;

	/**
	 * Constructor
	 */
	public StatisticReport() {
		success = 0;
		failure = 0;
		successRate = 0.0;
	}

	/**
	 * succeed for normalized an address
	 */
	public void succeed() {
		setSuccess(getSuccess() + 1);
	}

	/**
	 * failed to normalize an address
	 */
	public void failed() {
		setFailure(getFailure() + 1);
	}

	/**
	 * Calculate the success rate
	 */
	public void calculatePercentage() {
		int sum = getSuccess() + getFailure();
		if (sum != 0) {
			setSuccessRate((double) getSuccess() / sum);
		} else {
			setSuccessRate(0);
		}
	}

	/**
	 * Get the total number of success
	 * 
	 * @return the total number of success
	 */
	public int getSuccess() {
		return success;
	}

	/**
	 * Set the value for total successes
	 */
	public void setSuccess(int success) {
		this.success = success;
	}

	/**
	 * Get the total number of failure
	 * 
	 * @return the total number of failure
	 */
	public int getFailure() {
		return failure;
	}

	/**
	 * Set the value for total failures
	 */
	public void setFailure(int failure) {
		this.failure = failure;
	}

	/**
	 * Get the success rate
	 * 
	 * @return the success rate
	 */
	public double getSuccessRate() {
		return successRate;
	}

	/**
	 * Set the success rate
	 */
	public void setSuccessRate(double successRate) {
		this.successRate = successRate;
	}

	/**
	 * Override the toString
	 * 
	 * @return the statistic report
	 */
	public String toString() {
		calculatePercentage();
		String output = "Statistics:\n" + "Succeed: " + getSuccess() + " Failed: " + getFailure();
		NumberFormat defaultFormat = NumberFormat.getPercentInstance();
		defaultFormat.setMinimumFractionDigits(2);
		output += "\nSuccess rate: " + defaultFormat.format(getSuccessRate());
		return output;
	}
}
