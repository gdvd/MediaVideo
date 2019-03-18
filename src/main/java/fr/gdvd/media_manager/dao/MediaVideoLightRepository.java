package fr.gdvd.media_manager.dao;

import fr.gdvd.media_manager.entities.MediaVideoLight;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MediaVideoLightRepository extends MongoRepository<MediaVideoLight, ObjectId> {



}
