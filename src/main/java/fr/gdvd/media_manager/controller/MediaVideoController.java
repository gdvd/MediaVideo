package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.entitiesMysql.TypeMmi;
import fr.gdvd.media_manager.entitiesMysql.TypeName;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesNoDb.RequestImdb;
import fr.gdvd.media_manager.service.RequestWeb;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;


@Log4j2
@RestController
@RequestMapping(value = "video")
public class MediaVideoController {

    @Autowired
    private RequestWeb requestWeb;
    @Autowired
    private HttpServletRequest request;

    // Example : request.getHeader("origin")        -> http://localhost:4200
    // Example : request.getHeader("referer")       -> http://localhost:4200/path
    // Example : request.getHeader("user-agent")    -> navigator used
    // Example : request.getRemoteUser()            -> admin
    // Example : request.getUserPrincipal().getName()-> admin
    // Example : request.getServletPath()           -> /path/infos
    // Example : request.getHeader("authorization") -> token ("Baerer xxx…")

    @PostMapping(value="/requesttoimdb", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<RequestImdb> requesttoimdb(@RequestBody String req){
        if(req.equals("")) throw new RuntimeException("Request null");
        return requestWeb.getResultRequestWeb(req);
    }

    @GetMapping(value = "/getAllTypeName", produces =
            {MediaType.APPLICATION_JSON_VALUE})
    public List<String> getAllTypeName(){
        return requestWeb.getAllTypeName();
    }

    @GetMapping(value = "/getAllTypeNameWithId", produces =
            {MediaType.APPLICATION_JSON_VALUE})
    public List<TypeName> getAllTypeNameWithId(){
        return requestWeb.getAllTypeNameWithId();
    }

    @GetMapping(value = "/linkIdttWithIdmmi/{idmmi}/{idtt}", produces =
            {MediaType.APPLICATION_JSON_VALUE})
    public VideoFilm linkIdttWithIdmmi(@PathVariable("idmmi")String idmmi,
                                       @PathVariable("idtt")String idtt){
        return requestWeb.linkIdttWithIdmmi(idmmi, idtt);
    }
    @PostMapping(value = "/savetypemmi/{idMmi}")
    public TypeMmi savetypemmi(@RequestBody TypeMmi typeMmi,
                            @PathVariable("idMmi") String idMmi){
        return requestWeb.savetypemmi(typeMmi, idMmi);
    }
    @GetMapping(value = "/gettypemmiwithidtt/{idtt}", produces =
            {MediaType.APPLICATION_JSON_VALUE})
    public TypeMmi gettypemmiwithidtt(@PathVariable("idtt")String idtt){
        return requestWeb.gettypemmiwithidtt(idtt);
    }



}
