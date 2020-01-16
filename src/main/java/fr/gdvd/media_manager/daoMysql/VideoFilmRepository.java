package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
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

    @Query("SELECT case when count(vf)>0 THEN true ELSE false end from VideoFilm as vf where vf.idVideo = lower(:idtt)")
    boolean ifTtExist(@Param("idtt") String idtt);

    @Query("SELECT vf.idVideo " +
            "FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm as vf")
    List<String> getAllIds();

    @Query("select vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "  where vf.scoreOnHundred > 60 order by vf.dateModifFilm desc")
    Page<VideoFilm> getLastScore(Pageable p);

    @Query("select vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "  where vf.scoreOnHundred > :valueMin AND vf.dateModifFilm < :newdate order by vf.dateModifFilm desc")
    Page<VideoFilm> findVideofilmSubscribeByResMin(Pageable p, int valueMin, Date newdate);

    // Get VideoFilm from subscribe

    @Query("select vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "where vf.scoreOnHundred > :valueMin AND vf.scoreOnHundred < :valueMax " +
            "AND vf.dateModifFilm > :dateModifMin order by vf.dateModifFilm desc ")
    List<VideoFilm> findVideofilmDateAfter(int valueMin, int valueMax,
                                         Date dateModifMin);

    @Query("select vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "where vf.scoreOnHundred > :valueMin AND vf.scoreOnHundred < :valueMax " +
            "AND vf.dateModifFilm > :dateModifMin AND vf.dateModifFilm <= :dateModifMax " +
            "order by vf.dateModifFilm desc ")
    List<VideoFilm> findVideofilmDateBetween(int valueMin, int valueMax,
                                             Date dateModifMin, Date dateModifMax);

    @Query("select vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
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

    @Query("select vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
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


    @Query("select vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "Left join fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist as vfa " +
            "on vfa.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoArtist as va " +
            "on va.idVideoArtist = vfa.videoArtist " +
            "where vf.dateModifFilm > :dateModif and va.idVideoArtist =:idname " +
            "and vf.scoreOnHundred > :valueMin and vf.scoreOnHundred <= :valueMax " +
            "order by vf.dateModifFilm desc ")
    List<VideoFilm> findVFwithNameResMinMaxAndDateAfter( Date dateModif, int valueMin,
                                                         int valueMax, String idname);

    @Query("select vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
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


    @Query("select vf from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "Left join fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist as vfa " +
            "on vfa.videoFilm = vf.idVideo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoArtist as va " +
            "on va.idVideoArtist = vfa.videoArtist " +
            "where vf.dateModifFilm <= :dateModif and va.idVideoArtist =:idname " +
            "and vf.scoreOnHundred > :valueMin and vf.scoreOnHundred <= :valueMax " +
            "order by vf.dateModifFilm desc ")
    Page<VideoFilm> findVFwithNameResMinMaxByPage( Pageable p, Date dateModif, int valueMin,
                                                         int valueMax, String idname);

}
