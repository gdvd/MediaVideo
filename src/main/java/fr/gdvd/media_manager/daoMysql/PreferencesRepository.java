package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@RepositoryRestResource
public interface PreferencesRepository extends JpaRepository<Preferences, String> {

    Preferences findByIdPreferences(String idPreferences);

    /*@Transactional
    @Modifying
    @Query("update Preferences p SET p.extset=:myset WHERE p.idPreferences=:id")
    void modif(Set<String> myset, String id);*/

}
