package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PreferencesRepository extends JpaRepository<Preferences, String> {

    Preferences findByIdPreferences(String idPreferences);

}
