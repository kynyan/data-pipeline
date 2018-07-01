package project.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import project.dto.TicketDto;
import project.model.Message;

@Controller
public class WebSocketEndpoint {

    @MessageMapping("/tickets")
    @SendTo("/send/tickets")
    public TicketDto send(Message message) throws Exception {
        return new TicketDto(message.getTicket());
    }
}
