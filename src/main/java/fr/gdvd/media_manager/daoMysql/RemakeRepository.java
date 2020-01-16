package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.Remake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource
public interface RemakeRepository extends JpaRepository<Remake, Long> {

    @Modifying
    @Transactional
    void deleteByIdRemake(Long idrm2);
}
