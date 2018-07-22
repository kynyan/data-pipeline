package project.message_queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.GenericContainer;
import project.IntegrationTest;
import project.PersistenceConfig;
import project.PipelineConfig;
import project.WebSocketConfig;
import project.model.Message;
import project.redis.MessagePublisher;
import project.redis.RedisMessageSubscriber;
import project.utils.ObjectMapperFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
public class MessageQueueTest {
    @Rule
    public GenericContainer redis = new GenericContainer("redis:4.0.10").withExposedPorts(6379);

    @Autowired
    private MessagePublisher redisPublisher;

    private ObjectMapper objectMapper = ObjectMapperFactory.OBJECT_MAPPER_WITH_TIMESTAMPS;

    @Test
    public void shouldPublishMessage() throws IOException, InterruptedException {
        Message originalMessage = Message.random();
        redisPublisher.publish(objectMapper.writeValueAsString(originalMessage));
        Thread.sleep(1000); //this is to give redis time to publish and receive message. Ugly, should be refactored.
        List<String> messages = RedisMessageSubscriber.messageList;
        assertEquals(1, messages.size());
        String receivedMessage = messages.get(0);
        assertReflectionEquals(originalMessage, objectMapper.readValue(receivedMessage, Message.class));
    }
}
