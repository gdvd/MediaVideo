package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.TypeMmi;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface TypeMmiRepository extends JpaRepository<TypeMmi, Long> {

    TypeMmi findByVideoFilm(VideoFilm videoFilm);

}
