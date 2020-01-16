package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoComment;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VideoCommentRepository extends JpaRepository<VideoComment, Long> {

    VideoComment findByVideoFilm(VideoFilm vf);

}
