package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyVideoUserScore;
import fr.gdvd.media_manager.entitiesMysql.MyUser;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoUserScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VideoUserScoreRepository extends JpaRepository<VideoUserScore, EmbeddedKeyVideoUserScore> {

    VideoUserScore findByVideoFilmAndMyUser(VideoFilm vf, MyUser mu);
    VideoUserScore findByVideoFilm_IdVideoAndMyUser_IdMyUser(String idVideo, Long idMyUser);

}
