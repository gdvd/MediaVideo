package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.VideoNameExport;
import fr.gdvd.media_manager.service.ExportService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Log4j2
@RestController
@RequestMapping(value = "export")
public class ExportController {

    @Autowired
    private ExportService exportService;
    @Autowired
    private HttpServletRequest request;
    // Example : request.getHeader("origin")        -> http://localhost:4200
    // Example : request.getHeader("referer")       -> http://localhost:4200/path
    // Example : request.getHeader("user-agent")    -> navigator used
    // Example : request.getRemoteUser()            -> admin
    // Example : request.getUserPrincipal().getName()-> admin
    // Example : request.getServletPath()           -> /path/infos
    // Example : request.getHeader("authorization") -> token ("Baerer xxx…")


    @GetMapping(value = "/desactivationExport/{id}")
    public void desactivationExport(@PathVariable Long id){
        log.info("DesactivationExport id : "+id);
        exportService.toggleActivationExport(id);
    }
    @GetMapping(value = "/activationExport/{id}")
    public void activationExport(@PathVariable Long id){
        log.info("ActivationExport id : "+id);
        exportService.toggleActivationExport(id);
    }
    @GetMapping(value = "/deleteExport/{id}")
    public void deleteExport(@PathVariable Long id){
        log.info("deleteExport id : "+id);
        exportService.deleteExport(id);
    }
    @GetMapping(value = "/getOneMmi/{idmmi}")
    public MyMediaInfo getOneMmi(@PathVariable String idmmi){
        return exportService.getOneMmi(idmmi);
    }
    @GetMapping(value = "/getListUsers",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public  List<String> getListUsers(){
        return exportService.getListUsers();
    }

}
