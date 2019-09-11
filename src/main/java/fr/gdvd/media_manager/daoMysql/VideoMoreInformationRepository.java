package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoMoreInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface VideoMoreInformationRepository extends JpaRepository<VideoMoreInformation, Long> {

    Optional<VideoMoreInformation> findByVideoFilm(VideoFilm videoFilm);

}
