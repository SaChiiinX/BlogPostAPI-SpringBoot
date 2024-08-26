package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Message;

import java.util.List;

/**
 * MessageRepository is a Spring Data JPA repository for managing Message entities.
 * It provides methods to perform CRUD operations and custom queries on the Message table.
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {

    /**
     * Retrieves all messages posted by a specific account.
     * 
     * @param accountId the ID of the account whose messages are to be retrieved
     * @return a list of Message entities posted by the specified account
     */
    @Query("from Message where postedBy = :accountId")
    List<Message> getAllMessages(@Param("accountId") int accountId);

    /**
     * Updates the text of a specific message.
     * 
     * @param messageId the ID of the message to update
     * @param messageText the new text for the message
     * @return the updated Message entity
     */
    @Query("update Message set messageText = :messageText where messageId = :messageId")
    Message updateMessage(@Param("messageId") int messageId, @Param("messageText") String messageText);
}

