package project.message_queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.GenericContainer;
import project.PipelineConfig;
import project.model.Message;
import project.redis.RedisMessagePublisher;
import project.redis.RedisMessageSubscriber;
import project.utils.ObjectMapperFactory;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PipelineConfig.class)
public class MessageQueueTest {
    @Rule
    public GenericContainer redis = new GenericContainer("redis:4.0.10").withExposedPorts(6379);

    @Autowired
    private RedisMessagePublisher messagePublisher;

    private ObjectMapper objectMapper = ObjectMapperFactory.OBJECT_MAPPER_WITH_TIMESTAMPS;

    @Test
    public void shouldPublishMessage() throws IOException {
        Message originalMessage = Message.random();
        String redisUrl = redis.getContainerIpAddress() + ":" + redis.getMappedPort(6379);
        messagePublisher.publish(objectMapper.writeValueAsString(originalMessage));
        String receivedMessage = RedisMessageSubscriber.messageList.get(0);
        assertNotNull(receivedMessage);
        assertReflectionEquals(originalMessage, objectMapper.readValue(receivedMessage, Message.class));
    }
}
