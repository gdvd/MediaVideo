package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.Basket;
import fr.gdvd.media_manager.entitiesMysql.BasketName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface BasketRepository extends JpaRepository<Basket, Long> {

    List<Basket> findAllByMyUser_Login(String login);
    List<Basket> findAllByMyUser_IdMyUserAndBasketName_BasketName(Long idMyUser, String basketName);
    List<Basket> findAllByMyUser_LoginAndBasketName_BasketName(String login, String basketName);

    Optional<Basket> findByBasketNameAndMyUser_IdMyUserAndMyMediaInfo_IdMyMediaInfo(BasketName bn,
                                                                                    Long idMyUser,
                                                                                    String idMyMdeiaInfo);
    @Transactional
    @Modifying
    void deleteByBasketNameAndMyUser_IdMyUserAndMyMediaInfo_IdMyMediaInfo(BasketName bn,
                                                                          Long idMyUser,
                                                                          String idMyMdeiaInfo);

    @Transactional
    @Modifying
    void deleteAllByBasketNameAndMyUser_IdMyUser(BasketName bn, Long idMyUser);
}
