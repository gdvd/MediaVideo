package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    MyUser findByLogin(String login);
    List<MyUser> findAllByLoginNotNull();
    List<MyUser> findAllByActiveIsTrue();
    MyUser findByIdMyUser(Long id);
    MyUser findByApiKey(String apikey);
    void deleteByLogin(String id);

    @Query("SELECT usr.idMyUser, usr.login, usr.active, usr.nickName FROM MyUser AS usr")
    List<Tuple> lUserWithId();

    @Query("select login FROM MyUser AS usr where active=1")
    List<String> findAllLoginActive();

    @Query("select usr.login FROM MyUser AS usr where active=1 AND usr.idMyUser = :idUsr")
    Optional<String> findLoginByIdUser(Long idUsr);
}
