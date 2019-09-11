package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.BasketName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface BasketNameRepository extends JpaRepository<BasketName, Long> {

    Optional<BasketName> findByBasketName(String basketName);

}
