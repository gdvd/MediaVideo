package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyVideoFilmArtist;
import fr.gdvd.media_manager.entitiesMysql.VideoArtist;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VideoFilmArtistRepository extends JpaRepository<VideoFilmArtist, EmbeddedKeyVideoFilmArtist> {

    VideoFilmArtist findByVideoFilmAndVideoArtist(VideoFilm vf, VideoArtist va);
    VideoFilmArtist findByVideoFilm_IdVideoAndVideoArtist_IdVideoArtist(String idVideoFilm, String idVideoArtist);

}
