package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.MyUser;
import fr.gdvd.media_manager.entitiesMysql.Preferences;
import fr.gdvd.media_manager.entitiesMysql.PreferencesSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PreferencesSubscribeRepository extends JpaRepository<PreferencesSubscribe, Long> {

    List<PreferencesSubscribe> findByIdToSubAndMyUser(String idToSub, MyUser mu);
    List<PreferencesSubscribe> findAllByMyUser_Login(String login);
    Optional<PreferencesSubscribe> findByActiveAndMyUser_LoginAndId(
            boolean active, String login, Long id);
    Optional<PreferencesSubscribe> findByIdToSubAndMyUser_Login(String idToSub,String login);
    Optional<PreferencesSubscribe> findByMyUser_LoginAndIdToSubAndIdname(String login,
                                                                         String idToSub,
                                                                         String idname);
    Optional<PreferencesSubscribe> findByIdAndMyUser_Login(Long id,String login);
    Optional<PreferencesSubscribe> findByIdnameAndMyUser_Login(String idname,String login);
}
