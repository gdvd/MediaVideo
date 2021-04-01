package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.Basket;
import fr.gdvd.media_manager.entitiesMysql.BasketName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
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

    @Query("select b.basketName.basketName, mmi.idMyMediaInfo, " +
            "mmi.fileSize, vne.nameExport, vsp.pathGeneral, " +
            "vsp.title " +
            "from Basket as b " +
            "left join MyMediaInfo as mmi " +
            "on mmi.idMyMediaInfo=b.myMediaInfo " +
            "left join VideoSupportPath as vsp " +
            "on vsp.id_my_media_info=mmi.idMyMediaInfo " +
            "left join VideoNameExport as vne " +
            "on vne.idVideoNameExport=vsp.id_video_name_export " +
            "where b.myUser.login =:login and vsp.active=1 " +
            "and vne.active=1 and vne.complete=1 ")
    List<Tuple> getBasketsAndMmiAndVne(String login);

}
