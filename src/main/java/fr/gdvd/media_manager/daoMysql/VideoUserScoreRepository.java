package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyVideoUserScore;
import fr.gdvd.media_manager.entitiesMysql.MyUser;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoUserScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Tuple;
import java.util.List;

@RepositoryRestResource
public interface VideoUserScoreRepository extends JpaRepository<VideoUserScore, EmbeddedKeyVideoUserScore> {

    VideoUserScore findByVideoFilmAndMyUser(VideoFilm vf, MyUser mu);
    VideoUserScore findByVideoFilm_IdVideoAndMyUser_IdMyUser(String idVideo, Long idMyUser);

//Works but there's no comment
/*    @Query("select vf.idVideo, vus.noteOnHundred, vus.dateModifScoreUser " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "left join fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "on vus.videoFilm=vf.idVideo " +
            "left join fr.gdvd.media_manager.entitiesMysql.MyUser as mu " +
            "on mu.idMyUser=vus.myUser " +
            "where mu.login =:login")
    List<Tuple> getAllScoreByLogin(String login);*/

//Didn't works
    /*@Query("select vf.idVideo, vus.noteOnHundred, vus.dateModifScoreUser, csu.comment " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf " +
            "left join fr.gdvd.media_manager.entitiesMysql.VideoUserScore as vus " +
            "on vus.videoFilm=vf.idVideo " +
            "left join fr.gdvd.media_manager.entitiesMysql.MyUser as mu " +
            "on mu.idMyUser=vus.myUser " +
            "join fr.gdvd.media_manager.entitiesMysql.CommentScoreUser as csu " +
            "on csu.idCommentScoreUser=vus.commentScoreUser " +
            "where mu.login =:login")
    List<Tuple> getAllScoreByLogin(String login);*/

    List<VideoUserScore> findAllByMyUser(MyUser myUser);
}
