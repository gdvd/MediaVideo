package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyVideoSupportPath;
import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.VideoNameExport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;

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

    @Query("SELECT vne.idVideoNameExport, vne.nameExport FROM fr.gdvd.media_manager.entitiesMysql.VideoNameExport AS vne "+
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "as utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON utne.myUser=mu.idMyUser " +
            "where mu.login= ?1 AND mu.active=1 AND utne.active=1 AND vne.complete=1 " +
            "and vne.active=1")
    List<Tuple> lVneIdToName(String login);
//    List<Map<Long, String>> lVneIdToName(String login);
//    List<Map<Long, String>> lVneIdToName();


    /*@Query("SELECT vsp FROM fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= ?1 AND utne.active=1 AND vne.complete=1 and vne.active=1")
    List<VideoSupportPath> findMyVsp(String login);*/

    /*
    SELECT `video_name_export`.`name_export`,`video_name_export`.`id_video_name_export`,
    `user_to_name_export`.`active`, `my_user`.`login`
        FROM `video_name_export`

	LEFT JOIN `user_to_name_export` ON `user_to_name_export`.`id_video_name_export` = `video_name_export`.`id_video_name_export`
	LEFT JOIN `my_user` ON `user_to_name_export`.`id_my_user` = `my_user`.`id_my_user`;
    */
}
