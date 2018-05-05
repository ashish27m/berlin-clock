package com.ubs.opsit.interviews;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BerlinClock implements TimeConverter {

	final String LINE_SEPARATOR = "\r\n";
	final String INVALID_TIME = "Invalid Time";
	final String TIME_REGEX_PATTERN = "([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])";

	private boolean validateInputTime(String inputTime) {
		try {
			if (inputTime != null && !inputTime.isEmpty()) {
				Pattern timeRegexPattern = Pattern.compile(TIME_REGEX_PATTERN);
				Matcher timeMatcher = timeRegexPattern.matcher(inputTime);
				if (!timeMatcher.matches()) {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Exception occured in validation method :-" + e.getMessage());
		}
		return true;
	}

	@Override
	public String convertTime(String aTime) {
		if (!validateInputTime(aTime)) {
			return INVALID_TIME;
		}
		if (!aTime.matches("\\d\\d:\\d\\d:\\d\\d")) {
			throw new IllegalArgumentException("Time must be in the format HH:MM:SS");
		}
		int[] parts = Arrays.asList(aTime.split(":")).stream().mapToInt(Integer::parseInt).toArray();
		StringBuilder timeSB = new StringBuilder();
		timeSB.append(getSeconds(parts[2])).append(LINE_SEPARATOR);
		timeSB.append(getTopHours(parts[0])).append(LINE_SEPARATOR);
		timeSB.append(getBottomHours(parts[0])).append(LINE_SEPARATOR);
		timeSB.append(getTopMinutes(parts[1])).append(LINE_SEPARATOR);
		timeSB.append(getBottomMinutes(parts[1]));
		return timeSB.toString();
	}

	protected String getSeconds(int number) {
		if (number % 2 == 0)
			return "Y";
		else
			return "O";
	}

	protected String getTopHours(int number) {
		return getOnOff(4, getTopNumberOfOnSigns(number));
	}

	protected String getBottomHours(int number) {
		return getOnOff(4, number % 5);
	}

	protected String getTopMinutes(int number) {
		return getOnOff(11, getTopNumberOfOnSigns(number), "Y").replaceAll("YYY", "YYR");
	}

	protected String getBottomMinutes(int number) {
		return getOnOff(4, number % 5, "Y");
	}

	// Default value for onSign would be useful
	private String getOnOff(int lamps, int onSigns) {
		return getOnOff(lamps, onSigns, "R");
	}

	private String getOnOff(int lamps, int onSigns, String onSign) {
		String out = "";
		// String multiplication would be useful
		for (int i = 0; i < onSigns; i++) {
			out += onSign;
		}
		for (int i = 0; i < (lamps - onSigns); i++) {
			out += "O";
		}
		return out;
	}

	private int getTopNumberOfOnSigns(int number) {
		return (number - (number % 5)) / 5;
	}

}
