package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TenmoAppService {

    private final String API_BASE_URL = "http://localhost:8080/tenmo";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    //get all users
    public User[] getAllUsers(AuthenticatedUser authenticatedUser) {
        User[] users = null;
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(authenticatedUser), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }


    //get user by ID
    public User getUserByUserId(AuthenticatedUser authenticatedUser, int userId) {
        User user = null;
        try {
            ResponseEntity<User> response = restTemplate.exchange(API_BASE_URL + "/users/" + userId, HttpMethod.GET, makeAuthEntity(authenticatedUser), User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }


    //get balance
    public Balance getBalance(AuthenticatedUser authenticatedUser) {
        Balance balance = null;
        try {
            ResponseEntity<Balance> response = restTemplate.exchange(API_BASE_URL + "/balance", HttpMethod.GET, makeAuthEntity(authenticatedUser), Balance.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }


    //get account by User ID
    public Account getAccountByUserId(AuthenticatedUser authenticatedUser, int userId) {
        Account account = null;
        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "/account/users/" + userId, HttpMethod.GET, makeAuthEntity(authenticatedUser), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    //get account by Account ID
    public Account getAccountByAccountId(AuthenticatedUser authenticatedUser, int accountId) {
        Account account = null;
        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "/account/" + accountId, HttpMethod.GET, makeAuthEntity(authenticatedUser), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }


    //transfer
    public void transfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        try {
            restTemplate.exchange(API_BASE_URL + "/transfer", HttpMethod.POST, makeTransferAuthEntity(authenticatedUser, transfer), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    //get all transfers
    public Transfer[] getAllTransfers(AuthenticatedUser authenticatedUser, int accountId) {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfer/view/" + accountId, HttpMethod.GET, makeAuthEntity(authenticatedUser), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    //get transfer by Transfer ID
    public Transfer getTransferByTransferId(AuthenticatedUser authenticatedUser, int transferId) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/transfer/" + transferId, HttpMethod.GET, makeAuthEntity(authenticatedUser), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    //get transfer by Transfer Status ID
    public TransferStatus getTransferStatusByTransferStatusId (AuthenticatedUser authenticatedUser, int transferStatusId) {
        TransferStatus transferStatus = null;
        try {
            ResponseEntity<TransferStatus> response = restTemplate.exchange(API_BASE_URL + "/transfer/status/" + transferStatusId, HttpMethod.GET, makeAuthEntity(authenticatedUser), TransferStatus.class);
            transferStatus = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferStatus;
    }

    //get transfer type by Transfer Type ID
    public TransferType getTransferTypeByTransferTypeId (AuthenticatedUser authenticatedUser, int transferTypeId) {
        TransferType transferType = null;
        try {
            ResponseEntity<TransferType> response = restTemplate.exchange(API_BASE_URL + "/transfer/type/" + transferTypeId, HttpMethod.GET, makeAuthEntity(authenticatedUser), TransferType.class);
            transferType = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferType;
    }

    //approve a transfer
    public void approveTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        try {
            restTemplate.exchange(API_BASE_URL + "/transfer/approval", HttpMethod.PUT, makeTransferAuthEntity(authenticatedUser, transfer), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    //reject a transfer
    public void rejectTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        try {
            restTemplate.exchange(API_BASE_URL + "/transfer/rejection", HttpMethod.PUT, makeTransferAuthEntity(authenticatedUser, transfer), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }


    //http entity
    private HttpEntity makeAuthEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity(headers);
    }

    private HttpEntity<Transfer> makeTransferAuthEntity(AuthenticatedUser authenticatedUser, Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);
        return entity;
    }




}
