package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyMyMediaAudio;
import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.MyMediaLanguage;
import fr.gdvd.media_manager.entitiesMysql.MyMediaText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource
public interface MyMediaTextRepository extends JpaRepository<MyMediaText, EmbeddedKeyMyMediaAudio> {

    MyMediaText findByMyMediaInfoAndMyMediaLanguage(MyMediaInfo myMediaInfo, MyMediaLanguage myMediaLanguage);
    MyMediaText findByMyMediaInfo_IdMyMediaInfoAndMyMediaLanguage_IdMyMediaLanguage(String idMyMediaInfo,
                                                                                    Long idMyMediaLanguage);
/*    @Transactional
    @Modifying
    void deleteByMyMediaInfoAndMyMediaLanguage(MyMediaInfo mmi, MyMediaLanguage mml);*/

    @Transactional
    @Modifying
    @Query("DELETE FROM MyMediaText " +
            "WHERE id_my_media_info=:idmmi AND id_my_media_language=:idlanguage")
    void deleteOnelink(String idmmi, Long idlanguage);

}
