package fr.gdvd.media_manager.controller;


import fr.gdvd.media_manager.service.MediaPathServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("path")
@Log4j2
@RestController
public class MediaPathController /*extends BaseController*/ {

    @Autowired
    private MediaPathServiceImpl mediaPathServiceImpl;
    @Autowired
    private HttpServletRequest request;

//    @GetMapping(value = "/getOnePath/{path}",
           /* produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaPath getOnePath(@PathVariable String path){
        return mediaPathServiceImpl.getOnePath(path);
    }*/

   /*@GetMapping(value = "/getAllPath",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<MediaPath> getAllPath(){
        return mediaPathServiceImpl.getAllPath();
    }*/

//    @GetMapping(value = "/getAllPathActive",
            /*produces={MediaType.APPLICATION_JSON_VALUE})
    public List<MediaPath> getAllPathActive(){
        return mediaPathServiceImpl.getAllPathActive();
    }*/

    /*@GetMapping(value = "/desactivation/{id}")
    public void idDesactivation(@PathVariable String id){
        mediaPathServiceImpl.idDesactivation(id);
    }

    @GetMapping(value = "/activation/{id}")
    public void idActivation(@PathVariable String id){
        mediaPathServiceImpl.idActivation(id);
    }

//    @PostMapping(value = "/beforetosave/{i}")
    *//*public int beforetosave(@PathVariable int i,
                            @RequestBody MediaPath mediaPath){
        return mediaPathServiceImpl.beforetosave(i, mediaPath, request.getRemoteUser());
    }*//*

    @PutMapping(value = "/updatePath/{id}")
    public void updatePath(@PathVariable String id, @RequestBody int state){
        mediaPathServiceImpl.updatePath(id, state);
    }*/

}
