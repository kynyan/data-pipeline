package project.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

import static io.qala.datagen.RandomShortApi.positiveLong;

@Getter @Setter
@Accessors(chain = true)
@Entity
@Table(name = "messages")
public class Message {
    public static final String SEQUENCE_GENENERATOR = "user_seq_gen";
    public static final String SEQUENCE_NAME = "USER_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENENERATOR)
    @SequenceGenerator(name = SEQUENCE_GENENERATOR, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    private Long id;

    @Embedded
    private Ticket ticket;

    public static Message random() {
        return new Message().setId(positiveLong()).setTicket(Ticket.random());
    }
}
