package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoTrailler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VideoTraillerRepository extends JpaRepository<VideoTrailler, Long> {

    VideoTrailler findByVideoFilm(VideoFilm videoFilm);

}
