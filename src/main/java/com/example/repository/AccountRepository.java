package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Account;

/**
 * AccountRepository is a Spring Data JPA repository for managing Account entities.
 * It provides methods to perform CRUD operations and custom queries on the Account table.
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {

    /**
     * Verifies account details by checking the username and password.
     * 
     * @param username the username of the account
     * @param password the password of the account
     * @return the Account entity if the username and password match, or null if they do not match
     */
    @Query("from Account where username = :username and password = :password")
    Account verifyAccountDetails(@Param("username") String username, @Param("password") String password);

    /**
     * Checks if an account exists by its username.
     * 
     * @param username the username of the account
     * @return the Account entity if an account with the given username exists, or null if it does not exist
     */
    @Query("from Account where username = :username")
    Account isAccount(@Param("username") String username);

    /**
     * Checks if an account exists by its accountId.
     * 
     * @param accountId the ID of the account
     * @return the Account entity if an account with the given accountId exists, or null if it does not exist
     */
    @Query("from Account where accountId = :accountId")
    Account isAccount(@Param("accountId") int accountId);
}