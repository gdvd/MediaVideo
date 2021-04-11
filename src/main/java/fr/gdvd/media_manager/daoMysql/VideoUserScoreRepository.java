package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyVideoUserScore;
import fr.gdvd.media_manager.entitiesMysql.MyUser;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoUserScore;
import fr.gdvd.media_manager.entitiesNoDb.UserScoreLight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Tuple;
import java.util.List;

@RepositoryRestResource
public interface VideoUserScoreRepository extends JpaRepository<VideoUserScore, EmbeddedKeyVideoUserScore> {

    VideoUserScore findByVideoFilmAndMyUser(VideoFilm vf, MyUser mu);
    List<VideoUserScore> findAllByMyUser(MyUser myUser);
    VideoUserScore findByVideoFilm_IdVideoAndMyUser_IdMyUser(String idVideo, Long idMyUser);

    @Query("select vus.videoFilm.idVideo, vus.noteOnHundred, vus.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "where vus.myUser.idMyUser =:idUser and " +
            "vus.noteOnHundred >= :scoreMin and " +
            "vus.noteOnHundred <= :scoreMax " +
            "order by :sortBy ASC")
    List<Tuple> getAllIdVideoWithScoreAndLoginWithoutNullableAsc(
            Long idUser, int scoreMin, int scoreMax, String sortBy);

    @Query("select vus.videoFilm.idVideo " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "where vus.myUser.idMyUser =:idUser and " +
            "vus.noteOnHundred >= :scoreMin and " +
            "vus.noteOnHundred <= :scoreMax " +
            "order by :sortBy ASC")
    List<String> getAllIdVideoWithScoreAndLoginWithoutNullableAscV2(
            Long idUser, int scoreMin, int scoreMax, String sortBy);

    @Query("select vus.videoFilm.idVideo, vus.noteOnHundred, vus.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "where vus.myUser.idMyUser =:idUser and " +
            "vus.noteOnHundred >= :scoreMin and " +
            "vus.noteOnHundred <= :scoreMax " +
            "order by :sortBy DESC")
    List<Tuple> getAllIdVideoWithScoreAndLoginWithoutNullableDesc(
            Long idUser, int scoreMin, int scoreMax, String sortBy);

    @Query("select vus.videoFilm.idVideo " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "where vus.myUser.idMyUser =:idUser and " +
            "vus.noteOnHundred >= :scoreMin and " +
            "vus.noteOnHundred <= :scoreMax " +
            "order by :sortBy DESC")
    List<String> getAllIdVideoWithScoreAndLoginWithoutNullableDescV2(
            Long idUser, int scoreMin, int scoreMax, String sortBy);

    @Query("select vus.videoFilm.idVideo " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "where vus.myUser.idMyUser =:idUser and " +
            "vus.noteOnHundred >= :scoreMin and " +
            "vus.noteOnHundred <= :scoreMax " +
            "order by :sortBy DESC")
    List<String> getAllIdVideoWithScoreAndLoginWithoutNullableDescTEST(
            Long idUser, int scoreMin, int scoreMax, String sortBy);

    @Query("select count(vus) " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "where vus.myUser.idMyUser = :idUser And " +
            "vus.videoFilm.idVideo = :idVideo")
    int findVusNumberWithIdvideoAndIdLogin(
            Long idUser, String idVideo);

    @Query("select vus " +
            "from VideoUserScore as vus " +
            "where vus.myUser.idMyUser =:usr")
    List<VideoUserScore> getLastScore(Long usr, Pageable pageable);

    @Query("select vus " +
            "from VideoUserScore as vus " +
            "where vus.myUser.idMyUser =:usr " +
            "order by vus.dateModifScoreUser DESC")
    List<VideoUserScore> getLastScore00(Long usr, Pageable pageable);

    @Query("select vus.videoFilm.idVideo, vus.noteOnHundred, " +
            "vus.commentScoreUser.comment, vus.dateModifScoreUser " +
            "from VideoUserScore as vus " +
            "where vus.myUser.idMyUser =:usr " +
            "order by vus.dateModifScoreUser DESC")
    List<Tuple> getLastScore0(Long usr, Pageable pageable);

    @Query("select vus.videoFilm.idVideo, vus.noteOnHundred, " +
            "vus.commentScoreUser.comment, vus.dateModifScoreUser " +
            "from VideoUserScore as vus " +
            "where vus.myUser.idMyUser =:usr")
    List<Tuple> getLastScore1(Long usr, Pageable pageable);

    @Query("select vus.videoFilm.idVideo " +
            "from VideoUserScore as vus " +
            "where vus.myUser.idMyUser =:usr")
    List<Tuple> getLastScore2(Long usr);

    @Query("select vus.videoFilm.idVideo, vus.noteOnHundred, " +
            "vus.commentScoreUser.comment, vus.dateModifScoreUser " +
            "from VideoUserScore as vus " +
            "where vus.myUser.idMyUser =21 " +
            "order by vus.dateModifScoreUser DESC")
    List<Tuple> getLastScore3(Long usr, Pageable pageable);

    @Query("select vus.videoFilm.idVideo, vus.noteOnHundred, " +
            "vus.commentScoreUser.comment, vus.dateModifScoreUser " +
            "from VideoUserScore as vus " +
            "where vus.myUser.idMyUser =21 ")
    List<Tuple> getLastScore4(Long usr, Pageable pageable);

    @Query("select vus " +
            "from VideoUserScore as vus " +
            "where vus.myUser.idMyUser =:usr " +
            "order by vus.dateModifScoreUser DESC")
    List<UserScoreLight> getLastScore5(Long usr, Pageable pageable);

/*
//Works but there's no comment
    @Query("select vf.idVideo, vus.noteOnHundred, vus.dateModifScoreUser " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "left join fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "on vus.videoFilm=vf.idVideo " +
            "left join fr.gdvd.media_manager.entitiesMysql.MyUser as mu " +
            "on mu.idMyUser=vus.myUser " +
            "where mu.login =:login")
    List<Tuple> getAllScoreByLogin(String login);
*/

}
