package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.MyMediaLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Tuple;
import java.util.List;

@RepositoryRestResource
public interface MyMediaLanguageRepository extends JpaRepository<MyMediaLanguage, Long> {

    MyMediaLanguage findByLanguage(String language);

    @Query("select distinct ml.idMyMediaLanguage, ml.language, count(mmi) " +
            "from MyMediaLanguage as ml " +
            "left join MyMediaAudio as ma " +
            "on ml.idMyMediaLanguage=ma.myMediaLanguage " +
            "left join MyMediaInfo as mmi " +
            "on ma.myMediaInfo=mmi.idMyMediaInfo " +
            "where ml.language like :language " +
            "group by ml.language")
    List<Tuple> findMyLanguageByLanguage(String language);

    @Query("select distinct ml.idMyMediaLanguage, ml.language, count(mmi) " +
            "from MyMediaLanguage as ml " +
            "left join MyMediaAudio as ma " +
            "on ml.idMyMediaLanguage=ma.myMediaLanguage " +
            "left join MyMediaInfo as mmi " +
            "on ma.myMediaInfo=mmi.idMyMediaInfo " +
            "where ml.idMyMediaLanguage in (:idLanguage) " +
            "group by ml.idMyMediaLanguage")
    List<Tuple> findMyLanguageByIds(List<Long> idLanguage);




}
