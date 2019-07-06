package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyMyMediaAudio;
import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.MyMediaLanguage;
import fr.gdvd.media_manager.entitiesMysql.MyMediaText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MyMediaTextRepository extends JpaRepository<MyMediaText, EmbeddedKeyMyMediaAudio> {

    MyMediaText findByMyMediaInfoAndMyMediaLanguage(MyMediaInfo myMediaInfo, MyMediaLanguage myMediaLanguage);
    MyMediaText findByMyMediaInfo_IdMyMediaInfoAndMyMediaLanguage_IdMyMediaLanguage(String idMyMediaInfo,
                                                                                    Long idMyMediaLanguage);

}
