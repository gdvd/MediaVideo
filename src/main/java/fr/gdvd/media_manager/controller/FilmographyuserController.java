package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesNoDb.OneActor;
import fr.gdvd.media_manager.entitiesNoDb.OneFilmpgraphy;
import fr.gdvd.media_manager.entitiesNoDb.VideoFilmLight;
import fr.gdvd.media_manager.service.FilmographyuserServiceImpl;
import fr.gdvd.media_manager.service.RequestWeb;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
@Log4j2
@RestController
@RequestMapping(value = "filmographyuser")
public class FilmographyuserController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FilmographyuserServiceImpl filmographyuserService;
    @Autowired
    private RequestWeb requestWeb;

    @PostMapping(value = "/searchname/{exactName}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<OneActor> searchname(@RequestBody String name, @PathVariable int exactName){
        String login = request.getRemoteUser();
        log.info(login+" ===> Filmography.searchname : " + name);
        return filmographyuserService.searchname(name, exactName==1);
    }
    @GetMapping(value = "/filmographywithidnm/{idnm}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public OneFilmpgraphy filmographywithidnm(@PathVariable String idnm){
        String login = request.getRemoteUser();
        log.info(login+" ===> Filmography.filmographywithidnm : " + idnm);
        return filmographyuserService.filmographywithidnm(idnm, login);
    }
    @GetMapping(value = "/getOneVideoFilm/{idTt}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public VideoFilm getOneVideoFilm(@PathVariable String idTt){
        log.info("getOneVideoFilm idTt : "+ idTt);
        return requestWeb.getOneVideoFilm(idTt, "");
    }

    @PostMapping(value = "/loadFilmo/{idnm}")
    public List<VideoFilmLight> loadFilmo(@RequestBody List<String> lfilmo, @PathVariable String idnm){
        String login = request.getRemoteUser();
        log.info(login+" ===> Filmography.loadFilmo : " + idnm + " for list : " + lfilmo.toString());
        return filmographyuserService.loadFilmo(lfilmo, idnm);
    }
}
