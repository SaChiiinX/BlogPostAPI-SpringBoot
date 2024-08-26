package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exception.*;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

/**
 * AccountService is a service class responsible for handling business logic related to Account entities.
 * It interacts with the AccountRepository to perform various operations related to accounts.
 */
@Service
public class AccountService {
    private AccountRepository accountRepository;

    /**
     * Constructs an AccountService with the given AccountRepository.
     * 
     * @param accountRepository the repository used to perform account-related database operations
     */
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    /**
     * Returns the AccountRepository used by this service.
     * 
     * @return the AccountRepository
     */
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    /**
     * Registers a new account after validating the account details.
     * 
     * @param account the Account entity to be registered
     * @return the registered Account entity
     * @throws DuplicateUsernameException if an account with the same username already exists
     * @throws InvalidAccountDetailsException if the username is blank or the password is too short
     */
    public Account registerAccount(Account account) throws DuplicateUsernameException, InvalidAccountDetailsException{
        if(isAccount(account.getUsername())){
            throw new DuplicateUsernameException();
        }

        if(account.getUsername().isBlank() || account.getPassword().length() < 4){
            throw new InvalidAccountDetailsException();
        }

        return accountRepository.save(account);
    }

    /**
     * Verifies the account details for login.
     * 
     * @param account the Account entity containing the login credentials
     * @return the verified Account entity if credentials are valid
     * @throws InvalidAccountDetailsException if the credentials are invalid
     */
    public Account verifyAccount(Account account) throws InvalidAccountDetailsException{
        Account verifiedAccount = accountRepository.verifyAccountDetails(account.getUsername(), account.getPassword());
        if(verifiedAccount == null){
            throw new InvalidAccountDetailsException();
        }
        return verifiedAccount;
    }

    /**
     * Checks if an account exists with the given username.
     * 
     * @param username the username to check
     * @return true if an account with the given username exists, false otherwise
     */
    public boolean isAccount(String username){
        return accountRepository.isAccount(username) != null;
    }

    /**
     * Checks if an account exists with the given account ID.
     * 
     * @param accountId the ID of the account to check
     * @return true if an account with the given ID exists, false otherwise
     */
    public boolean isAccount(int accountId){
        return accountRepository.isAccount(accountId) != null;
    } 
}