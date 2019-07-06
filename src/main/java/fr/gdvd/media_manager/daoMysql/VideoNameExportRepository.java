package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyVideoSupportPath;
import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.VideoNameExport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface VideoNameExportRepository extends JpaRepository<VideoNameExport, EmbeddedKeyVideoSupportPath> {

    VideoNameExport findByNameExport(String nameExport);
    VideoNameExport findByIdVideoNameExport(Long idVideoNameExport);
    List<VideoNameExport> findAll();
    List<VideoNameExport> findAllByActiveIsTrueAndCompleteIsTrue();
    List<VideoNameExport> findAllByActive(boolean active);
    List<VideoNameExport> findAllByActiveAndComplete(boolean active, boolean complete);

    @Query("SELECT vne FROM fr.gdvd.media_manager.entitiesMysql.VideoNameExport AS vne " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= ?1 AND mu.active=1 AND utne.active=1 AND vne.complete=1 " +
            "and vne.active=1 order by vne.nameExport asc")
    List<VideoNameExport> findMyVne(String login);

    /*

    @Query("SELECT vsp FROM fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= ?1 AND utne.active=1 AND vne.complete=1 and vne.active=1")
    List<VideoSupportPath> findMyVsp(String login);

    */
}
