package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.BasketInfo;
import fr.gdvd.media_manager.entitiesNoDb.Langtopost;
import fr.gdvd.media_manager.entitiesNoDb.RequestImdb;

import java.io.File;
import java.util.List;

public interface RequestWeb {

    List<RequestImdb> getResultRequestWeb(String req, Long idTmmi);
    VideoFilm getOneVideoFilm(String link, String idMyMediaInfo);
    File downloaWebPage(String idtt, String filedestination);
    String linkToIdTt(String link);
    String encodeValue(String value);
    String fileToString(File f);
    List<String> getAllTypeName();
    List<TypeName> getAllTypeNameWithId();
    VideoFilm linkIdttWithIdmmi(String idmmi, String idtt);
    TypeMmi savetypemmi(TypeMmi typeMmi, String idMmi, String idVideo);
    TypeMmi gettypemmiwithidtt (String idtt);
    MyMediaAudio updatelanguage(Langtopost langtopost);
    MyMediaText updatetext(Langtopost langtopost);
    TypeMmi gettypemmiwithidmmi(String idtmmi);
    MyMediaInfo eraseLinkTmmiVideofilm(Long idTmmi, String idVideo, String idMmi);
    MyMediaInfo eraseLinkMmiTmmi(Long idTmmi, String idMmi);
    MyMediaInfo eraseTmmi(Long idTmmi, String idVideo, String idMmi);

    List<Basket> getbaskets(String login);

    List<Basket> addtobasket(String idMmi, String login, String nameBasket);

    BasketInfo getfilenameofidsbasket(BasketInfo bi);

    void deletelocalbasketname(String nameBasket, Long idUser);

    void deleteOneId(String nameBasket, Long idUser, String idMmi);
}
