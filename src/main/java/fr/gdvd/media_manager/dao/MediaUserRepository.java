package fr.gdvd.media_manager.dao;

import fr.gdvd.media_manager.entities.MediaUser;
import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MediaUserRepository extends
        CrudRepository<MediaUser, ObjectId> {
    MediaUser findByLogin(String login);

}
