package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyVideoSupportPath;
import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.VideoNameExport;
import fr.gdvd.media_manager.entitiesMysql.VideoSupportPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.List;

@RepositoryRestResource
public interface VideoSupportPathRepository extends JpaRepository<VideoSupportPath, EmbeddedKeyVideoSupportPath> {

    List<VideoSupportPath> findAllByMyMediaInfo_IdMyMediaInfoAndVideoNameExport_NameExport(String idMyMediaInfo, String nameExport);
    List<VideoSupportPath> findAllByMyMediaInfoAndVideoNameExport(MyMediaInfo myMediaInfo, VideoNameExport videoNameExport);
    List<VideoSupportPath> findAllByMyMediaInfo_IdMyMediaInfoAndVideoNameExport_IdVideoNameExport(String idMyMediaInfo, Long idVideoNameExport);
    VideoSupportPath findByPathGeneralAndMyMediaInfoAndVideoNameExport(String pathgeneral, MyMediaInfo myMediaInfo, VideoNameExport videoNameExport);
    VideoSupportPath findByTitleAndPathGeneralAndMyMediaInfo_IdMyMediaInfoAndVideoNameExport_IdVideoNameExport(String title, String pathgeneral, String idMyMediaInfo, Long idVideoNameExport);
    VideoSupportPath findByTitleAndPathGeneralAndVideoNameExport_IdVideoNameExport(String title, String pathGeneral, Long idVideoNameExport);
    List<VideoSupportPath> findAllByVideoNameExport_NameExport(String nameExport);
    List<VideoSupportPath> findAllByPathGeneralContains(String pathGeneral);
//    Page<VideoSupportPath> findAllByPathGeneralContainsAndActive(String pathGeneral, boolean active);

    @Query("SELECT vsp FROM fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND mu.active=1 AND utne.active=1 AND vne.complete=1 " +
            "and vne.active=1")
    List<VideoSupportPath> findMyVsp(String login);

    @Query("SELECT vsp FROM fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND mu.active=1 AND utne.active=1 AND vne.complete=1 " +
            "and vne.active=1 AND vsp.title LIKE :filter")
    Page<VideoSupportPath> findMyVspPP(String login, String filter, Pageable pageable);

    @Query("SELECT vsp FROM fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "AND vne.complete=1 and vne.active=1 AND vsp.pathGeneral LIKE :filter")
    List<VideoSupportPath> findMyVspWithFilter(String login, String filter);

    @Transactional
    @Modifying
    @Query("update fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "SET vsp.active=0 WHERE vsp.id_video_name_export = :id")
    void deactivatesWithIdVne(Long id);

    @Transactional
    @Modifying
    @Query("delete from fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "WHERE vsp.id_video_name_export = :id")
    void deleteWithIdVne(Long id);


    @Query("select title from fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "where id_my_media_info=:idmd5")
    List<String> getTitleswhereidmd5(String idmd5);

    @Query("select vsp.title, vsp.pathGeneral, vne.nameExport " +
            "from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where mmi.idMyMediaInfo=:idMmi")
    List<Tuple> findTitlePathAndVneWithidMmi(String idMmi);

    @Query("select vsp.active from fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "where vsp.id_my_media_info=:idMmi")
    List<Boolean> isVspActiveOrNot(String idMmi);

    @Transactional
    @Modifying
    @Query("update fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "set vsp.active=:active where vsp.id_my_media_info=:idMmi")
    void toggleactiveidMmi(String idMmi, boolean active);

}

