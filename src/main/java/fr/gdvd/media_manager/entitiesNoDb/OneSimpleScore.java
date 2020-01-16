package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OneSimpleScore {

    private String idVideoFilm;
    private String title;
    private Integer score;
    private Integer scoreUser;
    private long nbOfVote;
    private int actorPos;
    private Date dateModif;
    private String comment;

}
