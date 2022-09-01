package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/tenmo")
public class AppController {

    private UserDao userDao;
    private AccountDao accountDao;
    private TransferDao transferDao;
    private TransferStatusDao transferStatusDao;
    private TransferTypeDao transferTypeDao;

    public AppController(UserDao userDao, AccountDao accountDao, TransferDao transferDao, TransferStatusDao transferStatusDao, TransferTypeDao transferTypeDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.transferStatusDao = transferStatusDao;
        this.transferTypeDao = transferTypeDao;
    }

    @GetMapping("/balance")
    public Balance getBalance(Principal principal) {
        return accountDao.getBalance(principal.getName());
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUserByUserId(@PathVariable long id) {
        return userDao.findByUserID(id);
    }

    @GetMapping("/account/{id}")
    public Account getAccountByAccountId(@PathVariable long id) {
        return accountDao.getAccountByAccountId(id);
    }

    @GetMapping("/account/users/{id}")
    public Account getAccountByUserId(@PathVariable long id) {
        return accountDao.getAccountByUserId(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/transfer")
    public void transfer(@RequestBody Transfer transfer) {
        transferDao.transfer(transfer);
    }

    @PutMapping("/transfer/approval")
    public void approveTransfer(@RequestBody Transfer transfer) {
        transferDao.approveTransfer(transfer);
    }

    @PutMapping("/transfer/rejection")
    public void rejectTransfer(@RequestBody Transfer transfer) {
        transferDao.rejectTransfer(transfer);
    }

    @GetMapping("transfer/view/{id}")
    public List<Transfer> viewAllTransfersByAccountId(@PathVariable("id") Long accountId) {
        return transferDao.viewAllTransfersByAccountId(accountId);
    }

    @GetMapping("/transfer/{id}")
    public Transfer viewTransferByTransferId(@PathVariable("id") Long transferId) {
        return transferDao.viewTransferByTransferId(transferId);
    }

    @GetMapping("/transfer/status/{id}")
    public TransferStatus viewTransferStatusByTransferStatusId(@PathVariable("id") Long transferStatusId) {
        return transferStatusDao.viewTransferStatusByTransferStatusId(transferStatusId);
    }

    @GetMapping("/transfer/type/{id}")
    public TransferType viewTransferTypeByTransferTypeId(@PathVariable("id") Long transferTypeId) {
        return transferTypeDao.viewTransferTypeByTransferTypeId(transferTypeId);
    }
}

