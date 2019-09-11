package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.EmbeddedKeyUserToNameExport;
import fr.gdvd.media_manager.entitiesMysql.UserToNameExport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryRestResource
public interface UserToNameExportRepository extends JpaRepository<UserToNameExport, EmbeddedKeyUserToNameExport> {

    UserToNameExport findAllByVideoNameExport_IdVideoNameExportAndMyUser_Login(Long idNameExport, String login);

    @Transactional
    @Modifying
    @Query("delete fr.gdvd.media_manager.entitiesMysql.UserToNameExport AS utne " +
            "WHERE utne.id_video_name_export = :id")
    void deleteWithIdvne(Long id);

}
