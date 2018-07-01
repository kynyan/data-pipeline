package project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.dto.TicketDto;
import project.model.Message;
import project.repository.MessageRepository;
import project.utils.ObjectMapperFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;

    private ObjectMapper objectMapper = ObjectMapperFactory.OBJECT_MAPPER_WITH_TIMESTAMPS;

    public Message convertAndSaveMessage(String receivedMessage) {
        Message message = messageRepository.save(getConvertedMessage(receivedMessage));
        log.info("Message saved with ID [{}]", message.getId());
        return message;
    }

    public List<TicketDto> findAllTickets() {
        return messageRepository.findAll().stream()
                .map(Message::getTicket)
                .map(TicketDto::new).collect(Collectors.toList());
    }

    private Message getConvertedMessage(String receivedMessage) {
        project.model.Message message = null;
        try {
            message = objectMapper.readValue(receivedMessage, Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
