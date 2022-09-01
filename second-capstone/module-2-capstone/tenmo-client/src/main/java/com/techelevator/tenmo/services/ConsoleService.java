package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {
    //console font colors
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String PURPLE_BOLD = "\033[1;35m";
    public static final String BLUE_BOLD = "\033[1;34m";
    public static final String ANSI_RESET = "\u001B[0m";

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println(ANSI_CYAN + "*********************" + ANSI_RESET);
        System.out.println(BLUE_BOLD + "* Welcome to TEnmo! *" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "*********************" + ANSI_RESET);
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println(BLUE_BOLD + "1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit" + ANSI_RESET);
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println(BLUE_BOLD + "1: Current Balance");
        System.out.println("2: Transfer History");
        System.out.println("3: Pending Requests");
        System.out.println("4: Send");
        System.out.println("5: Request");
        System.out.println("0: Exit" + ANSI_RESET);
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString(ANSI_CYAN + "Username: ");
        String password = promptForString("Password: " + ANSI_RESET);
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(BLUE_BOLD + "Please enter a valid number: " + ANSI_RESET);
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(BLUE_BOLD + "Please enter a valid number" + ANSI_RESET);
            }
        }
    }

    public void pause() {
        System.out.println(PURPLE_BOLD + "\nPress Enter to continue..." + ANSI_RESET);
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }
}
