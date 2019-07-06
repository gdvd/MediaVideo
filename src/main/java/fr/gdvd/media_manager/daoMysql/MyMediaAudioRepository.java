package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyMyMediaAudio;
import fr.gdvd.media_manager.entitiesMysql.MyMediaAudio;
import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.MyMediaLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MyMediaAudioRepository extends JpaRepository<MyMediaAudio, EmbeddedKeyMyMediaAudio> {

    MyMediaAudio findByMyMediaInfoAndMyMediaLanguage(MyMediaInfo myMediaInfo, MyMediaLanguage myMediaLanguage);
    MyMediaAudio findByMyMediaInfo_IdMyMediaInfoAndMyMediaLanguage_IdMyMediaLanguage(String idMyMediaInfo,
                                                                              Long idMyMediaLanguage);

}
