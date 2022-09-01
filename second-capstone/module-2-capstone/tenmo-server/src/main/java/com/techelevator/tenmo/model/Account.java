package com.techelevator.tenmo.model;

public class Account {

    private Balance balance;
    private Long userId;
    private Long accountId;

    public Account() {
    }

    public Account(Balance balance, Long userId, Long accountId) {
        this.balance = balance;
        this.userId = userId;
        this.accountId = accountId;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
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
