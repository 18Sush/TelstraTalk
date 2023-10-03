package com.proj.kafkachat.consumer;

import com.proj.kafkachat.constants.KafkaConstants;
import com.proj.kafkachat.model.Message;
import com.proj.kafkachat.model.WebSocketSessionRegistry;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class MessageListener {
    @Autowired
    SimpMessagingTemplate template;
    WebSocketSessionRegistry sessionRegistry;
    
  

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listen(Message message) {
    	
 
        System.out.println("sending via kafka listener..");
        System.out.println(" message :"+ message.getContent());
       System.out.println("Message send to: " + message.getReceiver());
       System.out.println("Message received from: " + message.getSender());
        System.out.println(" file :"+ message.getFileContent());
        template.convertAndSend("/topic/group", message);
        
        
        //String recipientUserId = message.getRecipientUserId();
        //String notificationContent = message.getNotificationContent();
//        String notificationContent = "Message received from" + message.getSender();
//        WebSocketSession recipientSession = sessionRegistry.getSession(recipientUserId);
//        if (recipientSession != null) {
//            // Send the notification to the specific user's WebSocket session
//            template.convertAndSendToUser(recipientUserId, "/topic/group", notificationContent);
        
      
    }

}
