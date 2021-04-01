package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.Remake;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RepositoryRestResource
public interface RemakeRepository extends JpaRepository<Remake, Long> {

    Optional<Remake> findFirstByVideoFilms(VideoFilm vf);

    @Modifying
    @Transactional
    void deleteByIdRemake(Long idrm2);
}
