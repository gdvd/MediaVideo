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

    @Query("SELECT mmi.idMyMediaInfo, mmi.codecId " +
            "FROM MyMediaInfo as mmi JOIN MyMediaComment mmc ON mmc=mmi.idMyMediaInfo")
    List<Object> getObject();


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


    @Query("SELECT DISTINCT mmi " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoArtist AS va " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist AS vfa " +
            "ON vfa.videoArtist = va.idVideoArtist " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vfa.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi AS tmmi " +
            "ON tmmi.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo AS mmi " +
            "ON mmi.typeMmi = tmmi.idTypeMmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "ON vsp.id_my_media_info = mmi.idMyMediaInfo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport AS vne " +
            "ON vsp.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport AS utne " +
            "ON utne.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser AS mu " +
            "ON utne.myUser = mu.idMyUser " +
            "WHERE va.idVideoArtist = :nm AND mu.login = 'admin'")
    Page<MyMediaInfo> findMmiWithNMamePP(String nm, Pageable pageable);



    @Query("SELECT DISTINCT mmi " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoArtist AS va " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist AS vfa " +
            "ON vfa.videoArtist = va.idVideoArtist " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vfa.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi AS tmmi " +
            "ON tmmi.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo AS mmi " +
            "ON mmi.typeMmi = tmmi.idTypeMmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "ON vsp.id_my_media_info = mmi.idMyMediaInfo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport AS vne " +
            "ON vsp.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport AS utne " +
            "ON utne.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser AS mu " +
            "ON utne.myUser = mu.idMyUser " +
            "WHERE va.idVideoArtist = :nm AND mu.login = 'admin'")
    Page<MyMediaInfo> findMmiTtNullPP(String nm, Pageable pageable);


    @Query("SELECT DISTINCT mmi " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoArtist AS va " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist AS vfa " +
            "ON vfa.videoArtist = va.idVideoArtist " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vfa.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi AS tmmi " +
            "ON tmmi.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo AS mmi " +
            "ON mmi.typeMmi = tmmi.idTypeMmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "ON vsp.id_my_media_info = mmi.idMyMediaInfo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport AS vne " +
            "ON vsp.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport AS utne " +
            "ON utne.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser AS mu " +
            "ON utne.myUser = mu.idMyUser " +
            "WHERE va.idVideoArtist = :nm AND mu.active=1 AND utne.active=1 AND vne.complete=1 " +
            "AND vne.active=1 AND mu.login = :login")
    Page<MyMediaInfo> findMmiWithNMAndLoginPP(String nm, String login, Pageable pageable);


    @Query("SELECT DISTINCT mmi " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoArtist AS va " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist AS vfa " +
            "ON vfa.videoArtist = va.idVideoArtist " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vfa.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi AS tmmi " +
            "ON tmmi.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo AS mmi " +
            "ON mmi.typeMmi = tmmi.idTypeMmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "ON vsp.id_my_media_info = mmi.idMyMediaInfo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport AS vne " +
            "ON vsp.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport AS utne " +
            "ON utne.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser AS mu " +
            "ON utne.myUser = mu.idMyUser " +
            "WHERE va.firstLastName LIKE :nm AND mu.active=1 AND utne.active=1 AND vne.complete=1 " +
            "AND vne.active=1 AND mu.login = :login")
    Page<MyMediaInfo> findMmiLIKEfirstLastNamePP(String login, String nm,  Pageable pageable);

}
