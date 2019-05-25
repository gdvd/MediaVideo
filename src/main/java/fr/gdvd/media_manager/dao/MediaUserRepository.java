package fr.gdvd.media_manager.dao;

import fr.gdvd.media_manager.entities.MediaUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface MediaUserRepository extends
        MongoRepository<MediaUser, String> {

    List<MediaUser> findAllByLoginNotNull();
    List<MediaUser> findByActive();
    Optional<MediaUser> findByLogin(String login);
    Optional<MediaUser> findById(String id);
    MediaUser save(MediaUser mediaUser);
    void deleteByLogin(String id);


}
