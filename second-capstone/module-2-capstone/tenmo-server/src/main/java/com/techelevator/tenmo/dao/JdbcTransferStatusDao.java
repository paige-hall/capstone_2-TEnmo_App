package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferStatusDao implements TransferStatusDao {

    private JdbcTemplate jdbcTemplate;
    public JdbcTransferStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public TransferStatus viewTransferStatusByTransferStatusId(Long transferStatusId) {
        TransferStatus transferStatus = new TransferStatus();
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status WHERE transfer_status_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferStatusId);
        if (results.next()) {
            transferStatus = mapRowToTransferStatus(results);
        }
        return transferStatus;
    }


    private TransferStatus mapRowToTransferStatus(SqlRowSet rs) {
        TransferStatus transferStatus = new TransferStatus();
        transferStatus.setTransferStatusId(rs.getInt("transfer_status_id"));
        transferStatus.setTransferStatusDescription(rs.getString("transfer_status_desc"));
        return transferStatus;
    }
}
