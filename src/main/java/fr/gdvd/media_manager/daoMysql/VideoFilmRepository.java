package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesNoDb.VideoFilmLightIdScoreYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Tuple;
import java.util.Date;
import java.util.List;

@RepositoryRestResource
public interface VideoFilmRepository extends JpaRepository<VideoFilm, String> {

    @Query("SELECT case when count(vf)>0 THEN true ELSE false end from VideoFilm as vf " +
            "where vf.idVideo = lower(:idtt)")
    boolean ifTtExist(@Param("idtt") String idtt);

    @Query("SELECT distinct vf.idVideo " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm as vf")
    List<String> getAllIds();

    @Query("select distinct vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "  where vf.scoreOnHundred > 60 order by vf.dateModifFilm desc")
    Page<VideoFilm> getLastScore(Pageable p);

    @Query("select distinct vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "  where vf.scoreOnHundred > :valueMin AND vf.dateModifFilm < :newdate " +
            "order by vf.dateModifFilm desc")
    Page<VideoFilm> findVideofilmSubscribeByResMin(Pageable p, int valueMin,
                                                   Date newdate);

    // Get VideoFilm from subscribe

    @Query("select distinct vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "where vf.scoreOnHundred > :valueMin AND vf.scoreOnHundred < :valueMax " +
            "AND vf.dateModifFilm > :dateModifMin order by vf.dateModifFilm desc ")
    List<VideoFilm> findVideofilmDateAfter(int valueMin, int valueMax,
                                         Date dateModifMin);

    @Query("select distinct vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "where vf.scoreOnHundred > :valueMin AND vf.scoreOnHundred < :valueMax " +
            "AND vf.dateModifFilm > :dateModifMin AND vf.dateModifFilm <= :dateModifMax " +
            "order by vf.dateModifFilm desc ")
    List<VideoFilm> findVideofilmDateBetween(int valueMin, int valueMax,
                                             Date dateModifMin, Date dateModifMax);

    @Query("select  vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "Left join fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "on vus.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser as mu " +
            "on mu.idMyUser=vus.myUser " +
            "where vus.dateModifScoreUser > :dateModif and mu.idMyUser =:iduser " +
            "and vus.noteOnHundred > :scoremin and vus.noteOnHundred < :scoremax " +
            "order by vus.dateModifScoreUser desc ")
    List<VideoFilm> findVFwithUserResMinMaxAndDateAfter( Date dateModif, int scoremin,
                                                         int scoremax, Long iduser);

    @Query("select vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "Left join fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "on vus.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser as mu " +
            "on mu.idMyUser=vus.myUser " +
            "where vus.dateModifScoreUser >= :dateMin and " +
            "vus.dateModifScoreUser < :dateMax and mu.idMyUser =:iduser " +
            "and vus.noteOnHundred > :scoremin and vus.noteOnHundred < :scoremax " +
            "order by vus.dateModifScoreUser desc ")
    List<VideoFilm> findVFwithUserResMinMaxAndDateBetween(
            Date dateMin, Date dateMax, int scoremin, int scoremax, Long iduser);

    @Query("select  vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "Left join fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "on vus.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser as mu " +
            "on mu.idMyUser=vus.myUser " +
            "where vus.dateModifScoreUser <= :dateModif " +
            "and mu.idMyUser =:iduser " +
            "and vus.noteOnHundred > :scoremin and vus.noteOnHundred < :scoremax " +
            "order by vus.dateModifScoreUser desc ")
    Page<VideoFilm> findVFwithUserResMinMaxByPage(Pageable p,
            Date dateModif, int scoremin, int scoremax, Long iduser);


    @Query("select distinct vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "Left join fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist as vfa " +
            "on vfa.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoArtist as va " +
            "on va.idVideoArtist = vfa.videoArtist " +
            "where vf.dateModifFilm > :dateModif and va.idVideoArtist =:idname " +
            "and vf.scoreOnHundred > :valueMin and vf.scoreOnHundred <= :valueMax " +
            "order by vf.dateModifFilm desc ")
    List<VideoFilm> findVFwithNameResMinMaxAndDateAfter( Date dateModif, int valueMin,
                                                         int valueMax, String idname);

    @Query("select distinct vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "Left join fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist as vfa " +
            "on vfa.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoArtist as va " +
            "on va.idVideoArtist = vfa.videoArtist " +
            "where vf.dateModifFilm > :dateModifMin AND vf.dateModifFilm <= :dateModifMax " +
            "and va.idVideoArtist =:idname " +
            "and vf.scoreOnHundred > :valueMin and vf.scoreOnHundred <= :valueMax " +
            "order by vf.dateModifFilm desc ")
    List<VideoFilm> findVFwithNameResMinMaxAndDateBetween( Date dateModifMin,
            Date dateModifMax, int valueMin, int valueMax, String idname);


    @Query("select distinct vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "Left join fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist as vfa " +
            "on vfa.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoArtist as va " +
            "on va.idVideoArtist = vfa.videoArtist " +
            "where vf.dateModifFilm <= :dateModif and va.idVideoArtist =:idname " +
            "and vf.scoreOnHundred > :valueMin and vf.scoreOnHundred <= :valueMax " +
            "order by vf.dateModifFilm desc ")
    Page<VideoFilm> findVFwithNameResMinMaxByPage( Pageable p, Date dateModif,
                                                   int valueMin,
                                                   int valueMax, String idname);

    @Query("select distinct vf FROM fr.gdvd.media_manager.entitiesMysql.VideoTitle AS vt " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "ON vt.videoFilm = vf.idVideo "+
            "where vt.title LIKE :atitle " +
            "AND vf.scoreOnHundred >= :scoreMin AND vf.scoreOnHundred <= :scoreMax " +
            "AND vf.year >= :yearMin AND vf.year <= :yearMax")
    Page<VideoFilm> findVideofilmWithTitleScoreAndYear(Pageable p,
             String atitle, int scoreMin, int scoreMax,
             int yearMin, int yearMax);

    @Query("select distinct vf.idVideo, vf.scoreOnHundred, vf.year " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "where vf.idVideo LIKE lower(:idVideo) order by :sortBy ASC")
    List<Tuple> getListIdWithSYAsc(String idVideo, String sortBy);

    @Query("select distinct vf.idVideo " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "where vf.idVideo LIKE lower(:idVideo) order by :sortBy ASC")
    List<String> getListIdWithSYAscV2(String idVideo, String sortBy);

    @Query("select distinct vf.idVideo, vf.scoreOnHundred, vf.year " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "where vf.idVideo LIKE lower(:idVideo) order by :sortBy DESC")
    List<Tuple> getListIdWithSYDesc(String idVideo, String sortBy);

    @Query("select distinct vf.idVideo " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "where vf.idVideo LIKE lower(:idVideo) order by :sortBy DESC")
    List<String> getListIdWithSYDescV2(String idVideo, String sortBy);

    @Query("select distinct vf " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi as tmmi " +
            "ON vf.idVideo = tmmi.videoFilm " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "on tmmi.idTypeMmi = mmi.typeMmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "ON vsp.id_my_media_info = mmi.idMyMediaInfo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport AS vne " +
            "ON vsp.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport AS utne " +
            "ON utne.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser AS mu " +
            "ON utne.myUser = mu.idMyUser " +
            "where mu.login= :login AND " +
            "vf.idVideo = :idvf and " +
            "mu.active=1")
    VideoFilm findVideofilmWithIdvideofilmAndLogin(
            String idvf, String login);

    @Query("select DISTINCT vf.idVideo, " +
            "vf.scoreOnHundred, vf.year, " +
            "mmi.fileSize, mmi.width " +

            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi as tmmi " +
            "ON vf.idVideo = tmmi.videoFilm " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "on tmmi.idTypeMmi = mmi.typeMmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "ON vsp.id_my_media_info = mmi.idMyMediaInfo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport AS vne " +
            "ON vsp.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport AS utne " +
            "ON utne.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser AS mu " +
            "ON utne.myUser = mu.idMyUser " +

            "where vne.idVideoNameExport IN (:idsVNE) AND " +
            "mu.login =:login")
    List<Tuple> findAllIdsWithIdsVNE(List<Long> idsVNE, String login);

    @Query("select DISTINCT vf.idVideo " +

            "FROM VideoFilm AS vf " +
            "LEFT JOIN TypeMmi as tmmi " +
            "ON vf.idVideo = tmmi.videoFilm " +
            "LEFT JOIN MyMediaInfo as mmi " +
            "on tmmi.idTypeMmi = mmi.typeMmi " +
            "LEFT JOIN VideoSupportPath AS vsp " +
            "ON vsp.id_my_media_info = mmi.idMyMediaInfo " +
            "LEFT JOIN VideoNameExport AS vne " +
            "ON vsp.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN UserToNameExport AS utne " +
            "ON utne.id_video_name_export = vne.idVideoNameExport " +
            "LEFT JOIN MyUser AS mu " +
            "ON utne.myUser = mu.idMyUser " +

            "where vne.idVideoNameExport IN (:idsVNE) AND " +
            "mu.login =:login")
    List<String> findAllIdsWithIdsVNEV2(List<Long> idsVNE, String login);


    @Query("select DISTINCT vf.idVideo, " +
            "vf.scoreOnHundred, vf.year, " +
            "mmi.fileSize, mmi.width " +

            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi as tmmi " +
            "ON vf.idVideo = tmmi.videoFilm " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "on tmmi.idTypeMmi = mmi.typeMmi " +

            "where mmi.duration>=:durationMin and mmi.duration<=:durationMax " +
            "and mmi.width>=:widthMin and mmi.width<=:widthMax")
    List<Tuple> findAllIdsWithDurationAndWidthMmi(Double durationMin,Double durationMax,
                                                  int widthMin,int widthMax);

    @Query("select DISTINCT vf.idVideo " +

            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi as tmmi " +
            "ON vf.idVideo = tmmi.videoFilm " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "on tmmi.idTypeMmi = mmi.typeMmi " +

            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "ON vsp.id_my_media_info = mmi.idMyMediaInfo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport AS vne " +
            "ON vsp.id_video_name_export = vne.idVideoNameExport " +

            "where mmi.duration>=:durationMin and mmi.duration<=:durationMax " +
            "and mmi.width>=:widthMin and mmi.width<=:widthMax " +
            "and vsp.active=1 and vne.active=1")
    List<String> findAllIdsWithDurationAndWidthMmiV2(Double durationMin,Double durationMax,
                                                  int widthMin,int widthMax);

   @Query("select DISTINCT vf.idVideo, vf.scoreOnHundred, vf.year " +
           "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
           "INNER JOIN vf.videoKinds vk " +
           "where vk.idKind in (:idKind) ")
   List<Tuple> findAllidvfWithLidkind(List<Long> idKind);

   @Query("select DISTINCT vf.idVideo " +
           "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
           "INNER JOIN vf.videoKinds vk " +
           "where vk.idKind in (:idKind) ")
   List<String> findAllidvfWithLidkindV2(List<Long> idKind);

   @Query("select DISTINCT vf.idVideo, vf.scoreOnHundred, vf.year " +
           "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
           "INNER JOIN vf.videoKinds vk " +
           "where vk.idKind in (:idKind) and vk.idKind not in (:idKindNot)")
   List<Tuple> findAllidvfWithLidkindAndNot(List<Long> idKind, List<Long> idKindNot);

    @Query("select DISTINCT vf.idVideo, vf.scoreOnHundred, vf.year " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "INNER JOIN vf.videoKeywordSet vk " +
            "where vk.keywordEn like :keyword ")
    List<Tuple> findAllidvfWithKeyword(String keyword);

    @Query("select DISTINCT vf.idVideo " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "INNER JOIN vf.videoKeywordSet vk " +
            "where vk.keywordEn like :keyword ")
    List<String> findAllidvfWithKeywordV2(String keyword);

    @Query("select DISTINCT vf.idVideo, vf.scoreOnHundred, vf.year " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "INNER JOIN vf.videoKeywordSet vk " +
            "where vk.idKeyword in (:listkeyword) ")
    List<Tuple> findAllidvfWithListKeyword(List<Long> listkeyword);

    @Query("select DISTINCT vf.idVideo " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "INNER JOIN vf.videoKeywordSet vk " +
            "where vk.idKeyword in (:listkeyword) ")
    List<String> findAllidvfWithListKeywordV2(List<Long> listkeyword);

    @Query("select DISTINCT vf.idVideo, " +
            "vf.scoreOnHundred, vf.year " +

            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi as tmmi " +
            "ON vf.idVideo = tmmi.videoFilm " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "on tmmi.idTypeMmi = mmi.typeMmi " +
            "left join fr.gdvd.media_manager.entitiesMysql.MyMediaAudio as ma " +
            "on mmi.idMyMediaInfo=ma.myMediaInfo " +
            "left join fr.gdvd.media_manager.entitiesMysql.MyMediaLanguage as ml " +
            "on ma.myMediaLanguage=ml.idMyMediaLanguage " +

            "where ml.idMyMediaLanguage in (:lIdLanguages)")
    List<Tuple> findAllIdsWithLanguage(List<Long> lIdLanguages);

    @Query("select DISTINCT vf.idVideo " +

            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi as tmmi " +
            "ON vf.idVideo = tmmi.videoFilm " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "on tmmi.idTypeMmi = mmi.typeMmi " +
            "left join fr.gdvd.media_manager.entitiesMysql.MyMediaAudio as ma " +
            "on mmi.idMyMediaInfo=ma.myMediaInfo " +
            "left join fr.gdvd.media_manager.entitiesMysql.MyMediaLanguage as ml " +
            "on ma.myMediaLanguage=ml.idMyMediaLanguage " +

            "where ml.idMyMediaLanguage in (:lIdLanguages)")
    List<String> findAllIdsWithLanguageV2(List<Long> lIdLanguages);

    @Query("select DISTINCT vf.idVideo, " +
            "vf.scoreOnHundred, vf.year " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "INNER JOIN vf.videoCountries as vc " +
            "where vc.country like :requestCountry")
    List<Tuple> getIdsVFWithCountry(String requestCountry);

    @Query("select DISTINCT vf.idVideo " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "INNER JOIN vf.videoCountries as vc " +
            "where vc.country like :requestCountry")
    List<String> getIdsVFWithCountryV2(String requestCountry);


    @Query("select vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "INNER JOIN vf.videoTitles as vt " +
            "where vt.title like :atitle " +
            "AND (vf.scoreOnHundred >= :scoreMin " +
            "OR vf.scoreOnHundred = :scoreMayNull) " +
            "AND vf.scoreOnHundred <= :scoreMax " +
            "AND (vf.year >= :yearMin " +
            "OR vf.year = :yearMayNull) " +
            "AND vf.year <= :yearMax " +
            "order by :sortBy ASC")
    List<String> findIdVideoByOneTitleWithYearAndScoreAsc(
            String atitle, int scoreMin, int scoreMax,
            int yearMin, int yearMax, int scoreMayNull, int yearMayNull,
            String sortBy);

    @Query("select vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "INNER JOIN vf.videoTitles as vt " +
            "where vt.title like :atitle " +
            "AND (vf.scoreOnHundred >= :scoreMin " +
            "OR vf.scoreOnHundred = :scoreMayNull) " +
            "AND vf.scoreOnHundred <= :scoreMax " +
            "AND (vf.year >= :yearMin " +
            "OR vf.year = :yearMayNull) " +
            "AND vf.year <= :yearMax " +
            "order by :sortBy DESC")
    List<String> findIdVideoByOneTitleWithYearAndScoreDesc(
            String atitle, int scoreMin, int scoreMax,
            int yearMin, int yearMax, int scoreMayNull, int yearMayNull,
            String sortBy);


    @Query("select vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "INNER JOIN vf.videoTitles as vt " +
            "where vt.title like :atitle " +
            "AND (vf.scoreOnHundred >= :scoreMin " +
            "OR vf.scoreOnHundred = :scoreMayNull) " +
            "AND vf.scoreOnHundred <= :scoreMax " +
            "AND (vf.year >= :yearMin " +
            "OR vf.year = :yearMayNull) " +
            "AND vf.year <= :yearMax " +
            "order by vf.year ASC")
    List<String> findIdVideoByOneTitleWithYearAndScoreOrderYearAsc(
            String atitle, int scoreMin, int scoreMax,
            int yearMin, int yearMax, int scoreMayNull,
            int yearMayNull);

    @Query("select vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "INNER JOIN vf.videoTitles as vt " +
            "where vt.title like :atitle " +
            "AND (vf.scoreOnHundred >= :scoreMin " +
            "OR vf.scoreOnHundred = :scoreMayNull) " +
            "AND vf.scoreOnHundred <= :scoreMax " +
            "AND (vf.year >= :yearMin " +
            "OR vf.year = :yearMayNull) " +
            "AND vf.year <= :yearMax " +
            "order by vf.year DESC")
    List<String> findIdVideoByOneTitleWithYearAndScoreOrderYearDesc(
            String atitle, int scoreMin, int scoreMax,
            int yearMin, int yearMax, int scoreMayNull,
            int yearMayNull);

    @Query("select vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "INNER JOIN vf.videoTitles as vt " +
            "where vt.title like :atitle " +
            "AND (vf.scoreOnHundred >= :scoreMin " +
            "OR vf.scoreOnHundred = :scoreMayNull) " +
            "AND vf.scoreOnHundred <= :scoreMax " +
            "AND (vf.year >= :yearMin " +
            "OR vf.year = :yearMayNull) " +
            "AND vf.year <= :yearMax " +
            "order by vf.scoreOnHundred ASC")
    List<String> findIdVideoByOneTitleWithYearAndScoreOrderScoreAsc(
            String atitle, int scoreMin, int scoreMax,
            int yearMin, int yearMax, int scoreMayNull,
            int yearMayNull);

    @Query("select vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "INNER JOIN vf.videoTitles as vt " +
            "where vt.title like :atitle " +
            "AND (vf.scoreOnHundred >= :scoreMin " +
            "OR vf.scoreOnHundred = :scoreMayNull) " +
            "AND vf.scoreOnHundred <= :scoreMax " +
            "AND (vf.year >= :yearMin " +
            "OR vf.year = :yearMayNull) " +
            "AND vf.year <= :yearMax " +
            "order by vf.scoreOnHundred DESC")
    List<String> findIdVideoByOneTitleWithYearAndScoreOrderScoreDesc(
            String atitle, int scoreMin, int scoreMax,
            int yearMin, int yearMax, int scoreMayNull,
            int yearMayNull);

    @Query("select vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "INNER JOIN vf.videoTitles as vt " +
            "where vt.title like :atitle " +
            "AND (vf.scoreOnHundred >= :scoreMin " +
            "OR vf.scoreOnHundred = :scoreMayNull) " +
            "AND vf.scoreOnHundred <= :scoreMax " +
            "AND (vf.year >= :yearMin " +
            "OR vf.year = :yearMayNull) " +
            "AND vf.year <= :yearMax " +
            "order by vf.idVideo ASC")
    List<String> findIdVideoByOneTitleWithYearAndScoreOrderIdAsc(
            String atitle, int scoreMin, int scoreMax,
            int yearMin, int yearMax, int scoreMayNull,
            int yearMayNull);

    @Query("select vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "INNER JOIN vf.videoTitles as vt " +
            "where vt.title like :atitle " +
            "AND (vf.scoreOnHundred >= :scoreMin " +
            "OR vf.scoreOnHundred = :scoreMayNull) " +
            "AND vf.scoreOnHundred <= :scoreMax " +
            "AND (vf.year >= :yearMin " +
            "OR vf.year = :yearMayNull) " +
            "AND vf.year <= :yearMax " +
            "order by vf.idVideo DESC")
    List<String> findIdVideoByOneTitleWithYearAndScoreOrderIdDesc(
            String atitle, int scoreMin, int scoreMax,
            int yearMin, int yearMax, int scoreMayNull,
            int yearMayNull);


    @Query("select vf " +
            "FROM VideoFilm AS vf " +

            "INNER JOIN vf.videoUserScores as vus " +
            "where vus.id.idMyUser = :idPers " +

            "order by vus.noteOnHundred ASC")
    Page<VideoFilm> findAllSortByScoreUserAsc(Pageable p, Long idPers);

    @Query("select vf " +
            "FROM VideoFilm AS vf " +

            "INNER JOIN vf.videoUserScores as vus " +
            "where vus.id.idMyUser = :idPers " +

            "order by vus.noteOnHundred DESC")
    Page<VideoFilm> findAllSortByScoreUserDesc(Pageable p, Long idPers);



    @Query("select vf " +
            "FROM VideoFilm AS vf " +
            "order by vf.idVideo ASC")
    Page<VideoFilm> findAllSortByIdAsc(Pageable p);

    @Query("select vf " +
            "FROM VideoFilm AS vf " +
            "order by vf.idVideo DESC")
    Page<VideoFilm> findAllSortByIdDesc(Pageable p);

    @Query("select vf " +
            "FROM VideoFilm AS vf " +
            "order by vf.year ASC")
    Page<VideoFilm> findAllSortByYearAsc(Pageable p);

    @Query("select vf " +
            "FROM VideoFilm AS vf " +
            "order by vf.year DESC")
    Page<VideoFilm> findAllSortByYearDesc(Pageable p);

    @Query("select vf " +
            "FROM VideoFilm AS vf " +
            "order by vf.scoreOnHundred ASC")
    Page<VideoFilm> findAllSortByScoreAsc(Pageable p);

    @Query("select distinct vf " +
            "FROM VideoFilm AS vf " +
            "order by vf.scoreOnHundred DESC")
    Page<VideoFilm> findAllSortByScoreDesc(Pageable p);

    @Query("select distinct vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "order by vf.scoreOnHundred DESC")
    List<String> findAllSortByScoreDesc2();


    @Query("select distinct vf.idVideo, vus.noteOnHundred " +
            "FROM VideoFilm AS vf " +
            "inner join vf.videoUserScores as vus " +
            "where vus.myUser.idMyUser = :idUser " +
            "and vf.idVideo in (:ids) " +
            "order by vus.noteOnHundred ASC")
    List<Tuple> findAllSortByUserScoreAscWithIds(Long idUser, List<String> ids);

    @Query("select distinct vf.idVideo, vus.noteOnHundred " +
            "FROM VideoFilm AS vf " +
            "inner join vf.videoUserScores as vus " +
            "where vus.myUser.idMyUser = :idUser " +
            "and vf.idVideo in (:ids) " +
            "order by vus.noteOnHundred DESC")
    List<Tuple> findAllSortByUserScoreDescWithIds(Long idUser, List<String> ids);
    /*********************************************/

    @Query("select distinct vf.idVideo, vf.scoreOnHundred " +
            "FROM VideoFilm AS vf " +
            "where vf.idVideo in (:ids) " +
            "order by vf.scoreOnHundred ASC")
    List<Tuple> findAllSortByScoreAscWithList(List<String> ids);

    @Query("select distinct vf.idVideo, vf.scoreOnHundred " +
            "FROM VideoFilm AS vf " +
            "where vf.idVideo in (:ids) " +
            "order by vf.scoreOnHundred DESC")
    List<Tuple> findAllSortByScoreDescWithList(List<String> ids);


    @Query("select distinct vf.idVideo, vf.year " +
            "FROM VideoFilm AS vf " +
            "where vf.idVideo in (:ids) " +
            "order by vf.year ASC")
    List<Tuple> findAllSortByYearAscWithList(List<String> ids);

    @Query("select distinct vf.idVideo, vf.year " +
            "FROM VideoFilm AS vf " +
            "where vf.idVideo in (:ids) " +
            "order by vf.year DESC")
    List<Tuple> findAllSortByYearDescWithList(List<String> ids);

    @Query("select distinct vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "where vf.idVideo in (:ids) " +
            "order by vf.idVideo ASC")
    List<Tuple> findAllSortByIddbAscWithList(List<String> ids);

    @Query("select distinct vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "where vf.idVideo in (:ids) " +
            "order by vf.idVideo DESC")
    List<Tuple> findAllSortByIddbDescWithList(List<String> ids);

/* Page<VideoFilm>
getPageVideoFilmsSortByIddb
(Pageable p, boolean asc)*/

    @Query("select distinct vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "order by vf.idVideo DESC")
    List<Tuple> findAllSortByIddbDesc();

    @Query("select distinct vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "order by vf.idVideo ASC")
    List<Tuple> findAllSortByIddbAsc();

    @Query("select distinct vf " +
            "FROM VideoFilm AS vf " +
            "where vf.idVideo in (:lids) " +
            "order by vf.idVideo ASC")
    List<VideoFilm> findwithIdAndSortByIddbAsc(List<String> lids);

    @Query("select distinct vf " +
            "FROM VideoFilm AS vf " +
            "where vf.idVideo in (:lids) " +
            "order by vf.idVideo DESC")
    List<VideoFilm> findwithIdAndSortByIddbDesc(List<String> lids);

/* Page<VideoFilm>
getPageVideoFilmsSortByYear
(Pageable p, boolean asc)*/

    @Query("select distinct vf.idVideo, vf.year " +
            "FROM VideoFilm AS vf " +
            "order by vf.year DESC")
    List<Tuple> findAllSortByYearDesc();

    @Query("select distinct vf.idVideo, vf.year " +
            "FROM VideoFilm AS vf " +
            "order by vf.year ASC")
    List<Tuple> findAllSortByYearAsc();

    @Query("select distinct vf " +
            "FROM VideoFilm AS vf " +
            "where vf.idVideo in (:lids) " +
            "order by vf.year ASC")
    List<VideoFilm> findwithIdAndSortByYearAsc(List<String> lids);

    @Query("select distinct vf " +
            "FROM VideoFilm AS vf " +
            "where vf.idVideo in (:lids) " +
            "order by vf.year DESC")
    List<VideoFilm> findwithIdAndSortByYearDesc(List<String> lids);

/* Page<VideoFilm>
getPageVideoFilmsSortByScoreWithIdsVfTuple
(Pageable p,  boolean asc)*/

    @Query("select distinct vf.idVideo, vf.scoreOnHundred " +
            "FROM VideoFilm AS vf " +
            "order by vf.scoreOnHundred ASC")
    List<Tuple> findAllSortByScoreAsc3();

    @Query("select distinct vf.idVideo, vf.scoreOnHundred " +
            "FROM VideoFilm AS vf " +
            "order by vf.scoreOnHundred DESC")
    List<Tuple> findAllSortByScoreDesc3();

    @Query("select distinct vf " +
            "FROM VideoFilm AS vf " +
            "where vf.idVideo in (:lids) " +
            "order by vf.scoreOnHundred ASC")
    List<VideoFilm> findwithIdAndSortByScoreAsc(List<String> lids);

    @Query("select distinct vf " +
            "FROM VideoFilm AS vf " +
            "where vf.idVideo in (:lids) " +
            "order by vf.scoreOnHundred DESC")
    List<VideoFilm> findwithIdAndSortByScoreDesc(List<String> lids);

/* Page<VideoFilm>
getPageVideoFilmsSortByUserScoreWithIdsVfTuple
(Pageable p,  boolean asc)*/
    @Query("select distinct vf.idVideo, vus.noteOnHundred " +
            "FROM VideoFilm AS vf " +
            "inner join vf.videoUserScores as vus " +
            "where vus.myUser.idMyUser = :idUser " +
            "order by vus.noteOnHundred ASC")
    List<Tuple> findAllSortByUserScoreAsc(Long idUser);

    @Query("select distinct vf.idVideo, vus.noteOnHundred " +
            "FROM VideoFilm AS vf " +
            "inner join vf.videoUserScores as vus " +
            "where vus.myUser.idMyUser = :idUser " +
            "order by vus.noteOnHundred DESC")
    List<Tuple> findAllSortByUserScoreDesc(Long idUser);

    @Query("select distinct vf " +
            "FROM VideoFilm AS vf " +
            "inner join vf.videoUserScores as vus " +
            "where vus.myUser.idMyUser = :idUser " +
            "and vf.idVideo in (:lids) ")
    List<VideoFilm> findwithIdAndUserSortByScoreAsc(List<String> lids, Long idUser);

    @Query("select distinct vf " +
            "FROM VideoFilm AS vf " +
            "inner join vf.videoUserScores as vus " +
            "where vus.myUser.idMyUser = :idUser " +
            "and vf.idVideo in (:lids) ")
    List<VideoFilm> findwithIdAndUserSortByScoreDesc(List<String> lids, Long idUser);

    // Find on user score
@Query("select distinct vf.idVideo " +
        "FROM VideoFilm AS vf " +
        "inner join vf.videoUserScores as vus " +
        "where vus.myUser.idMyUser = :idUser " +
        "and vf.idVideo in (:lids) " +
        "and vus.noteOnHundred >= :scoreMin " +
        "and vus.noteOnHundred <= :scoreMax ")
List<Tuple> findAlltByUserScoreNotNull(Long idUser, List<String> lids,
                                       int scoreMin, int scoreMax);

    @Query("select distinct vf.idVideo " +
            "FROM VideoFilm AS vf " +
            "inner join vf.videoUserScores as vus " +
            "where vus.myUser.idMyUser = :idUser " +
            "and vf.idVideo in (:lids) " +
            "and (vus.noteOnHundred < :scoreMin " +
            "or vus.noteOnHundred > :scoreMax) ")
List<Tuple> findAlltByUserScoreNotNullAndNotInRange(Long idUser, List<String> lids,
                                                    int scoreMin, int scoreMax);


    @Query("select  vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "Left join fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "on vus.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser as mu " +
            "on mu.idMyUser=vus.myUser " +
            "where vus.dateModifScoreUser <= :dateModif " +
            "and mu.idMyUser =:iduser " +
            "order by vus.dateModifScoreUser desc ")
    List<VideoFilm> findVFwithUserOrderByDateModifScoreUser(Long iduser, Date dateModif,
                                                            Pageable pageable);

    @Query("select vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "Left join fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "on vus.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser as mu " +
            "on mu.idMyUser=vus.myUser " +
            "where vus.dateModifScoreUser < :dateMax and mu.idMyUser =:iduser " +
            "order by vus.dateModifScoreUser desc ")
    List<VideoFilm> findVFwithUserOrderByDateModifScoreUser0(Long iduser,
            Date dateMax,  Pageable pageable);
}
