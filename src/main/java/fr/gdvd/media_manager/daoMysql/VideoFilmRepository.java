package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VideoFilmRepository extends JpaRepository<VideoFilm, String> {

    @Query("SELECT case when count(vf)>0 THEN true ELSE false end from VideoFilm as vf where vf.idVideo = lower(:idtt)")
    boolean ifTtExist(@Param("idtt") String idtt);


//    @Query("SELECT vf.fk_type_mmi FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf WHERE vf.idVideo = :idtt")
//    @Query("SELECT vf.fkTypeMmi FROM fr.gdvd.media_manager.entitiesMysql.VideoFilm AS vf WHERE vf.idVideo = :idtt")


}