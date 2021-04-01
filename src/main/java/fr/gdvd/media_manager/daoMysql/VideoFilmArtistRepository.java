package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyVideoFilmArtist;
import fr.gdvd.media_manager.entitiesMysql.VideoArtist;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface VideoFilmArtistRepository extends JpaRepository<VideoFilmArtist, EmbeddedKeyVideoFilmArtist> {

    Optional<VideoFilmArtist> findByVideoFilmAndVideoArtist(VideoFilm vf, VideoArtist va);
//    Optional<VideoFilmArtist> findByVideoFilm_IdVideoAndVideoArtist_IdVideoArtist(String idVideoFilm, String idVideoArtist);

    @Query("select distinct vfa.videoFilm.idVideo, vfa.videoFilm.scoreOnHundred, " +
            "vfa.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist as vfa " +
            "where vfa.videoArtist.firstLastName LIKE :requestName and " +
            " (vfa.actor = :isActor or vfa.director = :isDirector or " +
            "vfa.music = :isMusic or vfa.producer = :isProducer or " +
            "vfa.writer = :isWriter) ")
    List<Tuple> findNameContentRequest(String requestName, boolean isActor,
      boolean isDirector, boolean isMusic, boolean isProducer, boolean isWriter);

    @Query("select distinct vfa.videoFilm.idVideo, vfa.videoFilm.scoreOnHundred, " +
            "vfa.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist as vfa " +
            "where vfa.videoArtist.firstLastName LIKE :requestName and " +
            " vfa.actor = :isActor ")
    List<Tuple> findNameContentRequestWithActor(String requestName, boolean isActor);

    @Query("select distinct vfa.videoFilm.idVideo, vfa.videoFilm.scoreOnHundred, " +
            "vfa.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist as vfa " +
            "where vfa.videoArtist.firstLastName LIKE :requestName and " +
            "vfa.director = :isDirector")
    List<Tuple> findNameContentRequestWithDirector(
            String requestName, boolean isDirector);

    @Query("select distinct vfa.videoFilm.idVideo, vfa.videoFilm.scoreOnHundred, " +
            "vfa.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist as vfa " +
            "where vfa.videoArtist.firstLastName LIKE :requestName and " +
            "vfa.music = :isMusic")
    List<Tuple> findNameContentRequestWithMusic(
            String requestName, boolean isMusic);

    @Query("select distinct vfa.videoFilm.idVideo, vfa.videoFilm.scoreOnHundred, " +
            "vfa.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist as vfa " +
            "where vfa.videoArtist.firstLastName LIKE :requestName and " +
            "vfa.producer = :isProducer")
    List<Tuple> findNameContentRequestWithProducer(
            String requestName, boolean isProducer);

    @Query("select distinct vfa.videoFilm.idVideo, vfa.videoFilm.scoreOnHundred, " +
            "vfa.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist as vfa " +
            "where vfa.videoArtist.firstLastName LIKE :requestName and " +
            "vfa.writer = :isWriter")
    List<Tuple> findNameContentRequestWithWriter(
            String requestName, boolean isWriter);

    /*@Query("select distinct vfa.videoFilm.idVideo, vfa.videoFilm.scoreOnHundred, " +
            "vfa.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist as vfa " +
            "where vfa.videoArtist.firstLastName LIKE :requestName and " +
            "vfa.writer = :isWriter")
    List<Tuple> withNameContentRequestWithWriter(
            String requestNameID, boolean isWriter);*/
    }
