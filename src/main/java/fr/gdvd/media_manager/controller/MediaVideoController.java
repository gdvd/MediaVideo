package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.Langtopost;
import fr.gdvd.media_manager.entitiesNoDb.RequestImdb;
import fr.gdvd.media_manager.entitiesNoDb.TitileWithIdttt;
import fr.gdvd.media_manager.service.RequestWeb;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
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

    // a verrif : pour avoir l'iP du client
    // oubien String ipAddress = request.getHeader("X-FORWARDED-FOR");
    // request.getRemoteHost()
    // HttpServletRequest request =
    // ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        String ip = request.getRemoteAddr();

    /*
    public class HttpReqRespUtils {
private static final String[] IP_HEADER_CANDIDATES = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"};

    public static String getClientIpAddressIfServletRequestExist() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return "0.0.0.0";}
        HttpServletRequest request = ((ServletRequestAttributes)    RequestContextHolder.getRequestAttributes()).getRequest();
        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                String ip = ipList.split(",")[0];
                return ip;}}
        return request.getRemoteAddr();}
    */


    @PostMapping(value="/requesttoimdb/{idTmmi}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<RequestImdb> requesttoimdb(@RequestBody String req, @PathVariable Long idTmmi){
        if(req.equals("")) throw new RuntimeException("Request null");
        log.info("RequestToImdb : "+req+" to IdTmmi : "+idTmmi);
        return requestWeb.getResultRequestWeb(req, idTmmi);
    }

    @GetMapping(value = "/linkIdttWithIdmmi/{idmmi}/{idvf}", produces =
            {MediaType.APPLICATION_JSON_VALUE})
    public VideoFilm linkIdttWithIdmmi(@PathVariable("idmmi")String idmmi,
                                       @PathVariable("idvf")String idvf){
        log.info("LinkIdttWithIdmmi : "+idvf+" to IdMmi : "+idmmi);
        return requestWeb.linkIdttWithIdmmi(idmmi, idvf);
    }

    @PostMapping(value = "/savetypemmi")
    public TypeMmi savetypemmi(@RequestBody TypeMmi typeMmi,
                               @RequestParam("idMmi") String idMmi,
                               @RequestParam("idVideo")@Nullable String idVideo){
        log.info("SaveTypeMmi id : "+typeMmi.getIdTypeMmi()+" to IdMmi : "+idMmi+ " and idVideoFilm : "+idVideo);
        return requestWeb.savetypemmi(typeMmi, idMmi, idVideo);
    }

    @GetMapping(value = "/eraseLinkTmmiVideofilm/{idTmmi}/{idVideo}/{idMmi}")
    public MyMediaInfo eraseLinkTmmiVideofilm(@PathVariable("idTmmi") Long idTmmi,
                                              @PathVariable("idVideo") String idVideo,
                                              @PathVariable("idMmi") String idMmi){
        log.warn("===> EraseLinkTmmiVideofilm idTmmi : "+idTmmi +" with IdMmi : "+idMmi+ " and idVideoFilm : "+idVideo);
        return requestWeb.eraseLinkTmmiVideofilm(idTmmi, idVideo, idMmi);
    }

    @GetMapping(value = "/eraseTmmi/{idTmmi}/{idVideo}/{idMmi}")
    public MyMediaInfo eraseTmmi(@PathVariable("idTmmi") Long idTmmi,
                                 @PathVariable("idVideo") String idVideo,
                                 @PathVariable("idMmi") String idMmi){
        log.warn("===> EraseTmmi idTmmi : "+idTmmi +" with IdMmi : "+idMmi+ " and idVideoFilm : "+idVideo);
        return requestWeb.eraseTmmi(idTmmi, idVideo, idMmi);
    }

    @GetMapping(value = "/eraseLinkMmiTmmi/{idTmmi}/{idMmi}")
    public MyMediaInfo eraseLinkMmiTmmi(@PathVariable("idTmmi") Long idTmmi,
                                        @PathVariable("idMmi") String idMmi){
        log.warn("===> EraseLinkMmiTmmi idTmmi : "+idTmmi +" with IdMmi : "+idMmi);
        return requestWeb.eraseLinkMmiTmmi(idTmmi, idMmi);
    }

    @GetMapping(value = "/gettypemmiwithidtt/{idtt}", produces =
            {MediaType.APPLICATION_JSON_VALUE})
    public TypeMmi gettypemmiwithidtt(@PathVariable("idtt")String idtt){
        log.info("===> Gettypemmiwithidtt : "+idtt);
        return requestWeb.gettypemmiwithidtt(idtt);
    }

    @GetMapping(value = "/gettypemmiwithidmmi/{idmmi}", produces =
            {MediaType.APPLICATION_JSON_VALUE})
    public TypeMmi gettypemmiwithidmmi(@PathVariable("idmmi")String idtmmi){
        log.info("===> Gettypemmiwithidmmi : "+idtmmi);
        return requestWeb.gettypemmiwithidmmi(idtmmi);
    }

    @PostMapping(value = "/updatelanguage")
    public MyMediaAudio updatelanguage(@NotNull @RequestBody Langtopost langtopost){
        log.info("===> Updatelanguage idmmi : "+langtopost.getIdMd5()
                +" OldLang : "+langtopost.getOldLang()
                +" NewLlang : "+langtopost.getNewLlang());
        return requestWeb.updatelanguage(langtopost);
    }

    @PostMapping(value = "/updatetext")
    public MyMediaText updatetext(@NotNull @RequestBody Langtopost langtopost){
        log.info("===> Updatetext idmmi : "+langtopost.getIdMd5()
                +" OldLang : "+langtopost.getOldLang()
                +" NewLlang : "+langtopost.getNewLlang());
        return requestWeb.updatetext(langtopost);
    }

    @GetMapping(value = "/setremake/{idvf}/{idremake}", produces =
            {MediaType.APPLICATION_JSON_VALUE})
    public Remake setremake(@PathVariable("idvf")String idvf, @PathVariable("idremake")String idremake){
        log.info("===> Setremake on : " + idvf+" with : " + idremake);
        return requestWeb.setremake(idvf, idremake);
    }

}
