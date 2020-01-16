package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.DateSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DateSubscribeRepository extends JpaRepository<DateSubscribe, Long> {

    List<DateSubscribe> findAllByPreferencesSubscribe_Id(Long id);

    @Query("select ds from fr.gdvd.media_manager.entitiesMysql.DateSubscribe as ds " +
            "left join fr.gdvd.media_manager.entitiesMysql.PreferencesSubscribe as ps " +
            "on ds.id = ps.id where ps.id=:id order by ds.dateModif ")
    List<DateSubscribe> findListOfDateSubscribeWithIdPreferencesub(Long id);


    @Transactional
    @Modifying
    void deleteById(Long id);
}
