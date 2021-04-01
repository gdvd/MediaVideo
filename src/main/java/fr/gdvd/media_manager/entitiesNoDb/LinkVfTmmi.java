package fr.gdvd.media_manager.entitiesNoDb;

import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LinkVfTmmi {

    private Long link;
    private VideoFilm vf;

}
