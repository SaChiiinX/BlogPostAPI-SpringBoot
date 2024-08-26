package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.*;
import com.example.exception.*;
import com.example.service.*;

/**
 * The SocialMediaController class is a REST controller that handles HTTP requests 
 * related to accounts and messages in a social media application.
 */
@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    /**
     * Constructs a SocialMediaController with the given AccountService and MessageService.
     * 
     * @param accountService the service to handle account-related operations
     * @param messageService the service to handle message-related operations
     */
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = new MessageService(messageService.getMessageRepository(), accountService.getAccountRepository());
    }

    /**
     * Registers a new account.
     * 
     * @param account the account to be registered
     * @return a ResponseEntity containing the registered Account if successful, 
     *         or a ResponseEntity with an appropriate status code if the registration fails
     */
    @PostMapping("/register")
    public ResponseEntity<Account> postAccount(@RequestBody Account account){
        try{
            Account registeredAccount = accountService.registerAccount(account);
            return ResponseEntity.ok(registeredAccount);
        }catch(DuplicateUsernameException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }catch(InvalidAccountDetailsException e){
            return ResponseEntity.status(400).body(null);
        }    
    }
    
    /**
     * Authenticates a user login.
     * 
     * @param account the account details for login
     * @return a ResponseEntity containing the authenticated Account if successful, 
     *         or a ResponseEntity with a 401 Unauthorized status code if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<Account> postLogin(@RequestBody Account account){
        try{
            Account login = accountService.verifyAccount(account);
            return ResponseEntity.ok(login);
        }catch(InvalidAccountDetailsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * Creates a new message.
     * 
     * @param message the message to be created
     * @return a ResponseEntity containing the created Message if successful, 
     *         or a ResponseEntity with a 400 Bad Request status code if creation fails
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message){
        try{
            Message addedMessage = messageService.addMessage(message);
            return ResponseEntity.ok(addedMessage);
        }catch(InvalidMessageException e){
            return ResponseEntity.status(400).body(null);
        }
    }

    /**
     * Retrieves all messages.
     * 
     * @return a ResponseEntity containing a list of all messages
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages(){
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    /**
     * Retrieves a message by its ID.
     * 
     * @param messageId the ID of the message to retrieve
     * @return a ResponseEntity containing the retrieved Message
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessaageById(@PathVariable("messageId") int messageId){
        return ResponseEntity.ok(messageService.getMessage(messageId));
    }

    /**
     * Deletes a message by its ID.
     * 
     * @param messageId the ID of the message to delete
     * @return a ResponseEntity containing 1 if the deletion was successful, or null if the message was not found
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable("messageId") int messageId){
        Integer ret = messageService.deleteMessage(messageId) == null ? null : 1;
        return ResponseEntity.ok(ret);
    }

    /**
     * Updates a message with new text.
     * 
     * @param messageId the ID of the message to update
     * @param newText the new text to update the message with
     * @return a ResponseEntity containing 1 if the update was successful, 
     *         or a ResponseEntity with a 400 Bad Request status code if the update fails
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> patchMessage(@PathVariable("messageId") int messageId, @RequestBody Message newText){
        try {
            messageService.updateMessage(messageId, newText);
            return ResponseEntity.ok(1);
        } catch (InvalidMessageException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    /**
     * Retrieves all messages for a specific account.
     * 
     * @param accountId the ID of the account to retrieve messages for
     * @return a ResponseEntity containing a list of messages for the specified account
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesById(@PathVariable("accountId") int accountId){
        return ResponseEntity.ok(messageService.getAllMessagesById(accountId));
    } 
}
