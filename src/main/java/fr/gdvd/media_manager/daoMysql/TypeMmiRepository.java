package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.TypeMmi;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface TypeMmiRepository extends JpaRepository<TypeMmi, Long> {

    List<TypeMmi> findByVideoFilm(VideoFilm videoFilm);

    @Query("select DISTINCT tm.videoFilm.idVideo, tm.idTypeMmi " +
            ", tm.videoFilm.scoreOnHundred, tm.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where " +
            "tm.typeName.idTypeName IN (:lt) and " +
            "tm.season >= :seasonMin and tm.season <= :seasonMax and " +
            "tm.episode >= :episodeMin and tm.episode <= :episodeMax and " +
            "tm.active=1 ")
    List<Tuple> getTypeMmiLightWithNameserieNull(
            List<Long> lt, int seasonMin, int seasonMax,
                int episodeMin, int episodeMax);

    @Query("select DISTINCT tm.videoFilm.idVideo " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where " +
            "tm.typeName.idTypeName IN (:lt) and " +
            "tm.season >= :seasonMin and tm.season <= :seasonMax and " +
            "tm.episode >= :episodeMin and tm.episode <= :episodeMax and " +
            "tm.active=1 ")
    List<String> getTypeMmiLightWithNameserieNullV2(
            List<Long> lt, int seasonMin, int seasonMax,
                int episodeMin, int episodeMax);

    @Query("select DISTINCT tm.videoFilm.idVideo, tm.idTypeMmi " +
            ", tm.videoFilm.scoreOnHundred, tm.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where " +
            "tm.typeName.idTypeName IN (:lt) and " +
            "tm.nameSerieVO LIKE :ns and " +
            "tm.season >= :seasonMin and tm.season <= :seasonMax and " +
            "tm.episode >= :episodeMin and tm.episode <= :episodeMax and " +
            "tm.active=1 AND " +
            "NOT (tm.nameSerieVO ='' and " +
            "tm.episode = 0 and tm.season = 0)")
    List<Tuple> getTypeMmiLight(List<Long> lt, String ns,
                                int seasonMin, int seasonMax,
                                int episodeMin, int episodeMax);

    @Query("select DISTINCT tm.videoFilm.idVideo " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where " +
            "tm.typeName.idTypeName IN (:lt) and " +
            "tm.nameSerieVO LIKE :ns and " +
            "tm.season >= :seasonMin and tm.season <= :seasonMax and " +
            "tm.episode >= :episodeMin and tm.episode <= :episodeMax and " +
            "tm.active=1 AND " +
            "NOT (tm.nameSerieVO ='' and " +
            "tm.episode = 0 and tm.season = 0)")
    List<String> getTypeMmiLightV2(List<Long> lt, String ns,
                                int seasonMin, int seasonMax,
                                int episodeMin, int episodeMax);

    @Query("select DISTINCT tm.videoFilm.idVideo, tm.idTypeMmi " +
            ", tm.videoFilm.scoreOnHundred, tm.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where " +
            "tm.nameSerieVO LIKE :ns and " +
            "tm.season >= :seasonMin and tm.season <= :seasonMax and " +
            "tm.episode >= :episodeMin and tm.episode <= :episodeMax and " +
            "tm.active=1 AND " +
            "NOT (tm.nameSerieVO ='' and " +
            "tm.episode = 0 and tm.season = 0)")
    List<Tuple> getTypeMmiLightAllType(String ns,
                                int seasonMin, int seasonMax,
                                int episodeMin, int episodeMax);

    @Query("select DISTINCT tm.videoFilm.idVideo " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where " +
            "tm.nameSerieVO LIKE :ns and " +
            "tm.season >= :seasonMin and tm.season <= :seasonMax and " +
            "tm.episode >= :episodeMin and tm.episode <= :episodeMax and " +
            "tm.active=1 AND " +
            "NOT (tm.nameSerieVO ='' and " +
            "tm.episode = 0 and tm.season = 0)")
    List<String> getTypeMmiLightAllTypeV2(String ns,
                                int seasonMin, int seasonMax,
                                int episodeMin, int episodeMax);

    @Query("select DISTINCT tm.videoFilm.idVideo, tm.idTypeMmi " +
            ", tm.videoFilm.scoreOnHundred, tm.videoFilm.year " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where " +
            "tm.season >= :seasonMin and tm.season <= :seasonMax and " +
            "tm.episode >= :episodeMin and tm.episode <= :episodeMax and " +
            "tm.active=1 ")
    List<Tuple> getTypeMmiLightAllTypeWithNameserieNull(
                                int seasonMin, int seasonMax,
                                int episodeMin, int episodeMax);

    @Query("select DISTINCT tm.videoFilm.idVideo " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where " +
            "tm.season >= :seasonMin and tm.season <= :seasonMax and " +
            "tm.episode >= :episodeMin and tm.episode <= :episodeMax and " +
            "tm.active=1 ")
    List<String> getTypeMmiLightAllTypeWithNameserieNullV2(
                                int seasonMin, int seasonMax,
                                int episodeMin, int episodeMax);

    @Query("select tm.videoFilm.idVideo " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where tm.idTypeMmi= :idTmmi ")
    Optional<String> getIdVideoFilmWithIdTmmi(Long idTmmi);

    @Query("select tm " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where tm.videoFilm.idVideo = :idVideo ")
    List<TypeMmi> findAllTmmiByIdVideoFilm(String idVideo);


    @Query("select tm.idTypeMmi " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where tm.videoFilm.idVideo = :idVideo ")
    List<Long> findIdsTmmiByIdVideoFilm(String idVideo);

    /*@Query("select tm.idTypeMmi " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where tm.videoFilm.idVideo = :idVideo AND tm.season >= :seasonMin " +
            "AND tm.season <= :seasonMax AND tm.episode >= :episodeMin AND " +
            "tm.episode <= :episodeMax")
    List<Long> findIdsTmmiByIdVideoFilmWithSeasonEpisodeNameserie(
            String idVideo, int seasonMin, int seasonMax, int episodeMin,
            int episodeMax);*/

    @Query("select tm.idTypeMmi " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where tm.videoFilm.idVideo = :idVideo AND tm.season >= :seasonMin " +
            "AND tm.season <= :seasonMax AND tm.episode >= :episodeMin AND " +
            "tm.episode <= :episodeMax AND tm.nameSerieVO LIKE :nameSerie ")
    List<Long> findIdsTmmiByIdVideoFilmWithSeasonEpisodeNameserie(
            String idVideo, int seasonMin, int seasonMax, int episodeMin,
            int episodeMax, String nameSerie);

    @Query("select tm.idTypeMmi " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where tm.videoFilm.idVideo = :idVideo AND tm.season >= :seasonMin " +
            "AND tm.season <= :seasonMax AND tm.episode >= :episodeMin AND " +
            "tm.episode <= :episodeMax ")
    List<Long> findIdsTmmiByIdVideoFilmWithSeasonEpisodeAndNameserieMaybeNull(
            String idVideo, int seasonMin, int seasonMax, int episodeMin,
            int episodeMax);

    TypeMmi findByIdTypeMmi(Long idTypeMmi);

    @Query("select DISTINCT tm.idTypeMmi, mmi.fileSize, mmi.width " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo as mmi " +
            "ON tm.idTypeMmi=mmi.idMyMediaInfo " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath AS vsp " +
            "ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport AS vne " +
            "ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "where vne.idVideoNameExport IN (:idsVNE) ")
    List<Tuple> findAllIdsWithIdsVNE(List<Long> idsVNE);

    @Query("select tm " +
            "from fr.gdvd.media_manager.entitiesMysql.TypeMmi as tm " +
            "where tm.videoFilm.idVideo = :idVideo")
    List<TypeMmi> getAllIdtmmiWithIdVF(String idVideo);
}
