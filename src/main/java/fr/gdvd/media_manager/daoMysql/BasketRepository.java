package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BasketRepository extends JpaRepository<Basket, Long> {

}
