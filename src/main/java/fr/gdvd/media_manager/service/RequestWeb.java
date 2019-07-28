package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entitiesMysql.TypeMmi;
import fr.gdvd.media_manager.entitiesMysql.TypeName;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesNoDb.RequestImdb;

import java.io.File;
import java.util.List;

public interface RequestWeb {

    List<RequestImdb> getResultRequestWeb(String req);
    VideoFilm getOneVideoFilm(String link);
    File downloaWebPage(String idtt);
    String linkToIdTt(String link);
    String encodeValue(String value);
    String fileToString(File f);
    List<String> getAllTypeName();
    List<TypeName> getAllTypeNameWithId();
    VideoFilm linkIdttWithIdmmi(String idmmi, String idtt);
    TypeMmi savetypemmi(TypeMmi typeMmi, String idMmi);

    TypeMmi gettypemmiwithidtt (String idtt);
}
