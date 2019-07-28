package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyVideoTitle;
import fr.gdvd.media_manager.entitiesMysql.VideoCountry;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface VideoTitleRepository extends JpaRepository<VideoTitle, EmbeddedKeyVideoTitle> {

    Optional<VideoTitle> findByVideoFilmAndVideoCountry(VideoFilm videoFilm, VideoCountry videoCountry);
    Optional<VideoTitle> findByVideoFilm_IdVideoAndVideoCountry_IdCountry(String idVideoFilm, Long idCountry);

}
