package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesNoDb.MediaInfoLight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Tuple;
import java.util.List;

@RepositoryRestResource
public interface MyMediaInfoRepository extends JpaRepository<MyMediaInfo, String> {

    MyMediaInfo findByIdMyMediaInfo(String idMyMediaInfo);

    @Query("SELECT mmi.idMyMediaInfo, mmi.codecId " +
            "FROM MyMediaInfo as mmi JOIN MyMediaComment mmc ON mmc=mmi.idMyMediaInfo")
    List<Object> getObject();


    @Query("SELECT DISTINCT mmi " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoTitle AS vt " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vt.videoFilm = vf.idVideo " +
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
            "where mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.complete=1 and vne.active=1 AND (vsp.title LIKE :filter OR vt.title LIKE :filter)")
    Page<MyMediaInfo> findMmiPPAndTitleVf(String login, String filter, Pageable pageable);

    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where (mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.complete=1 and vne.active=1) AND (vsp.title LIKE :filter OR mmi = " +

            "ALL( select distinct mmi FROM fr.gdvd.media_manager.entitiesMysql.VideoTitle AS vt " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vt.videoFilm = vf.idVideo " +
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
            "where mu.login= :login AND vt.title LIKE :filter ) )")
    Page<MyMediaInfo> findMmiPPAndTitleVfV2(String login, String filter, Pageable pageable);

    // OK
    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.complete=1 and vne.active=1 AND vsp.title LIKE :filter")
    Page<MyMediaInfo> findMmiPP(String login, String filter, Pageable pageable);

    //OK
    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where vsp.active=1 AND vne = " +

            "all(select vne from fr.gdvd.media_manager.entitiesMysql.VideoNameExport as vne " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.complete=1 AND vne.idVideoNameExport = :idVne " +
            "and vne.active=1)")
    Page<MyMediaInfo> findMmiWithFilterVNEPP(String login, Long idVne, Pageable pageable);

    // OK
    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where vsp.active=1 AND vsp.title LIKE :filter AND vne = " +

            "all(select vne from fr.gdvd.media_manager.entitiesMysql.VideoNameExport as vne " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.complete=1 AND vne.idVideoNameExport = :idVne " +
            "and vne.active=1)")
    Page<MyMediaInfo> findMmiWithFilterVNEPPV2(String login, Long idVne, String filter, Pageable pageable);

    // Didn't work
    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where vsp.active=1 AND vne = " +

            "all(select vne from fr.gdvd.media_manager.entitiesMysql.VideoNameExport as vne " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.complete=1  and vne.active=1)")
    Page<MyMediaInfo> findMmiPPV2(String login, Pageable pageable);

