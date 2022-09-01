package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Balance getBalance(String username) {
        Balance balance = new Balance();
        String sql = "SELECT balance FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE username = ?;";
        SqlRowSet results= jdbcTemplate.queryForRowSet(sql, username);
        if (results.next()) {
            String stringBalance = results.getString("balance");
            balance.setBalance(new BigDecimal(stringBalance));
        }
        return balance;
    }

    @Override
    public Account getAccountByUserId(Long userId) {
        Account account = new Account();
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()){
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountId(Long accountId) {
        Account account = new Account();
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);

        if (results.next()){
            account = mapRowToAccount(results);
        }
        return account;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getLong("account_id"));
        account.setUserId(rs.getLong("user_id"));
        Balance balance = new Balance();
        String stringBalance = rs.getString("balance");
        balance.setBalance(new BigDecimal(stringBalance));
        account.setBalance(balance);
        return account;
    }
}
