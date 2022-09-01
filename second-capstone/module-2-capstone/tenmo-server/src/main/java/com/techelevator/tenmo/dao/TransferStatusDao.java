package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;

public interface TransferStatusDao {

    TransferStatus viewTransferStatusByTransferStatusId(Long transferStatusId);

}
