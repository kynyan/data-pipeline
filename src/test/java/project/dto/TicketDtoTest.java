package project.dto;

import org.junit.Test;
import project.model.Message;
import project.model.Ticket;

import static org.junit.Assert.assertEquals;

public class TicketDtoTest {

    @Test
    public void shouldConvetTicketToDto() {
        Ticket ticket = Message.random().getTicket();
        TicketDto dto = new TicketDto(ticket);
        String expectedGame = String.format("%s-%s", ticket.getGame().getFirstTeam(), ticket.getGame().getSecondTeam());
        String expectedCity = ticket.getGame().getStadium().getCity();
        String expectedStadium = ticket.getGame().getStadium().getName();
        Integer expectedPrice = ticket.getPrice();
        assertEquals(expectedGame, dto.getGame());
        assertEquals(expectedCity, dto.getCity());
        assertEquals(expectedStadium, dto.getStadium());
        assertEquals(expectedPrice, dto.getPrice());
    }
}
