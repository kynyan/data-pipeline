package project.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Embedded;

import static io.qala.datagen.RandomShortApi.integer;

@Getter @Setter
@Accessors(chain = true)
public class Ticket {
    @Embedded
    private FootballMatch game;
    private Integer price;

    public static Ticket random() {
        return new Ticket().setGame(FootballMatch.random()).setPrice(integer(100, 500));
    }
}
