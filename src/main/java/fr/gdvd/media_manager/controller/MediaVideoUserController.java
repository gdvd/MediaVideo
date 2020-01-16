package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.daoMysql.MyUserRepository;
import fr.gdvd.media_manager.daoMysql.PreferencesRepository;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.*;
import fr.gdvd.media_manager.service.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

//@Secured({"ROLE_ADMIN", "ROLE_USER"})
//@PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
//@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})

@Log4j2
@RestController
@RequestMapping(value = "videouser")
public class MediaVideoUserController {

    @Autowired
    private RequestWeb requestWeb;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ManagmentFilesImpl managmentFiles;
    @Autowired
    private MediaAdminServiceImpl mediaAdminService;
    @Autowired
    private MyUserRepository myUserRepository;

    @PostMapping(value = "/listMmiForLoginPP")
    public Page<MyMediaInfo> listMmiForLoginPP(@RequestParam int page,
                                               @RequestParam int size,
                                               @RequestParam boolean filename,
                                               @RequestParam(defaultValue = "") String toSort,
                                               @RequestParam(defaultValue = "") String filtertt,
                                               @RequestBody String filter,
                                               @RequestParam(defaultValue = "") String vneName){
        String login = request.getRemoteUser();
        log.info(login+" || listMmiForLoginPP with filter : "+filter + " page : "+page+" size : "
                +size+" vne : "+vneName+ " and toSort : "+toSort+" and filename:"+filename);
        Page<MyMediaInfo> pmmi = managmentFiles.listMmiForLoginPP(login, page, size, toSort, filter, vneName, filename, filtertt);
        return pmmi;
    }

    @PostMapping(value = "/listMmiForLoginWithNamePP")
    public Page<MyMediaInfo> listMmiForLoginWithNamePP(@RequestParam int page,
                                                       @RequestParam int size,
                                                       @RequestParam(defaultValue = "") String toSort,
                                                       @RequestBody String filter){
        String login = request.getRemoteUser();
        log.info(login+" || listMmiForLoginPP with filter : '"+filter + "' | page : "+page+" | size : "
                +size+ " and toSort : '"+toSort);
        Page<MyMediaInfo> pmmi = managmentFiles.listMmiForLoginWithNamePP(login, page, size, toSort, filter);
        return pmmi;
    }

