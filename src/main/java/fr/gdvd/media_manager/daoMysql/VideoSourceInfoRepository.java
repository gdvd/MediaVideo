package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoSourceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface VideoSourceInfoRepository extends JpaRepository<VideoSourceInfo, String> {

    Optional<VideoSourceInfo> findByName(String name);
    Optional<VideoSourceInfo> findBySourceUrl(String sourceUrl);

}
