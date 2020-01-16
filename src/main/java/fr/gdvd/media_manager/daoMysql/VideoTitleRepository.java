package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyVideoTitle;
import fr.gdvd.media_manager.entitiesMysql.VideoCountry;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface VideoTitleRepository extends JpaRepository<VideoTitle, EmbeddedKeyVideoTitle> {

    Optional<VideoTitle> findByVideoFilmAndVideoCountry(VideoFilm videoFilm, VideoCountry videoCountry);
    Optional<VideoTitle> findByVideoFilm_IdVideoAndVideoCountry_IdCountry(String idVideoFilm, Long idCountry);
    Optional<VideoTitle> findByVideoFilm(VideoFilm vf);
    List<VideoTitle> findByVideoFilm_IdVideo(String idVideoFilm);
    @Query("select vt from fr.gdvd.media_manager.entitiesMysql.VideoTitle as vt " +
            "where vt.videoFilm =:vf")
    List<VideoTitle> findFirstByIdVideo(VideoFilm vf);
}
