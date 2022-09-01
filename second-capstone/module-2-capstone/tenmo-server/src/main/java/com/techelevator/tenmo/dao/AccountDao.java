package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;

public interface AccountDao {

    //balance, account id, user id
    Balance getBalance(String username);
    Account getAccountByUserId(Long userId);
    Account getAccountByAccountId(Long accountId);

}