    @GetMapping(value = "/lVneIdToName",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<VNELight> lVneIdToName(){
        String login = request.getRemoteUser();
        log.info(login+" || lVneIdToName");
        return managmentFiles.lVneIdToName(login);
    }

    @GetMapping(value = "/listUserWithId",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<UserLight> listUserWithId(){
        String login = request.getRemoteUser();
        log.info(login+" || listUserWithId");
        return managmentFiles.listUserWithId();
    }

    @GetMapping(value = "/getAllTypeName", produces =
            {MediaType.APPLICATION_JSON_VALUE})
    public List<String> getAllTypeName(){
        String login = request.getRemoteUser();
        log.info(login+" || getAllTypeName");
        return requestWeb.getAllTypeName();
    }

    @GetMapping(value = "/getAllTypeNameWithId", produces =
            {MediaType.APPLICATION_JSON_VALUE})
    public List<TypeName> getAllTypeNameWithId(){
        String login = request.getRemoteUser();
        log.info(login+" || getAllTypeNameWithId");
        return requestWeb.getAllTypeNameWithId();
    }

    @PostMapping(value = "/postscoreforuser")
    public VideoFilm postscoreforuser(@RequestBody ReqScore reqScore){
        String login = request.getRemoteUser();
        log.warn(login+" ===> Postscoreforuser idVideoFilm : "
                +reqScore.getIdtt()+" with note : "+reqScore.getScore());
        return mediaAdminService.addScoreToUser(reqScore, login);
    }
    @GetMapping(value = "/getbaskets")
    public List<Basket> getbaskets(){
        String login = request.getRemoteUser();
        log.info(login+" || getbaskets");
        return requestWeb.getbaskets(login);
    }
    @PostMapping(value = "/addtobasket/{idMmi}")
    public List<Basket> addtobasket(@PathVariable("idMmi")String idMmi, @RequestBody String nameBasket){
        String login = request.getRemoteUser();
        log.warn(login+" ===> Addtobasket idMmi : "+idMmi+" in : "+nameBasket);
        return requestWeb.addtobasket(idMmi, login, nameBasket);
    }
    @PostMapping(value = "/getfilenameofidsbasket",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public BasketInfo getfilenameofidsbasket(@RequestBody String nameBasket){
        String login = request.getRemoteUser();
        Long idUser = myUserRepository.findByLogin(login).getIdMyUser();
        List<BasketInfoElement> lbie = new ArrayList<>();
        BasketInfo bi = new BasketInfo(nameBasket, idUser, "", lbie);
        log.warn(login + " || getfilenameofidsbasket with namebasket : "+nameBasket);
        return requestWeb.getfilenameofidsbasket(bi);
    }
    @PostMapping(value = "/deletelocalbasketname",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public void deletelocalbasketname(@RequestBody String nameBasket){
        String login = request.getRemoteUser();
        Long idUser = myUserRepository.findByLogin(login).getIdMyUser();
        log.warn(login + " || deletelocalbasketname with namebasket : "+nameBasket);
        requestWeb.deletelocalbasketname(nameBasket, idUser);
    }
    @PostMapping(value = "/deleteOneId/{idMmi}", produces={MediaType.APPLICATION_JSON_VALUE})
    public void deleteOneId(@PathVariable("idMmi")String idMmi, @RequestBody String nameBasket){
        String login = request.getRemoteUser();
        Long idUser = myUserRepository.findByLogin(login).getIdMyUser();
        log.warn(login + " || deleteOneId with namebasket : "+nameBasket+ " and idmmi : "+idMmi);
        requestWeb.deleteOneId(nameBasket, idUser, idMmi);
    }
    @GetMapping(value = "/getLastScore", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<OneSimpleScore> getLastScore(){
        String login = request.getRemoteUser();
        log.info(login+" || getLastScore");
        return requestWeb.getLastScore();
    }

    @GetMapping(value = "/listuserstosub", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<String> listuserstosub(){
        String login = request.getRemoteUser();
        log.info(login+" || listuserstosub");
        return mediaAdminService.listuserstosub(login, true, true);
    }

    @PostMapping(value = "/subscribe", produces = {MediaType.APPLICATION_JSON_VALUE})
    public PreferencesSubscribeWithScore subscribe(@RequestBody PreferencesSubscribe preferencesSubscribe){
        String login = request.getRemoteUser();
        log.info(login+" || subscribe");
        return mediaAdminService.subscribe(login, preferencesSubscribe);
    }

    @GetMapping(value = "/getListOfSub", produces = {MediaType.APPLICATION_JSON_VALUE})
    public PreferencesSubscribeWithScore getListOfSub(){
        String login = request.getRemoteUser();
        log.info(login+" || getListOfSub");
        return mediaAdminService.getListOfSub(login);
    }

    @GetMapping(value = "/getallsubscribes", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PreferencesSubscribeWithScore> getallsubscribes(){
        String login = request.getRemoteUser();
        log.info(login+" || getallsubscribes");
        return mediaAdminService.getallsubscribes(login);
    }

    @GetMapping(value = "/validationsubscribe/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public PreferencesSubscribeWithScore validationsubscribe(@PathVariable("id")Long id){
        String login = request.getRemoteUser();
        log.info(login+" || validationsubscribe");
        return mediaAdminService.validationsubscribe(login, id);
    }


    @GetMapping(value = "/getOneSubscribe/{dateask}/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public PreferencesSubscribeWithScore getOneSubscribe(
            @PathVariable("dateask")int dateask,
            @PathVariable("id")Long id){
        String login = request.getRemoteUser();
        log.info(login+" || getOneSubscribe");
        return mediaAdminService.getOneSubscribe(login, dateask, id);
    }

    @PostMapping(value = "/postcommentforuser/{idMmi}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public MyMediaInfo postcommentforuser(@RequestBody String comment, @PathVariable String idMmi) {
        if (comment.equals("")) throw new RuntimeException("Request null");
        String login = request.getRemoteUser();
        log.info("Postcommentforuser : " + login + " to IdMmi : " + idMmi);
        return requestWeb.postcommentforuser(idMmi, comment);
    }

    @PostMapping(value = "/postcommentvideo/{idVideo}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public VideoFilm postcommentvideo(@RequestBody String comment, @PathVariable String idVideo) {
        if (comment.equals("")) throw new RuntimeException("Request null");
        String login = request.getRemoteUser();
        log.info("Postcommentvideo : " + login + " to idVideo : " + idVideo);
        return requestWeb.postcommentvideo(idVideo, comment);
    }

    @PostMapping(value = "/getTitleWithId")
    public TitileWithIdttt getTitleWithId(@NotNull @RequestBody TitileWithIdttt titileWithIdttt){
        log.info("===> GetTitleWithId titileWithIdttt : "+ titileWithIdttt.getIdtt());
        return requestWeb.getTitleWithId(titileWithIdttt);
    }

}
