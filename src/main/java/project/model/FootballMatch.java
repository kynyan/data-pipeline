package project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static io.qala.datagen.RandomShortApi.sample;

@Getter @Setter
@Accessors(chain = true)
@Embeddable
public class FootballMatch {
    private static List<String> countries = Arrays.asList("Argentina", "Australia", "Belgium", "Brazil", "Colombia",
            "Costa Rica", "Croatia", "Denmark", "Egypt", "England", "France", "Germany", "Iceland", "Iran", "Japan",
            "South Korea", "Mexico", "Morocco", "Negeria", "Panama", "Peru", "Poland", "Portugal", "Russia",
            "Saudi Arabia", "Senegal", "Serbia", "Spain", "Sweden", "Switzerland", "Tunisia", "Uruguay");
    private static List<String> stadiums = Arrays.asList("Arena", "Luzhniki", "Spartak", "Fisht");
    private static List<String> cities = Arrays.asList("Moscow", "Sochi", "St. Petersburg", "Yekaterinburg",
            "Nizhniy Novgorod", "Saransk", "Kazan", "Rostov-on-Don", "Yaroslavl", "Kaliningrad", "Volgograd");

    private String firstTeam;
    private String secondTeam;

    @Embedded
    private Stadium stadium;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate matchDate;

    public static FootballMatch random() {
        return new FootballMatch()
                .setFirstTeam(sample(countries))
                .setSecondTeam(sample(countries))
                .setMatchDate(LocalDate.now())
                .setStadium(new Stadium(sample(cities), sample(stadiums)));
    }
}
