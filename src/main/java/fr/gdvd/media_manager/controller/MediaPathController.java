package fr.gdvd.media_manager.controller;


import fr.gdvd.media_manager.entities.MediaPath;
import fr.gdvd.media_manager.service.MediaPathServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("path")
@Log4j2
@RestController
public class MediaPathController {

    @Autowired
    private MediaPathServiceImpl mediaPathServiceImpl;

    @GetMapping(value = "/getOnePath/{path}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaPath getOnePath(@PathVariable String path){
        return mediaPathServiceImpl.getOnePath(path);
    }

    @GetMapping(value = "/getAllPath",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<MediaPath> getAllPath(){
        return mediaPathServiceImpl.getAllPath();
    }
    /*@GetMapping(value="/allkeys")
    public List<String> getAllPath() {
        return nullmediaConfigService.getAllPath();
    }*/

    @PostMapping(value = "/beforetosave")
    public int beforetosave(@RequestBody MediaPath mediaPath){
        int result = 0;
        result++;
        log.info("******* receive MediaPath");
        mediaPath.setActive(false);
//        mediaPath.se
        return result;
    }


}
