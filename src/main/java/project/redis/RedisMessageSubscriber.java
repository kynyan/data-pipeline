package project.redis;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import project.service.MessageService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {
//    @Autowired
    private final MessageService messageService;

    public static List<String> messageList = new ArrayList<>();

    public void onMessage(Message message, @Nullable byte[] bytes) {
        String receivedMessage = message.toString();
        messageList.add(receivedMessage);
        log.info("Message received: " + receivedMessage);
        messageService.convertAndSaveMessage(receivedMessage);
    }
}
