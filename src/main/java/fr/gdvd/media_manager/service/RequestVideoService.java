package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.TypeName;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoKeyword;
import fr.gdvd.media_manager.entitiesNoDb.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RequestVideoService {

    public Page<VideoFilm> getRequestVideo(RequestVideo req, String login);

    List<TypeName> getAllTypeName(String login);

    List<UserLight> getListForScores(String login);

    List<LinkIdvfMmi> getMmiWithIdVideo(OptionGetMmiWithIdVf lIdVideowithoption, String login, String idsVne);

    MyMediaInfo submitNewAudio(NewLanguage newLanguage, String remoteUser);

    MyMediaInfo submitNewSubtitle(NewLanguage newLanguage, String remoteUser);

    MyMediaInfo submitserienumber(SubmitSerie submitSerie, String remoteUser);

    List<KeywordAndVFSize> searchKeyWords(String keywordEncoded, int keywordIs, String login);

    List<KeywordAndVFSize> getKeywordsWithId(List<String> lidk, String remoteUser);

    List<MyMediaLanguageAndNbMmi> searchLanguage(String keywordEncoded, int keywordIs, String login);

    List<MyMediaLanguageAndNbMmi> getLanguagesWithId(List<String> lidl, String remoteUser);

    List<MyMediaInfo> saveTitleForCurrentVF(String titleSerieVO, String idVF, String remoteUser);

    VideoFilm saveyear(String idVF, Integer year, String remoteUser);

    VideoFilm saveScore(String idVF, Integer score, String remoteUser);

    VideoFilm saveurlImg(String idVF, String url, String remoteUser);

    VideoFilm newComment(String idVF, String comment, String remoteUser);

    VideoFilm newTitle(String idVF, String title, String remoteUser);

    VideoFilm newKeyword(String idVF, String keyword, String remoteUser);
}
