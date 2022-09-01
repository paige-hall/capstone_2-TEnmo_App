package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TenmoAppService;

import java.math.BigDecimal;

public class App {
    //console font colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String BLUE_BOLD = "\033[1;34m";
    public static final String CYAN_BOLD = "\033[1;36m";
    public static final String BLACK_BOLD = "\033[1;30m";
    public static final String BLUE = "\033[0;34m";
    public static final String YELLOW = "\033[0;33m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN_UNDERLINED = "\033[4;32m";
    public static final String CYAN = "\033[0;36m";

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final TenmoAppService tenmoAppService = new TenmoAppService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private boolean stop = false;
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        while (stop == false) {
            loginMenu();
            if (currentUser != null) {
                mainMenu();
            }
        }
    }


    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection(CYAN_BOLD + "Please choose an option: " + ANSI_RESET);
            if (menuSelection == 0) {
                stop = true;
            }
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println(CYAN_BOLD + "Invalid Selection" + ANSI_RESET);
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println(CYAN_BOLD + "Please register a new user account" + ANSI_RESET);
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println(CYAN_BOLD + "Registration successful. You can now login." + ANSI_RESET);
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogOut() {
        currentUser = null;
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection(CYAN_BOLD + "Please choose an option: " + ANSI_RESET);
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println(CYAN_BOLD + "Invalid Selection" + ANSI_RESET);
            }
            consoleService.pause();
        }
        handleLogOut();
    }

    private void viewCurrentBalance() {
        // TODO Auto-generated method stub
        Balance balance = tenmoAppService.getBalance(currentUser);
        System.out.println(BLUE_BOLD + "Your current account balance is: " + GREEN_UNDERLINED + "$" + balance.getBalance() + ANSI_RESET);
    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        //users information
        Account userAccount = tenmoAppService.getAccountByUserId(currentUser, currentUser.getUser().getId());
        int userAccountId = userAccount.getAccountId();
        Transfer[] transfers = tenmoAppService.getAllTransfers(currentUser, userAccountId);

        //print to console
        System.out.println(BLACK_BOLD + "--------------------------------------------------------------" + ANSI_RESET);
        System.out.println(BLUE_BOLD + "TRANSFERS" + ANSI_RESET);
        System.out.println(BLUE_BOLD + "Transfer ID:      From/To:            Amount:" + ANSI_RESET);
        System.out.println(BLACK_BOLD + "--------------------------------------------------------------" + ANSI_RESET);
        System.out.println("");

        //loops through transfers and displays ID, TO, FROM, AMOUNT
        for (Transfer transfer : transfers) {
            if (transfer.getAccountFrom() == userAccountId) {
                String username = tenmoAppService.getUserByUserId(currentUser, tenmoAppService.getAccountByAccountId(currentUser, transfer.getAccountTo()).getUserId()).getUsername();
                System.out.println(YELLOW + transfer.getTransferId() + BLUE + "              To: " + YELLOW + username + ANSI_RESET + "             " + GREEN_UNDERLINED + "$" + transfer.getAmount() + ANSI_RESET);
            }
            if (transfer.getAccountTo() == userAccountId) {
                String username = tenmoAppService.getUserByUserId(currentUser, tenmoAppService.getAccountByAccountId(currentUser, transfer.getAccountFrom()).getUserId()).getUsername();
                System.out.println(YELLOW + transfer.getTransferId() + BLUE + "              From: " + YELLOW + username + ANSI_RESET + "           " + GREEN_UNDERLINED + "$" + transfer.getAmount() + ANSI_RESET);
            }
        }

        //prompts user for input on displaying transfer details or cancelling
        System.out.println("");
        int userInput = consoleService.promptForInt(CYAN_BOLD + "Please enter transfer ID to view details (0 to cancel): " + ANSI_RESET);

        if (userInput == 0) { //cancel transfer details view
            System.out.println(CYAN_BOLD + "Transfer details view cancelled" + ANSI_RESET);
        } else {  //display the transfer details
            Transfer transfer = tenmoAppService.getTransferByTransferId(currentUser, userInput);

            System.out.println(BLACK_BOLD + "--------------------------------------------------------------" + ANSI_RESET);
            System.out.println(BLUE_BOLD + "TRANSFER DETAILS" + ANSI_RESET);
            System.out.println(BLACK_BOLD + "--------------------------------------------------------------" + ANSI_RESET);
            System.out.println("");
            System.out.println(BLUE + "Transfer ID: " + YELLOW + transfer.getTransferId() + ANSI_RESET);
            System.out.println(BLUE + "From: " + YELLOW + tenmoAppService.getUserByUserId(currentUser, tenmoAppService.getAccountByAccountId(currentUser, transfer.getAccountFrom()).getUserId()).getUsername() + ANSI_RESET);
            System.out.println(BLUE + "To: " + YELLOW + tenmoAppService.getUserByUserId(currentUser, tenmoAppService.getAccountByAccountId(currentUser, transfer.getAccountTo()).getUserId()).getUsername() + ANSI_RESET);
            System.out.println(BLUE + "Type: " + YELLOW + tenmoAppService.getTransferTypeByTransferTypeId(currentUser, transfer.getTransferTypeId()).getTransferTypeDescription() + ANSI_RESET);
            System.out.println(BLUE + "Status: " + YELLOW + tenmoAppService.getTransferStatusByTransferStatusId(currentUser, transfer.getTransferStatusId()).getTransferStatusDescription() + ANSI_RESET);
            System.out.println(BLUE + "Amount: " + YELLOW + "$" + transfer.getAmount() + ANSI_RESET);
        }
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub
        //users information
        Account userAccount = tenmoAppService.getAccountByUserId(currentUser, currentUser.getUser().getId());
        int userAccountId = userAccount.getAccountId();
        Transfer[] transfers = tenmoAppService.getAllTransfers(currentUser, userAccountId);

        //print to console
        System.out.println(BLACK_BOLD + "--------------------------------------------------------------" + ANSI_RESET);
        System.out.println(BLUE_BOLD + "Pending Transfers" + ANSI_RESET);
        System.out.println("");
        System.out.println(BLUE_BOLD + "ID:               To:                  Amount:" + ANSI_RESET);
        System.out.println(BLACK_BOLD + "--------------------------------------------------------------" + ANSI_RESET);
        System.out.println("");

        //loop through transfers and pull pending transfers, display to user
        for (Transfer transfer : transfers) {
            if (transfer.getAccountFrom() == userAccountId && transfer.getTransferStatusId() == 1) {
                String username = tenmoAppService.getUserByUserId(currentUser, tenmoAppService.getAccountByAccountId(currentUser, transfer.getAccountTo()).getUserId()).getUsername();
                System.out.println(YELLOW + transfer.getTransferId() + BLUE_BOLD + "              To: " + YELLOW + username + "             " + GREEN_UNDERLINED + "$" + transfer.getAmount() + ANSI_RESET);
            }
        }

        //prompts user for input to view details or cancel
        System.out.println("");
        int transferId = consoleService.promptForInt(CYAN_BOLD + "Please enter transfer ID to view details (0 to cancel): " + ANSI_RESET);

        if (transferId == 0) { //cancel transfer details view
            System.out.println(CYAN_BOLD + "Transfer details view cancelled" + ANSI_RESET);
        } else { //display the transfer details
            Transfer transfer = tenmoAppService.getTransferByTransferId(currentUser, transferId);
            System.out.println("");
            System.out.println(BLACK_BOLD + "--------------------------------------------------------------" + ANSI_RESET);
            System.out.println(BLUE_BOLD + "Pending Transfer Details" + ANSI_RESET);
            System.out.println(BLACK_BOLD + "--------------------------------------------------------------" + ANSI_RESET);
            System.out.println("");
            System.out.println(BLUE_BOLD + "ID: " + YELLOW + transfer.getTransferId() + ANSI_RESET);
            System.out.println(BLUE_BOLD + "From: " + YELLOW + tenmoAppService.getUserByUserId(currentUser, tenmoAppService.getAccountByAccountId(currentUser, transfer.getAccountFrom()).getUserId()).getUsername() + ANSI_RESET);
            System.out.println(BLUE_BOLD + "To: " + YELLOW + tenmoAppService.getUserByUserId(currentUser, tenmoAppService.getAccountByAccountId(currentUser, transfer.getAccountTo()).getUserId()).getUsername() + ANSI_RESET);
            System.out.println(BLUE_BOLD + "Type: " + YELLOW + tenmoAppService.getTransferTypeByTransferTypeId(currentUser, transfer.getTransferTypeId()).getTransferTypeDescription() + ANSI_RESET);
            System.out.println(BLUE_BOLD + "Status: " + YELLOW + tenmoAppService.getTransferStatusByTransferStatusId(currentUser, transfer.getTransferStatusId()).getTransferStatusDescription() + ANSI_RESET);
            System.out.println(BLUE_BOLD + "Amount: " + YELLOW + "$" + transfer.getAmount() + ANSI_RESET);

            System.out.println("");
            //get input from user for pending transfer ID
            int pendingTransferInput = consoleService.promptForInt(CYAN_BOLD + "Please enter transfer ID to approve/reject (0 to cancel): " + ANSI_RESET);
            while (pendingTransferInput != transferId && pendingTransferInput != 0) {
                pendingTransferInput = consoleService.promptForInt(CYAN_BOLD + "Invalid command. Please enter transfer ID to approve/reject (0 to cancel): " + ANSI_RESET);
            }
            if (pendingTransferInput == 0) { //cancel
                System.out.println(CYAN_BOLD + "Pending transfer details cancelled" + ANSI_RESET);
            } else { //display transfer details information
                System.out.println("");
                System.out.println(BLUE_BOLD + "1: Approve" + ANSI_RESET);
                System.out.println(BLUE_BOLD + "2: Reject" + ANSI_RESET);
                System.out.println(BLUE_BOLD + "0: Don't Approve or Reject" + ANSI_RESET);
                System.out.println("");

                //get input from user to ACCEPT, REJECT, or IGNORE pending transfers
                int approveOrRejectInput = consoleService.promptForInt(CYAN_BOLD + "Please enter choice to approve/reject (0 to cancel): " + ANSI_RESET);
                //does not complete if balance is not sufficient
                if (transfer.getAmount().compareTo(tenmoAppService.getBalance(currentUser).getBalance()) > 0) {
                    approveOrRejectInput = 0;
                    System.out.println(CYAN_BOLD + "Insufficient funds" + ANSI_RESET);
                }
                //does not approve or reject pending transfer
                if (approveOrRejectInput == 0) {
                    System.out.println(CYAN_BOLD + "Request pending" + ANSI_RESET);
                }
                //approves transfer
                if (approveOrRejectInput == 1) {
                    tenmoAppService.approveTransfer(currentUser, transfer);
                    System.out.println(CYAN_BOLD + "Transfer request approved!" + ANSI_RESET);
                }
                //rejects transfer
                if (approveOrRejectInput == 2) {
                    tenmoAppService.rejectTransfer(currentUser, transfer);
                    System.out.println(CYAN_BOLD + "Transfer request rejected!" + ANSI_RESET);
                }
            }
        }
    }

    private void sendBucks() {
        // TODO Auto-generated method stub
        //shows all available users to send TE bucks to
        User[] users = tenmoAppService.getAllUsers(currentUser);
        showAllUsers(users);

        //prompts user for input to send money to another user
        int userInput = consoleService.promptForInt(BLUE_BOLD + "Enter the ID of the user you want to send money to (enter 0 to cancel): " + ANSI_RESET);
        if (userInput == 0 || userInput == currentUser.getUser().getId()) { //invalid entries
            System.out.println(BLUE_BOLD + "Transaction cancelled" + ANSI_RESET);
        } else { //gets user input for user ID to send money to
            int receivingUserId = 0;
            for (User user : users) {
                if (user.getId() == userInput) {
                    receivingUserId = userInput;
                }
            }
            //prompts user for input- amount to send
            BigDecimal amountToSend = consoleService.promptForBigDecimal(CYAN_BOLD + "Enter amount to send: " + ANSI_RESET);
            while (amountToSend.compareTo(BigDecimal.ZERO) <= 0) { //invalid amount negative / zero
                amountToSend = consoleService.promptForBigDecimal(CYAN_BOLD + "Transfer amount must be more than 0. Enter amount to transfer: " + ANSI_RESET);
            }
            while (amountToSend.compareTo(tenmoAppService.getBalance(currentUser).getBalance()) == 1) { //invalid amount- must have enough in account balance
                amountToSend = consoleService.promptForBigDecimal(CYAN_BOLD + "Transfer amount cannot be more than user balance. Enter amount to transfer: " + ANSI_RESET);
            }
            //valid transactions create a new Transfer, update user balances
            Transfer transfer = createTransfer(2, 2, receivingUserId, amountToSend);
            tenmoAppService.transfer(currentUser, transfer);

            Balance newBalance = tenmoAppService.getBalance(currentUser);
            checkAccountBalance(newBalance);
            System.out.println(BLUE_BOLD + "Sent: " + RED + "$" + amountToSend + ANSI_RESET);
            System.out.println(BLUE_BOLD + "Your new balance is: " + GREEN_UNDERLINED + "$" + checkAccountBalance(newBalance) + ANSI_RESET);
        }
    }

    private void requestBucks() {
        // TODO Auto-generated method stub
        //shows all available users to send TE bucks to
        User[] users = tenmoAppService.getAllUsers(currentUser);
        showAllUsers(users);
        //prompts user for input- ID of user they want to request money from
        int userInput = consoleService.promptForInt(BLUE_BOLD + "Enter the ID of the user you want to request money from (enter 0 to cancel): " + ANSI_RESET);
        if (userInput == 0 || userInput == currentUser.getUser().getId()) { //invalid entries
            System.out.println(BLUE_BOLD + "Transaction cancelled" + ANSI_RESET);
        } else {
            int requestedUserID = 0;
            for (User user : users) {
                if (user.getId() == userInput) {
                    requestedUserID = userInput;
                }
            }
            //prompts user for input- amount requesting
            BigDecimal amountToRequest = consoleService.promptForBigDecimal(BLUE_BOLD + "Enter amount to request: " + ANSI_RESET);
            while (amountToRequest.compareTo(BigDecimal.ZERO) <= 0) { //invalid request
                amountToRequest = consoleService.promptForBigDecimal(CYAN_BOLD + "Please enter a transfer amount greater than 0: " + ANSI_RESET);
            }
            //valid requests create a new transfer, update account balances
            Transfer transfer = createTransfer(1, 1, requestedUserID, amountToRequest);
            tenmoAppService.transfer(currentUser, transfer);

            Balance currentBalance = tenmoAppService.getBalance(currentUser);
            checkAccountBalance(currentBalance);
            System.out.println(BLUE_BOLD + "You requested: " + GREEN_UNDERLINED + "$" + amountToRequest + BLUE_BOLD + ": Status is pending until accepted." + ANSI_RESET);
            System.out.println(BLUE_BOLD + "Your current balance is: " + GREEN_UNDERLINED + "$" + checkAccountBalance(currentBalance) + ANSI_RESET);
        }
    }

    //displays users to console
    private void showAllUsers(User[] users) {
        System.out.println("");
        System.out.println(BLACK_BOLD + "--------------------------------------------------------------" + ANSI_RESET);
        System.out.println(BLUE_BOLD + "Choose an ID of one of the following users: " + ANSI_RESET);
        System.out.println(BLACK_BOLD + "--------------------------------------------------------------" + ANSI_RESET);
        for (User user : users) {
            if (user.getUsername().equals(currentUser.getUser().getUsername())) {
                continue;
            }
            System.out.println(YELLOW + user.getId() + ANSI_RESET + ": " + CYAN + user.getUsername() + ANSI_RESET);
            System.out.println("");
        }
    }

    //
    private BigDecimal checkAccountBalance(Balance newBalance) {
        BigDecimal balance = BigDecimal.valueOf(0.00);
        if (!newBalance.getBalance().equals(BigDecimal.ZERO)) {
            balance = newBalance.getBalance();
        }
        return balance;
    }

    //create a new transfer
    private Transfer createTransfer(int status, int type, int transferUserID, BigDecimal transferAmount) {
        Transfer transfer = new Transfer();
        transfer.setTransferStatusId(status);
        transfer.setTransferTypeId(type);

        Account fromAccount = tenmoAppService.getAccountByUserId(currentUser, transferUserID);
        Account toAccount = tenmoAppService.getAccountByUserId(currentUser, currentUser.getUser().getId());

        if (type == 2) {
            fromAccount = tenmoAppService.getAccountByUserId(currentUser, currentUser.getUser().getId());
            toAccount = tenmoAppService.getAccountByUserId(currentUser, transferUserID);
        }
        transfer.setAccountFrom(fromAccount.getAccountId());
        transfer.setAccountTo(toAccount.getAccountId());
        transfer.setAmount(transferAmount);
        return transfer;
    }
}



