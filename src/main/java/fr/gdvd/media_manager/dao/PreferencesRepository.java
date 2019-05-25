package fr.gdvd.media_manager.dao;

import fr.gdvd.media_manager.entities.Preferences;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource /* CrudRepository OR MongoRepository */
public interface PreferencesRepository extends MongoRepository<Preferences, String> {
}
