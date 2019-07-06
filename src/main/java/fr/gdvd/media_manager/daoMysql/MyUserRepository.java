package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    MyUser findByLogin(String login);
    List<MyUser> findAllByLoginNotNull();
//    List<MyUser> findByActive();
    List<MyUser> findAllByActiveIsTrue();
//    MyUser findByIdUser(Long id);
    MyUser findByIdMyUser(Long id);
    void deleteByLogin(String id);

}
