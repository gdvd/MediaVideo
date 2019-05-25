package fr.gdvd.media_manager.dao;

import fr.gdvd.media_manager.entities.MediaRole;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface MediaRoleRepository extends
        MongoRepository<MediaRole, String> {

    MediaRole findByRole(String role);
    Optional<MediaRole> findById(ObjectId objectId);
    List<MediaRole> findAll();

}
