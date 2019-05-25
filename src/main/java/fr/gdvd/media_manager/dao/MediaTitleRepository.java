package fr.gdvd.media_manager.dao;

import fr.gdvd.media_manager.entities.MediaTitle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MediaTitleRepository extends MongoRepository<MediaTitle, String> {

    MediaTitle getById(String id);

}
