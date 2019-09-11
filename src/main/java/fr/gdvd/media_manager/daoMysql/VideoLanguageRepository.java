package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface VideoLanguageRepository extends JpaRepository<VideoLanguage, Long> {

    Optional<VideoLanguage> findByLanguage(String language);
    Optional<VideoLanguage> findByUrlLanguage(String urlLanguage);


}