/*    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.complete=1 AND vne.idVideoNameExport = :idVne " +
            " and vne.active=1")
    Page<MyMediaInfo> findMmiWithFilterVNEPPV2(String login, Long idVne, Pageable pageable);*/

    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where (mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.complete=1 and vne.active=1 AND vne.idVideoNameExport = :idVne) " +
            "AND (vsp.title LIKE :filter OR mmi = " +

            "ALL( select distinct mmi FROM fr.gdvd.media_manager.entitiesMysql.VideoTitle AS vt " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vt.videoFilm = vf.idVideo " +
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
            "where mu.login= :login AND vne.idVideoNameExport = :idVne AND vt.title LIKE :filter ) )")
    Page<MyMediaInfo> findMmiWithFilterVNEPPAndTitleVf(String login, String filter,
                                             Long idVne, Pageable pageable);

    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where (mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.active=1 AND vne.idVideoNameExport = :idVne) " +
            "AND (vsp.title LIKE :filter OR mmi = " +

            "ALL( select distinct mmi FROM fr.gdvd.media_manager.entitiesMysql.VideoTitle AS vt " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vt.videoFilm = vf.idVideo " +
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
            "where mu.login= :login AND vne.idVideoNameExport = :idVne AND vt.title LIKE :filter ) )")
    Page<MyMediaInfo> findMmiWithFilterVNEPPAndTitleVfV2(String login, String filter,
                                             Long idVne, Pageable pageable);

    // Extremely slow, and incorrect result
    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where (mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "AND vsp.title LIKE :filter) OR (mmi = " +

            "ALL( select distinct mmi FROM fr.gdvd.media_manager.entitiesMysql.VideoTitle AS vt " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vt.videoFilm = vf.idVideo " +
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
            "where mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "AND vt.title LIKE :filter ) )")
    Page<MyMediaInfo> findMmiWithFilterPPAndTitleVfV4(String login, String filter, Pageable pageable);

    // Works for titleVideoFilm ////////////////////////////////////////////////////////////////////////////////////////
    @Query("select distinct mmi FROM fr.gdvd.media_manager.entitiesMysql.VideoTitle AS vt " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vt.videoFilm = vf.idVideo " +
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
            "where mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.active=1 AND vt.title LIKE :filter")
    Page<MyMediaInfo> findMmiPPAndTitleVfV5(String login, String filter, Pageable pageable);

    //
    @Query("select distinct mmi FROM fr.gdvd.media_manager.entitiesMysql.VideoTitle AS vt " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vt.videoFilm = vf.idVideo " +
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
            "where vf is not null and mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.active=1 AND vt.title LIKE :filter ")
    Page<MyMediaInfo> findMmiPPAndTitleVfV6(String login, String filter, Pageable pageable);

    //
    @Query("select distinct mmi FROM fr.gdvd.media_manager.entitiesMysql.VideoTitle AS vt " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vt.videoFilm = vf.idVideo " +
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
            "where vf is not null and mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.active=1 and vne.idVideoNameExport = :idVne AND vt.title LIKE :filter")
    Page<MyMediaInfo> findMmiPPWithFilterVNEAndTitleVf(String login, String filter, Long idVne, Pageable pageable);

// Work with videoTitle only
    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "left join fr.gdvd.media_manager.entitiesMysql.TypeMmi AS tmmi " +
            "on mmi.typeMmi = tmmi.idTypeMmi " +
            "left join fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "on tmmi.videoFilm = vf.idVideo " +
            "left join fr.gdvd.media_manager.entitiesMysql.VideoTitle AS vt " +
            "on vt.videoFilm = vf.idVideo " +
            "where vt.title LIKE :filter")
    Page<MyMediaInfo> findMmiWithFilterPPAndTitleVfV3(String filter, Pageable pageable);

// Didn't work
    @Query("select distinct mmi FROM fr.gdvd.media_manager.entitiesMysql.VideoTitle AS vt " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vf.idVideo = vt.videoFilm " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi AS tmmi " +
            "ON tmmi.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo AS mmi " +
            "ON mmi.typeMmi = tmmi.idTypeMmi " +
            "where vt.title LIKE :filter")
    Page<MyMediaInfo> findMmiWithFilterPPAndTitleVfV4(String filter, Pageable pageable);

/*
    @Query("SELECT DISTINCT mmi " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
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
            "WHERE vne.active=1 AND (vsp.title LIKE :filter) AND mu.login = 'admin'")
    Page<MyMediaInfo> findMmiOrTitlePP(String filter, Pageable pageable);*/

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
            "WHERE vne.active=1 AND va.idVideoArtist = :nm AND mu.login = 'admin'")
    Page<MyMediaInfo> findMmiWithNMamePP(String nm, Pageable pageable);


/*
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
    Page<MyMediaInfo> findMmiTtNullPP(String nm, Pageable pageable);*/


/*    @Query("SELECT DISTINCT mmi " +
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
            "WHERE va.idVideoArtist = :nm AND mu.active=1 AND utne.active=1 " +
            "AND vne.complete=1 AND mu.login = :login")
    Page<MyMediaInfo> findMmiWithNMAndLoginPP(String nm, String login, Pageable pageable);*/

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
            "WHERE va.firstLastName LIKE :nm AND mu.active=1 AND utne.active=1 " +
            "AND vne.complete=1 AND vne.active=1  AND mu.login = :login")
    Page<MyMediaInfo> findMmiLIKEfirstLastNamePP(String login, String nm,  Pageable pageable);

    @Query("SELECT typeMmi.idTypeMmi FROM fr.gdvd.media_manager.entitiesMysql.MyMediaInfo " +
            "WHERE idMyMediaInfo=:idmmi")
    Long findFkTypeMmiWithIdmmi(String idmmi);


}
