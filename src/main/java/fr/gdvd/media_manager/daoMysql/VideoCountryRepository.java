package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface VideoCountryRepository extends JpaRepository<VideoCountry, Long> {

    Optional<VideoCountry> findByCountry(String country);

}
