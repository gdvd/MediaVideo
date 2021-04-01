package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.TypeMmi;
import fr.gdvd.media_manager.entitiesNoDb.MediaInfoLight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
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
            "and vne.complete=1 and vne.active=1 AND (vsp.title LIKE :filter " +
            "OR vt.title LIKE :filter OR tmmi.nameSerieVO LIKE :filter OR tmmi.nameSerie LIKE :filter)")
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
            "and vne.complete=1 and vne.active=1 and vsp.active=1 AND vsp.title LIKE :filter")
    Page<MyMediaInfo> findMmiPPFilterFilename(String login, String filter, Pageable pageable);

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
            "and vne.active=1 AND (vt.title LIKE :filter " +
            " OR tmmi.nameSerieVO LIKE :filter OR tmmi.nameSerie LIKE :filter)")
    Page<MyMediaInfo> findMmiPPAndTitleVfV6(String login, String filter, Pageable pageable);

    //
    @Query("select distinct mmi " +
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
            "where vf is not null and mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.active=1 AND vf.idVideo LIKE :filtertt ")
    Page<MyMediaInfo> findMmiPPAndTitleTT(String login, String filtertt, Pageable pageable);

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
            "and vne.active=1 and vne.idVideoNameExport = :idVne AND " +
            "(vt.title LIKE :filter OR tmmi.nameSerieVO LIKE :filter OR tmmi.nameSerie LIKE :filter)")
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

    @Query("Select mmi.idMyMediaInfo FROM fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi ")
    List<String> getAllIdMmi();

    @Query("Select mmi.idMyMediaInfo FROM fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "where mmi.duration =:duration AND mmi.fileSize =:filesize AND mmi.width =:width " +
            "AND mmi.height =:height AND mmi.bitrate =:bitrate AND mmi.codecId =:codecId")
    List<String> findMmiWithFeatures(Double duration, Double filesize, int width, int height,
                                     Double bitrate, String codecId);

    @Query("Select mmi FROM fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "where mmi.typeMmi.idTypeMmi in (:ltmmi)")
    List<MyMediaInfo> findAllByAllIdTypeMyMediaInfo(List<Long> ltmmi);

    @Query("Select mmi FROM fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "where mmi.typeMmi in (:ltmmi)")
    List<MyMediaInfo> findAllByAllTypeMyMediaInfo(List<TypeMmi> ltmmi);

    //?
    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where vsp.active=1 and mmi.typeMmi.idTypeMmi in (:ltmmi) AND vne = " +

            "all(select vne from fr.gdvd.media_manager.entitiesMysql.VideoNameExport as vne " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.complete=1 and vne.active=1)")
    List<MyMediaInfo> findAllByAllTypeMyMediaInfoAndUser(List<Long> ltmmi, String login);

    //Didn't works
    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where vsp.active=1 and mmi.typeMmi.idTypeMmi in (:ltmmi) AND vne = " +

            "all(select vne from fr.gdvd.media_manager.entitiesMysql.VideoNameExport as vne " +
            "where vne.idVideoNameExport in (:idsVideoNameExport) )")
    List<MyMediaInfo> findAllByAllTypeMyMediaInfoAndUserLight(List<Long> ltmmi
            , List<Long> idsVideoNameExport);

    //Works
    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where vsp.active=1 and mmi.typeMmi.idTypeMmi in (:ltmmi) AND vne = " +

            "all(select vne from fr.gdvd.media_manager.entitiesMysql.VideoNameExport as vne " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.complete=1 AND vne.idVideoNameExport = :idVne " +
            "and vne.active=1)")
    List<MyMediaInfo> findAllByAllTypeMyMediaInfoAndUserLight2(List<Long> ltmmi,
                 String login, Long idVne);

    //Didn't works
    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where vsp.active=1 and mmi.typeMmi.idTypeMmi in (:ltmmi) AND vne = " +

            "all(select vne from fr.gdvd.media_manager.entitiesMysql.VideoNameExport as vne " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where vne.idVideoNameExport IN (:idsVne) AND mu.active=1 AND utne.active=1 " +
            "and vne.complete=1 and vne.active=1 AND mu.login= :login )")
    List<MyMediaInfo> findAllByAllTypeMyMediaInfoAndUserLight3(String login,
                                                                 List<Long> idsVne,  List<Long> ltmmi);

    //Works
    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where vsp.active=1 and mmi.typeMmi.idTypeMmi in (:ltmmi) AND vne = " +

            "all(select vne from fr.gdvd.media_manager.entitiesMysql.VideoNameExport as vne " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND mu.active=1 AND utne.active=1 " +
            "and vne.complete=1 AND vne.nameExport = '%' " +
            "and vne.active=1)")
    List<MyMediaInfo> findAllByAllTypeMyMediaInfoAndUserLight4(String login , List<Long> ltmmi);

    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where vsp.active=1 and mmi.typeMmi.idTypeMmi in (:ltmmi) AND " +
            "vne.nameExport IN (:lnamevne)")
    List<MyMediaInfo> findAllByAllTypeMyMediaInfoAndUserLight5(List<Long> ltmmi,
                                                               List<String> lnamevne);

    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where vsp.active=1 and mmi.typeMmi.idTypeMmi in (:ltmmi) AND " +
            "vne.idVideoNameExport IN (:lidsvideonameexport)")
    List<MyMediaInfo> findAllByAllTypeMyMediaInfoAndUserLight6(List<Long> ltmmi,
                                                               List<Long> lidsvideonameexport);

    @Query("select distinct mmi from fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where vsp.active=1 and mmi.typeMmi.idTypeMmi in (:ltmmi) AND " +
            "vne.idVideoNameExport IN (:lidsvideonameexport) AND " +
            "mmi.duration>=:durationMin AND mmi.duration<=:durationMax AND " +
            "mmi.width>=:widthMin AND mmi.width<=:widthMax")
    List<MyMediaInfo> findAllByAllTypeMyMediaInfoAndUserLight6AndDurationWidth(
            List<Long> ltmmi, List<Long> lidsvideonameexport, Double durationMin,
            Double durationMax, int widthMin, int widthMax);

    @Query("select distinct mmi " +
            "from MyMediaLanguage as ml " +
            "left join MyMediaAudio as ma on ml.idMyMediaLanguage=ma.myMediaLanguage " +
            "left join MyMediaInfo as mmi on ma.myMediaInfo=mmi.idMyMediaInfo " +
            "left join VideoSupportPath as vsp on mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN VideoNameExport as vne on vsp.id_video_name_export=vne.idVideoNameExport " +

            "where ml.idMyMediaLanguage in (:lIdLanguages) and " +
            "vsp.active=1 and " +
            "mmi.typeMmi.idTypeMmi in (:ltmmi) AND " +
            "vne.idVideoNameExport IN (:lidsvideonameexport)")
    List<MyMediaInfo> findAllByAllTypeMyMediaInfoAndUserLight6AndDurationWidthAndLanguage(
            List<Long> ltmmi, List<Long> lidsvideonameexport, List<Long> lIdLanguages);


    @Query("select count(mmi) " +
            "from MyMediaInfo as mmi " +
            "left join fr.gdvd.media_manager.entitiesMysql.MyMediaAudio as ma " +
            "on mmi.idMyMediaInfo=ma.myMediaInfo " +
            "left join fr.gdvd.media_manager.entitiesMysql.MyMediaLanguage as ml " +
            "on ma.myMediaLanguage=ml.idMyMediaLanguage " +
            "where ml.language like :language")
    Long getValue(String language);

    @Query("select distinct mmi " +
            "from MyMediaInfo as mmi " +
            "where mmi.typeMmi in(:ltm)")
    List<MyMediaInfo> findAllByTypeMmi(List<TypeMmi> ltm);

}
