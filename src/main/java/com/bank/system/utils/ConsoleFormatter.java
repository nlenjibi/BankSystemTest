package com.bank.system.utils;

public class ConsoleFormatter {
    private static final int CONSOLE_WIDTH = 63;
    // Constants for consistent formatting
    private static final String SEPARATOR = "=".repeat(CONSOLE_WIDTH);
    private static final String SUB_SEPARATOR = "-".repeat(CONSOLE_WIDTH);



    public static void printHeader(String title) {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println(centerText(title));
        System.out.println(SEPARATOR);
    }

    public static void printSubHeader(String title) {
        System.out.println();
        System.out.println(SUB_SEPARATOR);
        System.out.println(title);
        System.out.println(SUB_SEPARATOR);
    }

    public static void printSeparator() {
        System.out.println(SUB_SEPARATOR);
    }
    public static void printSubSeparator(int str) {
        var string = "-".repeat(str);
        System.out.println(string);
    }

    private static String centerText(String text) {
        if (text.length() >= CONSOLE_WIDTH) {
            return text;
        }
        int padding = (CONSOLE_WIDTH - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

     public void printSuccess(String message) {
        System.out.println("✅ " + message);
    }

    public void printError(String message) {
        System.out.println("❌ " + message);
    }

    public void printWarning(String message) {
        System.out.println("⚠️ " + message);
    }
}