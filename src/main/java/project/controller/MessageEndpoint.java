package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.TicketDto;
import project.model.Message;
import project.redis.RedisMessagePublisher;
import project.redis.RedisMessageSubscriber;
import project.service.MessageService;
import project.utils.ObjectMapperFactory;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class MessageEndpoint {

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;
    @Autowired
    private MessageService messageService;

    @PostMapping(path = "/message")
    public ResponseEntity publish(@RequestBody String message) {
        redisMessagePublisher.publish(message);
        return ResponseEntity.ok().build();
    }
    //endpoint is not in spec
    @GetMapping(path = "/message")
    public Message getMessage() {
        String message = RedisMessageSubscriber.messageList.get(0);
        try {
            return ObjectMapperFactory.OBJECT_MAPPER_WITH_TIMESTAMPS.readValue(message, Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(path = "/messages")
    public List<TicketDto> getTickets() {
        return messageService.findAllTickets();
    }
    //temporary endpoint (remove it before publishing)
    @GetMapping(path = "/random")
    public Message getRandomMessage() {
        return Message.random();
    }
}
