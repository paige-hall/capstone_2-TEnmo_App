package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    public void transfer(Transfer transfer);
    List<Transfer> viewAllTransfersByAccountId(Long accountId);
    Transfer viewTransferByTransferId(Long transferId);
    public void approveTransfer(Transfer transfer);
    public void rejectTransfer(Transfer transfer);

}
