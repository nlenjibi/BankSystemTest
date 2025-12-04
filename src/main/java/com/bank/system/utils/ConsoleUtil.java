package com.bank.system.utils;

import java.util.Scanner;
import java.util.function.Predicate;

public class ConsoleUtil {

    private static final Scanner scanner = new Scanner(System.in);

        // Read a full line
    public static String readString(String prompt, Predicate<String> validator, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (validator.test(input)) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }

    // Read an integer safely

    public static int getValidIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }
            try {

                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number: ");
            }
        }
    }
    public static double getValidDoubleInput(String prompt, Predicate<Double> validator, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }
            try {
                // Remove $ or commas if user types e.g. $1,500
                input = input.replaceAll("[$,]", "");
                double value = Double.parseDouble(input);
                if (validator.test(value)) {

                    return value;
                }
                System.out.println(errorMessage);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number: $");
            }
        }
    }
    public static String underline(String text, char lineChar) {
        int length = text.length();
        StringBuilder sb = new StringBuilder();

        // Add the text
        sb.append(text).append("\n");

        // Draw the line with same number of characters
        for (int i = 0; i <= length; i++) {
            sb.append(lineChar);
        }

        return sb.toString();
    }



    // Pause the console
    public static void pressEnterToContinue() {
        System.out.println("\nPress Enter to continue…");
        scanner.nextLine();
    }
    public static void print(Object text) {
        System.out.println(text);
    }
    public static void printf(String format, Object ... args) {
        System.out.printf(format, args);
    }
    // a custom print method to displace text to the console
    public static void pr(Object text) {
        System.out.print(text);
    }
    public void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void waitForEnter(String message) {
        System.out.print(message);
        scanner.nextLine();
    }
    public static void formatCurrency(String label, double amount) {
        System.out.printf("%s: $%.2f%n", label, amount);
    }
    public static boolean readConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Y") || input.equals("YES")) {
                return true;
            } else if (input.equals("N") || input.equals("NO")) {
                return false;
            }
            System.out.println("Please enter Y for Yes or N for No.");
        }
    }

    public static void printKeyValue(String key, String value) {
        System.out.printf("%-20s: %s%n", key, value);
    }
}