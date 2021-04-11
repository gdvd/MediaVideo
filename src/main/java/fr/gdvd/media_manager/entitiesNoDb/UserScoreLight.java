package fr.gdvd.media_manager.entitiesNoDb;

import fr.gdvd.media_manager.entitiesMysql.VideoUserScore;
import org.springframework.data.rest.core.config.Projection;

@Projection(
        name = "UserScoreLight",
        types = { VideoUserScore.class })
public interface UserScoreLight {
    String getIdVideo();
    String getDateModifScoreUser();
}
