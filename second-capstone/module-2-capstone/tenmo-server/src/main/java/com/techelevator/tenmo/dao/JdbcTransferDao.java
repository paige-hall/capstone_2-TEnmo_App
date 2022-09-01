package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void transfer(Transfer transfer) {
        boolean approved = transfer.getTransferStatusId() == 2;
        AccountDao accountDao = new JdbcAccountDao(jdbcTemplate);
        int fromAccountId = transfer.getAccountFrom();
        int toAccountId = transfer.getAccountTo();
        Account fromAccount = accountDao.getAccountByAccountId((long) fromAccountId);
        Account toAccount = accountDao.getAccountByAccountId((long) toAccountId);
        BigDecimal fromAccountBalance = fromAccount.getBalance().getBalance();
        BigDecimal toAccountBalance = toAccount.getBalance().getBalance();
        BigDecimal transferAmount = transfer.getAmount();
        if (approved) {
            fromAccountBalance = fromAccountBalance.subtract(transferAmount);
            toAccountBalance = toAccountBalance.add(transferAmount);
        }
        String sql = "START TRANSACTION; " +
                "UPDATE account " +
                "SET balance = ? " +
                "WHERE account_id = ?; " +
                "UPDATE account " +
                "SET balance = ? " +
                "WHERE account_id = ?; " +
                "INSERT INTO transfer (transfer_type_id, transfer_status_id,account_from, account_to, amount) " +
                " VALUES (?, ?, ?, ?, ?);" +
                "COMMIT;";

        jdbcTemplate.update(sql, fromAccountBalance, fromAccountId, toAccountBalance, toAccountId,
                transfer.getTransferTypeId(), transfer.getTransferStatusId(), fromAccountId, toAccountId, transferAmount);
    }


    @Override
    public List<Transfer> viewAllTransfersByAccountId(Long accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                " FROM transfer " +
                " WHERE account_from = ? " +
                " OR account_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);

        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }



    @Override
    public Transfer viewTransferByTransferId(Long transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                " FROM transfer " +
                " WHERE transfer_id = ?; ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }


    @Override
    public void approveTransfer(Transfer transfer) {
        int transferStatus = 2;
        AccountDao accountDao = new JdbcAccountDao(jdbcTemplate);
        int fromAccountId = transfer.getAccountFrom();
        int toAccountId = transfer.getAccountTo();
        Account fromAccount = accountDao.getAccountByAccountId((long) fromAccountId);
        Account toAccount = accountDao.getAccountByAccountId((long) toAccountId);
        BigDecimal fromAccountBalance = fromAccount.getBalance().getBalance();
        BigDecimal toAccountBalance = toAccount.getBalance().getBalance();
        BigDecimal transferAmount = transfer.getAmount();
        fromAccountBalance = fromAccountBalance.subtract(transferAmount);
        toAccountBalance = toAccountBalance.add(transferAmount);

        String sql = " START TRANSACTION; " +
                " UPDATE account " +
                " SET balance = ? " +
                " WHERE account_id = ?; " +
                " UPDATE account " +
                " SET balance = ? " +
                " WHERE account_id = ?; " +
                " UPDATE transfer " +
                " SET transfer_status_id = ?" +
                " WHERE transfer_id = ?; " +
                " COMMIT;";

        jdbcTemplate.update(sql, fromAccountBalance, fromAccountId, toAccountBalance, toAccountId, transferStatus, transfer.getTransferId());
    }


    @Override
    public void rejectTransfer(Transfer transfer) {
        int transferStatus = 3;

        String sql = " UPDATE transfer " +
                " SET transfer_status_id = ?" +
                " WHERE transfer_id = ?; ";

        jdbcTemplate.update(sql, transfer.getTransferId(), transferStatus);

    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        String stringAmount = rs.getString("amount");
        transfer.setAmount(new BigDecimal(stringAmount));
        return transfer;
    }

}

