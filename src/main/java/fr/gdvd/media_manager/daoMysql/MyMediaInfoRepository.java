package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesNoDb.MediaInfoLight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface MyMediaInfoRepository extends JpaRepository<MyMediaInfo, String> {

    MyMediaInfo findByIdMyMediaInfo(String idMyMediaInfo);

//    List<MyMediaInfo> findAll(Pageable pageable);

    /*@Query("select mmi from MyMediaInfo mmi JOIN VideoSupportPath ON mmi.videoSupportPaths=videoSupportPath WHERE videoSupportPaths.type = ?1")
    List<MyMediaInfo> getObject(String type);*/
    // JOIN VideoSupportPath vsp ON mmi.idMyMediaInfo=vsp.idMyMediaInfo

    @Query("SELECT mmi.idMyMediaInfo, mmi.codecId " +
            "FROM MyMediaInfo as mmi JOIN MyMediaComment mmc ON mmc=mmi.idMyMediaInfo")
    List<Object> getObject();

    //########################################################################################
    //############################# Test requete #############################################
    //########################################################################################
    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND mu.active=1 AND utne.active=1 AND vne.complete=1 " +
            "and vne.active=1 AND vsp.title LIKE :filter")
    Page<MyMediaInfo> findMmiPP(String login, String filter, Pageable pageable);

}
