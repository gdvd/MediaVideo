package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoArtist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VideoArtistRepository extends JpaRepository<VideoArtist, String> {


    @Query("select va from fr.gdvd.media_manager.entitiesMysql.VideoArtist as va " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist "+
            "AS vfa ON va.idVideoArtist=vfa.videoArtist "+
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm "+
            "AS vf ON vfa.videoArtist=vf.idVideo "+
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi "+
            "AS tmmi ON vf.idVideo=tmmi.videoFilm " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo " +
            "AS mmi ON mmi.idMyMediaInfo=tmmi.idTypeMmi " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoSupportPath " +
            "AS vsp ON mmi.idMyMediaInfo=vsp.id_my_media_info " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoNameExport " +
            "AS vne ON vsp.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.UserToNameExport " +
            "AS utne ON utne.id_video_name_export=vne.idVideoNameExport " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyUser " +
            "AS mu ON mu.idMyUser=utne.myUser " +
            "where mu.login= :login AND va.idVideoArtist LIKE :nm")
    Page<VideoArtist> findMnWithNmPP(String login, String nm, Pageable pageable);

    @Query("select va from fr.gdvd.media_manager.entitiesMysql.VideoArtist as va " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilmArtist "+
            "AS vfa ON va.idVideoArtist=vfa.videoArtist "+
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.VideoFilm "+
            "AS vf ON vfa.videoArtist=vf.idVideo "+
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.TypeMmi "+
            "AS tmmi ON vf.idVideo=tmmi.videoFilm " +
            "LEFT JOIN fr.gdvd.media_manager.entitiesMysql.MyMediaInfo " +
            "AS mmi ON mmi.idMyMediaInfo=tmmi.idTypeMmi " +
            "where va.firstLastName LIKE :nm")
    Page<VideoArtist> findMnPP(String nm, Pageable pageable);

}
