package project.controller;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.dto.TicketDto;
import project.model.Message;
import project.redis.RedisMessagePublisher;
import project.repository.MessageRepository;
import project.service.MessageService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(MockitoJUnitRunner.class)
public class MessageEndpointTest {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private RedisMessagePublisher redisMessagePublisher;

    @InjectMocks
    private MessageService messageService;

    @Before
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(MockMvcBuilders
                .standaloneSetup(new MessageEndpoint(redisMessagePublisher, messageService)));
    }

    @Test
    public void shouldGetAllTickets() {
        List<Message> randomMessages = getRandomMessages();
        Mockito.when(messageRepository.findAll()).thenReturn(randomMessages);
        List<TicketDto> expectedTickets = randomMessages.stream().map(Message::getTicket).map(TicketDto::new).collect(Collectors.toList());
        TicketDto[] tickets = given().when().get("/api/messages")
                .then().statusCode(200).extract().body().as(TicketDto[].class);
        assertReflectionEquals(expectedTickets, Arrays.asList(tickets));

    }

    private List<Message> getRandomMessages() {
        return Arrays.asList(Message.random(), Message.random());
    }

}
