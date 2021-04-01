package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoPoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VideoPosterRepository extends JpaRepository<VideoPoster, Long> {

//    VideoPoster findByVideoFilm(VideoFilm videoFilm);
    VideoPoster findFirstByVideoFilm(VideoFilm videoFilm);

}
