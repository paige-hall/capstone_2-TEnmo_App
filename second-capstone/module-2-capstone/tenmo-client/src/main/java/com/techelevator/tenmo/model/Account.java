package com.techelevator.tenmo.model;

public class Account {

    private Balance balance;
    private int userId;
    private int accountId;

    public Account(Balance balance, int userId, int accountId) {
        this.balance = balance;
        this.userId = userId;
        this.accountId = accountId;
    }

    public Account() {
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                ", userId=" + userId +
                ", accountId=" + accountId +
                '}';
    }
}


