package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.TypeName;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoKeyword;
import fr.gdvd.media_manager.entitiesNoDb.*;
import fr.gdvd.media_manager.service.RequestVideoServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RestController
@RequestMapping(value = "videoid")
public class MediaVideoIdController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RequestVideoServiceImpl requestVideoService;

    @PostMapping(value="/newRequest",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<VideoFilm> requesttoimdb(
            @RequestBody RequestVideo req ){
        String login = request.getRemoteUser();
        return requestVideoService.getRequestVideo(
                req, login);
    }

    @PostMapping(value="/getMmiWithIdVideo",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<LinkIdvfMmi> getMmiWithIdVideo(
           @NonNull @RequestBody OptionGetMmiWithIdVf lIdVideowithoption ){
        String login = request.getRemoteUser();
        return requestVideoService.getMmiWithIdVideo(lIdVideowithoption, login, "");
    }


    @GetMapping(value = "/getAllTypeName",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<TypeName> getAllTypeName(){
        String login = request.getRemoteUser();
        return requestVideoService.getAllTypeName(login);
    }

    @GetMapping(value = "/getListForScores",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserLight> getListForScores(){
        String login = request.getRemoteUser();
        return requestVideoService.getListForScores(login);
    }

    @PostMapping(value="/submitNewAudio",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public MyMediaInfo submitNewAudio(
            @NonNull @RequestBody NewLanguage newLanguage ){
        return requestVideoService.submitNewAudio(newLanguage, request.getRemoteUser());
    }

    @PostMapping(value="/submitNewSubtitle",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public MyMediaInfo submitNewSubtitle(
            @NonNull @RequestBody NewLanguage newLanguage ){
        return requestVideoService.submitNewSubtitle(newLanguage, request.getRemoteUser());
    }

    @PostMapping(value="/submitserienumber",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public MyMediaInfo submitserienumber(
            @NonNull @RequestBody SubmitSerie submitSerie ){
        return requestVideoService.submitserienumber(submitSerie, request.getRemoteUser());
    }


    @GetMapping(value = "/searchKeyWords/{keywordEncoded}/{keywordIs}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<KeywordAndVFSize> searchKeyWords(@NotNull @PathVariable String keywordEncoded,
                                             @NotNull @PathVariable int keywordIs){
        String login = request.getRemoteUser();
        return requestVideoService.searchKeyWords(keywordEncoded, keywordIs, login);
    }


    @GetMapping(value = "/searchLanguage/{keywordEncoded}/{keywordIs}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<MyMediaLanguageAndNbMmi> searchLanguage(@NotNull @PathVariable String keywordEncoded,
                                             @NotNull @PathVariable int keywordIs){
        String login = request.getRemoteUser();
        return requestVideoService.searchLanguage(keywordEncoded, keywordIs, login);
    }

    @PostMapping(value="/getKeywordsWithId",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<KeywordAndVFSize> getKeywordsWithId(
            @NonNull @RequestBody List<String> lidk ){
        return requestVideoService.getKeywordsWithId(lidk, request.getRemoteUser());
    }

    @PostMapping(value="/getLanguagesWithId",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<MyMediaLanguageAndNbMmi> getLanguagesWithId(
            @NonNull @RequestBody List<String> lidl ){
        return requestVideoService.getLanguagesWithId(lidl, request.getRemoteUser());
    }

    @PostMapping(value="/saveTitleForCurrentVF/{idVF}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<MyMediaInfo> saveTitleForCurrentVF(
            @NonNull @RequestBody String titleSerieVO, @NotNull @PathVariable String idVF ){
        return requestVideoService.saveTitleForCurrentVF(titleSerieVO
                , idVF, request.getRemoteUser());
    }

    @GetMapping(value="/saveyear/{idVF}/{year}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public VideoFilm saveyear(@NotNull @PathVariable String idVF ,
                                      @NotNull @PathVariable Integer year){
        return requestVideoService.saveyear(idVF
                , year, request.getRemoteUser());
    }

    @GetMapping(value="/saveScore/{idVF}/{score}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public VideoFilm saveScore(@NotNull @PathVariable String idVF ,
                                      @NotNull @PathVariable Integer score){
        return requestVideoService.saveScore(idVF
                , score, request.getRemoteUser());
    }

    @PostMapping(value="/saveurlImg/{idVF}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public VideoFilm saveurlImg(@NotNull @PathVariable String idVF ,
                                      @NotNull @RequestBody String url){
        return requestVideoService.saveurlImg(idVF
                , url, request.getRemoteUser());
    }
    @PostMapping(value="/newComment/{idVF}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public VideoFilm newComment(@NotNull @PathVariable String idVF ,
                                      @NotNull @RequestBody String comment){
        return requestVideoService.newComment(idVF
                , comment, request.getRemoteUser());
    }

    @PostMapping(value="/newTitle/{idVF}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public VideoFilm newTitle(@NotNull @PathVariable String idVF ,
                                      @NotNull @RequestBody String title){
        return requestVideoService.newTitle(idVF
                , title, request.getRemoteUser());
    }

    @PostMapping(value="/newKeyword/{idVF}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public VideoFilm newKeyword(@NotNull @PathVariable String idVF ,
                                      @NotNull @RequestBody String keyword){
        return requestVideoService.newKeyword(idVF
                , keyword, request.getRemoteUser());
    }

}
