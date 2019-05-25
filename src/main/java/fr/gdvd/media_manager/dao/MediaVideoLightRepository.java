package fr.gdvd.media_manager.dao;

import fr.gdvd.media_manager.entities.MediaVideoLight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface MediaVideoLightRepository extends MongoRepository<MediaVideoLight, String> {

    MediaVideoLight getById(String id);
    List<MediaVideoLight> findMediaVideoLightsByTitleContaining(String req);
    List<MediaVideoLight> findAll();

}
