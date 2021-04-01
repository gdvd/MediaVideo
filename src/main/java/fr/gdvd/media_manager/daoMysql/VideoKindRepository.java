package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoKeyword;
import fr.gdvd.media_manager.entitiesMysql.VideoKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface VideoKindRepository extends JpaRepository<VideoKind, Long> {

    Optional<VideoKind> findByKindEn(String keywordEn);

    @Query("select vk.idKind " +
            "from fr.gdvd.media_manager.entitiesMysql.VideoKind as vk ")
    List<Long> findAllIdVideoKind();

}
