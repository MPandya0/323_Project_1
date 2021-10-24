package csulb.cecs323.app;

import java.util.Scanner;

public class UserInput {

	/**
	 * Checks if the inputted value is an double.
	 * 
	 * @param prompt - message to user.
	 * @return the valid input.
	 */
	public static double getDouble(String prompt) {
		System.out.print(prompt);
		double input = 0.0;
		boolean validInput = false;
		while (!validInput) {
			String s = getString();
			try {
				input = Double.parseDouble(s);
				validInput = true;
			} catch (Exception e) {
				System.out.print("Invalid input.\n" + prompt);
			}
		}
		return input;
	}

	/**
	 * Checks if the inputted value is an integer and within the specified range
	 * (ex: 1-10)
	 * 
	 * @param low    - lower bound of the range.
	 * @param high   - upper bound of the range.
	 * @param prompt - message to user.
	 * @return the valid input.
	 */
	public static int getIntRange(int low, int high, String prompt) {
		System.out.print(prompt);
		int input = 0;
		boolean validNumber = false;
		while (!validNumber) {
			String s = getString();
			try {
				input = Integer.parseInt(s);
				if (input < low || input > high)
					throw new Exception();
				validNumber = true;
			} catch (NumberFormatException e) {
				System.out.print("\nInvalid input.\n" + prompt);
			} catch (Exception e) {
				System.out.print("\nInput out of range.\n" + prompt);
			}
		}
		return input;
	}

	/**
	 * Checks if the inputted value is a positive integer.
	 * 
	 * @param prompt - message to user.
	 * @return the valid input.
	 */
	public static int getPosInt(String prompt) {
		System.out.print(prompt);
		int input = 0;
		boolean validInput = false;
		while (!validInput) {
			String s = getString();
			try {
				input = Integer.parseInt(s);
				if (input < 0)
					throw new Exception();
				validInput = true;
			} catch (NumberFormatException e) {
				System.out.print("\nInvalid input.\n" + prompt);
			} catch (Exception e) {
				System.out.print("\nInput must be non-negative.\n" + prompt);
			}
		}
		return input;
	}

	/**
	 * Takes in a yes/no from the user.
	 * 
	 * @param prompt - Message to user.
	 * @return true if yes, false if no.
	 */
	public static boolean getYesNo(String prompt) {
		System.out.print(prompt);
		while (true) {
			String s = getString();
			if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("y")) {
				return true;
			} else if (s.equalsIgnoreCase("no") || s.equalsIgnoreCase("n")) {
				return false;
			} else {
				System.out.print("Invalid input.\n" + prompt);
			}
		}
	}

	/**
	 * Takes in a string from the user.
	 * 
	 * @return the inputted String.
	 */
	public static String getString() {
		Scanner in = new Scanner(System.in);
		return in.nextLine();
	}
}