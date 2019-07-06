package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.MyMediaLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MyMediaLanguageRepository extends JpaRepository<MyMediaLanguage, Long> {

    MyMediaLanguage findByLanguage(String language);
}
