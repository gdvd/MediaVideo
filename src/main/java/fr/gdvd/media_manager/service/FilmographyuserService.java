package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entitiesNoDb.OneActor;
import fr.gdvd.media_manager.entitiesNoDb.OneFilmpgraphy;
import fr.gdvd.media_manager.entitiesNoDb.VideoFilmLight;

import java.util.List;

public interface FilmographyuserService {

    public List<OneActor> searchname(String name, boolean exactName);

    OneFilmpgraphy filmographywithidnm(String idnm, String login);

    List<VideoFilmLight> loadFilmo(List<String> lfilmo, String idNm);
}
