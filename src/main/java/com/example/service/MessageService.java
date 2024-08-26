package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.InvalidMessageException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

/**
 * MessageService is a service class responsible for handling business logic related to Message entities.
 * It interacts with the MessageRepository and AccountRepository to perform various operations related to messages.
 */
@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    /**
     * Constructs a MessageService with the given MessageRepository and AccountRepository.
     * 
     * @param messageRepository the repository used to perform message-related database operations
     * @param accountRepository the repository used to perform account-related database operations
     */
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Returns the MessageRepository used by this service.
     * 
     * @return the MessageRepository
     */
    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    /**
     * Adds a new message after validating it.
     * 
     * @param message the Message entity to be added
     * @return the added Message entity
     * @throws InvalidMessageException if the message text is blank, exceeds 255 characters, or if the account does not exist
     */
    public Message addMessage(Message message) throws InvalidMessageException {
        if(message.getMessageText().isBlank() || message.getMessageText().length() > 255 || accountRepository.isAccount(message.getPostedBy()) == null){
            throw new InvalidMessageException();
        }

        return messageRepository.save(message);
    }

    /**
     * Retrieves a message by its ID.
     * 
     * @param messageId the ID of the message to retrieve
     * @return the Message entity if found, or null if not found
     */
    public Message getMessage(int messageId){
        return messageRepository.findById(messageId).orElse(null);
    }

    /**
     * Retrieves all messages.
     * 
     * @return a list of all Message entities
     */
    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    /**
     * Retrieves all messages posted by a specific account.
     * 
     * @param accountId the ID of the account whose messages are to be retrieved
     * @return a list of Message entities posted by the specified account
     */
    public List<Message> getAllMessagesById(int accountId){
        return messageRepository.getAllMessages(accountId);
    }

    /**
     * Deletes a message by its ID.
     * 
     * @param messageId the ID of the message to delete
     * @return the deleted Message entity if it was found and deleted, or null if not found
     */
    public Message deleteMessage(int messageId){
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()){
            Message deletedMessage = optionalMessage.get();
            messageRepository.delete(deletedMessage);
            return deletedMessage;
        }
        return null;
    }

    /**
     * Updates the text of a specific message.
     * 
     * @param messageId the ID of the message to update
     * @param newMessage the Message entity containing the new text
     * @throws InvalidMessageException if the message does not exist, the new text is blank, empty, or exceeds 255 characters
     */
    public void updateMessage(int messageId, Message newMessage) throws InvalidMessageException{
        Message old = getMessage(messageId);
        String newText = newMessage.getMessageText();
        if(old == null || newText.isBlank() || newText.isEmpty() || newText.length() > 255){
            throw new InvalidMessageException();
        }
        old.setMessageText(newText);
        messageRepository.save(old);
    }
}