package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyVideoTitle;
import fr.gdvd.media_manager.entitiesMysql.VideoCountry;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface VideoTitleRepository extends JpaRepository<VideoTitle, EmbeddedKeyVideoTitle> {

    Optional<VideoTitle> findByVideoFilmAndVideoCountry(VideoFilm videoFilm, VideoCountry videoCountry);
    List<VideoTitle> findByVideoFilm_IdVideo(String idVideoFilm);
    Optional<VideoTitle> findByVideoFilm_IdVideoAndVideoCountry_IdCountry(String idVideoFilm, Long idCountry);
    Optional<VideoTitle> findByVideoCountryAndVideoFilm(
            VideoCountry vc, VideoFilm vf);
    @Query("select vt.title from VideoTitle as vt " +
            "where vt.id.idVideo = :idvideo")
    List<String> findTitlesByIdvideo(String idvideo);

}
