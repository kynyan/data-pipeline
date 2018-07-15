package project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.TicketDto;
import project.model.Message;
import project.redis.RedisMessagePublisher;
import project.service.MessageService;
import project.utils.ObjectMapperFactory;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class MessageEndpoint {

    private final RedisMessagePublisher redisMessagePublisher;
    private final MessageService messageService;

    @PostMapping(path = "/message")
    public ResponseEntity publish(@RequestBody String message) {
        redisMessagePublisher.publish(message);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/messages")
    public List<TicketDto> getTickets() {
        return messageService.findAllTickets();
    }

    //helper endpoint which is not specified in requirements
    @PostMapping(path = "/random")
    public ResponseEntity generateAndSaveRandomMessage() throws JsonProcessingException {
        redisMessagePublisher.publish(ObjectMapperFactory.OBJECT_MAPPER_WITH_TIMESTAMPS.writeValueAsString(Message.random()));
        return ResponseEntity.ok().build();
    }
}
