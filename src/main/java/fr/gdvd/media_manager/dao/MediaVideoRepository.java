package fr.gdvd.media_manager.dao;

import fr.gdvd.media_manager.entities.MediaVideo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MediaVideoRepository extends MongoRepository<MediaVideo, String> {

    MediaVideo getById(String id);

}
