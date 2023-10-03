package com.proj.kafkachat.controller;


import com.proj.kafkachat.constants.KafkaConstants;
import com.proj.kafkachat.model.DeadLetterMessage;

import com.proj.kafkachat.model.Message;
import com.proj.kafkachat.model.MessageType;
import com.proj.kafkachat.model.WebSocketSessionRegistry;
import com.proj.kafkachat.repository.MessageRepository;
import com.proj.kafkachat.repository.deadletterrepository;


import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@CrossOrigin(origins="*")
public class ChatController {
	
	private final Logger logger = LoggerFactory.getLogger(ChatController.class);
	
	private Message mostRecentMessage = null;
	
	@Autowired
	public ChatController(SimpMessagingTemplate template, KafkaTemplate<String, Message> kafkaTemplate) {
		super();
		this.template = template;
		this.kafkaTemplate = kafkaTemplate;
	}

	@Autowired
    SimpMessagingTemplate template;
	
    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private deadletterrepository deadLetterMessageRepository;
    
    private byte[] fileContent;

	private String fileName;
    
    MessageType type=null;


    
private  long EXPIRATION_TIME_SECONDS = 3600;
@PostMapping(value = "/api/send", consumes = {"multipart/form-data"})
public ResponseEntity<String> sendMessage(
        @RequestPart("sender") String sender,
        @RequestPart("receiver") String receiver,
        @RequestPart(value = "content", required = false) String content,
        @RequestPart(value = "file", required = false) MultipartFile file) {

    long currentTimeMillis = System.currentTimeMillis();
    long expirationTimeInMillis = currentTimeMillis + (EXPIRATION_TIME_SECONDS * 1000);
     
    
    
	if (file != null && !file.isEmpty()) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            byte[] fileContent = file.getBytes();

            if (currentTimeMillis >= expirationTimeInMillis) {
                DeadLetterMessage deadLetterMessage = new DeadLetterMessage(sender, content, fileContent,
                        System.currentTimeMillis(), receiver);
                deadLetterMessage.setFileName(fileName);
                deadLetterMessage.setReceiver(receiver);

                deadLetterMessageRepository.save(deadLetterMessage);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Expired message sent to dead-letter queue: Message expired");
            } else {
                Message message = new Message(sender, receiver, content, fileName, type, fileContent);
                message.setSender(sender);
                message.setReceiver(receiver);
                message.setContent(content);   
                message.setFileName(fileName);
                message.setTimestamp(LocalDateTime.now().toString());
                message.setFileContent(fileContent);
                

                // Sending the message (text or file) to Kafka topic
                kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message).get();

                // Save the message to the chat repository
                messageRepository.save(message);

                mostRecentMessage = message;

                return ResponseEntity.status(HttpStatus.CREATED).body("Message with file sent successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message with file");
        }
    } else if (content != null) {
        // Handle sending only text
        if (currentTimeMillis >= expirationTimeInMillis) {
            DeadLetterMessage deadLetterMessage = new DeadLetterMessage(sender, content, fileContent,
                    System.currentTimeMillis(), receiver);
            deadLetterMessage.setFileName(fileName);
            deadLetterMessage.setReceiver(receiver);

            deadLetterMessageRepository.save(deadLetterMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Expired message sent to dead-letter queue: Message expired");
        } else {
        	Message message = new Message(sender, receiver, content, fileName, type, fileContent);
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(content);   
            message.setFileName(fileName);
            message.setTimestamp(LocalDateTime.now().toString());
            message.setFileContent(fileContent);
            // Sending the message (text) to Kafka topic
            try {
                kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            // Save the message to the chat repository
            messageRepository.save(message);

            mostRecentMessage = message;

            return ResponseEntity.status(HttpStatus.CREATED).body("Message sent successfully");
        }
    } else {
        return ResponseEntity.badRequest().body("Invalid request. Provide either file, text, or both.");
    }
}
    
    @GetMapping("/download/recent")
	 public ResponseEntity<byte[]> downloadRecentFile() {
	     if (mostRecentMessage != null) {
	         byte[] fileContent = mostRecentMessage.getFileContent();

	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentDisposition(ContentDisposition.builder("attachment")
	                 .filename(mostRecentMessage.getFileName())
	                 .build());

	         headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	         headers.add("File-Download-Success", "File '" + mostRecentMessage.getFileName() + "' downloaded successfully!");

	         // Set the custom 'file-name' header
	         headers.add("file_name", mostRecentMessage.getFileName());

	         return ResponseEntity.ok()
	                 .headers(headers)
	                 .body(fileContent);
	     } else {
	         return ResponseEntity.notFound().build();
	     }
	 }

	
    
    @PostMapping(value ="/test")
    public void testMessage(
    		   @RequestPart("sender") String sender,
    		   @RequestPart("receiver") String receiver,
    		   @RequestPart("content") String content,
    		   @RequestPart("type") MessageType type ) {
    	
    	 Message message = new Message();
         message.setSender(sender);
         message.setReceiver(receiver);
         message.setType(type);
         message.setTimestamp(LocalDateTime.now().toString());

    	System.out.println("testMessage");
    	System.out.println(message.getSender());
    	System.out.println(message.getReceiver());
    	System.out.println(message.getContent());
    	
    }
    
//    @MessageMapping("/application")
//    @SendTo("/all/messages")
//    public Message send(final Message message) throws Exception {
//        return message;
//    }
//
//  


    //    -------------- WebSocket API ----------------
    @MessageMapping("/sendMessage")
    @SendTo("/topic/group")
    public Message broadcastGroupMessage(@Payload Message message) {
        //Sending this message to all the subscribers
        return message;
    }

    @GetMapping("/chat-history")
    public ResponseEntity<List<Message>> getAllChatHistory() {
        // Retrieve all chat history messages from the repository
        List<Message> chatHistory = messageRepository.findAll();

        if (!chatHistory.isEmpty()) {
            return ResponseEntity.ok(chatHistory);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    
    
    @GetMapping("/retrieve-dead-letter-messages")
    public List<DeadLetterMessage> retrieveDeadLetterMessages(@RequestParam(value = "sender") String sender) {
        // Retrieve dead-letter messages based on the sender from the deadletterrepository
        return deadLetterMessageRepository.findBySender(sender);
    }

    @GetMapping("/retrieve-all-dead-letter-messages")
    public List<DeadLetterMessage> retrieveAllDeadLetterMessages() {
        // Retrieve all dead-letter messages from the deadletterrepository
        return deadLetterMessageRepository.findAll();
    }
    /*@PostMapping("/api/resubmit/{messageId}")
    public ResponseEntity<String> resubmitMessage(@PathVariable Long messageId) {
        Optional<DeadLetterMessage> optionalDeadLetterMessage = deadLetterMessageRepository.findById(messageId);

        if (optionalDeadLetterMessage.isPresent()) {
            DeadLetterMessage deadLetterMessage = optionalDeadLetterMessage.get();

            // Ensure that deadLetterMessage contains the expected values
            String sender = deadLetterMessage.getSender();
            String receiver = deadLetterMessage.getReceiver();
            String content = deadLetterMessage.getContent();
            byte[] fileContent = deadLetterMessage.getFileContent();

            try {
              
                
            	Message message = new Message(sender, receiver, content, fileName, type, fileContent);

                // Send the message to Kafka
                kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message).get();

                // Save the message to the message repository
                messageRepository.save(message);

                // Delete the message from the dead letter repository
                deadLetterMessageRepository.delete(deadLetterMessage);

                return ResponseEntity.status(HttpStatus.CREATED).body("Message resubmitted successfully");
            } catch (Exception e) {
                // Add logging for the exception
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to resubmit message");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

           /*try {
               // Simulate a failure by always throwing an exception
               throw new RuntimeException("Simulated failure");

               // The following code will not be executed due to the exception
               // Send the message to Kafka
               // kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, messageToResubmit).get();

               // Save the message to the chat repository
               // chatMessageRepository.save(messageToResubmit);

               // Delete the message from the dead letter repository
               // deadLetterMessageRepository.delete(deadLetterMessage);

               // return ResponseEntity.status(HttpStatus.CREATED).body("Message resubmitted successfully");
           } catch (Exception e) {
               // Log the error
               e.printStackTrace();

               // Mark the message as a poison message and delete it
               deadLetterMessage.setPoison(true);
               deadLetterMessageRepository.delete(deadLetterMessage);
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to resubmit message (marked as poison)");
           }
       } else {
           return ResponseEntity.notFound().build();
       }
   }*/
    @PostMapping("api/resubmit/{messageId}")
    public ResponseEntity<String> resubmitDeadLetter(
            @PathVariable("messageId") Long messageId,
            @RequestParam("sender") String sender,
            @RequestParam("receiver") String receiver) {
        // Retrieve the dead letter message by ID
        Optional<DeadLetterMessage> optionalDeadLetter = deadLetterMessageRepository.findById(messageId);

        if (optionalDeadLetter.isPresent()) {
            DeadLetterMessage deadLetterMessage = optionalDeadLetter.get();

            // Create a new message from the dead letter
            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(deadLetterMessage.getContent());
            message.setFileName(deadLetterMessage.getFileName());
            message.setType(MessageType.TEXT); // Set the type as needed
            message.setFileContent(deadLetterMessage.getFileContent());
            message.setTimestamp(LocalDateTime.now().toString());

            // Send the resubmitted message to Kafka
            try {
                kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to resubmit dead letter message");
            }

            // Save the resubmitted message to the message repository
            messageRepository.save(message);

            // Delete the dead letter message
            deadLetterMessageRepository.deleteById(messageId);

            return ResponseEntity.status(HttpStatus.CREATED).body("Dead letter message resubmitted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @KafkaListener(topics = "kafka-chat-3", groupId = "kafka-sandbox")
    public void receiveMessage(String message) {
        // Handle the received message

        // Log the message to the separate log file
        logger.info("Received message: " + message);
    }

	public void setMostRecentMessage(Message createSampleMessage) {
		// TODO Auto-generated method stub
		
	}
}