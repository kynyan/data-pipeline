package project.model;

import io.qala.datagen.RandomShortApi;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Embedded;

@Getter @Setter
@Accessors(chain = true)
public class Ticket {
    @Embedded
    private FootballMatch game;
    private double price;

    public static Ticket random() {
        return new Ticket().setGame(FootballMatch.random()).setPrice(RandomShortApi.Double(100, 500));
    }
}
