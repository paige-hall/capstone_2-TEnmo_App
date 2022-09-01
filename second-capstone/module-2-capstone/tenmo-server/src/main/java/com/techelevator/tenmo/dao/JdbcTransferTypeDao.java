package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferTypeDao implements TransferTypeDao {

    private JdbcTemplate jdbcTemplate;
    public JdbcTransferTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferType viewTransferTypeByTransferTypeId(Long transferTypeId) {
        TransferType transferType = new TransferType();
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_type WHERE transfer_type_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferTypeId);
        if (results.next()) {
            transferType = mapRowToTransferType(results);
        }
        return transferType;
    }

    private TransferType mapRowToTransferType(SqlRowSet rs) {
        TransferType transferType = new TransferType();
        transferType.setTransferTypeId(rs.getInt("transfer_type_id"));
        transferType.setTransferTypeDescription(rs.getString("transfer_type_desc"));
        return transferType;
    }

}
