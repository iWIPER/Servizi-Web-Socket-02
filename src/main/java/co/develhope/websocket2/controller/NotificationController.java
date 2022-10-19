package co.develhope.websocket2.controller;

import co.develhope.websocket2.entities.ClientMessageDTO;
import co.develhope.websocket2.entities.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/broadcast-message")
    public ResponseEntity sendNotificationToClients(@RequestBody MessageDTO message){
        simpMessagingTemplate.convertAndSend("/topic/broadcast",message);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    @MessageMapping("/client-message")
    @SendTo("/topic/broadcast")
    public MessageDTO handleMessageFromWebSocket(ClientMessageDTO message){
        System.out.println("You have received a new message on /app/hello | " + message.toString());
        return new MessageDTO("Welcome message to " + message.getClientName() , " - Message: " + message.getClientAlert(), " IMPORTANT - " + message.getClientMsg());
    }
}