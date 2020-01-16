package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class VideoFilmLight {

    private String idTt;
    private int actorPos;
    private int score;

}
