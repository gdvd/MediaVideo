package fr.gdvd.media_manager.dao;

import fr.gdvd.media_manager.entities.MediaConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MediaConfigRepository extends MongoRepository<MediaConfig, String> {



}
