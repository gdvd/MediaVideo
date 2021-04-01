package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface VideoKeywordRepository extends JpaRepository<VideoKeyword, Long> {

    Optional<VideoKeyword> findByKeywordEn(String keywordEn);
    Optional<VideoKeyword> findByKeywordFr(String keywordFr);

    @Query("select distinct vk.keywordEn, vk.idKeyword, vk.videoFilms.size " +
            "from VideoKeyword as vk " +
            "INNER JOIN vk.videoFilms " +
            "where vk.keywordEn like :keywordEn")
    List<Tuple> findAllByKeywordEn(String keywordEn);

    @Query("select distinct vk.keywordEn, vk.idKeyword, vk.videoFilms.size " +
            "from VideoKeyword as vk " +
            "INNER JOIN vk.videoFilms " +
            "where vk.idKeyword in (:lid)")
    List<Tuple> findAllByIdkeyword(List<Long> lid);

    Optional<VideoKeyword> findByKeywordEnAndVideoFilms(String keyword,
                                                        List<VideoFilm> videoFilms);


}
