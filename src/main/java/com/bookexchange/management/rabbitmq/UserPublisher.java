package com.bookexchange.management.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPublisher {

    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;


    @Autowired
    public UserPublisher(AmqpTemplate amqpTemplate, ObjectMapper objectMapper) {
        this.amqpTemplate = amqpTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendUserMessage(Long id, String email) {
        try {
            // Create a user object
            UserDetail user = new UserDetail(id, email);

            // Convert the user object to JSON
            String message = objectMapper.writeValueAsString(user);

            // Send the message to the exchange with routing key 'user.routing.key'
            amqpTemplate.convertAndSend("user.exchange", "user.routing.key", message);
            System.out.println("Message sent: " + message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Simple User class to hold the data
    public static class UserDetail {
        private Long id;
        private String email;

        // Constructors, Getters, Setters
        public UserDetail(Long id, String email) {
            this.id = id;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

